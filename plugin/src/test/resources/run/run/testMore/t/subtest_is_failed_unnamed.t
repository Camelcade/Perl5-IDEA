use warnings FATAL => 'all';
use strict;
use Test::More;

subtest 'Wrapping subtest' => sub {
    is("Actual ' \n text", "Expe'cted\ntext");
};
done_testing;