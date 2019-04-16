package Foo::Bar;


has mojoattr => 42;

$self->helper(mysuperhelper => sub {
    my ($first, $second, %kwargs) = @_;
});