package Moose::Meta::Role::Method;
our $VERSION = '2.2101';

use strict;
use warnings;

use parent 'Moose::Meta::Method';

sub _make_compatible_with {
    my $self = shift;
    my ($other) = @_;

    # XXX: this is pretty gross. the issue here is blah blah blah
    # see the comments in CMOP::Method::Meta and CMOP::Method::Wrapped
    return $self unless $other->_is_compatible_with($self->_real_ref_name);

    return $self->SUPER::_make_compatible_with(@_);
}

1;

# ABSTRACT: A Moose Method metaclass for Roles

__END__

=pod

=head1 DESCRIPTION

This is primarily used to mark methods coming from a role
as being different. Right now it is nothing but a subclass
of L<Moose::Meta::Method>.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
