package Moose::Meta::Role::Method::Required;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use overload
    '""' => sub { shift->name },   # stringify to method name
    'bool' => sub { 1 },
    fallback => 1;

use parent 'Class::MOP::Object';

# This is not a Moose::Meta::Role::Method because it has no implementation, it
# is just a name

__PACKAGE__->meta->add_attribute('name' => (
    reader   => 'name',
    required => 1,
    Class::MOP::_definition_context(),
));

sub new { shift->_new(@_) }

1;

# ABSTRACT: A Moose metaclass for required methods in Roles

__END__

=pod

=head1 DESCRIPTION

=head1 INHERITANCE

C<Moose::Meta::Role::Method::Required> is a subclass of L<Class::MOP::Object>.
It is B<not> a subclass of C<Moose::Meta::Role::Method> since it does not
provide an implementation of the method.

=head1 METHODS

=head2 Moose::Meta::Role::Method::Required->new(%options)

This creates a new type constraint based on the provided C<%options>:

=over 4

=item * name

The method name. This is required.

=back

=head2 $method->name

Returns the required method's name, as provided to the constructor.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
