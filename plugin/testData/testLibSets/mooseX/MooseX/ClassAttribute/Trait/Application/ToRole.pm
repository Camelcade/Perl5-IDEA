package MooseX::ClassAttribute::Trait::Application::ToRole;

use strict;
use warnings;

our $VERSION = '0.29';

use Moose::Util::MetaRole;
use MooseX::ClassAttribute::Trait::Application::ToClass;

use namespace::autoclean;
use Moose::Role;

with 'MooseX::ClassAttribute::Trait::Application';

around apply => sub {
    my $orig = shift;
    my $self = shift;
    my $role1 = shift;
    my $role2 = shift;

    $role2 = Moose::Util::MetaRole::apply_metaroles(
        for            => $role2,
        role_metaroles => {
            role                 => [ 'MooseX::ClassAttribute::Trait::Role' ],
            application_to_class =>
                [ 'MooseX::ClassAttribute::Trait::Application::ToClass' ],
            application_to_role  =>
                [ 'MooseX::ClassAttribute::Trait::Application::ToRole' ],
        },
    );

    $self->$orig($role1, $role2);
};

sub _apply_class_attributes {
    my $self = shift;
    my $role1 = shift;
    my $role2 = shift;

    foreach my $attribute_name ($role1->get_class_attribute_list()) {
        if ($role2->has_class_attribute($attribute_name)
            && $role2->get_class_attribute($attribute_name)
            != $role1->get_class_attribute($attribute_name)) {

            require Moose;
            Moose->throw_error("Role '"
                . $role1->name()
                . "' has encountered a class attribute conflict "
                . "during composition. This is fatal error and cannot be disambiguated."
            );
        }
        else {
            $role2->add_class_attribute(
                $role1->get_class_attribute($attribute_name)->clone());
        }
    }
}

1;

# ABSTRACT: A trait that supports applying class attributes to roles

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Application::ToRole - A trait that supports applying class attributes to roles

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This trait is used to allow the application of roles containing class
attributes to roles.

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
