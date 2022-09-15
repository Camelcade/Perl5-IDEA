package Moose::Exception::BuilderMustBeAMethodName;
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
    "builder must be a defined scalar value which is a method name";
}

__PACKAGE__->meta->make_immutable;
1;
