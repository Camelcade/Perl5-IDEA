package Moose::Exception::InvalidHasProvidedInARole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

has 'attribute_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    "Usage: has 'name' => ( key => value, ... )";
}

__PACKAGE__->meta->make_immutable;
1;
