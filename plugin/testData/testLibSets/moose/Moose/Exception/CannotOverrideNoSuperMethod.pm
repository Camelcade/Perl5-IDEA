package Moose::Exception::CannotOverrideNoSuperMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

has 'method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'class' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "You cannot override '".$self->method_name."' because it has no super method";
}

__PACKAGE__->meta->make_immutable;
1;
