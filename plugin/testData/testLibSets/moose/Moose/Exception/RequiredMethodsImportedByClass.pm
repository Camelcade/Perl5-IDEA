package Moose::Exception::RequiredMethodsImportedByClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::Role';

has 'missing_methods' => (
    traits   => ['Array'],
    is       => 'ro',
    isa      => 'ArrayRef[Moose::Meta::Role::Method::Required]',
    handles  => { method_count    => 'count',
                  get_method_at   => 'get',
                  get_all_methods => 'elements',
    },
    required => 1
);

has 'imported_method' => (
    is       => 'ro',
    isa      => 'Moose::Meta::Role::Method::Required',
    required => 1
);

sub _build_message {
    my $self = shift;

    my $noun = $self->method_count == 1 ? 'method' : 'methods';
    my $list = Moose::Util::english_list( map { q{'} . $_ . q{'} } $self->get_all_methods );

    my ($class, $role, $method) = ($self->class_name,
                                   $self->role_name,
                                   $self->imported_method);

    my ($class_quoted, $role_quoted) = ("'".$class."'","'".$role."'");

    "$role_quoted requires the $noun $list "
        . "to be implemented by $class_quoted. "
        . "If you imported functions intending to use them as "
        . "methods, you need to explicitly mark them as such, via "
        . "$class->meta->add_method($method"
        . " => \\&$method)";
}

__PACKAGE__->meta->make_immutable;
1;
