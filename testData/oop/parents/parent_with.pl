package Foo;
use parent qw/someparents/;
with qw/someotherparent4 someotherparent5/;

package Bar;

@ISA = qw/walker_is_wrong/;

