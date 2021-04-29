package Moose::Exception::NoDestructorClassSpecified;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::ParamsHash';

sub _build_message {
    "The 'inline_destructor' option is present, but no destructor class was specified";
}

__PACKAGE__->meta->make_immutable;
1;
