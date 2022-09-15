#  You may distribute under the terms of either the GNU General Public License
#  or the Artistic License (the same terms as Perl itself)
#
#  (C) Paul Evans, 2011-2020 -- leonerd@leonerd.org.uk

package Future;

use strict;
use warnings;
no warnings 'recursion'; # Disable the "deep recursion" warning

our $VERSION = '0.44';

use Carp qw(); # don't import croak
use Scalar::Util qw(weaken blessed reftype);
use B qw(svref_2object);
use Time::HiRes qw(gettimeofday tv_interval);

# we are not overloaded, but we want to check if other objects are
require overload;

require Future::Exception;

our @CARP_NOT = qw(Future::Utils);

use constant DEBUG => !!$ENV{PERL_FUTURE_DEBUG};

our $TIMES = DEBUG || $ENV{PERL_FUTURE_TIMES};

=head1 NAME

C<Future> - represent an operation awaiting completion

=head1 SYNOPSIS

   my $future = Future->new;

   perform_some_operation(
      on_complete => sub {
         $future->done( @_ );
      }
   );

   $future->on_ready( sub {
      say "The operation is complete";
   } );

=head1 DESCRIPTION

A C<Future> object represents an operation that is currently in progress, or
has recently completed. It can be used in a variety of ways to manage the flow
of control, and data, through an asynchronous program.

Some futures represent a single operation and are explicitly marked as ready
by calling the C<done> or C<fail> methods. These are called "leaf" futures
here, and are returned by the C<new> constructor.

Other futures represent a collection of sub-tasks, and are implicitly marked
as ready depending on the readiness of their component futures as required.
These are called "convergent" futures here as they converge control and
data-flow back into one place. These are the ones returned by the various
C<wait_*> and C<need_*> constructors.

It is intended that library functions that perform asynchronous operations
would use future objects to represent outstanding operations, and allow their
calling programs to control or wait for these operations to complete. The
implementation and the user of such an interface would typically make use of
different methods on the class. The methods below are documented in two
sections; those of interest to each side of the interface.

It should be noted however, that this module does not in any way provide an
actual mechanism for performing this asynchronous activity; it merely provides
a way to create objects that can be used for control and data flow around
those operations. It allows such code to be written in a neater,
forward-reading manner, and simplifies many common patterns that are often
involved in such situations.

See also L<Future::Utils> which contains useful loop-constructing functions,
to run a future-returning function repeatedly in a loop.

Unless otherwise noted, the following methods require at least version
I<0.08>.

=head2 FAILURE CATEGORIES

While not directly required by C<Future> or its related modules, a growing
convention of C<Future>-using code is to encode extra semantics in the
arguments given to the C<fail> method, to represent different kinds of
failure.

The convention is that after the initial message string as the first required
argument (intended for display to humans), the second argument is a short
lowercase string that relates in some way to the kind of failure that
occurred. Following this is a list of details about that kind of failure,
whose exact arrangement or structure are determined by the failure category.
For example, L<IO::Async> and L<Net::Async::HTTP> use this convention to
indicate at what stage a given HTTP request has failed:

   ->fail( $message, http => ... )  # an HTTP-level error during protocol
   ->fail( $message, connect => ... )  # a TCP-level failure to connect a
                                       # socket
   ->fail( $message, resolve => ... )  # a resolver (likely DNS) failure
                                       # to resolve a hostname

By following this convention, a module remains consistent with other
C<Future>-based modules, and makes it easy for program logic to gracefully
handle and manage failures by use of the C<catch> method.

=head2 SUBCLASSING

This class easily supports being subclassed to provide extra behavior, such as
giving the C<get> method the ability to block and wait for completion. This
may be useful to provide C<Future> subclasses with event systems, or similar.

Each method that returns a new future object will use the invocant to
construct its return value. If the constructor needs to perform per-instance
setup it can override the C<new> method, and take context from the given
instance.

   sub new
   {
      my $proto = shift;
      my $self = $proto->SUPER::new;

      if( ref $proto ) {
         # Prototype was an instance
      }
      else {
         # Prototype was a class
      }

      return $self;
   }

If an instance overrides the L</await> method, this will be called by C<get>
and C<failure> if the instance is still pending.

In most cases this should allow future-returning modules to be used as if they
were blocking call/return-style modules, by simply appending a C<get> call to
the function or method calls.

   my ( $results, $here ) = future_returning_function( @args )->get;

=head2 DEBUGGING

By the time a C<Future> object is destroyed, it ought to have been completed
or cancelled. By enabling debug tracing of objects, this fact can be checked.
If a future object is destroyed without having been completed or cancelled, a
warning message is printed.

   $ PERL_FUTURE_DEBUG=1 perl -MFuture -E 'my $f = Future->new'
   Future=HASH(0xaa61f8) was constructed at -e line 1 and was lost near -e line 0 before it was ready.

Note that due to a limitation of perl's C<caller> function within a C<DESTROY>
destructor method, the exact location of the leak cannot be accurately
determined. Often the leak will occur due to falling out of scope by returning
from a function; in this case the leak location may be reported as being the
line following the line calling that function.

   $ PERL_FUTURE_DEBUG=1 perl -MFuture
   sub foo {
      my $f = Future->new;
   }

   foo();
   print "Finished\n";

   Future=HASH(0x14a2220) was constructed at - line 2 and was lost near - line 6 before it was ready.
   Finished

A warning is also printed in debug mode if a C<Future> object is destroyed
that completed with a failure, but the object believes that failure has not
been reported anywhere.

   $ PERL_FUTURE_DEBUG=1 perl -Mblib -MFuture -E 'my $f = Future->fail("Oops")'
   Future=HASH(0xac98f8) was constructed at -e line 1 and was lost near -e line 0 with an unreported failure of: Oops

Such a failure is considered reported if the C<get> or C<failure> methods are
called on it, or it had at least one C<on_ready> or C<on_fail> callback, or
its failure is propagated to another C<Future> instance (by a sequencing or
converging method).

=head2 Future::AsyncAwait::Awaitable ROLE

Since version 0.43 this module provides the L<Future::AsyncAwait::Awaitable>
API. Subclass authors should note that several of the API methods are provided
by special optimised internal methods, which may require overriding in your
subclass if your internals are different from that of this module.

=cut

=head1 CONSTRUCTORS

=cut

=head2 new

   $future = Future->new

   $future = $orig->new

Returns a new C<Future> instance to represent a leaf future. It will be marked
as ready by any of the C<done>, C<fail>, or C<cancel> methods. It can be
called either as a class method, or as an instance method. Called on an
instance it will construct another in the same class, and is useful for
subclassing.

This constructor would primarily be used by implementations of asynchronous
interfaces.

=cut

# Callback flags
use constant {
    CB_DONE       => 1 << 0, # Execute callback on done
    CB_FAIL       => 1 << 1, # Execute callback on fail
    CB_CANCEL     => 1 << 2, # Execute callback on cancellation

    CB_SELF       => 1 << 3, # Pass $self as first argument
    CB_RESULT     => 1 << 4, # Pass result/failure as a list

    CB_SEQ_ONDONE => 1 << 5, # Sequencing on success (->then)
    CB_SEQ_ONFAIL => 1 << 6, # Sequencing on failure (->else)

    CB_SEQ_IMDONE => 1 << 7, # $code is in fact immediate ->done result
    CB_SEQ_IMFAIL => 1 << 8, # $code is in fact immediate ->fail result
};

use constant CB_ALWAYS => CB_DONE | CB_FAIL | CB_CANCEL;

# Useful for identifying CODE references
sub CvNAME_FILE_LINE {
    my ($code) = @_;
    my $cv = svref_2object($code);

    my $name = join "::", $cv->STASH->NAME, $cv->GV->NAME;
    return $name unless $cv->GV->NAME eq "__ANON__";

    # $cv->GV->LINE isn't reliable, as outside of perl -d mode all anon CODE
    # in the same file actually shares the same GV. :(
    # Walk the optree looking for the first COP
    my $cop = $cv->START;
    $cop = $cop->next while $cop and ref $cop ne "B::COP" and ref $cop ne "B::NULL";

    return $cv->GV->NAME if ref $cop eq "B::NULL";
    sprintf "%s(%s line %d)", $cv->GV->NAME, $cop->file, $cop->line;
}

sub _callable {
    my ($cb) = @_;
    defined $cb and (reftype($cb) eq 'CODE' || overload::Method($cb, '&{}'));
}

sub new {
    my $proto = shift;
    return bless {
        ready     => 0,
        callbacks => [], # [] = [$type, ...]
        (DEBUG ?
            (do {
                my $at = Carp::shortmess("constructed");
                chomp $at;
                $at =~ s/\.$//;
                constructed_at => $at
            })
            : ()),
        ($TIMES ?
            (btime => [ gettimeofday ])
            : ()),
    }, (ref $proto || $proto);
}

