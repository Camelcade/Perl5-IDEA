package Moose::Exception::TriggerMustBeACodeRef;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "Trigger must be a CODE ref on attribute (".$self->attribute_name.")";
}

__PACKAGE__->meta->make_immutable;
1;
