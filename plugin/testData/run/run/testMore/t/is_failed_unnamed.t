use warnings FATAL => 'all';
use strict;
use Test::More;

is("Actual ' \n text", "Expe'cted\ntext");
done_testing;