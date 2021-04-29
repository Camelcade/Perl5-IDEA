package Moose::Exception::OverrideConflictInSummation;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

use Moose::Util 'find_meta';

has 'role_application' => (
    is       => 'ro',
    isa      => 'Moose::Meta::Role::Application::RoleSummation',
    required => 1
);

has 'role_names' => (
    traits   => ['Array'],
    is       => 'bare',
    isa      => 'ArrayRef[Str]',
    handles  => {
        role_names      => 'elements',
    },
    required => 1,
    documentation => "This attribute is an ArrayRef containing role names, if you want metaobjects\n".
                     "associated with these role names, then call method roles on the exception object.\n",
);

has 'method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'two_overrides_found' => (
    is       => 'ro',
    isa      => 'Bool',
    required => 1,
    default  => 0
);

sub roles {
    my $self = shift;
    my @role_names = $self->role_names;
    my @roles = map { find_meta($_) } @role_names;
    return @roles;
}

sub _build_message {
    my $self = shift;

    my @roles = $self->role_names;
    my $role_names = join "|", @roles;

    if( $self->two_overrides_found ) {
        return "We have encountered an 'override' method conflict ".
               "during composition (Two 'override' methods of the same name encountered). ".
               "This is a fatal error.";
    }
    else {
        return "Role '$role_names' has encountered an 'override' method conflict " .
               "during composition (A local method of the same name has been found). This " .
               "is a fatal error." ;
    }
}

__PACKAGE__->meta->make_immutable;
1;