*AWAIT_CLONE = sub {shift->new}; # We need to respect subclassing

my $GLOBAL_END;
END {$GLOBAL_END = 1;}

sub DESTROY_debug {
    my $self = shift;
    return if $GLOBAL_END;
    return if $self->{ready} and ($self->{reported} or !$self->{failure});

    my $lost_at = join " line ", (caller)[1, 2];
    # We can't actually know the real line where the last reference was lost;
    # a variable set to 'undef' or close of scope, because caller can't see it;
    # the current op has already been updated. The best we can do is indicate
    # 'near'.

    if ($self->{ready} and $self->{failure}) {
        warn "${\$self->__selfstr} was $self->{constructed_at} and was lost near $lost_at with an unreported failure of: " .
            $self->{failure}[0] . "\n";
    }
    elsif (!$self->{ready}) {
        warn "${\$self->__selfstr} was $self->{constructed_at} and was lost near $lost_at before it was ready.\n";
    }
}
*DESTROY = \&DESTROY_debug if DEBUG;

=head2 done I<(class method)>

=head2 fail I<(class method)>

   $future = Future->done( @values )

   $future = Future->fail( $exception, $category, @details )

I<Since version 0.26.>

Shortcut wrappers around creating a new C<Future> then immediately marking it
as done or failed.

=head2 wrap

   $future = Future->wrap( @values )

I<Since version 0.14.>

If given a single argument which is already a C<Future> reference, this will
be returned unmodified. Otherwise, returns a new C<Future> instance that is
already complete, and will yield the given values.

This will ensure that an incoming argument is definitely a C<Future>, and may
be useful in such cases as adapting synchronous code to fit asynchronous
libraries driven by C<Future>.

=cut

sub wrap {
    my $class = shift;
    my @values = @_;

    if (@values == 1 and blessed $values[0] and $values[0]->isa(__PACKAGE__)) {
        return $values[0];
    }
    else {
        return $class->done(@values);
    }
}

=head2 call

   $future = Future->call( \&code, @args )

I<Since version 0.15.>

A convenient wrapper for calling a C<CODE> reference that is expected to
return a future. In normal circumstances is equivalent to

   $future = $code->( @args )

except that if the code throws an exception, it is wrapped in a new immediate
fail future. If the return value from the code is not a blessed C<Future>
reference, an immediate fail future is returned instead to complain about this
fact.

=cut

sub call {
    my $class = shift;
    my ($code, @args) = @_;

    my $f;
    eval {
        $f = $code->(@args);
        1
    } or $f = $class->fail($@);
    blessed $f and $f->isa("Future") or $f = $class->fail("Expected " . CvNAME_FILE_LINE($code) . " to return a Future");

    return $f;
}

sub _shortmess {
    my $at = Carp::shortmess($_[0]);
    chomp $at;
    $at =~ s/\.$//;
    return $at;
}

sub _mark_ready {
    my $self = shift;
    $self->{ready} = 1;
    $self->{ready_at} = _shortmess $_[0] if DEBUG;

    if ($TIMES) {
        $self->{rtime} = [ gettimeofday ];
    }

    delete $self->{on_cancel};
    $_->[0] and $_->[0]->_revoke_on_cancel($_->[1]) for @{$self->{revoke_when_ready}};
    delete $self->{revoke_when_ready};

    my $callbacks = delete $self->{callbacks} or return;

    my $cancelled = $self->{cancelled};
    my $fail = defined $self->{failure};
    my $done = !$fail && !$cancelled;

    my @result = $done ? @{$self->{result}} :
        $fail ? @{$self->{failure}} :
            ();

    foreach my $cb (@$callbacks) {
        my ($flags, $code) = @$cb;
        my $is_future = blessed($code) && $code->isa("Future");

        next if $done and not ($flags & CB_DONE);
        next if $fail and not ($flags & CB_FAIL);
        next if $cancelled and not ($flags & CB_CANCEL);

        $self->{reported} = 1 if $fail;

        if ($is_future) {
            $done ? $code->done(@result) :
                $fail ? $code->fail(@result) :
                    $code->cancel;
        }
        elsif ($flags & (CB_SEQ_ONDONE | CB_SEQ_ONFAIL)) {
            my (undef, undef, $fseq) = @$cb;
            if (!$fseq) {
                # weaken()ed; it might be gone now
                # This warning should always be printed, even not in DEBUG mode.
                # It's always an indication of a bug
                Carp::carp +(DEBUG ? "${\$self->__selfstr} ($self->{constructed_at})"
                    : "${\$self->__selfstr} $self") .
                    " lost a sequence Future";
                next;
            }

            my $f2;
            if ($done and $flags & CB_SEQ_ONDONE or
                $fail and $flags & CB_SEQ_ONFAIL) {

                if ($flags & CB_SEQ_IMDONE) {
                    $fseq->done(@$code);
                    next;
                }
                elsif ($flags & CB_SEQ_IMFAIL) {
                    $fseq->fail(@$code);
                    next;
                }

                my @args = (
                    ($flags & CB_SELF ? $self : ()),
                    ($flags & CB_RESULT ? @result : ()),
                );

                unless (eval {
                    $f2 = $code->(@args);
                    1
                }) {
                    $fseq->fail($@);
                    next;
                }

                unless (blessed $f2 and $f2->isa("Future")) {
                    $fseq->fail("Expected " . CvNAME_FILE_LINE($code) . " to return a Future");
                    next;
                }

                $fseq->on_cancel($f2);
            }
            else {
                $f2 = $self;
            }

            if ($f2->is_ready) {
                $f2->on_ready($fseq) if !$f2->{cancelled};
            }
            else {
                push @{$f2->{callbacks}}, [ CB_DONE | CB_FAIL, $fseq ];
                weaken($f2->{callbacks}[-1][1]);
            }
        }
        else {
            $code->(
                ($flags & CB_SELF ? $self : ()),
                ($flags & CB_RESULT ? @result : ()),
            );
        }
    }
}

=head1 METHODS

As there are a lare number of methods on this class, they are documented here
in several sections.

=cut

=head1 INSPECTION METHODS

The following methods query the internal state of a Future instance without
modifying it or otherwise causing side-effects.

=cut

=head2 is_ready

   $ready = $future->is_ready

Returns true on a leaf future if a result has been provided to the C<done>
method, failed using the C<fail> method, or cancelled using the C<cancel>
method.

Returns true on a convergent future if it is ready to yield a result,
depending on its component futures.

=cut

sub is_ready {
    my $self = shift;
    return $self->{ready};
}

*AWAIT_IS_READY = \&is_ready;

=head2 is_done

   $done = $future->is_done

Returns true on a future if it is ready and completed successfully. Returns
false if it is still pending, failed, or was cancelled.

=cut

sub is_done {
    my $self = shift;
    return $self->{ready} && !$self->{failure} && !$self->{cancelled};
}

=head2 is_failed

   $failed = $future->is_failed

I<Since version 0.26.>

Returns true on a future if it is ready and it failed. Returns false if it is
still pending, completed successfully, or was cancelled.

=cut

sub is_failed {
    my $self = shift;
    return $self->{ready} && !!$self->{failure}; # boolify
}

=head2 is_cancelled

   $cancelled = $future->is_cancelled

Returns true if the future has been cancelled by C<cancel>.

=cut

sub is_cancelled {
    my $self = shift;
    return $self->{cancelled};
}

*AWAIT_IS_CANCELLED = \&is_cancelled;

=head2 state

   $str = $future->state

I<Since version 0.36.>

Returns a string describing the state of the future, as one of the three
states named above; namely C<done>, C<failed> or C<cancelled>, or C<pending>
if it is none of these.

=cut

sub state {
    my $self = shift;
    return !$self->{ready} ? "pending" :
        DEBUG ? $self->{ready_at} :
            $self->{failure} ? "failed" :
                $self->{cancelled} ? "cancelled" :
                    "done";
}

=head1 IMPLEMENTATION METHODS

These methods would primarily be used by implementations of asynchronous
interfaces.

=cut

=head2 done

   $future->done( @result )

Marks that the leaf future is now ready, and provides a list of values as a
result. (The empty list is allowed, and still indicates the future as ready).
Cannot be called on a convergent future.

If the future is already cancelled, this request is ignored. If the future is
already complete with a result or a failure, an exception is thrown.

=cut

