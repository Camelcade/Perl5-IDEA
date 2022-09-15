package MooseX::Role::Parameterized::Meta::Role::Parameterized;
# ABSTRACT: metaclass for parameterized roles

our $VERSION = '1.11';

use Moose;
extends 'Moose::Meta::Role';
with 'MooseX::Role::Parameterized::Meta::Trait::Parameterized';

__PACKAGE__->meta->make_immutable;
no Moose;

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Role::Parameterized::Meta::Role::Parameterized - metaclass for parameterized roles

=head1 VERSION

version 1.11

=head1 DESCRIPTION

This is the metaclass for parameterized roles; that is, parameterizable roles
with their parameters bound. See
L<MooseX::Role::Parameterized::Meta::Trait::Parameterized> which has all the guts.

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Role-Parameterized>
(or L<bug-MooseX-Role-Parameterized@rt.cpan.org|mailto:bug-MooseX-Role-Parameterized@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Shawn M Moore <code@sartak.org>

=head1 COPYRIGHT AND LICENSE

This software is copyright (c) 2008 by Shawn M Moore.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
