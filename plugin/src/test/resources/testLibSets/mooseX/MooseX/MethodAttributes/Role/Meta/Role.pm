package MooseX::MethodAttributes::Role::Meta::Role;
# ABSTRACT: metarole role for storing code attributes

our $VERSION = '0.32';

use Moose ();
use Moose::Util::MetaRole;
use Moose::Util qw/find_meta does_role ensure_all_roles/;

use Moose::Role;

use MooseX::MethodAttributes ();
use MooseX::MethodAttributes::Role ();

use namespace::autoclean;

#pod =head1 SYNOPSIS
#pod
#pod     package MyRole;
#pod     use MooseX::MethodAttributes::Role;
#pod
#pod     sub foo : Bar Baz('corge') { ... }
#pod
#pod     package MyClass
#pod     use Moose;
#pod
#pod     with 'MyRole';
#pod
#pod     my $attrs = MyClass->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]
#pod
#pod =head1 DESCRIPTION
#pod
#pod This module is a metaclass role which is applied by L<MooseX::MethodAttributes::Role>, allowing
#pod you to add code attributes to methods in Moose roles.
#pod
#pod These attributes can then be found by introspecting the role metaclass, and are automatically copied
#pod into any classes or roles that the role is composed onto.
#pod
#pod =head1 CAVEATS
#pod
#pod =over
#pod
#pod =item *
#pod
#pod Currently roles with attributes cannot have methods excluded
#pod or aliased, and will in turn confer this property onto any roles they
#pod are composed onto.
#pod
#pod =back
#pod
#pod =cut

with qw/
    MooseX::MethodAttributes::Role::Meta::Map
    MooseX::MethodAttributes::Role::Meta::Role::Application
/;

$Moose::VERSION >= 0.9301
    ? around composition_class_roles => sub {
    my ($orig, $self) = @_;
    return $self->$orig,
        'MooseX::MethodAttributes::Role::Meta::Role::Application::Summation';
}
    : has '+composition_class_roles' => (
    default => sub {[ 'MooseX::MethodAttributes::Role::Meta::Role::Application::Summation' ]},
);

#pod =method initialize
#pod
#pod Ensures that the package containing the role methods does the
#pod L<MooseX::MethodAttributes::Role::AttrContainer> role during initialisation,
#pod which in turn is responsible for capturing the method attributes on the class
#pod and registering them with the metaclass.
#pod
#pod =cut

after 'initialize' => sub {
    my ($self, $class, %args) = @_;
    ensure_all_roles($class, 'MooseX::MethodAttributes::Role::AttrContainer');
};

#pod =method method_metaclass
#pod
#pod Wraps the normal method and ensures that the method metaclass performs the
#pod L<MooseX::MethodAttributes::Role::Meta::Method> role, which allows you to
#pod introspect the attributes from the method objects returned by the MOP when
#pod querying the metaclass.
#pod
#pod =cut

# FIXME - Skip this logic if the method metaclass already does the right role?
around method_metaclass => sub {
    my $orig = shift;
    my $self = shift;
    return $self->$orig(@_) if scalar @_;
    Moose::Meta::Class->create_anon_class(
        superclasses => [ $self->$orig ],
        roles        => [ qw/
            MooseX::MethodAttributes::Role::Meta::Method
        / ],
        cache        => 1,
    )->name();
};


sub _copy_attributes {
    my ($self, $thing) = @_;

    push @{$thing->_method_attribute_list}, @{$self->_method_attribute_list};
    @{$thing->_method_attribute_map}{ (keys(%{$self->_method_attribute_map}), keys(%{$thing->_method_attribute_map})) }
        = (values(%{$self->_method_attribute_map}), values(%{$thing->_method_attribute_map}));
};

# This allows you to say use Moose::Role -traits => 'MethodAttributes'
# This is replaced by MooseX::MethodAttributes::Role, and this trait registration
# is now only present for backwards compatibility reasons.
package # Hide from PAUSE
Moose::Meta::Role::Custom::Trait::MethodAttributes;

sub register_implementation {'MooseX::MethodAttributes::Role::Meta::Role'}

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::MethodAttributes::Role::Meta::Role - metarole role for storing code attributes

=head1 VERSION

version 0.32

=head1 SYNOPSIS

    package MyRole;
    use MooseX::MethodAttributes::Role;

    sub foo : Bar Baz('corge') { ... }

    package MyClass
    use Moose;

    with 'MyRole';

    my $attrs = MyClass->meta->get_method('foo')->attributes; # ["Bar", "Baz('corge')"]

=head1 DESCRIPTION

This module is a metaclass role which is applied by L<MooseX::MethodAttributes::Role>, allowing
you to add code attributes to methods in Moose roles.

These attributes can then be found by introspecting the role metaclass, and are automatically copied
into any classes or roles that the role is composed onto.

=head1 METHODS

=head2 initialize

Ensures that the package containing the role methods does the
L<MooseX::MethodAttributes::Role::AttrContainer> role during initialisation,
which in turn is responsible for capturing the method attributes on the class
and registering them with the metaclass.

=head2 method_metaclass

Wraps the normal method and ensures that the method metaclass performs the
L<MooseX::MethodAttributes::Role::Meta::Method> role, which allows you to
introspect the attributes from the method objects returned by the MOP when
querying the metaclass.

=head1 CAVEATS

=over

=item *

Currently roles with attributes cannot have methods excluded
or aliased, and will in turn confer this property onto any roles they
are composed onto.

=back

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-MethodAttributes>
(or L<bug-MooseX-MethodAttributes@rt.cpan.org|mailto:bug-MooseX-MethodAttributes@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHORS

=over 4

=item *

Florian Ragwitz <rafl@debian.org>

=item *

Tomas Doran <bobtfish@bobtfish.net>

=back

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2009 by Florian Ragwitz.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
