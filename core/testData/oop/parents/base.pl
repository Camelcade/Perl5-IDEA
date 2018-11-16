package Foo;

use base;
use base qw/superbase superbase2/;

package Bar;

use base qw/walker_is_wrong/;
