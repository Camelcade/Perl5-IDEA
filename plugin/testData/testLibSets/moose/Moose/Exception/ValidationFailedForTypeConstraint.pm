package Moose::Exception::ValidationFailedForTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

has 'value' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1,
);

has 'type' => (
    is       => 'ro',
    isa      => Moose::Util::TypeConstraints->duck_type(["get_message", "name"]),
    required => 1
);

sub _build_message {
    my $self = shift;

    my $error = $self->type->get_message( $self->value );

    return $error unless $self->is_attribute_set;

    my $attribute_name = $self->attribute->name;
    return
        "Attribute ($attribute_name) does not pass the type constraint because: $error";
}

__PACKAGE__->meta->make_immutable;
1;
