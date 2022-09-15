package Moose::Meta::Method::Delegation;
our $VERSION = '2.2101';

use strict;
use warnings;

use Scalar::Util 'blessed', 'weaken';
use Try::Tiny;

use parent 'Moose::Meta::Method',
         'Class::MOP::Method::Generated';

use Moose::Util 'throw_exception';

sub new {
    my $class   = shift;
    my %options = @_;

    ( exists $options{attribute} )
        || throw_exception( MustSupplyAnAttributeToConstructWith => params => \%options,
                                                                    class  => $class
                          );

    ( blessed( $options{attribute} )
            && $options{attribute}->isa('Moose::Meta::Attribute') )
        || throw_exception( MustSupplyAMooseMetaAttributeInstance => params => \%options,
                                                                     class  => $class
                          );

    ( $options{package_name} && $options{name} )
        || throw_exception( MustSupplyPackageNameAndName => params => \%options,
                                                            class  => $class
                          );

    ( $options{delegate_to_method} && ( !ref $options{delegate_to_method} )
            || ( 'CODE' eq ref $options{delegate_to_method} ) )
        || throw_exception( MustSupplyADelegateToMethod => params => \%options,
                                                           class  => $class
                          );

    exists $options{curried_arguments}
        || ( $options{curried_arguments} = [] );

    ( $options{curried_arguments} &&
        ( 'ARRAY' eq ref $options{curried_arguments} ) )
        || throw_exception( MustSupplyArrayRefAsCurriedArguments => params     => \%options,
                                                                    class_name => $class
                          );

    my $self = $class->_new( \%options );

    weaken( $self->{'attribute'} );

    $self->_initialize_body;

    return $self;
}

sub _new {
    my $class = shift;
    my $options = @_ == 1 ? $_[0] : {@_};

    return bless $options, $class;
}

sub curried_arguments { (shift)->{'curried_arguments'} }

sub associated_attribute { (shift)->{'attribute'} }

sub delegate_to_method { (shift)->{'delegate_to_method'} }

sub _initialize_body {
    my $self = shift;

    my $method_to_call = $self->delegate_to_method;
    return $self->{body} = $method_to_call
        if ref $method_to_call;

    # We don't inline because it's faster, we do it because when the method is
    # inlined, any errors thrown because of the delegated method have a _much_
    # nicer stack trace, as the trace doesn't include any Moose internals.
    $self->{body} = $self->_generate_inline_method;

    return;
}

sub _generate_inline_method {
    my $self = shift;

    my $attr = $self->associated_attribute;
    my $delegate = $self->delegate_to_method;

    my $method_name = B::perlstring( $self->name );
    my $attr_name   = B::perlstring( $self->associated_attribute->name );

    my $undefined_attr_throw = $self->_inline_throw_exception(
        'AttributeValueIsNotDefined',
        sprintf( <<'EOF', $method_name, $attr_name ) );
method    => $self->meta->find_method_by_name(%s),
instance  => $self,
attribute => $self->meta->find_attribute_by_name(%s),
EOF

    my $not_an_object_throw = $self->_inline_throw_exception(
        'AttributeValueIsNotAnObject',
        sprintf( <<'EOF', $method_name, $attr_name ) );
method      => $self->meta->find_method_by_name(%s),
instance    => $self,
attribute   => $self->meta->find_attribute_by_name(%s),
given_value => $proxy,
EOF

    my $get_proxy
        = $attr->has_read_method ? $attr->get_read_method : '$reader';

    my $args = @{ $self->curried_arguments } ? '@curried, @_' : '@_';
    my $source = sprintf(
        <<'EOF', $get_proxy, $undefined_attr_throw, $not_an_object_throw, $delegate, $args );
sub {
    my $self = shift;

    my $proxy = $self->%s;
    if ( !defined $proxy ) {
        %s;
    }
    elsif ( ref $proxy && !Scalar::Util::blessed($proxy) ) {
        %s;
    }
    return $proxy->%s( %s );
}
EOF

    my $description
        = 'inline delegation in '
        . $self->package_name . ' for '
        . $attr->name . '->'
        . $delegate;

    my $definition = $attr->definition_context;
    # While all attributes created in the usual way (via Moose's has()) will
    # define this, there's no guarantee that this must be defined. For
    # example, when Moo inflates a class to Moose it does not define these (as
    # of Moo 2.003).
    $description .= " (attribute declared in $definition->{file} at line $definition->{line})"
        if defined $definition->{file} && defined $definition->{line};

    return try {
        $self->_compile_code(
            source      => $source,
            description => $description,
        );
    }
    catch {
        $self->_throw_exception(
            'CouldNotGenerateInlineAttributeMethod',
            instance => $self,
            error    => $_,
            option   => 'handles for ' . $attr->name . '->' . $delegate,
        );
    };
}

sub _eval_environment {
    my $self = shift;

    my %env;
    if ( @{ $self->curried_arguments } ) {
        $env{'@curried'} = $self->curried_arguments;
    }

    unless ( $self->associated_attribute->has_read_method ) {
        $env{'$reader'} = \( $self->_get_delegate_accessor );
    }

    return \%env;
}

sub _get_delegate_accessor {
    my $self = shift;

    my $accessor = $self->associated_attribute->get_read_method_ref;

    # If it's blessed it's a Moose::Meta::Method
    return blessed $accessor
        ? ( $accessor->body )
        : $accessor;
}

1;

# ABSTRACT: A Moose Method metaclass for delegation methods

__END__

=pod

=head1 DESCRIPTION

This is a subclass of L<Moose::Meta::Method> for delegation
methods.

=head1 METHODS

=head2 Moose::Meta::Method::Delegation->new(%options)

This creates the delegation methods based on the provided C<%options>.

=over 4

=item I<attribute>

This must be an instance of C<Moose::Meta::Attribute> which this
accessor is being generated for. This options is B<required>.

=item I<delegate_to_method>

The method in the associated attribute's value to which we
delegate. This can be either a method name or a code reference.

=item I<curried_arguments>

An array reference of arguments that will be prepended to the argument list for
any call to the delegating method.

=back

=head2 $metamethod->associated_attribute

Returns the attribute associated with this method.

=head2 $metamethod->curried_arguments

Return any curried arguments that will be passed to the delegated method.

=head2 $metamethod->delegate_to_method

Returns the method to which this method delegates, as passed to the
constructor.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
