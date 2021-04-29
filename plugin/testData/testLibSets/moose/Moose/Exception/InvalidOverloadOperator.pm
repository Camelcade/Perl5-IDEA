package Moose::Exception::InvalidOverloadOperator;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has operator => (
    is       => 'ro',
    isa      => 'Defined',
    required => 1,
);

sub _build_message {
    my $self = shift;
    'The operator parameter you passed to the Moose::Meta::Overload constructor ('
        . $self->operator()
        . ') was not a valid overloading operator';
}

__PACKAGE__->meta->make_immutable;
1;
