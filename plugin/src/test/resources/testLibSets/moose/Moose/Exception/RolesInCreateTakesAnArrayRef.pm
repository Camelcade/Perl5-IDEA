package Moose::Exception::RolesInCreateTakesAnArrayRef;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

sub _build_message {
    my $self = shift;
    "You must pass an ARRAY ref of roles";
}

__PACKAGE__->meta->make_immutable;
1;
