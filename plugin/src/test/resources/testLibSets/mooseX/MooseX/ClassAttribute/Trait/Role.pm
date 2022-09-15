package MooseX::ClassAttribute::Trait::Role;

use strict;
use warnings;

our $VERSION = '0.29';

use MooseX::ClassAttribute::Meta::Role::Attribute;
use Scalar::Util qw(blessed);

use namespace::autoclean;
use Moose::Role;

with 'MooseX::ClassAttribute::Trait::Mixin::HasClassAttributes';

around add_class_attribute => sub {
    my $orig = shift;
    my $self = shift;
    my $attr = (
        blessed $_[0] && $_[0]->isa('Class::MOP::Mixin::AttributeCore')
            ? $_[0]
            : MooseX::ClassAttribute::Meta::Role::Attribute->new(@_)
    );

    $self->$orig($attr);

    return $attr;
};

sub _attach_class_attribute {
    my ($self, $attribute) = @_;

    $attribute->attach_to_role($self);
}

sub composition_class_roles {
    return 'MooseX::ClassAttribute::Trait::Role::Composite';
}

1;

# ABSTRACT: A trait for roles with class attributes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Role - A trait for roles with class attributes

=head1 VERSION

version 0.29

=head1 SYNOPSIS

  for my $attr ( HasClassAttributes->meta()->get_all_class_attributes() )
  {
      print $attr->name();
  }

=head1 DESCRIPTION

This role adds awareness of class attributes to a role metaclass object. It
provides a set of introspection methods that largely parallel the existing
attribute methods, except they operate on class attributes.

=head1 METHODS

Every method provided by this role has an analogous method in
C<Class::MOP::Class> or C<Moose::Meta::Class> for regular attributes.

=head2 $meta->has_class_attribute($name)

=head2 $meta->get_class_attribute($name)

=head2 $meta->get_class_attribute_list()

These methods are exactly like their counterparts in
L<MooseX::ClassAttribute::Trait::Class>.

=head2 $meta->add_class_attribute(...)

This accepts the same options as the L<Moose::Meta::Attribute>
C<add_attribute()> method. However, if an attribute is specified as
"required" an error will be thrown.

=head2 $meta->remove_class_attribute($name)

If the named class attribute exists, it is removed from the role.

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
