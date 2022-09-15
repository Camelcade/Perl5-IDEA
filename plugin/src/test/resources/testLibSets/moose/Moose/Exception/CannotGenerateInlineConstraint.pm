package Moose::Exception::CannotGenerateInlineConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

has 'parameterizable_type_object_name' => (
    is            => 'ro',
    isa           => 'Str',
    required      => 1,
    documentation => "This attribute can be used for fetching parameterizable type constraint(Moose::Meta::TypeConstraint::Parameterizable):\n".
                     "    my \$type_constraint =  Moose::Util::TypeConstraints::find_type_constraint( \$exception->type_name );\n",
);

has 'value' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $type = $self->type_name;

    return "Can't generate an inline constraint for $type, since none was defined";
}

__PACKAGE__->meta->make_immutable;
1;
