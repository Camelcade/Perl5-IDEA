package MyTest::Constants;

use constant LIBRARY_CONST1 => 1;
use constant {
    'LIBRARY_CONST2' => 1,
    qw/LIBRARY_CONST3 42 LIBRARY_CONST4 42/
};

1;