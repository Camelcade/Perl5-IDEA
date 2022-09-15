package Moose::Exception::AttributeIsRequired;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has 'attribute_name' => (
    is            => 'ro',
    isa           => 'Str',
    required      => 1,
    documentation => "This attribute can be used for fetching attribute instance:\n".
                     "    my \$class = Moose::Util::find_meta( \$exception->class_name );\n".
                     "    my \$attribute = \$class->get_attribute( \$exception->attribute_name );\n",
);

has 'attribute_init_arg' => (
    is  => 'ro',
    isa => 'Str',
);

has 'params' => (
    is        => 'ro',
    isa       => 'HashRef',
    predicate => 'has_params',
);

sub _build_message {
    my $self = shift;

    my $name = $self->attribute_name;
    my $msg  = "Attribute ($name)";

    my $init_arg = $self->attribute_init_arg;
    if ( defined $init_arg && $name ne $init_arg ) {
        $msg .= ", passed as ($init_arg),";
    }

    $msg .= ' is required';

    return $msg;
}

__PACKAGE__->meta->make_immutable;
1;
