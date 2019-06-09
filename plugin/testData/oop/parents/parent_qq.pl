package Foo;

use parent;
use parent "superparent::duperparent";

package Bar;

@ISA = qw/walker_is_wrong/;

