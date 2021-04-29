package Moose::Meta::Method::Destructor;
our $VERSION = '2.2101';

use strict;
use warnings;

use Devel::GlobalDestruction ();
use Scalar::Util 'blessed', 'weaken';
use Try::Tiny;

use parent 'Moose::Meta::Method',
         'Class::MOP::Method::Inlined';

use Moose::Util 'throw_exception';

sub new {
    my $class   = shift;
    my %options = @_;

    (ref $options{options} eq 'HASH')
        || throw_exception( MustPassAHashOfOptions => params => \%options,
                                                      class  => $class
                          );

    ($options{package_name} && $options{name})
        || throw_exception( MustSupplyPackageNameAndName => params => \%options,
                                                            class  => $class
                          );

    my $self = bless {
        # from our superclass
        'body'                 => undef,
        'package_name'         => $options{package_name},
        'name'                 => $options{name},
        # ...
        'options'              => $options{options},
        'definition_context'   => $options{definition_context},
        'associated_metaclass' => $options{metaclass},
    } => $class;

    # we don't want this creating
    # a cycle in the code, if not
    # needed
    weaken($self->{'associated_metaclass'});

    $self->_initialize_body;

    return $self;
}

## accessors

sub options              { (shift)->{'options'}              }

## method

sub is_needed {
    my $self      = shift;
    my $metaclass = shift;

    ( blessed $metaclass && $metaclass->isa('Class::MOP::Class') )
        || throw_exception( MethodExpectedAMetaclassObject => metaclass => $metaclass,
                                                              class     => $self
                          );

    return $metaclass->find_method_by_name("DEMOLISHALL");
}

sub _initialize_body {
    my $self = shift;
    # TODO:
    # the %options should also include a both
    # a call 'initializer' and call 'SUPER::'
    # options, which should cover approx 90%
    # of the possible use cases (even if it
    # requires some adaption on the part of
    # the author, after all, nothing is free)

    my $class = $self->associated_metaclass->name;
    my @source = (
        'sub {',
            'my $self = shift;',
            'return ' . $self->_generate_fallback_destructor('$self'),
                'if Scalar::Util::blessed($self) ne \'' . $class . '\';',
            $self->_generate_DEMOLISHALL('$self'),
            'return;',
        '}',
    );
    warn join("\n", @source) if $self->options->{debug};

    my $code = try {
        $self->_compile_code(source => \@source);
    }
    catch {
        my $source = join("\n", @source);
        throw_exception( CouldNotEvalDestructor => method_destructor_object => $self,
                                                   source                   => $source,
                                                   error                    => $_
                       );
    };

    $self->{'body'} = $code;
}

sub _generate_fallback_destructor {
    my $self = shift;
    my ($inv) = @_;

    return $inv . '->Moose::Object::DESTROY(@_)';
}

sub _generate_DEMOLISHALL {
    my $self = shift;
    my ($inv) = @_;

    my @methods = $self->associated_metaclass->find_all_methods_by_name('DEMOLISH');
    return unless @methods;

    return (
        'local $?;',
        'my $igd = Devel::GlobalDestruction::in_global_destruction;',
        'Try::Tiny::try {',
            (map { $inv . '->' . $_->{class} . '::DEMOLISH($igd);' } @methods),
        '}',
        'Try::Tiny::catch {',
            'die $_;',
        '};',
    );
}


1;

# ABSTRACT: Method Meta Object for destructors

__END__

=pod

=head1 DESCRIPTION

This class is a subclass of L<Class::MOP::Method::Inlined> that
provides Moose-specific functionality for inlining destructors.

To understand this class, you should read the
L<Class::MOP::Method::Inlined> documentation as well.

=head1 INHERITANCE

C<Moose::Meta::Method::Destructor> is a subclass of
L<Moose::Meta::Method> I<and> L<Class::MOP::Method::Inlined>.

=head1 METHODS

=head2 Moose::Meta::Method::Destructor->new(%options)

This constructs a new object. It accepts the following options:

=over 4

=item * package_name

The package for the class in which the destructor is being
inlined. This option is required.

=item * name

The name of the destructor method. This option is required.

=item * metaclass

The metaclass for the class this destructor belongs to. This is
optional, as it can be set later by calling C<<
$metamethod->attach_to_class >>.

=back

=head2 Moose::Meta;:Method::Destructor->is_needed($metaclass)

Given a L<Moose::Meta::Class> object, this method returns a boolean
indicating whether the class needs a destructor. If the class or any
of its parents defines a C<DEMOLISH> method, it needs a destructor.

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