sub done {
    my $self = shift;

    if (ref $self) {
        $self->{cancelled} and return $self;
        $self->{ready} and Carp::croak "${\$self->__selfstr} is already " . $self->state . " and cannot be ->done";
        $self->{subs} and Carp::croak "${\$self->__selfstr} is not a leaf Future, cannot be ->done";
        $self->{result} = [ @_ ];
        $self->_mark_ready("done");
    }
    else {
        $self = $self->new;
        $self->{ready} = 1;
        $self->{ready_at} = _shortmess "done" if DEBUG;
        $self->{result} = [ @_ ];
    }

    return $self;
}

# TODO: For efficiency we can implement better versions of these as individual
#  methods know which case is being invoked
*AWAIT_NEW_DONE = *AWAIT_DONE = \&done;

=head2 fail

   $future->fail( $exception, $category, @details )

Marks that the leaf future has failed, and provides an exception value. This
exception will be thrown by the C<get> method if called. 

The exception must evaluate as a true value; false exceptions are not allowed.
A failure category name and other further details may be provided that will be
returned by the C<failure> method in list context.

If the future is already cancelled, this request is ignored. If the future is
already complete with a result or a failure, an exception is thrown.

If passed a L<Future::Exception> instance (i.e. an object previously thrown by
the C<get>), the additional details will be preserved. This allows the
additional details to be transparently preserved by such code as

   ...
   catch {
      return Future->fail($@);
   }

=cut

sub fail {
    my $self = shift;
    my ($exception, @more) = @_;

    if (ref $exception eq "Future::Exception") {
        @more = ($exception->category, $exception->details);
        $exception = $exception->message;
    }

    $exception or Carp::croak "$self ->fail requires an exception that is true";

    if (ref $self) {
        $self->{cancelled} and return $self;
        $self->{ready} and Carp::croak "${\$self->__selfstr} is already " . $self->state . " and cannot be ->fail'ed";
        $self->{subs} and Carp::croak "${\$self->__selfstr} is not a leaf Future, cannot be ->fail'ed";
        $self->{failure} = [ $exception, @more ];
        $self->_mark_ready("failed");
    }
    else {
        $self = $self->new;
        $self->{ready} = 1;
        $self->{ready_at} = _shortmess "failed" if DEBUG;
        $self->{failure} = [ $exception, @more ];
    }

    return $self;
}

# TODO: For efficiency we can implement better versions of these as individual
#  methods know which case is being invoked
*AWAIT_NEW_FAIL = *AWAIT_FAIL = \&fail;

=head2 die

   $future->die( $message, $category, @details )

I<Since version 0.09.>

A convenient wrapper around C<fail>. If the exception is a non-reference that
does not end in a linefeed, its value will be extended by the file and line
number of the caller, similar to the logic that C<die> uses.

Returns the C<$future>.

=cut

sub die :method {
    my $self = shift;
    my ($exception, @more) = @_;

    if (!ref $exception and $exception !~ m/\n$/) {
        $exception .= sprintf " at %s line %d\n", (caller)[1, 2];
    }

    $self->fail($exception, @more);
}

=head2 on_cancel

   $future->on_cancel( $code )

If the future is not yet ready, adds a callback to be invoked if the future is
cancelled by the C<cancel> method. If the future is already ready the method
is ignored.

If the future is later cancelled, the callbacks will be invoked in the reverse
order to that in which they were registered.

   $on_cancel->( $future )

If passed another C<Future> instance, the passed instance will be cancelled
when the original future is cancelled. In this case, the reference is only
strongly held while the target future remains pending. If it becomes ready,
then there is no point trying to cancel it, and so it is removed from the
originating future's cancellation list.

=cut

sub on_cancel {
    my $self = shift;
    my ($code) = @_;

    my $is_future = blessed($code) && $code->isa("Future");
    $is_future or _callable($code) or
        Carp::croak "Expected \$code to be callable or a Future in ->on_cancel";

    $self->{ready} and return $self;

    push @{$self->{on_cancel}}, $code;
    if ($is_future) {
        push @{$code->{revoke_when_ready}}, my $r = [ $self, \$self->{on_cancel}[-1] ];
        weaken($r->[0]);
        weaken($r->[1]);
    }

    return $self;
}

# An optimised version for Awaitable role
sub AWAIT_ON_CANCEL {
    my $self = shift;
    my ($f2) = @_;

    push @{$self->{on_cancel}}, $f2;
    push @{$f2->{revoke_when_ready}}, my $r = [ $self, \$self->{on_cancel}[-1] ];
    weaken($r->[0]);
    weaken($r->[1]);
}

sub _revoke_on_cancel {
    my $self = shift;
    my ($ref) = @_;

    undef $$ref;
    $self->{empty_on_cancel_slots}++;

    my $on_cancel = $self->{on_cancel} or return;

    # If the list is nontrivally large and over half-empty / under half-full, compact it
    if (@$on_cancel >= 8 and $self->{empty_on_cancel_slots} >= 0.5 * @$on_cancel) {
        # We can't grep { defined } because that will break all the existing SCALAR refs
        my $idx = 0;
        while ($idx < @$on_cancel) {
            defined $on_cancel->[$idx] and $idx++, next;
            splice @$on_cancel, $idx, 1, ();
        }
        $self->{empty_on_cancel_slots} = 0;
    }
}

=head1 USER METHODS

These methods would primarily be used by users of asynchronous interfaces, on
objects returned by such an interface.

=cut

=head2 on_ready

   $future->on_ready( $code )

If the future is not yet ready, adds a callback to be invoked when the future
is ready. If the future is already ready, invokes it immediately.

In either case, the callback will be passed the future object itself. The
invoked code can then obtain the list of results by calling the C<get> method.

   $on_ready->( $future )

If passed another C<Future> instance, the passed instance will have its
C<done>, C<fail> or C<cancel> methods invoked when the original future
completes successfully, fails, or is cancelled respectively.

Returns the C<$future>.

=cut

sub on_ready {
    my $self = shift;
    my ($code) = @_;

    my $is_future = blessed($code) && $code->isa("Future");
    $is_future or _callable($code) or
        Carp::croak "Expected \$code to be callable or a Future in ->on_ready";

    if ($self->{ready}) {
        my $fail = defined $self->{failure};
        my $done = !$fail && !$self->{cancelled};

        $self->{reported} = 1 if $fail;

        $is_future ? ($done ? $code->done(@{$self->{result}}) :
            $fail ? $code->fail(@{$self->{failure}}) :
                $code->cancel)
            : $code->($self);
    }
    else {
        push @{$self->{callbacks}}, [ CB_ALWAYS | CB_SELF, $self->wrap_cb(on_ready => $code) ];
    }

    return $self;
}

# An optimised version for Awaitable role
sub AWAIT_ON_READY {
    my $self = shift;
    my ($code) = @_;
    push @{$self->{callbacks}}, [ CB_ALWAYS | CB_SELF, $self->wrap_cb(on_ready => $code) ];
}

=head2 result

   @result = $future->result

   $result = $future->result

I<Since version 0.44.>

If the future is ready and completed successfully, returns the list of
results that had earlier been given to the C<done> method on a leaf future,
or the list of component futures it was waiting for on a convergent future. In
scalar context it returns just the first result value.

If the future is ready but failed, this method raises as an exception the
failure that was given to the C<fail> method. If additional details were given
to the C<fail> method, an exception object is constructed to wrap them of type
L<Future::Exception>.

If the future was cancelled or is not yet ready an exception is thrown.

=cut

sub result {
    my $self = shift;
    $self->{ready} or
        Carp::croak("${\$self->__selfstr} is not yet ready");
    if (my $failure = $self->{failure}) {
        $self->{reported} = 1;
        my $exception = $failure->[0];
        $exception = Future::Exception->new(@$failure) if @$failure > 1;
        !ref $exception && $exception =~ m/\n$/ ? CORE::die $exception : Carp::croak $exception;
    }
    $self->{cancelled} and Carp::croak "${\$self->__selfstr} was cancelled";
    return $self->{result}->[0] unless wantarray;
    return @{$self->{result}};
}

# TODO do we want to rename this AWAIT_RESULT ?
*AWAIT_GET = \&result;

=head2 get

   @result = $future->get

   $result = $future->get

If the future is ready, returns the result or throws the failure exception as
per L</result>.

If it is not yet ready then L</block_until_ready> is invoked to wait for a
ready state, and the result returned as above.

=cut

sub get {
    my $self = shift;
    $self->await unless $self->{ready};
    return $self->result;
}

=head2 await

   $f = $f->await

I<Since version 0.44.>

Blocks until the future instance is no longer pending.

Returns the invocant future itself, so it is useful for chaining.

Usually, calling code would either force the future using L</get>, or use
either C<then> chaining or C<async/await> syntax to wait for results. This
method is useful in cases where the exception-throwing part of C<get> is not
required, perhaps because other code will be testing the result using
L</is_done> or similar.

   if( $f->await->is_done ) {
      ...
   }

This method is intended for subclasses to override. The default implementation
will throw an exception if called on a still-pending instance.

