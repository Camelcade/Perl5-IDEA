use warnings;
use strict;
package MooseX::Types::Moose;
# ABSTRACT: Type exports that match the types shipped with L<Moose>

our $VERSION = '0.50';

use MooseX::Types;
use Moose::Util::TypeConstraints ();

use namespace::autoclean;

#pod =head1 SYNOPSIS
#pod
#pod   package Foo;
#pod   use Moose;
#pod   use MooseX::Types::Moose qw( ArrayRef Int Str );
#pod   use Carp qw( croak );
#pod
#pod   has 'name',
#pod     is  => 'rw',
#pod     isa => Str;
#pod
#pod   has 'ids',
#pod     is  => 'rw',
#pod     isa => ArrayRef[Int];
#pod
#pod   sub add {
#pod       my ($self, $x, $y) = @_;
#pod       croak 'First arg not an Int'  unless is_Int($x);
#pod       croak 'Second arg not an Int' unless is_Int($y);
#pod       return $x + $y;
#pod   }
#pod
#pod   1;
#pod
#pod =head1 DESCRIPTION
#pod
#pod This package contains a virtual library for L<MooseX::Types> that
#pod is able to export all types known to L<Moose>. See L<MooseX::Types>
#pod for general usage information.
#pod
#pod =cut

# all available builtin types as short and long name
my %BuiltIn_Storage
    = map {($_) x 2}
    Moose::Util::TypeConstraints->list_all_builtin_type_constraints;

#pod =head1 METHODS
#pod
#pod =head2 type_storage
#pod
#pod Overrides L<MooseX::Types::Base>' C<type_storage> to provide a hash
#pod reference containing all built-in L<Moose> types.
#pod
#pod =cut

# use prepopulated builtin hash as type storage
sub type_storage {\%BuiltIn_Storage}

#pod =head1 SEE ALSO
#pod
#pod L<Moose>,
#pod L<Moose::Util::TypeConstraints>
#pod
#pod =cut

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::Moose - Type exports that match the types shipped with L<Moose>

=head1 VERSION

version 0.50

=head1 SYNOPSIS

  package Foo;
  use Moose;
  use MooseX::Types::Moose qw( ArrayRef Int Str );
  use Carp qw( croak );

  has 'name',
    is  => 'rw',
    isa => Str;

  has 'ids',
    is  => 'rw',
    isa => ArrayRef[Int];

  sub add {
      my ($self, $x, $y) = @_;
      croak 'First arg not an Int'  unless is_Int($x);
      croak 'Second arg not an Int' unless is_Int($y);
      return $x + $y;
  }

  1;

=head1 DESCRIPTION

This package contains a virtual library for L<MooseX::Types> that
is able to export all types known to L<Moose>. See L<MooseX::Types>
for general usage information.

=head1 METHODS

=head2 type_storage

Overrides L<MooseX::Types::Base>' C<type_storage> to provide a hash
reference containing all built-in L<Moose> types.

=head1 SEE ALSO

L<Moose>,
L<Moose::Util::TypeConstraints>

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Types>
(or L<bug-MooseX-Types@rt.cpan.org|mailto:bug-MooseX-Types@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Robert "phaylon" Sedlacek <rs@474.at>

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2007 by Robert "phaylon" Sedlacek.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
