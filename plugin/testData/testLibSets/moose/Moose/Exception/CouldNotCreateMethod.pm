package Moose::Exception::CouldNotCreateMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

has 'option_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'option_value' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has 'error' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "Could not create the '".$self->option_name."' method for " . $self->attribute->name . " because : ".$self->error;
}

__PACKAGE__->meta->make_immutable;
1;
