package Mojolicious::Validator;
use Mojo::Base -base;

use Mojo::DynamicMethods;
use Mojo::Util 'trim';
use Mojolicious::Validator::Validation;

has checks  => sub { {} };
has filters => sub { {trim => \&_trim} };

sub add_check {
  my ($self, $name, $cb) = @_;
  $self->checks->{$name} = $cb;
  Mojo::DynamicMethods::register 'Mojolicious::Validator::Validation', $self,
    $name, $cb;
  return $self;
}

sub add_filter { $_[0]->filters->{$_[1]} = $_[2] and return $_[0] }

sub new {
  my $self = shift->SUPER::new(@_);

  $self->add_check(equal_to => \&_equal_to);
  $self->add_check(in       => \&_in);
  $self->add_check(like     => sub { $_[2] !~ $_[3] });
  $self->add_check(num      => \&_num);
  $self->add_check(size     => \&_size);
  $self->add_check(upload => sub { !ref $_[2] || !$_[2]->isa('Mojo::Upload') });

  return $self;
}

sub validation {
  Mojolicious::Validator::Validation->new(validator => shift);
}

sub _equal_to {
  my ($v, $name, $value, $to) = @_;
  return 1 unless defined(my $other = $v->input->{$to});
  return $value ne $other;
}

sub _in {
  my ($v, $name, $value) = (shift, shift, shift);
  $value eq $_ && return undef for @_;
  return 1;
}

sub _num {
  my ($v, $name, $value, $min, $max) = @_;
  return 1 if $value !~ /^[0-9]+$/;
  return defined $min && $min > $value || defined $max && $max < $value;
}

sub _size {
  my ($v, $name, $value, $min, $max) = @_;
  my $len = ref $value ? $value->size : length $value;
  return $len < $min || $len > $max;
}

sub _trim { trim $_[2] // '' }

1;

=encoding utf8

=head1 NAME

Mojolicious::Validator - Validate values

=head1 SYNOPSIS

  use Mojolicious::Validator;

  my $validator = Mojolicious::Validator->new;
  my $v = $validator->validation;
  $v->input({foo => 'bar'});
  $v->required('foo')->like(qr/ar$/);
  say $v->param('foo');

=head1 DESCRIPTION

L<Mojolicious::Validator> validates values for L<Mojolicious>.

=head1 CHECKS

These validation checks are available by default.

=head2 equal_to

  $v = $v->equal_to('foo');

String value needs to be equal to the value of another field.

=head2 in

  $v = $v->in('foo', 'bar', 'baz');

String value needs to match one of the values in the list.

=head2 like

  $v = $v->like(qr/^[A-Z]/);

String value needs to match the regular expression.

=head2 num

  $v = $v->num;
  $v = $v->num(2, 5);
  $v = $v->num(2, undef);
  $v = $v->num(undef, 5);

String value needs to be a non-fractional number and if provided in the given
range.

=head2 size

  $v = $v->size(2, 5);

String value length or size of L<Mojo::Upload> object in bytes needs to be
between these two values.

=head2 upload

  $v = $v->upload;

Value needs to be a L<Mojo::Upload> object, representing a file upload.

=head1 FILTERS

These filters are available by default.

=head2 trim

  $v = $v->optional('foo', 'trim');

Trim whitespace characters from both ends of string value with
L<Mojo::Util/"trim">.

=head1 ATTRIBUTES

L<Mojolicious::Validator> implements the following attributes.

=head2 checks

  my $checks = $validator->checks;
  $validator = $validator->checks({size => sub {...}});

Registered validation checks, by default only L</"equal_to">, L</"in">,
L</"like">, L</"num">, L</"size"> and L</"upload"> are already defined.

=head1 METHODS

L<Mojolicious::Validator> inherits all methods from L<Mojo::Base> and
implements the following new ones.

=head2 add_check

  $validator = $validator->add_check(size => sub {...});

Register a validation check.

  $validator->add_check(foo => sub {
    my ($v, $name, $value, @args) = @_;
    ...
    return undef;
  });

=head2 add_filter

  $validator = $validator->add_filter(trim => sub {...});

Register a new filter.

  $validator->add_filter(foo => sub {
    my ($v, $name, $value) = @_;
    ...
    return $value;
  });

=head2 new

  my $validator = Mojolicious::Validator->new;

Construct a new L<Mojolicious::Validator> object.

=head2 validation

  my $v = $validator->validation;

Build L<Mojolicious::Validator::Validation> object to perform validations.

  my $v = $validator->validation;
  $v->input({foo => 'bar'});
  $v->required('foo')->size(1, 5);
  say $v->param('foo');

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut
