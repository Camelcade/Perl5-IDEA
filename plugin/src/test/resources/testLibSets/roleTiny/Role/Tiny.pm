package Role::Tiny;
use strict;
use warnings;

our $VERSION = '2.002004';
$VERSION =~ tr/_//d;

our %INFO;
our %APPLIED_TO;
our %COMPOSED;
our %COMPOSITE_INFO;
our @ON_ROLE_CREATE;

# Module state workaround totally stolen from Zefram's Module::Runtime.

BEGIN {
  *_WORK_AROUND_BROKEN_MODULE_STATE = "$]" < 5.009 ? sub(){1} : sub(){0};
  *_WORK_AROUND_HINT_LEAKAGE
    = "$]" < 5.011 && !("$]" >= 5.009004 && "$]" < 5.010001)
      ? sub(){1} : sub(){0};
  *_CONSTANTS_DEFLATE = "$]" >= 5.012 && "$]" < 5.020 ? sub(){1} : sub(){0};
}

sub _getglob { no strict 'refs'; \*{$_[0]} }
sub _getstash { no strict 'refs'; \%{"$_[0]::"} }

sub croak {
  require Carp;
  no warnings 'redefine';
  *croak = \&Carp::croak;
  goto &Carp::croak;
}

sub Role::Tiny::__GUARD__::DESTROY {
  delete $INC{$_[0]->[0]} if @{$_[0]};
}

sub _load_module {
  my ($module) = @_;
  (my $file = "$module.pm") =~ s{::}{/}g;
  return 1
    if $INC{$file};

  # can't just ->can('can') because a sub-package Foo::Bar::Baz
  # creates a 'Baz::' key in Foo::Bar's symbol table
  return 1
    if grep !/::\z/, keys %{_getstash($module)};
  my $guard = _WORK_AROUND_BROKEN_MODULE_STATE
    && bless([ $file ], 'Role::Tiny::__GUARD__');
  local %^H if _WORK_AROUND_HINT_LEAKAGE;
  require $file;
  pop @$guard if _WORK_AROUND_BROKEN_MODULE_STATE;
  return 1;
}

sub _require_module {
  _load_module($_[1]);
}

sub _all_subs {
  my ($me, $package) = @_;
  my $stash = _getstash($package);
  return {
    map {;
      no strict 'refs';
      # this is an ugly hack to populate the scalar slot of any globs, to
      # prevent perl from converting constants back into scalar refs in the
      # stash when they are used (perl 5.12 - 5.18). scalar slots on their own
      # aren't detectable through pure perl, so this seems like an acceptable
      # compromise.
      ${"${package}::${_}"} = ${"${package}::${_}"}
        if _CONSTANTS_DEFLATE;
      $_ => \&{"${package}::${_}"}
    }
    grep exists &{"${package}::${_}"},
    grep !/::\z/,
    keys %$stash
  };
}

sub import {
  my $target = caller;
  my $me = shift;
  strict->import;
  warnings->import;
  my $non_methods = $me->_non_methods($target);
  $me->_install_subs($target, @_);
  $me->make_role($target);
  $me->_mark_new_non_methods($target, $non_methods)
    if $non_methods && %$non_methods;
  return;
}

sub _mark_new_non_methods {
  my ($me, $target, $old_non_methods) = @_;

  my $non_methods = $INFO{$target}{non_methods};

  my $subs = $me->_all_subs($target);
  for my $sub (keys %$subs) {
    if ( exists $old_non_methods->{$sub} && $non_methods->{$sub} != $subs->{$sub} ) {
      $non_methods->{$sub} = $subs->{$sub};
    }
  }

  return;
}

