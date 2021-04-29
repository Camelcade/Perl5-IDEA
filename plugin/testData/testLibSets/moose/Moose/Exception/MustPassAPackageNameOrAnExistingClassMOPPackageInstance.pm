package Moose::Exception::MustPassAPackageNameOrAnExistingClassMOPPackageInstance;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::ParamsHash';

has 'class' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    "You must pass a package name or an existing Class::MOP::Package instance";
}

__PACKAGE__->meta->make_immutable;
1;
