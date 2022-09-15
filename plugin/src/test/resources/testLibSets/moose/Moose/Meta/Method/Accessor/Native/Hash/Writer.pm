package Moose::Meta::Method::Accessor::Native::Hash::Writer;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP::MiniTrait;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Writer',
     'Moose::Meta::Method::Accessor::Native::Hash',
     'Moose::Meta::Method::Accessor::Native::Collection';

sub _inline_coerce_new_values {
    my $self = shift;

    return unless $self->associated_attribute->should_coerce;

    return unless $self->_tc_member_type_can_coerce;

    return <<'EOF';
if (@_) {
    my %h = @_;
    @h{ sort keys %h } = map { $member_coercion->($_) } @h{ sort keys %h };
}
EOF
}

sub _new_members { 'values %{ { @_ } }' }

sub _copy_old_value {
    my $self = shift;
    my ($slot_access) = @_;

    return '{ %{ (' . $slot_access . ') } }';
}

no Moose::Role;

1;
