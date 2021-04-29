use strict;
use warnings;
package Moose::Meta::Attribute::Native;
our $VERSION = '2.2101';

use Module::Runtime 'require_module';

my @trait_names = qw(Bool Counter Number String Array Hash Code);

for my $trait_name (@trait_names) {
    my $trait_class = "Moose::Meta::Attribute::Native::Trait::$trait_name";
    my $meta = Class::MOP::Class->initialize(
        "Moose::Meta::Attribute::Custom::Trait::$trait_name"
    );

    if ($meta->find_method_by_name('register_implementation')) {
        my $class = $meta->name->register_implementation;
        die "An implementation for $trait_name already exists " .
            "(found '$class' when trying to register '$trait_class')"
    }
    $meta->add_method(register_implementation => sub {
        # resolve_metatrait_alias will load classes anyway, but throws away
        # their error message; we WANT to die if there's a problem
        require_module($trait_class);
        return $trait_class;
    });
}

1;

# ABSTRACT: Delegate to native Perl types

__END__

=pod

=head1 SYNOPSIS

  package MyClass;
  use Moose;

  has 'mapping' => (
      traits  => ['Hash'],
      is      => 'rw',
      isa     => 'HashRef[Str]',
      default => sub { {} },
      handles => {
          exists_in_mapping => 'exists',
          ids_in_mapping    => 'keys',
          get_mapping       => 'get',
          set_mapping       => 'set',
          set_quantity      => [ set => 'quantity' ],
      },
  );

  my $obj = MyClass->new;
  $obj->set_quantity(10);      # quantity => 10
  $obj->set_mapping('foo', 4); # foo => 4
  $obj->set_mapping('bar', 5); # bar => 5
  $obj->set_mapping('baz', 6); # baz => 6

  # prints 5
  print $obj->get_mapping('bar') if $obj->exists_in_mapping('bar');

  # prints 'quantity, foo, bar, baz'
  print join ', ', $obj->ids_in_mapping;

=head1 DESCRIPTION

Native delegations allow you to delegate to native Perl data
structures as if they were objects. For example, in the L</SYNOPSIS> you can
see a hash reference being treated as if it has methods named C<exists()>,
C<keys()>, C<get()>, and C<set()>.

The delegation methods (mostly) map to Perl builtins and operators. The return
values of these delegations should be the same as the corresponding Perl
operation. Any deviations will be explicitly documented.

=head1 API

Native delegations are enabled by passing certain options to C<has> when
creating an attribute.

=head2 traits

To enable this feature, pass the appropriate name in the C<traits> array
reference for the attribute. For example, to enable this feature for hash
reference, we include C<'Hash'> in the list of traits.

=head2 isa

You will need to make sure that the attribute has an appropriate type. For
example, to use this with a Hash you must specify that your attribute is some
sort of C<HashRef>.

=head2 handles

This is just like any other delegation, but only a hash reference is allowed
when defining native delegations. The keys are the methods to be created in
the class which contains the attribute. The values are the methods provided by
the associated trait. Currying works the same way as it does with any other
delegation.

See the docs for each native trait for details on what methods are available.

=head1 TRAITS FOR NATIVE DELEGATIONS

Below are some simple examples of each native trait. More features are
available than what is shown here; this is just a quick synopsis.

=over

=item Array (L<Moose::Meta::Attribute::Native::Trait::Array>)

    has 'queue' => (
        traits  => ['Array'],
        is      => 'ro',
        isa     => 'ArrayRef[Str]',
        default => sub { [] },
        handles => {
            add_item  => 'push',
            next_item => 'shift',
            # ...
        }
    );

=item Bool (L<Moose::Meta::Attribute::Native::Trait::Bool>)

    has 'is_lit' => (
        traits  => ['Bool'],
        is      => 'ro',
        isa     => 'Bool',
        default => 0,
        handles => {
            illuminate  => 'set',
            darken      => 'unset',
            flip_switch => 'toggle',
            is_dark     => 'not',
            # ...
        }
    );

=item Code (L<Moose::Meta::Attribute::Native::Trait::Code>)

    has 'callback' => (
        traits  => ['Code'],
        is      => 'ro',
        isa     => 'CodeRef',
        default => sub {
            sub {'called'}
        },
        handles => {
            call => 'execute',
            # ...
        }
    );

=item Counter (L<Moose::Meta::Attribute::Native::Trait::Counter>)

    has 'counter' => (
        traits  => ['Counter'],
        is      => 'ro',
        isa     => 'Num',
        default => 0,
        handles => {
            inc_counter   => 'inc',
            dec_counter   => 'dec',
            reset_counter => 'reset',
            # ...
        }
    );

=item Hash (L<Moose::Meta::Attribute::Native::Trait::Hash>)

    has 'options' => (
        traits  => ['Hash'],
        is      => 'ro',
        isa     => 'HashRef[Str]',
        default => sub { {} },
        handles => {
            set_option => 'set',
            get_option => 'get',
            has_option => 'exists',
            # ...
        }
    );

=item Number (L<Moose::Meta::Attribute::Native::Trait::Number>)

    has 'integer' => (
        traits  => ['Number'],
        is      => 'ro',
        isa     => 'Int',
        default => 5,
        handles => {
            set => 'set',
            add => 'add',
            sub => 'sub',
            mul => 'mul',
            div => 'div',
            mod => 'mod',
            abs => 'abs',
            # ...
        }
    );

=item String (L<Moose::Meta::Attribute::Native::Trait::String>)

    has 'text' => (
        traits  => ['String'],
        is      => 'ro',
        isa     => 'Str',
        default => q{},
        handles => {
            add_text     => 'append',
            replace_text => 'replace',
            # ...
        }
    );

=back

=head1 COMPATIBILITY WITH MooseX::AttributeHelpers

This feature used to be a separated CPAN distribution called
L<MooseX::AttributeHelpers>.

When the feature was incorporated into the Moose core, some of the API details
were changed. The underlying capabilities are the same, but some details of
the API were changed.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
