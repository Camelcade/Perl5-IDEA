use strict;
use warnings FATAL => 'all';
use v5.10;

# substitutes searchOptions.xml without perl from with perl
# run clean_search_options.pl withPerl.xml withoutPerl.xml

if (@ARGV != 2) {
    say STDERR "Run script as: clean_search_options.pl withPerl.xml withoutPerl.xml";
    exit -1;
}
say STDERR "Substituting $ARGV[1] from $ARGV[0]";

my $default_options = {};
my $perl_options = {};

# reading
read_configurables(shift @ARGV, $perl_options);
read_configurables(shift @ARGV, $default_options);

# substituting
substitute_hashes($perl_options, $default_options);

# printing
print <<'EOM';
<?xml version="1.0" encoding="UTF-8"?>
<options>
EOM

foreach my $configurable (sort keys %$perl_options) {
    my $content = $perl_options->{$configurable};
    if (keys %$content < 1) {
        next;
    }
    say "\t$configurable";

    foreach my $option (sort keys %$content) {
        say "\t\t$option";
    }

    say "\t</configurable>";
}

say '</options>';



sub read_configurables {
    my $file_name = shift;
    my $options = shift;

    open my $fh, '<', $file_name or die "$! $file_name";
    while (my $line = <$fh>) {
        $line =~ s/^\s*//;
        $line =~ s/\s*$//;
        if ($line !~ /<configurable/) {
            say STDERR "Unknown line: $line";
        }
        if ($line =~ /\/>$/) {
            next;
        }
        $options->{$line} = read_options($fh);
    }
    close $fh;
}

sub read_options {
    my $file_handle = shift;
    my $options = {};
    while (my $line = <$file_handle>) {
        $line =~ s/^\s*//;
        $line =~ s/\s*$//;
        if ($line =~ /<\/?configurable/) {
            last;
        }
        elsif ($line =~ /<option/) {
            $options->{$line} = 1;
        }
        else {
            say STDERR "Unknown option line: $line";
        }
    }
    return $options;
}

sub substitute_hashes {
    my $from = shift;
    my $what = shift;

    foreach my $configurable (keys %$what) {
        if (!exists $from->{$configurable}) {
            next;
        }
        delete @{$from->{$configurable}}{keys %{$what->{$configurable}}};
    }
}