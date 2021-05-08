package MooseX::Role::Parameterized::Meta::Trait::Parameterizable;
# ABSTRACT: trait for parameterizable roles

our $VERSION = '1.11';

use Moose::Role;
use MooseX::Role::Parameterized::Meta::Role::Parameterized;
use MooseX::Role::Parameterized::Parameters;
use Module::Runtime 'use_module';
use namespace::autoclean;

has parameterized_role_metaclass => (
    is      => 'ro',
    isa     => 'ClassName',
    default => 'MooseX::Role::Parameterized::Meta::Role::Parameterized',
);

has parameters_class => (
    is      => 'ro',
    isa     => 'ClassName',
    default => 'MooseX::Role::Parameterized::Parameters',
);

has parameters_metaclass => (
    is        => 'rw',
    isa       => 'Moose::Meta::Class',
    lazy      => 1,
    builder   => '_build_parameters_metaclass',
    handles   => {
        has_parameter        => 'has_attribute',
        add_parameter        => 'add_attribute',
        construct_parameters => 'new_object',
    },
    predicate => '_has_parameters_metaclass',
);

has role_generator => (
    is        => 'rw',
    isa       => 'CodeRef',
    predicate => 'has_role_generator',
);

sub _build_parameters_metaclass {
    my $self = shift;

    return $self->parameters_class->meta->create_anon_class(
        superclasses => [ $self->parameters_class ],
    );
}

my $package_counter = 0;
sub generate_role {
    my $self = shift;
    my %args = @_;

    my $parameters = blessed($args{parameters})
        ? $args{parameters}
        : $self->construct_parameters(%{$args{parameters}});

    confess "A role generator is required to apply parameterized roles (did you forget the 'role { ... }' block in your parameterized role '" . $self->name . "'?)"
        unless $self->has_role_generator;

    my $parameterized_role_metaclass = $self->parameterized_role_metaclass;
    use_module($parameterized_role_metaclass);

    my $package = $args{package};
    unless ($package) {
        $package_counter++;
        $package = $self->name . '::__ANON__::SERIAL::' . $package_counter;
    }
    my $role = $parameterized_role_metaclass->create(
        $package,
        genitor    => $self,
        parameters => $parameters,
    );

    local $MooseX::Role::Parameterized::CURRENT_METACLASS = $role;

    # The generate_role method is being called directly by things like
    # MooseX::ClassCompositor. We don't want to force such modules to pass
    # this arg so we default to something sane.
    my $orig_apply = $args{orig_apply} || Moose::Meta::Role->can('apply');
    $self->$orig_apply($role);

    $self->role_generator->($parameters,
        operating_on => $role,
        consumer     => $args{consumer},
    );

    # don't just return $role here, because it might have been changed when
    # metaroles are applied
    return $MooseX::Role::Parameterized::CURRENT_METACLASS;
}

sub _role_for_combination {
    my $self = shift;
    my $parameters = shift;

    return $self->generate_role(
        parameters => $parameters,
    );
}

around apply => sub {
    my $orig = shift;
    my $self = shift;
    my $consumer = shift;
    my %args = @_;

    my $role = $self->generate_role(
        consumer   => $consumer,
        parameters => \%args,
        orig_apply => $orig,
    );

    $role->apply($consumer, %args);
};

around reinitialize => sub {
    my $orig = shift;
    my $class = shift;
    my ($pkg) = @_;
    my $meta = blessed($pkg) ? $pkg : find_meta($pkg);

    my $meta_meta = $meta->meta;

    my %p;
    if ($meta_meta->can('does_role') && $meta_meta->does_role(__PACKAGE__)) {
        %p = map {$_ => $meta->$_}
            qw(parameterized_role_metaclass parameters_class);
        $p{parameters_metaclass} = $meta->parameters_metaclass
            if $meta->_has_parameters_metaclass;
        $p{role_generator} = $meta->role_generator
            if $meta->has_role_generator;
    }

    my $new = $class->$orig(
        @_,
        %p,
    );

    return $new;
};

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Role::Parameterized::Meta::Trait::Parameterizable - trait for parameterizable roles

=head1 VERSION

version 1.11

=head1 DESCRIPTION

This is the trait that is applied to the metaclass for parameterizable roles,
roles that have their parameters currently unbound. These are the roles that
you use L<Moose/with>, but instead of composing the parameterizable role, we
construct a new parameterized role
(L<MooseX::Role::Parameterized::Meta::Role::Parameterized>) and use that new
parameterized role instead.

=head1 ATTRIBUTES

=head2 parameterized_role_metaclass

The name of the class that will be used to construct the parameterized role.

=head2 parameters_class

The name of the class that will be used to construct the parameters object.

=head2 parameters_metaclass

A metaclass representing this role's parameters. It will be an anonymous
subclass of L</parameters_class>. Each call to
L<MooseX::Role::Parameters/parameter> adds an attribute to this metaclass.

When this role is consumed, the parameters object will be instantiated using
this metaclass.

=head2 role_generator

A code reference that is used to generate a role based on the parameters
provided by the consumer. The user usually specifies it using the
L<MooseX::Role::Parameterized/role> keyword.

=head1 METHODS

=head2 add_parameter $name, %options

Delegates to L<Moose::Meta::Class/add_attribute> on the
L</parameters_metaclass> object.

=head2 construct_parameters %arguments

Creates a new L<MooseX::Role::Parameterized::Parameters> object using metaclass
L</parameters_metaclass>.

The arguments are those specified by the consumer as parameter values.

=head2 generate_role %arguments

This method generates and returns a new instance of
L</parameterized_role_metaclass>. It can take any combination of
three named arguments:

=over 4

=item parameters

A hashref of parameters for the role, same as would be passed in at a "with"
statement.

=item package

A package name that, if present, we will use for the generated role; if not,
we generate an anonymous role.

=item consumer

=for stopwords metaobject

A consumer metaobject, if available.

=back

=head2 apply

Overrides L<Moose::Meta::Role/apply> to automatically generate the
parameterized role.

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Role-Parameterized>
(or L<bug-MooseX-Role-Parameterized@rt.cpan.org|mailto:bug-MooseX-Role-Parameterized@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Shawn M Moore <code@sartak.org>

=head1 COPYRIGHT AND LICENSE

This software is copyright (c) 2008 by Shawn M Moore.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
