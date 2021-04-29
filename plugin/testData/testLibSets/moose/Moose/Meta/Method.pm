package Moose::Meta::Method;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP::MiniTrait;

use parent 'Class::MOP::Method';

Class::MOP::MiniTrait::apply(__PACKAGE__, 'Moose::Meta::Object::Trait');

1;

# ABSTRACT: A Moose Method metaclass

__END__

=pod

=head1 DESCRIPTION

This class is a subclass of L<Class::MOP::Method> that provides
additional Moose-specific functionality, all of which is private.

To understand this class, you should read the L<Class::MOP::Method>
documentation.

=head1 INHERITANCE

C<Moose::Meta::Method> is a subclass of L<Class::MOP::Method>.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
