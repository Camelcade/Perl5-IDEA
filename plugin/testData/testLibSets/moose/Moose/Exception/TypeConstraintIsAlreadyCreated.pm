package Moose::Exception::TypeConstraintIsAlreadyCreated;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

use Moose::Util::TypeConstraints 'find_type_constraint';

has 'package_defined_in' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    my $self                    = shift;
    my $type_name               = $self->type_name;
    my $type                    = find_type_constraint( $type_name );
    my $type_package_defined_in = $type->_package_defined_in;
    my $package_defined_in      = $self->package_defined_in;
    return "The type constraint '$type_name' has already been created in $type_package_defined_in and cannot be created again in $package_defined_in";
}

__PACKAGE__->meta->make_immutable;
1;
