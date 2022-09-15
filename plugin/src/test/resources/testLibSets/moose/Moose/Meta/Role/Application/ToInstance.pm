package Moose::Meta::Role::Application::ToInstance;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use Scalar::Util 'blessed';
use List::Util 1.33 'all';
use Devel::OverloadInfo 0.004 'is_overloaded';

use parent 'Moose::Meta::Role::Application';

__PACKAGE__->meta->add_attribute('rebless_params' => (
    reader  => 'rebless_params',
    default => sub { {} },
    Class::MOP::_definition_context(),
));

use constant _NEED_OVERLOAD_HACK_FOR_OBJECTS => "$]" < 5.008009;

sub apply {
    my ( $self, $role, $object, $args ) = @_;

    my $obj_meta = Class::MOP::class_of($object) || 'Moose::Meta::Class';

    # This is a special case to handle the case where the object's metaclass
    # is a Class::MOP::Class, but _not_ a Moose::Meta::Class (for example,
    # when applying a role to a Moose::Meta::Attribute object).
    $obj_meta = 'Moose::Meta::Class'
        unless $obj_meta->isa('Moose::Meta::Class');

    my $class = $obj_meta->create_anon_class(
        superclasses => [ blessed($object) ],
        roles => [ $role, keys(%$args) ? ($args) : () ],
        cache => (all { $_ eq '-alias' || $_ eq '-excludes' } keys %$args),
    );

    $class->rebless_instance( $object, %{ $self->rebless_params } );

    if ( _NEED_OVERLOAD_HACK_FOR_OBJECTS
        && is_overloaded( ref $object ) ) {

        # need to use $_[2] here to apply to the object in the caller
        _reset_amagic($_[2]);
    }

    return $object;
}

1;

# ABSTRACT: Compose a role into an instance

__END__

=pod

=head1 DESCRIPTION

=head2 METHODS

=over 4

=item B<new>

=item B<meta>

=item B<apply>

=item B<rebless_params>

=back

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
