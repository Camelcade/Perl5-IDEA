package Moose::Exception::CannotFindTypeGivenToMatchOnType;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'to_match' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has 'action' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has 'type' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $type = $self->type;

    return "Cannot find or parse the type '$type'"
}

__PACKAGE__->meta->make_immutable;
1;
