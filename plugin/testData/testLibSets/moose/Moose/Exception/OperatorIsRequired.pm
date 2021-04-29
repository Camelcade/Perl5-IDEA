package Moose::Exception::OperatorIsRequired;
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
    "operator is required";
}

__PACKAGE__->meta->make_immutable;
1;
