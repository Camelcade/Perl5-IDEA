package Moose::Meta::Instance;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP::MiniTrait;

use parent 'Class::MOP::Instance';

Class::MOP::MiniTrait::apply(__PACKAGE__, 'Moose::Meta::Object::Trait');

1;

# ABSTRACT: The Moose Instance metaclass

__END__

=pod

=head1 SYNOPSIS

    # nothing to see here

=head1 DESCRIPTION

This class provides the low level data storage abstractions for
attributes.

Using this API directly in your own code violates encapsulation, and
we recommend that you use the appropriate APIs in
L<Moose::Meta::Class> and L<Moose::Meta::Attribute> instead. Those
APIs in turn call the methods in this class as appropriate.

At present, this is an empty subclass of L<Class::MOP::Instance>, so
you should see that class for all API details.

=head1 INHERITANCE

C<Moose::Meta::Instance> is a subclass of L<Class::MOP::Instance>.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
