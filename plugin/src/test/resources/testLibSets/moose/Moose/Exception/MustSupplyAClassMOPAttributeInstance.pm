package Moose::Exception::MustSupplyAClassMOPAttributeInstance;
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
    "You must supply an attribute which is a 'Class::MOP::Attribute' instance";
}

__PACKAGE__->meta->make_immutable;
1;
