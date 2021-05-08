package MooseX::MethodAttributes::Role::Meta::Method::Wrapped;
# ABSTRACT: wrapped metamethod role allowing code attribute introspection

our $VERSION = '0.32';

use Moose::Role;

use namespace::autoclean;

#pod =method attributes
#pod
#pod Gets the list of code attributes of the original method this meta method wraps.
#pod
#pod =cut

sub attributes {
    my ($self) = @_;
    return $self->get_original_method->attributes;
}

sub _get_attributed_coderef {
    my ($self) = @_;
    return $self->get_original_method->_get_attributed_coderef;
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Method::Wrapped - wrapped metamethod role allowing code attribute introspection

=head1 VERSION

version 0.32

=head1 METHODS

=head2 attributes

Gets the list of code attributes of the original method this meta method wraps.

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
