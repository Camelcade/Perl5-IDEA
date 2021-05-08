package MooseX::MethodAttributes::Role::AttrContainer;
# ABSTRACT: capture code attributes in the class' metaclass

our $VERSION = '0.32';

use Moose::Role;
use Moose::Util qw/find_meta/;

use namespace::autoclean;

#pod =method MODIFY_CODE_ATTRIBUTES ($code, @attrs)
#pod
#pod Accepts a list of attributes for a coderef and stores it the class' metaclass.
#pod
#pod See L<attributes>.
#pod
#pod =cut

sub MODIFY_CODE_ATTRIBUTES {
    my ($class, $code, @attrs) = @_;
    find_meta($class)->register_method_attributes($code, \@attrs);
    return ();
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::AttrContainer - capture code attributes in the class' metaclass

=head1 VERSION

version 0.32

=head1 METHODS

=head2 MODIFY_CODE_ATTRIBUTES ($code, @attrs)

Accepts a list of attributes for a coderef and stores it the class' metaclass.

See L<attributes>.

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
