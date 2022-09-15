package Moose::Exception::ReferencesAreNotAllowedAsDefault;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

has 'class' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'attribute_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "References are not allowed as default values, you must wrap the default of '".
        $self->attribute_name."' in a CODE reference (ex: sub { [] } and not [])";
}

__PACKAGE__->meta->make_immutable;
1;
