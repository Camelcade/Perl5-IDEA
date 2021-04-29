package Moose::Exception::InitializeTakesUnBlessedPackageName;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'package_name' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1,
);

sub _build_message {
    "You must pass a package name and it cannot be blessed";
}

__PACKAGE__->meta->make_immutable;
1;
