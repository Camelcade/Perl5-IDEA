package Moose::Meta::Class::Immutable::Trait;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP;
use Scalar::Util qw( blessed );

use parent 'Class::MOP::Class::Immutable::Trait';

use Moose::Util 'throw_exception';

sub add_role { $_[1]->_immutable_cannot_call }

sub calculate_all_roles {
    my $orig = shift;
    my $self = shift;
    @{ $self->{__immutable}{calculate_all_roles} ||= [ $self->$orig ] };
}

sub calculate_all_roles_with_inheritance {
    my $orig = shift;
    my $self = shift;
    @{ $self->{__immutable}{calculate_all_roles_with_inheritance} ||= [ $self->$orig ] };
}

sub does_role {
    shift;
    my $self = shift;
    my $role = shift;

    (defined $role)
        || throw_exception( RoleNameRequired => class_name => $self->name );

    $self->{__immutable}{does_role} ||= { map { $_->name => 1 } $self->calculate_all_roles_with_inheritance };

    my $name = blessed $role ? $role->name : $role;

    return $self->{__immutable}{does_role}{$name};
}

1;

# ABSTRACT: Implements immutability for metaclass objects

__END__

=pod

=head1 DESCRIPTION

This class makes some Moose-specific metaclass methods immutable. This
is deep guts.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
