package Moose::Meta::Mixin::AttributeCore;
our $VERSION = '2.2101';

use strict;
use warnings;

use parent 'Class::MOP::Mixin::AttributeCore';

__PACKAGE__->meta->add_attribute(
    'isa' => (
        reader => '_isa_metadata',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'does' => (
        reader => '_does_metadata',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'is' => (
        reader => '_is_metadata',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'required' => (
        reader => 'is_required',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'lazy' => (
        reader => 'is_lazy',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'lazy_build' => (
        reader => 'is_lazy_build',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'coerce' => (
        reader => 'should_coerce',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'weak_ref' => (
        reader => 'is_weak_ref',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'auto_deref' => (
        reader => 'should_auto_deref',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'type_constraint' => (
        reader    => 'type_constraint',
        predicate => 'has_type_constraint',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'trigger' => (
        reader    => 'trigger',
        predicate => 'has_trigger',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'handles' => (
        reader    => 'handles',
        writer    => '_set_handles',
        predicate => 'has_handles',
        Class::MOP::_definition_context(),
    )
);

__PACKAGE__->meta->add_attribute(
    'documentation' => (
        reader    => 'documentation',
        predicate => 'has_documentation',
        Class::MOP::_definition_context(),
    )
);

1;

# ABSTRACT: Core attributes shared by attribute metaclasses

__END__

=pod

=head1 DESCRIPTION

This class implements the core attributes (aka properties) shared by all Moose
attributes. See the L<Moose::Meta::Attribute> documentation for API details.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
