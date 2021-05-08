package MooseX::MethodAttributes::Role::Meta::Role::Application;
# ABSTRACT: generic role for applying a role with method attributes to something

our $VERSION = '0.32';

use Moose::Role;
use Moose::Util qw/find_meta/;
use MooseX::MethodAttributes ();
use MooseX::MethodAttributes::Role ();
use Carp qw/croak/;
use namespace::autoclean;

requires qw/
    _copy_attributes
    apply
/;

#pod =method apply
#pod
#pod The apply method is wrapped to ensure that the correct metaclasses to hold and propagate
#pod method attribute data are present on the target for role application, delegates to
#pod the original method to actually apply the role, then ensures that any attributes from
#pod the role are copied to the target class.
#pod
#pod =cut

around 'apply' => sub {
    my ($orig, $self, $thing, %opts) = @_;
    $thing = $self->_apply_metaclasses($thing);

    my $ret = $self->$orig($thing, %opts);

    $self->_copy_attributes($thing);

    return $ret;
};

sub _apply_metaclasses {
    my ($self, $thing) = @_;
    if ($thing->isa('Moose::Meta::Class')) {
        $thing = MooseX::MethodAttributes->init_meta(for_class => $thing->name);
    }
    elsif ($thing->isa('Moose::Meta::Role')) {
        $thing = MooseX::MethodAttributes::Role->init_meta(for_class => $thing->name);
    }
    else {
        croak("Composing " . __PACKAGE__ . " onto instances is unsupported");
    }

    # Note that the metaclass instance we started out with may have been turned
    # into lies by the metatrait role application process, so we explicitly
    # re-fetch it here.

    return find_meta($thing->name);
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Role::Application - generic role for applying a role with method attributes to something

=head1 VERSION

version 0.32

=head1 METHODS

=head2 apply

The apply method is wrapped to ensure that the correct metaclasses to hold and propagate
method attribute data are present on the target for role application, delegates to
the original method to actually apply the role, then ensures that any attributes from
the role are copied to the target class.

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
