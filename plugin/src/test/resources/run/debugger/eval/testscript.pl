use strict;
use warnings FATAL => 'all';
use v5.10;

eval <<eval;
    say "evaled  in " . __PACKAGE__;
    sub foobar{
    say 42;
    say 43;
    say 44;
    }
eval

foobar();
foobar();
