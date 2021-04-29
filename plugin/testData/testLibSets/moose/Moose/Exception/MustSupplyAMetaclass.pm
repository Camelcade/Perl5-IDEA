package Moose::Exception::MustSupplyAMetaclass;
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
    my $self = shift;
    "You must pass a metaclass instance if you want to inline";
}

__PACKAGE__->meta->make_immutable;
1;
