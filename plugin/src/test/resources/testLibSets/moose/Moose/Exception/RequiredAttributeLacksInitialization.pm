package Moose::Exception::RequiredAttributeLacksInitialization;
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
    "A required attribute must have either 'init_arg', 'builder', or 'default'";
}

__PACKAGE__->meta->make_immutable;
1;
