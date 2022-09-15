package Moose::Meta::Object::Trait;
our $VERSION = '2.2101';

use Scalar::Util qw(blessed);

sub _get_compatible_metaclass {
    my $orig = shift;
    my $self = shift;
    return $self->$orig(@_)
        || $self->_get_compatible_metaclass_by_role_reconciliation(@_);
}

sub _get_compatible_metaclass_by_role_reconciliation {
    my $self = shift;
    my ($other_name) = @_;
    my $meta_name = blessed($self) ? $self->_real_ref_name : $self;

    return unless Moose::Util::_classes_differ_by_roles_only(
        $meta_name, $other_name
    );

    return Moose::Util::_reconcile_roles_for_metaclass(
        $meta_name, $other_name
    );
}

1;

# ABSTRACT: Some overrides for L<Class::MOP::Object> functionality

__END__

=pod

=head1 DESCRIPTION

This module is entirely private, you shouldn't ever need to interact with
it directly.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
