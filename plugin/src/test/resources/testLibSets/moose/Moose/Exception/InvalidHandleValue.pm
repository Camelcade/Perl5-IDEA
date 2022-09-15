package Moose::Exception::InvalidHandleValue;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Instance';

has 'handle_value' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    my $self = shift;
    "All values passed to handles must be strings or ARRAY references, not ".$self->handle_value;
}

__PACKAGE__->meta->make_immutable;
1;
