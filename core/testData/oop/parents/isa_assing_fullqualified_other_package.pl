package Foo;

@Foo::ISA = ();
@Bar::ISA = qw/superfoo1 superbar1/;

package Bar;

@ISA = qw/walkeriswrong/;
