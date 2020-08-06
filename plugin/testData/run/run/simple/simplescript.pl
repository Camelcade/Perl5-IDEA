use strict;
use warnings FATAL => 'all';
use v5.10;

sub othersub {
    say 'This is an oter sub'
}

sub somesub {
    say 'This is a sub';
    othersub();
}

say 'this is main body';
somesub;


