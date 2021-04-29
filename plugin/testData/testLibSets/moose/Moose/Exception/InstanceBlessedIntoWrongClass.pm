package Moose::Exception::InstanceBlessedIntoWrongClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash', 'Moose::Exception::Role::Class', 'Moose::Exception::Role::Instance';

sub _build_message {
    my $self = shift;
    "Objects passed as the __INSTANCE__ parameter must already be blessed into the correct class, but ".$self->instance." is not a " . $self->class_name;
}

__PACKAGE__->meta->make_immutable;
1;
