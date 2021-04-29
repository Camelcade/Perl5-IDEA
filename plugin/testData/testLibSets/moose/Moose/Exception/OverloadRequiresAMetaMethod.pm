package Moose::Exception::OverloadRequiresAMetaMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    my $self = shift;
    'If you provide a method parameter to the Moose::Meta::Overload constructor it must be a Class::MOP::Method object';
}

__PACKAGE__->meta->make_immutable;
1;
