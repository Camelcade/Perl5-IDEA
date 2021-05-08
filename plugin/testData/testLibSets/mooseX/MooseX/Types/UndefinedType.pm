use warnings;
use strict;
package MooseX::Types::UndefinedType;
# ABSTRACT: a fallback type for when a type cannot be found

our $VERSION = '0.50';

use Moose::Util::TypeConstraints ();
use Carp::Clan '^MooseX::Types';
use namespace::autoclean 0.16;

use overload '""' => sub {shift->name},
    fallback      => 1;

#pod =head1 DESCRIPTION
#pod
#pod Whenever a type handle function (e.g. C<Int()> can't find a type
#pod constraint under its full name, it assumes it has not yet been defined.
#pod It will then return an instance of this class, handling only
#pod stringification, name and possible identification of undefined types.
#pod
#pod Later, when you try to use the Undefined Type Constraint, autovivification will
#pod be attempted.
#pod
#pod =head1 METHODS
#pod
#pod =head2 new
#pod
#pod Takes a full type name as argument and returns an instance of this
#pod class.
#pod
#pod =cut

sub new {
    return bless { name => $_[1] }, $_[0];
}

#pod =head2 name
#pod
#pod Returns the stored type name.
#pod
#pod =cut

sub name {
    return $_[0]->{name};
}

#pod =head2 __autovivify
#pod
#pod Try to see if the type constraint has yet been defined and if so create it.
#pod
#pod =cut

sub __autovivify {
    my ($self) = @_;
    if (my $tc = $self->{instance}) {
        return $tc;
    }
    elsif (my $new_tc = Moose::Util::TypeConstraints::find_type_constraint($self->name)) {
        $self->{instance} = $new_tc;
        return $new_tc;
    }
    else {
        return;
    }
}

#pod =head2 can_be_inlined
#pod
#pod Make sure that if a type hasn't been defined yet when Moose wants to inline it,
#pod we don't allow inlining.
#pod
#pod =cut

sub can_be_inlined {
    my $self = shift;
    if (my $type_constraint = $self->__autovivify) {
        return $type_constraint->can_be_inlined;
    }
    else {
        return;
    }
}

#pod =head2 AUTOLOAD
#pod
#pod Try to autovivify and delegate
#pod
#pod =cut

sub AUTOLOAD {
    my ($self, @args) = @_;
    my ($method) = our $AUTOLOAD =~ /([^:]+)$/;

    if (my $type_constraint = $self->__autovivify) {
        return $type_constraint->$method(@args);
    }
    else {
        croak "Method '$method' is not supported for " . $self->name;
    }
}

#pod =head2 DESTROY
#pod
#pod Moose::Meta::TypeConstraint::Parameterizable complains if this isn't here. TODO
#pod to find out why.
#pod
#pod =cut

sub DESTROY {
    return;
}

#pod =head1 SEE ALSO
#pod
#pod L<MooseX::Types::Moose>,
#pod L<Moose::Util::TypeConstraints>,
#pod L<Moose::Meta::TypeConstraint>,
#pod L<Carp::Clan>
#pod
#pod =cut


1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::UndefinedType - a fallback type for when a type cannot be found

=head1 VERSION

version 0.50

=head1 DESCRIPTION

Whenever a type handle function (e.g. C<Int()> can't find a type
constraint under its full name, it assumes it has not yet been defined.
It will then return an instance of this class, handling only
stringification, name and possible identification of undefined types.

Later, when you try to use the Undefined Type Constraint, autovivification will
be attempted.

=head1 METHODS

=head2 new

Takes a full type name as argument and returns an instance of this
class.

=head2 name

Returns the stored type name.

=head2 __autovivify

Try to see if the type constraint has yet been defined and if so create it.

=head2 can_be_inlined

Make sure that if a type hasn't been defined yet when Moose wants to inline it,
we don't allow inlining.

=head2 AUTOLOAD

Try to autovivify and delegate

=head2 DESTROY

Moose::Meta::TypeConstraint::Parameterizable complains if this isn't here. TODO
to find out why.

=head1 SEE ALSO

L<MooseX::Types::Moose>,
L<Moose::Util::TypeConstraints>,
L<Moose::Meta::TypeConstraint>,
L<Carp::Clan>

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
