package MooseX::Types::Base;
# ABSTRACT: Type library base class

our $VERSION = '0.50';

use Moose;

use Carp::Clan qw(^MooseX::Types);
use Sub::Exporter qw(build_exporter);
use Moose::Util::TypeConstraints qw(find_type_constraint);

use namespace::autoclean;

#pod =head1 DESCRIPTION
#pod
#pod You normally won't need to interact with this class by yourself. It is
#pod merely a collection of functionality that type libraries need to
#pod interact with moose and the rest of the L<MooseX::Types> module.
#pod
#pod =cut

my $UndefMsg = q{Unable to find type '%s' in library '%s'};

#pod =head1 METHODS
#pod
#pod =cut

#pod =head2 import
#pod
#pod Provides the import mechanism for your library. See
#pod L<MooseX::Types/"LIBRARY USAGE"> for syntax details on this.
#pod
#pod =cut

sub import {
    my ($class, @args) = @_;

    # filter or create options hash for S:E
    my $options = (@args and (ref($args[0]) eq 'HASH')) ? $args[0] : undef;

    # preserve additional options, to ensure types are installed into the type library's namespace
    my %ex_spec = %{$options || {}};
    delete @ex_spec{ qw(-wrapper -into -full) };

    unless ($options) {
        $options = {};
        unshift @args, $options;
    }

    # all types known to us
    my @types = $class->type_names;

    # determine the wrapper, -into is supported for compatibility reasons
    my $wrapper = $options->{ -wrapper } || 'MooseX::Types';

    $args[0]->{into} = $options->{ -into }
        if exists $options->{ -into };

    my %ex_util;

    TYPE:
    for my $type_short (@types) {

        # find type name and object, create undefined message
        my $type_full = $class->get_type($type_short)
            or croak "No fully qualified type name stored for '$type_short'";
        my $type_cons = find_type_constraint($type_full);
        my $undef_msg = sprintf($UndefMsg, $type_short, $class);

        # the type itself
        push @{$ex_spec{exports}},
            $type_short,
            sub {
                bless $wrapper->type_export_generator($type_short, $type_full),
                    'MooseX::Types::EXPORTED_TYPE_CONSTRAINT';
            };

        # the check helper
        my $check_name = "is_${type_short}";
        push @{$ex_spec{exports}},
            $check_name,
            sub {$wrapper->check_export_generator($type_short, $type_full, $undef_msg)};

        # only export coercion helper if full (for libraries) or coercion is defined
        next TYPE
            unless $options->{ -full }
                or ($type_cons and $type_cons->has_coercion);

        my $coercion_name = "to_${type_short}";
        push @{$ex_spec{exports}},
            $coercion_name,
            sub {$wrapper->coercion_export_generator($type_short, $type_full, $undef_msg)};
        $ex_util{ $type_short }{to}++; # shortcut to remember this exists
    }

    # create S:E exporter and increase export level unless specified explicitly
    my $exporter = build_exporter \%ex_spec;
    $options->{into_level}++
        unless $options->{into};

    # remember requested symbols to determine what helpers to auto-export
    my %was_requested =
        map {($_ => 1)}
            grep {not ref}
                @args;

    # determine which additional symbols (helpers) to export along
    my %add;
    EXPORT:
    for my $type (grep {exists $was_requested{ $_ }} @types) {
        $add{ "is_$type" }++
            unless $was_requested{ "is_$type" };
        next EXPORT
            unless exists $ex_util{ $type }{to};
        $add{ "to_$type" }++
            unless $was_requested{ "to_$type" };
    }

    # and on to the real exporter
    my @new_args = (@args, keys %add);
    return $class->$exporter(@new_args);
}

#pod =head2 get_type
#pod
#pod This returns a type from the library's store by its name.
#pod
#pod =cut

sub get_type {
    my ($class, $type) = @_;

    # useful message if the type couldn't be found
    croak "Unknown type '$type' in library '$class'"
        unless $class->has_type($type);

    # return real name of the type
    return $class->type_storage->{ $type };
}

#pod =head2 type_names
#pod
#pod Returns a list of all known types by their name.
#pod
#pod =cut

sub type_names {
    my ($class) = @_;

    # return short names of all stored types
    return keys %{$class->type_storage};
}

#pod =head2 add_type
#pod
#pod Adds a new type to the library.
#pod
#pod =cut

