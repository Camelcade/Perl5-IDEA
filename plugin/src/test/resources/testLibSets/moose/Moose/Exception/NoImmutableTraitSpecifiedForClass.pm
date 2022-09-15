package Moose::Exception::NoImmutableTraitSpecifiedForClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::ParamsHash';

use Moose::Util 'find_meta';

sub _build_message {
    my $self  = shift;
    my $class = find_meta( $self->class_name );
    "no immutable trait specified for $class";
}

__PACKAGE__->meta->make_immutable;
1;
