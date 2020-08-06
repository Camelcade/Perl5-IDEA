use warnings FATAL => 'all';
use strict;
use Test::More;

subtest 'Wrapping subtest' => sub {
    is(1, 1, 'with name');
};
done_testing;