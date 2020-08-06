use warnings FATAL => 'all';
use strict;
use Test::More;

subtest 'Wrapping subtest' => sub {
    is(2, 2);
};
done_testing;