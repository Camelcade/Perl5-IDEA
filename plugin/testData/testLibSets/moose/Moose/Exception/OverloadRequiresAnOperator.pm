package Moose::Exception::OverloadRequiresAnOperator;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    my $self = shift;
    'You must provide an operator parameter when constructing a Moose::Meta::Overload object';
}

__PACKAGE__->meta->make_immutable;
1;
