package MooseX::Role::Parameterized; # git description: v1.10-8-g9de4ac3
# ABSTRACT: Moose roles with composition parameters
# KEYWORDS: moose extension parameter role arguments dynamic parameterised parameterizable parameterisable

our $VERSION = '1.11';

use 5.008001;
use Moose 2.0300 ();
use Moose::Exporter;
use Carp 'confess';
use Moose::Util 'find_meta';
use namespace::clean 0.19;

use MooseX::Role::Parameterized::Meta::Trait::Parameterizable;

our $CURRENT_METACLASS;

sub current_metaclass {$CURRENT_METACLASS}

my $meta_lookup = sub {
    my $for = shift;
    current_metaclass() || find_meta($for);
};

Moose::Exporter->setup_import_methods(
    also           => 'Moose::Role',
    with_caller    => [ 'parameter', 'role' ],
    with_meta      => [ 'method', 'with' ],
    meta_lookup    => $meta_lookup,
    role_metaroles => {
        role => [ 'MooseX::Role::Parameterized::Meta::Trait::Parameterizable' ],
    },
);

sub parameter {
    my $caller = shift;

    confess "'parameter' may not be used inside of the role block"
        if current_metaclass && current_metaclass->genitor->name eq $caller;

    my $meta = find_meta($caller);

    my $names = shift;
    $names = [ $names ] if !ref($names);

    for my $name (@$names) {
        $meta->add_parameter($name => (
            is => 'ro',
            @_,
        ));
    }
}

sub role(&) {
    my $caller = shift;
    my $role_generator = shift;

    confess "'role' may not be used inside of the role block"
        if current_metaclass && current_metaclass->genitor->name eq $caller;

    find_meta($caller)->role_generator($role_generator);
}

sub method {
    my $meta = shift;
    my $name = shift;
    my $body = shift;

    my $method = $meta->method_metaclass->wrap(
        package_name => $meta->name,
        name         => $name,
        body         => $body,
    );

    $meta->add_method($name => $method);
}

sub with {
    local $CURRENT_METACLASS = undef;
    Moose::Role::with(@_);
}


1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Role::Parameterized - Moose roles with composition parameters

=head1 VERSION

version 1.11

=head1 SYNOPSIS

    package Counter;
    use MooseX::Role::Parameterized;

    parameter name => (
        isa      => 'Str',
        required => 1,
    );

    role {
        my $p = shift;

        my $name = $p->name;

        has $name => (
            is      => 'rw',
            isa     => 'Int',
            default => 0,
        );

        method "increment_$name" => sub {
            my $self = shift;
            $self->$name($self->$name + 1);
        };

        method "reset_$name" => sub {
            my $self = shift;
            $self->$name(0);
        };
    };

    package MyGame::Weapon;
    use Moose;

    with Counter => { name => 'enchantment' };

    package MyGame::Wand;
    use Moose;

    with Counter => { name => 'zapped' };

=head1 DESCRIPTION

Your parameterized role consists of two new things: parameter declarations
and a C<role> block.

Parameters are declared using the L</parameter> keyword which very much
resembles L<Moose/has>. You can use any option that L<Moose/has> accepts. The
default value for the C<is> option is C<ro> as that's a very common case. Use
C<< is => 'bare' >> if you want no accessor. These parameters will get their
values when the consuming class (or role) uses L<Moose/with>. A parameter
object will be constructed with these values, and passed to the C<role> block.

The C<role> block then uses the usual L<Moose::Role> keywords to build up a
role. You can shift off the parameter object to inspect what the consuming
class provided as parameters. You use the parameters to customize your
role however you wish.

There are many possible implementations for parameterized roles (hopefully with
a consistent enough API); I believe this to be the easiest and most flexible
design. Coincidentally, Pugs originally had an eerily similar design.

See L<MooseX::Role::Parameterized::Extending> for some tips on how to extend
this module.

=head2 Why a parameters object?

I've been asked several times "Why use a parameter I<object> and not just a
parameter I<hashref>? That would eliminate the need to explicitly declare your
parameters."

The benefits of using an object are similar to the benefits of using Moose. You
get an easy way to specify lazy defaults, type constraint, delegation, and so
on. You get to use MooseX modules.

=for Pod::Coverage current_metaclass method parameter role with

=head1 L<MooseX::Role::Parameterized::Tutorial>

B<Stop!> If you're new here, please read
L<MooseX::Role::Parameterized::Tutorial> for a much gentler introduction.

=for stopwords metaobject

You also get the usual introspective and intercessory abilities that come
standard with the metaobject protocol. Ambitious users should be able to add
traits to the parameters metaclass to further customize behavior. Please let
me know if you're doing anything viciously complicated with this extension. :)

=head1 CAVEATS

You must use this syntax to declare methods in the role block:
C<< method NAME => sub { ... }; >>. This is due to a limitation in Perl. In
return though you can use parameters I<in your methods>!

=head1 SEE ALSO

L<http://sartak.org/2009/01/parametric-roles-in-perl-5.html>

L<http://sartak.org/2009/05/the-design-of-parameterized-roles.html>

L<http://stevan-little.blogspot.com/2009/07/thoughts-on-parameterized-roles.html>

L<http://perldition.org/articles/Parameterized%20Roles%20with%20MooseX::Declare.pod>

L<http://www.modernperlbooks.com/mt/2011/01/the-parametric-role-of-my-mvc-plugin-system.html>

L<http://jjnapiorkowski.typepad.com/modern-perl/2010/08/parameterized-roles-and-method-traits-redo.html>

L<http://sartak.org/talks/yapc-asia-2009/(parameterized)-roles/>

=for stopwords Joose

L<https://github.com/SamuraiJack/JooseX-Role-Parameterized> - this extension ported to JavaScript's Joose

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Role-Parameterized>
(or L<bug-MooseX-Role-Parameterized@rt.cpan.org|mailto:bug-MooseX-Role-Parameterized@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Shawn M Moore <code@sartak.org>

=head1 CONTRIBUTORS

=for stopwords Karen Etheridge Dave Rolsky Jesse Luehrs Oliver Charles Yuval Kogman Robert 'phaylon' Sedlacek Florian Ragwitz Mark Fowler Chris Weyl Csson Andy Jack Ricardo Signes Todd Hepler

=over 4

=item *

Karen Etheridge <ether@cpan.org>

=item *

Dave Rolsky <autarch@urth.org>

=item *

Jesse Luehrs <doy@tozt.net>

=item *

Oliver Charles <oliver.g.charles@googlemail.com>

=item *

Yuval Kogman <nothingmuch@woobling.org>

=item *

Robert 'phaylon' Sedlacek <rs@474.at>

=item *

Florian Ragwitz <rafl@debian.org>

=item *

Mark Fowler <mark@twoshortplanks.com>

=item *

Chris Weyl <cweyl@alumni.drew.edu>

=item *

Csson <erik.carlsson@live.com>

=item *

Andy Jack <github@veracity.ca>

=item *

Ricardo Signes <rjbs@cpan.org>

=item *

Todd Hepler <thepler@employees.org>

=back

=head1 COPYRIGHT AND LICENSE

This software is copyright (c) 2008 by Shawn M Moore.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
