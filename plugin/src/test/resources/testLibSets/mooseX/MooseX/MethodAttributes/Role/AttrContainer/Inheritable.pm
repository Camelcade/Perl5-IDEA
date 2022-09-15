package MooseX::MethodAttributes::Role::AttrContainer::Inheritable;
# ABSTRACT: capture code attributes in the automatically initialized metaclass instance

our $VERSION = '0.32';

#pod =head1 DESCRIPTION
#pod
#pod This role extends C<MooseX::MethodAttributes::Role::AttrContainer> with the
#pod functionality of automatically initializing a metaclass for the caller and
#pod applying the meta roles relevant for capturing method attributes.
#pod
#pod =cut

use Moose::Role;
use MooseX::MethodAttributes ();

use namespace::autoclean;

with 'MooseX::MethodAttributes::Role::AttrContainer';

before MODIFY_CODE_ATTRIBUTES => sub {
    my ($class, $code, @attrs) = @_;
    return unless @attrs;
    MooseX::MethodAttributes->init_meta(for_class => $class);
};

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::AttrContainer::Inheritable - capture code attributes in the automatically initialized metaclass instance

=head1 VERSION

version 0.32

=head1 DESCRIPTION

This role extends C<MooseX::MethodAttributes::Role::AttrContainer> with the
functionality of automatically initializing a metaclass for the caller and
applying the meta roles relevant for capturing method attributes.

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