=cut

sub await {
    my $self = shift;
    return $self if $self->{ready};
    Carp::croak "$self is not yet complete and does not provide ->await";
}

=head2 block_until_ready

   $f = $f->block_until_ready

I<Since version 0.40.>

Now a synonym for L</await>. New code should invoke C<await> directly.

=cut

sub block_until_ready {
    my $self = shift;
    return $self->await;
}

=head2 unwrap

   @values = Future->unwrap( @values )

I<Since version 0.26.>

If given a single argument which is a C<Future> reference, this method will
call C<get> on it and return the result. Otherwise, it returns the list of
values directly in list context, or the first value in scalar. Since it
involves an implicit blocking wait, this method can only be used on immediate
futures or subclasses that implement L</await>.

This will ensure that an outgoing argument is definitely not a C<Future>, and
may be useful in such cases as adapting synchronous code to fit asynchronous
libraries that return C<Future> instances.

=cut

sub unwrap {
    shift; # $class
    my @values = @_;

    if (@values == 1 and blessed $values[0] and $values[0]->isa(__PACKAGE__)) {
        return $values[0]->get;
    }
    else {
        return $values[0] if !wantarray;
        return @values;
    }
}

=head2 on_done

   $future->on_done( $code )

If the future is not yet ready, adds a callback to be invoked when the future
is ready, if it completes successfully. If the future completed successfully,
invokes it immediately. If it failed or was cancelled, it is not invoked at
all.

The callback will be passed the result passed to the C<done> method.

   $on_done->( @result )

If passed another C<Future> instance, the passed instance will have its
C<done> method invoked when the original future completes successfully.

Returns the C<$future>.

=cut

sub on_done {
    my $self = shift;
    my ($code) = @_;

    my $is_future = blessed($code) && $code->isa("Future");
    $is_future or _callable($code) or
        Carp::croak "Expected \$code to be callable or a Future in ->on_done";

    if ($self->{ready}) {
        return $self if $self->{failure} or $self->{cancelled};

        $is_future ? $code->done(@{$self->{result}})
            : $code->(@{$self->{result}});
    }
    else {
        push @{$self->{callbacks}}, [ CB_DONE | CB_RESULT, $self->wrap_cb(on_done => $code) ];
    }

    return $self;
}

=head2 failure

   $exception = $future->failure

   $exception, $category, @details = $future->failure

If the future is ready, returns the exception passed to the C<fail> method or
C<undef> if the future completed successfully via the C<done> method.

If it is not yet ready then L</await> is invoked to wait for a ready state.

If called in list context, will additionally yield the category name and list
of the details provided to the C<fail> method.

Because the exception value must be true, this can be used in a simple C<if>
statement:

   if( my $exception = $future->failure ) {
      ...
   }
   else {
      my @result = $future->result;
      ...
   }

=cut

sub failure {
    my $self = shift;
    $self->await unless $self->{ready};
    return unless $self->{failure};
    $self->{reported} = 1;
    return $self->{failure}->[0] if !wantarray;
    return @{$self->{failure}};
}

=head2 on_fail

   $future->on_fail( $code )

If the future is not yet ready, adds a callback to be invoked when the future
is ready, if it fails. If the future has already failed, invokes it
immediately. If it completed successfully or was cancelled, it is not invoked
at all.

The callback will be passed the exception and other details passed to the
C<fail> method.

   $on_fail->( $exception, $category, @details )

If passed another C<Future> instance, the passed instance will have its
C<fail> method invoked when the original future fails.

To invoke a C<done> method on a future when another one fails, use a CODE
reference:

   $future->on_fail( sub { $f->done( @_ ) } );

Returns the C<$future>.

=cut

sub on_fail {
    my $self = shift;
    my ($code) = @_;

    my $is_future = blessed($code) && $code->isa("Future");
    $is_future or _callable($code) or
        Carp::croak "Expected \$code to be callable or a Future in ->on_fail";

    if ($self->{ready}) {
        return $self if not $self->{failure};
        $self->{reported} = 1;

        $is_future ? $code->fail(@{$self->{failure}})
            : $code->(@{$self->{failure}});
    }
    else {
        push @{$self->{callbacks}}, [ CB_FAIL | CB_RESULT, $self->wrap_cb(on_fail => $code) ];
    }

    return $self;
}

=head2 cancel

   $future->cancel

Requests that the future be cancelled, immediately marking it as ready. This
will invoke all of the code blocks registered by C<on_cancel>, in the reverse
order. When called on a convergent future, all its component futures are also
cancelled. It is not an error to attempt to cancel a future that is already
complete or cancelled; it simply has no effect.

Returns the C<$future>.

=cut

sub cancel {
    my $self = shift;

    return $self if $self->{ready};

    $self->{cancelled}++;
    my $on_cancel = delete $self->{on_cancel};
    foreach my $code ($on_cancel ? reverse @$on_cancel : ()) {
        defined $code or next;
        my $is_future = blessed($code) && $code->isa("Future");
        $is_future ? $code->cancel
            : $code->($self);
    }
    $self->_mark_ready("cancel");

    return $self;
}

=head1 SEQUENCING METHODS

The following methods all return a new future to represent the combination of
its invocant followed by another action given by a code reference. The
combined activity waits for the first future to be ready, then may invoke the
code depending on the success or failure of the first, or may run it
regardless. The returned sequence future represents the entire combination of
activity.

In some cases the code should return a future; in some it should return an
immediate result. If a future is returned, the combined future will then wait
for the result of this second one. If the combinined future is cancelled, it
will cancel either the first future or the second, depending whether the first
had completed. If the code block throws an exception instead of returning a
value, the sequence future will fail with that exception as its message and no
further values.

As it is always a mistake to call these sequencing methods in void context and lose the
reference to the returned future (because exception/error handling would be
silently dropped), this method warns in void context.

=cut

sub _sequence {
    my $f1 = shift;
    my ($code, $flags) = @_;

    # For later, we might want to know where we were called from
    my $func = (caller 1)[3];
    $func =~ s/^.*:://;

    $flags & (CB_SEQ_IMDONE | CB_SEQ_IMFAIL) or _callable($code) or
        Carp::croak "Expected \$code to be callable in ->$func";

    if (!defined wantarray) {
        Carp::carp "Calling ->$func in void context";
    }

    if ($f1->is_ready) {
        # Take a shortcut
        return $f1 if $f1->is_done and not ($flags & CB_SEQ_ONDONE) or
            $f1->{failure} and not ($flags & CB_SEQ_ONFAIL);

        if ($flags & CB_SEQ_IMDONE) {
            return Future->done(@$code);
        }
        elsif ($flags & CB_SEQ_IMFAIL) {
            return Future->fail(@$code);
        }

        my @args = (
            ($flags & CB_SELF ? $f1 : ()),
            ($flags & CB_RESULT ? $f1->is_done ? @{$f1->{result}} :
                $f1->{failure} ? @{$f1->{failure}} :
                    () : ()),
        );

        my $fseq;
        unless (eval {
            $fseq = $code->(@args);
            1
        }) {
            return Future->fail($@);
        }

        unless (blessed $fseq and $fseq->isa("Future")) {
            return Future->fail("Expected " . CvNAME_FILE_LINE($code) . " to return a Future");
        }

        return $fseq;
    }

    my $fseq = $f1->new;
    $fseq->on_cancel($f1);

    # TODO: if anyone cares about the op name, we might have to synthesize it
    # from $flags
    $code = $f1->wrap_cb(sequence => $code) unless $flags & (CB_SEQ_IMDONE | CB_SEQ_IMFAIL);

    push @{$f1->{callbacks}}, [ CB_DONE | CB_FAIL | $flags, $code, $fseq ];
    weaken($f1->{callbacks}[-1][2]);

    return $fseq;
}

=head2 then

   $future = $f1->then( \&done_code )

I<Since version 0.13.>

Returns a new sequencing C<Future> that runs the code if the first succeeds.
Once C<$f1> succeeds the code reference will be invoked and is passed the list
of results. It should return a future, C<$f2>. Once C<$f2> completes the
sequence future will then be marked as complete with whatever result C<$f2>
gave. If C<$f1> fails then the sequence future will immediately fail with the
same failure and the code will not be invoked.

   $f2 = $done_code->( @result )

=head2 else

   $future = $f1->else( \&fail_code )

I<Since version 0.13.>

Returns a new sequencing C<Future> that runs the code if the first fails. Once
C<$f1> fails the code reference will be invoked and is passed the failure and
other details. It should return a future, C<$f2>. Once C<$f2> completes the
sequence future will then be marked as complete with whatever result C<$f2>
gave. If C<$f1> succeeds then the sequence future will immediately succeed
with the same result and the code will not be invoked.

   $f2 = $fail_code->( $exception, $category, @details )

