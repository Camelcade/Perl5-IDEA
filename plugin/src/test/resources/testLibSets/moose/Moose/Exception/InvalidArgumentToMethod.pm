package Moose::Exception::InvalidArgumentToMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'argument' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has [qw(type type_of_argument method_name)] => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'ordinal' => (
    is        => 'ro',
    isa       => 'Str',
    predicate => 'is_ordinal_set'
);

has 'argument_noun' => (
    is      => 'ro',
    isa     => 'Str',
    default => 'argument'
);

sub _build_message {
    my $self = shift;
    my $article = ( $self->type_of_argument =~ /^[aeiou]/ ? 'an ' : 'a ');
    my $arg_noun = $self->argument_noun;

    if( $self->is_ordinal_set ) {
        "The ".$self->ordinal." $arg_noun passed to ".$self->method_name." must be ".$article.$self->type_of_argument;
    }
    else {
        "The $arg_noun passed to ".$self->method_name." must be ".$article.$self->type_of_argument;
    }
}

__PACKAGE__->meta->make_immutable;
1;
