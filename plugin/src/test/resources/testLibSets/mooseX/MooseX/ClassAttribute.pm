package MooseX::ClassAttribute;

use strict;
use warnings;

our $VERSION = '0.29';

# This module doesn't really need these pragmas - this is just for the benefit
# of prereq scanning.
use namespace::clean 0.20 ();
use namespace::autoclean 0.11 ();

use Moose 2.00 ();
use Moose::Exporter;
use Moose::Util;
use MooseX::ClassAttribute::Trait::Class;
use MooseX::ClassAttribute::Trait::Role;
use MooseX::ClassAttribute::Trait::Application::ToClass;
use MooseX::ClassAttribute::Trait::Application::ToRole;

Moose::Exporter->setup_import_methods(
    with_meta       => [ 'class_has' ],
    class_metaroles => {
        class => [ 'MooseX::ClassAttribute::Trait::Class' ],
    },
    role_metaroles  => {
        role                 => [ 'MooseX::ClassAttribute::Trait::Role' ],
        application_to_class =>
            [ 'MooseX::ClassAttribute::Trait::Application::ToClass' ],
        application_to_role  =>
            [ 'MooseX::ClassAttribute::Trait::Application::ToRole' ],
    },
);

sub class_has {
    my $meta = shift;
    my $name = shift;

    my $attrs = ref $name eq 'ARRAY' ? $name : [ $name ];

    my %options = (definition_context => _caller_info(), @_);

    $meta->add_class_attribute($_, %options) for @{$attrs};
}

# Copied from Moose::Util in 2.06
sub _caller_info {
    my $level = @_ ? ($_[0] + 1) : 2;
    my %info;
    @info{qw(package file line)} = caller($level);
    return \%info;
}

1;

# ABSTRACT: Declare class attributes Moose-style

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute - Declare class attributes Moose-style

=head1 VERSION

version 0.29

=head1 SYNOPSIS

    package My::Class;

    use Moose;
    use MooseX::ClassAttribute;

    class_has 'Cache' =>
        ( is      => 'rw',
          isa     => 'HashRef',
          default => sub { {} },
        );

    __PACKAGE__->meta()->make_immutable();

    no Moose;
    no MooseX::ClassAttribute;

    # then later ...

    My::Class->Cache()->{thing} = ...;

=head1 DESCRIPTION

This module allows you to declare class attributes in exactly the same
way as object attributes, using C<class_has()> instead of C<has()>.

You can use any feature of Moose's attribute declarations, including
overriding a parent's attributes, delegation (C<handles>), attribute traits,
etc. All features should just work. The one exception is the "required" flag,
which is not allowed for class attributes.

The accessor methods for class attribute may be called on the class
directly, or on objects of that class. Passing a class attribute to
the constructor will not set that attribute.

=head1 FUNCTIONS

This class exports one function when you use it, C<class_has()>. This
works exactly like Moose's C<has()>, but it declares class attributes.

One little nit is that if you include C<no Moose> in your class, you won't
remove the C<class_has()> function. To do that you must include C<no
MooseX::ClassAttribute> as well. Or you can just use L<namespace::autoclean>
instead.

=head2 Implementation and Immutability

This module will add a role to your class's metaclass, See
L<MooseX::ClassAttribute::Trait::Class> for details. This role
provides introspection methods for class attributes.

Class attributes themselves do the
L<MooseX::ClassAttribute::Trait::Attribute> role.

=head2 Cooperation with Metaclasses and Traits

This module should work with most attribute metaclasses and traits,
but it's possible that conflicts could occur. This module has been
tested to work with Moose's native traits.

=head2 Class Attributes in Roles

You can add a class attribute to a role. When that role is applied to a class,
the class will have the relevant class attributes added. Note that attribute
defaults will be calculated when the class attribute is composed into the
class.

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|http://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-ClassAttribute>
(or L<bug-moosex-classattribute@rt.cpan.org|mailto:bug-moosex-classattribute@rt.cpan.org>).

I am also usually active on IRC as 'drolsky' on C<irc://irc.perl.org>.

=head1 DONATIONS

If you'd like to thank me for the work I've done on this module, please
consider making a "donation" to me via PayPal. I spend a lot of free time
creating free software, and would appreciate any support you'd care to offer.

Please note that B<I am not suggesting that you must do this> in order for me
to continue working on this particular software. I will continue to do so,
inasmuch as I have in the past, for as long as it interests me.

Similarly, a donation made in this way will probably not make me work on this
software much more, unless I get so many donations that I can consider working
on free software full time (let's all have a chuckle at that together).

To donate, log into PayPal and send money to autarch@urth.org, or use the
button at L<http://www.urth.org/~autarch/fs-donation.html>.

=head1 AUTHOR

Dave Rolsky <autarch@urth.org>

=head1 CONTRIBUTORS

=for stopwords Andrew Rodland Karen Etheridge Rafael Kitover Robert Buels Shawn M Moore

=over 4

=item *

Andrew Rodland <andrew@cleverdomain.org>

=item *

Karen Etheridge <ether@cpan.org>

=item *

Rafael Kitover <rkitover@cpan.org>

=item *

Robert Buels <rmb32@cornell.edu>

=item *

Shawn M Moore <sartak@gmail.com>

=back

=head1 COPYRIGHT AND LICENCE

This software is Copyright (c) 2016 by Dave Rolsky.

This is free software, licensed under:

  The Artistic License 2.0 (GPL Compatible)

=cut
