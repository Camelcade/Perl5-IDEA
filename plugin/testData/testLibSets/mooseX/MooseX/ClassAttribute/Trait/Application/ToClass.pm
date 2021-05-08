package MooseX::ClassAttribute::Trait::Application::ToClass;

use strict;
use warnings;

our $VERSION = '0.29';

use namespace::autoclean;
use Moose::Role;

with 'MooseX::ClassAttribute::Trait::Application';

around apply => sub {
    my $orig = shift;
    my $self = shift;
    my $role = shift;
    my $class = shift;

    $class = Moose::Util::MetaRole::apply_metaroles(
        for             => $class,
        class_metaroles => {
            class => [ 'MooseX::ClassAttribute::Trait::Class' ],
        },
    );

    $self->$orig($role, $class);
};

sub _apply_class_attributes {
    my $self = shift;
    my $role = shift;
    my $class = shift;

    my $attr_metaclass = $class->attribute_metaclass();

    foreach my $attribute_name ($role->get_class_attribute_list()) {
        if ($class->has_class_attribute($attribute_name)
            && $class->get_class_attribute($attribute_name)
            != $role->get_class_attribute($attribute_name)) {
            next;
        }
        else {
            $class->add_class_attribute(
                $role->get_class_attribute($attribute_name)
                    ->attribute_for_class($attr_metaclass));
        }
    }
}

1;

# ABSTRACT: A trait that supports applying class attributes to classes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Application::ToClass - A trait that supports applying class attributes to classes

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This trait is used to allow the application of roles containing class
attributes to classes.

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
