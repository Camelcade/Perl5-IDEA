#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';
use v5.10;

my $filename = 'PerlElementTypesGenerated.java';
my %all_tokens_map = ();
if (open my $if, '<', $filename)
{
    while(my $line = <$if>)
    {
        if ($line =~ /IElementType\s+(\S+?)\s+\=\s+PerlElementTypeFactory.getTokenType/)
        {
            $all_tokens_map{$1} = 1;
        }
    }

    $filename = 'positive_tokens.txt';
    if (open $if, '<', $filename)
    {
        while(my $line = <$if>)
        {
            if ($line =~ /([\S]+?),/)
            {
                delete $all_tokens_map{$1};
            }
        }
    }
    else
    {
        die "Unable to open $filename $!";
    }

}
else
{
    die "Unable to open $filename $!";
}

say join ",\n", sort keys %all_tokens_map;