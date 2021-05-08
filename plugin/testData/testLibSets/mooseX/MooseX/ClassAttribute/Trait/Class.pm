package MooseX::ClassAttribute::Trait::Class;

use strict;
use warnings;

our $VERSION = '0.29';

use MooseX::ClassAttribute::Trait::Attribute;
use Scalar::Util qw(blessed);

use namespace::autoclean;
use Moose::Role;

with 'MooseX::ClassAttribute::Trait::Mixin::HasClassAttributes';

has _class_attribute_values => (
    traits   => [ 'Hash' ],
    is       => 'ro',
    isa      => 'HashRef',
    handles  => {
        'get_class_attribute_value'   => 'get',
        'set_class_attribute_value'   => 'set',
        'has_class_attribute_value'   => 'exists',
        'clear_class_attribute_value' => 'delete',
    },
    lazy     => 1,
    default  => sub {$_[0]->_class_attribute_values_hashref()},
    init_arg => undef,
);

around add_class_attribute => sub {
    my $orig = shift;
    my $self = shift;
    my $attr = (
        blessed $_[0] && $_[0]->isa('Class::MOP::Attribute')
            ? $_[0]
            : $self->_process_class_attribute(@_)
    );

    $self->$orig($attr);

    return $attr;
};

sub _post_add_class_attribute {
    my $self = shift;
    my $attr = shift;

    my $name = $attr->name();

    my $e = do {
        local $@;
        eval {$attr->install_accessors()};
        $@;
    };

    if ($e) {
        $self->remove_attribute($name);
        die $e;
    }
}

sub _attach_class_attribute {
    my ($self, $attribute) = @_;
    $attribute->attach_to_class($self);
}

# It'd be nice if I didn't have to replicate this for class
# attributes, since it's basically just a copy of
# Moose::Meta::Class->_process_attribute
sub _process_class_attribute {
    my $self = shift;
    my $name = shift;
    my @args = @_;

    @args = %{$args[0]} if scalar @args == 1 && ref($args[0]) eq 'HASH';

    if ($name =~ /^\+(.*)/) {
        return $self->_process_inherited_class_attribute($1, @args);
    }
    else {
        return $self->_process_new_class_attribute($name, @args);
    }
}

sub _process_new_class_attribute {
    my $self = shift;
    my $name = shift;
    my %p = @_;

    if ($p{traits}) {
        push @{$p{traits}}, 'MooseX::ClassAttribute::Trait::Attribute';
    }
    else {
        $p{traits} = [ 'MooseX::ClassAttribute::Trait::Attribute' ];
    }

    return Moose::Meta::Attribute->interpolate_class_and_new($name, %p);
}

sub _process_inherited_class_attribute {
    my $self = shift;
    my $name = shift;
    my %p = @_;

    my $inherited_attr = $self->find_class_attribute_by_name($name);

    (defined $inherited_attr)
        || confess
        "Could not find an attribute by the name of '$name' to inherit from";

    return $inherited_attr->clone_and_inherit_options(%p);
}

around remove_class_attribute => sub {
    my $orig = shift;
    my $self = shift;

    my $removed_attr = $self->$orig(@_)
        or return;

    $removed_attr->remove_accessors();
    $removed_attr->detach_from_class();

    return $removed_attr;
};

sub get_all_class_attributes {
    my $self = shift;

    my %attrs = map {
        my $meta = Class::MOP::class_of($_);
        $meta && $meta->can('_class_attribute_map')
            ? %{$meta->_class_attribute_map()}
            : ()
    }
        reverse $self->linearized_isa;

    return values %attrs;
}

sub compute_all_applicable_class_attributes {
    warn
        'The compute_all_applicable_class_attributes method has been deprecated.'
            . " Use get_all_class_attributes instead.\n";

    shift->compute_all_applicable_class_attributes(@_);
}

