package Moose::Meta::Method::Meta;
our $VERSION = '2.2101';

use strict;
use warnings;

use parent 'Moose::Meta::Method',
         'Class::MOP::Method::Meta';

sub _is_caller_mop_internal {
    my $self = shift;
    my ($caller) = @_;
    return 1 if $caller =~ /^Moose(?:::|$)/;
    return $self->SUPER::_is_caller_mop_internal($caller);
}

# XXX: ugh multiple inheritance
sub wrap {
    my $class = shift;
    return $class->Class::MOP::Method::Meta::wrap(@_);
}

sub _make_compatible_with {
    my $self = shift;
    return $self->Class::MOP::Method::Meta::_make_compatible_with(@_);
}

1;

# ABSTRACT: A Moose Method metaclass for C<meta> methods

__END__

=pod

=head1 DESCRIPTION

This class is a subclass of L<Class::MOP::Method::Meta> that
provides additional Moose-specific functionality, all of which is
private.

To understand this class, you should read the
L<Class::MOP::Method::Meta> documentation.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
