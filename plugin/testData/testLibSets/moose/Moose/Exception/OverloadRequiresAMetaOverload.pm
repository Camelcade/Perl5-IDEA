package Moose::Exception::OverloadRequiresAMetaOverload;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    my $self = shift;
    'If you provide an original_overload parameter to the Moose::Meta::Overload constructor it must be a Moose::Meta::Overload object';
}

__PACKAGE__->meta->make_immutable;
1;