=head2 then I<(2 arguments)>

   $future = $f1->then( \&done_code, \&fail_code )

The C<then> method can also be passed the C<$fail_code> block as well, giving
a combination of C<then> and C<else> behaviour.

This operation is designed to be compatible with the semantics of other future
systems, such as Javascript's Q or Promises/A libraries.

=cut

my $make_donecatchfail_sub = sub {
    my ($with_f, $done_code, $fail_code, @catch_list) = @_;

    my $func = (caller 1)[3];
    $func =~ s/^.*:://;

    !$done_code or _callable($done_code) or
        Carp::croak "Expected \$done_code to be callable in ->$func";
    !$fail_code or _callable($fail_code) or
        Carp::croak "Expected \$fail_code to be callable in ->$func";

    my %catch_handlers = @catch_list;
    _callable($catch_handlers{$_}) or
        Carp::croak "Expected catch handler for '$_' to be callable in ->$func"
        for keys %catch_handlers;

    sub {
        my $self = shift;
        my @maybe_self = $with_f ? ($self) : ();

        if (!$self->{failure}) {
            return $self unless $done_code;
            return $done_code->(@maybe_self, @{$self->{result}});
        }
        else {
            my $name = $self->{failure}[1];
            if (defined $name and $catch_handlers{$name}) {
                return $catch_handlers{$name}->(@maybe_self, @{$self->{failure}});
            }
            return $self unless $fail_code;
            return $fail_code->(@maybe_self, @{$self->{failure}});
        }
    };
};

