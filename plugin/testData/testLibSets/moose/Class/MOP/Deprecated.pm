package Class::MOP::Deprecated;
our $VERSION = '2.2101';

use strict;
use warnings;

use Package::DeprecationManager -deprecations => {
    'Class::Load wrapper functions' => '2.1100',
};

1;

# ABSTRACT: Manages deprecation warnings for Class::MOP

__END__

=pod

=head1 DESCRIPTION

    use Class::MOP::Deprecated -api_version => $version;

=head1 FUNCTIONS

This module manages deprecation warnings for features that have been
deprecated in Class::MOP.

If you specify C<< -api_version => $version >>, you can use deprecated features
without warnings. Note that this special treatment is limited to the package
that loads C<Class::MOP::Deprecated>.

=cut
