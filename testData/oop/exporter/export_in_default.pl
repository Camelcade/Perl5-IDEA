package Foo;

given ($a){
    default{
        @EXPORT = qw/this is the end/;
    }
}

package Bar;

@EXPORT = qw/visitor is wrong/;
