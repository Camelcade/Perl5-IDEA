package MooseX::MethodAttributes; # git description: v0.31-13-g5621366
# ABSTRACT: Code attribute introspection

our $VERSION = '0.32';

use Moose ();
use Moose::Exporter;
use Moose::Util::MetaRole;
use Moose::Util qw/find_meta does_role/;
# Ensure trait is registered
use MooseX::MethodAttributes::Role::Meta::Role ();
use namespace::autoclean;

#pod =head1 SYNOPSIS
#pod
#pod     package MyClass;
#pod
#pod     use Moose;
#pod     use MooseX::MethodAttributes;
#pod
#pod     sub foo : Bar Baz('corge') { ... }
#pod
#pod     my $attrs = MyClass->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]
#pod
#pod =head1 DESCRIPTION
#pod
#pod This module allows code attributes of methods to be introspected using Moose
#pod meta method objects.
#pod
#pod =begin Pod::Coverage
#pod
#pod init_meta
#pod
#pod =end Pod::Coverage
#pod
#pod =cut

Moose::Exporter->setup_import_methods(
    also => 'Moose',
);

sub init_meta {
    my ($class, %options) = @_;

    my $for_class = $options{for_class};
    my $meta = find_meta($for_class);

    return $meta if $meta
        && does_role($meta, 'MooseX::MethodAttributes::Role::Meta::Class')
        && does_role($meta->method_metaclass, 'MooseX::MethodAttributes::Role::Meta::Method')
        && does_role($meta->wrapped_method_metaclass, 'MooseX::MethodAttributes::Role::Meta::Method::MaybeWrapped');

    $meta = Moose::Meta::Class->create($for_class)
        unless $meta;

    $meta = Moose::Util::MetaRole::apply_metaroles(
        for             => $for_class,
        class_metaroles => {
            class          => [ 'MooseX::MethodAttributes::Role::Meta::Class' ],
            method         => [ 'MooseX::MethodAttributes::Role::Meta::Method' ],
            wrapped_method => [
                'MooseX::MethodAttributes::Role::Meta::Method::MaybeWrapped' ],
        },
    );

    Moose::Util::MetaRole::apply_base_class_roles(
        for_class => $for_class,
        roles     => [ 'MooseX::MethodAttributes::Role::AttrContainer' ],
    );

    return $meta;
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes - Code attribute introspection

=head1 VERSION

version 0.32

=head1 SYNOPSIS

    package MyClass;

    use Moose;
    use MooseX::MethodAttributes;

    sub foo : Bar Baz('corge') { ... }

    my $attrs = MyClass->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]

=head1 DESCRIPTION

This module allows code attributes of methods to be introspected using Moose
meta method objects.

=for Pod::Coverage init_meta

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-MethodAttributes>
(or L<bug-MooseX-MethodAttributes@rt.cpan.org|mailto:bug-MooseX-MethodAttributes@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHORS

=over 4

=item *

Florian Ragwitz <rafl@debian.org>

=item *

Tomas Doran <bobtfish@bobtfish.net>

=back

=head1 CONTRIBUTORS

=for stopwords Karen Etheridge Dave Rolsky Marcus Ramberg Graham Knop Peter E Karman David Steinbrunner

=over 4

=item *

Karen Etheridge <ether@cpan.org>

=item *

Dave Rolsky <autarch@urth.org>

=item *

Marcus Ramberg <marcus@nordaaker.com>

=item *

Graham Knop <haarg@haarg.org>

=item *

Peter E Karman <pek@dewpoint.msi.umn.edu>

=item *

David Steinbrunner <dsteinbrunner@pobox.com>

=back

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2009 by Florian Ragwitz.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
