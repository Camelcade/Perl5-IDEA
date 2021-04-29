package Moose::Exception::IncompatibleMetaclassOfSuperclass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has [qw/superclass_name superclass_meta_type class_meta_type/] => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    my $self            = shift;
    my $class_name      = $self->class_name;
    my $class_meta_type = $self->class_meta_type;
    my $superclass_name = $self->superclass_name;
    my $supermeta_type  = $self->superclass_meta_type;

    return "The metaclass of $class_name ($class_meta_type)" .
           " is not compatible with the metaclass of its superclass, " .
           "$superclass_name ($supermeta_type)";
}

__PACKAGE__->meta->make_immutable;
1;
