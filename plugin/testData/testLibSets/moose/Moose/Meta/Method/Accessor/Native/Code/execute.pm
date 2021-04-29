package Moose::Meta::Method::Accessor::Native::Code::execute;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return $slot_access . '->(@_)';
}

no Moose::Role;

1;
