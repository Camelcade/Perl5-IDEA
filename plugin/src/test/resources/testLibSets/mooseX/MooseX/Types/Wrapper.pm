package MooseX::Types::Wrapper;
# ABSTRACT: Wrap exports from a library

our $VERSION = '0.50';

use Moose;
use Carp::Clan qw(^MooseX::Types);
use Module::Runtime 'use_module';

use namespace::autoclean;

extends 'MooseX::Types';

#pod =head1 DESCRIPTION
#pod
#pod See L<MooseX::Types/SYNOPSIS> for detailed usage.
#pod
#pod =head1 METHODS
#pod
#pod =head2 import
#pod
#pod =cut

sub import {
    my ($class, @args) = @_;
    my %libraries = @args == 1 ? (Moose => $args[0]) : @args;

    for my $l (keys %libraries) {

        croak qq($class expects an array reference as import spec)
            unless ref $libraries{ $l } eq 'ARRAY';

        my $library_class
            = ($l eq 'Moose' ? 'MooseX::Types::Moose' : $l);
        use_module($library_class);

        $library_class->import({
            -into    => scalar(caller),
            -wrapper => $class,
        }, @{$libraries{ $l }});
    }
    return 1;
}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::Wrapper - Wrap exports from a library

=head1 VERSION

version 0.50

=head1 DESCRIPTION

See L<MooseX::Types/SYNOPSIS> for detailed usage.

=head1 METHODS

=head2 import

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
