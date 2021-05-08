use strict;
use warnings;
package MooseX::Types::Combine;
# ABSTRACT: Combine type libraries for exporting

our $VERSION = '0.50';

use Module::Runtime 'use_module';
use namespace::autoclean;

#pod =head1 SYNOPSIS
#pod
#pod     package CombinedTypeLib;
#pod
#pod     use base 'MooseX::Types::Combine';
#pod
#pod     __PACKAGE__->provide_types_from(qw/TypeLib1 TypeLib2/);
#pod
#pod     package UserClass;
#pod
#pod     use CombinedTypeLib qw/Type1 Type2 ... /;
#pod
#pod =head1 DESCRIPTION
#pod
#pod Allows you to create a single class that will allow you to export types from
#pod multiple type libraries:
#pod
#pod     package TransportTypes;
#pod
#pod     use base 'MooseX::Types::Combine';
#pod
#pod     __PACKAGE__->provide_types_from(qw/ MotorizedTypes UnmotorizedTypes /);
#pod
#pod     1;
#pod
#pod In this example all types defined in C<MotorizedTypes> and C<UnmotorizedTypes>
#pod are available through the C<TransportTypes> combined type library.
#pod
#pod     package SkiingTrip;
#pod
#pod     use Moose;
#pod
#pod     use TransportTypes qw( CarType SkisType );
#pod
#pod     has car => ( is => 'ro', isa => CarType, required => 1 );
#pod     has ski_rack => ( is => 'ro', isa => ArrayRef[SkisType], required => 1 );
#pod     ...
#pod
#pod Libraries on the right end of the list passed to L</provide_types_from> take
#pod precedence over those on the left in case of conflicts.  So, in the above
#pod example if both the C<MotorizedTypes> and C<UnmotorizedTypes> libraries provided
#pod a C<Bike> type, you'd get the bicycle from C<UnmotorizedTypes> not the
#pod motorbike from C<MorotizedTypes>.
#pod
#pod You can also further combine combined type libraries with additional type
#pod libraries or other combined type libraries in the same way to provide even
#pod larger type libraries:
#pod
#pod     package MeetingTransportTypes;
#pod
#pod     use base 'MooseX::Types::Combine';
#pod
#pod     __PACKAGE__->provide_types_from(qw/ TransportTypes TelepresenceTypes /);
#pod
#pod     1;
#pod
#pod =cut

sub import {
    my ($class, @types) = @_;
    my $caller = caller;

    my $where_to_import_to = $caller;
    if (ref $types[0] eq 'HASH') {
        my $extra = shift @types;
        $where_to_import_to = $extra->{-into} if exists $extra->{-into};
    }

    my %types = $class->_provided_types;

    if (grep {$_ eq ':all'} @types) {
        $_->import({ -into => $where_to_import_to }, q{:all})
            for $class->provide_types_from;
        return;
    }

    my %from;
    for my $type (@types) {
        unless ($types{$type}) {
            my @type_libs = $class->provide_types_from;

            die
                "$caller asked for a type ($type) which is not found in any of the"
                    . " type libraries (@type_libs) combined by $class\n";
        }

        push @{$from{ $types{$type} }}, $type;
    }

    $_->import({ -into => $where_to_import_to }, @{$from{ $_ }})
        for keys %from;
}

#pod =head1 CLASS METHODS
#pod
#pod =head2 provide_types_from
#pod
#pod Sets or returns a list of type libraries (or combined type libraries) to
#pod re-export from.
#pod
#pod =cut

sub provide_types_from {
    my ($class, @libs) = @_;

    my $store =
        do {
            no strict 'refs';
            \@{"${class}::__MOOSEX_TYPELIBRARY_LIBRARIES"}
        };

    if (@libs) {
        $class->_check_type_lib($_) for @libs;
        @$store = @libs;

        my %types = map {
            my $lib = $_;
            map +($_ => $lib), $lib->type_names
        } @libs;

        $class->_provided_types(%types);
    }

    @$store;
}

sub _check_type_lib {
    my ($class, $lib) = @_;

    use_module($lib);

    die "Cannot use $lib in a combined type library, it does not provide any types"
        unless $lib->can('type_names');
}

sub _provided_types {
    my ($class, %types) = @_;

    my $types =
        do {
            no strict 'refs';
            \%{"${class}::__MOOSEX_TYPELIBRARY_TYPES"}
        };

    %$types = %types
        if keys %types;

    %$types;
}

#pod =head2 type_names
#pod
#pod Returns a list of all known types by their name.
#pod
#pod =cut

sub type_names {
    my ($class) = @_;

    my %types = $class->_provided_types();
    return keys %types;
}

#pod =head1 SEE ALSO
#pod
#pod L<MooseX::Types>
#pod
#pod =cut

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::Combine - Combine type libraries for exporting

=head1 VERSION

version 0.50

=head1 SYNOPSIS

    package CombinedTypeLib;

    use base 'MooseX::Types::Combine';

    __PACKAGE__->provide_types_from(qw/TypeLib1 TypeLib2/);

    package UserClass;

    use CombinedTypeLib qw/Type1 Type2 ... /;

=head1 DESCRIPTION

Allows you to create a single class that will allow you to export types from
multiple type libraries:

    package TransportTypes;

    use base 'MooseX::Types::Combine';

    __PACKAGE__->provide_types_from(qw/ MotorizedTypes UnmotorizedTypes /);

    1;

In this example all types defined in C<MotorizedTypes> and C<UnmotorizedTypes>
are available through the C<TransportTypes> combined type library.

    package SkiingTrip;

    use Moose;

    use TransportTypes qw( CarType SkisType );

    has car => ( is => 'ro', isa => CarType, required => 1 );
    has ski_rack => ( is => 'ro', isa => ArrayRef[SkisType], required => 1 );
    ...

Libraries on the right end of the list passed to L</provide_types_from> take
precedence over those on the left in case of conflicts.  So, in the above
example if both the C<MotorizedTypes> and C<UnmotorizedTypes> libraries provided
a C<Bike> type, you'd get the bicycle from C<UnmotorizedTypes> not the
motorbike from C<MorotizedTypes>.

You can also further combine combined type libraries with additional type
libraries or other combined type libraries in the same way to provide even
larger type libraries:

    package MeetingTransportTypes;

    use base 'MooseX::Types::Combine';

    __PACKAGE__->provide_types_from(qw/ TransportTypes TelepresenceTypes /);

    1;

=head1 CLASS METHODS

=head2 provide_types_from

Sets or returns a list of type libraries (or combined type libraries) to
re-export from.

=head2 type_names

Returns a list of all known types by their name.

=head1 SEE ALSO

L<MooseX::Types>

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
