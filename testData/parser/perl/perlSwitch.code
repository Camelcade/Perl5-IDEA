#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

use Switch;
switch ($val) {
    case 1        { print "number 1" }
    case "a"    { print "string a" }
    case [ 1 .. 10, 42 ]    { print "number in list" }
    case (@array)    { print "number in list" }
    case /\w+/    { print "pattern" }
    case qr/\w+/    { print "pattern" }
    case (%hash)    { print "entry in hash" }
    case (\%hash)    { print "entry in hash" }
    case (\&sub)    { print "arg to subroutine" }
    else { print "previous case not true" }
}

use Switch;
# AND LATER...
%special = ( woohoo => 1, d'oh => 1 );
while (<>) {
    switch ($_) {
        case (%special) { print "homer\n"; }      # if $special{$_}
        case m/a-z/i     { print "alpha\n"; }      # if $_ =~ /a-z/i
        case [ 1 .. 9 ]     { print "small num\n"; }  # if $_ in [1..9]
        case { $_[0] >= 10 } {
            # if $_ >= 10
            my $age = <>;
            switch (sub { $_[0] < $age } ) {
                case 20  { print "teens\n"; }     # if 20 < $age
                case 30  { print "twenties\n"; }  # if 30 < $age
                else { print "history\n"; }
            }
        }
        #                print "must be punctuation\n" case m/\W / ;  # if $_ ~= /\W/ # see #822
    }
}

sub classify_digit
{
    switch ($_[0]) {
        case 0            { return 'zero' }
        case [ 2, 4, 6, 8 ]    { return 'even' }
        case [ 1, 3, 4, 7, 9 ]  { return 'odd' }
        case /[A-F]/i     { return 'hex' }
    }
}

switch ($val) {
    case 1      {
        handle_num_1();
        next
    }    # and try next case...
    case "1"    {
        handle_str_1();
        next
    }    # and try next case...
    case [ 0 .. 9 ] { handle_num_any(); }       # and we're done
    case /\d/   {
        handle_dig_any();
        next
    }  # and try next case...
    case m/.*/   {
        handle_str_any();
        next
    }  # and try next case...
}

switch ($val) {
    case [ 0 .. 9 ] {
        handle_num_any();
        next if $val < 7;
    }
    case /\d/   { handle_dig_any(); }
}

switch ($val) {
    case [ 0 .. 9 ] {
        handle_num_any();
        last if $val >= 7;
        next;
    }
    case /\d/   { handle_dig_any(); }
}

use Switch 'fallthrough';
switch ($val) {
    case 1      { handle_num_1(); }
    case "1"    { handle_str_1(); }
    case [ 0 .. 9 ] {
        handle_num_any();
        last
    }
    case /\d/   { handle_dig_any(); }
    case m/.*/   { handle_str_any(); }
}

