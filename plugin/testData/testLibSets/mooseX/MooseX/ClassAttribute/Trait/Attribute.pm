package MooseX::ClassAttribute::Trait::Attribute;

use strict;
use warnings;

our $VERSION = '0.29';

use namespace::autoclean;
use Moose::Role;

# This is the worst role evar! Really, this should be a subclass,
# because it overrides a lot of behavior. However, as a subclass it
# won't cooperate with _other_ subclasses.

around _process_options => sub {
    my $orig = shift;
    my $class = shift;
    my $name = shift;
    my $options = shift;

    confess 'A class attribute cannot be required'
        if $options->{required};

    return $class->$orig($name, $options);
};

after attach_to_class => sub {
    my $self = shift;
    my $meta = shift;

    $self->_initialize($meta)
        unless $self->is_lazy();
};

before detach_from_class => sub {
    my $self = shift;
    my $meta = shift;

    $self->clear_value($meta);
};

sub _initialize {
    my $self = shift;
    my $metaclass = shift;

    if ($self->has_default()) {
        $self->set_value(
            undef,
            $self->default($self->associated_class())
        );
    }
    elsif ($self->has_builder()) {
        $self->set_value(undef, $self->_call_builder($metaclass->name()));
    }
}

around default => sub {
    my $orig = shift;
    my $self = shift;

    my $default = $self->$orig();

    if ($self->is_default_a_coderef() && @_) {
        return $default->(@_);
    }

    return $default;
};

around _call_builder => sub {
    shift;
    my $self = shift;
    my $class = shift;

    my $builder = $self->builder();

    return $class->$builder()
        if $class->can($self->builder);

    confess("$class does not support builder method '"
        . $self->builder
        . "' for attribute '"
        . $self->name
        . "'");
};

around set_value => sub {
    shift;
    my $self = shift;
    shift; # ignoring instance or class name
    my $value = shift;

    $self->associated_class()
        ->set_class_attribute_value($self->name() => $value);
};

around get_value => sub {
    shift;
    my $self = shift;

    return $self->associated_class()
        ->get_class_attribute_value($self->name());
};

around has_value => sub {
    shift;
    my $self = shift;

    return $self->associated_class()
        ->has_class_attribute_value($self->name());
};

around clear_value => sub {
    shift;
    my $self = shift;

    return $self->associated_class()
        ->clear_class_attribute_value($self->name());
};

if ($Moose::VERSION < 1.99) {
    around inline_get => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_get_class_slot_value($self->slots());
    };

    around inline_set => sub {
        shift;
        my $self = shift;
        shift;
        my $value = shift;

        my $meta = $self->associated_class();

        my $code
            = $meta->_inline_set_class_slot_value($self->slots(), $value)
            . ";";
        $code .= $meta->_inline_weaken_class_slot_value(
            $self->slots(),
            $value
        )
            . "    if ref $value;"
            if $self->is_weak_ref();

        return $code;
    };

    around inline_has => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_is_class_slot_initialized($self->slots());
    };

    around inline_clear => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_deinitialize_class_slot($self->slots());
    };
}
else {
    around _inline_instance_get => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_get_class_slot_value($self->slots());
    };

    around _inline_instance_set => sub {
        shift;
        my $self = shift;
        shift;
        my $value = shift;

        return $self->associated_class()
            ->_inline_set_class_slot_value($self->slots(), $value);
    };

    around _inline_instance_has => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_is_class_slot_initialized($self->slots());
    };

    around _inline_instance_clear => sub {
        shift;
        my $self = shift;

        return $self->associated_class()
            ->_inline_deinitialize_class_slot($self->slots());
    };

    around _inline_weaken_value => sub {
        shift;
        my $self = shift;
        shift;
        my $value = shift;

        return unless $self->is_weak_ref();

        return (
            $self->associated_class->_inline_weaken_class_slot_value(
                $self->slots(), $value
            ),
            'if ref ' . $value . ';',
        );
    };
}

1;

# ABSTRACT: A trait for class attributes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Attribute - A trait for class attributes

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This role modifies the behavior of class attributes in various
ways. It really should be a subclass of C<Moose::Meta::Attribute>, but
if it were then it couldn't be combined with other attribute
metaclasses, like C<MooseX::AttributeHelpers>.

There are no new public methods implemented by this role. All it does
is change the behavior of a number of existing methods.

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
