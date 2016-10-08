use strict;
use v5.10;

my $files = 0;
my @dirs = glob '*';
for my $dir (@dirs){
    next unless -d $dir;
    say "Entering $dir";
    chdir $dir;
	for my $filename (glob '*')
	{
		if( $filename =~ /_/ )
		{
			my @chunks = split '_', $filename;
			s/^(.)/\U\1/ for @chunks[1..$#chunks];
			my $targetname = join '', @chunks;
			say "\tRenaming $filename  => $targetname";
			system "git mv $filename $targetname";
			$files++;
		}
	}
	chdir '..';
}
say "$files files total";