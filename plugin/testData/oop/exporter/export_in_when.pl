package Foo;

given ($a)
{
    when(1){
        @EXPORT = qw/this is the end/;
    }
}

package Bar;

@EXPORT = qw/visitor is wrong/;
