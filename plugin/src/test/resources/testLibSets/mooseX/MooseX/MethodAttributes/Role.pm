package MooseX::MethodAttributes::Role;
# ABSTRACT: code attribute introspection

our $VERSION = '0.32';

use Moose ();
use Moose::Exporter;
use Moose::Util::MetaRole;
use Moose::Util qw/find_meta does_role ensure_all_roles/;
# Ensure trait is registered
use MooseX::MethodAttributes::Role::Meta::Role ();
use namespace::autoclean;

#pod =head1 SYNOPSIS
#pod
#pod     package MyRole;
#pod     use MooseX::MethodAttributes::Role;
#pod
#pod     sub foo : Bar Baz('corge') { ... }
#pod
#pod     my $attrs = MyRole->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]
#pod
#pod =head1 DESCRIPTION
#pod
#pod This module allows you to write a Moose Role with code attributes of methods to
#pod be introspected using Moose meta method objects.
#pod
#pod =begin Pod::Coverage
#pod
#pod init_meta
#pod
#pod =end Pod::Coverage
#pod
#pod =cut

Moose::Exporter->setup_import_methods(also => 'Moose::Role');

sub init_meta {
    my ($class, %options) = @_;

    my $for_class = $options{for_class};
    my $meta = find_meta($for_class);

    return $meta if $meta
        && does_role($meta, 'MooseX::MethodAttributes::Role::Meta::Role');

    $meta = Moose::Meta::Role->create($for_class)
        unless $meta;

    $meta = Moose::Util::MetaRole::apply_metaroles(
        for            => $meta->name,
        role_metaroles => {
            role => [ 'MooseX::MethodAttributes::Role::Meta::Role' ],
        },
    );

    ensure_all_roles($meta->name,
        'MooseX::MethodAttributes::Role::AttrContainer',
    );

    return $meta;
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role - code attribute introspection

=head1 VERSION

version 0.32

=head1 SYNOPSIS

    package MyRole;
    use MooseX::MethodAttributes::Role;

    sub foo : Bar Baz('corge') { ... }

    my $attrs = MyRole->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]

=head1 DESCRIPTION

This module allows you to write a Moose Role with code attributes of methods to
be introspected using Moose meta method objects.

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

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2009 by Florian Ragwitz.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
