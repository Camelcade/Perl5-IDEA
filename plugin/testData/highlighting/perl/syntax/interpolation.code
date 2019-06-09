use strict;
use warnings;

m/([\$*])(([\w\:\']*)\bVERSION)\b.*\=/;

our $test;
my $abc = 'abc scalar';
my @abc = ('abc array');
my %abc = (key => 'abc hash');
my $abc_ref = \'scalar_ref';
my $abc_aref = ['array_ref'];

$abc =~ /test $abc test/;
$abc =~ /test @abc test/;
$abc =~ /test $abc[0] test/;
$abc =~ /test $abc{test} test/;
$abc =~ /test $abc{'test'} test/;
$abc =~ /test $abc{"test"} test/;
$abc =~ /test $abc{`test`} test/;
$abc =~ /te (?# This is a comment) st $abc{$abc} test/;
$abc =~ /test $abc{$abc[$abc]} test/;
$abc =~ /test $abc{$abc{$abc}} test/;

$abc =~ m/test $abc test/;
$abc =~ m/test @abc test/;
$abc =~ m/test $abc[0] test/;
$abc =~ m/test $abc{test} test/;
$abc =~ m/test $abc{'test'} test/;
$abc =~ m/test $abc{"test"} test/;
$abc =~ m/test $abc{`test`} test/;
$abc =~ m/test $abc{$abc} test/;
$abc =~ m/test $abc{$abc[$abc]} test/;
$abc =~ m/test $abc{$abc{$abc}} test/;

$abc =~ qr/test $abc test/;
$abc =~ qr/test @abc test/;
$abc =~ qr/test $abc[0] test/;
$abc =~ qr/test $abc{test} test/;
$abc =~ qr/test $abc{'test'} test/;
$abc =~ qr/test $abc{"test"} test/;
$abc =~ qr/test $abc{`test`} test/;
$abc =~ qr/test $abc{$abc} test/;
$abc =~ qr/test $abc{$abc[$abc]} test/;
$abc =~ qr/test $abc{$abc{$abc}} test/;

$abc =~ s/test $abc test/test $abc test/;
$abc =~ s/test @abc test/test @abc test/;
$abc =~ s/test $abc[0] test/test $abc[0] test/;
$abc =~ s/test $abc{test} test/test $abc{test} test/;
$abc =~ s/test $abc{'test'} test/test $abc{'test'} test/;
$abc =~ s/test $abc{"test"} test/test $abc{"test"} test/;
$abc =~ s/test $abc{`test`} test/test $abc{`test`} test/;
$abc =~ s/test $abc{$abc} test/test $abc{$abc} test/;
$abc =~ s/test $abc{$abc[$abc]} test/test $abc{$abc[$abc]} test/;
$abc =~ s/test $abc{$abc{$abc}} test/test $abc{$abc{$abc}} test/;

$abc =~ s/test $abc test/test $abc/e;
$abc =~ s/test @abc test/test @abc/e;
$abc =~ s/test $abc[0] test/test $abc[0]/e;
$abc =~ s/test $abc{test} test/test $abc{test}/e;
$abc =~ s/test $abc{'test'} test/test $abc{'test'}/e;
$abc =~ s/test $abc{"test"} test/test $abc{"test"}/e;
$abc =~ s/test $abc{`test`} test/test $abc{`test`}/e;
$abc =~ s/test $abc{$abc} test/test $abc{$abc}/e;
$abc =~ s/test $abc{$abc[$abc]} test/test $abc{$abc[$abc]}/e;
$abc =~ s/test $abc{$abc{$abc}} test/test $abc{$abc{$abc}}/e;

$abc =~ s/
               test
               $abc
               test  /
               test
               $abc test
   /x;
$abc =~ s/test @abc test/test @abc test/x;
$abc =~ s/test $abc[0] test/test $abc[0] test/x;
$abc =~ s/test $abc{test} test/test $abc{test} test/x;
$abc =~ s/test $abc{'test'} test/test $abc{'test'} test/x;
$abc =~ s/test $abc{"test"} test/test $abc{"test"} test/x;
$abc =~ s/test $abc{`test`} test/test $abc{`test`} test/x;
$abc =~ s/test $abc{$abc} test/test $abc{$abc} test/x;
$abc =~ s/test $abc{$abc[$abc]} test/test $abc{$abc[$abc]} test/x;
$abc =~ s/test $abc{$abc{$abc}} test/test $abc{$abc{$abc}} test/x;

$abc =~ s{test $abc test}
        {test $abc test};
$abc =~ s{test @abc test}
        {test @abc test};
$abc =~ s{test $abc[0] test}
        {test $abc[0] test};
$abc =~ s{test $abc{test} test}
        {test $abc{test} test};
$abc =~ s{test $abc{'test'} test}
        {test $abc{'test'} test};
$abc =~ s{test $abc{"test"} test}
        {test $abc{"test"} test};
$abc =~ s{test $abc{`test`} test}
        {test $abc{`test`} test};
$abc =~ s{test $abc{$abc} test}
        {test $abc{$abc} test};
$abc =~ s{test $abc{$abc[$abc]} test}
        {test $abc{$abc[$abc]} test};
$abc =~ s{test $abc{$abc{$abc}} test}
        {test $abc{$abc{$abc}} test};

$abc =~ s{test $abc test}
        {print $abc}e;
$abc =~ s{test @abc test}
        {print @abc}e;
$abc =~ s{test $abc[0] test}
        {print $abc[0]}e;
$abc =~ s{test $abc{test} test}
        {print $abc{test}}e;
$abc =~ s{test $abc{'test'} test}
        {print $abc{'test'}}e;
$abc =~ s{test $abc{"test"} test}
        {print $abc{"test"}}e;
$abc =~ s{test $abc{`test`} test}
        {print $abc{`test`}}e;
$abc =~ s{test $abc{$abc} test}
        {print $abc{$abc}}e;
$abc =~ s{test $abc{$abc[$abc]} test}
        {print $abc{$abc[$abc]}}e;
$abc =~ s{test $abc{$abc{$abc}} test}
        {print $abc{$abc{$abc}}}e;

$abc =~ /test $abc test/x;
$abc =~ /test @abc test/x;
$abc =~ /test $abc[0] test/x;
$abc =~ /test $abc{test} test/x;
$abc =~ /test $abc{'test'} test/x;
$abc =~ /test $abc{"test"} test/x;
$abc =~ /test $abc{`test`} test/x;
$abc =~ /test $abc{$abc} test/x;
$abc =~ /test $abc{$abc[$abc]} test/x;
$abc =~ /test $abc{$abc{$abc}} test/x;

$abc =~ m/test $abc test/x;
$abc =~ m/test @abc test/x;
$abc =~ m/test $abc[0] test/x;
$abc =~ m/test $abc{test} test/x;
$abc =~ m/test $abc{'test'} test/x;
$abc =~ m/test $abc{"test"} test/x;
$abc =~ m/test $abc{`test`} test/x;
$abc =~ m/test $abc{$abc} test/x;
$abc =~ m/test $abc{$abc[$abc]} test/x;
$abc =~ m/test $abc{$abc{$abc}} test/x;

$abc =~ qr/test $abc test/x;
$abc =~ qr/test @abc test/x;
$abc =~ qr/test $abc[0] test/x;
$abc =~ qr/test $abc{test} test/x;
$abc =~ qr/test $abc{'test'} test/x;
$abc =~ qr/test $abc{"test"} test/x;
$abc =~ qr/test $abc{`test`} test/x;
$abc =~ qr/test $abc{$abc} test/x;
$abc =~ qr/test $abc{$abc[$abc]} test/x;
$abc =~ qr/test $abc{$abc{$abc}} test/x;

$abc =~ s/test $abc test/test $abc test/x;
$abc =~ s/test @abc test/test @abc test/x;
$abc =~ s/test $abc[0] test/test $abc[0] test/x;
$abc =~ s/test $abc{test} test/test $abc{test} test/x;
$abc =~ s/test $abc{'test'} test/test $abc{'test'} test/x;
$abc =~ s/test $abc{"test"} test/test $abc{"test"} test/x;
$abc =~ s/test $abc{`test`} test/test $abc{`test`} test/x;
$abc =~ s/test $abc{$abc} test/test $abc{$abc} test/x;
$abc =~ s/test $abc{$abc[$abc]} test/test $abc{$abc[$abc]} test/x;
$abc =~ s/test $abc{$abc{$abc}} test/test $abc{$abc{$abc}} test/x;

$abc =~ s{test $abc test}
        {test $abc test}x;
$abc =~ s{test @abc test}
        {test @abc test}x;
$abc =~ s{test $abc[0] test}
        {test $abc[0] test}x;
$abc =~ s{test $abc{test} test}
        {test $abc{test} test}x;
$abc =~ s{test $abc{'test'} test}
        {test $abc{'test'} test}x;
$abc =~ s{test $abc{"test"} test}
        {test $abc{"test"} test}x;
$abc =~ s{test $abc{`test`} test}
        {test $abc{`test`} test}x;
$abc =~ s{test $abc{$abc} test}
        {test $abc{$abc} test}x;
$abc =~ s{
            test $abc{$abc[$abc]} test
        }{
            test $abc{$abc[$abc]} test
        }x;
$abc =~ s{test $abc{$abc{$abc}} test}
        {test $abc{$abc{$abc}} test}x;

$abc = 1 ? 1 : 0;

$abc =~ s{test $abc test}
        {print $abc}ex;
$abc =~ s{test @abc test}
        {print @abc}ex;
$abc =~ s{test $abc[0] test}
        {print $abc[0]}ex;
$abc =~ s{test $abc{test} test}
        {print $abc{test}}ex;
$abc =~ s{test $abc{'test'} test}
        {print $abc{'test'}}ex;
$abc =~ s{test $abc{"test"} test}
        {print $abc{"test"}}ex;
$abc =~ s{test $abc{`test`} test}
        {print $abc{`test`}}ex;
$abc =~ s{test $abc{$abc} test}
        {print $abc{$abc}}ex;
$abc =~ s{test $abc{$abc[$abc]} test}
        {print $abc{$abc[$abc]}}ex;
$abc =~ s{test $abc{$abc{$abc}} test}
        {print $abc{$abc{$abc}}}ex;

say "test $abc test";
say "test @abc test";
say "test %abc test";
say "test *test test";
say "test $#abc test";
say "test $abc[0] test";
say "test $abc{key} test";
say "test $abc{'key'} test";
say "test $abc{'compound key'} test";
say "test $abc{`key`} test";
say "test $abc{`compound key`} test";
say "test $$abc_ref test";
say "test @$abc_aref test";

qw{this is a test $test with };

say qq/test $abc test/;
say qq/test @abc test/;
say qq/test %abc test/;
say qq/test *test test/;
say qq/test $#abc test/;
say qq/test $abc[0] test/;
say qq/test $abc{key} test/;
say qq/test $abc{'key'} test/;
say qq/test $abc{'complex key'} test/;
say qq/"test $abc{"key"} test"/;
say qq/"test $abc{"complex key"} test"/;
say qq/"test $abc{`key`} test"/;
say qq/"test $abc{`complex key`} test"/;
say qq/test $$abc_ref test/;
say qq/test @$abc_aref test/;

say "test ${abc} test";
say "test @{abc} test";
say "test %{abc} test";
say "test *{test} test";
say "test $#{abc} test";
say "test ${abc}[0] test";
say "test ${abc}{key} test";
say "test ${abc}{'key'} test";
say "test ${abc}{'complex key'} test";
say "test ${${abc_ref}} test";
say "test @{${abc_aref}} test";
say "This is an auto-completion $UNIVERSAL::VERSION test";

say 'test $abc test';
say 'test @abc test';
say 'test %abc test';
say 'test *test test';
say 'test $#abc test';
say 'test $abc[0] test';
say 'test $abc{key} test';
say 'test $abc{"key"} test';
say 'test $$abc_ref test';
say 'test @$abc_aref test';

say q/test $abc test/;
say q/test @abc test/;
say q/test %abc test/;
say q/test *test test/;
say q/test $#abc test/;
say q/test $abc[0] test/;
say q/test $abc{key} test/;
say q/test $abc{'key'} test/;
say q/test $$abc_ref test/;
say q/test @$abc_aref test/;

say `test $abc test`;
say `test @abc test`;
say `test %abc test`;
say `test *test test`;
say `test $#abc test`;
say `test $abc[0] test`;
say `test $abc{key} test`;
say `test $abc{'key'} test`;
say `test $abc{'complex key'} test`;
say `test $abc{"key"} test`;
say `test $abc{"complex key"} test`;
say `test $$abc_ref test`;
say `test @$abc_aref test`;

say qx/test $abc test/;
say qx/test @abc test/;
say qx/test %abc test/;
say qx/test *test test/;
say qx/test $#abc test/;
say qx/test $abc[0] test/;
say qx/test $abc{key} test/;
say qx/test $abc{'key'} test/;
say qx/test $abc{'complex key'} test/;
say qx/test $abc{"key"} test/;
say qx/test $abc{"complex key"} test/;
say qx/test $abc{`key`} test/;
say qx/test $abc{`complex key`} test/;
say qx/test $$abc_ref test/;
say qx/test @$abc_aref test/;

qw##;

qw<asdf[<asdf]>asdf>;
qw(asdf[(asdf])asdf);
qw{asdf[a{sdf]a}sdf};
qw[asdf[asdf]asdf];

qw #something
    (
    test
    (test2) t\)est3
    te(st4\ test5
    tes)t6\
    te\(st7
    );

qw#
   test
    (test2) t\)est3
    te(st4\ test5
    tes)t6\
    te\(st7
    #;

qw#testing#;
qr#testing#;
qx#testing#;
qq#'testing#;
q#testing#;
tr#testing#testing#;
m#testing#;

qw#testing#;
qr#testing#;
qx#testing#;
qq#'testing#;
q#testing#;
tr#testing#testing#;
m#testing#;

qw/testing/;
qw'testing testing';
qw{testing testing};

qr'testing';
qr/testing/;
qr{testing};

qx/testing/;
qx'testing';
qx{testing};

qq/testing/;
qq'testing';
qq{testing};

q/testing/;
q'testing';
q{testing};

tr'testing'testing';
tr/testing/testing/;
tr{testing}{testing};

y'testing'testing';
y#testing#testing#;
y/testing/testing/;
y{testing}{testing};

m'testing';
m{testing};
m/testing/;

s'testing'testing';
s#testing#testing#;
s/testing/testing/;
s{testing}{testing};



