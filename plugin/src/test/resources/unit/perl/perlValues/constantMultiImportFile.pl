package Foo::Bar;
our @EXPORT = qw/
    MYCONST OHTER
/;

sub blabla {
    69;
}

use constant {
    MYCONST => 'Foo::Bar',
    OHTER   => 42
};
