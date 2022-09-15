package Moose::Exception::MetaclassMustBeDerivedFromClassMOPClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'class_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "The metaclass (".$self->class_name.") must be derived from Class::MOP::Class";
}

__PACKAGE__->meta->make_immutable;
1;
