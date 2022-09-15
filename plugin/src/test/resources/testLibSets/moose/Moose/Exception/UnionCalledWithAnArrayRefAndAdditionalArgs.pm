package Moose::Exception::UnionCalledWithAnArrayRefAndAdditionalArgs;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'array' => (
    is       => 'ro',
    isa      => 'ArrayRef',
    required => 1
);

has 'args' => (
    is       => 'ro',
    isa      => 'ArrayRef',
    required => 1
);

sub _build_message {
    "union called with an array reference and additional arguments";
}

__PACKAGE__->meta->make_immutable;
1;
