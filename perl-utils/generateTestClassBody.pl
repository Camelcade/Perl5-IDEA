# would be nice to have such action
use strict;
use warnings;
use v5.10;

my $class_body = shift @ARGV;

for my $file (<*.code>) {
    $file =~ s/^(.)(.+?)\.code/\U$1\E$2/;
    say <<EOM;
    public void test$file(){
        $class_body
    }

EOM

}