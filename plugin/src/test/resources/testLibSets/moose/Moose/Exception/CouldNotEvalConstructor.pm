package Moose::Exception::CouldNotEvalConstructor;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'constructor_method' => (
    is       => 'ro',
    isa      => 'Class::MOP::Method::Constructor',
    required => 1
);

has 'source' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'error' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $error = $self->error;
    my $source = $self->source;

    return "Could not eval the constructor :\n\n$source\n\nbecause :\n\n$error";
}

__PACKAGE__->meta->make_immutable;
1;
