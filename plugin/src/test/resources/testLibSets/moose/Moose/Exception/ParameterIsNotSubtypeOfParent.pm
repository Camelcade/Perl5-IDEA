package Moose::Exception::ParameterIsNotSubtypeOfParent;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

use Moose::Util::TypeConstraints qw/find_type_constraint/;

has 'type_parameter' => (
    is => 'ro',
    isa => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $type_parameter = $self->type_parameter;
    my $type           = find_type_constraint( $self->type_name );
    my $parent         = $type->parent->type_parameter;

    return "$type_parameter is not a subtype of $parent";
}

__PACKAGE__->meta->make_immutable;
1;
