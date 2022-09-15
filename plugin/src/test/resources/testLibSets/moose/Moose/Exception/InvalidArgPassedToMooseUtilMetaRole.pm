package Moose::Exception::InvalidArgPassedToMooseUtilMetaRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'argument' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $error = 'When using Moose::Util::MetaRole, you must pass a Moose class name,'
        . ' role name, metaclass object, or metarole object.';

    my $arg = $self->argument;
    my $found = blessed $arg ? $arg : Class::MOP::class_of($arg);

    my $error2;

    if ( defined $found && blessed $found ) {
        $error2 = " You passed ".$arg.", and we resolved this to a "
            . ( blessed $found )
            . ' object.';
    }
    elsif ( !defined $found ) {
        $error2 = " You passed ".( defined $arg ? $arg : "undef" ).", and this did not resolve to a metaclass or metarole."
            . ' Maybe you need to call Moose->init_meta to initialize the metaclass first?';
    }
    else {
        $error2 = " You passed an undef."
            . ' Maybe you need to call Moose->init_meta to initialize the metaclass first?';
    }

    $error.$error2;
}

__PACKAGE__->meta->make_immutable;
1;
