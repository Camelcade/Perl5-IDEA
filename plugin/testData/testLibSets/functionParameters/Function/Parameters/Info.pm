package Function::Parameters::Info;

use v5.14.0;
use warnings;

use Function::Parameters;
use Carp ();

our $VERSION = '2.001003';

{
    package Function::Parameters::Param;

    use overload
        fallback => 1,
        '""'     => method(@){ $self->{name} },
    ;

    method new($class: :$name, :$type) {
        bless { @_ }, $class
    }

    method name() {$self->{name}}
    method type() {$self->{type}}
}

method new($class:
           :$keyword,
    :$nshift,
    :$_positional_required,
    :$_positional_optional,
    :$_named_required,
    :$_named_optional,
           :$slurpy,
) {
    bless { @_ }, $class
}

method keyword() {$self->{keyword}}
method nshift() {$self->{nshift}}
method slurpy() {$self->{slurpy}}
method positional_optional() {@{$self->{_positional_optional}}}
method named_required() {@{$self->{_named_required}}}
method named_optional() {@{$self->{_named_optional}}}

method positional_required() {
    my @p = @{$self->{_positional_required}};
    splice @p, 0, $self->nshift;
    @p
}

method args_min() {
    my $r = 0;
    $r += @{$self->{_positional_required}};
    $r += $self->named_required * 2;
    $r
}

method args_max() {
    return 0 + 'Inf' if defined $self->slurpy || $self->named_required || $self->named_optional;
    my $r = 0;
    $r += @{$self->{_positional_required}};
    $r += $self->positional_optional;
    $r
}

method invocant() {
    my $nshift = $self->nshift;
    return undef
        if $nshift == 0;
    return $self->{_positional_required}[0]
        if $nshift == 1;
    Carp::croak "Can't return a single invocant; this function has $nshift";
}

method invocants() {
    my @p = @{$self->{_positional_required}};
    splice @p, $self->nshift;
    @p
}

'ok'

__END__

=encoding UTF-8

=head1 NAME

Function::Parameters::Info - Information about parameter lists

=head1 SYNOPSIS

  use Function::Parameters;
  
  fun foo($x, $y, :$hello, :$world = undef) {}
  
  my $info = Function::Parameters::info \&foo;
  my @p0 = $info->invocants;            # ()
  my @p1 = $info->positional_required;  # ('$x', '$y')
  my @p2 = $info->positional_optional;  # ()
  my @p3 = $info->named_required;       # ('$hello')
  my @p4 = $info->named_optional;       # ('$world')
  my $p5 = $info->slurpy;               # undef
  my $min = $info->args_min;  # 4
  my $max = $info->args_max;  # inf
  
  my @invocants = Function::Parameters::info(method () { 42 })->invocants;
  # ('$self')
  
  my $slurpy = Function::Parameters::info(fun (@) {})->slurpy;  # '@'

=head1 DESCRIPTION

L<C<Function::Parameters::info>|Function::Parameters/Introspection> returns
objects of this class to describe parameter lists of functions. See below for
L</Parameter Objects>.  The following methods are available:

=head3 $info->invocants

Returns a list of parameter objects for the variables into which initial
arguments are L<C<shift>|perlfunc/shift ARRAY>ed automatically (or a count in
scalar context). This will usually return C<()> for normal functions and
C<('$self')> for methods.

=head3 $info->positional_required

Returns a list of parameter objects for the required positional parameters (or
a count in scalar context).

=head3 $info->positional_optional

Returns a list of parameter objects for the optional positional parameters (or
a count in scalar context).

=head3 $info->named_required

Returns a list of parameter objects for the required named parameters (or a
count in scalar context).

=head3 $info->named_optional

Returns a list of parameter objects for the optional named parameters (or a
count in scalar context).

=head3 $info->slurpy

Returns a parameter object for the final array or hash that gobbles up all remaining
arguments, or C<undef> if no such thing exists.

=head3 $info->args_min

Returns the minimum number of arguments this function requires. This is
computed as follows: Invocants and required positional parameters count 1 each.
Optional parameters don't count. Required named parameters count 2 each (key +
value). Slurpy parameters don't count either because they accept empty lists.

=head3 $info->args_max

Returns the maximum number of arguments this function accepts. This is computed
as follows: If there are any named or slurpy parameters, the result is C<Inf>.
Otherwise the result is the number of all invocants and positional parameters.

=head3 $info->invocant

Similar to L</$info-E<gt>invocants> above: Returns C<undef> if the number of
invocants is 0, a parameter object for the invocant if there is exactly 1, and
throws an exception otherwise.

=head3 Parameter Objects

Many of the methods described above return parameter objects.  These objects
have two methods: C<name>, which returns the name of the parameter (as a plain
string), and C<type>, which returns the corresponding type constraint object
(or undef if there was no type specified).

This should be invisible if you don't care about types because the objects also
L<overload|overload> stringification to call C<name>. That is, if you treat
parameter objects like strings, they behave like strings (i.e. their names).

=head1 SEE ALSO

L<Function::Parameters>

=head1 AUTHOR

Lukas Mai, C<< <l.mai at web.de> >>

=head1 COPYRIGHT & LICENSE

Copyright 2013, 2016 Lukas Mai.

This program is free software; you can redistribute it and/or modify it
under the terms of either: the GNU General Public License as published
by the Free Software Foundation; or the Artistic License.

See http://dev.perl.org/licenses/ for more information.

=cut
