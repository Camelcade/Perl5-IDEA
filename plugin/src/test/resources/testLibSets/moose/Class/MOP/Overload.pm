package Class::MOP::Overload;
our $VERSION = '2.2101';

use strict;
use warnings;

use overload ();
use Scalar::Util qw( blessed weaken );
use Try::Tiny;

use parent 'Class::MOP::Object';

my %Operators = (
    map { $_ => 1 }
    grep { $_ ne 'fallback' }
    map  { split /\s+/ } values %overload::ops
);

sub new {
    my ( $class, %params ) = @_;

    unless ( defined $params{operator} ) {
        $class->_throw_exception('OverloadRequiresAnOperator');
    }
    unless ( $Operators{ $params{operator} } ) {
        $class->_throw_exception(
            'InvalidOverloadOperator',
            operator => $params{operator},
        );
    }

    unless ( defined $params{method_name} || $params{coderef} ) {
        $class->_throw_exception(
            'OverloadRequiresAMethodNameOrCoderef',
            operator => $params{operator},
        );
    }

    if ( $params{coderef} ) {
        unless ( defined $params{coderef_package}
            && defined $params{coderef_name} ) {

            $class->_throw_exception('OverloadRequiresNamesForCoderef');
        }
    }

    if ( $params{method}
        && !try { $params{method}->isa('Class::MOP::Method') } ) {

        $class->_throw_exception('OverloadRequiresAMetaMethod');
    }

    if ( $params{associated_metaclass}
        && !try { $params{associated_metaclass}->isa('Class::MOP::Module') } )
    {

        $class->_throw_exception('OverloadRequiresAMetaClass');
    }

    my @optional_attrs
        = qw( method_name coderef coderef_package coderef_name method associated_metaclass );

    return bless {
        operator => $params{operator},
        map { defined $params{$_} ? ( $_ => $params{$_} ) : () }
            @optional_attrs
        },
        $class;
}

sub operator { $_[0]->{operator} }

sub method_name { $_[0]->{method_name} }
sub has_method_name { exists $_[0]->{method_name} }

sub method { $_[0]->{method} }
sub has_method { exists $_[0]->{method} }

sub coderef { $_[0]->{coderef} }
sub has_coderef { exists $_[0]->{coderef} }

sub coderef_package { $_[0]->{coderef_package} }
sub has_coderef_package { exists $_[0]->{coderef_package} }

sub coderef_name { $_[0]->{coderef_name} }
sub has_coderef_name { exists $_[0]->{coderef_name} }

sub associated_metaclass { $_[0]->{associated_metaclass} }

sub is_anonymous {
    my $self = shift;
    return $self->has_coderef && $self->coderef_name eq '__ANON__';
}

sub attach_to_class {
    my ( $self, $class ) = @_;
    $self->{associated_metaclass} = $class;
    weaken $self->{associated_metaclass};
}

sub clone {
    my $self = shift;

    my $clone = bless { %{$self}, @_ }, blessed($self);
    weaken $clone->{associated_metaclass} if $clone->{associated_metaclass};

    $clone->_set_original_overload($self);

    return $clone;
}

sub original_overload { $_[0]->{original_overload} }
sub _set_original_overload { $_[0]->{original_overload} = $_[1] }

sub _is_equal_to {
    my $self  = shift;
    my $other = shift;

    if ( $self->has_coderef ) {
        return unless $other->has_coderef;
        return $self->coderef == $other->coderef;
    }
    else {
        return $self->method_name eq $other->method_name;
    }
}

1;

# ABSTRACT: Overload Meta Object

__END__

=pod

=head1 SYNOPSIS

    my $meta     = Class->meta;
    my $overload = $meta->get_overloaded_operator('+');

    if ( $overload->has_method_name ) {
        print 'Method for + is ', $overload->method_name, "\n";
    }
    else {
        print 'Overloading for + is implemented by ',
            $overload->coderef_name, " sub\n";
    }

=head1 DESCRIPTION

This class provides meta information for overloading in classes and roles.

=head1 INHERITANCE

C<Class::MOP::Overload> is a subclass of L<Class::MOP::Object>.

=head1 METHODS

=head2 Class::MOP::Overload->new(%options)

This method creates a new C<Class::MOP::Overload> object. It accepts a number
of options:

=over 4

=item * operator

This is a string that matches an operator known by the L<overload> module,
such as C<""> or C<+>. This is required.

=item * method_name

The name of the method which implements the overloading. Note that this does
not need to actually correspond to a real method, since it's okay to declare a
not-yet-implemented overloading.

Either this or the C<coderef> option must be passed.

=item * method

A L<Class::MOP::Method> object for the method which implements the
overloading.

This is optional.

=item * coderef

A coderef which implements the overloading.

Either this or the C<method_name> option must be passed.

=item * coderef_package

The package where the coderef was defined.

This is required if C<coderef> is passed.

=item * coderef_name

The name of the coderef. This can be "__ANON__".

This is required if C<coderef> is passed.

=item * associated_metaclass

A L<Class::MOP::Module> object for the associated class or role.

This is optional.

=back

=head2 $overload->operator

Returns the operator for this overload object.

=head2 $overload->method_name

Returns the method name that implements overloading, if it has one.

=head2 $overload->has_method_name

Returns true if the object has a method name.

=head2 $overload->method

Returns the L<Class::MOP::Method> that implements overloading, if it has one.

=head2 $overload->has_method

Returns true if the object has a method.

=head2 $overload->coderef

Returns the coderef that implements overloading, if it has one.

=head2 $overload->has_coderef

Returns true if the object has a coderef.

=head2 $overload->coderef_package

Returns the package for the coderef that implements overloading, if it has
one.

=head2 $overload->has_coderef

Returns true if the object has a coderef package.

=head2 $overload->coderef_name

Returns the sub name for the coderef that implements overloading, if it has
one.

=head2 $overload->has_coderef_name

Returns true if the object has a coderef name.

=head2 $overload->is_anonymous

Returns true if the overloading is implemented by an anonymous coderef.

=head2 $overload->associated_metaclass

Returns the L<Class::MOP::Module> (class or role) that is associated with the
overload object.

=head2 $overload->clone

Clones the overloading object, setting C<original_overload> in the process.

=head2 $overload->original_overload

For cloned objects, this returns the L<Class::MOP::Overload> object from which
they were cloned. This can be used to determine the source of an overloading
in a class that came from a role, for example.

=cut
