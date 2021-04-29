package Moose::Exception::MustSpecifyAtleastOneRoleToApplicant;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'applicant' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1,
);

sub _build_message {
    my $self = shift;
    "Must specify at least one role to apply to ".$self->applicant;
}

__PACKAGE__->meta->make_immutable;
1;
