use warnings;
use strict;
package MooseX::Types::Util;
# ABSTRACT: Common utility functions for the distribution

our $VERSION = '0.50';

use Scalar::Util 'blessed';
use base 'Exporter';
use namespace::autoclean;

#pod =head1 DESCRIPTION
#pod
#pod This package the exportable functions that many parts in
#pod L<MooseX::Types> might need.
#pod
#pod =cut

our @EXPORT_OK = qw(filter_tags has_available_type_export);

#pod =head1 FUNCTIONS
#pod
#pod =head2 filter_tags
#pod
#pod Takes a list and returns two references. The first is a hash reference
#pod containing the tags as keys and the number of their appearance as values.
#pod The second is an array reference containing all other elements.
#pod
#pod =cut

sub filter_tags {
    my (@list) = @_;
    my (%tags, @other);
    for (@list) {
        if (/^:(.*)$/) {
            $tags{ $1 }++;
            next;
        }
        push @other, $_;
    }
    return \%tags, \@other;
}

#pod =head2 has_available_type_export
#pod
#pod   TypeConstraint | Undef = has_available_type_export($package, $name);
#pod
#pod This function allows you to introspect if a given type export is available
#pod I<at this point in time>. This means that the C<$package> must have imported
#pod a type constraint with the name C<$name>, and it must be still in its symbol
#pod table.
#pod
#pod Two arguments are expected:
#pod
#pod =over 4
#pod
#pod =item $package
#pod
#pod The name of the package to introspect.
#pod
#pod =item $name
#pod
#pod The name of the type export to introspect.
#pod
#pod =back
#pod
#pod B<Note> that the C<$name> is the I<exported> name of the type, not the declared
#pod one. This means that if you use L<Sub::Exporter>s functionality to rename an import
#pod like this:
#pod
#pod   use MyTypes Str => { -as => 'MyStr' };
#pod
#pod you would have to introspect this type like this:
#pod
#pod   has_available_type_export $package, 'MyStr';
#pod
#pod The return value will be either the type constraint that belongs to the export
#pod or an undefined value.
#pod
#pod =cut

sub has_available_type_export {
    my ($package, $name) = @_;

    my $sub = $package->can($name)
        or return undef;

    return undef
        unless blessed $sub && $sub->isa('MooseX::Types::EXPORTED_TYPE_CONSTRAINT');

    return $sub->();
}

#pod =head1 SEE ALSO
#pod
#pod L<MooseX::Types::Moose>, L<Exporter>
#pod
#pod =cut

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::Util - Common utility functions for the distribution

=head1 VERSION

version 0.50

=head1 DESCRIPTION

This package the exportable functions that many parts in
L<MooseX::Types> might need.

=head1 FUNCTIONS

=head2 filter_tags

Takes a list and returns two references. The first is a hash reference
containing the tags as keys and the number of their appearance as values.
The second is an array reference containing all other elements.

=head2 has_available_type_export

  TypeConstraint | Undef = has_available_type_export($package, $name);

This function allows you to introspect if a given type export is available
I<at this point in time>. This means that the C<$package> must have imported
a type constraint with the name C<$name>, and it must be still in its symbol
table.

Two arguments are expected:

=over 4

=item $package

The name of the package to introspect.

=item $name

The name of the type export to introspect.

=back

B<Note> that the C<$name> is the I<exported> name of the type, not the declared
one. This means that if you use L<Sub::Exporter>s functionality to rename an import
like this:

  use MyTypes Str => { -as => 'MyStr' };

you would have to introspect this type like this:

  has_available_type_export $package, 'MyStr';

The return value will be either the type constraint that belongs to the export
or an undefined value.

=head1 SEE ALSO

L<MooseX::Types::Moose>, L<Exporter>

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
