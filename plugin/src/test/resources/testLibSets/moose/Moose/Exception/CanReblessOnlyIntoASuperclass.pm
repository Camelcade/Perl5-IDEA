package Moose::Exception::CanReblessOnlyIntoASuperclass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::Instance', 'Moose::Exception::Role::InstanceClass';

sub _build_message {
    my $self = shift;
    "You may rebless only into a superclass of (".blessed( $self->instance )."), of which (". $self->class_name .") isn't."
}

__PACKAGE__->meta->make_immutable;
1;
