package Moose::Meta::Attribute::Native::Trait::Counter;
our $VERSION = '2.2101';

use Moose::Role;
with 'Moose::Meta::Attribute::Native::Trait';

sub _helper_type { 'Num' }
sub _root_types { 'Num', 'Int' }

no Moose::Role;

1;

# ABSTRACT: Helper trait for Int attributes which represent counters

__END__

=pod

=head1 SYNOPSIS

  package MyHomePage;
  use Moose;

  has 'counter' => (
      traits  => ['Counter'],
      is      => 'ro',
      isa     => 'Num',
      default => 0,
      handles => {
          inc_counter   => 'inc',
          dec_counter   => 'dec',
          reset_counter => 'reset',
      },
  );

  my $page = MyHomePage->new();
  $page->inc_counter;    # same as $page->counter( $page->counter + 1 );
  $page->dec_counter;    # same as $page->counter( $page->counter - 1 );

  my $count_by_twos = 2;
  $page->inc_counter($count_by_twos);

=head1 DESCRIPTION

This trait provides native delegation methods for counters. A counter can be
any sort of number (integer or not). The delegation methods allow you to
increment, decrement, or reset the value.

=head1 DEFAULT TYPE

If you don't provide an C<isa> value for your attribute, it will default to
C<Num>.

=head1 PROVIDED METHODS

=over 4

=item * B<set($value)>

Sets the counter to the specified value and returns the new value.

This method requires a single argument.

=item * B<inc>

=item * B<inc($arg)>

Increases the attribute value by the amount of the argument, or by 1 if no
argument is given. This method returns the new value.

This method accepts a single argument.

=item * B<dec>

=item * B<dec($arg)>

Decreases the attribute value by the amount of the argument, or by 1 if no
argument is given. This method returns the new value.

This method accepts a single argument.

=item * B<reset>

Resets the value stored in this slot to its default value, and returns the new
value.

=back

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
