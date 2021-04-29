package Moose::Exception::AccessorMustReadWrite;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "Cannot define an accessor name on a read-only attribute, accessors are read/write";
}

__PACKAGE__->meta->make_immutable;
1;
