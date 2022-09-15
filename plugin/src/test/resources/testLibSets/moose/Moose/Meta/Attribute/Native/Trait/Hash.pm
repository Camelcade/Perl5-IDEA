package Moose::Meta::Attribute::Native::Trait::Hash;
our $VERSION = '2.2101';

use Moose::Role;
with 'Moose::Meta::Attribute::Native::Trait';

sub _helper_type { 'HashRef' }

no Moose::Role;

1;

# ABSTRACT: Helper trait for HashRef attributes

__END__

=pod

=head1 SYNOPSIS

  package Stuff;
  use Moose;

  has 'options' => (
      traits    => ['Hash'],
      is        => 'ro',
      isa       => 'HashRef[Str]',
      default   => sub { {} },
      handles   => {
          set_option     => 'set',
          get_option     => 'get',
          has_no_options => 'is_empty',
          num_options    => 'count',
          delete_option  => 'delete',
          option_pairs   => 'kv',
      },
  );

=head1 DESCRIPTION

This trait provides native delegation methods for hash references.

=head1 PROVIDED METHODS

=head2 get($key, $key2, $key3...)

Returns values from the hash.

In list context it returns a list of values in the hash for the given keys. In
scalar context it returns the value for the last key specified.

This method requires at least one argument.

=head2 set($key =E<gt> $value, $key2 =E<gt> $value2...)

Sets the elements in the hash to the given values. It returns the new values
set for each key, in the same order as the keys passed to the method.

This method requires at least two arguments, and expects an even number of
arguments.

=head2 delete($key, $key2, $key3...)

Removes the elements with the given keys.

In list context it returns a list of values in the hash for the deleted
keys. In scalar context it returns the value for the last key specified.

=head2 keys

Returns the list of keys in the hash.

This method does not accept any arguments.

=head2 exists($key)

Returns true if the given key is present in the hash.

This method requires a single argument.

=head2 defined($key)

Returns true if the value of a given key is defined.

This method requires a single argument.

=head2 values

Returns the list of values in the hash.

This method does not accept any arguments.

=head2 kv

Returns the key/value pairs in the hash as an array of array references.

  for my $pair ( $object->option_pairs ) {
      print "$pair->[0] = $pair->[1]\n";
  }

This method does not accept any arguments.

=head2 elements

In list context, this returns the key/value pairs in the hash.

In scalar context, this returns the count of keys plus values.  In other words,
it's the same as L<keys> times two.

This method does not accept any arguments.

=head2 clear

Resets the hash to an empty value, like C<%hash = ()>.

This method does not accept any arguments.

=head2 count

Returns the number of elements in the hash. Also useful to check for a nonempty hash, because C<count> returns a true (nonzero) value if there is something in the hash:
C<< has_options => 'count' >>.

This method does not accept any arguments.

=head2 is_empty

If the hash is populated, returns false. Otherwise, returns true.

This method does not accept any arguments.

=head2 accessor($key)

=head2 accessor($key, $value)

If passed one argument, returns the value of the specified key. If passed two
arguments, sets the value of the specified key.

When called as a setter, this method returns the value that was set.

=head2 shallow_clone

This method returns a shallow clone of the hash reference.  The return value
is a reference to a new hash with the same keys and values.  It is I<shallow>
because any values that were references in the original will be the I<same>
references in the clone.

=head2 Why no C<each>?

We have deliberately omitted a method for C<each>, due to its stateful
interaction with the hash iterator. Using C<keys> or C<kv> is much safer.

=head2 Moose::Meta::Attribute->meta

This will return a L<Class::MOP::Class> instance for this class.

It should also be noted that L<Class::MOP> will actually bootstrap
this module by installing a number of attribute meta-objects into its
metaclass.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
