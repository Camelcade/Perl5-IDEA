use strict;
use warnings;
use v5.10;

for my $file (<*.code>, <*.txt>) {
    my $newfile = $file;
    $newfile =~ s/^_//;
    #	$newfile = "_$file";
    say "$file => $newfile";
    rename $file, $newfile;
}

system 'git add *.txt';
system 'git add *.code';