package Moose::Deprecated;
our $VERSION = '2.2101';

use strict;
use warnings;

use Package::DeprecationManager 0.07 -deprecations => {
    'non-arrayref form of enum'         => '2.1100',
    'non-arrayref form of duck_type'    => '2.1100',
    },
    -ignore => [qr/^(?:Class::MOP|Moose)(?:::)?/],
    ;

1;

# ABSTRACT: Manages deprecation warnings for Moose

__END__

=pod

=head1 DESCRIPTION

    use Moose::Deprecated -api_version => $version;

=head1 FUNCTIONS

This module manages deprecation warnings for features that have been
deprecated in Moose.

If you specify C<< -api_version => $version >>, you can use deprecated features
without warnings. Note that this special treatment is limited to the package
that loads C<Moose::Deprecated>.

=cut
