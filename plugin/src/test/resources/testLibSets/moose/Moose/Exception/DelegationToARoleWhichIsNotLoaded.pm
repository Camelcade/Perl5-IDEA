package Moose::Exception::DelegationToARoleWhichIsNotLoaded;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

has 'role_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    my $self = shift;
    "The ".$self->attribute->name." attribute is trying to delegate to a role which has not been loaded - ".$self->role_name;
}

__PACKAGE__->meta->make_immutable;
1;
