package Moose::Exception::NeitherTypeNorTypeNameIsGiven;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "You need to give type or type_name or both";
}

__PACKAGE__->meta->make_immutable;
1;
