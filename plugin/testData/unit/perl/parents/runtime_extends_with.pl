package Foo;

extends 'someparent';
with print 42;
with 'someotherparent2', 'someotherparent3';
with qw/someotherparent4 someotherparent5/;

package Bar;

@ISA = qw/walker_is_wrong/;

