package Moose::Exception::PackageDoesNotUseMooseExporter;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'package' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'is_loaded' => (
    is       => 'ro',
    isa      => 'Bool',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $package = $self->package;
    return "Package in also ($package) does not seem to "
           . "use Moose::Exporter"
           . ( $self->is_loaded ? "" : " (is it loaded?)" );
}

__PACKAGE__->meta->make_immutable;
1;
