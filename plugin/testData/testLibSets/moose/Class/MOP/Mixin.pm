package Class::MOP::Mixin;
our $VERSION = '2.2101';

use strict;
use warnings;

use Scalar::Util 'blessed';
use Module::Runtime 'use_module';

sub meta {
    require Class::MOP::Class;
    Class::MOP::Class->initialize( blessed( $_[0] ) || $_[0] );
}

sub _throw_exception {
    my ($class, $exception_type, @args_to_exception) = @_;
    die use_module( "Moose::Exception::$exception_type" )->new( @args_to_exception );
}

1;

# ABSTRACT: Base class for mixin classes

__END__

=pod

=head1 DESCRIPTION

This class provides a few methods which are useful in all metaclasses.

=head1 METHODS

=head2 Class::MOP::Mixin->meta

This returns a L<Class::MOP::Class> object for the mixin class.

=head2 Class::MOP::Mixin->_throw_exception

Throws an exception in the L<Moose::Exception> family. This should ONLY be
used internally -- any callers outside Class::MOP::* should be using the
version in L<Moose::Util> instead.

=cut
