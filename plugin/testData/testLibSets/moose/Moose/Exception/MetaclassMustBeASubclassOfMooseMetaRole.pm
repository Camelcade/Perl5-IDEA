package Moose::Exception::MetaclassMustBeASubclassOfMooseMetaRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

sub _build_message {
    my $self = shift;
    "The Metaclass ".$self->role_name." must be a subclass of Moose::Meta::Role."
}

__PACKAGE__->meta->make_immutable;
1;
