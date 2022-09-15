package MooseX::MethodAttributes::Inheritable;
# ABSTRACT: inheritable code attribute introspection

our $VERSION = '0.32';

#pod =head1 SYNOPSIS
#pod
#pod     package BaseClass;
#pod     use base qw/MooseX::MethodAttributes::Inheritable/;
#pod
#pod     package SubClass;
#pod     use base qw/BaseClass/;
#pod
#pod     sub foo : Bar {}
#pod
#pod     my $attrs = SubClass->meta->get_method('foo')->attributes; # ["Bar"]
#pod
#pod =head1 DESCRIPTION
#pod
#pod This module does the same as C<MooseX::MethodAttributes>, except that classes
#pod inheriting from other classes using it don't need to do anything special to get
#pod their code attributes captured.
#pod
#pod Note that instead of inheriting from this module, you can instead simply
#pod compose (C<with>) the L<MooseX::MethodAttributes::Role::AttrContainer::Inheritable> role.
#pod
#pod =cut

# Ensure trait is registered
use MooseX::MethodAttributes::Role::Meta::Role ();

use Moose;

use namespace::autoclean;

with 'MooseX::MethodAttributes::Role::AttrContainer::Inheritable';

__PACKAGE__->meta->make_immutable;
1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Inheritable - inheritable code attribute introspection

=head1 VERSION

version 0.32

=head1 SYNOPSIS

    package BaseClass;
    use base qw/MooseX::MethodAttributes::Inheritable/;

    package SubClass;
    use base qw/BaseClass/;

    sub foo : Bar {}

    my $attrs = SubClass->meta->get_method('foo')->attributes; # ["Bar"]

=head1 DESCRIPTION

This module does the same as C<MooseX::MethodAttributes>, except that classes
inheriting from other classes using it don't need to do anything special to get
their code attributes captured.

Note that instead of inheriting from this module, you can instead simply
compose (C<with>) the L<MooseX::MethodAttributes::Role::AttrContainer::Inheritable> role.

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
