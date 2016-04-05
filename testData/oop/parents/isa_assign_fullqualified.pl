package Foo;

@Foo::ISA = ();
@Foo::ISA = qw/superfoo1 superbar1/;

package Bar;

@ISA = qw/walkeriswrong/;
