sub somesub {}

method somemethod {}

func somefunc {}

has someattr => (is => 'rw');

__PACKAGE__->mk_accessors(qw/testaccessor/);

use Exception::Class 'SOmething' => {
    alias => 'testalias'
};

use constant testconstant => 42;

has otherattr => qw/
    is rw
    reader testreader
    writer testwriter
    clearer testclearer
    predicate testpredicate
/,
    handles   => [ qw/testhandles/ ];

has [ qw/dupes blabla/ ] => is => 'rw';