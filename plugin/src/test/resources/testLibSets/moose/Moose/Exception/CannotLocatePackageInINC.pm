package Moose::Exception::CannotLocatePackageInINC;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

has 'INC' => (
    is       => 'ro',
    isa      => 'ArrayRef',
    required => 1
);

has 'possible_packages' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'metaclass_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'type' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    my $self = shift;
    my $possible_packages = $self->possible_packages;
    my @inc = @{$self->INC};

    return "Can't locate $possible_packages in \@INC (\@INC contains: @INC)."
}

__PACKAGE__->meta->make_immutable;
1;
