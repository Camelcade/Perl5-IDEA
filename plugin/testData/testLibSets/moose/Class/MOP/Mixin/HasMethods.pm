package Class::MOP::Mixin::HasMethods;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP::Method::Meta;

use Scalar::Util 'blessed', 'reftype';
use Sub::Util 1.40 'set_subname';

use parent 'Class::MOP::Mixin';

sub _meta_method_class { 'Class::MOP::Method::Meta' }

sub _add_meta_method {
    my $self = shift;
    my ($name) = @_;
    my $existing_method = $self->can('find_method_by_name')
                              ? $self->find_method_by_name($name)
                              : $self->get_method($name);
    return if $existing_method
           && $existing_method->isa($self->_meta_method_class);
    $self->add_method(
        $name => $self->_meta_method_class->wrap(
            name                 => $name,
            package_name         => $self->name,
            associated_metaclass => $self,
        )
    );
}

sub wrap_method_body {
    my ( $self, %args ) = @_;

    ( $args{body} && 'CODE' eq reftype $args{body} )
        || $self->_throw_exception( CodeBlockMustBeACodeRef => instance => $self,
                                                                    params   => \%args
                                       );
    $self->method_metaclass->wrap(
        package_name => $self->name,
        %args,
    );
}

sub add_method {
    my ( $self, $method_name, $method ) = @_;
    ( defined $method_name && length $method_name )
        || $self->_throw_exception( MustDefineAMethodName => instance => $self );

    my $package_name = $self->name;

    my $body;
    if ( blessed($method) && $method->isa('Class::MOP::Method') ) {
        $body = $method->body;
        if ( $method->package_name ne $package_name ) {
            $method = $method->clone(
                package_name => $package_name,
                name         => $method_name,
            );
        }

        $method->attach_to_class($self);
    }
    else {
        # If a raw code reference is supplied, its method object is not created.
        # The method object won't be created until required.
        $body = $method;
    }

    $self->_method_map->{$method_name} = $method;

    my ($current_package, $current_name) = Class::MOP::get_code_info($body);

    set_subname($package_name . '::' . $method_name, $body)
        unless defined $current_name && $current_name !~ /^__ANON__/;

    $self->add_package_symbol("&$method_name", $body);

    # we added the method to the method map too, so it's still valid
    $self->update_package_cache_flag;
}

sub _code_is_mine {
    my ( $self, $code ) = @_;

    my ( $code_package, $code_name ) = Class::MOP::get_code_info($code);

    return ( $code_package && $code_package eq $self->name )
        || ( $code_package eq 'constant' && $code_name eq '__ANON__' );
}

sub has_method {
    my ( $self, $method_name ) = @_;

    ( defined $method_name && length $method_name )
        || $self->_throw_exception( MustDefineAMethodName => instance => $self );

    my $method = $self->_get_maybe_raw_method($method_name);
    return if not $method;

    return defined($self->_method_map->{$method_name} = $method);
}

sub get_method {
    my ( $self, $method_name ) = @_;

    ( defined $method_name && length $method_name )
        || $self->_throw_exception( MustDefineAMethodName => instance => $self );

    my $method = $self->_get_maybe_raw_method($method_name);
    return if not $method;

    return $method if blessed($method) && $method->isa('Class::MOP::Method');

    return $self->_method_map->{$method_name} = $self->wrap_method_body(
        body                 => $method,
        name                 => $method_name,
        associated_metaclass => $self,
    );
}

sub _get_maybe_raw_method {
    my ( $self, $method_name ) = @_;

    my $map_entry = $self->_method_map->{$method_name};
    return $map_entry if defined $map_entry;

    my $code = $self->get_package_symbol("&$method_name");

    return unless $code && $self->_code_is_mine($code);

    return $code;
}

sub remove_method {
    my ( $self, $method_name ) = @_;

    ( defined $method_name && length $method_name )
        || $self->_throw_exception( MustDefineAMethodName => instance => $self );

    my $removed_method = delete $self->_method_map->{$method_name};

    $self->remove_package_symbol("&$method_name");

    $removed_method->detach_from_class
        if blessed($removed_method) && $removed_method->isa('Class::MOP::Method');

    # still valid, since we just removed the method from the map
    $self->update_package_cache_flag;

    return $removed_method;
}

sub get_method_list {
    my $self = shift;

    return keys %{ $self->_full_method_map };
}

sub _get_local_methods {
    my $self = shift;

    return values %{ $self->_full_method_map };
}

sub _restore_metamethods_from {
    my $self = shift;
    my ($old_meta) = @_;

    my $package_name = $self->name;

    # Check if Perl debugger is enabled
    my $debugger_enabled = ($^P & 0x10);
    my $debug_method_info;

    for my $method ($old_meta->_get_local_methods) {
        my $method_name = $method->name;

        # Track DB::sub information for this method if debugger is enabled.
        # This contains original method filename and line numbers.
        $debug_method_info = '';
        if ($debugger_enabled) {
            $debug_method_info = $DB::sub{$package_name . "::" . $method_name}
        }

        $method->_make_compatible_with($self->method_metaclass);
        $self->add_method($method_name => $method);

        # Restore method debug information, which can be clobbered by add_method.
        # Note that we handle this here instead of in add_method, because we
        # only want to preserve the original debug info in cases where we are
        # restoring a method, not overwriting a method.
        if ($debugger_enabled && $debug_method_info) {
            $DB::sub{$package_name . "::" . $method_name} = $debug_method_info;
        }
    }
}

sub reset_package_cache_flag  { (shift)->{'_package_cache_flag'} = undef }
sub update_package_cache_flag {
    my $self = shift;
    # NOTE:
    # we can manually update the cache number
    # since we are actually adding the method
    # to our cache as well. This avoids us
    # having to regenerate the method_map.
    # - SL
    $self->{'_package_cache_flag'} = Class::MOP::check_package_cache_flag($self->name);
}

sub _full_method_map {
    my $self = shift;

    my $pkg_gen = Class::MOP::check_package_cache_flag($self->name);

    if (($self->{_package_cache_flag_full} || -1) != $pkg_gen) {
        # forcibly reify all method map entries
        $self->get_method($_)
            for $self->list_all_package_symbols('CODE');
        $self->{_package_cache_flag_full} = $pkg_gen;
    }

    return $self->_method_map;
}

1;

# ABSTRACT: Methods for metaclasses which have methods

__END__

=pod

=head1 DESCRIPTION

This class implements methods for metaclasses which have methods
(L<Class::MOP::Class> and L<Moose::Meta::Role>). See L<Class::MOP::Class> for
API details.

=cut