sub find_class_attribute_by_name {
    my $self = shift;
    my $name = shift;

    foreach my $class ($self->linearized_isa()) {
        my $meta = Class::MOP::class_of($class)
            or next;

        return $meta->get_class_attribute($name)
            if $meta->can('has_class_attribute')
                && $meta->has_class_attribute($name);
    }

    return;
}

sub _class_attribute_values_hashref {
    my $self = shift;

    no strict 'refs';
    return \%{$self->_class_attribute_var_name()};
}

sub _class_attribute_var_name {
    my $self = shift;

    return $self->name() . q'::__ClassAttributeValues';
}

sub _inline_class_slot_access {
    my $self = shift;
    my $name = shift;

    return
        '$'
            . $self->_class_attribute_var_name . '{"'
            . quotemeta($name) . '"}';
}

sub _inline_get_class_slot_value {
    my $self = shift;
    my $name = shift;

    return $self->_inline_class_slot_access($name);
}

sub _inline_set_class_slot_value {
    my $self = shift;
    my $name = shift;
    my $val_name = shift;

    return $self->_inline_class_slot_access($name) . ' = ' . $val_name;
}

sub _inline_is_class_slot_initialized {
    my $self = shift;
    my $name = shift;

    return 'exists ' . $self->_inline_class_slot_access($name);
}

sub _inline_deinitialize_class_slot {
    my $self = shift;
    my $name = shift;

    return 'delete ' . $self->_inline_class_slot_access($name);
}

sub _inline_weaken_class_slot_value {
    my $self = shift;
    my $name = shift;

    return
        'Scalar::Util::weaken( '
            . $self->_inline_class_slot_access($name) . ')';
}

1;

# ABSTRACT: A trait for classes with class attributes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Class - A trait for classes with class attributes

=head1 VERSION

version 0.29

=head1 SYNOPSIS

  for my $attr ( HasClassAttributes->meta()->get_all_class_attributes() )
  {
      print $attr->name();
  }

=head1 DESCRIPTION

This role adds awareness of class attributes to a metaclass object. It
provides a set of introspection methods that largely parallel the
existing attribute methods, except they operate on class attributes.

=head1 METHODS

Every method provided by this role has an analogous method in
C<Class::MOP::Class> or C<Moose::Meta::Class> for regular attributes.

=head2 $meta->has_class_attribute($name)

=head2 $meta->get_class_attribute($name)

=head2 $meta->get_class_attribute_list()

These methods operate on the current metaclass only.

=head2 $meta->add_class_attribute(...)

This accepts the same options as the L<Moose::Meta::Attribute>
C<add_attribute()> method. However, if an attribute is specified as
"required" an error will be thrown.

=head2 $meta->remove_class_attribute($name)

If the named class attribute exists, it is removed from the class,
along with its accessor methods.

=head2 $meta->get_all_class_attributes()

This method returns a list of attribute objects for the class and all
its parent classes.

=head2 $meta->find_class_attribute_by_name($name)

This method looks at the class and all its parent classes for the
named class attribute.

=head2 $meta->get_class_attribute_value($name)

=head2 $meta->set_class_attribute_value($name, $value)

=head2 $meta->set_class_attribute_value($name)

=head2 $meta->clear_class_attribute_value($name)

These methods operate on the storage for class attribute values, which
is attached to the metaclass object.

There's really no good reason for you to call these methods unless
you're doing some deep hacking. They are named as public methods
solely because they are used by other meta roles and classes in this
distribution.

=head1 BUGS

See L<MooseX::ClassAttribute> for details.

Bugs may be submitted through L<the RT bug tracker|http://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-ClassAttribute>
(or L<bug-moosex-classattribute@rt.cpan.org|mailto:bug-moosex-classattribute@rt.cpan.org>).

I am also usually active on IRC as 'drolsky' on C<irc://irc.perl.org>.

=head1 AUTHOR

Dave Rolsky <autarch@urth.org>

=head1 COPYRIGHT AND LICENCE

This software is Copyright (c) 2016 by Dave Rolsky.

This is free software, licensed under:

  The Artistic License 2.0 (GPL Compatible)

=cut
