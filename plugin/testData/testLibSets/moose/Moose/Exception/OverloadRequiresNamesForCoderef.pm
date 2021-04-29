package Moose::Exception::OverloadRequiresNamesForCoderef;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    my $self = shift;
    'If you provide a coderef parameter to the Moose::Meta::Overload constructor you must also provide coderef_package and coderef_name parameters';
}

__PACKAGE__->meta->make_immutable;
1;
