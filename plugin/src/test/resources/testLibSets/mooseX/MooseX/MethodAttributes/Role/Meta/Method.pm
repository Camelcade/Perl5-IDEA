package MooseX::MethodAttributes::Role::Meta::Method;
# ABSTRACT: metamethod role allowing code attribute introspection

our $VERSION = '0.32';

use Moose::Role;

use namespace::autoclean;

#pod =attr attributes
#pod
#pod Gets the list of code attributes of the method represented by this meta method.
#pod
#pod =cut

has attributes => (
    is      => 'ro',
    lazy    => 1,
    builder => '_build_attributes',
);

#pod =method _build_attributes
#pod
#pod Builds the value of the C<attributes> attribute based on the attributes
#pod captured in the associated meta class.
#pod
#pod =cut

sub _build_attributes {
    my ($self) = @_;
    return $self->associated_metaclass->get_method_attributes($self->_get_attributed_coderef);
}

sub _get_attributed_coderef {
    my ($self) = @_;
    return $self->body;
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Method - metamethod role allowing code attribute introspection

=head1 VERSION

version 0.32

=head1 ATTRIBUTES

=head2 attributes

Gets the list of code attributes of the method represented by this meta method.

=head1 METHODS

=head2 _build_attributes

Builds the value of the C<attributes> attribute based on the attributes
captured in the associated meta class.

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
