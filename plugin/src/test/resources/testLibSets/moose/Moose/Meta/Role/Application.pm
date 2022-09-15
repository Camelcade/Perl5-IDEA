package Moose::Meta::Role::Application;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;
use overload ();

use List::Util 1.33 qw( all );

use Moose::Util 'throw_exception';

__PACKAGE__->meta->add_attribute('method_exclusions' => (
    init_arg => '-excludes',
    reader   => 'get_method_exclusions',
    default  => sub { [] },
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('method_aliases' => (
    init_arg => '-alias',
    reader   => 'get_method_aliases',
    default  => sub { {} },
    Class::MOP::_definition_context(),
));

sub new {
    my ($class, %params) = @_;
    $class->_new(\%params);
}

sub is_method_excluded {
    my ($self, $method_name) = @_;
    foreach (@{$self->get_method_exclusions}) {
        return 1 if $_ eq $method_name;
    }
    return 0;
}

sub is_method_aliased {
    my ($self, $method_name) = @_;
    exists $self->get_method_aliases->{$method_name} ? 1 : 0
}

sub is_aliased_method {
    my ($self, $method_name) = @_;
    my %aliased_names = reverse %{$self->get_method_aliases};
    exists $aliased_names{$method_name} ? 1 : 0;
}

sub apply {
    my $self = shift;

    $self->check_role_exclusions(@_);
    $self->check_required_methods(@_);
    $self->check_required_attributes(@_);

    $self->apply_overloading(@_);
    $self->apply_attributes(@_);
    $self->apply_methods(@_);

    $self->apply_override_method_modifiers(@_);

    $self->apply_before_method_modifiers(@_);
    $self->apply_around_method_modifiers(@_);
    $self->apply_after_method_modifiers(@_);
}

sub check_role_exclusions           { throw_exception( "CannotCallAnAbstractMethod" ); }
sub check_required_methods          { throw_exception( "CannotCallAnAbstractMethod" ); }
sub check_required_attributes       { throw_exception( "CannotCallAnAbstractMethod" ); }

sub apply_attributes                { throw_exception( "CannotCallAnAbstractMethod" ); }
sub apply_methods                   { throw_exception( "CannotCallAnAbstractMethod" ); }
sub apply_override_method_modifiers { throw_exception( "CannotCallAnAbstractMethod" ); }
sub apply_method_modifiers          { throw_exception( "CannotCallAnAbstractMethod" ); }

sub apply_before_method_modifiers   { (shift)->apply_method_modifiers('before' => @_) }
sub apply_around_method_modifiers   { (shift)->apply_method_modifiers('around' => @_) }
sub apply_after_method_modifiers    { (shift)->apply_method_modifiers('after'  => @_) }

sub apply_overloading {
    my ( $self, $role, $other ) = @_;

    return unless $role->is_overloaded;

    unless ( $other->is_overloaded ) {
        $other->set_overload_fallback_value(
            $role->get_overload_fallback_value );
    }

    for my $overload ( $role->get_all_overloaded_operators ) {
        next if $other->has_overloaded_operator( $overload->operator );
        $other->add_overloaded_operator(
            $overload->operator => $overload->clone );
    }
}

1;

# ABSTRACT: A base class for role application

__END__

=pod

=head1 DESCRIPTION

This is the abstract base class for role applications.

The API for this class and its subclasses still needs some
consideration, and is intentionally not yet documented.

=head2 METHODS

=over 4

=item B<new>

=item B<meta>

=item B<get_method_exclusions>

=item B<is_method_excluded>

=item B<get_method_aliases>

=item B<is_aliased_method>

=item B<is_method_aliased>

=item B<apply>

=item B<check_role_exclusions>

=item B<check_required_methods>

=item B<check_required_attributes>

=item B<apply_attributes>

=item B<apply_methods>

=item B<apply_overloading>

=item B<apply_method_modifiers>

=item B<apply_before_method_modifiers>

=item B<apply_after_method_modifiers>

=item B<apply_around_method_modifiers>

=item B<apply_override_method_modifiers>

=back

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
