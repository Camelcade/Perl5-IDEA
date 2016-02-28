use strict;
use v5.10;

for my $file (sort <*.code>)
{
    $file =~ s/\.code//;
    say <<"EOM";
    public void test$file()
    {
        doTest("$file");
    }
EOM
}