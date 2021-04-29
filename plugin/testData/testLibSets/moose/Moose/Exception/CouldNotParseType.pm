package Moose::Exception::CouldNotParseType;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'type' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'position' => (
    is       => 'ro',
    isa      => 'Int',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $type = $self->type;
    my $length = length($type);
    my $position = $self->position;

    return "'$type' didn't parse (parse-pos=$position"
        . " and str-length=$length)";
}

__PACKAGE__->meta->make_immutable;
1;
