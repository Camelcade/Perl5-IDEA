package Moose::Exception::AttributeConflictInSummation;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role', 'Moose::Exception::Role::AttributeName';

has 'second_role_name' => (
    is         => 'ro',
    isa        => 'Str',
    required   => 1,
);

sub _build_message {
    my $self = shift;

    my $role1     = $self->role_name;
    my $role2     = $self->second_role_name;
    my $attr_name = $self->attribute_name;

    return "We have encountered an attribute conflict with '$attr_name'"
           . " during role composition. "
           . " This attribute is defined in both $role1 and $role2."
           . " This is a fatal error and cannot be disambiguated.";
}

__PACKAGE__->meta->make_immutable;
1;