sub add_type {
    my ($class, $type) = @_;

    # store type with library prefix as real name
    $class->type_storage->{ $type } = "${class}::${type}";
}

#pod =head2 has_type
#pod
#pod Returns true or false depending on if this library knows a type by that
#pod name.
#pod
#pod =cut

sub has_type {
    my ($class, $type) = @_;

    # check if we stored a type under that name
    return !!$class->type_storage->{ $type };
}

#pod =head2 type_storage
#pod
#pod Returns the library's type storage hash reference. You shouldn't use this
#pod method directly unless you know what you are doing. It is not an internal
#pod method because overriding it makes virtual libraries very easy.
#pod
#pod =cut

sub type_storage {
    my ($class) = @_;

    # return a reference to the storage in ourself
    {
        no strict 'refs';
        return \%{$class . '::__MOOSEX_TYPELIBRARY_STORAGE'};
    }
}

#pod =head2 registered_class_types
#pod
#pod Returns the class types registered within this library. Don't use directly.
#pod
#pod =cut

sub registered_class_types {
    my ($class) = @_;

    {
        no strict 'refs';
        return \%{$class . '::__MOOSEX_TYPELIBRARY_CLASS_TYPES'};
    }
}

#pod =head2 register_class_type
#pod
#pod Register a C<class_type> for use in this library by class name.
#pod
#pod =cut

sub register_class_type {
    my ($class, $type) = @_;

    croak "Not a class_type"
        unless $type->isa('Moose::Meta::TypeConstraint::Class');

    $class->registered_class_types->{$type->class} = $type;
}

#pod =head2 get_registered_class_type
#pod
#pod Get a C<class_type> registered in this library by name.
#pod
#pod =cut

sub get_registered_class_type {
    my ($class, $name) = @_;

    $class->registered_class_types->{$name};
}

#pod =head2 registered_role_types
#pod
#pod Returns the role types registered within this library. Don't use directly.
#pod
#pod =cut

sub registered_role_types {
    my ($class) = @_;

    {
        no strict 'refs';
        return \%{$class . '::__MOOSEX_TYPELIBRARY_ROLE_TYPES'};
    }
}

#pod =head2 register_role_type
#pod
#pod Register a C<role_type> for use in this library by role name.
#pod
#pod =cut

sub register_role_type {
    my ($class, $type) = @_;

    croak "Not a role_type"
        unless $type->isa('Moose::Meta::TypeConstraint::Role');

    $class->registered_role_types->{$type->role} = $type;
}

#pod =head2 get_registered_role_type
#pod
#pod Get a C<role_type> registered in this library by role name.
#pod
#pod =cut

sub get_registered_role_type {
    my ($class, $name) = @_;

    $class->registered_role_types->{$name};
}

#pod =head1 SEE ALSO
#pod
#pod L<MooseX::Types::Moose>
#pod
#pod =cut

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::Base - Type library base class

=head1 VERSION

version 0.50

=head1 DESCRIPTION

You normally won't need to interact with this class by yourself. It is
merely a collection of functionality that type libraries need to
interact with moose and the rest of the L<MooseX::Types> module.

=head1 METHODS

=head2 import

Provides the import mechanism for your library. See
L<MooseX::Types/"LIBRARY USAGE"> for syntax details on this.

=head2 get_type

This returns a type from the library's store by its name.

=head2 type_names

Returns a list of all known types by their name.

=head2 add_type

Adds a new type to the library.

=head2 has_type

Returns true or false depending on if this library knows a type by that
name.

=head2 type_storage

Returns the library's type storage hash reference. You shouldn't use this
method directly unless you know what you are doing. It is not an internal
method because overriding it makes virtual libraries very easy.

=head2 registered_class_types

Returns the class types registered within this library. Don't use directly.

=head2 register_class_type

Register a C<class_type> for use in this library by class name.

=head2 get_registered_class_type

Get a C<class_type> registered in this library by name.

=head2 registered_role_types

Returns the role types registered within this library. Don't use directly.

=head2 register_role_type

Register a C<role_type> for use in this library by role name.

=head2 get_registered_role_type

Get a C<role_type> registered in this library by role name.

=head1 SEE ALSO

L<MooseX::Types::Moose>

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Types>
(or L<bug-MooseX-Types@rt.cpan.org|mailto:bug-MooseX-Types@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Robert "phaylon" Sedlacek <rs@474.at>

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2007 by Robert "phaylon" Sedlacek.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
