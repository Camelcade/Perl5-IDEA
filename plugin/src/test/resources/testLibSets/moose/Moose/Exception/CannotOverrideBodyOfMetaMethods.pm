package Moose::Exception::CannotOverrideBodyOfMetaMethods;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

has 'class' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    "Overriding the body of meta methods is not allowed";
}

__PACKAGE__->meta->make_immutable;
1;
