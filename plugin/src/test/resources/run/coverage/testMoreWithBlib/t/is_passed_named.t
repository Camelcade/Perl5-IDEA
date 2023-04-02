use warnings FATAL => 'all';
use strict;
use Test::More;

use_ok('MyPackage');

require MyPackage;

MyPackage::somesub();

done_testing;