package Moose::Exception::HandlesMustBeAHashRef;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Instance';

has 'given_handles' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    my $self = shift;
    "The 'handles' option must be a HASH reference, not ".$self->given_handles;
}

__PACKAGE__->meta->make_immutable;
1;
