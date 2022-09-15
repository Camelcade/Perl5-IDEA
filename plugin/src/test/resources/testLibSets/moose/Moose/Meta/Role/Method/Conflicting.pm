package Moose::Meta::Role::Method::Conflicting;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Util;

use parent 'Moose::Meta::Role::Method::Required';

__PACKAGE__->meta->add_attribute('roles' => (
    reader   => 'roles',
    required => 1,
    Class::MOP::_definition_context(),
));

sub roles_as_english_list {
    my $self = shift;
    Moose::Util::english_list( map { q{'} . $_ . q{'} } @{ $self->roles } );
}

1;

# ABSTRACT: A Moose metaclass for conflicting methods in Roles

__END__

=pod

=head1 DESCRIPTION

=head1 INHERITANCE

C<Moose::Meta::Role::Method::Conflicting> is a subclass of
L<Moose::Meta::Role::Method::Required>.

=head1 METHODS

=head2 Moose::Meta::Role::Method::Conflicting->new(%options)

This creates a new type constraint based on the provided C<%options>:

=over 4

=item * name

The method name. This is required.

=item * roles

The list of role names that generated the conflict. This is required.

=back

=head2 $method->name

Returns the conflicting method's name, as provided to the constructor.

=head2 $method->roles

Returns the roles that generated this conflicting method, as provided to the
constructor.

=head2 $method->roles_as_english_list

Returns the roles that generated this conflicting method as an English list.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
