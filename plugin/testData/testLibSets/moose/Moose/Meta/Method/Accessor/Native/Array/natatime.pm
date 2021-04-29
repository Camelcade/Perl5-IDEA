package Moose::Meta::Method::Accessor::Native::Array::natatime;
our $VERSION = '2.2101';

use strict;
use warnings;

use Params::Util ();

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _minimum_arguments { 1 }

sub _maximum_arguments { 2 }

sub _inline_check_arguments {
    my $self = shift;

    return (
        'if (!defined($_[0]) || $_[0] !~ /^\d+$/) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[0],'.
                                            'method_name             => "natatime",'.
                                            'type_of_argument        => "integer",'.
                                            'type                    => "Int",'.
                                            'argument_noun           => "n value"',
            ) . ';',
        '}',
        'if (@_ == 2 && !Params::Util::_CODELIKE($_[1])) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[1],'.
                                            'method_name             => "natatime",'.
                                            'type_of_argument        => "code reference",'.
                                            'type                    => "CodeRef",'.
                                            'ordinal                 => "second"',
            ) . ';',
        '}',
    );
}

sub _inline_return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return (
        'my $step = $_[0];',
        'my @values = @{ (' . $slot_access . ') };',
        'my $iter = sub { splice @values, 0, $step };',
        'if ($_[1]) {',
            'while (my @vals = $iter->()) {',
                '$_[1]->(@vals);',
            '}',
        '}',
        'else {',
            'return $iter;',
        '}',
    );
}

# Not called, but needed to satisfy the Reader role
sub _return_value { }

no Moose::Role;

1;
