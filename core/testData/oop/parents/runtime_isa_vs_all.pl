package Foo;
use base qw/somebase/;
use parent qw/someparents/;
extends 'someparent123';
with print 42;
with 'someotherparent2', 'someotherparent3';
with qw/someotherparent4 someotherparent5/;
extends 'someparent';
our @ISA = qw/someisa/;

package Bar;

@ISA = qw/walker_is_wrong/;

