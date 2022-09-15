package Moose::Meta::TypeConstraint;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use overload '0+'     => sub { refaddr(shift) }, # id an object
             '""'     => sub { shift->name },   # stringify to tc name
             bool     => sub { 1 },
             fallback => 1;

use Eval::Closure;
use Scalar::Util qw(refaddr);
use Sub::Util qw(set_subname);
use Try::Tiny;

use parent 'Class::MOP::Object';

use Moose::Util 'throw_exception';

__PACKAGE__->meta->add_attribute('name'       => (
    reader => 'name',
    Class::MOP::_definition_context(),
));
__PACKAGE__->meta->add_attribute('parent'     => (
    reader    => 'parent',
    predicate => 'has_parent',
    Class::MOP::_definition_context(),
));

my $null_constraint = sub { 1 };
__PACKAGE__->meta->add_attribute('constraint' => (
    reader  => 'constraint',
    writer  => '_set_constraint',
    default => sub { $null_constraint },
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('message'   => (
    accessor  => 'message',
    predicate => 'has_message',
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('_default_message' => (
    accessor  => '_default_message',
    Class::MOP::_definition_context(),
));

# can't make this a default because it has to close over the type name, and
# cmop attributes don't have lazy
my $_default_message_generator = sub {
    my $name = shift;
    sub {
        my $value = shift;
        # have to load it late like this, since it uses Moose itself
        my $can_partialdump = try {
            # versions prior to 0.14 had a potential infinite loop bug
            require Devel::PartialDump;
            Devel::PartialDump->VERSION(0.14);
            1;
        };
        if ($can_partialdump) {
            $value = Devel::PartialDump->new->dump($value);
        }
        else {
            $value = (defined $value ? overload::StrVal($value) : 'undef');
        }
        return "Validation failed for '" . $name . "' with value $value";
    }
};
__PACKAGE__->meta->add_attribute('coercion'   => (
    accessor  => 'coercion',
    predicate => 'has_coercion',
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('inlined' => (
    init_arg  => 'inlined',
    accessor  => 'inlined',
    predicate => '_has_inlined_type_constraint',
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('inline_environment' => (
    init_arg => 'inline_environment',
    accessor => '_inline_environment',
    default  => sub { {} },
    Class::MOP::_definition_context(),
));

sub parents {
    my $self = shift;
    $self->parent;
}

# private accessors

__PACKAGE__->meta->add_attribute('compiled_type_constraint' => (
    accessor  => '_compiled_type_constraint',
    predicate => '_has_compiled_type_constraint',
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('package_defined_in' => (
    accessor => '_package_defined_in',
    Class::MOP::_definition_context(),
));

sub new {
    my $class = shift;
    my ($first, @rest) = @_;
    my %args = ref $first ? %$first : $first ? ($first, @rest) : ();
    $args{name} = $args{name} ? "$args{name}" : "__ANON__";

    if ( exists $args{message}
      && (!ref($args{message}) || ref($args{message}) ne 'CODE') ) {
        throw_exception( MessageParameterMustBeCodeRef => params => \%args,
                                                          class  => $class
                       );
    }

    my $self  = $class->_new(%args);
    $self->compile_type_constraint()
        unless $self->_has_compiled_type_constraint;
    $self->_default_message($_default_message_generator->($self->name))
        unless $self->has_message;
    return $self;
}



sub coerce {
    my $self = shift;

    my $coercion = $self->coercion;

    unless ($coercion) {
        throw_exception( CoercingWithoutCoercions => type_name => $self->name );
    }

    return $_[0] if $self->check($_[0]);

    return $coercion->coerce(@_);
}

sub assert_coerce {
    my $self = shift;

    my $result = $self->coerce(@_);

    $self->assert_valid($result);

    return $result;
}

sub check {
    my ($self, @args) = @_;
    my $constraint_subref = $self->_compiled_type_constraint;
    return $constraint_subref->(@args) ? 1 : undef;
}

sub validate {
    my ($self, $value) = @_;
    if ($self->_compiled_type_constraint->($value)) {
        return undef;
    }
    else {
        $self->get_message($value);
    }
}

sub can_be_inlined {
    my $self = shift;

    if ( $self->has_parent && $self->constraint == $null_constraint ) {
        return $self->parent->can_be_inlined;
    }

    return $self->_has_inlined_type_constraint;
}

sub _inline_check {
    my $self = shift;

    unless ( $self->can_be_inlined ) {
        throw_exception( CannotInlineTypeConstraintCheck => type_name => $self->name );
    }

    if ( $self->has_parent && $self->constraint == $null_constraint ) {
        return $self->parent->_inline_check(@_);
    }

    return '( do { ' . $self->inlined->( $self, @_ ) . ' } )';
}

sub inline_environment {
    my $self = shift;

    if ( $self->has_parent && $self->constraint == $null_constraint ) {
        return $self->parent->inline_environment;
    }

    return $self->_inline_environment;
}

sub assert_valid {
    my ( $self, $value ) = @_;

    return 1 if $self->check($value);

    throw_exception(
        'ValidationFailedForTypeConstraint',
        type  => $self,
        value => $value
    );
}

sub get_message {
    my ($self, $value) = @_;
    my $msg = $self->has_message
        ? $self->message
        : $self->_default_message;
    local $_ = $value;
    return $msg->($value);
}

## type predicates ...

sub equals {
    my ( $self, $type_or_name ) = @_;

    my $other = Moose::Util::TypeConstraints::find_type_constraint($type_or_name);
    return if not $other;

    return 1 if $self == $other;

    return unless $self->constraint == $other->constraint;

    if ( $self->has_parent ) {
        return unless $other->has_parent;
        return unless $self->parent->equals( $other->parent );
    } else {
        return if $other->has_parent;
    }

    return;
}

sub is_a_type_of {
    my ($self, $type_or_name) = @_;

    my $type = Moose::Util::TypeConstraints::find_type_constraint($type_or_name);
    return if not $type;

    ($self->equals($type) || $self->is_subtype_of($type));
}

sub is_subtype_of {
    my ($self, $type_or_name) = @_;

    my $type = Moose::Util::TypeConstraints::find_type_constraint($type_or_name);
    return if not $type;

    my $current = $self;

    while (my $parent = $current->parent) {
        return 1 if $parent->equals($type);
        $current = $parent;
    }

    return 0;
}

## compiling the type constraint

sub compile_type_constraint {
    my $self = shift;
    $self->_compiled_type_constraint($self->_actually_compile_type_constraint);
}

## type compilers ...

sub _actually_compile_type_constraint {
    my $self = shift;

    if ( $self->can_be_inlined ) {
        return eval_closure(
            source      => 'sub { ' . $self->_inline_check('$_[0]') . ' }',
            environment => $self->inline_environment,
        );
    }

    my $check = $self->constraint;
    unless ( defined $check ) {
        throw_exception( NoConstraintCheckForTypeConstraint => type_name => $self->name );
    }

    return $self->_compile_subtype($check)
        if $self->has_parent;

    return $self->_compile_type($check);
}

sub _compile_subtype {
    my ($self, $check) = @_;

    # gather all the parent constraints in order
    my @parents;
    foreach my $parent ($self->_collect_all_parents) {
        push @parents => $parent->constraint;
    }

    @parents = grep { $_ != $null_constraint } reverse @parents;

    unless ( @parents ) {
        return $self->_compile_type($check);
    } else {
        # general case, check all the constraints, from the first parent to ourselves
        my @checks = @parents;
        push @checks, $check if $check != $null_constraint;
        return set_subname(
            $self->name => sub {
                my (@args) = @_;
                local $_ = $args[0];
                foreach my $check (@checks) {
                    return undef unless $check->(@args);
                }
                return 1;
            }
        );
    }
}

sub _compile_type {
    my ($self, $check) = @_;

    return $check if $check == $null_constraint; # Item, Any

    return set_subname(
        $self->name => sub {
            my (@args) = @_;
            local $_ = $args[0];
            $check->(@args);
        }
    );
}

## other utils ...

sub _collect_all_parents {
    my $self = shift;
    my @parents;
    my $current = $self->parent;
    while (defined $current) {
        push @parents => $current;
        $current = $current->parent;
    }
    return @parents;
}

sub create_child_type {
    my ($self, %opts) = @_;
    my $class = ref $self;
    return $class->new(%opts, parent => $self);
}

1;

# ABSTRACT: The Moose Type Constraint metaclass

__END__

=pod

=head1 DESCRIPTION

This class represents a single type constraint. Moose's built-in type
constraints, as well as constraints you define, are all stored in a
L<Moose::Meta::TypeConstraint::Registry> object as objects of this
class.

=head1 INHERITANCE

C<Moose::Meta::TypeConstraint> is a subclass of L<Class::MOP::Object>.

=head1 METHODS

=head2 Moose::Meta::TypeConstraint->new(%options)

This creates a new type constraint based on the provided C<%options>:

=over 4

=item * name

The constraint name. If a name is not provided, it will be set to
"__ANON__".

=item * parent

A C<Moose::Meta::TypeConstraint> object which is the parent type for
the type being created. This is optional.

=item * constraint

This is the subroutine reference that implements the actual constraint
check. This defaults to a subroutine which always returns true.

=item * message

A subroutine reference which is used to generate an error message when
the constraint fails. This is optional.

=item * coercion

A L<Moose::Meta::TypeCoercion> object representing the coercions to
the type. This is optional.

=item * inlined

A subroutine which returns a string suitable for inlining this type
constraint. It will be called as a method on the type constraint object, and
will receive a single additional parameter, a variable name to be tested
(usually C<"$_"> or C<"$_[0]">.

This is optional.

=item * inline_environment

A hash reference of variables to close over. The keys are variables names, and
the values are I<references> to the variables.

=back

=head2 $constraint->equals($type_name_or_object)

Returns true if the supplied name or type object is the same as the
current type.

=head2 $constraint->is_subtype_of($type_name_or_object)

Returns true if the supplied name or type object is a parent of the
current type.

=head2 $constraint->is_a_type_of($type_name_or_object)

Returns true if the given type is the same as the current type, or is
a parent of the current type. This is a shortcut for checking
C<equals> and C<is_subtype_of>.

=head2 $constraint->coerce($value)

This will attempt to coerce the value to the type. If the type does not
have any defined coercions this will throw an error.

If no coercion can produce a value matching C<$constraint>, the original
value is returned.

=head2 $constraint->assert_coerce($value)

This method behaves just like C<coerce>, but if the result is not valid
according to C<$constraint>, an error is thrown.

=head2 $constraint->check($value)

Returns true if the given value passes the constraint for the type.

=head2 $constraint->validate($value)

This is similar to C<check>. However, if the type I<is valid> then the
method returns an explicit C<undef>. If the type is not valid, we call
C<< $self->get_message($value) >> internally to generate an error
message.

=head2 $constraint->assert_valid($value)

Like C<check> and C<validate>, this method checks whether C<$value> is
valid under the constraint.  If it is, it will return true.  If it is not,
an exception will be thrown with the results of
C<< $self->get_message($value) >>.

=head2 $constraint->name

Returns the type's name, as provided to the constructor.

=head2 $constraint->parent

Returns the type's parent, as provided to the constructor, if any.

=head2 $constraint->has_parent

Returns true if the type has a parent type.

=head2 $constraint->parents

Returns all of the types parents as an list of type constraint objects.

=head2 $constraint->constraint

Returns the type's constraint, as provided to the constructor.

=head2 $constraint->get_message($value)

This generates a method for the given value. If the type does not have
an explicit message, we generate a default message.

=head2 $constraint->has_message

Returns true if the type has a message.

=head2 $constraint->message

Returns the type's message as a subroutine reference.

=head2 $constraint->coercion

Returns the type's L<Moose::Meta::TypeCoercion> object, if one
exists.

=head2 $constraint->has_coercion

Returns true if the type has a coercion.

=head2 $constraint->can_be_inlined

Returns true if this type constraint can be inlined. A type constraint which
subtypes an inlinable constraint and does not add an additional constraint
"inherits" its parent type's inlining.

=head2 $constraint->create_child_type(%options)

This returns a new type constraint of the same class using the
provided C<%options>. The C<parent> option will be the current type.

This method exists so that subclasses of this class can override this
behavior and change how child types are created.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
