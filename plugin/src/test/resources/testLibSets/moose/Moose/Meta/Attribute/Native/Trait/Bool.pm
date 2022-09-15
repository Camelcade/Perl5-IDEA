package Moose::Meta::Attribute::Native::Trait::Bool;
our $VERSION = '2.2101';

use Moose::Role;
with 'Moose::Meta::Attribute::Native::Trait';

sub _helper_type { 'Bool' }

no Moose::Role;

1;

# ABSTRACT: Helper trait for Bool attributes

__END__

=pod

=head1 SYNOPSIS

  package Room;
  use Moose;

  has 'is_lit' => (
      traits  => ['Bool'],
      is      => 'rw',
      isa     => 'Bool',
      default => 0,
      handles => {
          illuminate  => 'set',
          darken      => 'unset',
          flip_switch => 'toggle',
          is_dark     => 'not',
      },
  );

  my $room = Room->new();
  $room->illuminate;        # same as $room->is_lit(1);
  $room->darken;            # same as $room->is_lit(0);
  $room->flip_switch;       # same as $room->is_lit(not $room->is_lit);
  return $room->is_dark;    # same as !$room->is_lit

=head1 DESCRIPTION

This trait provides native delegation methods for boolean values. A boolean is
a scalar which can be C<1>, C<0>, C<"">, or C<undef>.

=head1 DEFAULT TYPE

If you don't provide an C<isa> value for your attribute, it will default to
C<Bool>.

=head1 PROVIDED METHODS

None of these methods accept arguments.

=over 4

=item * B<set>

Sets the value to C<1> and returns C<1>.

=item * B<unset>

Set the value to C<0> and returns C<0>.

=item * B<toggle>

Toggles the value. If it's true, set to false, and vice versa.

Returns the new value.

=item * B<not>

Equivalent of 'not C<$value>'.

=back

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
