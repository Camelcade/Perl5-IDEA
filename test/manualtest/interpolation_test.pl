use strict;
use warnings;

my $server;
Carp::croak "Can't authenticate to '@{[$server->host_port]}' with '$method' method";

qq{
eval 'exec $interpreter $arg -S \$0 \${1+"\$\@"}'
    if 0; # not running under some shell
};

'exec $interpreter $arg -S \$0 \${1+"\$\@"}';

our $test;
my $abc = 'abc scalar';
my @abc = ('abc array');
my %abc = (key => 'abc hash');
my $abc_ref = \'scalar_ref';
my $abc_aref = ['array_ref'];

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
say "This is an auto-completion $UNIVERSAL::VERSION test"

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
