package Moose::Exception::ValidationFailedForInlineTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has 'type_constraint_message' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'attribute_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'value' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

has 'new_member' => (
    is        => 'ro',
    isa       => 'Bool',
    default   => 0,
    predicate => 'is_a_new_member'
);

sub _build_message {
    my $self = shift;

    my $line1;

    if( $self->new_member ) {
        $line1 = "A new member value for ".$self->attribute_name." does not pass its type constraint because: "
    }
    else {
        $line1 = "Attribute (".$self->attribute_name.") does not pass the type constraint because: ";
    }

    return $line1 . $self->type_constraint_message;
}

__PACKAGE__->meta->make_immutable;
1;
