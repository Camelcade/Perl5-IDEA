package Foo;

sub somesub
{
    my ($somevar) = @_;
    @EXPORT = qw/this is the end/;
}

package Bar;

@EXPORT = qw/visitor is wrong/;
