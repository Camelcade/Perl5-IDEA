#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';
use mro;
use v5.10;
use File::Find;
use File::Path;
use Cwd;
use Data::Dumper;
use JSON::XS;

my $current_root;
my $namespaces = { };

my $result = { };
my $coder = JSON::XS->new()->canonical( 1 )->pretty( 1 );
my $limit = 10;

collect_namespaces();

foreach my $package (sort keys %$namespaces)
{
    next if $package eq 'Canary::Stability';
    say STDERR "Scanning $package";
    my $command = "perl annotations_fetcher.pl $package";
    if (open my $fh, '-|', $command)
    {
        local $/;
        my $json = <$fh>;
        close($fh);
        next unless $json;

        my $decoded = eval {$coder->decode( $json )};
        warn $@ and next if $@;

        foreach my $ns (keys %{$decoded->{ns}})
        {
            if (!exists $result->{$ns} || $ns eq $package)
            {
                $result->{$ns} = $decoded->{ns}->{$ns};
            }
        }
        #        last unless --$limit;

    }
    else
    {
        die 'Error running command $command';
    }
}

my $root = './annotations';
foreach my $ns (keys %$result)
{
    my $data = $result->{$ns};

    next if ( keys %$data == 1 && $data->{mro} && $data->{mro} eq 'dfs');

    my @chunks = split '::', $ns;
    my $filename = pop @chunks;
    $filename .= '.pmea';
    my $dirname = $root.'/'.join '/', @chunks;
    File::Path::make_path($dirname);
    my $filepath = $dirname.'/'.$filename;
    if (open my $of, '>', $filepath)
    {
        printf $of "#\@parent %s\n", join ',', @{$data->{isa}} if ( $data->{isa});
        printf $of "#\@export %s\n", join ',', @{$data->{export}} if ( $data->{export});
        printf $of "#\@export_ok %s\n", join ',', @{$data->{export_ok}} if ( $data->{export_ok});
        printf $of "#\@mro %s\n", $data->{mro};
        printf $of "package %s;\n", $ns;
        close $of;
    }
    else
    {
        die "Unable top open $filepath $!";
    }
}


sub collect_namespaces
{

    foreach my $path (@INC)
    {
        next if $path eq '.';
        say STDERR $path;
        $current_root = Cwd::realpath($path);
        File::Find::find(\&process_file, $path);
    }
}

sub process_file
{
    my $path = $File::Find::name || return;
    return if $path !~ /\.pm$/i;

    $path = Cwd::realpath($path);
    if ($path =~ /^$current_root.(.+)\.pm$/i)
    {
        $path = $1;
        $path =~ s/[\/]/::/g;
        $namespaces->{$path} //= 1;
    }
}
