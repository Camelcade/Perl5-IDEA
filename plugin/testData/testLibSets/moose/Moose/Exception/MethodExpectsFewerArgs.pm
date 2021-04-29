package Moose::Exception::MethodExpectsFewerArgs;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

has 'maximum_args' => (
    is       => 'ro',
    isa      => 'Int',
    required => 1,
);

sub _build_message {
    my $self = shift;
    my $max = $self->maximum_args;
    "Cannot call ".$self->method_name." with ".
        ( $max ? "more than $max" : 'any'). " argument".( $max == 1 ? '' : 's' );
}

__PACKAGE__->meta->make_immutable;
1;
