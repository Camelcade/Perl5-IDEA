package Moose::Exception::CouldNotCreateWriter;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::EitherAttributeOrAttributeName', 'Moose::Exception::Role::Instance';

has 'error' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $error = $self->error;
    my $attribute_name = $self->attribute_name;

    return "Could not create writer for '$attribute_name' "
        . "because $error";
}

__PACKAGE__->meta->make_immutable;
1;
