package Moose::Exception;
our $VERSION = '2.2101';

use Moose;
use Devel::StackTrace 2.03;

has 'trace' => (
    is            => 'ro',
    isa           => 'Devel::StackTrace',
    builder       => '_build_trace',
    lazy          => 1,
    documentation => "This attribute is read-only and isa L<Devel::StackTrace>. ".
                     'It is lazy & dependent on $exception->message.'
);

has 'message' => (
    is            => 'ro',
    isa           => 'Defined',
    builder       => '_build_message',
    lazy          => 1,
    documentation => "This attribute is read-only and isa Defined. ".
                     "It is lazy and has a default value 'Error'."
);

use overload(
    q{""}    => 'as_string',
    bool     => sub () { 1 },
    fallback => 1,
);

sub _build_trace {
    my $self = shift;

    # skip frames that are method calls on the exception object, which include
    # the object itself in the arguments (but Devel::LeakTrace really ought to
    # be weakening all references in its frames)
    my $skip = 0;
    while (my @c = caller(++$skip)) {
        last if ($c[3] =~ /^(.*)::new$/ || $c[3] =~ /^\S+ (.*)::new \(defined at /)
            && $self->isa($1);
    }
    $skip++;

    Devel::StackTrace->new(
        message => $self->message,
        indent  => 1,
        skip_frames => $skip,
        no_refs => 1,
    );
}

sub _build_message {
    "Error";
}

sub BUILD {
    my $self = shift;
    $self->trace;
}

sub as_string {
    my $self = shift;

    if ( $ENV{MOOSE_FULL_EXCEPTION} ) {
        return $self->trace->as_string;
    }

    my @frames;
    my $last_frame;
    my $in_moose = 1;
    for my $frame ( $self->trace->frames ) {
        if ( $in_moose && $frame->package =~ /^(?:Moose|Class::MOP)(?::|$)/ )
        {
            $last_frame = $frame;
            next;
        }
        elsif ($last_frame) {
            push @frames, $last_frame;
            undef $last_frame;
        }

        $in_moose = 0;
        push @frames, $frame;
    }

    # This would be a somewhat pathological case, but who knows
    return $self->trace->as_string unless @frames;

    my $message = ( shift @frames )->as_string( 1, {} ) . "\n";
    $message .= join q{}, map { $_->as_string( 0, {} ) . "\n" } @frames;

    return $message;
}

__PACKAGE__->meta->make_immutable;
1;

# ABSTRACT: Superclass for Moose internal exceptions

__END__

=pod

=head1 DESCRIPTION

This class contains attributes which are common to all Moose internal
exception classes.

=head1 WARNING WARNING WARNING

If you're writing your own exception classes, you should instead prefer
the L<Throwable> role or the L<Throwable::Error> superclass - this is
effectively a cut-down internal fork of the latter, and not designed
for use in user code.

Of course if you're writing metaclass traits, it would then make sense to
subclass the relevant Moose exceptions - but only then.

=head1 METHODS

=head2 $exception->message

This attribute contains the exception message.

Every subclass of L<Moose::Exception> is expected to override
C<_build_message> method in order to construct this value.

=head2 $exception->trace

This attribute contains the stack trace for the given exception. It returns a
L<Devel::StackTrace> object.

=head2 $exception->as_string

This method returns a stringified form of the exception, including a stack
trace. By default, this method skips Moose-internal stack frames until it sees
a caller outside of the Moose core. If the C<MOOSE_FULL_EXCEPTION> environment
variable is true, these frames are included.

=head1 SEE ALSO

=over 4

=item * L<Moose::Manual::Exceptions>

=back

=cut
