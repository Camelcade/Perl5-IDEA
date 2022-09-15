use utf8;
use strict;
use warnings FATAL => 'all';

sub something {
    my $scalar = 1;
    my @array = (1, 2, 3);
    my %hash = (4, 5, 6, 7);
    my $string = "приветик";
    my $encoded_string = "$string";
    utf8::upgrade($encoded_string);
    my $scalar_ref = \$string;
    bless $scalar_ref, 'Foo::Bar';
    my $array_ref = \@array;
    my $hash_ref = \%hash;
    use Tie::Scalar;
    my $tied_scalar = "";
    tie $tied_scalar, 'Tie::StdScalar';
    my $stdout_ref = \*STDOUT;
    my $stderr_ref = \*STDERR;
    my $stdin_ref = \*STDIN;
    my $regexp = qr/super\d+uper/;
    my $code = sub {print 'hi'};
    format testformat =
test
.
    my $format = *testformat{FORMAT};
    print 'done';
}

BEGIN{
    print 'begin block';
}

print 42;
something();
something();
something();
