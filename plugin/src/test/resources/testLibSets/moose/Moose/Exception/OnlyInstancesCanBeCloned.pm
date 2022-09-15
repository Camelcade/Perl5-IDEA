package Moose::Exception::OnlyInstancesCanBeCloned;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::ParamsHash';

has 'instance' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1,
);

sub _build_message {
    my $self = shift;
    "You can only clone instances, (".$self->instance.") is not a blessed instance";
}

__PACKAGE__->meta->make_immutable;
1;
