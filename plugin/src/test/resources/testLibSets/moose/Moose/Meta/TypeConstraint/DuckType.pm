package Moose::Meta::TypeConstraint::DuckType;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use B;
use Scalar::Util 'blessed';
use List::Util 1.33 qw(all);
use Moose::Util 'english_list';

use Moose::Util::TypeConstraints ();

use parent 'Moose::Meta::TypeConstraint';

__PACKAGE__->meta->add_attribute('methods' => (
    accessor => 'methods',
    Class::MOP::_definition_context(),
));

my $inliner = sub {
    my $self = shift;
    my $val  = shift;

    return $self->parent->_inline_check($val)
         . ' && do {' . "\n"
             . 'my $val = ' . $val . ';' . "\n"
             . '&List::Util::all(' . "\n"
                 . 'sub { $val->can($_) },' . "\n"
                 . join(', ', map { B::perlstring($_) } @{ $self->methods })
             . ');' . "\n"
         . '}';
};

sub new {
    my ( $class, %args ) = @_;

    $args{parent}
        = Moose::Util::TypeConstraints::find_type_constraint('Object');

    my @methods = @{ $args{methods} };
    $args{constraint} = sub {
        my $val = $_[0];
        return all { $val->can($_) } @methods;
    };

    $args{inlined} = $inliner;

    my $self = $class->SUPER::new(\%args);

    $self->compile_type_constraint()
        unless $self->_has_compiled_type_constraint;

    return $self;
}

sub equals {
    my ( $self, $type_or_name ) = @_;

    my $other = Moose::Util::TypeConstraints::find_type_constraint($type_or_name);

    return unless $other->isa(__PACKAGE__);

    my @self_methods  = sort @{ $self->methods };
    my @other_methods = sort @{ $other->methods };

    return unless @self_methods == @other_methods;

    while ( @self_methods ) {
        my $method = shift @self_methods;
        my $other_method = shift @other_methods;

        return unless $method eq $other_method;
    }

    return 1;
}

sub create_child_type {
    my ($self, @args) = @_;
    return Moose::Meta::TypeConstraint->new(@args, parent => $self);
}

sub get_message {
    my $self = shift;
    my ($value) = @_;

    if ($self->has_message) {
        return $self->SUPER::get_message(@_);
    }

    return $self->SUPER::get_message($value) unless blessed($value);

    my @methods = grep { !$value->can($_) } @{ $self->methods };
    my $class = blessed $value;
    $class ||= $value;

    return $class
         . " is missing methods "
         . english_list(map { "'$_'" } @methods);
}

1;

# ABSTRACT: Type constraint for duck typing

__END__

=pod

=head1 DESCRIPTION

This class represents type constraints based on an enumerated list of
required methods.

=head1 INHERITANCE

C<Moose::Meta::TypeConstraint::DuckType> is a subclass of
L<Moose::Meta::TypeConstraint>.

=head1 METHODS

=head2 Moose::Meta::TypeConstraint::DuckType->new(%options)

This creates a new duck type constraint based on the given
C<%options>.

It takes the same options as its parent, with several
exceptions. First, it requires an additional option, C<methods>. This
should be an array reference containing a list of required method
names. Second, it automatically sets the parent to the C<Object> type.

Finally, it ignores any provided C<constraint> option. The constraint
is generated automatically based on the provided C<methods>.

=head2 $constraint->methods

Returns the array reference of required methods provided to the
constructor.

=head2 $constraint->create_child_type

This returns a new L<Moose::Meta::TypeConstraint> object with the type
as its parent.

Note that it does I<not> return a C<Moose::Meta::TypeConstraint::DuckType>
object!

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
