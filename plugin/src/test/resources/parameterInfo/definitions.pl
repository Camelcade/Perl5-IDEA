package Foo::Bar;

sub list_and_shifts {
    my ($var, undef, $othervar) = (@_, shift, shift);
}

use constant SINGLE_CONST => 42;

use constant {
    MULTICONST1 => 42,
    MULTICONST2 => 69
};

method mymethod_with_invocant($class: $arg, $another) {
}
method mymethod($method_arg, $method_another) {
}
