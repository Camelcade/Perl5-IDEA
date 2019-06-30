package Foo;

use parent;
use parent qw/superparent superparent2/;

package Bar;

@ISA = qw/walker_is_wrong/;

