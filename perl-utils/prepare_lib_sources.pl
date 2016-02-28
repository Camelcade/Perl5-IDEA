use strict;
use v5.10;
use File::Copy;

my $dstdir = shift @ARGV;

my $srcdir;

for my $inc (@INC)
{
    next if $inc eq '.';
    $srcdir = $inc;
    $srcdir =~ s{/+$}{}g;
    $dstdir =~ s{/+$}{}g;
    say STDERR "Converting $srcdir => $dstdir";

    process($srcdir);
}

sub process{
    my $src = shift;

    for my $file (<$src/*>){
        process($file) if -d $file;
        if( $file =~ /\.(pm|pl|t)$/ )
        {
            my $newfile = $file;
            $newfile =~ s{$srcdir/}{};
            $newfile =~ s{[/.-]}{_}g;
            $newfile .= '.code';

            say "$file => $newfile";
            copy $file, "$dstdir/$newfile" or warn $!;
        }
    }

}