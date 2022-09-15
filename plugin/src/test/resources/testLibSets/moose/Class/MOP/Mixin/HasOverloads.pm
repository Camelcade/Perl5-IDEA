package Class::MOP::Mixin::HasOverloads;
our $VERSION = '2.2101';

use strict;
use warnings;

use Class::MOP::Overload;

use Devel::OverloadInfo 0.005 'overload_info', 'overload_op_info';
use Scalar::Util 'blessed';

use overload ();

use parent 'Class::MOP::Mixin';

sub is_overloaded {
    my $self = shift;
    Devel::OverloadInfo::is_overloaded($self->name);
}

sub get_overload_list {
    my $self = shift;

    my $info = $self->_overload_info;
    return grep { $_ ne 'fallback' } keys %{$info}
}

sub get_all_overloaded_operators {
    my $self = shift;
    return map { $self->_overload_for($_) } $self->get_overload_list;
}

sub has_overloaded_operator {
    my $self = shift;
    my ($op) = @_;
    return defined $self->_overload_info_for($op);
}

sub _overload_map {
    $_[0]->{_overload_map} ||= {};
}

sub get_overloaded_operator {
    my $self = shift;
    my ($op) = @_;
    return $self->_overload_map->{$op} ||= $self->_overload_for($op);
}

use constant _SET_FALLBACK_EACH_TIME => "$]" < 5.120;

sub add_overloaded_operator {
    my $self = shift;
    my ( $op, $overload ) = @_;

    my %p = ( associated_metaclass => $self );
    if ( !ref $overload ) {
        %p = (
            %p,
            operator             => $op,
            method_name          => $overload,
            associated_metaclass => $self,
        );
        $p{method} = $self->get_method($overload)
            if $self->has_method($overload);
        $overload = Class::MOP::Overload->new(%p);
    }
    elsif ( !blessed $overload) {
        my ($coderef_package, $coderef_name) = Class::MOP::get_code_info($overload);
        $overload = Class::MOP::Overload->new(
            operator        => $op,
            coderef         => $overload,
            coderef_name    => $coderef_name,
            coderef_package => $coderef_package,
            %p,
        );
    }

    $overload->attach_to_class($self);
    $self->_overload_map->{$op} = $overload;

    my %overload = (
          $op => $overload->has_coderef
        ? $overload->coderef
        : $overload->method_name
    );

    # Perl 5.10 and earlier appear to have a bug where setting a new
    # overloading operator wipes out the fallback value unless we pass it each
    # time.
    if (_SET_FALLBACK_EACH_TIME) {
        $overload{fallback} = $self->get_overload_fallback_value;
    }

    $self->name->overload::OVERLOAD(%overload);
}

sub remove_overloaded_operator {
    my $self = shift;
    my ($op) = @_;

    delete $self->_overload_map->{$op};

    # overload.pm provides no api for this - but the problem that makes this
    # necessary has been fixed in 5.18
    $self->get_or_add_package_symbol('%OVERLOAD')->{dummy}++
        if "$]" < 5.017000;

    $self->remove_package_symbol('&(' . $op);
}

sub get_overload_fallback_value {
    my $self = shift;
    return ($self->_overload_info_for('fallback') || {})->{value};
}

sub set_overload_fallback_value {
    my $self  = shift;
    my $value = shift;

    $self->name->overload::OVERLOAD( fallback => $value );
}

# We could cache this but we'd need some logic to clear it at all the right
# times, which seems more tedious than it's worth.
sub _overload_info {
    my $self = shift;
    return overload_info( $self->name ) || {};
}

sub _overload_info_for {
    my $self = shift;
    my $op   = shift;
    return overload_op_info( $self->name, $op );
}

sub _overload_for {
    my $self = shift;
    my $op   = shift;

    my $map = $self->_overload_map;
    return $map->{$op} if $map->{$op};

    my $info = $self->_overload_info_for($op);
    return unless $info;

    my %p = (
        operator             => $op,
        associated_metaclass => $self,
    );

    if ( $info->{code} && !$info->{method_name} ) {
        $p{coderef} = $info->{code};
        @p{ 'coderef_package', 'coderef_name' }
            = $info->{code_name} =~ /(.+)::([^:]+)/;
    }
    else {
        $p{method_name} = $info->{method_name};
        if ( $self->has_method( $p{method_name} ) ) {
            $p{method} = $self->get_method( $p{method_name} );
        }
    }

    return $map->{$op} = Class::MOP::Overload->new(%p);
}

1;

# ABSTRACT: Methods for metaclasses which have overloads

__END__

=pod

=head1 DESCRIPTION

This class implements methods for metaclasses which have overloads
(L<Class::MOP::Clas> and L<Moose::Meta::Role>). See L<Class::MOP::Class> for
API details.

=cut
