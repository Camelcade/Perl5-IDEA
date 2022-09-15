package Moose::Exception::TypeConstraintCannotBeUsedForAParameterizableType;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

has 'parent_type_name' => (
    is            => 'ro',
    isa           => 'Str',
    required      => 1,
    documentation => "This attribute can be used for fetching type constraint(Moose::Meta::TypeConstraint):\n".
                     "    my \$type_constraint =  Moose::Util::TypeConstraints::find_type_constraint( \$exception->parent_type_name );\n",
);

sub _build_message {
    my $self             = shift;
    my $type_name        = $self->type_name;
    my $parent_type_name = $self->parent_type_name;
    "The $type_name constraint cannot be used, because "
        . "$parent_type_name doesn't subtype or coerce from a parameterizable type.";
}

__PACKAGE__->meta->make_immutable;
1;
