package Moose::Exception::NoCasesMatched;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'to_match' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has 'cases_to_be_matched' => (
    is       => 'ro',
    isa      => 'ArrayRef',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $to_match = $self->to_match;

    return "No cases matched for $to_match";
}

__PACKAGE__->meta->make_immutable;
1;
