package Moose::Exception::UnableToRecognizeDelegateMetaclass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

has 'delegate_metaclass' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $meta = $self->delegate_metaclass;

    return "Unable to recognize the delegate metaclass '$meta'";
}

__PACKAGE__->meta->make_immutable;
1;
