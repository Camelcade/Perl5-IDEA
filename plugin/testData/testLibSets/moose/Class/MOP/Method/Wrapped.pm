package Class::MOP::Method::Wrapped;
our $VERSION = '2.2101';

use strict;
use warnings;

use Scalar::Util 'blessed';
use Sub::Util 1.40 'set_subname';

use parent 'Class::MOP::Method';

# NOTE:
# this ugly beast is the result of trying
# to micro optimize this as much as possible
# while not completely loosing maintainability.
# At this point it's "fast enough", after all
# you can't get something for nothing :)
my $_build_wrapped_method = sub {
    my $modifier_table = shift;
    my ($before, $after, $around) = (
        $modifier_table->{before},
        $modifier_table->{after},
        $modifier_table->{around},
    );
    if (@$before && @$after) {
        $modifier_table->{cache} = sub {
            for my $c (@$before) { $c->(@_) };
            my @rval;
            ((defined wantarray) ?
                ((wantarray) ?
                    (@rval = $around->{cache}->(@_))
                    :
                    ($rval[0] = $around->{cache}->(@_)))
                :
                $around->{cache}->(@_));
            for my $c (@$after) { $c->(@_) };
            return unless defined wantarray;
            return wantarray ? @rval : $rval[0];
        }
    }
    elsif (@$before) {
        $modifier_table->{cache} = sub {
            for my $c (@$before) { $c->(@_) };
            return $around->{cache}->(@_);
        }
    }
    elsif (@$after) {
        $modifier_table->{cache} = sub {
            my @rval;
            ((defined wantarray) ?
                ((wantarray) ?
                    (@rval = $around->{cache}->(@_))
                    :
                    ($rval[0] = $around->{cache}->(@_)))
                :
                $around->{cache}->(@_));
            for my $c (@$after) { $c->(@_) };
            return unless defined wantarray;
            return wantarray ? @rval : $rval[0];
        }
    }
    else {
        $modifier_table->{cache} = $around->{cache};
    }
};

sub wrap {
    my ( $class, $code, %params ) = @_;

    (blessed($code) && $code->isa('Class::MOP::Method'))
        || $class->_throw_exception( CanOnlyWrapBlessedCode => params => \%params,
                                                      class  => $class,
                                                      code   => $code
                          );

    my $modifier_table = {
        cache  => undef,
        orig   => $code->body,
        before => [],
        after  => [],
        around => {
            cache   => $code->body,
            methods => [],
        },
    };
    $_build_wrapped_method->($modifier_table);

    # get these from the original unless explicitly overridden
    my $pkg_name    = $params{package_name} || $code->package_name;
    my $method_name = $params{name}         || $code->name;

    return $class->SUPER::wrap(
        sub {
            my $wrapped
                = set_subname( "${pkg_name}::_wrapped_${method_name}" =>
                    $modifier_table->{cache} );
            return $wrapped->(@_) ;
        },
        package_name    => $pkg_name,
        name            => $method_name,
        original_method => $code,
        modifier_table  => $modifier_table,
    );
}

sub _new {
    my $class = shift;
    return Class::MOP::Class->initialize($class)->new_object(@_)
        if $class ne __PACKAGE__;

    my $params = @_ == 1 ? $_[0] : {@_};

    return bless {
        # inherited from Class::MOP::Method
        'body'                 => $params->{body},
        'associated_metaclass' => $params->{associated_metaclass},
        'package_name'         => $params->{package_name},
        'name'                 => $params->{name},
        'original_method'      => $params->{original_method},

        # defined in this class
        'modifier_table'       => $params->{modifier_table}
    } => $class;
}

sub get_original_method {
    my $code = shift;
    $code->original_method;
}

sub add_before_modifier {
    my $code     = shift;
    my $modifier = shift;
    unshift @{$code->{'modifier_table'}->{before}} => $modifier;
    $_build_wrapped_method->($code->{'modifier_table'});
}

sub before_modifiers {
    my $code = shift;
    return @{$code->{'modifier_table'}->{before}};
}

sub add_after_modifier {
    my $code     = shift;
    my $modifier = shift;
    push @{$code->{'modifier_table'}->{after}} => $modifier;
    $_build_wrapped_method->($code->{'modifier_table'});
}

sub after_modifiers {
    my $code = shift;
    return @{$code->{'modifier_table'}->{after}};
}

{
    # NOTE:
    # this is another possible candidate for
    # optimization as well. There is an overhead
    # associated with the currying that, if
    # eliminated might make around modifiers
    # more manageable.
    my $compile_around_method = sub {{
        my $f1 = pop;
        return $f1 unless @_;
        my $f2 = pop;
        push @_, sub { $f2->( $f1, @_ ) };
        redo;
    }};

    sub add_around_modifier {
        my $code     = shift;
        my $modifier = shift;
        unshift @{$code->{'modifier_table'}->{around}->{methods}} => $modifier;
        $code->{'modifier_table'}->{around}->{cache} = $compile_around_method->(
            @{$code->{'modifier_table'}->{around}->{methods}},
            $code->{'modifier_table'}->{orig}
        );
        $_build_wrapped_method->($code->{'modifier_table'});
    }
}

sub around_modifiers {
    my $code = shift;
    return @{$code->{'modifier_table'}->{around}->{methods}};
}

sub _make_compatible_with {
    my $self = shift;
    my ($other) = @_;

    # XXX: this is pretty gross. the issue here is that CMOP::Method::Wrapped
    # objects are subclasses of CMOP::Method, but when we get to moose, they'll
    # need to be compatible with Moose::Meta::Method, which isn't possible. the
    # right solution here is to make ::Wrapped into a role that gets applied to
    # whatever the method_metaclass happens to be and get rid of
    # wrapped_method_metaclass entirely, but that's not going to happen until
    # we ditch cmop and get roles into the bootstrapping, so. i'm not
    # maintaining the previous behavior of turning them into instances of the
    # new method_metaclass because that's equally broken, and at least this way
    # any issues will at least be detectable and potentially fixable. -doy
    return $self unless $other->_is_compatible_with($self->_real_ref_name);

    return $self->SUPER::_make_compatible_with(@_);
}

1;

# ABSTRACT: Method Meta Object for methods with before/after/around modifiers

__END__

=pod

=head1 DESCRIPTION

This is a L<Class::MOP::Method> subclass which implements before,
after, and around method modifiers.

=head1 METHODS

=head2 Class::MOP::Method::Wrapped->wrap($metamethod, %options)

This is the constructor. It accepts a L<Class::MOP::Method> object and
a hash of options.

The options are:

=over 4

=item * name

The method name (without a package name). This will be taken from the
provided L<Class::MOP::Method> object if it is not provided.

=item * package_name

The package name for the method. This will be taken from the provided
L<Class::MOP::Method> object if it is not provided.

=item * associated_metaclass

An optional L<Class::MOP::Class> object. This is the metaclass for the
method's class.

=back

=head2 $metamethod->get_original_method

This returns the L<Class::MOP::Method> object that was passed to the
constructor.

=head2 $metamethod->add_before_modifier($code)

=head2 $metamethod->add_after_modifier($code)

=head2 $metamethod->add_around_modifier($code)

These methods all take a subroutine reference and apply it as a
modifier to the original method.

=head2 $metamethod->before_modifiers

=head2 $metamethod->after_modifiers

=head2 $metamethod->around_modifiers

These methods all return a list of subroutine references which are
acting as the specified type of modifier.

=cut
