package Moose::Exception::ClassDoesTheExcludedRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role', 'Moose::Exception::Role::Class';

has 'excluded_role_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self               = shift;
    my $excluded_role_name = $self->excluded_role_name;
    my $class_name         = $self->class_name;
    return "The class $class_name does the excluded role '$excluded_role_name'";
}

__PACKAGE__->meta->make_immutable;
1;
