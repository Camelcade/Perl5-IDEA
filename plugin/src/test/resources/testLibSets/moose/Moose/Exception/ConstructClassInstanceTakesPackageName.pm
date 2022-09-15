package Moose::Exception::ConstructClassInstanceTakesPackageName;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "You must pass a package name";
}

__PACKAGE__->meta->make_immutable;
1;
