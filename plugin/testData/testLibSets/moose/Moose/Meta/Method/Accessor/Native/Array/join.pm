package Moose::Meta::Method::Accessor::Native::Array::join;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Util ();

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _minimum_arguments { 1 }

sub _maximum_arguments { 1 }

sub _inline_check_arguments {
    my $self = shift;

    return (
        'if (!Moose::Util::_STRINGLIKE0($_[0])) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[0],'.
                                            'method_name             => "join",'.
                                            'type_of_argument        => "string",'.
                                            'type                    => "Str",',
            ) . ';',
        '}',
    );
}

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return 'join $_[0], @{ (' . $slot_access . ') }';
}

no Moose::Role;

1;
