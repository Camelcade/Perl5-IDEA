use warnings FATAL => 'all';
use strict;
use Test::More;

pass "Initial";

SKIP:{
    skip "Testing skip", 2;
    fail("Fail");
    pass("Pass");
}

pass "Last one";

done_testing;