sub then {
    my $self = shift;
    my $done_code = shift;
    my $fail_code = (@_ % 2) ? pop : undef;
    my @catch_list = @_;

    if ($done_code and !@catch_list and !$fail_code) {
        return $self->_sequence($done_code, CB_SEQ_ONDONE | CB_RESULT);
    }

    # Complex
    return $self->_sequence($make_donecatchfail_sub->(
        0, $done_code, $fail_code, @catch_list,
    ), CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

sub else {
    my $self = shift;
    my ($fail_code) = @_;

    return $self->_sequence($fail_code, CB_SEQ_ONFAIL | CB_RESULT);
}

=head2 catch

   $future = $f1->catch(
      name => \&code,
      name => \&code, ...
   )

I<Since version 0.33.>

Returns a new sequencing C<Future> that behaves like an C<else> call which
dispatches to a choice of several alternative handling functions depending on
the kind of failure that occurred. If C<$f1> fails with a category name (i.e.
the second argument to the C<fail> call) which exactly matches one of the
string names given, then the corresponding code is invoked, being passed the
same arguments as a plain C<else> call would take, and is expected to return a
C<Future> in the same way.

   $f2 = $code->( $exception, $category, @details )

If C<$f1> does not fail, fails without a category name at all, or fails with a
category name that does not match any given to the C<catch> method, then the
returned sequence future immediately completes with the same result, and no
block of code is invoked.

If passed an odd-sized list, the final argument gives a function to invoke on
failure if no other handler matches.

   $future = $f1->catch(
      name => \&code, ...
      \&fail_code,
   )

This feature is currently still a work-in-progress. It currently can only cope
with category names that are literal strings, which are all distinct. A later
version may define other kinds of match (e.g. regexp), may specify some sort
of ordering on the arguments, or any of several other semantic extensions. For
more detail on the ongoing design, see
L<https://rt.cpan.org/Ticket/Display.html?id=103545>.

=head2 then I<(multiple arguments)>

   $future = $f1->then( \&done_code, @catch_list, \&fail_code )

I<Since version 0.33.>

The C<then> method can be passed an even-sized list inbetween the
C<$done_code> and the C<$fail_code>, with the same meaning as the C<catch>
method.

=cut

sub catch {
    my $self = shift;
    my $fail_code = (@_ % 2) ? pop : undef;
    my @catch_list = @_;

    return $self->_sequence($make_donecatchfail_sub->(
        0, undef, $fail_code, @catch_list,
    ), CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

=head2 transform

   $future = $f1->transform( %args )

Returns a new sequencing C<Future> that wraps the one given as C<$f1>. With no
arguments this will be a trivial wrapper; C<$future> will complete or fail
when C<$f1> does, and C<$f1> will be cancelled when C<$future> is.

By passing the following named arguments, the returned C<$future> can be made
to behave differently to C<$f1>:

=over 8

=item done => CODE

Provides a function to use to modify the result of a successful completion.
When C<$f1> completes successfully, the result of its C<get> method is passed
into this function, and whatever it returns is passed to the C<done> method of
C<$future>

=item fail => CODE

Provides a function to use to modify the result of a failure. When C<$f1>
fails, the result of its C<failure> method is passed into this function, and
whatever it returns is passed to the C<fail> method of C<$future>.

=back

=cut

sub transform {
    my $self = shift;
    my %args = @_;

    my $xfrm_done = $args{done};
    my $xfrm_fail = $args{fail};

    return $self->_sequence(sub {
        my $self = shift;
        if (!$self->{failure}) {
            return $self unless $xfrm_done;
            my @result = $xfrm_done->(@{$self->{result}});
            return $self->new->done(@result);
        }
        else {
            return $self unless $xfrm_fail;
            my @failure = $xfrm_fail->(@{$self->{failure}});
            return $self->new->fail(@failure);
        }
    }, CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

=head2 then_with_f

   $future = $f1->then_with_f( ... )

I<Since version 0.21.>

Returns a new sequencing C<Future> that behaves like C<then>, but also passes
the original future, C<$f1>, to any functions it invokes.

   $f2 = $done_code->( $f1, @result )
   $f2 = $catch_code->( $f1, $category, @details )
   $f2 = $fail_code->( $f1, $category, @details )

This is useful for conditional execution cases where the code block may just
return the same result of the original future. In this case it is more
efficient to return the original future itself.

=cut

sub then_with_f {
    my $self = shift;
    my $done_code = shift;
    my $fail_code = (@_ % 2) ? pop : undef;
    my @catch_list = @_;

    if ($done_code and !@catch_list and !$fail_code) {
        return $self->_sequence($done_code, CB_SEQ_ONDONE | CB_SELF | CB_RESULT);
    }

    return $self->_sequence($make_donecatchfail_sub->(
        1, $done_code, $fail_code, @catch_list,
    ), CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

=head2 then_done

=head2 then_fail

   $future = $f->then_done( @result )

   $future = $f->then_fail( $exception, $category, @details )

I<Since version 0.22.>

Convenient shortcuts to returning an immediate future from a C<then> block,
when the result is already known.

=cut

sub then_done {
    my $self = shift;
    my (@result) = @_;
    return $self->_sequence(\@result, CB_SEQ_ONDONE | CB_SEQ_IMDONE);
}

sub then_fail {
    my $self = shift;
    my (@failure) = @_;
    return $self->_sequence(\@failure, CB_SEQ_ONDONE | CB_SEQ_IMFAIL);
}

=head2 else_with_f

   $future = $f1->else_with_f( \&code )

I<Since version 0.21.>

Returns a new sequencing C<Future> that runs the code if the first fails.
Identical to C<else>, except that the code reference will be passed both the
original future, C<$f1>, and its exception and other details.

   $f2 = $code->( $f1, $exception, $category, @details )

This is useful for conditional execution cases where the code block may just
return the same result of the original future. In this case it is more
efficient to return the original future itself.

=cut

sub else_with_f {
    my $self = shift;
    my ($fail_code) = @_;

    return $self->_sequence($fail_code, CB_SEQ_ONFAIL | CB_SELF | CB_RESULT);
}

=head2 else_done

=head2 else_fail

   $future = $f->else_done( @result )

   $future = $f->else_fail( $exception, $category, @details )

I<Since version 0.22.>

Convenient shortcuts to returning an immediate future from a C<else> block,
when the result is already known.

=cut

sub else_done {
    my $self = shift;
    my (@result) = @_;
    return $self->_sequence(\@result, CB_SEQ_ONFAIL | CB_SEQ_IMDONE);
}

sub else_fail {
    my $self = shift;
    my (@failure) = @_;
    return $self->_sequence(\@failure, CB_SEQ_ONFAIL | CB_SEQ_IMFAIL);
}

=head2 catch_with_f

   $future = $f1->catch_with_f( ... )

I<Since version 0.33.>

Returns a new sequencing C<Future> that behaves like C<catch>, but also passes
the original future, C<$f1>, to any functions it invokes.

=cut

sub catch_with_f {
    my $self = shift;
    my $fail_code = (@_ % 2) ? pop : undef;
    my @catch_list = @_;

    return $self->_sequence($make_donecatchfail_sub->(
        1, undef, $fail_code, @catch_list,
    ), CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

=head2 followed_by

   $future = $f1->followed_by( \&code )

Returns a new sequencing C<Future> that runs the code regardless of success or
failure. Once C<$f1> is ready the code reference will be invoked and is passed
one argument, C<$f1>. It should return a future, C<$f2>. Once C<$f2> completes
the sequence future will then be marked as complete with whatever result
C<$f2> gave.

   $f2 = $code->( $f1 )

=cut

sub followed_by {
    my $self = shift;
    my ($code) = @_;

    return $self->_sequence($code, CB_SEQ_ONDONE | CB_SEQ_ONFAIL | CB_SELF);
}

=head2 without_cancel

   $future = $f1->without_cancel

I<Since version 0.30.>

Returns a new sequencing C<Future> that will complete with the success or
failure of the original future, but if cancelled, will not cancel the
original. This may be useful if the original future represents an operation
that is being shared among multiple sequences; cancelling one should not
prevent the others from running too.

=cut

sub without_cancel {
    my $self = shift;
    my $new = $self->new;

    $self->on_ready(sub {
        my $self = shift;
        if ($self->{failure}) {
            $new->fail(@{$self->{failure}});
        }
        else {
            $new->done(@{$self->{result}});
        }
    });

    $new->{orig} = $self; # just to strongref it - RT122920
    $new->on_ready(sub {undef $_[0]->{orig}});

    return $new;
}

=head2 retain

   $f = $f->retain

I<Since version 0.36.>

Creates a reference cycle which causes the future to remain in memory until
it completes. Returns the invocant future.

In normal situations, a C<Future> instance does not strongly hold a reference
to other futures that it is feeding a result into, instead relying on that to
be handled by application logic. This is normally fine because some part of
the application will retain the top-level Future, which then strongly refers
to each of its components down in a tree. However, certain design patterns,
such as mixed Future-based and legacy callback-based API styles might end up
creating Futures simply to attach callback functions to them. In that
situation, without further attention, the Future may get lost due to having no
strong references to it. Calling C<< ->retain >> on it creates such a
reference which ensures it persists until it completes. For example:

   Future->needs_all( $fA, $fB )
      ->on_done( $on_done )
      ->on_fail( $on_fail )
      ->retain;

=cut

sub retain {
    my $self = shift;
    return $self->on_ready(sub {undef $self});
}

=head1 CONVERGENT FUTURES

The following constructors all take a list of component futures, and return a
new future whose readiness somehow depends on the readiness of those
components. The first derived class component future will be used as the
prototype for constructing the return value, so it respects subclassing
correctly, or failing that a plain C<Future>.

=cut

sub _new_convergent {
    shift; # ignore this class
    my ($subs) = @_;

    foreach my $sub (@$subs) {
        blessed $sub and $sub->isa("Future") or Carp::croak "Expected a Future, got $sub";
    }

    # Find the best prototype. Ideally anything derived if we can find one.
    my $self;
    ref($_) eq "Future" or $self = $_->new, last for @$subs;

    # No derived ones; just have to be a basic class then
    $self ||= Future->new;

    $self->{subs} = $subs;

    # This might be called by a DESTROY during global destruction so it should
    # be as defensive as possible (see RT88967)
    $self->on_cancel(sub {
        foreach my $sub (@$subs) {
            $sub->cancel if $sub and !$sub->{ready};
        }
    });

    return $self;
}

=head2 wait_all

   $future = Future->wait_all( @subfutures )

Returns a new C<Future> instance that will indicate it is ready once all of
the sub future objects given to it indicate that they are ready, either by
success, failure or cancellation. Its result will be a list of its component
futures.

When given an empty list this constructor returns a new immediately-done
future.

This constructor would primarily be used by users of asynchronous interfaces.

=cut

sub wait_all {
    my $class = shift;
    my @subs = @_;

    unless (@subs) {
        my $self = $class->done;
        $self->{subs} = [];
        return $self;
    }

    my $self = Future->_new_convergent(\@subs);

    my $pending = 0;
    $_->{ready} or $pending++ for @subs;

    # Look for immediate ready
    if (!$pending) {
        $self->{result} = [ @subs ];
        $self->_mark_ready("wait_all");
        return $self;
    }

    weaken(my $weakself = $self);
    my $sub_on_ready = sub {
        return unless my $self = $weakself;

        $pending--;
        $pending and return;

        $self->{result} = [ @subs ];
        $self->_mark_ready("wait_all");
    };

    foreach my $sub (@subs) {
        $sub->{ready} or $sub->on_ready($sub_on_ready);
    }

    return $self;
}

=head2 wait_any

   $future = Future->wait_any( @subfutures )

Returns a new C<Future> instance that will indicate it is ready once any of
the sub future objects given to it indicate that they are ready, either by
success or failure. Any remaining component futures that are not yet ready
will be cancelled. Its result will be the result of the first component future
that was ready; either success or failure. Any component futures that are
cancelled are ignored, apart from the final component left; at which point the
result will be a failure.

When given an empty list this constructor returns an immediately-failed
future.

This constructor would primarily be used by users of asynchronous interfaces.

=cut

sub wait_any {
    my $class = shift;
    my @subs = @_;

    unless (@subs) {
        my $self = $class->fail("Cannot ->wait_any with no subfutures");
        $self->{subs} = [];
        return $self;
    }

    my $self = Future->_new_convergent(\@subs);

    # Look for immediate ready
    my $immediate_ready;
    foreach my $sub (@subs) {
        $sub->{ready} and $immediate_ready = $sub, last;
    }

    if ($immediate_ready) {
        foreach my $sub (@subs) {
            $sub->{ready} or $sub->cancel;
        }

        if ($immediate_ready->{failure}) {
            $self->{failure} = [ @{$immediate_ready->{failure}} ];
        }
        else {
            $self->{result} = [ @{$immediate_ready->{result}} ];
        }
        $self->_mark_ready("wait_any");
        return $self;
    }

    my $pending = 0;

    weaken(my $weakself = $self);
    my $sub_on_ready = sub {
        return unless my $self = $weakself;
        return if $self->{result} or $self->{failure}; # don't recurse on child ->cancel

        return if --$pending and $_[0]->{cancelled};

        if ($_[0]->{cancelled}) {
            $self->{failure} = [ "All component futures were cancelled" ];
        }
        elsif ($_[0]->{failure}) {
            $self->{failure} = [ @{$_[0]->{failure}} ];
        }
        else {
            $self->{result} = [ @{$_[0]->{result}} ];
        }

        foreach my $sub (@subs) {
            $sub->{ready} or $sub->cancel;
        }

        $self->_mark_ready("wait_any");
    };

    foreach my $sub (@subs) {
        # No need to test $sub->{ready} since we know none of them are
        $sub->on_ready($sub_on_ready);
        $pending++;
    }

    return $self;
}

=head2 needs_all

   $future = Future->needs_all( @subfutures )

Returns a new C<Future> instance that will indicate it is ready once all of the
sub future objects given to it indicate that they have completed successfully,
or when any of them indicates that they have failed. If any sub future fails,
then this will fail immediately, and the remaining subs not yet ready will be
cancelled. Any component futures that are cancelled will cause an immediate
failure of the result.

If successful, its result will be a concatenated list of the results of all
its component futures, in corresponding order. If it fails, its failure will
be that of the first component future that failed. To access each component
future's results individually, use C<done_futures>.

When given an empty list this constructor returns a new immediately-done
future.

This constructor would primarily be used by users of asynchronous interfaces.

=cut

sub needs_all {
    my $class = shift;
    my @subs = @_;

    unless (@subs) {
        my $self = $class->done;
        $self->{subs} = [];
        return $self;
    }

    my $self = Future->_new_convergent(\@subs);

    # Look for immediate fail
    my $immediate_fail;
    foreach my $sub (@subs) {
        $sub->{ready} and $sub->{failure} and $immediate_fail = $sub, last;
    }

    if ($immediate_fail) {
        foreach my $sub (@subs) {
            $sub->{ready} or $sub->cancel;
        }

        $self->{failure} = [ @{$immediate_fail->{failure}} ];
        $self->_mark_ready("needs_all");
        return $self;
    }

    my $pending = 0;
    $_->{ready} or $pending++ for @subs;

    # Look for immediate done
    if (!$pending) {
        $self->{result} = [ map {@{$_->{result}}} @subs ];
        $self->_mark_ready("needs_all");
        return $self;
    }

    weaken(my $weakself = $self);
    my $sub_on_ready = sub {
        return unless my $self = $weakself;
        return if $self->{result} or $self->{failure}; # don't recurse on child ->cancel

        if ($_[0]->{cancelled}) {
            $self->{failure} = [ "A component future was cancelled" ];
            foreach my $sub (@subs) {
                $sub->cancel if !$sub->{ready};
            }
            $self->_mark_ready("needs_all");
        }
        elsif ($_[0]->{failure}) {
            $self->{failure} = [ @{$_[0]->{failure}} ];
            foreach my $sub (@subs) {
                $sub->cancel if !$sub->{ready};
            }
            $self->_mark_ready("needs_all");
        }
        else {
            $pending--;
            $pending and return;

            $self->{result} = [ map {@{$_->{result}}} @subs ];
            $self->_mark_ready("needs_all");
        }
    };

    foreach my $sub (@subs) {
        $sub->{ready} or $sub->on_ready($sub_on_ready);
    }

    return $self;
}

=head2 needs_any

   $future = Future->needs_any( @subfutures )

Returns a new C<Future> instance that will indicate it is ready once any of
the sub future objects given to it indicate that they have completed
successfully, or when all of them indicate that they have failed. If any sub
future succeeds, then this will succeed immediately, and the remaining subs
not yet ready will be cancelled. Any component futures that are cancelled are
ignored, apart from the final component left; at which point the result will
be a failure.

If successful, its result will be that of the first component future that
succeeded. If it fails, its failure will be that of the last component future
to fail. To access the other failures, use C<failed_futures>.

Normally when this future completes successfully, only one of its component
futures will be done. If it is constructed with multiple that are already done
however, then all of these will be returned from C<done_futures>. Users should
be careful to still check all the results from C<done_futures> in that case.

When given an empty list this constructor returns an immediately-failed
future.

This constructor would primarily be used by users of asynchronous interfaces.

=cut

sub needs_any {
    my $class = shift;
    my @subs = @_;

    unless (@subs) {
        my $self = $class->fail("Cannot ->needs_any with no subfutures");
        $self->{subs} = [];
        return $self;
    }

    my $self = Future->_new_convergent(\@subs);

    # Look for immediate done
    my $immediate_done;
    my $pending = 0;
    foreach my $sub (@subs) {
        $sub->{ready} and !$sub->{failure} and $immediate_done = $sub, last;
        $sub->{ready} or $pending++;
    }

    if ($immediate_done) {
        foreach my $sub (@subs) {
            $sub->{ready} ? $sub->{reported} = 1 : $sub->cancel;
        }

        $self->{result} = [ @{$immediate_done->{result}} ];
        $self->_mark_ready("needs_any");
        return $self;
    }

    # Look for immediate fail
    my $immediate_fail = 1;
    foreach my $sub (@subs) {
        $sub->{ready} or $immediate_fail = 0, last;
    }

    if ($immediate_fail) {
        $_->{reported} = 1 for @subs;
        # For consistency we'll pick the last one for the failure
        $self->{failure} = [ $subs[-1]->{failure} ];
        $self->_mark_ready("needs_any");
        return $self;
    }

    weaken(my $weakself = $self);
    my $sub_on_ready = sub {
        return unless my $self = $weakself;
        return if $self->{result} or $self->{failure}; # don't recurse on child ->cancel

        return if --$pending and $_[0]->{cancelled};

        if ($_[0]->{cancelled}) {
            $self->{failure} = [ "All component futures were cancelled" ];
            $self->_mark_ready("needs_any");
        }
        elsif ($_[0]->{failure}) {
            $pending and return;

            $self->{failure} = [ @{$_[0]->{failure}} ];
            $self->_mark_ready("needs_any");
        }
        else {
            $self->{result} = [ @{$_[0]->{result}} ];
            foreach my $sub (@subs) {
                $sub->cancel if !$sub->{ready};
            }
            $self->_mark_ready("needs_any");
        }
    };

    foreach my $sub (@subs) {
        $sub->{ready} or $sub->on_ready($sub_on_ready);
    }

    return $self;
}

=head1 METHODS ON CONVERGENT FUTURES

The following methods apply to convergent (i.e. non-leaf) futures, to access
the component futures stored by it.

=cut

=head2 pending_futures

   @f = $future->pending_futures

=head2 ready_futures

   @f = $future->ready_futures

=head2 done_futures

   @f = $future->done_futures

=head2 failed_futures

   @f = $future->failed_futures

=head2 cancelled_futures

   @f = $future->cancelled_futures

Return a list of all the pending, ready, done, failed, or cancelled
component futures. In scalar context, each will yield the number of such
component futures.

=cut

sub pending_futures {
    my $self = shift;
    $self->{subs} or Carp::croak "Cannot call ->pending_futures on a non-convergent Future";
    return grep {not $_->{ready}} @{$self->{subs}};
}

sub ready_futures {
    my $self = shift;
    $self->{subs} or Carp::croak "Cannot call ->ready_futures on a non-convergent Future";
    return grep {$_->{ready}} @{$self->{subs}};
}

sub done_futures {
    my $self = shift;
    $self->{subs} or Carp::croak "Cannot call ->done_futures on a non-convergent Future";
    return grep {$_->{ready} and not $_->{failure} and not $_->{cancelled}} @{$self->{subs}};
}

sub failed_futures {
    my $self = shift;
    $self->{subs} or Carp::croak "Cannot call ->failed_futures on a non-convergent Future";
    return grep {$_->{ready} and $_->{failure}} @{$self->{subs}};
}

sub cancelled_futures {
    my $self = shift;
    $self->{subs} or Carp::croak "Cannot call ->cancelled_futures on a non-convergent Future";
    return grep {$_->{ready} and $_->{cancelled}} @{$self->{subs}};
}

=head1 TRACING METHODS

=head2 set_label

=head2 label

   $future = $future->set_label( $label )

   $label = $future->label

I<Since version 0.28.>

Chaining mutator and accessor for the label of the C<Future>. This should be a
plain string value, whose value will be stored by the future instance for use
in debugging messages or other tooling, or similar purposes.

=cut

sub set_label {
    my $self = shift;
    ($self->{label}) = @_;
    return $self;
}

sub label {
    my $self = shift;
    return $self->{label};
}

sub __selfstr {
    my $self = shift;
    return "$self" unless defined $self->{label};
    return "$self (\"$self->{label}\")";
}

=head2 btime

=head2 rtime

   [ $sec, $usec ] = $future->btime

   [ $sec, $usec ] = $future->rtime

I<Since version 0.28.>

Accessors that return the tracing timestamps from the instance. These give the
time the instance was constructed ("birth" time, C<btime>) and the time the
result was determined (the "ready" time, C<rtime>). Each result is returned as
a two-element ARRAY ref, containing the epoch time in seconds and
microseconds, as given by C<Time::HiRes::gettimeofday>.

In order for these times to be captured, they have to be enabled by setting
C<$Future::TIMES> to a true value. This is initialised true at the time the
module is loaded if either C<PERL_FUTURE_DEBUG> or C<PERL_FUTURE_TIMES> are
set in the environment.

=cut

sub btime {
    my $self = shift;
    return $self->{btime};
}

sub rtime {
    my $self = shift;
    return $self->{rtime};
}

=head2 elapsed

   $sec = $future->elapsed

I<Since version 0.28.>

If both tracing timestamps are defined, returns the number of seconds of
elapsed time between them as a floating-point number. If not, returns
C<undef>.

=cut

sub elapsed {
    my $self = shift;
    return undef unless defined $self->{btime} and defined $self->{rtime};
    return $self->{elapsed} ||= tv_interval($self->{btime}, $self->{rtime});
}

=head2 wrap_cb

   $cb = $future->wrap_cb( $operation_name, $cb )

I<Since version 0.31.>

I<Note: This method is experimental and may be changed or removed in a later
version.>

This method is invoked internally by various methods that are about to save a
callback CODE reference supplied by the user, to be invoked later. The default
implementation simply returns the callback argument as-is; the method is
provided to allow users to provide extra behaviour. This can be done by
applying a method modifier of the C<around> kind, so in effect add a chain of
wrappers. Each wrapper can then perform its own wrapping logic of the
callback. C<$operation_name> is a string giving the reason for which the
callback is being saved; currently one of C<on_ready>, C<on_done>, C<on_fail>
or C<sequence>; the latter being used for all the sequence-returning methods.

This method is intentionally invoked only for CODE references that are being
saved on a pending C<Future> instance to be invoked at some later point. It
does not run for callbacks to be invoked on an already-complete instance. This
is for performance reasons, where the intended behaviour is that the wrapper
can provide some amount of context save and restore, to return the operating
environment for the callback back to what it was at the time it was saved.

For example, the following wrapper saves the value of a package variable at
the time the callback was saved, and restores that value at invocation time
later on. This could be useful for preserving context during logging in a
Future-based program.

   our $LOGGING_CTX;

   no warnings 'redefine';

   my $orig = Future->can( "wrap_cb" );
   *Future::wrap_cb = sub {
      my $cb = $orig->( @_ );

      my $saved_logging_ctx = $LOGGING_CTX;

      return sub {
         local $LOGGING_CTX = $saved_logging_ctx;
         $cb->( @_ );
      };
   };

At this point, any code deferred into a C<Future> by any of its callbacks will
observe the C<$LOGGING_CTX> variable as having the value it held at the time
the callback was saved, even if it is invoked later on when that value is
different.

Remember when writing such a wrapper, that it still needs to invoke the
previous version of the method, so that it plays nicely in combination with
others (see the C<< $orig->( @_ ) >> part).

=cut

sub wrap_cb {
    my $self = shift;
    my ($op, $cb) = @_;
    return $cb;
}

=head1 EXAMPLES

The following examples all demonstrate possible uses of a C<Future>
object to provide a fictional asynchronous API.

For more examples, comparing the use of C<Future> with regular call/return
style Perl code, see also L<Future::Phrasebook>.

=head2 Providing Results

By returning a new C<Future> object each time the asynchronous function is
called, it provides a placeholder for its eventual result, and a way to
indicate when it is complete.

   sub foperation
   {
      my %args = @_;

      my $future = Future->new;

      do_something_async(
         foo => $args{foo},
         on_done => sub { $future->done( @_ ); },
      );

      return $future;
   }

In most cases, the C<done> method will simply be invoked with the entire
result list as its arguments. In that case, it is convenient to use the
L<curry> module to form a C<CODE> reference that would invoke the C<done>
method.

    my $future = Future->new;

    do_something_async(
       foo => $args{foo},
       on_done => $future->curry::done,
    );

The caller may then use this future to wait for a result using the C<on_ready>
method, and obtain the result using C<get>.

   my $f = foperation( foo => "something" );

   $f->on_ready( sub {
      my $f = shift;
      say "The operation returned: ", $f->result;
   } );

=head2 Indicating Success or Failure

Because the stored exception value of a failed future may not be false, the
C<failure> method can be used in a conditional statement to detect success or
failure.

   my $f = foperation( foo => "something" );

   $f->on_ready( sub {
      my $f = shift;
      if( not my $e = $f->failure ) {
         say "The operation succeeded with: ", $f->result;
      }
      else {
         say "The operation failed with: ", $e;
      }
   } );

By using C<not> in the condition, the order of the C<if> blocks can be
arranged to put the successful case first, similar to a C<try>/C<catch> block.

Because the C<get> method re-raises the passed exception if the future failed,
it can be used to control a C<try>/C<catch> block directly. (This is sometimes
called I<Exception Hoisting>).

   use Syntax::Keyword::Try;

   $f->on_ready( sub {
      my $f = shift;
      try {
         say "The operation succeeded with: ", $f->result;
      }
      catch {
         say "The operation failed with: ", $_;
      }
   } );

Even neater still may be the separate use of the C<on_done> and C<on_fail>
methods.

   $f->on_done( sub {
      my @result = @_;
      say "The operation succeeded with: ", @result;
   } );
   $f->on_fail( sub {
      my ( $failure ) = @_;
      say "The operation failed with: $failure";
   } );

=head2 Immediate Futures

Because the C<done> method returns the future object itself, it can be used to
generate a C<Future> that is immediately ready with a result. This can also be
used as a class method.

   my $f = Future->done( $value );

Similarly, the C<fail> and C<die> methods can be used to generate a C<Future>
that is immediately failed.

   my $f = Future->die( "This is never going to work" );

This could be considered similarly to a C<die> call.

An C<eval{}> block can be used to turn a C<Future>-returning function that
might throw an exception, into a C<Future> that would indicate this failure.

   my $f = eval { function() } || Future->fail( $@ );

This is neater handled by the C<call> class method, which wraps the call in
an C<eval{}> block and tests the result:

   my $f = Future->call( \&function );

=head2 Sequencing

The C<then> method can be used to create simple chains of dependent tasks,
each one executing and returning a C<Future> when the previous operation
succeeds.

   my $f = do_first()
              ->then( sub {
                 return do_second();
              })
              ->then( sub {
                 return do_third();
              });

The result of the C<$f> future itself will be the result of the future
returned by the final function, if none of them failed. If any of them fails
it will fail with the same failure. This can be considered similar to normal
exception handling in synchronous code; the first time a function call throws
an exception, the subsequent calls are not made.

=head2 Merging Control Flow

A C<wait_all> future may be used to resynchronise control flow, while waiting
for multiple concurrent operations to finish.

   my $f1 = foperation( foo => "something" );
   my $f2 = foperation( bar => "something else" );

   my $f = Future->wait_all( $f1, $f2 );

   $f->on_ready( sub {
      say "Operations are ready:";
      say "  foo: ", $f1->result;
      say "  bar: ", $f2->result;
   } );

This provides an ability somewhat similar to C<CPS::kpar()> or
L<Async::MergePoint>.

=cut

=head1 KNOWN ISSUES

=head2 Cancellation of Non-Final Sequence Futures

The behaviour of future cancellation still has some unanswered questions
regarding how to handle the situation where a future is cancelled that has a
sequence future constructed from it.

In particular, it is unclear in each of the following examples what the
behaviour of C<$f2> should be, were C<$f1> to be cancelled:

   $f2 = $f1->then( sub { ... } ); # plus related ->then_with_f, ...

   $f2 = $f1->else( sub { ... } ); # plus related ->else_with_f, ...

   $f2 = $f1->followed_by( sub { ... } );

In the C<then>-style case it is likely that this situation should be treated
as if C<$f1> had failed, perhaps with some special message. The C<else>-style
case is more complex, because it may be that the entire operation should still
fail, or it may be that the cancellation of C<$f1> should again be treated
simply as a special kind of failure, and the C<else> logic run as normal.

To be specific; in each case it is unclear what happens if the first future is
cancelled, while the second one is still waiting on it. The semantics for
"normal" top-down cancellation of C<$f2> and how it affects C<$f1> are already
clear and defined.

=head2 Cancellation of Divergent Flow

A further complication of cancellation comes from the case where a given
future is reused multiple times for multiple sequences or convergent trees.

In particular, it is in clear in each of the following examples what the
behaviour of C<$f2> should be, were C<$f1> to be cancelled:

   my $f_initial = Future->new; ...
   my $f1 = $f_initial->then( ... );
   my $f2 = $f_initial->then( ... );

   my $f1 = Future->needs_all( $f_initial );
   my $f2 = Future->needs_all( $f_initial );

The point of cancellation propagation is to trace backwards through stages of
some larger sequence of operations that now no longer need to happen, because
the final result is no longer required. But in each of these cases, just
because C<$f1> has been cancelled, the initial future C<$f_initial> is still
required because there is another future (C<$f2>) that will still require its
result.

Initially it would appear that some kind of reference-counting mechanism could
solve this question, though that itself is further complicated by the
C<on_ready> handler and its variants.

It may simply be that a comprehensive useful set of cancellation semantics
can't be universally provided to cover all cases; and that some use-cases at
least would require the application logic to give extra information to its
C<Future> objects on how they should wire up the cancel propagation logic.

Both of these cancellation issues are still under active design consideration;
see the discussion on RT96685 for more information
(L<https://rt.cpan.org/Ticket/Display.html?id=96685>).

=cut

=head1 SEE ALSO

=over 4

=item *

L<Future::AsyncAwait> - deferred subroutine syntax for futures

Provides a neat syntax extension for writing future-based code.

=item *

L<Future::IO> - Future-returning IO methods

Provides methods similar to core IO functions, which yield results by Futures.

=item *

L<Promises> - an implementation of the "Promise/A+" pattern for asynchronous
programming

A different alternative implementation of a similar idea.

=item *

L<curry> - Create automatic curried method call closures for any class or
object

=item *

"The Past, The Present and The Future" - slides from a talk given at the
London Perl Workshop, 2012.

L<https://docs.google.com/presentation/d/1UkV5oLcTOOXBXPh8foyxko4PR28_zU_aVx6gBms7uoo/edit>

=item *

"Futures advent calendar 2013"

L<http://leonerds-code.blogspot.co.uk/2013/12/futures-advent-day-1.html>

=item *

"Asynchronous Programming with Futures" - YAPC::EU 2014

L<https://www.youtube.com/watch?v=u9dZgFM6FtE>

=back

=cut

=head1 TODO

=over 4

=item *

Consider the ability to pass the constructor a C<block> CODEref, instead of
needing to use a subclass. This might simplify async/etc.. implementations,
and allows the reuse of the idea of subclassing to extend the abilities of
C<Future> itself - for example to allow a kind of Future that can report
incremental progress.

=back

=cut

=head1 AUTHOR

Paul Evans <leonerd@leonerd.org.uk>

=cut

0x55AA;