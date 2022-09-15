package Moose::Exception::UnionTakesAtleastTwoTypeNames;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "You must pass in at least 2 type names to make a union";
}

__PACKAGE__->meta->make_immutable;
1;
