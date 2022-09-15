use warnings FATAL => 'all';
use strict;
use Test::More;

pass "Initial";

TODO:{
    local $TODO = "Here is TODO reason";
    fail();
    pass();
}

pass "Last one";

done_testing;