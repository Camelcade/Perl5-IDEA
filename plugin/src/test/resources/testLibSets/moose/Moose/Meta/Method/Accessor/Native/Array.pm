package Moose::Meta::Method::Accessor::Native::Array;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

sub _inline_check_var_is_valid_index {
    my $self = shift;
    my ($var) = @_;

    return (
        'if (!defined(' . $var . ') || ' . $var . ' !~ /^-?\d+$/) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => '.$var.','.
                                            'method_name             => "'.$self->delegate_to_method.'",'.
                                            'type_of_argument        => "integer",'.
                                            'type                    => "Int",'.
                                            'argument_noun           => "index"',
            ) . ';',
        '}',
    );
}

no Moose::Role;

1;
