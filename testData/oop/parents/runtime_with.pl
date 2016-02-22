package Foo;

with print 42;
with 'someparent1', 'someparent2';

package Bar;

@ISA = qw/walker_is_wrong/;

