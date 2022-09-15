package Foo;
use base qw/somebase/;
extends 'someparent123';
with print 42;
with 'someotherparent2', 'someotherparent3';
extends 'someparent';
our @ISA = qw/someisa/;
use parent qw/someparents/;
with qw/someotherparent4 someotherparent5/;

package Bar;

@ISA = qw/walker_is_wrong/;

