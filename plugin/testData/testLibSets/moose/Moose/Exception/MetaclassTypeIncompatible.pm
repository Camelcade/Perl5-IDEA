package Moose::Exception::MetaclassTypeIncompatible;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

use Moose::Util 'find_meta';

has [qw(superclass_name metaclass_type)] => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $class_name = $self->class_name;
    my $superclass_name = $self->superclass_name;
    my $metaclass_type = $self->metaclass_type;

    my $metaclass_type_name = $metaclass_type;
    $metaclass_type_name =~ s/_(?:meta)?class$//;
    $metaclass_type_name =~ s/_/ /g;

    my $class = find_meta( $class_name );

    my $self_metaclass_type = $class->$metaclass_type;

    my $super_meta = Class::MOP::get_metaclass_by_name($superclass_name);
    my $super_metatype = $super_meta->$metaclass_type;

   return "The $metaclass_type metaclass for $class_name"
   . " ($self_metaclass_type) is not compatible with the $metaclass_type_name"
   . " metaclass of its superclass, $superclass_name ($super_metatype)";
}

__PACKAGE__->meta->make_immutable;
1;
