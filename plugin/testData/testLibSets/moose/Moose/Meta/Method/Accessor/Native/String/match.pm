package Moose::Meta::Method::Accessor::Native::String::match;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Util ();
use Params::Util ();

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _minimum_arguments { 1 }

sub _maximum_arguments { 1 }

sub _inline_check_arguments {
    my $self = shift;

    return (
        'if (!Moose::Util::_STRINGLIKE0($_[0]) && !Params::Util::_REGEX($_[0])) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[0],'.
                                            'type                    => "Str|RegexpRef",'.
                                            'type_of_argument        => "string or regexp reference",'.
                                            'method_name             => "match"',
            ) . ';',
        '}',
    );
}

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return $slot_access . ' =~ $_[0]';
}

no Moose::Role;

1;
