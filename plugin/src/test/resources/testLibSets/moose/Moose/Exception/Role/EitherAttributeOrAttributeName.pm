package Moose::Exception::Role::EitherAttributeOrAttributeName;
our $VERSION = '2.2101';

use Moose::Util 'throw_exception';
use Moose::Role;

has 'attribute_name' => (
    is         => 'ro',
    isa        => 'Str',
    lazy_build => 1
);

has 'attribute' => (
    is        => 'ro',
    isa       => 'Class::MOP::Attribute',
    predicate => 'has_attribute'
);

has 'params' => (
    is        => 'ro',
    isa       => 'HashRef',
    predicate => 'has_params',
);

sub _build_attribute_name {
    my $self = shift;

    if( !$self->has_attribute )
    {
        throw_exception("NeitherAttributeNorAttributeNameIsGiven");
    }

    return $self->attribute->name;
}

after "BUILD" => sub {
    my $self = $_[0];

    if( $self->has_attribute_name &&
        $self->has_attribute &&
        ( $self->attribute->name ne $self->attribute_name ) )
    {
        throw_exception( AttributeNamesDoNotMatch => attribute_name => $self->attribute_name,
                                                     attribute      => $self->attribute
                       );
    }
};

1;
