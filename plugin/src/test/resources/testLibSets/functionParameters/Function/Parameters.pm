package Function::Parameters;

use v5.14.0;
use warnings;

use Carp qw(croak confess);
use Scalar::Util qw(blessed);

sub _croak {
    my (undef, $file, $line) = caller 1;
    die @_, " at $file line $line.\n";
}

use XSLoader;
BEGIN {
    our $VERSION = '2.001003';
    #$VERSION =~ s/-TRIAL[0-9]*\z//;
    XSLoader::load;
}

sub _assert_valid_identifier {
    my ($name, $with_dollar) = @_;
    my $bonus = $with_dollar ? '\$' : '';
    $name =~ /\A${bonus}[^\W\d]\w*\z/
        or confess qq{"$name" doesn't look like a valid identifier};
}

sub _assert_valid_attributes {
    my ($attrs) = @_;
    $attrs =~ m{
        \A \s*+
            : \s*+
            (?&ident) (?! [^\s:(] ) (?&param)?+ \s*+
            (?:
            (?: : \s*+ )?
            (?&ident) (?! [^\s:(] ) (?&param)?+ \s*+
            )*+
            \z

            (?(DEFINE)
            (?<ident>
            [^\W\d]
            \w*+
            )
            (?<param>
            \(
            [^()\\]*+
            (?:
            (?:
            \\ .
            |
            (?&param)
            )
            [^()\\]*+
            )*+
            \)
            )
            )
    }sx or confess qq{"$attrs" doesn't look like valid attributes};
}

sub _reify_type_moose {
    require Moose::Util::TypeConstraints;
    Moose::Util::TypeConstraints::find_or_create_isa_type_constraint($_[0])
}

sub _malformed_type {
    my ($type, $msg) = @_;
    my $pos = pos $_[0];
    substr $type, $pos, 0, ' <-- HERE ';
    croak "Malformed type: $msg marked by <-- HERE in '$type'";
}

sub _reify_type_auto_term {
    # (str, caller)
    $_[0] =~ /\G ( \w+ (?: :: \w+)* ) \s* /xgc or _malformed_type $_[0], "missing type name";
    my $name = $1;
    $name = "$_[1]::$name" unless $name =~ /::/;
    my $fun = do {
        no strict 'refs';
        defined &$name or croak "Undefined type name $name";
        \&$name
    };

    $_[0] =~ /\G \[ \s* /xgc
        or return $fun;

    my @args;
    until ($_[0] =~ /\G \] \s* /xgc) {
        $_[0] =~ /\G , \s* /xgc or _malformed_type $_[0], "missing ',' or ']'"
            if @args;
        push @args, &_reify_type_auto_union;
    }

    sub {$fun->([ map $_->(), @args ])}
}

sub _reify_type_auto_union {
    # (str, caller)
    my $fun = &_reify_type_auto_term;
    while ($_[0] =~ /\G \| \s* /xgc) {
        my $right = &_reify_type_auto_term;
        my $left = $fun;
        $fun = sub {$left->() | $right->()};
    }
    $fun
}

sub _reify_type_auto {
    my ($type) = @_;
    my $caller = caller;

    $type =~ /\G \s+ /xgc;
    my $tfun = _reify_type_auto_union $type, $caller;
    $type =~ /\G \z/xgc or _malformed_type $type, "trailing garbage";
    $tfun->()
}

sub _delete_default {
    my ($href, $key, $default) = @_;
    exists $href->{$key} ? delete $href->{$key} : $default
}

sub _find_or_add_idx {
    my ($array, $x) = @_;
    my $index;
    for my $i (0 .. $#$array) {
        if ($array->[$i] == $x) {
            $index = $i;
            last;
        }
    }
    unless (defined $index) {
        $index = @$array;
        push @$array, $x;
    }
    $index
}

my %type_map = (
    function_strict    => {},
    function_lax       => {
        defaults => 'function_strict',
        strict   => 0,
    },
    function           => { defaults => 'function_strict' },

    method_strict      => {
        defaults   => 'function_strict',
        attributes => ':method',
        shift      => '$self',
        invocant   => 1,
    },
    method_lax         => {
        defaults => 'method_strict',
        strict   => 0,
    },
    method             => { defaults => 'method_strict' },

    classmethod_strict => {
        defaults => 'method_strict',
        shift    => '$class',
    },
    classmethod_lax    => {
        defaults => 'classmethod_strict',
        strict   => 0,
    },
    classmethod        => { defaults => 'classmethod_strict' },

    around             => {
        defaults    => 'method',
        name        => 'required',
        install_sub => 'around',
        shift       => [ '$orig', '$self' ],
        runtime     => 1,
    },
    (
        map +(
            $_ => {
                defaults    => 'method',
                name        => 'required',
                install_sub => $_,
                runtime     => 1,
            }
        ), qw(
            before after augment override
        ),
    ),
);

my %import_map = (
    fun          => 'function',
    (
        map +($_ => $_),
            qw(
                method
                classmethod
                before
                after
                around
                augment
                override
            )
    ),

    ':strict'    => {
        fun    => 'function_strict',
        method => 'method_strict',
    },

    ':lax'       => {
        fun    => 'function_lax',
        method => 'method_lax',
    },

    ':std'       => [ qw(fun method) ],
    ':modifiers' => [ qw(
        before
        after
        around
        augment
        override
    ) ],
);
for my $v (values %import_map) {
    if (ref $v eq 'ARRAY') {
        $v = {
            map +($_ => $import_map{$_} || die "Internal error: $v => $_"),
                @$v
        };
    }
}

sub import {
    my $class = shift;

    my %imports;
    @_ = qw(:std) if !@_;
    for my $item (@_) {
        my $part;
        if (ref $item) {
            $part = $item;
        }
        else {
            my $type = $import_map{$item}
                or croak qq{"$item" is not exported by the $class module};
            $part = ref $type
                ? $type
                : { $item => $type };
        }
        @imports{keys %$part} = values %$part;
    }

    my %spec;

    for my $name (sort keys %imports) {
        _assert_valid_identifier $name;
        my $proto_type = $imports{$name};

        $proto_type = { defaults => $proto_type } unless ref $proto_type;

        my %type = %$proto_type;
        while (my $defaults = delete $type{defaults}) {
            my $base = $type_map{$defaults}
                or confess qq["$defaults" doesn't look like a valid type (one of ${\join ', ', sort keys %type_map})];
            %type = (%$base, %type);
        }

        if (exists $type{strict}) {
            $type{check_argument_count} ||= $type{strict};
            delete $type{strict};
        }

        my %clean;

        $clean{name} = delete $type{name} // 'optional';
        $clean{name} =~ /\A(?:optional|required|prohibited)\z/
            or confess qq["$clean{name}" doesn't look like a valid name attribute (one of optional, required, prohibited)];

        $clean{attrs} = delete $type{attributes} // '';
        _assert_valid_attributes $clean{attrs} if $clean{attrs};

        if (!exists $type{reify_type}) {
            $clean{reify_type} = \&_reify_type_auto;
        }
        else {
            my $rt = delete $type{reify_type} // '(undef)';
            if (!ref $rt) {
                $rt =
                    $rt eq 'auto' ? \&_reify_type_auto :
                        $rt eq 'moose' ? \&_reify_type_moose :
                            confess qq{"$rt" isn't a known predefined type reifier};
            }
            elsif (ref $rt ne 'CODE') {
                confess qq{"$rt" doesn't look like a type reifier};
            }

            $clean{reify_type} = $rt;
        }

        if (!exists $type{install_sub}) {
            $clean{install_sub} = '';
        }
        else {
            my $is = delete $type{install_sub};
            if (!ref $is) {
                _assert_valid_identifier $is;
            }
            elsif (ref $is ne 'CODE') {
                confess qq{"$is" doesn't look like a sub installer};
            }

            $clean{install_sub} = $is;
        }

        $clean{shift} = do {
            my $shift = delete $type{shift} // [];
            $shift = [ $shift ] if !ref $shift;
            my $str = '';
            my @shifty_types;
            for my $item (@$shift) {
                my ($name, $type);
                if (ref $item) {
                    @$item == 2 or confess "A 'shift' item must have 2 elements, not " . @$item;
                    ($name, $type) = @$item;
                }
                else {
                    $name = $item;
                }
                _assert_valid_identifier $name, 1;
                $name eq '$_' and confess q[Using "$_" as a parameter is not supported];
                $str .= $name;
                if (defined $type) {
                    blessed($type) or confess "${name}'s type must be an object, not $type";
                    my $index = _find_or_add_idx \@shifty_types, $type;
                    $str .= "/$index";
                }
                $str .= ' ';
            }
            $clean{shift_types} = \@shifty_types;
            $str
        };

        $clean{default_arguments} = _delete_default \%type, 'default_arguments', 1;
        $clean{named_parameters} = _delete_default \%type, 'named_parameters', 1;
        $clean{types} = _delete_default \%type, 'types', 1;
        $clean{invocant} = _delete_default \%type, 'invocant', 0;
        $clean{runtime} = _delete_default \%type, 'runtime', 0;
        $clean{check_argument_count} = _delete_default \%type, 'check_argument_count', 1;
        $clean{check_argument_types} = _delete_default \%type, 'check_argument_types', 1;

        %type and confess "Invalid keyword property: @{[ sort keys %type ]}";

        $spec{$name} = \%clean;
    }

    my %config = %{$^H{+HINTK_CONFIG} // {}};
    for my $kw (keys %spec) {
        my $type = $spec{$kw};

        my $flags =
            $type->{name} eq 'prohibited' ? FLAG_ANON_OK :
                $type->{name} eq 'required' ? FLAG_NAME_OK :
                    FLAG_ANON_OK | FLAG_NAME_OK
        ;
        $flags |= FLAG_DEFAULT_ARGS if $type->{default_arguments};
        $flags |= FLAG_CHECK_NARGS if $type->{check_argument_count};
        $flags |= FLAG_CHECK_TARGS if $type->{check_argument_types};
        $flags |= FLAG_INVOCANT if $type->{invocant};
        $flags |= FLAG_NAMED_PARAMS if $type->{named_parameters};
        $flags |= FLAG_TYPES_OK if $type->{types};
        $flags |= FLAG_RUNTIME if $type->{runtime};
        $config{$kw} = {
            HINTSK_FLAGS, => $flags,
            HINTSK_SHIFT, => $type->{shift},
            HINTSK_ATTRS, => $type->{attrs},
            HINTSK_REIFY, => $type->{reify_type},
            HINTSK_INSTL, => $type->{install_sub},
            !@{$type->{shift_types}} ? () : (
                HINTSK_SHIF2, => $type->{shift_types},
            ),
        };
    }
    $^H{+HINTK_CONFIG} = \%config;
}

sub unimport {
    my $class = shift;

    if (!@_) {
        delete $^H{+HINTK_CONFIG};
        return;
    }

    my %config = %{$^H{+HINTK_CONFIG}};
    delete @config{@_};
    $^H{+HINTK_CONFIG} = \%config;
}


our %metadata;

sub _register_info {
    my (
        $key,
        $declarator,
        $shift,
        $positional_required,
        $positional_optional,
        $named_required,
        $named_optional,
        $slurpy,
        $slurpy_type,
    ) = @_;

    my $info = {
        declarator          => $declarator,
        shift               => $shift,
        positional_required => $positional_required,
        positional_optional => $positional_optional,
        named_required      => $named_required,
        named_optional      => $named_optional,
        slurpy              => defined $slurpy ? [ $slurpy, $slurpy_type ] : undef,
    };

    $metadata{$key} = $info;
}

sub _mkparam1 {
    my ($pair) = @_;
    my ($v, $t) = @{$pair || []} or return undef;
    Function::Parameters::Param->new(
        name => $v,
        type => $t,
    )
}

sub _mkparams {
    my @r;
    while (my ($v, $t) = splice @_, 0, 2) {
        push @r, Function::Parameters::Param->new(
            name => $v,
            type => $t,
        );
    }
    \@r
}

sub info {
    my ($func) = @_;
    my $key = _cv_root $func or return undef;
    my $info = $metadata{$key} or return undef;
    require Function::Parameters::Info;
    Function::Parameters::Info->new(
        keyword => $info->{declarator},
        nshift  => $info->{shift},
        slurpy  => _mkparam1($info->{slurpy}),
        (
            map +("_$_" => _mkparams @{$info->{$_}}),
                qw(
                    positional_required
                    positional_optional
                    named_required
                    named_optional
                )
        )
    )
}

'ok'

__END__

=encoding UTF-8

=for highlighter language=perl

=head1 NAME

Function::Parameters - define functions and methods with parameter lists ("subroutine signatures")

=head1 SYNOPSIS

 use Function::Parameters;

 # plain function
 fun foo($x, $y, $z = 5) {
     return $x + $y + $z;
 }
 print foo(1, 2), "\n";  # 8

 # method with implicit $self
 method bar($label, $n) {
     return "$label: " . ($n * $self->scale);
 }

 # named arguments: order doesn't matter in the call
 fun create_point(:$x, :$y, :$color) {
     print "creating a $color point at ($x, $y)\n";
 }
 create_point(
     color => "red",
     x     => 10,
     y     => 5,
 );

 package Derived {
     use Function::Parameters qw(:std :modifiers);
     use Moo;

     extends 'Base';

     has 'go_big' => (
         is => 'ro',
     );

     # "around" method with implicit $orig and $self
     around size() {
         return $self->$orig() * 2 if $self->go_big;
         return $self->$orig();
     }
 }

=head1 DESCRIPTION

This module provides two new keywords, C<fun> and C<method>, for defining
functions and methods with parameter lists. At minimum this saves you from
having to unpack C<@_> manually, but this module can do much more for you.

The parameter lists provided by this module are similar to the C<signatures>
feature available in perl v5.20+. However, this module supports all perl
versions starting from v5.14, it offers far more features than core signatures,
and it is not experimental. The downside is that you need a C compiler if you
want to install it from source, as it uses Perl's
L<keyword plugin|perlapi/PL_keyword_plugin> API in order to work reliably
without requiring a source filter.

=head2 Default functionality

This module is a lexically scoped pragma: If you C<use Function::Parameters>
inside a block or file, the keywords won't be available outside of that block
or file.

You can also disable C<Function::Parameters> within a block:

 {
     no Function::Parameters;  # disable all keywords
     ...
 }

Or explicitly list the keywords you want to disable:

 {
     no Function::Parameters qw(method);
     # 'method' is a normal identifier here
     ...
 }

You can also explicitly list the keywords you want to enable:

 use Function::Parameters qw(fun);  # provides 'fun' but not 'method'
 use Function::Parameters qw(method);  # provides 'method' but not 'fun'

=head3 Simple parameter lists

By default you get two keywords, C<fun> and C<method> (but see
L</Customizing and extending> below). C<fun> is very similar to C<sub>. You can
use it to define both named and anonymous functions:

 fun left_pad($str, $n) {
     return sprintf '%*s', $n, $str;
 }

 print left_pad("hello", 10), "\n";

 my $twice = fun ($x) { $x * 2 };
 print $twice->(21), "\n";

In the simplest case the parameter list is just a comma-separated list of zero
or more scalar variables (enclosed in parentheses, following the function name,
if any).

C<Function::Parameters> automatically validates the arguments your function is
called with. If the number of arguments doesn't match the parameter list, an
exception is thrown.

Apart from that, the parameter variables are defined and initialized as if by:

 sub left_pad {
     sub left_pad;
     my ($str, $n) = @_;
     ...
 }

In particular, C<@_> is still available in functions defined by C<fun> and
holds the original argument list.

The inner C<sub left_pad;> declaration is intended to illustrate that the name
of the function being defined is in scope in its own body, meaning you can call
it recursively without having to use parentheses:

 fun fac($n) {
     return 1 if $n < 2;
     return $n * fac $n - 1;
 }

In a normal C<sub> the last line would have had to be written
C<return $n * fac($n - 1);>.

C<method> is almost the same as C<fun> but automatically creates a C<$self>
variable as the first parameter (which is removed from C<@_>):

 method foo($x, $y) {
    ...
 }

 # works like:
 sub foo :method {
    my $self = shift;
    my ($x, $y) = @_;
    ...
 }

As you can see, the C<:method> attribute is also added automatically (see
L<attributes/method> for details).

In some cases (e.g. class methods) C<$self> is not the best name for the
invocant of the method. You can override it on a case-by-case basis by putting
a variable name followed by a C<:> (colon) as the first thing in the parameter
list:

 method new($class: $x, $y) {
     return bless { x => $x, y => $y }, $class;
 }

Here the invocant is named C<$class>, not C<$self>. It looks a bit weird but
still works the same way if the remaining parameter list is empty:

 method from_env($class:) {
     return $class->new($ENV{x}, $ENV{y});
 }

=head3 Default arguments

(Most of the following examples use C<fun> only. Unless specified otherwise
everything applies to C<method> as well.)

You can make some arguments optional by giving them default values.

 fun passthrough($x, $y = 42, $z = []) {
     return ($x, $y, $z);
 }

In this example the first parameter C<$x> is required but C<$y> and C<$z> are
optional.

 passthrough('a', 'b', 'c', 'd')   # error: Too many arguments
 passthrough('a', 'b', 'c')        # returns ('a', 'b', 'c')
 passthrough('a', 'b')             # returns ('a', 'b', [])
 passthrough('a', undef)           # returns ('a', undef, [])
 passthrough('a')                  # returns ('a', 42, [])
 passthrough()                     # error: Too few arguments

Default arguments are evaluated whenever a corresponding real argument is not
passed in by the caller. C<undef> counts as a real argument; you can't use the
default value for parameter I<N> and still pass a value for parameter I<N+1>.
C<$z = []> means each call that doesn't pass a third argument gets a new array
reference (they're not shared between calls).

Default arguments are evaluated as part of the function body, allowing for
silliness such as:

 fun weird($name = return "nope") {
     print "Hello, $name!\n";
     return $name;
 }

 weird("Larry");  # prints "Hello, Larry!" and returns "Larry"
 weird();         # returns "nope" immediately; function body doesn't run

Preceding parameters are in scope for default arguments:

 fun dynamic_default($x, $y = length $x) {
    return "$x/$y";
 }

 dynamic_default("hello", 0)  # returns "hello/0"
 dynamic_default("hello")     # returns "hello/5"
 dynamic_default("abc")       # returns "abc/3"

If you just want to make a parameter optional without giving it a special
value, write C<$param = undef>. There is a special shortcut syntax for
this case: C<$param = undef> can also be written C<$param => (with no following
expression).

 fun foo($x = undef, $y = undef, $z = undef) {
     # three arguments, all optional
     ...
 }

 fun foo($x=, $y=, $z=) {
     # shorter syntax, same meaning
     ...
 }

Optional parameters must come at the end. It is not possible to have a required
parameter after an optional one.

=head3 Slurpy/rest parameters

The last parameter of a function or method can be an array. This lets you slurp
up any number of arguments the caller passes (0 or more).

 fun scale($factor, @values) {
     return map { $_ * $factor } @values;
 }

 scale(10, 1 .. 4)  # returns (10, 20, 30, 40)
 scale(10)          # returns ()

You can also use a hash, but then the number of arguments has to be even.

=head3 Named parameters

As soon as your functions take more than three arguments, it gets harder to
keep track of what argument means what:

 foo($handle, $w, $h * 2 + 15, 1, 24, 'icon');
 # what do these arguments mean?

C<Function::Parameters> offers an alternative for these kinds of situations in
the form of named parameters. Unlike the parameters described previously, which
are identified by position, these parameters are identified by name:

 fun create_point(:$x, :$y, :$color) {
     ...
 }

 # Case 1
 create_point(
     x     => 50,
     y     => 50,
     color => 0xff_00_00,
 );

To create a named parameter, put a C<:> (colon) in front of it in the parameter
list. When the function is called, the arguments have to be supplied in the
form of a hash initializer (a list of alternating keys/values). As with a hash,
the order of key/value pairs doesn't matter (except in the case of duplicate
keys, where the last occurrence wins):

 # Case 2
 create_point(
     color => 0xff_00_00,
     x     => 50,
     y     => 50,
 );

 # Case 3
 create_point(
     x     => 200,
     color => 0x12_34_56,
     color => 0xff_00_00,
     x     => 50,
     y     => 50,
 );

Case 1, Case 2, and Case 3 all mean the same thing.

As with positional parameters, you can make named parameters optional by
supplying a L<default argument|/Default arguments>:

 fun create_point(:$x, :$y, :$color = 0x00_00_00) {
     ...
 }

 create_point(x => 0, y => 64)  # color => 0x00_00_00 is implicit

If you want to accept any key/value pairs, you can add a
L<rest parameter|/Slurpy/rest parameters> (hashes are particularly useful):

 fun accept_all_keys(:$name, :$age, %rest) {
     ...
 }

 accept_all_keys(
     age     => 42,
     gender  => 2,
     name    => "Jamie",
     marbles => [],
 );
 # $name = "Jamie";
 # $age = 42;
 # %rest = (
 #     gender  => 2,
 #     marbles => [],
 # );

You can combine positional and named parameters but all positional parameters
have to come first:

 method output(
    $data,
    :$handle       = $self->output_handle,
    :$separator    = $self->separator,
    :$quote_fields = 0,
 ) {
     ...
 }

 $obj->output(["greetings", "from", "space"]);
 $obj->output(
    ["a", "random", "example"],
    quote_fields => 1,
    separator    => ";",
 );

=head3 Unnamed parameters

If your function doesn't use a particular parameter at all, you can omit its
name and just write a sigil in the parameter list:

 register_callback('click', fun ($target, $) {
     ...
 });

Here we're calling a hypothetical C<register_callback> function that registers
our coderef to be called in response to a C<click> event. It will pass two
arguments to the click handler, but the coderef only cares about the first one
(C<$target>). The second parameter doesn't even get a name (just a sigil,
C<$>). This marks it as unused.

This case typically occurs when your functions have to conform to an externally
imposed interface, e.g. because they're called by someone else. It can happen
with callbacks or methods that don't need all of the arguments they get.

You can use unnamed L<slurpy parameters|/Slurpy/rest parameters> to accept and
ignore all following arguments. In particular, C<fun foo(@)> is a lot like
C<sub foo> in that it accepts and ignores any number of arguments (apart from
leaving them in C<@_>).

=head3 Type constraints

It is possible to automatically check the types of arguments passed to your
function. There are two ways to do this.

=over

=item 1.

 use Types::Standard qw(Str Int ArrayRef);

 fun foo(Str $label, ArrayRef[Int] $counts) {
     ...
 }

In this variant you simply put the name of a type in front of a parameter. The
way this works is that C<Function::Parameters> parses the type using very
simple rules:

=over

=item *

A I<type> is a sequence of one or more simple types, separated by C<|> (pipe).
C<|> is meant for union types (e.g. C<Str | ArrayRef[Int]> would accept either
a string or reference to an array of integers).

=item *

A I<simple type> is an identifier, optionally followed by a list of one or more
types, separated by C<,> (comma), enclosed in C<[> C<]> (square brackets).

=back

C<Function::Parameters> then resolves simple types by looking for functions of
the same name in your current package. A type specification like
C<Str | ArrayRef[Int]> ends up running the Perl code
C<Str() | ArrayRef([Int()])> (at compile time, while the function definition is
being processed). In other words, C<Function::Parameters> doesn't support any
types natively; it simply uses whatever is in scope.

You don't have to define these functions yourself. You can also import them
from a type library such as L<C<Types::Standard>|Types::Standard> or
L<C<MooseX::Types::Moose>|MooseX::Types::Moose>.

The only requirement is that the returned value (here referred to as C<$tc>,
for "type constraint") is an object that provides C<< $tc->check($value) >>
and C<< $tc->get_message($value) >> methods. C<check> is called to determine
whether a particular value is valid; it should return a true or false value.
C<get_message> is called on values that fail the C<check> test; it should
return a string that describes the error.

=item 2.

 my ($my_type, $some_other_type);
 BEGIN {
     $my_type = Some::Constraint::Class->new;
     $some_other_type = Some::Other::Class->new;
 }

 fun foo(($my_type) $label, ($some_other_type) $counts) {
     ...
 }

In this variant you enclose an arbitrary Perl expression in C<(> C<)>
(parentheses) and put it in front of a parameter. This expression is evaluated
at compile time and must return a type constraint object as described above.
(If you use variables here, make sure they're defined at compile time.)

=back

=head3 Method modifiers

C<Function::Parameters> has support for method modifiers as provided by
L<C<Moo>|Moo> or L<C<Moose>|Moose>. They're not exported by default, so you
have to say

 use Function::Parameters qw(:modifiers);

to get them. This line gives you method modifiers I<only>; C<fun> and C<method>
are not defined. To get both the standard keywords and method modifiers, you
can either write two C<use> lines:

 use Function::Parameters;
 use Function::Parameters qw(:modifiers);

or explicitly list the keywords you want:

 use Function::Parameters qw(fun method :modifiers);

or add the C<:std> import tag (which gives you the default import behavior):

 use Function::Parameters qw(:std :modifiers);

This defines the following additional keywords: C<before>, C<after>, C<around>,
C<augment>, C<override>. These work mostly like C<method>, but they don't
install the function into your package themselves. Instead they invoke whatever
C<before>, C<after>, C<around>, C<augment>, or C<override> function
(respectively) is in scope to do the job.

 before foo($x, $y, $z) {
     ...
 }

works like

 &before('foo', method ($x, $y, $z) {
     ...
 });

C<after>, C<augment>, and C<override> work the same way.

C<around> is slightly different: Instead of shifting off the first element of
C<@_> into C<$self> (as C<method> does), it shifts off I<two> values:

 around foo($x, $y, $z) {
     ...
 }

works like

 &around('foo', sub :method {
     my $orig = shift;
     my $self = shift;
     my ($x, $y, $z) = @_;
     ...
 });

(except you also get the usual C<Function::Parameters> features such as
checking the number of arguments, etc).

C<$orig> and C<$self> both count as invocants and you can override their names
like this:

 around foo($original, $object: $x, $y, $z) {
     # $original is a reference to the wrapped method;
     # $object is the object we're being called on
     ...
 }

If you use C<:> to pick your own invocant names in the parameter list of
C<around>, you must specify exactly two variables.

These modifiers also differ from C<fun> and C<method> (and C<sub>) in that they
require a function name (there are no anonymous method modifiers) and they
take effect at runtime, not compile time. When you say C<fun foo() {}>, the
C<foo> function is defined right after the closing C<}> of the function body is
parsed. But with e.g. C<before foo() {}>, the declaration becomes a normal
function call (to the C<before> function in the current package), which is
performed at runtime.

=head3 Prototypes and attributes

You can specify attributes (see L<perlsub/Subroutine Attributes>) for your
functions using the usual syntax:

 fun deref($x) :lvalue {
    ${$x}
 }

 my $silly;
 deref(\$silly) = 42;

To specify a prototype (see L<perlsub/Prototypes>), use the C<prototype>
attribute:

 fun mypush($aref, @values) :prototype(\@@) {
     push @{$aref}, @values;
 }

=head3 Introspection

The function C<Function::Parameters::info> lets you introspect parameter lists
at runtime. It is not exported, so you have to call it by its full name.

It takes a reference to a function and returns either C<undef> (if it knows
nothing about the function) or an object that describes the parameter list of
the given function. See
L<C<Function::Parameters::Info>|Function::Parameters::Info> for details.

=head2 Customizing and extending

=head3 Wrapping C<Function::Parameters>

Due to its nature as a lexical pragma, importing from C<Function::Parameters>
always affects the scope that is currently being compiled. If you want to write
a wrapper module that enables C<Function::Parameters> automatically, just call
C<< Function::Parameters->import >> from your own C<import> method (and
C<< Function::Parameters->unimport >> from your C<unimport>, as required).

=head3 Gory details of importing

At the lowest layer C<use Function::Parameters ...> takes a list of one or more
hash references. Each key is a keyword to be defined as specified by the
corresponding value, which must be another hash reference containing
configuration options.

 use Function::Parameters
     {
         keyword_1 => { ... },
         keyword_2 => { ... },
     },
     {
         keyword_3 => { ... },
     };

If you don't specify a particular option, its default value is used. The
available configuration options are:

=over

=item C<attributes>

(string) The attributes that every function declared with this
keyword should have (in the form of source code, with a leading C<:>).

Default: nothing

=item C<check_argument_count>

(boolean) Whether functions declared with this keyword should check how many
arguments they are called with. If false, omitting a required argument sets it
to C<undef> and excess arguments are silently ignored. If true, an exception is
thrown if too few or too many arguments are passed.

Default: C<1>

=item C<check_argument_types>

(boolean) Whether functions declared with this keyword should check the types
of the arguments they are called with. If false,
L<type constraints|/Type constraints> are parsed but silently ignored. If true,
an exception is thrown if an argument fails a type check.

Default: C<1>

=item C<default_arguments>

(boolean) Whether functions declared with this keyword should allow default
arguments in their parameter list. If false,
L<default arguments|/Default arguments> are a compile-time error.

Default: C<1>

=item C<install_sub>

(sub name or reference) If this is set, named functions declared with this
keyword are not entered into the symbol table directly. Instead the subroutine
specified here (by name or reference) is called with two arguments, the name of
the function being declared and a reference to its body.

Default: nothing

=item C<invocant>

(boolean) Whether functions declared with this keyword should allow explicitly
specifying invocant(s) at the beginning of the parameter list (as in
C<($invocant: ...)> or C<($invocant1, $invocant2, $invocant3: ...)>).

Default: 0

=item C<name>

(string) There are three possible values for this option. C<'required'> means
functions declared with this keyword must have a name. C<'prohibited'> means
specifying a name is not allowed. C<'optional'> means this keyword can be used
for both named and anonymous functions.

Default: C<'optional'>

=item C<named_parameters>

(boolean) Whether functions declared with this keyword should allow named
parameters. If false, L<named parameters|/Named parameters> are a compile-time
error.

Default: C<1>

=item C<reify_type>

(coderef or C<'auto'> or C<'moose'>) The code reference used to resolve
L<type constraints|/Type constraints> in functions declared with this keyword.
It is called once for each type constraint that doesn't use the C<( EXPR )>
syntax, with one argument, the text of the type in the parameter list (e.g.
C<'ArrayRef[Int]'>). The package the function declaration is in is available
through L<C<caller>|perlfunc/caller EXPR>.

The only requirement is that the returned value (here referred to as C<$tc>,
for "type constraint") is an object that provides C<< $tc->check($value) >>
and C<< $tc->get_message($value) >> methods. C<check> is called to determine
whether a particular value is valid; it should return a true or false value.
C<get_message> is called on values that fail the C<check> test; it should
return a string that describes the error.

Instead of a code reference you can also specify one of two strings.

C<'auto'> stands for a built-in type reifier that treats identifiers as
subroutine names, C<[> C<]> as an array reference, and C<|> as bitwise or. In
other words, it parses and executes type constraints (mostly) as if they had
been Perl source code.

C<'moose'> stands for a built-in type reifier that loads
L<C<Moose::Util::TypeConstraints>|Moose::Util::TypeConstraints> and just
forwards to
L<C<find_or_create_isa_type_constraint>|Moose::Util::TypeConstraints/find_or_create_isa_type_constraint($type_name)>.

Default: C<'auto'>

=item C<runtime>

(boolean) Whether functions declared with this keyword should be installed into
the symbol table at runtime. If false, named functions are defined (or their
L<C<install_sub>|/C<install_sub>> is invoked if specified) immediately after
their declaration is parsed (as with L<C<sub>|perlfunc/sub NAME BLOCK>). If
true, function declarations become normal statements that only take effect at
runtime (similar to C<*foo = sub { ... };> or
C<< $install_sub->('foo', sub { ... }); >>, respectively).

Default: C<0>

=item C<shift>

(string or arrayref) In its simplest form, this is the name of a variable that
acts as the default invocant (a required leading argument that is removed from
C<@_>) for all functions declared with this keyword (e.g.  C<'$self'> for
methods). You can also set this to an array reference of strings, which lets
you specify multiple default invocants, or even to an array reference of array
references of the form C<[ $name, $type ]> (where C<$name> is the variable name
and C<$type> is a L<type constraint object|/Type constraints>), which lets you
specify multiple default invocants with type constraints.

If you define any default invocants here and also allow individual declarations
to override the default (with C<< invocant => 1 >>), the number of overridden
invocants must match the default. For example, C<method> has a default invocant
of C<$self>, so C<method foo($x, $y: $z)> is invalid because it tries to define
two invocants.

Default: C<[]> (meaning no invocants)

=item C<strict>

(boolean) Whether functions declared with this keyword should do "strict"
checks on their arguments. Currently setting this simply sets
L<C<check_argument_count>|/C<check_argument_count>> to the same value with no
other effects.

Default: nothing

=item C<types>

(boolean) Whether functions declared with this keyword should allow type
constraints in their parameter lists. If false, trying to use
L<type constraints|/Type constraints> is a compile-time error.

Default: C<1>

=back

You can get the same effect as C<use Function::Parameters;> by saying:

 use Function::Parameters {
     fun => {
         # 'fun' uses default settings only
     },
     method => {
         attributes => ':method',
         shift      => '$self',
         invocant   => 1,
         # the rest is defaults
     },
 };

=head3 Configuration bundles

Because specifying all these configuration options from scratch each time is a
lot of writing, C<Function::Parameters> offers configuration bundles in the
form of special strings. These strings can be used to replace a configuration
hash completely or as the value of the C<defaults> pseudo-option within a
configuration hash. The latter lets you use the configuration bundle behind the
string to provide defaults and tweak them with your own settings.

The following bundles are available:

=over

=item C<function_strict>

Equivalent to C<{}>, i.e. all defaults.

=item C<function_lax>

Equivalent to:

 {
     defaults => 'function_strict',
     strict   => 0,
 }

i.e. just like L<C<function_strict>|/C<function_strict>> but with
L<C<strict>|/C<strict>> checks turned off.

=item C<function>

Equivalent to C<function_strict>. This is what the default C<fun> keyword
actually uses. (In version 1 of this module, C<function> was equivalent to
C<function_lax>.)

=item C<method_strict>

Equivalent to:

 {
     defaults   => 'function_strict',
     attributes => ':method',
     shift      => '$self',
     invocant   => 1,
 }

=item C<method_lax>

Equivalent to:

 {
     defaults => 'method_strict',
     strict   => 0,
 }

i.e. just like L<C<method_strict>|/C<method_strict>> but with
L<C<strict>|/C<strict>> checks turned off.

=item C<method>

Equivalent to C<method_strict>. This is what the default C<method> keyword
actually uses. (In version 1 of this module, C<method> was equivalent to
C<method_lax>.)

=item C<classmethod_strict>

Equivalent to:

 {
     defaults => 'method_strict',
     shift    => '$class',
 }

i.e. just like L<C<method_strict>|/C<method_strict>> but the implicit first
parameter is called C<$class>, not C<$self>.

=item C<classmethod_lax>

Equivalent to:

 {
     defaults => 'classmethod_strict',
     strict   => 0,
 }

i.e. just like L<C<classmethod_strict>|/C<classmethod_strict>> but with
L<C<strict>|/C<strict>> checks turned off.

=item C<classmethod>

Equivalent to C<classmethod_strict>. This is currently not used anywhere within
C<Function::Parameters>.

=item C<around>

Equivalent to:

 {
     defaults    => 'method',
     install_sub => 'around',
     shift       => ['$orig', '$self'],
     runtime     => 1,
     name        => 'required',
 }

i.e. just like L<C<method>|/C<method>> but with a custom installer
(C<'around'>), two implicit first parameters, only taking effect at
runtime, and a method name is required.

=item C<before>

Equivalent to:

 {
     defaults    => 'method',
     install_sub => 'before',
     runtime     => 1,
     name        => 'required',
 }

i.e. just like L<C<method>|/C<method>> but with a custom installer
(C<'before'>), only taking effect at runtime, and a method name is required.

=item C<after>

Equivalent to:

 {
     defaults    => 'method',
     install_sub => 'after',
     runtime     => 1,
     name        => 'required',
 }

i.e. just like L<C<method>|/C<method>> but with a custom installer
(C<'after'>), only taking effect at runtime, and a method name is required.

=item C<augment>

Equivalent to:

 {
     defaults    => 'method',
     install_sub => 'augment',
     runtime     => 1,
     name        => 'required',
 }

i.e. just like L<C<method>|/C<method>> but with a custom installer
(C<'augment'>), only taking effect at runtime, and a method name is required.

=item C<override>

Equivalent to:

 {
     defaults    => 'method',
     install_sub => 'override',
     runtime     => 1,
     name        => 'required',
 }

i.e. just like L<C<method>|/C<method>> but with a custom installer
(C<'override'>), only taking effect at runtime, and a method name is required.

=back

You can get the same effect as C<use Function::Parameters;> by saying:

 use Function::Parameters {
     fun    => { defaults => 'function' },
     method => { defaults => 'method' },
 };

or:

 use Function::Parameters {
     fun    => 'function',
     method => 'method',
 };

=head3 Import tags

In addition to hash references you can also use special strings in your import
list. The following import tags are available:

=over

=item C<'fun'>

Equivalent to C<< { fun => 'function' } >>.

=item C<'method'>

Equivalent to C<< { method => 'method' } >>.

=item C<'classmethod'>

Equivalent to C<< { classmethod => 'classmethod' } >>.

=item C<'before'>

Equivalent to C<< { before => 'before' } >>.

=item C<'after'>

Equivalent to C<< { after => 'after' } >>.

=item C<'around'>

Equivalent to C<< { around => 'around' } >>.

=item C<'augment'>

Equivalent to C<< { augment => 'augment' } >>.

=item C<'override'>

Equivalent to C<< { override => 'override' } >>.

=item C<':strict'>

Equivalent to C<< { fun => 'function_strict', method => 'method_strict' } >>
but that's just the default behavior anyway.

=item C<':lax'>

Equivalent to C<< { fun => 'function_lax', method => 'method_lax' } >>, i.e. it
provides C<fun> and C<method> keywords that define functions that don't check
their arguments.

=item C<':std'>

Equivalent to C<< 'fun', 'method' >>. This is what's used by default:

 use Function::Parameters;

is the same as:

 use Function::Parameters qw(:std);

=item C<':modifiers'>

Equivalent to C<< 'before', 'after', 'around', 'augment', 'override' >>.

=back

For example, when you say

 use Function::Parameters qw(:modifiers);

C<:modifiers> is an import tag that L<expands to|/C<':modifiers'>>

 use Function::Parameters qw(before after around augment override);

Each of those is another import tag. Stepping through the first one:

 use Function::Parameters qw(before);

is L<equivalent to|/C<'before'>>:

 use Function::Parameters { before => 'before' };

This says to define the keyword C<before> according to the
L<configuration bundle C<before>|/C<before>>:

 use Function::Parameters {
     before => {
         defaults    => 'method',
         install_sub => 'before',
         runtime     => 1,
         name        => 'required',
     },
 };

The C<< defaults => 'method' >> part L<pulls in|/Configuration bundles> the
contents of the L<C<'method'> configuration bundle|/C<method>> (which is the
same as L<C<'method_strict'>|/C<method_strict>>):

 use Function::Parameters {
     before => {
         defaults    => 'function_strict',
         attributes  => ':method',
         shift       => '$self',
         invocant    => 1,
         install_sub => 'before',
         runtime     => 1,
         name        => 'required',
     },
 };

This in turn uses the
L<C<'function_strict'> configuration bundle|/C<function_strict>> (which is
empty because it consists of default values only):

 use Function::Parameters {
     before => {
         attributes  => ':method',
         shift       => '$self',
         invocant    => 1,
         install_sub => 'before',
         runtime     => 1,
         name        => 'required',
     },
 };

But if we wanted to be completely explicit, we could write this as:

 use Function::Parameters {
     before => {
         check_argument_count => 1,
         check_argument_types => 1,
         default_arguments    => 1,
         named_parameters     => 1,
         reify_type           => 'auto',
         types                => 1,

         attributes  => ':method',
         shift       => '$self',
         invocant    => 1,
         install_sub => 'before',
         runtime     => 1,
         name        => 'required',
     },
 };

=head2 Incompatibilites with version 1 of C<Function::Parameters>

=over

=item *

Version 1 defaults to lax mode (no argument checks). To get the same behavior
on both version 1 and version 2, explicitly write either
C<use Function::Parameters qw(:strict);> (the new default) or
C<use Function::Parameters qw(:lax);> (the old default). (Or write
C<use Function::Parameters 2;> to trigger an error if an older version of
C<Function::Parameters> is loaded.)

=item *

Parameter lists used to be optional. The syntax C<fun foo { ... }> would accept
any number of arguments. This syntax has been removed; you now have to write
C<fun foo(@) { ... }> to accept (and ignore) all arguments. On the other hand,
if you meant for the function to take no arguments, write C<fun foo() { ... }>.

=item *

There used to be a shorthand syntax for prototypes: Using C<:(...)> (i.e. an
attribute with an empty name) as the first attribute was equivalent to
C<:prototype(...)>. This syntax has been removed.

=item *

The default type reifier used to be hardcoded to use L<C<Moose>|Moose> (as in
C<< reify_type => 'moose' >>). This has been changed to use whatever type
functions are in scope (C<< reify_type => 'auto' >>).

=item *

Type reifiers used to see the wrong package in
L<C<caller>|perlfunc/caller EXPR>. As a workaround the correct calling package
used to be passed as a second argument. This problem has been fixed and the
second argument has been removed. (Technically this is a core perl bug
(L<RT #129239|https://rt.perl.org/Public/Bug/Display.html?id=129239>) that
wasn't so much fixed as worked around in C<Function::Parameters>.)

If you want your type reifier to be compatible with both versions, you can do
this:

 sub my_reifier {
     my ($type, $package) = @_;
     $package //= caller;
     ...
 }

Or using C<Function::Parameters> itself:

 fun my_reifier($type, $package = caller) {
     ...
 }

=back

=begin :README

=head1 INSTALLATION

To download and install this module, use your favorite CPAN client, e.g.
L<C<cpan>|cpan>:

=for highlighter language=sh

    cpan Function::Parameters

Or L<C<cpanm>|cpanm>:

    cpanm Function::Parameters

To do it manually, run the following commands (after downloading and unpacking
the tarball):

    perl Makefile.PL
    make
    make test
    make install

=end :README

=head1 SUPPORT AND DOCUMENTATION

After installing, you can find documentation for this module with the
L<C<perldoc>|perldoc> command.

=for highlighter language=sh

    perldoc Function::Parameters

You can also look for information at
L<https://metacpan.org/pod/Function::Parameters>.

To see a list of open bugs, visit
L<https://rt.cpan.org/Public/Dist/Display.html?Name=Function-Parameters>.

To report a new bug, send an email to
C<bug-Function-Parameters [at] rt.cpan.org>.

=head1 SEE ALSO

L<Function::Parameters::Info>,
L<Moose>,
L<Moo>,
L<Type::Tiny>

=head1 AUTHOR

Lukas Mai, C<< <l.mai at web.de> >>

=head1 COPYRIGHT & LICENSE

Copyright (C) 2010-2014, 2017 Lukas Mai.

This program is free software; you can redistribute it and/or modify it
under the terms of either: the GNU General Public License as published
by the Free Software Foundation; or the Artistic License.

See L<http://dev.perl.org/licenses/> for more information.

=cut
