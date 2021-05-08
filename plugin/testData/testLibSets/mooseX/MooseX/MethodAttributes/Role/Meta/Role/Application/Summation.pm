package MooseX::MethodAttributes::Role::Meta::Role::Application::Summation;
# ABSTRACT: Role applied to the class responsible for role summation which ensures method attributes propagate from the roles being summed onto the combined role.

our $VERSION = '0.32';

use Moose::Role;
use Moose::Util qw/does_role/;
use namespace::autoclean;

with 'MooseX::MethodAttributes::Role::Meta::Role::Application';

sub _copy_attributes {
    my ($self, $thing) = @_;
    for my $role (@{$self->get_roles}) {
        $role->_copy_attributes($thing)
            if does_role($role, 'MooseX::MethodAttributes::Role::Meta::Role');
    }
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Role::Application::Summation - Role applied to the class responsible for role summation which ensures method attributes propagate from the roles being summed onto the combined role.

=head1 VERSION

version 0.32

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
