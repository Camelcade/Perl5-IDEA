#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';
use v5.10;

use constant PI => 3.14;

use constant {
    simple => 1,
    'array' => qw( one two three ),
    "array2" => ('four', 'five', 'six')
};


$a->{test};
say join ', ', (simple);
say join ', ', array;
say join ', ', array2;
