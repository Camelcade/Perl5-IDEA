package Moose::Meta::Method::Accessor::Native::String::replace;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Util ();
use Params::Util ();

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Writer';

sub _minimum_arguments { 1 }

sub _maximum_arguments { 2 }

sub _inline_check_arguments {
    my $self = shift;

    return (
        'if (!Moose::Util::_STRINGLIKE0($_[0]) && !Params::Util::_REGEX($_[0])) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[0],'.
                                            'method_name             => "replace",'.
                                            'ordinal                 => "first",'.
                                            'type_of_argument        => "string or regexp reference",'.
                                            'type                    => "Str|RegexpRef"',
            ) . ';',
        '}',
        'if (!Moose::Util::_STRINGLIKE0($_[1]) && !Params::Util::_CODELIKE($_[1])) {',
            $self->_inline_throw_exception( InvalidArgumentToMethod =>
                                            'argument                => $_[1],'.
                                            'method_name             => "replace",'.
                                            'ordinal                 => "second",'.
                                            'type_of_argument        => "string or code reference",'.
                                            'type                    => "Str|CodeRef"',
            ) . ';',
        '}',
    );
}

sub _potential_value {
    my $self = shift;
    my ($slot_access) = @_;

    return '(do { '
             . 'my $val = ' . $slot_access . '; '
             . 'ref $_[1] '
                 . '? $val =~ s/$_[0]/$_[1]->()/e '
                 . ': $val =~ s/$_[0]/$_[1]/; '
             . '$val; '
         . '})';
}

sub _inline_optimized_set_new_value {
    my $self = shift;
    my ($inv, $new, $slot_access) = @_;

    return (
        'ref $_[1]',
            '? ' . $slot_access . ' =~ s/$_[0]/$_[1]->()/e',
            ': ' . $slot_access . ' =~ s/$_[0]/$_[1]/;',
     );
}

no Moose::Role;

1;
