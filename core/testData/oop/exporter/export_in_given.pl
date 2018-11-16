package Foo;

given ($a)
{
    @EXPORT = qw/this is the end/;
}

package Bar;

@EXPORT = qw/visitor is wrong/;
