package Moose::Exception::AutoDeRefNeedsArrayRefOrHashRef;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "You cannot auto-dereference anything other than a ArrayRef or HashRef on attribute (".$self->attribute_name.")";
}

__PACKAGE__->meta->make_immutable;
1;
