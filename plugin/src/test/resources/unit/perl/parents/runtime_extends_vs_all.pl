package Foo;
use base qw/somebase/;
use parent qw/someparents/;
our @ISA = qw/someisa/;
extends 'someparent123';
with print 42;
with 'someotherparent2', 'someotherparent3';
with qw/someotherparent4 someotherparent5/;
extends 'someparent';

package Bar;

@ISA = qw/walker_is_wrong/;

