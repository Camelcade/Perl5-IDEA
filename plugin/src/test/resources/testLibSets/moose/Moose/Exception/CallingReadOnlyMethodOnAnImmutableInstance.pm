package Moose::Exception::CallingReadOnlyMethodOnAnImmutableInstance;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "The '".$self->method_name."' method is read-only when called on an immutable instance";
}

__PACKAGE__->meta->make_immutable;
1;
