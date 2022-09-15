package MooseX::MethodAttributes::Role::Meta::Map;
# ABSTRACT: generic role for storing code attributes used by classes and roles with attributes

our $VERSION = '0.32';

use Moose::Role;

use namespace::autoclean;

has _method_attribute_map => (
    is      => 'ro',
    isa     => 'HashRef[ArrayRef[Str]]',
    lazy    => 1,
    default => sub {+{}},
);

has _method_attribute_list => (
    is      => 'ro',
    isa     => 'ArrayRef[Int]',
    lazy    => 1,
    default => sub {[]},
);

#pod =method register_method_attributes ($code, $attrs)
#pod
#pod Register a list of attributes for a code reference.
#pod
#pod =cut

sub register_method_attributes {
    my ($self, $code, $attrs) = @_;
    push @{$self->_method_attribute_list}, 0 + $code;
    $self->_method_attribute_map->{ 0 + $code } = $attrs;
    return;
}

#pod =method get_method_attributes ($code)
#pod
#pod Get a list of attributes associated with a coderef.
#pod
#pod =cut

sub get_method_attributes {
    my ($self, $code) = @_;
    return $self->_method_attribute_map->{ 0 + $code } || [];
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Map - generic role for storing code attributes used by classes and roles with attributes

=head1 VERSION

version 0.32

=head1 METHODS

=head2 register_method_attributes ($code, $attrs)

Register a list of attributes for a code reference.

=head2 get_method_attributes ($code)

Get a list of attributes associated with a coderef.

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
