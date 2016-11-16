package MyTest::Some::Package;

our @EXPORT  = qw/
	somesub1
	someconst1
	multiconst1
/;

our @EXPORT_OK = qw/
	somesub2
	SOMECONST2
	MULTICONST2
/;


sub somesub1{}
sub somesub2{}
use constant someconst1 => 1;
use constant SOMECONST2 => 1;
use constant {
	multiconst1 => 1,
	MULTICONST2 => 2,
};