sub make_role {
  my ($me, $target) = @_;

  return if $me->is_role($target);
  $INFO{$target}{is_role} = 1;

  my $non_methods = $me->_all_subs($target);
  delete @{$non_methods}{grep /\A\(/, keys %$non_methods};
  $INFO{$target}{non_methods} = $non_methods;

  # a role does itself
  $APPLIED_TO{$target} = { $target => undef };
  foreach my $hook (@ON_ROLE_CREATE) {
    $hook->($target);
  }
}

sub _install_subs {
  my ($me, $target) = @_;
  return if $me->is_role($target);
  my %install = $me->_gen_subs($target);
  *{_getglob("${target}::${_}")} = $install{$_}
    for sort keys %install;
  return;
}

sub _gen_subs {
  my ($me, $target) = @_;
  (
    (map {;
      my $type = $_;
      $type => sub {
        my $code = pop;
        my @names = ref $_[0] eq 'ARRAY' ? @{ $_[0] } : @_;
        push @{$INFO{$target}{modifiers}||=[]}, [ $type, @names, $code ];
        return;
      };
    } qw(before after around)),
    requires => sub {
      push @{$INFO{$target}{requires}||=[]}, @_;
      return;
    },
    with => sub {
      $me->apply_roles_to_package($target, @_);
      return;
    },
  );
}

sub role_application_steps {
  qw(
    _install_methods
    _check_requires
    _install_modifiers
    _copy_applied_list
  );
}

sub _copy_applied_list {
  my ($me, $to, $role) = @_;
  # copy our role list into the target's
  @{$APPLIED_TO{$to}||={}}{keys %{$APPLIED_TO{$role}}} = ();
}

sub apply_roles_to_object {
  my ($me, $object, @roles) = @_;
  my $class = ref($object);
  # on perl < 5.8.9, magic isn't copied to all ref copies. bless the parameter
  # directly, so at least the variable passed to us will get any magic applied
  bless($_[1], $me->create_class_with_roles($class, @roles));
}

my $role_suffix = 'A000';
sub _composite_name {
  my ($me, $superclass, @roles) = @_;

  my $new_name = $superclass . '__WITH__' . join '__AND__', @roles;

  if (length($new_name) > 252) {
    $new_name = $COMPOSED{abbrev}{$new_name} ||= do {
      my $abbrev = substr $new_name, 0, 250 - length $role_suffix;
      $abbrev =~ s/(?<!:):$//;
      $abbrev.'__'.$role_suffix++;
    };
  }
  return $new_name;
}

sub create_class_with_roles {
  my ($me, $superclass, @roles) = @_;

  $me->_require_module($superclass);
  $me->_check_roles(@roles);

  my $new_name = $me->_composite_name($superclass, @roles);

  return $new_name
    if $COMPOSED{class}{$new_name};

  return $me->_build_class_with_roles($new_name, $superclass, @roles);
}

sub _build_class_with_roles {
  my ($me, $new_name, $superclass, @roles) = @_;

  $COMPOSED{base}{$new_name} = $superclass;
  @{*{_getglob("${new_name}::ISA")}} = ( $superclass );
  $me->apply_roles_to_package($new_name, @roles);
  $COMPOSED{class}{$new_name} = 1;
  return $new_name;
}

sub _check_roles {
  my ($me, @roles) = @_;
  croak "No roles supplied!" unless @roles;

  my %seen;
  if (my @dupes = grep 1 == $seen{$_}++, @roles) {
    croak "Duplicated roles: ".join(', ', @dupes);
  }

  foreach my $role (@roles) {
    $me->_require_module($role);
    croak "${role} is not a ${me}" unless $me->is_role($role);
  }
}

our %BACKCOMPAT_HACK;
$BACKCOMPAT_HACK{+__PACKAGE__} = 0;
sub _want_backcompat_hack {
  my $me = shift;
  return $BACKCOMPAT_HACK{$me}
    if exists $BACKCOMPAT_HACK{$me};
  no warnings 'uninitialized';
  $BACKCOMPAT_HACK{$me} =
    $me->can('apply_single_role_to_package') != \&apply_single_role_to_package
    && $me->can('role_application_steps') == \&role_application_steps
}

our $IN_APPLY_ROLES;
sub apply_single_role_to_package {
  return
    if $IN_APPLY_ROLES;
  local $IN_APPLY_ROLES = 1;

  my ($me, $to, $role) = @_;
  $me->apply_roles_to_package($to, $role);
}

sub apply_role_to_package {
  my ($me, $to, $role) = @_;
  $me->apply_roles_to_package($to, $role);
}

sub apply_roles_to_package {
  my ($me, $to, @roles) = @_;
  croak "Can't apply roles to object with apply_roles_to_package"
    if ref $to;

  $me->_check_roles(@roles);

  my @have_conflicts;
  my %role_methods;

  if (@roles > 1) {
    my %conflicts = %{$me->_composite_info_for(@roles)->{conflicts}};
    @have_conflicts = grep $to->can($_), keys %conflicts;
    delete @conflicts{@have_conflicts};

    if (keys %conflicts) {
      my $class = $COMPOSED{base}{$to} || $to;
      my $fail =
        join "\n",
          map {
            "Due to a method name conflict between roles "
            .join(' and ', map "'$_'", sort values %{$conflicts{$_}})
            .", the method '$_' must be implemented by '$class'"
          } sort keys %conflicts;
      croak $fail;
    }

    %role_methods = map +($_ => $me->_concrete_methods_of($_)), @roles;
  }

  if (!$IN_APPLY_ROLES and _want_backcompat_hack($me)) {
    local $IN_APPLY_ROLES = 1;
    foreach my $role (@roles) {
      $me->apply_single_role_to_package($to, $role);
    }
  }

  my $role_methods;
  foreach my $step ($me->role_application_steps) {
    foreach my $role (@roles) {
      # conflicting methods are supposed to be treated as required by the
      # composed role. we don't have an actual composed role, but because
      # we know the target class already provides them, we can instead
      # pretend that the roles don't do for the duration of application.
      $role_methods = $role_methods{$role} and (
        (local @{$role_methods}{@have_conflicts}),
        (delete @{$role_methods}{@have_conflicts}),
      );

      $me->$step($to, $role);
    }
  }
  $APPLIED_TO{$to}{join('|',@roles)} = 1;
}

sub _composite_info_for {
  my ($me, @roles) = @_;
  $COMPOSITE_INFO{join('|', sort @roles)} ||= do {
    my %methods;
    foreach my $role (@roles) {
      my $this_methods = $me->_concrete_methods_of($role);
      $methods{$_}{$this_methods->{$_}} = $role for keys %$this_methods;
    }
    delete $methods{$_} for grep keys(%{$methods{$_}}) == 1, keys %methods;
    +{ conflicts => \%methods }
  };
}

sub _check_requires {
  my ($me, $to, $name, $requires) = @_;
  $requires ||= $INFO{$name}{requires} || [];
  if (my @requires_fail = grep !$to->can($_), @$requires) {
    # role -> role, add to requires, role -> class, error out
    if (my $to_info = $INFO{$to}) {
      push @{$to_info->{requires}||=[]}, @requires_fail;
    } else {
      croak "Can't apply ${name} to ${to} - missing ".join(', ', @requires_fail);
    }
  }
}

sub _non_methods {
  my ($me, $role) = @_;
  my $info = $INFO{$role} or return {};

  my %non_methods = %{ $info->{non_methods} || {} };

  # this is only for backwards compatibility with older Moo, which
  # reimplements method tracking rather than calling our method
  my %not_methods = reverse %{ $info->{not_methods} || {} };
  return \%non_methods unless keys %not_methods;

  my $subs = $me->_all_subs($role);
  for my $sub (grep !/\A\(/, keys %$subs) {
    my $code = $subs->{$sub};
    if (exists $not_methods{$code}) {
      $non_methods{$sub} = $code;
    }
  }

  return \%non_methods;
}

sub _concrete_methods_of {
  my ($me, $role) = @_;
  my $info = $INFO{$role};

  return $info->{methods}
    if $info && $info->{methods};

  my $non_methods = $me->_non_methods($role);

  my $subs = $me->_all_subs($role);
  for my $sub (keys %$subs) {
    if ( exists $non_methods->{$sub} && $non_methods->{$sub} == $subs->{$sub} ) {
      delete $subs->{$sub};
    }
  }

  if ($info) {
    $info->{methods} = $subs;
  }
  return $subs;
}

sub methods_provided_by {
  my ($me, $role) = @_;
  $me->_require_module($role);
  croak "${role} is not a ${me}" unless $me->is_role($role);
  sort (keys %{$me->_concrete_methods_of($role)}, @{$INFO{$role}->{requires}||[]});
}

sub _install_methods {
  my ($me, $to, $role) = @_;

  my $methods = $me->_concrete_methods_of($role);

  my %existing_methods;
  @existing_methods{keys %{ $me->_all_subs($to) }} = ();

  # _concrete_methods_of caches its result on roles.  that cache needs to be
  # invalidated after applying roles
  delete $INFO{$to}{methods} if $INFO{$to};

  foreach my $i (keys %$methods) {
    next
      if exists $existing_methods{$i};

    my $glob = _getglob "${to}::${i}";
    *$glob = $methods->{$i};

    # overloads using method names have the method stored in the scalar slot
    # and &overload::nil in the code slot.
    next
      unless $i =~ /^\(/
        && ((defined &overload::nil && $methods->{$i} == \&overload::nil)
            || (defined &overload::_nil && $methods->{$i} == \&overload::_nil));

    my $overload = ${ _getglob "${role}::${i}" };
    next
      unless defined $overload;

    *$glob = \$overload;
  }

  $me->_install_does($to);
}

sub _install_modifiers {
  my ($me, $to, $name) = @_;
  return unless my $modifiers = $INFO{$name}{modifiers};
  my $info = $INFO{$to};
  my $existing = ($info ? $info->{modifiers} : $COMPOSED{modifiers}{$to}) ||= [];
  my @modifiers = grep {
    my $modifier = $_;
    !grep $_ == $modifier, @$existing;
  } @{$modifiers||[]};
  push @$existing, @modifiers;

  if (!$info) {
    foreach my $modifier (@modifiers) {
      $me->_install_single_modifier($to, @$modifier);
    }
  }
}

my $vcheck_error;

sub _install_single_modifier {
  my ($me, @args) = @_;
  defined($vcheck_error) or $vcheck_error = do {
    local $@;
    eval {
      require Class::Method::Modifiers;
      Class::Method::Modifiers->VERSION(1.05);
      1;
    } ? 0 : $@;
  };
  $vcheck_error and die $vcheck_error;
  Class::Method::Modifiers::install_modifier(@args);
}

my $FALLBACK = sub { 0 };
sub _install_does {
  my ($me, $to) = @_;

  # only add does() method to classes
  return if $me->is_role($to);

  my $does = $me->can('does_role');
  # add does() only if they don't have one
  *{_getglob "${to}::does"} = $does unless $to->can('does');

  return
    if $to->can('DOES') and $to->can('DOES') != (UNIVERSAL->can('DOES') || 0);

  my $existing = $to->can('DOES') || $to->can('isa') || $FALLBACK;
  my $new_sub = sub {
    my ($proto, $role) = @_;
    $proto->$does($role) or $proto->$existing($role);
  };
  no warnings 'redefine';
  return *{_getglob "${to}::DOES"} = $new_sub;
}

# optimize for newer perls
require mro
  if "$]" >= 5.009_005;

if (defined &mro::get_linear_isa) {
  *_linear_isa = \&mro::get_linear_isa;
}
else {
  my $e;
  {
    local $@;
# this routine is simplified and not fully compatible with mro::get_linear_isa
# but for our use the order doesn't matter, so we don't need to care
    eval <<'END_CODE' or $e = $@;
sub _linear_isa($;$) {
  if (defined &mro::get_linear_isa) {
    no warnings 'redefine', 'prototype';
    *_linear_isa = \&mro::get_linear_isa;
    goto &mro::get_linear_isa;
  }

  my @check = shift;
  my @lin;

  my %found;
  while (defined(my $check = shift @check)) {
    push @lin, $check;
    no strict 'refs';
    unshift @check, grep !$found{$_}++, @{"$check\::ISA"};
  }

  return \@lin;
}

1;
END_CODE
  }
  die $e if defined $e;
}

sub does_role {
  my ($proto, $role) = @_;
  foreach my $class (@{_linear_isa(ref($proto)||$proto)}) {
    return 1 if exists $APPLIED_TO{$class}{$role};
  }
  return 0;
}

sub is_role {
  my ($me, $role) = @_;
  return !!($INFO{$role} && (
    $INFO{$role}{is_role}
    # these are for backward compatibility with older Moo that overrode some
    # methods without calling the originals, thus not getting is_role set
    || $INFO{$role}{requires}
    || $INFO{$role}{not_methods}
    || $INFO{$role}{non_methods}
  ));
}

1;
__END__

=encoding utf-8

=head1 NAME

Role::Tiny - Roles: a nouvelle cuisine portion size slice of Moose

=head1 SYNOPSIS

 package Some::Role;

 use Role::Tiny;

 sub foo { ... }

 sub bar { ... }

 around baz => sub { ... };

 1;

elsewhere

 package Some::Class;

 use Role::Tiny::With;

 # bar gets imported, but not foo
 with 'Some::Role';

 sub foo { ... }

 # baz is wrapped in the around modifier by Class::Method::Modifiers
 sub baz { ... }

 1;

If you wanted attributes as well, look at L<Moo::Role>.

=head1 DESCRIPTION

C<Role::Tiny> is a minimalist role composition tool.

=head1 ROLE COMPOSITION

Role composition can be thought of as much more clever and meaningful multiple
inheritance.  The basics of this implementation of roles is:

=over 2

=item *

If a method is already defined on a class, that method will not be composed in
from the role. A method inherited by a class gets overridden by the role's
method of the same name, though.

=item *

If a method that the role L</requires> to be implemented is not implemented,
role application will fail loudly.

=back

Unlike L<Class::C3>, where the B<last> class inherited from "wins," role
composition is the other way around, where the class wins. If multiple roles
are applied in a single call (single with statement), then if any of their
provided methods clash, an exception is raised unless the class provides
a method since this conflict indicates a potential problem.

=head2 ROLE METHODS

All subs created after importing Role::Tiny will be considered methods to be
composed. For example:

    package MyRole;
    use List::Util qw(min);
    sub mysub { }
    use Role::Tiny;
    use List::Util qw(max);
    sub mymethod { }

In this role, C<max> and C<mymethod> will be included when composing MyRole,
and C<min> and C<mysub> will not. For additional control, L<namespace::clean>
can be used to exclude undesired subs from roles.

=head1 IMPORTED SUBROUTINES

=head2 requires

 requires qw(foo bar);

Declares a list of methods that must be defined to compose role.

=head2 with

 with 'Some::Role1';

 with 'Some::Role1', 'Some::Role2';

Composes another role into the current role (or class via L<Role::Tiny::With>).

If you have conflicts and want to resolve them in favour of Some::Role1 you
can instead write:

 with 'Some::Role1';
 with 'Some::Role2';

If you have conflicts and want to resolve different conflicts in favour of
different roles, please refactor your codebase.

=head2 before

 before foo => sub { ... };

See L<< Class::Method::Modifiers/before method(s) => sub { ... }; >> for full
documentation.

Note that since you are not required to use method modifiers,
L<Class::Method::Modifiers> is lazily loaded and we do not declare it as
a dependency. If your L<Role::Tiny> role uses modifiers you must depend on
both L<Class::Method::Modifiers> and L<Role::Tiny>.

=head2 around

 around foo => sub { ... };

See L<< Class::Method::Modifiers/around method(s) => sub { ... }; >> for full
documentation.

Note that since you are not required to use method modifiers,
L<Class::Method::Modifiers> is lazily loaded and we do not declare it as
a dependency. If your L<Role::Tiny> role uses modifiers you must depend on
both L<Class::Method::Modifiers> and L<Role::Tiny>.

=head2 after

 after foo => sub { ... };

See L<< Class::Method::Modifiers/after method(s) => sub { ... }; >> for full
documentation.

Note that since you are not required to use method modifiers,
L<Class::Method::Modifiers> is lazily loaded and we do not declare it as
a dependency. If your L<Role::Tiny> role uses modifiers you must depend on
both L<Class::Method::Modifiers> and L<Role::Tiny>.

=head2 Strict and Warnings

In addition to importing subroutines, using C<Role::Tiny> applies L<strict> and
L<warnings> to the caller.

=head1 SUBROUTINES

=head2 does_role

 if (Role::Tiny::does_role($foo, 'Some::Role')) {
   ...
 }

Returns true if class has been composed with role.

This subroutine is also installed as ->does on any class a Role::Tiny is
composed into unless that class already has an ->does method, so

  if ($foo->does('Some::Role')) {
    ...
  }

will work for classes but to test a role, one must use ::does_role directly.

Additionally, Role::Tiny will override the standard Perl C<DOES> method
for your class. However, if C<any> class in your class' inheritance
hierarchy provides C<DOES>, then Role::Tiny will not override it.

=head1 METHODS

=head2 make_role

 Role::Tiny->make_role('Some::Role');

Makes a package into a role, but does not export any subs into it.

=head2 apply_roles_to_package

 Role::Tiny->apply_roles_to_package(
   'Some::Package', 'Some::Role', 'Some::Other::Role'
 );

Composes role with package.  See also L<Role::Tiny::With>.

=head2 apply_roles_to_object

 Role::Tiny->apply_roles_to_object($foo, qw(Some::Role1 Some::Role2));

Composes roles in order into object directly. Object is reblessed into the
resulting class. Note that the object's methods get overridden by the role's
ones with the same names.

=head2 create_class_with_roles

 Role::Tiny->create_class_with_roles('Some::Base', qw(Some::Role1 Some::Role2));

Creates a new class based on base, with the roles composed into it in order.
New class is returned.

=head2 is_role

 Role::Tiny->is_role('Some::Role1')

Returns true if the given package is a role.

=head1 CAVEATS

=over 4

=item * On perl 5.8.8 and earlier, applying a role to an object won't apply any
overloads from the role to other copies of the object.

=item * On perl 5.16 and earlier, applying a role to a class won't apply any
overloads from the role to any existing instances of the class.

=back

=head1 SEE ALSO

L<Role::Tiny> is the attribute-less subset of L<Moo::Role>; L<Moo::Role> is
a meta-protocol-less subset of the king of role systems, L<Moose::Role>.

Ovid's L<Role::Basic> provides roles with a similar scope, but without method
modifiers, and having some extra usage restrictions.

=head1 AUTHOR

mst - Matt S. Trout (cpan:MSTROUT) <mst@shadowcat.co.uk>

=head1 CONTRIBUTORS

dg - David Leadbeater (cpan:DGL) <dgl@dgl.cx>

frew - Arthur Axel "fREW" Schmidt (cpan:FREW) <frioux@gmail.com>

hobbs - Andrew Rodland (cpan:ARODLAND) <arodland@cpan.org>

jnap - John Napiorkowski (cpan:JJNAPIORK) <jjn1056@yahoo.com>

ribasushi - Peter Rabbitson (cpan:RIBASUSHI) <ribasushi@cpan.org>

chip - Chip Salzenberg (cpan:CHIPS) <chip@pobox.com>

ajgb - Alex J. G. Burzyński (cpan:AJGB) <ajgb@cpan.org>

doy - Jesse Luehrs (cpan:DOY) <doy at tozt dot net>

perigrin - Chris Prather (cpan:PERIGRIN) <chris@prather.org>

Mithaldu - Christian Walde (cpan:MITHALDU) <walde.christian@googlemail.com>

ilmari - Dagfinn Ilmari Mannsåker (cpan:ILMARI) <ilmari@ilmari.org>

tobyink - Toby Inkster (cpan:TOBYINK) <tobyink@cpan.org>

haarg - Graham Knop (cpan:HAARG) <haarg@haarg.org>

=head1 COPYRIGHT

Copyright (c) 2010-2012 the Role::Tiny L</AUTHOR> and L</CONTRIBUTORS>
as listed above.

=head1 LICENSE

This library is free software and may be distributed under the same terms
as perl itself.

=cut
