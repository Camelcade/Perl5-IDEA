package Moose::Meta::Attribute::Native::Trait;
our $VERSION = '2.2101';

use Moose::Role;
use Module::Runtime 'require_module';
use Moose::Deprecated;
use Moose::Util 'throw_exception';
use Moose::Util::TypeConstraints;

requires '_helper_type';

before '_process_options' => sub {
    my ( $self, $name, $options ) = @_;

    $self->_check_helper_type( $options, $name );
};

sub _check_helper_type {
    my ( $self, $options, $name ) = @_;

    my $type = $self->_helper_type;

    $options->{isa} = $type
        unless exists $options->{isa};

    my $isa;
    my $isa_name;

    if (   blessed( $options->{isa} )
        && $options->{isa}->can('does')
        && $options->{isa}->does('Specio::Constraint::Role::Interface') ) {

        $isa = $options->{isa};
        require Specio::Library::Builtins;
        return if $isa->is_a_type_of( Specio::Library::Builtins::t($type) );
        $isa_name = $isa->name() || $isa->description();
    }
    else {
        $isa = Moose::Util::TypeConstraints::find_or_create_type_constraint(
            $options->{isa} );
        return if $isa->is_a_type_of($type);
        $isa_name = $isa->name();
    }

    throw_exception( WrongTypeConstraintGiven => required_type  => $type,
                                                 given_type     => $isa_name,
                                                 attribute_name => $name,
                                                 params         => $options
                   );
}

before 'install_accessors' => sub { (shift)->_check_handles_values };

sub _check_handles_values {
    my $self = shift;

    my %handles = $self->_canonicalize_handles;

    for my $original_method ( values %handles ) {
        my $name = $original_method->[0];

        my $accessor_class = $self->_native_accessor_class_for($name);

        ( $accessor_class && $accessor_class->can('new') )
            || confess
            "$name is an unsupported method type - $accessor_class";
    }
}

around '_canonicalize_handles' => sub {
    shift;
    my $self    = shift;
    my $handles = $self->handles;

    return unless $handles;

    unless ( 'HASH' eq ref $handles ) {
       throw_exception( HandlesMustBeAHashRef => instance      => $self,
                                                 given_handles => $handles
                      );
    }

    return
        map { $_ => $self->_canonicalize_handles_value( $handles->{$_} ) }
        keys %$handles;
};

sub _canonicalize_handles_value {
    my $self  = shift;
    my $value = shift;

    if ( ref $value && 'ARRAY' ne ref $value ) {
        throw_exception( InvalidHandleValue => instance     => $self,
                                               handle_value => $value
                       );
    }

    return ref $value ? $value : [$value];
}

around '_make_delegation_method' => sub {
    my $next = shift;
    my ( $self, $handle_name, $method_to_call ) = @_;

    my ( $name, @curried_args ) = @$method_to_call;

    my $accessor_class = $self->_native_accessor_class_for($name);

    die "Cannot find an accessor class for $name"
        unless $accessor_class && $accessor_class->can('new');

    return $accessor_class->new(
        name               => $handle_name,
        package_name       => $self->associated_class->name,
        delegate_to_method => $name,
        attribute          => $self,
        is_inline          => 1,
        curried_arguments  => \@curried_args,
        root_types         => [ $self->_root_types ],
    );
};

sub _root_types {
    return $_[0]->_helper_type;
}

sub _native_accessor_class_for {
    my ( $self, $suffix ) = @_;

    my $role
        = 'Moose::Meta::Method::Accessor::Native::'
        . $self->_native_type . '::'
        . $suffix;

    require_module($role);
    return Moose::Meta::Class->create_anon_class(
        superclasses =>
            [ $self->accessor_metaclass, $self->delegation_metaclass ],
        roles => [$role],
        cache => 1,
    )->name;
}

sub _build_native_type {
    my $self = shift;

    for my $role_name ( map { $_->name } $self->meta->calculate_all_roles ) {
        return $1 if $role_name =~ /::Native::Trait::(\w+)$/;
    }

    throw_exception( CannotCalculateNativeType => instance => $self );
}

has '_native_type' => (
    is      => 'ro',
    isa     => 'Str',
    lazy    => 1,
    builder => '_build_native_type',
);

no Moose::Role;
no Moose::Util::TypeConstraints;

1;

# ABSTRACT: Shared role for native delegation traits

__END__

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=head1 SEE ALSO

Documentation for Moose native traits can be found in
L<Moose::Meta::Attribute::Native>.

=cut
