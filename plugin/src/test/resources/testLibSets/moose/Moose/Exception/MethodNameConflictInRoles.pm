package Moose::Exception::MethodNameConflictInRoles;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has 'conflict' => (
    traits     => ['Array'],
    is         => 'ro',
    isa        => 'ArrayRef[Moose::Meta::Role::Method::Conflicting]',
    handles    => { conflict_methods_count => 'count',
                    get_method_at          => 'get',
                    get_all_methods        => 'elements',
    },
    required   => 1
);

sub _get_method_names {
    my $self = shift;

    return ( $self->conflict_methods_count == 1 ?
        "'".$self->get_method_at(0)->name."'":
        Moose::Util::english_list( map { q{'} . $_->name . q{'} } $self->get_all_methods ) );
}

sub _build_message {
    my $self = shift;
    my $count = $self->conflict_methods_count;
    my $roles = $self->get_method_at(0)->roles_as_english_list;

    if( $count == 1 )
    {
        "Due to a method name conflict in roles "
        .$roles.", the method ".$self->_get_method_names
        ." must be implemented or excluded by '".$self->class_name."'";
    }
    else
    {
        "Due to method name conflicts in roles "
        .$roles.", the methods ".$self->_get_method_names
        ." must be implemented or excluded by '".$self->class_name."'";
    }
}

__PACKAGE__->meta->make_immutable;
1;
