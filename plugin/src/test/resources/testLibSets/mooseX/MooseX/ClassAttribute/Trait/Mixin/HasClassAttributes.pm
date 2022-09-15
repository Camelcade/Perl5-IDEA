package MooseX::ClassAttribute::Trait::Mixin::HasClassAttributes;

use strict;
use warnings;

our $VERSION = '0.29';

use namespace::autoclean;
use Moose::Role;

has _class_attribute_map => (
    traits   => [ 'Hash' ],
    is       => 'ro',
    isa      => 'HashRef[Class::MOP::Mixin::AttributeCore]',
    handles  => {
        '_add_class_attribute'     => 'set',
        'has_class_attribute'      => 'exists',
        'get_class_attribute'      => 'get',
        '_remove_class_attribute'  => 'delete',
        'get_class_attribute_list' => 'keys',
    },
    default  => sub {{}},
    init_arg => undef,
);

# deprecated
sub get_class_attribute_map {
    return $_[0]->_class_attribute_map();
}

sub add_class_attribute {
    my $self = shift;
    my $attribute = shift;

    ($attribute->isa('Class::MOP::Mixin::AttributeCore'))
        || confess
        "Your attribute must be an instance of Class::MOP::Mixin::AttributeCore (or a subclass)";

    $self->_attach_class_attribute($attribute);

    my $attr_name = $attribute->name;

    $self->remove_class_attribute($attr_name)
        if $self->has_class_attribute($attr_name);

    my $order = (scalar keys %{$self->_attribute_map});
    $attribute->_set_insertion_order($order);

    $self->_add_class_attribute($attr_name => $attribute);

    # This method is called to allow for installing accessors. Ideally, we'd
    # use method overriding, but then the subclass would be responsible for
    # making the attribute, which would end up with lots of code
    # duplication. Even more ideally, we'd use augment/inner, but this is
    # Class::MOP!
    $self->_post_add_class_attribute($attribute)
        if $self->can('_post_add_class_attribute');

    return $attribute;
}

sub remove_class_attribute {
    my $self = shift;
    my $name = shift;

    (defined $name && $name)
        || confess 'You must provide an attribute name';

    my $removed_attr = $self->get_class_attribute($name);
    return unless $removed_attr;

    $self->_remove_class_attribute($name);

    return $removed_attr;
}

1;

# ABSTRACT: A mixin trait for things which have class attributes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Mixin::HasClassAttributes - A mixin trait for things which have class attributes

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This trait is like L<Class::MOP::Mixin::HasAttributes>, except that it works
with class attributes instead of object attributes.

See L<MooseX::ClassAttribute::Trait::Class> and
L<MooseX::ClassAttribute::Trait::Role> for API details.

=head1 BUGS

See L<MooseX::ClassAttribute> for details.

Bugs may be submitted through L<the RT bug tracker|http://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-ClassAttribute>
(or L<bug-moosex-classattribute@rt.cpan.org|mailto:bug-moosex-classattribute@rt.cpan.org>).

I am also usually active on IRC as 'drolsky' on C<irc://irc.perl.org>.

=head1 AUTHOR

Dave Rolsky <autarch@urth.org>

=head1 COPYRIGHT AND LICENCE

This software is Copyright (c) 2016 by Dave Rolsky.

This is free software, licensed under:

  The Artistic License 2.0 (GPL Compatible)

=cut
