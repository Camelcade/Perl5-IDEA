package Moose::Meta::TypeCoercion;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use Moose::Meta::Attribute;
use Moose::Util::TypeConstraints ();

use Moose::Util 'throw_exception';

__PACKAGE__->meta->add_attribute('type_coercion_map' => (
    reader  => 'type_coercion_map',
    default => sub { [] },
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute(
    Moose::Meta::Attribute->new('type_constraint' => (
        reader   => 'type_constraint',
        weak_ref => 1,
        Class::MOP::_definition_context(),
    ))
);

# private accessor
__PACKAGE__->meta->add_attribute('compiled_type_coercion' => (
    accessor => '_compiled_type_coercion',
    Class::MOP::_definition_context(),
));

sub new {
    my $class = shift;
    my $self  = Class::MOP::class_of($class)->new_object(@_);
    $self->compile_type_coercion;
    return $self;
}

sub compile_type_coercion {
    my $self = shift;
    my @coercion_map = @{$self->type_coercion_map};
    my @coercions;
    while (@coercion_map) {
        my ($constraint_name, $action) = splice(@coercion_map, 0, 2);
        my $type_constraint = ref $constraint_name ? $constraint_name : Moose::Util::TypeConstraints::find_or_parse_type_constraint($constraint_name);

        unless ( defined $type_constraint ) {
            throw_exception( CouldNotFindTypeConstraintToCoerceFrom => constraint_name => $constraint_name,
                                                                       instance        => $self
                           );
        }

        push @coercions => [
            $type_constraint->_compiled_type_constraint,
            $action
        ];
    }
    $self->_compiled_type_coercion(sub {
        my $thing = shift;
        foreach my $coercion (@coercions) {
            my ($constraint, $converter) = @$coercion;
            if ($constraint->($thing)) {
                local $_ = $thing;
                return $converter->($thing);
            }
        }
        return $thing;
    });
}

sub has_coercion_for_type {
    my ($self, $type_name) = @_;
    my %coercion_map = @{$self->type_coercion_map};
    exists $coercion_map{$type_name} ? 1 : 0;
}

sub add_type_coercions {
    my ($self, @new_coercion_map) = @_;

    my $coercion_map = $self->type_coercion_map;
    my %has_coercion = @$coercion_map;

    while (@new_coercion_map) {
        my ($constraint_name, $action) = splice(@new_coercion_map, 0, 2);

        if ( exists $has_coercion{$constraint_name} ) {
            throw_exception( CoercionAlreadyExists => constraint_name => $constraint_name,
                                                      instance        => $self
                           );
        }

        push @{$coercion_map} => ($constraint_name, $action);
    }

    # and re-compile ...
    $self->compile_type_coercion;
}

sub coerce { $_[0]->_compiled_type_coercion->($_[1]) }


1;

# ABSTRACT: The Moose Type Coercion metaclass

__END__

=pod

=head1 DESCRIPTION

A type coercion object is basically a mapping of one or more type
constraints and the associated coercions subroutines.

It's unlikely that you will need to instantiate an object of this
class directly, as it's part of the deep internals of Moose.

=head1 METHODS

=head2 Moose::Meta::TypeCoercion->new(%options)

Creates a new type coercion object, based on the options provided.

=over 4

=item * type_constraint

This is the L<Moose::Meta::TypeConstraint> object for the type that is
being coerced I<to>.

=back

=head2 $coercion->type_coercion_map

This returns the map of type constraints to coercions as an array
reference. The values of the array alternate between type names and
subroutine references which implement the coercion.

The value is an array reference because coercions are tried in the
order they are added.

=head2 $coercion->type_constraint

This returns the L<Moose::Meta::TypeConstraint> that was passed to the
constructor.

=head2 $coercion->has_coercion_for_type($type_name)

Returns true if the coercion can coerce the named type.

=head2 $coercion->add_type_coercions( $type_name => $sub, ... )

This method takes a list of type names and subroutine references. If
the coercion already has a mapping for a given type, it throws an
exception.

Coercions are actually

=head2 $coercion->coerce($value)

This method takes a value and applies the first valid coercion it
finds.

This means that if the value could belong to more than type in the
coercion object, the first coercion added is used.

=head2 Moose::Meta::TypeCoercion->meta

This will return a L<Class::MOP::Class> instance for this class.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
