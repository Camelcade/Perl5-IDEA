#!/usr/bin/perl

########################################################
# Please file all bug reports, patches, and feature
# requests under:
#      https://sourceforge.net/p/logwatch/_list/tickets
# and copy
#    Mike "MrC" Cappella <mike (at) cappella (dot) us>
# Help requests and discusion can be filed under:
#      https://sourceforge.net/p/logwatch/discussion/
########################################################


##########################################################################
# Amavis-logwatch: written and maintained by:
#
#    Mike "MrC" Cappella <mike (at) cappella (dot) us>
#      http://logreporters.sourceforge.net/
#
# Please send all comments, suggestions, bug reports regarding this
# program/module to the email address above.  I will respond as quickly
# as possible. [MrC]
#######################################################
### All work since Dec 12, 2006 (logwatch CVS revision 1.28)
### Copyright (c) 2006-2012  Mike Cappella
###
### Covered under the included MIT/X-Consortium License:
###    http://www.opensource.org/licenses/mit-license.php
### All modifications and contributions by other persons to
### this script are assumed to have been donated to the
### Logwatch project and thus assume the above copyright
### and licensing terms.  If you want to make contributions
### under your own copyright or a different license this
### must be explicitly stated in the contribution an the
### Logwatch project reserves the right to not accept such
### contributions.  If you have made significant
### contributions to this script and want to claim
### copyright please contact logwatch-devel@lists.sourceforge.net.
##########################################################

##########################################################################
# The original amavis logwatch filter was written by
# Jim O'Halloran <jim @ kendle.com.au>, and has had many contributors over
# the years.
#
# CVS log removed: see Changes file for amavis-logwatch at
#    http://logreporters.sourceforge.net/
# or included with the standalone amavis-logwatch distribution
##########################################################################

package Logreporters;
use 5.008;
use strict;
use warnings;
no warnings "uninitialized";
use re 'taint';

our $Version = '1.51.03';
our $progname_prefix = 'amavis';

# Specifies the default configuration file for use in standalone mode.
my $config_file = "/usr/local/etc/${progname_prefix}-logwatch.conf";

#MODULE: ../Logreporters/Utils.pm
package Logreporters::Utils;

use 5.008;
use strict;
use re 'taint';
use warnings;

BEGIN {
    use Exporter ();
    use vars qw(@ISA @EXPORT @EXPORT_OK %EXPORT_TAGS $VERSION);
    $VERSION = '1.003';
    @ISA = qw(Exporter);
    @EXPORT = qw(&formathost &get_percentiles &get_percentiles2 &get_frequencies &commify &unitize
        &get_usable_sectvars &add_section &begin_section_group &end_section_group
        &get_version &unique_list);
    @EXPORT_OK = qw(&gen_test_log);
}

use subs qw(@EXPORT @EXPORT_OK);


# Formats IP and hostname for even column spacing
#
sub formathost($ $) {
    # $_[0] : hostip
    # $_[1] : hostname;

    if (!$Logreporters::Config::Opts{'unknown'} and $_[1] eq 'unknown') {
        return $_[0];
    }

    return sprintf "%-$Logreporters::Config::Opts{'ipaddr_width'}s  %s",
        $_[0] eq '' ? '*unknown' : $_[0],
        $_[1] eq '' ? '*unknown' : lc $_[1];
}

# Add a new section to the end of a section table
#
sub add_section($$$$$;$) {
    my $sref = shift;
    die "Improperly specified Section entry: $_[0]" if !defined $_[3];

    my $entry = {
        CLASS  => 'DATA',
        NAME   => $_[0],
        DETAIL => $_[1],
        FMT    => $_[2],
        TITLE  => $_[3],
    };
    $entry->{'DIVISOR'} = $_[4] if defined $_[4];
    push @$sref, $entry;
}

{
    my $group_level = 0;

    # Begin a new section group.  Groups can nest.
    #
    sub begin_section_group($;@) {
        my $sref = shift;
        my $group_name = shift;
        my $entry = {
            CLASS   => 'GROUP_BEGIN',
            NAME    => $group_name,
            LEVEL   => ++$group_level,
            HEADERS => [ @_ ],
        };
        push @$sref, $entry;
    }

    # Ends a section group.
    #
    sub end_section_group($;@) {
        my $sref = shift;
        my $group_name = shift;
        my $entry = {
            CLASS   => 'GROUP_END',
            NAME    => $group_name,
            LEVEL   => --$group_level,
            FOOTERS => [ @_ ],
        };
        push @$sref, $entry;
    }
}

# Generate and return a list of section table entries or
# limiter key names, skipping any formatting entries.
# If 'namesonly' is set, limiter key names are returned,
# otherwise an array of section array records is returned.
sub get_usable_sectvars(\@ $) {
    my ($sectref, $namesonly) = @_;
    my (@sect_list, %unique_names);

    foreach my $sref (@$sectref) {
        #print "get_usable_sectvars: $sref->{NAME}\n";
        next unless $sref->{CLASS} eq 'DATA';
        if ($namesonly) {
            $unique_names{$sref->{NAME}} = 1;
        }
        else {
            push @sect_list, $sref;
        }
    }
    # return list of unique names
    if ($namesonly) {
        return keys %unique_names;
    }
    return @sect_list;
}

# Print program and version info, preceeded by an optional string, and exit.
#
sub get_version() {

    print STDOUT "@_\n" if ($_[0]);
    print STDOUT "$Logreporters::progname: $Logreporters::Version\n";
    exit 0;
}


# Returns a list of percentile values given a
# sorted array of numeric values.  Uses the formula:
#
# r = 1 + (p(n-1)/100) = i + d  (Excel method)
#
# r = rank
# p = desired percentile
# n = number of items
# i = integer part
# d = decimal part
#
# Arg1 is an array ref to the sorted series
# Arg2 is a list of percentiles to use

sub get_percentiles(\@ @) {
    my ($aref, @plist) = @_;
    my ($n, $last, $r, $d, $i, @vals, $Yp);

    $last = $#$aref;
    $n = $last + 1;
    #printf "%6d" x $n . "\n", @{$aref};

    #printf "n: %4d, last: %d\n", $n, $last;
    foreach my $p (@plist) {
        $r = 1 + ($p * ($n - 1) / 100.0);
        $i = int($r); # integer part
        # domain: $i = 1 .. n
        if ($i == $n) {
            $Yp = $aref->[$last];
        }
        elsif ($i == 0) {
            $Yp = $aref->[0];
            print "CAN'T HAPPEN: $Yp\n";
        }
        else {
            $d = $r - $i; # decimal part
            #p = Y[i] + d(Y[i+1] - Y[i]), but since we're 0 based, use i=i-1
            $Yp = $aref->[$i - 1] + ($d * ($aref->[$i] - $aref->[$i - 1]));
        }
        #printf "\np(%6.2f), r: %6.2f, i: %6d, d: %6.2f, Yp: %6d", $p, $r, $i, $d, $Yp;
        push @vals, $Yp;
    }

    return @vals;
}

sub get_num_scores($) {
    my $scoretab_r = shift;

    my $totalscores = 0;

    for (my $i = 0; $i < @$scoretab_r; $i += 2) {
        $totalscores += $scoretab_r->[$i + 1]
    }

    return $totalscores;
}

# scoretab
#
#  (score1, n1), (score2, n2), ... (scoreN, nN)
#     $i   $i+1
#
# scores are 0 based (0 = 1st score)
sub get_nth_score($ $) {
    my ($scoretab_r, $n) = @_;

    my $i = 0;
    my $n_cur_scores = 0;
    #print "Byscore (", .5 * @$scoretab_r, "): "; for (my $i = 0; $i < $#$scoretab_r / 2; $i++) { printf "%9s (%d) ", $scoretab_r->[$i], $scoretab_r->[$i+1]; } ; print "\n";

    while ($i < $#$scoretab_r) {
        #print "Samples_seen: $n_cur_scores\n";
        $n_cur_scores += $scoretab_r->[$i + 1];
        if ($n_cur_scores >= $n) {
            #printf "range: %s  %s  %s\n", $i >= 2 ? $scoretab_r->[$i - 2] : '<begin>', $scoretab_r->[$i], $i+2 > $#$scoretab_r ? '<end>' : $scoretab_r->[$i + 2];
            #printf "n: $n, i: %8d, n_cur_scores: %8d, score: %d x %d hits\n", $i, $n_cur_scores, $scoretab_r->[$i], $scoretab_r->[$i+1];
            return $scoretab_r->[$i];
        }

        $i += 2;
    }
    print "returning last score $scoretab_r->[$i]\n";
    return $scoretab_r->[$i];
}

sub get_percentiles2(\@ @) {
    my ($scoretab_r, @plist) = @_;
    my ($n, $last, $r, $d, $i, @vals, $Yp);

    #$last = $#$scoretab_r - 1;
    $n = get_num_scores($scoretab_r);
    #printf "\n%6d" x $n . "\n", @{$scoretab_r};

    #printf "\n\tn: %4d, @$scoretab_r\n", $n;
    foreach my $p (@plist) {
        ###print "\nPERCENTILE: $p\n";
        $r = 1 + ($p * ($n - 1) / 100.0);
        $i = int($r); # integer part
        if ($i == $n) {
            #print "last:\n";
            #$Yp = $scoretab_r->[$last];
            $Yp = get_nth_score($scoretab_r, $n);
        }
        elsif ($i == 0) {
            #$Yp = $scoretab_r->[0];
            print "1st: CAN'T HAPPEN\n";
            $Yp = get_nth_score($scoretab_r, 1);
        }
        else {
            $d = $r - $i; # decimal part
            #p = Y[i] + d(Y[i+1] - Y[i]), but since we're 0 based, use i=i-1
            my $ithvalprev = get_nth_score($scoretab_r, $i);
            my $ithval = get_nth_score($scoretab_r, $i + 1);
            $Yp = $ithvalprev + ($d * ($ithval - $ithvalprev));
        }
        #printf "p(%6.2f), r: %6.2f, i: %6d, d: %6.2f, Yp: %6d\n", $p, $r, $i, $d, $Yp;
        push @vals, $Yp;
    }

    return @vals;
}


# Returns a list of frequency distributions given an incrementally sorted
# set of sorted scores, and an incrementally sorted list of buckets
#
# Arg1 is an array ref to the sorted series
# Arg2 is a list of frequency buckets to use
sub get_frequencies(\@ @) {
    my ($aref, @blist) = @_;

    my @vals = (0) x (@blist);
    my @sorted_blist = sort {$a <=> $b} @blist;
    my $bucket_index = 0;

    OUTER:
    foreach my $score (@$aref) {
        #print "Score: $score\n";
        for my $i ($bucket_index .. @sorted_blist - 1) {
            #print "\tTrying Bucket[$i]: $sorted_blist[$i]\n";
            if ($score > $sorted_blist[$i]) {
                $bucket_index++;
            }
            else {
                #printf "\t\tinto Bucket[%d]\n", $bucket_index;
                $vals[$bucket_index]++;
                next OUTER;
            }
        }
        #printf "\t\tinto Bucket[%d]\n", $bucket_index - 1;
        $vals[$bucket_index - 1]++;
    }

    return @vals;
}

# Inserts commas in numbers for easier readability
#
sub commify($) {
    return undef if !defined($_[0]);

    my $text = reverse $_[0];
    $text =~ s/(\d\d\d)(?=\d)(?!\d*\.)/$1,/g;
    return scalar reverse $text;
}

# Unitize a number, and return appropriate printf formatting string
#
sub unitize($ $) {
    my ($num, $fmt) = @_;
    my $kilobyte = 2 ** 10;
    my $megabyte = 2 ** 20;
    my $gigabyte = 2 ** 30;
    my $terabyte = 2 ** 40;

    if ($num >= $terabyte) {
        $num /= $terabyte;
        $fmt .= '.3fT';
    }
    elsif ($num >= $gigabyte) {
        $num /= $gigabyte;
        $fmt .= '.3fG';
    }
    elsif ($num >= $megabyte) {
        $num /= $megabyte;
        $fmt .= '.3fM';
    }
    elsif ($num >= $kilobyte) {
        $num /= $kilobyte;
        $fmt .= '.3fK';
    }
    else {
        $fmt .= 'd ';
    }

    return ($num, $fmt);
}

# Returns a sublist of the supplied list of elements in an unchanged order,
# where only the first occurrence of each defined element is retained
# and duplicates removed
#
# Borrowed from amavis 2.6.2
#
sub unique_list(@) {
    my ($r) = @_ == 1 && ref($_[0]) ? $_[0] : \@_; # accept list, or a list ref
    my (%seen);
    my (@unique) = grep {defined($_) && !$seen{$_}++} @$r;

    return @unique;
}

# Generate a test maillog file from the '#TD' test data lines
# The test data file is placed in /var/tmp/maillog.autogen
#
# arg1: "postfix" or "amavis"
# arg2: path to postfix-logwatch or amavis-logwatch from which to read '#TD' data
#
# Postfix TD syntax:
#    TD<service><QID>(<count>) log entry
#
sub gen_test_log($) {
    my $scriptpath = shift;

    my $toolname = $Logreporters::progname_prefix;
    my $datafile = "/var/tmp/maillog-${toolname}.autogen";

    die "gen_test_log: invalid toolname $toolname" if ($toolname !~ /^(postfix|amavis)$/);

    eval {
        require Sys::Hostname;
        require Fcntl;
    } or die "Unable to create test data file: required module(s) not found\n$@";

    my $syslogtime = localtime;
    $syslogtime =~ s/^....(.*) \d{4}$/$1/;

    my ($hostname) = split /\./, Sys::Hostname::hostname();

    # # avoid -T issues
    # delete @ENV{'IFS', 'CDPATH', 'ENV', 'BASH_ENV'};

    my $flags = &Fcntl::O_CREAT | &Fcntl::O_WRONLY | &Fcntl::O_TRUNC;
    sysopen(FH, $datafile, $flags) or die "Can't create test data file: $!";
    print "Generating test log data file from $scriptpath: $datafile\n";

    my $id;
    @ARGV = ($scriptpath);
    if ($toolname eq 'postfix') {
        my %services = (
            DEF => 'smtpd',
            bQ  => 'bounce',
            cN  => 'cleanup',
            cQ  => 'cleanup',
            lQ  => 'local',
            m   => 'master',
            p   => 'pickup',
            pQ  => 'pickup',
            ppQ => 'pipe',
            pfw => 'postfwd',
            pg  => 'postgrey',
            pgQ => 'postgrey',
            ps  => 'postsuper',
            qQ  => 'qmgr',
            s   => 'smtp',
            sQ  => 'smtp',
            sd  => 'smtpd',
            sdN => 'smtpd',
            sdQ => 'smtpd',
            spf => 'policy-spf',
            vN  => 'virtual',
            vQ  => 'virtual',
        );
        $id = 'postfix/smtp[12345]';

        while (<>) {
            if (/^\s*#TD([a-zA-Z]*[NQ]?)(\d+)?(?:\(([^)]+)\))? (.*)$/) {
                my ($service, $count, $qid, $line) = ($1, $2, $3, $4);

                #print "SERVICE: %s, QID: %s, COUNT: %s, line: %s\n", $service, $qid, $count, $line;

                if ($service eq '') {
                    $service = 'DEF';
                }
                die("No such service: \"$service\": line \"$_\"") if (!exists $services{$service});

                $id = $services{$service} . '[123]';
                $id = 'postfix/' . $id unless $services{$service} eq 'postgrey';
                #print "searching for service: \"$service\"\n\tFound $id\n";
                if ($service =~ /N$/) {$id .= ': NOQUEUE';}
                elsif ($service =~ /Q$/) {$id .= $qid ? $qid : ': DEADBEEF';}

                $line =~ s/ +/ /g;
                $line =~ s/^ //g;
                #print "$syslogtime $hostname $id: \"$line\"\n" x ($count ? $count : 1);
                print FH "$syslogtime $hostname $id: $line\n" x ($count ? $count : 1);
            }
        }
    }
    else {
        #amavis
        my %services = (
            DEF => 'amavis',
            dcc => 'dccproc',
        );
        while (<>) {
            if (/^\s*#TD([a-z]*)(\d+)? (.*)$/) {
                my ($service, $count, $line) = ($1, $2, $3);
                if ($service eq '') {
                    $service = 'DEF';
                }
                die("No such service: \"$service\": line \"$_\"") if (!exists $services{$service});
                $id = $services{$service} . '[123]:';
                if ($services{$service} eq 'amavis') {
                    $id .= ' (9999-99)';
                }
                print FH "$syslogtime $hostname $id $line\n" x ($count ? $count : 1)
            }
        }
    }

    close FH or die "Can't close $datafile: $!";
}

1;

#MODULE: ../Logreporters/Config.pm
package Logreporters::Config;

use 5.008;
use strict;
use re 'taint';
use warnings;

BEGIN {
    use Exporter ();
    use vars qw(@ISA @EXPORT @EXPORT_OK %EXPORT_TAGS $VERSION);
    $VERSION = '1.002';
    @ISA = qw(Exporter);
    @EXPORT = qw(&init_run_mode &add_option &get_options &init_cmdline &get_vars_from_file
        &process_limiters &process_debug_opts &init_getopts_table_common &zero_opts
        @Optspec %Opts %Configvars @Limiters %line_styles $fw1 $fw2 $sep1 $sep2
        &D_CONFIG &D_ARGS &D_VARS &D_TREE &D_SECT &D_UNMATCHED &D_TEST &D_ALL
    );
}

use subs @EXPORT;

our @Optspec = (); # options table used by Getopts

our %Opts = ();       # program-wide options
our %Configvars = (); # configuration file variables
our @Limiters;

# Report separator characters and widths
our ($fw1, $fw2) = (22, 10);
our ($sep1, $sep2) = ('=', '-');

use Getopt::Long;

BEGIN {
    import Logreporters::Utils qw(&get_usable_sectvars);
}

our %line_styles = (
    truncate => 0,
    wrap     => 1,
    full     => 2,
);

sub init_run_mode($);
sub confighash_to_cmdline(\%);
sub get_vars_from_file(\% $);
sub process_limiters(\@);
sub add_option(@);
sub get_options($);
sub init_getopts_table_common(@);
sub set_supplemental_reports($$);
# debug constants
sub D_CONFIG() {1 << 0}
sub D_ARGS() {1 << 1}
sub D_VARS() {1 << 2}
sub D_TREE() {1 << 3}
sub D_SECT() {1 << 4}
sub D_UNMATCHED() {1 << 5}

sub D_TEST() {1 << 30}
sub D_ALL() {1 << 31}

my %debug_words = (
    config    => D_CONFIG,
    args      => D_ARGS,
    vars      => D_VARS,
    tree      => D_TREE,
    sect      => D_SECT,
    unmatched => D_UNMATCHED,

    test      => D_TEST,
    all       => 0xffffffff,
);

# Clears %Opts hash and initializes basic running mode options in
# %Opts hash by setting keys: 'standalone', 'detail', and 'debug'.
# Call early.
#
sub init_run_mode($) {
    my $config_file = shift;
    $Opts{'debug'} = 0;

    # Logwatch passes a filter's options via environment variables.
    # When running standalone (w/out logwatch), use command line options
    $Opts{'standalone'} = exists($ENV{LOGWATCH_DETAIL_LEVEL}) ? 0 : 1;

    # Show summary section by default
    $Opts{'summary'} = 1;

    if ($Opts{'standalone'}) {
        process_debug_opts($ENV{'LOGREPORTERS_DEBUG'}) if exists($ENV{'LOGREPORTERS_DEBUG'});
    }
    else {
        $Opts{'detail'} = $ENV{'LOGWATCH_DETAIL_LEVEL'};
        # XXX
        #process_debug_opts($ENV{'LOGWATCH_DEBUG'}) if exists ($ENV{'LOGWATCH_DEBUG'});
    }

    # first process --debug, --help, and --version options
    add_option('debug=s', sub {
        process_debug_opts($_[1]);
        1
    });
    add_option('version', sub {
        &Logreporters::Utils::get_version();
        1;
    });
    get_options(1);

    # now process --config_file, so that all config file vars are read first
    add_option('config_file|f=s', sub {
        get_vars_from_file(%Configvars, $_[1]);
        1;
    });
    get_options(1);

    # if no config file vars were read
    if ($Opts{'standalone'} and !keys(%Configvars) and -f $config_file) {
        print "Using default config file: $config_file\n" if $Opts{'debug'} & D_CONFIG;
        get_vars_from_file(%Configvars, $config_file);
    }
}

sub get_options($) {
    my $pass_through = shift;
    #$SIG{__WARN__} = sub { print "*** $_[0]*** options error\n" };
    # ensure we're called after %Opts is initialized
    die "get_options: program error: %Opts is empty" unless exists $Opts{'debug'};

    my $p = new Getopt::Long::Parser;

    if ($pass_through) {
        $p->configure(qw(pass_through permute));
    }
    else {
        $p->configure(qw(no_pass_through no_permute));
    }
    #$p->configure(qw(debug));

    if ($Opts{'debug'} & D_ARGS) {
        print "\nget_options($pass_through): enter\n";
        printf "\tARGV(%d): ", scalar @ARGV;
        print @ARGV, "\n";
        print "\t$_ ", defined $Opts{$_} ? "=> $Opts{$_}\n" : "\n" foreach sort keys %Opts;
    }

    if ($p->getoptions(\%Opts, @Optspec) == 0) {
        print STDERR "Use ${Logreporters::progname} --help for options\n";
        exit 1;
    }
    if ($Opts{'debug'} & D_ARGS) {
        print "\t$_ ", defined $Opts{$_} ? "=> $Opts{$_}\n" : "\n" foreach sort keys %Opts;
        printf "\tARGV(%d): ", scalar @ARGV;
        print @ARGV, "\n";
        print "get_options: exit\n";
    }
}

sub add_option(@) {
    push @Optspec, @_;
}

# untaint string, borrowed from amavisd-new
sub untaint($) {
    no re 'taint';

    my ($str);
    if (defined($_[0])) {
        local ($1); # avoid Perl taint bug: tainted global $1 propagates taintedness
        $str = $1 if $_[0] =~ /^(.*)$/;
    }

    return $str;
}

sub init_getopts_table_common(@) {
    my @supplemental_reports = @_;

    print "init_getopts_table_common: enter\n" if $Opts{'debug'} & D_ARGS;

    add_option('help', sub {
        print STDOUT Logreporters::usage(undef);
        exit 0
    });
    add_option('gen_test_log=s', sub {
        Logreporters::Utils::gen_test_log($_[1]);
        exit 0;
    });
    add_option('detail=i');
    add_option('nodetail', sub {
        # __none__ will set all limiters to 0 in process_limiters
        # since they are not known (Sections table is not yet built).
        push @Limiters, '__none__';
        # 0 = disable supplemental_reports
        set_supplemental_reports(0, \@supplemental_reports);
    });
    add_option('max_report_width=i');
    add_option('summary!');
    add_option('show_summary=i', sub {
        $Opts{'summary'} = $_[1];
        1;
    });
    # untaint ipaddr_width for use w/sprintf() in Perl v5.10
    add_option('ipaddr_width=i', sub {
        $Opts{'ipaddr_width'} = untaint($_[1]);
        1;
    });

    add_option('sect_vars!');
    add_option('show_sect_vars=i', sub {
        $Opts{'sect_vars'} = $_[1];
        1;
    });

    add_option('syslog_name=s');
    add_option('wrap', sub {
        $Opts{'line_style'} = $line_styles{$_[0]};
        1;
    });
    add_option('full', sub {
        $Opts{'line_style'} = $line_styles{$_[0]};
        1;
    });
    add_option('truncate', sub {
        $Opts{'line_style'} = $line_styles{$_[0]};
        1;
    });
    add_option('line_style=s', sub {
        my $style = lc($_[1]);
        my @list = grep(/^$style/, keys %line_styles);
        if (!@list) {
            print STDERR "Invalid line_style argument \"$_[1]\"\n";
            print STDERR "Option line_style argument must be one of \"wrap\", \"full\", or \"truncate\".\n";
            print STDERR "Use $Logreporters::progname --help for options\n";
            exit 1;
        }
        $Opts{'line_style'} = $line_styles{lc($list[0])};
        1;
    });

    add_option('limit|l=s', sub {
        my ($limiter, $lspec) = split(/=/, $_[1]);
        if (!defined $lspec) {
            printf STDERR "Limiter \"%s\" requires value (ex. --limit %s=10)\n", $_[1], $_[1];
            exit 2;
        }
        foreach my $val (split(/(?:\s+|\s*,\s*)/, $lspec)) {
            if ($val !~ /^\d+$/ and
                $val !~ /^(\d*)\.(\d+)$/ and
                $val !~ /^::(\d+)$/ and
                $val !~ /^:(\d+):(\d+)?$/ and
                $val !~ /^(\d+):(\d+)?:(\d+)?$/) {
                printf STDERR "Limiter value \"$val\" invalid in \"$limiter=$lspec\"\n";
                exit 2;
            }
        }
        push @Limiters, lc $_[1];
    });

    print "init_getopts_table_common: exit\n" if $Opts{'debug'} & D_ARGS;
}

sub get_option_names() {
    my (@ret, @tmp);
    foreach (@Optspec) {
        if (ref($_) eq '') {
            # process only the option names
            my $spec = $_;
            $spec =~ s/=.*$//;
            $spec =~ s/([^|]+)\!$/$1|no$1/g;
            @tmp = split /[|]/, $spec;
            #print "PUSHING: @tmp\n";
            push @ret, @tmp;
        }
    }
    return @ret;
}

# Set values for the configuration variables passed via hashref.
# Variables are of the form ${progname_prefix}_KEYNAME.
#
# Because logwatch lowercases all config file entries, KEYNAME is
# case-insensitive.
#
sub init_cmdline() {
    my ($href, $configvar, $value, $var);

    # logwatch passes all config vars via environment variables
    $href = $Opts{'standalone'} ? \%Configvars : \%ENV;

    # XXX: this is cheeze: need a list of valid limiters, but since
    # the Sections table is not built yet, we don't know what is
    # a limiter and what is an option, as there is no distinction in
    # variable names in the config file (perhaps this should be changed).
    my @valid_option_names = get_option_names();
    die "Options table not yet set" if !scalar @valid_option_names;

    print "confighash_to_cmdline: @valid_option_names\n" if $Opts{'debug'} & D_ARGS;
    my @cmdline = ();
    while (($configvar, $value) = each %$href) {
        if ($configvar =~ s/^${Logreporters::progname_prefix}_//o) {
            # distinguish level limiters from general options
            # would be easier if limiters had a unique prefix
            $configvar = lc $configvar;
            my $ret = grep(/^$configvar$/i, @valid_option_names);
            if ($ret == 0) {
                print "\tLIMITER($ret): $configvar = $value\n" if $Opts{'debug'} & D_ARGS;
                push @cmdline, '-l', "$configvar" . "=$value";
            }
            else {
                print "\tOPTION($ret): $configvar = $value\n" if $Opts{'debug'} & D_ARGS;
                unshift @cmdline, $value if defined($value);
                unshift @cmdline, "--$configvar";
            }
        }
    }
    unshift @ARGV, @cmdline;
}

# Obtains the variables from a logwatch-style .conf file, for use
# in standalone mode.  Returns an ENV-style hash of key/value pairs.
#
sub get_vars_from_file(\% $) {
    my ($href, $file) = @_;
    my ($var, $val);

    print "get_vars_from_file: enter: processing file: $file\n" if $Opts{'debug'} & D_CONFIG;

    my $message = undef;
    my $ret = stat($file);
    if ($ret == 0) {$message = $!;}
    elsif (!-r _) {$message = "Permission denied";}
    elsif (-d _) {$message = "Is a directory";}
    elsif (!-f _) {$message = "Not a regular file";}

    if ($message) {
        print STDERR "Configuration file \"$file\": $message\n";
        exit 2;
    }

    my $prog = $Logreporters::progname_prefix;
    open FILE, '<', "$file" or die "unable to open configuration file $file: $!";
    while (<FILE>) {
        chomp;
        next if (/^\s*$/); # ignore all whitespace lines
        next if (/^\*/);   # ignore logwatch's *Service lines
        next if (/^\s*#/); # ignore comment lines
        if (/^\s*\$(${prog}_[^=\s]+)\s*=\s*"?([^"]+)"?$/o) {
            ($var, $val) = ($1, $2);
            if ($val =~ /^(?:no|false)$/i) {$val = 0;}
            elsif ($val =~ /^(?:yes|true)$/i) {$val = 1;}
            elsif ($val eq '') {
                $var =~ s/${prog}_/${prog}_no/;
                $val = undef;
            }

            print "\t\"$var\" => \"$val\"\n" if $Opts{'debug'} & D_CONFIG;

            $href->{$var} = $val;
        }
    }
    close FILE or die "failed to close configuration handle for $file: $!";
    print "get_vars_from_file: exit\n" if $Opts{'debug'} & D_CONFIG;
}

sub process_limiters(\@) {
    my ($sectref) = @_;

    my ($limiter, $var, $val, @errors);
    my @l = get_usable_sectvars(@$sectref, 1);

    if ($Opts{'debug'} & D_VARS) {
        print "process_limiters: enter\n";
        print "\tLIMITERS: @Limiters\n";
    }
    while ($limiter = shift @Limiters) {
        my @matched = ();

        printf "\t%-30s  ", $limiter if $Opts{'debug'} & D_VARS;
        # disable all limiters when limiter is __none__: see 'nodetail' cmdline option
        if ($limiter eq '__none__') {
            $Opts{$_} = 0 foreach @l;
            next;
        }

        ($var, $val) = split /=/, $limiter;

        if ($val eq '') {
            push @errors, "Limiter \"$var\" requires value (ex. --limit limiter=10)";
            next;
        }

        # try exact match first, then abbreviated match next
        if (scalar(@matched = grep(/^$var$/, @l)) == 1 or scalar(@matched = grep(/^$var/, @l)) == 1) {
            $limiter = $matched[0]; # unabbreviate limiter
            print "MATCH: $var: $limiter => $val\n" if $Opts{'debug'} & D_VARS;
            # XXX move limiters into section hash entry...
            $Opts{$limiter} = $val;
            next;
        }
        print "matched=", scalar @matched, ": @matched\n" if $Opts{'debug'} & D_VARS;

        push @errors, "Limiter \"$var\" is " . (scalar @matched == 0 ? "invalid" : "ambiguous: @matched");
    }
    print "\n" if $Opts{'debug'} & D_VARS;

    if (@errors) {
        print STDERR "$_\n" foreach @errors;
        exit 2;
    }

    # Set the default value of 10 for each section if no limiter exists.
    # This allows output for each section should there be no configuration
    # file or missing limiter within the configuration file.
    foreach (@l) {
        $Opts{$_} = 10 unless exists $Opts{$_};
    }

    # Enable collection for each section if a limiter is non-zero.
    foreach (@l) {
        #print "L is: $_\n";
        #print "DETAIL: $Opts{'detail'}, OPTS: $Opts{$_}\n";
        $Logreporters::TreeData::Collecting{$_} = (($Opts{'detail'} >= 5) && $Opts{$_}) ? 1 : 0;
    }
    #print "OPTS: \n"; map { print "$_ => $Opts{$_}\n"} keys %Opts;
    #print "COLLECTING: \n"; map { print "$_ => $Logreporters::TreeData::Collecting{$_}\n"} keys %Logreporters::TreeData::Collecting;
}

# Enable/disable supplemental reports
# arg1:     0=off, 1=on
# arg2,...: list of supplemental report keywords
sub set_supplemental_reports($$) {
    my ($onoff, $aref) = @_;

    $Opts{$_} = $onoff foreach (@$aref);
}

sub process_debug_opts($) {
    my $optstring = shift;

    my @errors = ();
    foreach (split(/\s*,\s*/, $optstring)) {
        my $word = lc $_;
        my @matched = grep(/^$word/, keys %debug_words);

        if (scalar @matched == 1) {
            $Opts{'debug'} |= $debug_words{$matched[0]};
            next;
        }

        if (scalar @matched == 0) {
            push @errors, "Unknown debug keyword \"$word\"";
        }
        else { # > 1
            push @errors, "Ambiguous debug keyword abbreviation \"$word\": (matches: @matched)";
        }
    }
    if (@errors) {
        print STDERR "$_\n" foreach @errors;
        print STDERR "Debug keywords: ", join(' ', sort keys %debug_words), "\n";
        exit 2;
    }
}

# Zero the options controlling level specs and those
# any others passed via Opts key.
#
# Zero the options controlling level specs in the
# Detailed section, and set all other report options
# to disabled. This makes it easy via command line to
# disable the entire summary section, and then re-enable
# one or more sections for specific reports.
#
#   eg. progname --nodetail --limit forwarded=2
#
sub zero_opts($ @) {
    my $sectref = shift;
    # remaining args: list of Opts keys to zero

    map {$Opts{$_} = 0;
        print "zero_opts: $_ => 0\n" if $Opts{'debug'} & D_VARS;} @_;
    map {$Opts{$_} = 0} get_usable_sectvars(@$sectref, 1);
}

1;

#MODULE: ../Logreporters/TreeData.pm
package Logreporters::TreeData;

use 5.008;
use strict;
use re 'taint';
use warnings;
no warnings "uninitialized";

BEGIN {
    use Exporter ();
    use vars qw(@ISA @EXPORT @EXPORT_OK %EXPORT_TAGS $VERSION);
    $VERSION = '1.001';
    @ISA = qw(Exporter);
    @EXPORT = qw(%Totals %Counts %Collecting $END_KEY);
    @EXPORT_OK = qw(&printTree &buildTree);

}

use subs @EXPORT_OK;

BEGIN {
    import Logreporters::Config qw(%line_styles);
}

# Totals and Counts are the log line accumulator hashes.
# Totals: maintains per-section grand total tallies for use in Summary section
# Counts: is a multi-level hash, which maintains per-level key totals.
our (%Totals, %Counts);

# The Collecting hash determines which sections will be captured in
# the Counts hash.  Counts are collected only if a section is enabled,
# and this hash obviates the need to test both existence and
# non-zero-ness of the Opts{'keyname'} (either of which cause capture).
# XXX The Opts hash could be used ....
our %Collecting = ();

sub buildTree(\% $ $ $ $ $);
sub printTree($ $ $ $ $);
=pod
[ a:b:c, ... ]

which would be interpreted as follows:

a = show level a detail
b = show at most b items at this level
c = minimum count that will be shown
=cut

sub printTree($ $ $ $ $) {
    my ($treeref, $lspecsref, $line_style, $max_report_width, $debug) = @_;
    my ($entry, $line);
    my $cutlength = $max_report_width - 3;

    my $topn = 0;
    foreach $entry (sort bycount @$treeref) {
        ref($entry) ne "HASH" and die "Unexpected entry in tree: $entry\n";

        #print "LEVEL: $entry->{LEVEL}, TOTAL: $entry->{TOTAL}, HASH: $entry, DATA: $entry->{DATA}\n";

        # Once the top N lines have been printed, we're done
        if ($lspecsref->[$entry->{LEVEL}]{topn}) {
            if ($topn++ >= $lspecsref->[$entry->{LEVEL}]{topn}) {
                print '     ', '   ' x ($entry->{LEVEL} + 3), "...\n"
                    unless ($debug) and do {
                        $line = '     ' . '   ' x ($entry->{LEVEL} + 3) . '...';
                        printf "%-130s L%d: topn reached(%d)\n", $line, $entry->{LEVEL} + 1, $lspecsref->[$entry->{LEVEL}]{topn};
                    };
                last;
            }
        }

        # Once the item's count falls below the given threshold, we're done at this level
        # unless a top N is specified, as threshold has lower priority than top N
        elsif ($lspecsref->[$entry->{LEVEL}]{threshold}) {
            if ($entry->{TOTAL} <= $lspecsref->[$entry->{LEVEL}]{threshold}) {
                print '     ', '   ' x ($entry->{LEVEL} + 3), "...\n"
                    unless ($debug) and do {
                        $line = '     ' . ('   ' x ($entry->{LEVEL} + 3)) . '...';
                        printf "%-130s L%d: threshold reached(%d)\n", $line, $entry->{LEVEL} + 1, $lspecsref->[$entry->{LEVEL}]{threshold};
                    };
                last;
            }
        }

        $line = sprintf "%8d%s%s", $entry->{TOTAL}, '   ' x ($entry->{LEVEL} + 2), $entry->{DATA};

        if ($debug) {
            printf "%-130s %-60s\n", $line, $entry->{DEBUG};
        }

        # line_style full, or lines < max_report_width

        #printf "MAX: $max_report_width, LEN: %d, CUTLEN $cutlength\n", length($line);
        if ($line_style == $line_styles{'full'} or length($line) <= $max_report_width) {
            print $line, "\n";
        }
        elsif ($line_style == $line_styles{'truncate'}) {
            print substr($line, 0, $cutlength), '...', "\n";
        }
        elsif ($line_style == $line_styles{'wrap'}) {
            my $leader = ' ' x 8 . '   ' x ($entry->{LEVEL} + 2);
            print substr($line, 0, $max_report_width, ''), "\n";
            while (length($line)) {
                print $leader, substr($line, 0, $max_report_width - length($leader), ''), "\n";
            }
        }
        else {
            die('unexpected line style');
        }

        printTree($entry->{CHILDREF}, $lspecsref, $line_style, $max_report_width, $debug) if (exists $entry->{CHILDREF});
    }
}

my $re_IP_strict = qr/\b(25[0-5]|2[0-4]\d|[01]?\d{1,2})\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})\b/;
# XXX optimize this using packed default sorting.  Analysis shows speed isn't an issue though
sub bycount {
    # Sort by totals, then IP address if one exists, and finally by data as a string

    local $SIG{__WARN__} = sub {print "*** PLEASE REPORT:\n*** $_[0]*** Unexpected: \"$a->{DATA}\", \"$b->{DATA}\"\n"};

    $b->{TOTAL} <=> $a->{TOTAL}

        ||

        pack('C4' => $a->{DATA} =~ /^$re_IP_strict/o) cmp pack('C4' => $b->{DATA} =~ /^$re_IP_strict/o)

        ||

        $a->{DATA} cmp $b->{DATA}
}

#
# Builds a tree of REC structures from the multi-key %Counts hashes
#
# Parameters:
#    Hash:  A multi-key hash, with keys being used as category headings, and leaf data
#           being tallies for that set of keys
#    Level: This current recursion level.  Call with 0.
#
# Returns:
#    Listref: A listref, where each item in the list is a rec record, described as:
#           DATA:      a string: a heading, or log data
#           TOTAL:     an integer: which is the subtotal of this item's children
#           LEVEL:     an integer > 0: representing this entry's level in the tree
#           CHILDREF:  a listref: references a list consisting of this node's children
#    Total: The cumulative total of items found for a given invocation
#
# Use the special key variable $END_KEY, which is "\a\a" (two ASCII bell's) to end a,
# nested hash early, or the empty string '' may be used as the last key.

our $END_KEY = "\a\a";

sub buildTree(\% $ $ $ $ $) {
    my ($href, $max_level_section, $levspecref, $max_level_global, $recurs_level, $show_unique, $debug) = @_;
    my ($subtotal, $childList, $rec);

    my @treeList = ();
    my $total = 0;

    foreach my $item (sort keys %$href) {
        if (ref($href->{$item}) eq "HASH") {
            #print " " x ($recurs_level * 4), "HASH: LEVEL $recurs_level: Item: $item, type: \"", ref($href->{$item}), "\"\n";

            ($subtotal, $childList) = buildTree(%{$href->{$item}}, $max_level_section, $levspecref, $max_level_global, $recurs_level + 1, $debug);

            if ($recurs_level < $max_level_global and $recurs_level < $max_level_section) {
                # me + children
                $rec = {
                    DATA     => $item,
                    TOTAL    => $subtotal,
                    LEVEL    => $recurs_level,
                    CHILDREF => $childList,
                };

                if ($debug) {
                    $rec->{DEBUG} = sprintf "L%d: levelspecs: %2d/%2d/%2d/%2d, Count: %10d",
                        $recurs_level + 1, $max_level_global, $max_level_section,
                        $levspecref->[$recurs_level]{topn}, $levspecref->[$recurs_level]{threshold}, $subtotal;
                }
                push(@treeList, $rec);
            }
        }
        else {
            if ($item ne '' and $item ne $END_KEY and $recurs_level < $max_level_global and $recurs_level < $max_level_section) {
                $rec = {
                    DATA  => $item,
                    TOTAL => $href->{$item},
                    LEVEL => $recurs_level,
                    #CHILDREF => undef,
                };
                if ($debug) {
                    $rec->{DEBUG} = sprintf "L%d: levelspecs: %2d/%2d/%2d/%2d, Count: %10d",
                        $recurs_level, $max_level_global, $max_level_section,
                        $levspecref->[$recurs_level]{topn}, $levspecref->[$recurs_level]{threshold}, $href->{$item};
                }
                push(@treeList, $rec);
            }
            $subtotal = $href->{$item};
        }

        $total += $subtotal;
    }

    #print " " x ($recurs_level * 4), "LEVEL $recurs_level: Returning from recurs_level $recurs_level\n";

    return ($total, \@treeList);
}

1;

#MODULE: ../Logreporters/Reports.pm
package Logreporters::Reports;

use 5.008;
use strict;
use re 'taint';
use warnings;
no warnings "uninitialized";

BEGIN {
    use Exporter ();
    use vars qw(@ISA @EXPORT @EXPORT_OK %EXPORT_TAGS $VERSION);
    $VERSION = '1.002';
    @ISA = qw(Exporter);
    @EXPORT = qw(&inc_unmatched &print_unmatched_report &print_percentiles_report2
        &print_summary_report &print_detail_report);
    @EXPORT_OK = qw();
}

use subs @EXPORT_OK;

BEGIN {
    import Logreporters::Config qw(%Opts $fw1 $fw2 $sep1 $sep2 &D_UNMATCHED &D_TREE);
    import Logreporters::Utils qw(&commify &unitize &get_percentiles &get_percentiles2);
    import Logreporters::TreeData qw(%Totals %Counts &buildTree &printTree);
}

my (%unmatched_list);

our $origline; # unmodified log line, for error reporting and debug

sub inc_unmatched($) {
    my ($id) = @_;
    $unmatched_list{$origline}++;
    print "UNMATCHED($id): \"$origline\"\n" if $Opts{'debug'} & D_UNMATCHED;
}

# Print unmatched lines
#
sub print_unmatched_report() {
    return unless (keys %unmatched_list);

    print "\n\n**Unmatched Entries**\n";
    foreach my $line (sort {$unmatched_list{$b} <=> $unmatched_list{$a}} keys %unmatched_list) {
        printf "%8d   %s\n", $unmatched_list{$line}, $line;
    }
}

=pod
   ****** Summary ********************************************************
          2   Miscellaneous warnings

      20621   Total messages scanned ----------------  100.00%
    662.993M  Total bytes scanned                  695,198,092
   ========   ================================================

      19664   Ham -----------------------------------   95.36%
      19630     Clean passed                            95.19%
         34     Bad header passed                        0.16%

        942   Spam ----------------------------------    4.57%
        514     Spam blocked                             2.49%
        428     Spam discarded (no quarantine)           2.08%

         15   Malware -------------------------------    0.07%
         15     Malware blocked                          0.07%


       1978   SpamAssassin bypassed
         18   Released from quarantine
       1982   Whitelisted
          3   Blacklisted
         12   MIME error
         51   Bad header (debug supplemental)
         28   Extra code modules loaded at runtime
=cut
# Prints the Summary report section
#
sub print_summary_report(\@) {
    my ($sections) = @_;
    my ($keyname, $cur_level);
    my @lines;

    my $expand_header_footer = sub {
        my $line = undef;

        foreach my $horf (@_) {
            # print blank line if keyname is newline
            if ($horf eq "\n") {
                $line .= "\n";
            }
            elsif (my ($sepchar) = ($horf =~ /^(.)$/o)) {
                $line .= sprintf "%s   %s\n", $sepchar x 8, $sepchar x 50;
            }
            else {
                die "print_summary_report: unsupported header or footer type \"$horf\"";
            }
        }
        return $line;
    };

    if ($Opts{'detail'} >= 5) {
        my $header = "****** Summary ";
        print $header, '*' x ($Opts{'max_report_width'} - length $header), "\n\n";
    }

    my @headers;
    foreach my $sref (@$sections) {
        # headers and separators
        die "Unexpected Section $sref" if (ref($sref) ne 'HASH');

        # Start of a new section group.
        # Expand and save headers to output at end of section group.
        if ($sref->{CLASS} eq 'GROUP_BEGIN') {
            $cur_level = $sref->{LEVEL};
            $headers[$cur_level] = &$expand_header_footer(@{$sref->{HEADERS}});
        }
        elsif ($sref->{CLASS} eq 'GROUP_END') {
            my $prev_level = $sref->{LEVEL};

            # If this section had lines to output, tack on headers and footers,
            # removing extraneous newlines.
            if ($lines[$cur_level]) {
                # squish multiple blank lines
                if ($headers[$cur_level] and substr($headers[$cur_level], 0, 1) eq "\n") {
                    if (!defined $lines[$prev_level][-1] or $lines[$prev_level][-1] eq "\n") {
                        $headers[$cur_level] =~ s/^\n+//;
                    }
                }

                push @{$lines[$prev_level]}, $headers[$cur_level] if $headers[$cur_level];
                push @{$lines[$prev_level]}, @{$lines[$cur_level]};
                my $f = &$expand_header_footer(@{$sref->{FOOTERS}});
                push @{$lines[$prev_level]}, $f if $f;
                $lines[$cur_level] = undef;
            }

            $headers[$cur_level] = undef;
            $cur_level = $prev_level;
        }
        elsif ($sref->{CLASS} eq 'DATA') {
            # Totals data
            $keyname = $sref->{NAME};
            if ($Totals{$keyname} > 0) {
                my ($numfmt, $desc, $divisor) = ($sref->{FMT}, $sref->{TITLE}, $sref->{DIVISOR});

                my $fmt = '%8';
                my $extra = ' %25s';
                my $total = $Totals{$keyname};

                # Z format provides  unitized or unaltered totals, as appropriate
                if ($numfmt eq 'Z') {
                    ($total, $fmt) = unitize($total, $fmt);
                }
                else {
                    $fmt .= "$numfmt ";
                    $extra = '';
                }

                if ($divisor and $$divisor) {
                    # XXX generalize this
                    if (ref($desc) eq 'ARRAY') {
                        $desc = @$desc[0] . ' ' . @$desc[1] x (42 - 2 - length(@$desc[0]));
                    }

                    push @{$lines[$cur_level]},
                        sprintf "$fmt  %-42s %6.2f%%\n", $total, $desc,
                            $$divisor == $Totals{$keyname} ? 100.00 : $Totals{$keyname} * 100 / $$divisor;
                }
                else {
                    my $out = sprintf "$fmt  %-23s", $total, $desc;
                    if ($extra) {
                        $out .= sprintf " $extra", commify($Totals{$keyname});
                    }
                    $out .= "\n";
                    push @{$lines[$cur_level]}, $out;
                }
            }
        }
        else {
            die "print_summary_report: unexpected control...";
        }
    }
    print @{$lines[0]};
    print "\n";
}

# Prints the Detail report section
#
# Note: side affect; deletes each key in Totals/Counts
# after printout.  Only the first instance of a key in
# the Section table will result in Detail output.
sub print_detail_report(\@) {
    my ($sections) = @_;
    my $header_printed = 0;

    return unless (keys %Counts);

    #use Devel::Size qw(size total_size);

    foreach my $sref (@$sections) {
        next unless $sref->{CLASS} eq 'DATA';
        # only print detail for this section if DETAIL is enabled
        # and there is something in $Counts{$keyname}
        next unless $sref->{DETAIL};
        next unless exists $Counts{$sref->{NAME}};

        my $keyname = $sref->{NAME};
        my $max_level = undef;
        my $print_this_key = 0;

        my @levelspecs = ();
        clear_level_specs($max_level, \@levelspecs);
        if (exists $Opts{$keyname}) {
            $max_level = create_level_specs($Opts{$keyname}, $Opts{'detail'}, \@levelspecs);
            $print_this_key = 1 if ($max_level);
        }
        else {
            $print_this_key = 1;
        }
        #print_level_specs($max_level,\@levelspecs);

        # at detail 5, print level 1, detail 6: level 2, ...

        #print STDERR "building: $keyname\n";
        my ($count, $treeref) =
            buildTree(%{$Counts{$keyname}}, defined($max_level) ? $max_level : 11,
                \@levelspecs, $Opts{'detail'} - 4, 0, $Opts{'debug'} & D_TREE);

        if ($count > 0) {
            if ($print_this_key) {
                my $desc = $sref->{TITLE};
                $desc =~ s/^\s+//;

                if (!$header_printed) {
                    my $header = "****** Detail ($max_level) ";
                    print $header, '*' x ($Opts{'max_report_width'} - length $header), "\n";
                    $header_printed = 1;
                }
                printf "\n%8d   %s %s\n", $count, $desc,
                    $Opts{'sect_vars'} ?
                        ('-' x ($Opts{'max_report_width'} - 18 - length($desc) - length($keyname))) . " [ $keyname ] -" :
                        '-' x ($Opts{'max_report_width'} - 12 - length($desc))
            }

            printTree($treeref, \@levelspecs, $Opts{'line_style'}, $Opts{'max_report_width'},
                $Opts{'debug'} & D_TREE);
        }
        #print STDERR "Total size Counts: ", total_size(\%Counts), "\n";
        #print STDERR "Total size Totals: ", total_size(\%Totals), "\n";
        $treeref = undef;
        $Totals{$keyname} = undef;
        delete $Totals{$keyname};
        delete $Counts{$keyname};
    }
    #print "\n";
}

=pod

Print out a standard percentiles report

   === Delivery Delays Percentiles ===============================================================
                          0%       25%       50%       75%       90%       95%       98%      100%
   -----------------------------------------------------------------------------------------------
   Before qmgr          0.01      0.70      1.40  45483.70  72773.08  81869.54  87327.42  90966.00
   In qmgr              0.00      0.00      0.00      0.01      0.01      0.01      0.01      0.01
   Conn setup           0.00      0.00      0.00      0.85      1.36      1.53      1.63      1.70
   Transmission         0.03      0.47      0.92      1.61      2.02      2.16      2.24      2.30
   Total                0.05      1.18      2.30  45486.15  72776.46  81873.23  87331.29  90970.00
   ===============================================================================================

   === Postgrey Delays Percentiles ===========================================================
                      0%       25%       50%       75%       90%       95%       98%      100%
   -------------------------------------------------------------------------------------------
   Postgrey       727.00    727.00    727.00    727.00    727.00    727.00    727.00    727.00
   ===========================================================================================

 tableref:
   data table: ref to array of arrays, first cell is label, subsequent cells are data
 title:
   table's title
 percentiles_str:
   string of space or comma separated integers, which are the percentiles
   calculated and output as table column data
=cut
sub print_percentiles_report2($$$) {
    my ($tableref, $title, $percentiles_str) = @_;

    return unless @$tableref;

    my $myfw2 = $fw2 - 1;
    my @percents = split /[ ,]/, $percentiles_str;

    # Calc y label width from the hash's keys. Each key is padded with the
    # string "#: ", # where # is a single-digit sort index.
    my $y_label_max_width = 0;
    for (@$tableref) {
        $y_label_max_width = length($_->[0]) if (length($_->[0]) > $y_label_max_width);
    }

    # Titles row
    my $col_titles_str = sprintf "%-${y_label_max_width}s" . "%${myfw2}s%%" x @percents, ' ', @percents;
    my $table_width = length($col_titles_str);

    # Table header row
    my $table_header_str = sprintf "%s %s ", $sep1 x 3, $title;
    $table_header_str .= $sep1 x ($table_width - length($table_header_str));

    print "\n", $table_header_str;
    print "\n", $col_titles_str;
    print "\n", $sep2 x $table_width;

    my (@p, @coldata, @xformed);
    foreach (@$tableref) {
        my ($title, $ref) = ($_->[0], $_->[1]);
        #xxx my @sorted = sort { $a <=> $b } @{$_->[1]};

        my @byscore = ();

        for my $bucket (sort {$a <=> $b} keys %$ref) {
            #print "Key: $title: Bucket: $bucket = $ref->{$bucket}\n";
            # pairs: bucket (i.e. key), tally
            push @byscore, $bucket, $ref->{$bucket};
        }

        my @p = get_percentiles2(@byscore, @percents);
        printf "\n%-${y_label_max_width}s" . "%${fw2}.2f" x scalar(@p), $title, @p;
    }

=pod
   foreach (@percents) {
      #printf "\n%-${y_label_max_width}s" . "%${fw2}.2f" x scalar (@p), substr($title,3), @p;
      printf "\n%3d%%", $title;
      foreach my $val (@{shift @xformed}) {
         my $unit;
         if ($val > 1000) {
            $unit = 's';
            $val /= 1000;
         }
         else {
            $unit = '';
         }
         printf "%${fw3}.2f%-2s", $val, $unit;
      }
   }
=cut

    print "\n", $sep1 x $table_width, "\n";
}

sub clear_level_specs($ $) {
    my ($max_level, $lspecsref) = @_;
    #print "Zeroing $max_level rows of levelspecs\n";
    $max_level = 0 if (not defined $max_level);
    for my $x (0 .. $max_level) {
        $lspecsref->[$x]{topn} = undef;
        $lspecsref->[$x]{threshold} = undef;
    }
}

# topn      = 0 means don't limit
# threshold = 0 means no min threshold
sub create_level_specs($ $ $) {
    my ($optkey, $gdetail, $lspecref) = @_;

    return 0 if ($optkey eq "0");

    my $max_level = $gdetail; # default to global detail level
    my (@specsP1, @specsP2, @specsP3);

    #printf "create_level_specs: key: %s => \"%s\", max_level: %d\n", $optkey, $max_level;

    foreach my $sp (split /[\s,]+/, $optkey) {
        #print "create_level_specs:  SP: \"$sp\"\n";
        # original level specifier
        if ($sp =~ /^\d+$/) {
            $max_level = $sp;
            #print "create_level_specs:  max_level set: $max_level\n";
        }
        # original level specifier + topn at level 1
        elsif ($sp =~ /^(\d*)\.(\d+)$/) {
            if ($1) {$max_level = $1;}
            else {$max_level = $gdetail;} # top n specified, but no max level

            # force top N at level 1 (zero based)
            push @specsP1, { level => 0, topn => $2, threshold => 0 };
        }
        # newer level specs
        elsif ($sp =~ /^::(\d+)$/) {
            push @specsP3, { level => undef, topn => 0, threshold => $1 };
        }
        elsif ($sp =~ /^:(\d+):(\d+)?$/) {
            push @specsP2, { level => undef, topn => $1, threshold => defined $2 ? $2 : 0 };
        }
        elsif ($sp =~ /^(\d+):(\d+)?:(\d+)?$/) {
            push @specsP1, { level => ($1 > 0 ? $1 - 1 : 0), topn => $2 ? $2 : 0, threshold => $3 ? $3 : 0 };
        }
        else {
            print STDERR "create_level_specs: unexpected levelspec ignored: \"$sp\"\n";
        }
    }

    #foreach my $sp (@specsP3, @specsP2, @specsP1) {
    #   printf "Sorted specs: L%d, topn: %3d, threshold: %3d\n", $sp->{level}, $sp->{topn}, $sp->{threshold};
    #}

    my ($min, $max);
    foreach my $sp (@specsP3, @specsP2, @specsP1) {
        ($min, $max) = (0, $max_level);

        if (defined $sp->{level}) {
            $min = $max = $sp->{level};
        }
        for my $level ($min .. $max) {
            #printf "create_level_specs: setting L%d, topn: %s, threshold: %s\n", $level, $sp->{topn}, $sp->{threshold};
            $lspecref->[$level]{topn} = $sp->{topn} if ($sp->{topn});
            $lspecref->[$level]{threshold} = $sp->{threshold} if ($sp->{threshold});
        }
    }

    return $max_level;
}

sub print_level_specs($ $) {
    my ($max_level, $lspecref) = @_;
    for my $level (0 .. $max_level) {
        printf "LevelSpec Row %d: %3d %3d\n", $level, $lspecref->[$level]{topn}, $lspecref->[$level]{threshold};
    }
}

1;


package Logreporters;

BEGIN {
    import Logreporters::Utils;
    import Logreporters::Config;
    import Logreporters::TreeData qw(%Totals %Counts %Collecting printTree buildTree);
    import Logreporters::Reports;
}
use 5.008;
use strict;
use warnings;
no warnings "uninitialized";
use re 'taint';

use Getopt::Long;
use File::Basename;

our $progname = fileparse($0);

# the list of supplemental reports available in the Detail section
#p0f
my @supplemental_reports = qw(
    autolearn score_percentiles score_frequencies sarules timings sa_timings startinfo
);

# Default values for various options, used if no config file exists,
# or some option is not set.
#
# These are used to reset default values after an option has been
# disabled (via undef'ing its value).  This allows a report to be
# disabled via config file or --nodetail, but reenabled via subsequent
# command line option
my %Defaults = (
    detail                 => 10,               # report level detail
    max_report_width       => 100,              # maximum line width for report output
    line_style             => undef,            # lines > max_report_width, 0=truncate,1=wrap,2=full
    syslog_name            => $progname_prefix, # amavis' syslog service name
    sect_vars              => 0,                # show section vars in detail report hdrs
    ipaddr_width           => 15,               # width for printing ip addresses
    first_recip_only       => 0,                # Show only the first recipient, or all

    autolearn              => 1, # show Autolearn report
    bayes                  => 1, # show hit Bayesian buckets
    #p0f                    => 'all all',               # p0f hits report
    sarules                => '20 20',               # show SpamAssassin rules hit
    score_frequencies      => '-10 -5 0 5 10 20 30', # buckets shown in spam scores report
    score_percentiles      => '0 50 90 95 98 100',   # percentiles shown in spam scores report
    startinfo              => 1,                     # show amavis startup info
    timings                => 95,                    # show top N% of the timings report
    timings_percentiles    => '0 5 25 50 75 95 100', # percentiles shown in timing report
    sa_timings             => 95,                    # show top N% of the SA timings report
    sa_timings_percentiles => '0 5 25 50 75 95 100', # percentiles shown in SA timing report
);

my $usage_str = <<"END_USAGE";
Usage: $progname [ ARGUMENTS ] [logfile ...]

   ARGUMENTS can be one or more of options listed below.  Later options override earlier ones.
   Any argument may be abbreviated to an unambiguous length.  Input comes from named logfiles,
   or STDIN.

   --debug AREAS                       provide debug output for AREAS
   --help                              print usage information
   --version                           print program version

   --config_file FILE, -f FILE         use alternate configuration file FILE
   --syslog_name PATTERN               only consider log lines that match
                                       syslog service name PATTERN

   --detail LEVEL                      print LEVEL levels of detail
                                       (default: 10)
   --nodetail                          set all detail levels to 0
   --[no]summary                       display the summary section

   --ipaddr_width WIDTH                use WIDTH chars for IP addresses in
                                       address/hostname pairs
   --line_style wrap|full|truncate     disposition of lines > max_report_width
                                       (default: truncate)
   --full                              same as --line_style=full
   --truncate                          same as --line_style=truncate
   --wrap                              same as --line_style=wrap
   --max_report_width WIDTH            limit report width to WIDTH chars
                                       (default: 100)
   --limit L=V, -l L=V                 set level limiter L with value V
   --[no]sect_vars                     [do not] show config file var/cmd line
                                       option names in section titles

   --[no]autolearn                     show autolearn report
   --[no]by_ccat_summary               include by contents category grouping in summary
   --[no]first_recip_only              show first recipient only, or all recipients
   --nosarules                         disable SpamAssassin spam and ham rules hit reports
   --sarules "S,H"                     enable SpamAssassin spam and ham rules reports, showing
   --sarules "default"                 showing the top S spam and top H ham rules hit (range:
                                       0..., "all", or the keyword "default").
   --noscore_frequencies               disable spam score frequency report
   --score_frequencies "B1 [B2 ...]"   enable spam score frequency report, using buckets
   --score_frequencies "default"       specified with B1 [B2 ...] (range: real numbers), or using their
                                       internal default values when the keyword "default" is given
   --noscore_percentiles               disable spam score percentiles report
   --score_percentiles "P1 [P2 ...]"   enable spam score percentiles report, using percentiles
   --score_percentiles "default"       specified with P1 [P2 ...] (range: 0...100), or using their
                                       internal default values when the keyword "default" is given
   --[no]startinfo                     show latest amavis startup details, if available

   --nosa_timings                      disable the SA timings report (same as --sa_timings 0)
   --sa_timings PERCENT                show top PERCENT percent of the SA timings report (range: 0...100)
   --sa_timings_percentiles "P1 [P2 ...]"
                                       set SA timings report percentiles to P1 [P2 ...]  (range: 0...100)

   --notimings                         disable the timings report (same as --timings 0)
   --timings PERCENT                   show top PERCENT percent of the timings report (range: 0...100)
   --timings_percentiles "P1 [P2 ...]" set timings report percentiles to P1 [P2 ...]  (range: 0...100)
END_USAGE

# local prototypes
sub usage($);
sub init_getopts_table();
sub init_defaults();
sub build_sect_table();

sub parse_vals($$);
sub triway_opts($$);

sub printSpamScorePercentilesReport;
sub printSpamScoreFrequencyReport;
sub printAutolearnReport;
sub printSARulesReport;
sub printTimingsReport($$$$);
sub printStartupInfoReport;
sub strip_trace($);
sub prioritize_cmdline(@);

sub create_ignore_list();
sub check_ignore_list($ \@);

# lines that match any RE in this list will be ignored.
# see create_ignore_list();
my @ignore_list_final = ();

# The Sections table drives Summary and Detail reports.  For each entry in the
# table, if there is data available, a line will be output in the Summary report.
# Additionally, a sub-section will be output in the Detail report if both the
# global --detail, and the section's limiter variable, are sufficiently high (a
# non-existent section limiter variable is considered to be sufficiently high).
#
my @Sections;

# Initialize main running mode and basic opts
init_run_mode($config_file);

# Configure the Getopts options table
init_getopts_table();

# Place configuration file/environment variables onto command line
init_cmdline();

# Initialize default values
init_defaults();

# Process command line arguments, 0=no_permute,no_pass_through
get_options(0);

# Build the Section table
build_sect_table();

# Run through the list of Limiters, setting the limiters in %Opts.
process_limiters(@Sections);

# Set collection for any enabled supplemental sections
foreach (@supplemental_reports) {
    $Logreporters::TreeData::Collecting{$_} = (($Opts{'detail'} >= 5) && $Opts{$_}) ? 1 : 0;
}

# Don't collect SpamScores when not necessary
$Collecting{'spamscores'} = ($Opts{'detail'} >= 5 && ($Opts{'score_percentiles'} || $Opts{'score_frequencies'})) ? 1 : 0;

if (!defined $Opts{'line_style'}) {
    # default line style to full if detail >= 11, or truncate otherwise
    $Opts{'line_style'} =
        ($Opts{'detail'} > 10) ? $line_styles{'full'} : $line_styles{'truncate'};
}

# Create the list of REs used to match against log lines
create_ignore_list();

my (%Timings, %TimingsSA, @TimingsTotals, @TimingsSATotals);
my (%SaveLine, %StartInfo);
my (%SpamScores, %spamtags, %p0ftags);

# Priority: VIRUS BANNED UNCHECKED SPAM SPAMMY BADH OVERSIZED MTA CLEAN
my %ccatmajor_to_sectkey = (
    'INFECTED'    => 'malware',
    'BANNED'      => 'bannedname',
    'UNCHECKED'   => 'unchecked',
    'SPAM'        => 'spam',
    'SPAMMY'      => 'spammy',
    'BAD-HEADER'  => 'badheader',
    'OVERSIZED'   => 'oversized',
    'MTA-BLOCKED' => 'mta',
    'CLEAN'       => 'clean',
    'TEMPFAIL'    => 'tempfail',
    'OTHER'       => 'other',
);

my %ccatmajor_to_priority = (
    'INFECTED'    => 9,
    'BANNED'      => 8,
    'UNCHECKED'   => 7,
    'SPAM'        => 6,
    'SPAMMY'      => 5,
    'BAD-HEADER'  => 4,
    'OVERSIZED'   => 3,
    'MTA-BLOCKED' => 2,
    'CLEAN'       => 1,
    'TEMPFAIL'    => 0,
    'OTHER'       => 0,
);

# for reports
my %ccatmajor_to_spamham = (
    'INFECTED'    => 'malware',
    'BANNED'      => 'bannedname',
    'UNCHECKED'   => 'unchecked',
    'SPAM'        => 'spam',
    'SPAMMY'      => 'spam',
    'BAD-HEADER'  => 'ham',
    'OVERSIZED'   => 'ham',
    'MTA-BLOCKED' => 'ham',
    'CLEAN'       => 'ham',
    'TEMPFAIL'    => 'ham',
    'OTHER'       => 'ham',
);

my $logline_maxlen = 980;

# Create the list of REs against which log lines are matched.
# Lines that match any of the patterns in this list are ignored.
#
# Note: This table is created at runtime, due to a Perl bug which
# I reported as perl bug #56202:
#
#    http://rt.perl.org/rt3/Public/Bug/Display.html?id=56202
#

sub create_ignore_list() {
    push @ignore_list_final, qr/^lookup_ip_acl/;
    push @ignore_list_final, qr/^lookup_acl/;
    push @ignore_list_final, qr/^lookup_hash/;
    push @ignore_list_final, qr/^lookup_re/;
    push @ignore_list_final, qr/^lookup_ldap/;
    push @ignore_list_final, qr/^lookup_sql_field.* result=[YN]$/;
    push @ignore_list_final, qr/^lookup .* does not match$/;
    push @ignore_list_final, qr/^lookup [[(]/;
    push @ignore_list_final, qr/^lookup => /;
    push @ignore_list_final, qr/^lookup: /;
    push @ignore_list_final, qr/^save_info_preliminary/; # log level 4
    push @ignore_list_final, qr/^save_info_final/;       # log level 4
    push @ignore_list_final, qr/^sql: /;
    push @ignore_list_final, qr/^sql_storage: retrying/;
    push @ignore_list_final, qr/^sql flush: /;
    push @ignore_list_final, qr/^sql print/;
    push @ignore_list_final, qr/^sql begin transaction/;
    push @ignore_list_final, qr/^sql rollback/;
    push @ignore_list_final, qr/^mail_via_sql: /;
    push @ignore_list_final, qr/^CALLING SA check$/;
    push @ignore_list_final, qr/^calling SA parse,/;
    push @ignore_list_final, qr/^timer set to \d+/;
    push @ignore_list_final, qr/^query_keys/;
    push @ignore_list_final, qr/^find_or_save_addr: /;
    push @ignore_list_final, qr/^header: /;
    push @ignore_list_final, qr/^DO_QUARANTINE, /;
    push @ignore_list_final, qr/^DEBUG_ONESHOT: /;
    push @ignore_list_final, qr/^TempDir::/;
    push @ignore_list_final, qr/^check_mail_begin_task: /;
    push @ignore_list_final, qr/^program: .*?(anomy|altermime|disclaimer).*? said: /; # log_level 2
    push @ignore_list_final, qr/^body (?:type|hash): /;
    push @ignore_list_final, qr/^\d+\.From: <.*>, \d+.Mail_From:/;
    push @ignore_list_final, qr/^The amavisd daemon is (?:apparently )?not running/;
    push @ignore_list_final, qr/^rw_loop/;
    push @ignore_list_final, qr/^[SL]MTP[><]/;
    push @ignore_list_final, qr/^[SL]MTP response for/;
    push @ignore_list_final, qr/^dsn:/i, # DSN or dsn
        push @ignore_list_final, qr/^enqueue: /;
    push @ignore_list_final, qr/^write_header: /;
    push @ignore_list_final, qr/^banned check: /;
    push @ignore_list_final, qr/^child_finish_hook/;
    push @ignore_list_final, qr/^inspect_dsn:/;
    push @ignore_list_final, qr/^client IP address unknown/;
    push @ignore_list_final, qr/^final_destiny/;
    push @ignore_list_final, qr/^one_response_for_all/;
    push @ignore_list_final, qr/^headers CLUSTERING/;
    push @ignore_list_final, qr/^notif=/;
    push @ignore_list_final, qr/^\(about to connect/;
    push @ignore_list_final, qr/^Original mail size/;
    push @ignore_list_final, qr/^TempDir removal/;
    push @ignore_list_final, qr/^Issued a new file name/;
    push @ignore_list_final, qr/^starting banned checks/;
    push @ignore_list_final, qr/^skip admin notification/;
    push @ignore_list_final, qr/^do_notify_and_quarantine - done/;
    push @ignore_list_final, qr/^do_[a-zA-Z]+.* done$/i;
    push @ignore_list_final, qr/^Remote host presents itself as:/;
    push @ignore_list_final, qr/^connect_to_ldap/;
    push @ignore_list_final, qr/^connect_to_sql: trying /;
    push @ignore_list_final, qr/^ldap begin_work/;
    push @ignore_list_final, qr/^Connecting to LDAP server/;
    push @ignore_list_final, qr/^loaded base policy bank/;
    push @ignore_list_final, qr/^\d+\.From:/;
    push @ignore_list_final, qr/^Syslog (retries|warnings)/;
    push @ignore_list_final, qr/^smtp connection cache/;
    push @ignore_list_final, qr/^smtp cmd> /;
    push @ignore_list_final, qr/^smtp session/;
    push @ignore_list_final, qr/^Ignoring stale PID file/;
    push @ignore_list_final, qr/^mime_decode_preamble/;
    push @ignore_list_final, qr/^doing banned check for/;
    push @ignore_list_final, qr/^open_on_specific_fd/;
    push @ignore_list_final, qr/^reparenting /;
    push @ignore_list_final, qr/^Issued a new pseudo part: /;
    push @ignore_list_final, qr/^run_command: /;
    push @ignore_list_final, qr/^result line from file/;
    push @ignore_list_final, qr/^Charging /;
    push @ignore_list_final, qr/^check_for_banned /;
    push @ignore_list_final, qr/^Extracting mime components$/;
    push @ignore_list_final, qr/^response to /;
    push @ignore_list_final, qr/^File-type of /;
    push @ignore_list_final, qr/^Skip admin notification, /;
    push @ignore_list_final, qr/^run_av: /;
    push @ignore_list_final, qr/^string_to_mime_entity /;
    push @ignore_list_final, qr/^ndn_needed=/;
    push @ignore_list_final, qr/^sending RCPT TO:/;
    push @ignore_list_final, qr/^decode_parts: /;
    push @ignore_list_final, qr/^decompose_part: /;
    push @ignore_list_final, qr/^setting body type: /;
    push @ignore_list_final, qr/^mime_decode_epilogue: /;
    push @ignore_list_final, qr/^string_to_mime_entity: /;
    push @ignore_list_final, qr/^at the END handler: /;
    push @ignore_list_final, qr/^Amavis::.* called$/;
    push @ignore_list_final, qr/^Amavis::.* close,/;
    push @ignore_list_final, qr/^dkim: /; # XXX provide stats
    push @ignore_list_final, qr/^collect banned table/;
    push @ignore_list_final, qr/^collect_results from/;
    push @ignore_list_final, qr/^blocking contents category is/;
    push @ignore_list_final, qr/^running file\(/;
    push @ignore_list_final, qr/^Found av scanner/;
    push @ignore_list_final, qr/^Found myself/;
    push @ignore_list_final, qr/^mail_via_smtp/;
    push @ignore_list_final, qr/^switch_to_client_time/;
    push @ignore_list_final, qr/^parse_message_id/;
    push @ignore_list_final, qr/^parse_received: /;
    push @ignore_list_final, qr/^parse_ip_address_from_received: /;
    push @ignore_list_final, qr/^fish_out_ip_from_received: /;
    push @ignore_list_final, qr/^Waiting for the process \S+ to terminate/;
    push @ignore_list_final, qr/^Valid PID file \(younger than sys uptime/;
    push @ignore_list_final, qr/^Sending SIG\S+ to amavisd/;
    push @ignore_list_final, qr/^Can't send SIG\S+ to process/;
    push @ignore_list_final, qr/^killing process/;
    push @ignore_list_final, qr/^no need to kill process/;
    push @ignore_list_final, qr/^process .* is still alive/;
    push @ignore_list_final, qr/^Daemon \[\d+\] terminated by SIG/;
    push @ignore_list_final, qr/^storage and lookups will use .* to SQL/;
    push @ignore_list_final, qr/^idle_proc, /;
    push @ignore_list_final, qr/^switch_to_my_time/;
    push @ignore_list_final, qr/^TempDir::strip: /;
    push @ignore_list_final, qr/^rmdir_recursively/;
    push @ignore_list_final, qr/^sending [SL]MTP response/;
    push @ignore_list_final, qr/^prolong_timer/;
    push @ignore_list_final, qr/^process_request:/;
    push @ignore_list_final, qr/^exiting process_request/;
    push @ignore_list_final, qr/^post_process_request_hook: /;
    push @ignore_list_final, qr/^SMTP session over/;
    push @ignore_list_final, qr/^updating snmp variables/;
    push @ignore_list_final, qr/^best_try_originator_ip/;
    push @ignore_list_final, qr/^mail checking ended: /; # log level 2
    push @ignore_list_final, qr/^The amavisd daemon is already running/;
    push @ignore_list_final, qr/^AUTH not needed/;
    push @ignore_list_final, qr/^load: \d+ %, total idle/;
    push @ignore_list_final, qr/^policy protocol: [^=]+=\S+(?:,\S+)*$/; # allow "policy protocol: INVALID ..." later
    push @ignore_list_final, qr/^penpals: /;
    push @ignore_list_final, qr/^Not calling virus scanners, no files to scan in/;
    push @ignore_list_final, qr/^local delivery: /;
    push @ignore_list_final, qr/^run_as_subprocess: child process \S*: Broken pipe/;
    push @ignore_list_final, qr/^initializing Mail::SpamAssassin/;
    push @ignore_list_final, qr/^Error reading mail header section/; # seems to occur gen. due to perl getline() bug
    push @ignore_list_final, qr/^flatten_and_tidy_dir/;
    push @ignore_list_final, qr/^do_7zip: member/;
    push @ignore_list_final, qr/^Expanding \S+ archive/;
    push @ignore_list_final, qr/^files_to_scan:/;
    push @ignore_list_final, qr/^Unzipping p\d+/;
    push @ignore_list_final, qr/^writing mail text to SQL/;
    push @ignore_list_final, qr/^strip_tempdir/;
    push @ignore_list_final, qr/^no parts, file/;
    push @ignore_list_final, qr/^warnsender_with_pass/;
    push @ignore_list_final, qr/^RETURNED FROM SA check/;
    push @ignore_list_final, qr/^mime_traverse: /;
    push @ignore_list_final, qr/^do_spam: /;
    push @ignore_list_final, qr/^prepare_tempdir: /;
    push @ignore_list_final, qr/^check_header: /;
    push @ignore_list_final, qr/^skip admin notification/;
    push @ignore_list_final, qr/^do_executable: not a/;
    push @ignore_list_final, qr/^Skip spam admin notification, no administrators$/;
    push @ignore_list_final, qr/^skip banned check for/;
    push @ignore_list_final, qr/^is_outgoing /;
    push @ignore_list_final, qr/^NO Disclaimer/;
    push @ignore_list_final, qr/^Using \(\S+\) on file/;
    push @ignore_list_final, qr/^no anti-spam code loaded/;
    push @ignore_list_final, qr/^entered child_init_hook/;
    push @ignore_list_final, qr/^body type/;
    push @ignore_list_final, qr/^establish_or_refresh/;
    push @ignore_list_final, qr/^get_body_digest/;
    push @ignore_list_final, qr/^ask_daemon_internal/;
    push @ignore_list_final, qr/^Turning AV infection into a spam report, name already accounted for/;
    push @ignore_list_final, qr/^Calling virus scanners/;
    push @ignore_list_final, qr/^timer stopped after /;
    push @ignore_list_final, qr/^virus_presence /;
    push @ignore_list_final, qr/^cache entry /;
    push @ignore_list_final, qr/^generate_mail_id /;
    push @ignore_list_final, qr/^Load low precedence policybank/;
    push @ignore_list_final, qr/^warm restart on /; # XXX could be placed instartup info
    push @ignore_list_final, qr/^Signalling a SIGHUP to a running daemon/;
    push @ignore_list_final, qr/^Deleting db files /;
    push @ignore_list_final, qr/^address modified \(/;
    push @ignore_list_final, qr/^Request: AM\.PDP /;
    push @ignore_list_final, qr/^DSPAM result: /;
    push @ignore_list_final, qr/^bind to \//;
    push @ignore_list_final, qr/^ZMQ enabled: /;

    push @ignore_list_final, qr/^Inserting header field: X-Amavis-Hold: /;
    push @ignore_list_final, qr/^Decoding of .* failed, leaving it unpacked: /;

    # various forms of "Using ..."
    # more specific, interesting variants already captured: search "Using"
    push @ignore_list_final, qr/^Using \(.*\) on dir:/;
    push @ignore_list_final, qr/^Using [^:]+: \(built-in interface\)/;
    push @ignore_list_final, qr/^Using \(.*\): /;
    push @ignore_list_final, qr/: sleeping for /;
    push @ignore_list_final, qr/creating socket by /;

    # unanchored
    push @ignore_list_final, qr/\bRUSAGE\b/;
    push @ignore_list_final, qr/: Sending .* to UNIX socket/;
}

# Notes:
#
#   - IN REs, always use /o flag or qr// at end of RE when RE uses unchanging interpolated vars
#   - In REs, email addresses may be empty "<>" - capture using *, not + ( eg. from=<[^>]*> )
#   - See additional notes below, search for "Note:".
#   - XXX indicates change, fix or more thought required

# Main processing loop
#
while (<>) {
    chomp;
    s/ +$//;
    next if $_ eq '';

    $Logreporters::Reports::origline = $_;

    if ($Opts{'standalone'}) {
        next unless s/^[A-Z][a-z]{2} [ \d]\d \d{2}:\d{2}:\d{2} (?:<[^>]+> )?\S+ $Opts{'syslog_name'}(?:\[\d+\])?: (?:\[ID \d+ \w+\.\w+\] )?//o;
    }

    my $p1 = $_;
    my ($p2, $pid);
    my $action = "blocked"; # default action is blocked if not present in log

    # For now, ignore the amavis startup timing lines.  Need to do this
    # before stripping out the amavis pid to differentiate these from the
    # scan timing reports
    next if ($p1 =~ /^TIMING/);

    my $linelen = length $p1;
    # Strip amavis process id-instance id, or release id
    if (($pid, $p2) = ($p1 =~ /^\(([^)]+)\) (.*)$/)) {
        $p1 = $p2;
    }

    # Handle continuation lines.  Continuation lines should be in order per PID, meaning line1, line2, line3,
    # but never line3, line1, line2.
    #
    # amavis log lines as chopped by sub write_log are exactly 980 characters long starting with '(' as in:
    #  amavis[47061]: (47061-15) SPAM, etc  ...
    #                 ^ <-----980------------->
    # but this can be changed in amavis via $logline_maxlen.
    # There may also be the alert markers (!) and (!!) preceeding any continuation ellipsis.
    #

    # ... a continued line ...
    if ($p1 =~ s/^(\([!]{1,2}\))?\.\.\.//) {
        if (!exists($SaveLine{$pid})) {
            my $alert = $1;
            #printf "Unexpected continue line: \"%s\"\n", $p1;
            $SaveLine{$pid} = $alert || '';
        }
        $SaveLine{$pid} .= $p1;
        next if $SaveLine{$pid} =~ s/\.\.\.$//; # next if line has more pieces
    }

    # this line continues ...
    if ($p1 =~ /\.\.\.$/ and $linelen == $logline_maxlen) {
        $p1 =~ s/\.\.\.$//;
        $SaveLine{$pid} = $p1;
        next;
    }

    if (exists($SaveLine{$pid})) {
        # printf "END OF SaveLine: %s\n", $SaveLine{$pid};
        $p1 = delete $SaveLine{$pid};
    }

    #if (length($p1) > 10000) {
    #   printf "Long log entry %d chars: \"%s\"\n", length($p1), $p1;
    #   next;
    #}

    next if (
        # Place REs here that should ignore log lines otherwise caught below.
        # Some are located here historically, and need to be checked for candidates
        # to be relocated to ignore_list_final.
        ($p1 =~ /^do_ascii/)
            or ($p1 =~ /^Checking/)
            or ($p1 =~ /^header_edits_for_quar: /)
            or ($p1 =~ /^Not-Delivered/)
            or ($p1 =~ /^SpamControl/)
            or ($p1 =~ /^Perl/)
            or ($p1 =~ /^ESMTP/)
            or ($p1 =~ /^(?:\(!+\))?(\S+ )?(?:FWD|SEND) from /)      # log level 4
            or ($p1 =~ /^(?:\(!+\))?(\S+ )?(?:ESMTP|FWD|SEND) via /) # log level 4
            or ($p1 =~ /^tempdir being removed/)
            or ($p1 =~ /^do_notify_and_quar(?:antine)?: .*ccat/)
            or ($p1 =~ /^cached [a-zA-Z0-9]+ /)
            or ($p1 =~ /^loaded policy bank/)
            or ($p1 =~ /^p\.path/)
            or ($p1 =~ /^virus_scan: /)
            or ($p1 =~ /^Requesting (a |)process rundown after [0-9]+ tasks/)
            or ($p1 =~ /^Cached (virus|spam) check expired/)
            or ($p1 =~ /^pr(?:esent|ovid)ing full original message to scanners as/) # log level 2
            or ($p1 =~ /^Actual message size [0-9]+ B(,| greater than the) declared [0-9]+ B/)
            or ($p1 =~ /^disabling DSN/)
            or ($p1 =~ /^Virus ([^,]+ )?matches [^,]+, sender addr ignored/)
            or ($p1 =~ /^release /)
            or ($p1 =~ /^adding SA score \S+ to existing/)
            or ($p1 =~ /^Maia:/)     # redundant
            or ($p1 =~ /^AM\.PDP  /) # this appears to be always have two spaces
            # because in amavisd::preprocess_policy_query() when $ampdp is
            # set, it will pass an unset $attr_ref->{'mail_id'} to do_log(1
            or ($p1 =~ /^_(?:WARN|DIE):$/) # bug: empty _WARN|_DIE: http://marc.info/?l=amavis-user&m=121725098111422&w=2

            # non-begin anchored
            or ($p1 =~ /result: clean$/)
            or ($p1 =~ /DESTROY called$/)
            or ($p1 =~ /email\.txt no longer exists, can't re-use it/)
            or ($p1 =~ /SPAM\.TAG2/)
            or ($p1 =~ /BAD-HEADER\.TAG2/)
            or ($p1 =~ /: Connecting to socket/)
            or ($p1 =~ /broken pipe \(don't worry\), retrying/)
            or ($p1 =~ /(?:Sending|on dir:) (?:CONT)?SCAN /)
    );

    my ($ip, $from, $to, $key,, $reason, $item,
        $decoder, $scanner, $stage, $sectkey);

    # Coerce older "INFECTED" quarantined lines into "Blocked INFECTED",
    # to be processed in the Passed/Blocked section.
    if ($p1 =~ /^INFECTED.*, quarantine/) {
        $p1 = 'Blocked ' . $p1;
    }

    # SPAM entry occurs at kill level
    # SPAM-TAG entry occurs at log level 2, when spam header is inserted
    # log_level >= 2 || (log_level > 2 && syslog_priority=debug)
    my ($tagtype, $fromto, $isspam, $tags, $tests, $autolearn);

    # amavisd-new 2.7.0 changes SPAM-TAG to Spam-tag and its log_level to 3
    if (($tagtype, $fromto, $isspam, $tags, $tests, $autolearn) = ($p1 =~ /^((?i:SPAM(?:-TAG)?)), (.*), (Yes|No), score=[-+x\d.]+(.*) tests=\[([^\]]*)](?:, autolearn=(\w+))?/) or
        ($tagtype, $fromto, $isspam, $tags, $tests) = ($p1 =~ /^((?i:SPAM(?:-TAG)?)), (.*), (Yes|No), hits=[-+x\d.]+(.*) tests=(.*)(?:, quarantine )?/)) {

        #TD SPAM, <from@example.com> -> <to@sample.com>, Yes, score=17.709 tag=-10 tag2=6.31 kill=6.31 tests=[AWL=-0.678, BAYES_99=4], autolearn=spam, quarantine Cc4+GUJhgpqh (spam-quarantine)
        #TD SPAM, <from@example.com> -> <to@sample.net>, Yes, score=21.161 tag=x tag2=8.15 kill=8.15 tests=[BAYES_99=2.5, FORGED_RCVD_HELO=0.135], autolearn=no, quarantine m6lWPoTGJ2O (spam-quarantine)
        #TD SPAM, <from@example.com> -> <to@sample.net>, Yes, score=17.887 tag=-10 tag2=6.31 kill=6.31 tests=[BAYES_99=4], autolearn=spam, quarantine VFYjDOVTW4zd (spam-quarantine)
        #TD SPAM-TAG, <from@example.com> -> <to@sample.net>, No, score=-0.069 tagged_above=-10 required=6.31 tests=[BAYES_00=-2.599, FROM_ENDS_IN_NUMS=2.53]
        #TD SPAM-TAG, <from@example.com> -> <to@sample.net>, No, score=-1.294 required=8.15 tests=[BAYES_00=-2.599, FROM_LOCAL_HEX=1.305]
        # pre 2.3.3
        #TD SPAM-TAG, <from@example.com> -> <to@sample.net>, Yes, hits=6.159 tagged_above=-999 required=3.4 tests=BAYES_99=3.5, FUZZY_CPILL=0.518, HTML_MESSAGE=0.001, URIBL_WS_SURBL=2.14
        #TD SPAM, <from@example.com> -> <to@sample.net>, Yes, hits=8.1 tag1=-999.0 tag2=7.0 kill=7.0 tests=MANGLED_TAKE, UPPERCASE_25_50, quarantine spam-14156-09 (maia-spam-quarantine)

        $Totals{'tagged'}++ if uc($tagtype) eq 'SPAM-TAG';

        if ($tests) {
            my $type = $isspam =~ /^Y/ ? 'Spam' : 'Ham';

            # Note: A SPAM line may be followed by an almost identical SPAM-TAG line.  To avoid double counting,
            # maintain a list of (abbreviated) SPAM tag lines keyed by pid.  Since pid's are recycled,
            # maintain an approximation of uniqueness by combining several components from the log
            # line (we can't use the date information, as in logwatch, it is not present).
            # XXX: It is safe to delete an entry when the final Passed/Block line occurs

            #TD SPAM, <from@example.com> -> <to@sample.net>, Yes, score=34.939 tag=x tag2=6.31 kill=6.31 tests=[DATE_IN_FUTURE_03_06=1.961], autolearn=disabled
            #TD SPAM-TAG, <from@example.com> -> <to@sample.net>, Yes, score=34.939 required=6.31 tests=[DATE_IN_FUTURE_03_06=1.961]
            #TD SPAM, <from@example.com> -> tod@sample.net>, Yes, score=31.565 tag=x tag2=6.9 kill=6.9 tests=[AV:Sanesecurity.Phishing.Bank.2666.UNOFFICIAL=4.1, AV:Sanesecurity.Phishing.Bank.2666.UNOFFICIAL=4.1, BAYES_99=4, DCC_CHECK=4, DIGEST_MULTIPLE=0.001, FORGED_MUA_OUTLOOK=3.116, FORGED_OUTLOOK_HTML=0.001, FORGED_OUTLOOK_TAGS=0.001, HTML_MESSAGE=0.001, L_AV_SS_Phish=5, MIME_HTML_ONLY=1.457, NORMAL_HTTP_TO_IP=0.001, RAZOR2_CF_RANGE_51_100=2, RAZOR2_CF_RANGE_E4_51_100=1.5, RAZOR2_CF_RANGE_E8_51_100=1.5, RAZOR2_CHECK=3, RDNS_NONE=0.1, URIBL_PH_SURBL=1.787] autolearn=spam


            my $tagstr = $fromto . '/' . $isspam . '/' . $tests;
            if (uc($tagtype) eq 'SPAM-TAG' and exists $spamtags{$pid}) {
                next if ($spamtags{$pid} eq $tagstr);
            }
            $spamtags{$pid} = $tagstr;

            #for (split /=[^,]+(?:, +|$)/, $tests)
            # amavis < 2.6.2 would double list AV names when using
            # @virus_name_to_spam_score_maps.
            my @unique_tests = unique_list(split /, +/, $tests);
            for (@unique_tests) {
                # skip possible trailing junk ("quarantine, ...") when older non-bracked tests=xxx is used
                next if !/[^=]+=[\-.\d]+/;
                my ($id, $val) = split /=/;
                if ($id =~ /^BAYES_\d+$/) {
                    $Counts{'bayes'}{$id}++ if ($Collecting{'bayes'});
                }
                if ($Opts{'sarules'}) {
                    if ($id eq 'DKIM_POLICY_SIGNSOME') {$val = 0}
                    elsif ($id eq 'AWL') {$val = '-'}
                    $Counts{'sarules'}{$type}{sprintf "%6s %s", $val, $id}++;
                }
            }
            # Handled below
            #autolearn= is available only at ll>=3 or SPAM messages; so ham doesn't naturally occur here
            # SA 2.5/2.6 : ham/spam/no
            # SA 3.0+    : ham/spam/no/disabled failed/unavailable
            #$Counts{'autolearn'}{$type}{$autolearn}++    if ($Opts{'autolearn'});
        }
    }

    # Passed or Blocked
    elsif ($p1 =~ /^(Passed|Blocked)(.*)/) {
        $action = lcfirst $1;
        ($p1 = $2) =~ s/^\s+//;

        $p1 =~ s/^,/CLEAN,/; # canonicalize older log entries
        #print "P1: \"$p1\"\n";

        # amavis 20030616p10-5
        #TD Passed, <from@example.com> -> <to@sample.net>, Message-ID: <652.44494541@example.com>, Hits: 4.377
        #TD Passed, <from@example.com> -> <to@sample.net>, Message-ID: <B5C@example.com>, Hits: -
        #TD Passed, <from@example.com> -> <to@sample.net>, quarantine IJHkgliCm2Ia, Message-ID: <20080307140552.16E127641E@example.com>, Hits: 0.633

        #TD Passed CLEAN, [10.0.0.1] [10.0.0.1] <from@example.com> -> <to@sample.net>, Message-ID: <2qxz191@example.com>, mail_id: w4DHD8, Hits: -2.599, size: 3045, queued_as: 2056, 2664 ms
        #TD Passed CLEAN, [10.0.0.1] [10.0.0.1] <from@example.com> -> <to@sample.net>, Message-ID: <2qxz191@example.com>, mail_id: w4DHD8, Hits: -2.541-3, size: 3045, queued_as: 2056, 2664 ms
        #TD Blocked SPAM, [10.0.0.1] [192.168.0.1] <bogus@example.com> -> <to@sample.net>, quarantine: spam-EzEbE9W, Message-ID: <117894@example.com>, mail_id: EzEbE9W, Hits: 6.364, size: 16493, 6292 ms
        #TD Blocked SPAM, LOCAL [10.0.0.1] [10.0.0.2] <bogus@example.com> -> <to@sample.net>, quarantine: spam-EzEbE9W, Message-ID: <110394@example.com>, mail_id: EzEbE9W, Hits: 6.364, size: 16493, 6292 ms
        #TD Blocked SPAM, [IPv6:2001:630:d0:f102:230:48ff:fe77:96e] [192.168.0.1] <joe@example.com> -> <user@sample.net>, quarantine: spam-EzEbE9W, Message-ID: <11780394@example.com>, mail_id: EzEbE9W, Hits: 6.364, size: 16493, 6292 ms
        #TD Passed SPAMMY, ORIGINATING/MYNETS LOCAL [10.0.0.1] [10.0.0.1] <from@example.com> -> <to1@sample.net>,<to2@sample.net>, quarantine: spam-EzEbE9W, Message-ID: <11780394@example.com>, mail_id: EzEbE9W, Hits: 6.364, size: 16493, 6292 ms
        #TD Blocked SPAM, B-BANK/C-BANK/B-BANK [10.0.0.1] [10.0.0.1] <from@sample.net> -> <to@example.com>, quarantine: spam-EzEbE9W, Message-ID: <11780394@example.com>, mail_id: EzEbE9W, Hits: 6.364, size: 16493, 6292 ms
        #TD Blocked SPAM, [10.0.0.1] [10.0.0.1] <from@example.com> -> <to@sample.net>, quarantine: spam-AV49p5, Message-ID: <1.007@sample.net>, mail_id: AV49p5, Hits: 7.487, size: 27174, 4406 ms
        #TD Passed SPAM, MYNETS <root@example.com> -> <root@example.com>, quarantine: spam-V3Wq, Message-ID: <220.1B@example.com>, mail_id: V3Wq, Hits: 7, size: 8838, queued_as: C63EC, 18 ms
        #TD Passed SPAM, <> -> <"fred).flintstone"@domain.tld>, Message-ID: <200801180104.CAA23669@aserver.sub.adomain.tld>, mail_id: 6AzQ1g0l5RgP, Hits: 9.061, size: 5555, queued_as: C1840506CB8, 8766 ms
        #TD Blocked INFECTED (HTML.Phishing.Bank-43), [198.168.0.1] [10.0.0.1] <bogus@example.com> -> <to@sample.net>, quarantine: virus-SCwJcs, Message-ID: <509@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Blocked INFECTED (Trojan.Downloader.Small-9993), LOCAL [10.0.0.2] [10.0.0.2] <bogus@example.net> -> <to@example.com>, quarantine: virus-SCwJcs, Message-ID: <9009@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Blocked BANNED (multipart/report | message/partial,.txt), [192.168.0.1] [10.0.0.2] <> -> <someuser@sample.net>, quarantine: virus-SCwJcs, Message-ID: <509@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Blocked BANNED (multipart/report | message/partial,.txt), LOCAL [192.168.0.1] [10.0.0.2] <> -> <someuser@sample.net>, quarantine: virus-SCwJcs, Message-ID: <509@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Blocked BANNED (multipart/mixed | application/octet-stream,.asc,=?iso-8859-1?Q?FTP=5FFile=5F (1)=File(1).reg), [192.168.0.0] [192.168.0.0] <from@example.com> -> <to@sample.us>, quarantine: virus-SCwJcs, Message-ID: <509@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Blocked BANNED (multipart/related | application/zip,.zip,card.zip | .exe,.exe-ms,Card.exe), [10.0.0.2] [10.0.0.2] <from@example.com> -> <to@sample.net>, quarantine: banned-9OXm4Q3ah, Message-ID: <08517$@from>, mail_id: 9OXm4Q3ah, Hits: -, size: 2366, 3803 ms
        #TD Passed BAD-HEADER, [192.168.0.1] [10.0.0.2] <bogus@example.com> -> <someuser@sample.net>, quarantine: virus-SCwJcs, Message-ID: <df@acm.org>, mail_id: SCwJcs, Hits: 2.54 size: 4134, 3721 ms
        #TD Passed BAD-HEADER, LOCAL [192.168.0.1] [10.0.0.2] <bogus@example.com> -> <someuser@sample.net>, quarantine: virus-SCwJcs, Message-ID: <df@acm.org>, mail_id: SCwJcs, Hits: 3.2 size: 4134, 3721 ms
        #TD Passed BAD-HEADER, MYNETS AM.PDP [127.0.0.1] [127.0.0.1] <bogus@example.com> -> <someuser@sample.net>, quarantine: virus-SCwJcs, Message-ID: <df@acm.org>, mail_id: SCwJcs, Hits: 1.2 size: 4134, 3721 ms
        #TD Passed BAD-HEADER, ORIGINATING/MYNETS LOCAL [10.0.0.1] [10.0.0.1] <from@sample.net> -> <to1@sample.net>,<to2@sample.net>,<to3@example.com>, quarantine: virus-SCwJcs, Message-ID: <df@acm.org>, mail_id: SCwJcs, Hits: -, size: 4134, 3721 ms
        #TD Passed BAD-HEADER, [10.0.0.1] [10.0.0.2] <from@example.com> -> <to@sample.net>, quarantine: badh-lxR, Message-ID: <7fm@example.com>, mail_id: lxR, Hits: -2.292, size: 422, queued_as: E3B, 981 ms
        #TD Passed UNCHECKED, MYNETS LOCAL [192.168.0.1] [192.168.0.1] <from@sample.net> -> <to@example.com> Message-ID: <002e01c759c7$5de437b0$0a02a8c0@somehost>, mail_id: 7vtR-7BAvHZV, Hits: -, queued_as: B5420C2E10, 6585 ms
        #TD Blocked MTA-BLOCKED, LOCAL [192.168.0.1] [192.168.0.2] <from@example.com> -> <to@sample.net>, Message-ID: <438548@example.com>, mail_id: tfgTCiyvFw, Hits: -2.54, size: 4895, 31758 ms
        #TD Blocked OVERSIZED, LOCAL [10.0.0.1] [10.0.0.1] <f@example.com> -> <t@sample.net>, Message-ID: <435@example.com>, mail_id: tfTivFw, Hits: -2.54, size: 444444895, 31758 ms
        #TD Blocked OTHER, LOCAL [10.0.0.1] [10.0.0.1] <f@example.com> -> <t@sample.net>, Message-ID: <435@example.com>, mail_id: tfTivFw, Hits: -2.54, size: 495, 31758 ms
        #TD Blocked TEMPFAIL, [10.0.0.2] [10.0.0.1] <user@example.com> -> <to@sample.net>, Message-ID: <200703302301.9f1899470@example.com>, mail_id: bgf52ZCNbPo, Hits: -2.586, 3908 ms

        #2.3.1
        #<>,<info@example.com>,Passed,Hits=-3.3,Message-ID=<200506440.1.sample.net>,Size=51458
        #20030616p10-5
        #Not-Delivered, <from@example.com> -> <to@localhost>, quarantine spam-ea32770-03, Message-ID: <BAA618FE2CB585@localhost>, Hits: 9.687

        # malwarepassed, malwareblocked
        # xxx very old
        # Virus found - quarantined|
        #amavisd-new-20030616
        # INFECTED (JS/IllWill-A), <from@[127.0.0.1]> -> <to@sample.net>, quarantine virus-20040811-207-0-03, Message-ID: <0440.5577-101@sample.net>, Hits: -
        # INFECTED (Exploit.HTML.IFrame, Worm.SomeFool.P), <from@sample.net> -> <to@example.com>,<to2@example.com>, quarantine qiO2ZG4K, Message-ID: <200608.5A5@mail.example.com>, Hits: -
        #XXX (?:(Passed|Blocked) )?INFECTED \(([^\)]+)\),[A-Z .]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [<(]([^>)]*)[>)] -> [(<]([^(<]+)[)>]/o ))
        #XXX elsif (($action, $key, $ip, $from, $to) = ( $p1 =~ /^(?:Virus found - quarantined|(?:(Passed|Blocked) )?INFECTED) \(([^\)]+)\),[A-Z .]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [<(]([^>)]*)[>)] -> [(<]([^(<]+)[(>]/o ))

        # the first IP is the envelope sender.
        if ($p1 !~ /^(CLEAN|SPAM(?:MY)?|INFECTED \(.*?\)|BANNED \(.*?\)|BAD-HEADER(?:-\d)?|UNCHECKED|MTA-BLOCKED|OVERSIZED|OTHER|TEMPFAIL)(?: \{[^}]+\})?, ([^[]+ )?(?:([^<]+) )?[<(](.*?)[>)] -> ([(<].*?[)>]), (?:.*Hits: ([-+.\d]+))(?:.* size: (\d+))?(?:.* autolearn=(\w+))?/) {
            inc_unmatched('passblock');
            next;
        }
        my $trigger;
        my ($ccatmajor, $pbanks, $ips, $from, $reciplist, $hits, $size, $autolearn) = ($1, $2, $3, $4, $5, $6, $7, $8);

        $Totals{'bytesscanned'} += $size if defined $size;

        #print "ccatmajor: \"$ccatmajor\", pbanks: \"$pbanks\"\n";
        if ($ccatmajor =~ /^(INFECTED|BANNED) \((.*)\)$/) {
            ($ccatmajor, $trigger) = ($1, $2);
            #print "\tccatmajor: \"$ccatmajor\", trigger: \"$trigger\"\n";
        }

        $ccatmajor =~ s/(BAD-HEADER)-\d/$1/; # strip amavis 2.7's [:ccat|minor] BAD-HEADER sub-classification
        $sectkey = $ccatmajor_to_sectkey{$ccatmajor} . $action;
        $Totals{$sectkey}++;

        # Not checked by spamassassin, due to $sa_mail_body_size_limit or @bypass_spam_checks_maps
        if ($hits eq '-') {
            # Don't increment sabypassed for INFECTED (SA intentionally not called)
            unless ($ccatmajor eq 'INFECTED') {
                # The following order is used, the first condition met decides the outcome:
                #  1. a virus is detected: mail is considered infected;
                #  2. contains banned name or type: mail is considered banned;
                #  3. spam level is above kill level for at least one recipient, or a sender is blacklisted: mail is considered spam;
                #  4. bad (invalid) headers: mail is considered as having a bad header.
                # Priority: VIRUS BANNED UNCHECKED SPAM SPAMMY BADH OVERSIZED MTA CLEAN
                $Totals{'sabypassed'}++;
            }
        }
        else {
            if ($Collecting{'spamscores'}) {
                no re 'taint';
                if ($hits =~ /^(-?[.\d]+)([-+])([.\d]+)$/) {
                    $hits = eval $1 . $2 . $3; # untaint $hits, to sum $1 and $3 values
                }
                # SA not called for ccats INFECTED and BANNED (Hits: -).
                # UNCHECKED may have a score, so we can't distinguish Ham from Spam
                push @{$SpamScores{$ccatmajor_to_spamham{$ccatmajor}}}, $hits;
            }
        }

        # autolearn is available here only if enabled in amavis template
        if ($autolearn ne '' and $Opts{'autolearn'}) {
            #if ($autolearn ne '' and ($ccatmajor eq 'SPAM' or $ccatmajor eq 'CLEAN')) {
            #  SA 2.5/2.6 : ham/spam/no
            #  SA 3.0+    : ham/spam/no/disabled/failed/unavailable
            # printf "INC: autolearn: %s, %s: %d\n", $ccatmajor eq 'SPAM' ? 'Spam' : 'Ham', $autolearn, $Opts{'autolearn'};;
            # Priorities other than SPAM will be considered HAM for autolearn stats
            $Counts{'autolearn'}{$ccatmajor eq 'SPAM' ? 'Spam' : 'Ham'}{$autolearn}++;
        }

        # p0f fingerprinting
        if (exists $p0ftags{$pid}) {
            my ($ip, $score, $os) = split(/\//, $p0ftags{$pid});
            $Counts{'p0f'}{ucfirst($ccatmajor_to_spamham{$ccatmajor})}{$os}{$ip}++;
            #print "Deleting p0ftag: $pid\n";
            delete $p0ftags{$pid};
        }

        next unless ($Collecting{$sectkey});
        # cleanpassed never gets here...

        # prefer xforward IP if it exists
        # $ip_a => %a  original SMTP session client IP address (empty if unknown, e.g. no XFORWARD)
        # $ip_e => %e  best guess of the originator IP address collected from the Received trace
        my ($ip_a, $ip_e) = split(/ /, $ips, 2);

        $ip = $ip_a ? $ip_a : $ip_e;
        $ip =~ s/[[\]]//g;
        #print "ip: \"$ip\", ip_a: \"$ip_a\", ip_e: \"$ip_e\", from: \"$from\", reciplist: \"$reciplist\"; hits: \"$hits\"\n";
        $ip = '*unknown IP' if ($ip eq '');
        $from = '<>' if ($from eq '');

        # Show first recipient only, or all
        my @recips = split /,/, $reciplist;
        @recips = map {/^<(.+)>$/} @recips;
        # show only first recipient
        $to = lc($Opts{'first_recip_only'} ? $recips[0] : "@recips");

        if ($ccatmajor eq 'INFECTED') { # $ccatmajor: INFECTED  malwarepassed, malwareblocked
            $Counts{$sectkey}{$trigger}{$to}{$ip}{$from}++;
        }
        elsif ($ccatmajor eq 'BANNED') { # $ccatmajor: BANNED  bannednamepassed, bannednameblocked
            $Counts{$sectkey}{$to}{$trigger}{$ip}{$from}++;
        }
        else {
            # $ccatmajor: CLEAN | SPAM{MY} | BAD-HEADER | UNCHECKED | MTA-BLOCKED | OVERSIZED | OTHER | TEMPFAIL
            # cleanpassed, cleanblocked, spampassed, spamblocked, badheaderpassed, badheaderblocked
            # uncheckedpassed, uncheckblocked, mtapassed, mtablocked, oversizedpassed, oversizedblocked
            # otherpassed, otherblocked, tempfailpassed, tempfailblocked
            $Counts{$sectkey}{$to}{$ip}{$from}++;
        }

        # old...
        #XXX elsif (($action, $item, $ip, $from, $to) = ( $p1 =~ /^(?:(Blocked|Passed) )?BANNED (?:name\/type )?\((.+)\),[^[]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [<(]([^>)]*)[>)] -> [(<]([^(<]+)[(>]/o))
        #XXXX  elsif (($action, $ip, $from, $to) = ( $p1 =~ /^(?:(Passed|Blocked) )?UNCHECKED,[^[]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [<(]([^>)]*)[>)] -> [(<]([^>)]*)[)>]/o ))
        #XXX elsif (($action, $ip, $from, $to) = ( $p1 =~ /^(?:(Passed|Blocked) )?TEMPFAIL,[^[]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [<(]([^>)]*)[>)] -> [(<]([^>)]*)[)>]/o ))
        #XXX elsif (($action, $ip, $from, $to) = ( $p1 =~ /^(?:(Blocked|Passed) )?BAD-HEADER,[^[]*(?: \[($re_IP)\])?(?: \[$re_IP\])* [(<]([^>)]*)[)>](?: -> [(<]([^>)]+)[)>])[^:]*/o ))
        # amavis 2.3.1
        #BAD-HEADER, <> -> <info@example.com>, Message-ID: <200506440.1.sample.net>, Hits=-3.3 tag1=3.0 tag2=7.5 kill=7.5, tests=ALL_TRUSTED=-3.3, [10.0.0.1]
    } # end Passed or Blocked

    # MAIA
    elsif ($p1 =~ /^FAKE SENDER, ([^:]+): ($[^,]+), (.*)$/o) {
        #TD FAKE SENDER, SPAM: 192.168.0.1, bogus@example.com
        $Totals{'fakesender'}++;
        next unless ($Collecting{'fakesender'});
        $Counts{'fakesender'}{$1}{$2}{$3}++;
    }
    elsif ($p1 =~ /^p\d+ \d+(?:\/\d+)* Content-Type: ([^,]+)(?:, size: [^,]+, name: (.*))?/) {
        my ($ts, $name) = ($1, $2);
        #TD p006 1 Content-Type: multipart/mixed
        #TD p008 1/1 Content-Type: multipart/signed
        #TD p001 1/1/1 Content-Type: text/plain, size: 460 B, name:
        #TD p002 1/1/2 Content-Type: application/pgp-signature, size: 189 B, name:
        #TD p002 1/2 Content-Type: application/octet-stream, size: 3045836 B, name: abc.pdf
        next unless ($Collecting{'contenttype'});
        my ($type, $subtype) = $ts !~ '""' ? split /\//, $ts : ('unspecified', 'unspecified');

        $name = '' if !defined $name or $name =~ /^\s*$/;
        $Counts{'contenttype'}{$type}{$subtype}{$name}++;
    }

    # LMTP/SMTP connection
    # NOTE:  no longer used. size data now being obtained from Passed/Block line, as size info may not be available here
    #elsif (my ($size) = ($p1 =~ /^[LS]MTP:(?:\[$re_IP\])?:\d+ [^:]+: [<(](?:.*?)[>)] -> \S+ (?:SIZE=(\d+))?.*?Received: / )) {
    elsif ($p1 =~ /^[LS]MTP ?:/) {
        #TD LMTP::10024 /var/spool/amavis/tmp/amavis-20070119T144757-09086: <from@example.com> -> <to@sample.net> SIZE=1000 Received: from mail.sample.net ([127.0.0.1]) by localhost (mail.sample.net [127.0.0.1]) (amavisd-new, port 10024) with LMTP for <to@sample.net>; Fri, 19 Jan 2007 15:41:45 -0800 (PST)
        #TD SMTP:[127.0.0.1]:10024 /var/spool/amavis/tmp/amavis-20070119T144757-09086: <from@example.com> -> <to@sample.net>,<recip@sample.net> SIZE=2500000 Received: from mail.sample.net ([127.0.0.1]) by localhost (mail.sample.net [127.0.0.1]) (amavisd-new, port 10024) with LMTP for <to@sample.net>; Fri, 19 Jan 2007 15:41:45 -0800 (PST)
        #TD SMTP::10024 /var/lib/amavis/tmp/amavis-27-26927: <from@example.com> -> <to@example.net> Received: from localhost ([127.0.0.1]) by localhost (example.com [127.0.0.1]) (amavisd-new, port 10024) with SMTP for <to@example.net>; Sat,  7 Jun 2008 23:09:34 +0200 (CEST)
        #$Totals{'bytesscanned'} += $size  if defined $size;
    }

    #(\S+) ([^[(]+)(.*)$
    elsif ($p1 =~ /^OS_fingerprint: (\S+) ([-\d.]+) (\S+)(?: ([^[(]+|\[[^]]+\]))?/o) {
        #TD OS_fingerprint: 213.193.24.113 29.789 Linux 2.6 (newer, 1) (up: 1812 hrs), (distance 14, link: ethernet/modem)
        #TD OS_fingerprint: 10.47.2.155 -1.312 MYNETWORKS
        # Note: safe to delete entry when the final Passed/Block line occurs
        if ($Collecting{'p0f'}) {
            my ($genre, $vers) = ($3, $4);
            #print "p0f:\t$3\t\t$vers\n";
            if ($genre eq 'Windows') {
                local ($1);
                $vers = $1 if $vers =~ /^(\S+) /;
                $genre .= ' ' . $vers;
            }
            elsif ($genre eq 'UNKNOWN') {
                $genre = 'Unknown';
            }
            $p0ftags{$pid} = join('/', $1, $2, $genre);
            #print "Added PID: $pid, $p0ftags{$pid}\n";
        }
    }
    elsif (($reason) = ($p1 =~ /^BAD HEADER from [^:]+: (.+)$/) or
        ($reason) = ($p1 =~ /check_header: \d, (.+)$/)) {
        # When log_level > 1, provide additional header or MIME violations

        # amavisd < 2.4.0, log_level >= 1
        #TD BAD HEADER from <bogus@example.com>: Improper use of control character (char 0D hex) in message header 'Received': Received: example.com[10.0.0.1\r]
        #TD BAD HEADER from <bogus@example.com>: Non-encoded 8-bit data (char F7 hex) in message header 'Subject': Subject: \367\345\370\361 \344\351\351\362\345\365\n
        #TD BAD HEADER from <bogus@example.com>: MIME error: error: part did not end with expected boundary
        #TD BAD HEADER from (bulk ) <bogus@bounces@lists.example.com>: Non-encoded 8-bit data (char E6 hex) in message header 'Subject': Subject: spam\\346ham\\n
        #TD BAD HEADER from (list) <bogus@bounces@lists.example.com>: MIME error: error: part did not end with expected boundary
        #  amavisd >= 2.4.3, log_level >= 2
        #TD check_header: 2, Non-encoded 8-bit data (char AE hex): Subject: RegionsNet\\256 Online Banking\\n
        #TD check_header: 2, Non-encoded 8-bit data (char E1 hex): From: "any user" <from\\341k@example.com>\\n
        #TD check_header: 3, Improper use of control character (char 0D hex): Content-type: text/html; charset=i...
        #TD check_header: 8, Duplicate header field: "Reply-To"
        #TD check_header: 8, Duplicate header field: "Subject"
        #TD check_header: 4, Improper folded header field made up entirely of whitespace (char 09 hex): X-Loop-Detect: 3\\n\\t\\n
        #TD check_header: 4, Improper folded header field made up entirely of whitespace: Received: ...8 ;         Thu, 10 Jan 2008 03:41:35 +0100\\n\\t \\n


        my $subreason;
        if ($reason =~ /^(.*?) \((char \S+ hex)\)(.*)$/) {
            $reason = $1;
            my ($char, $sub) = ($2, $3);

            $sub =~ s/^in message header '[^:]+': //;
            $sub =~ s/^: //;
            $subreason = "$char: $sub";
        }
        elsif ($reason =~ /^(Improper folded header field made up entirely of whitespace):? (.*)/) {
            $reason = $1;
            $subreason = $2;
        }
        elsif ($reason =~ /^(Duplicate header field): "(.+)"$/) {
            $reason = $1;
            $subreason = $2;
        }
        elsif ($reason =~ /^(MIME error): (?:error: )?(.+)$/) {
            $reason = $1;
            $subreason = $2;
        }

        $Totals{'badheadersupp'}++;
        next unless ($Collecting{'badheadersupp'});
        $Counts{'badheadersupp'}{$reason}{$subreason}++;
    }
    elsif ($p1 =~ /^truncating a message passed to SA at/) {
        #TD truncating a message passed to SA at 431018 bytes, orig 1875912
        $Totals{'truncatedmsg'}++;
    }
    elsif ($p1 =~ /: spam level exceeds quarantine cutoff level/ or
        $p1 =~ /: cutoff, blacklisted/) {
        #TD do_notify_and_quarantine: spam level exceeds quarantine cutoff level 20
        #TD do_notify_and_quarantine: cutoff, blacklisted
        $Totals{'spamdiscarded'}++;
    }
    elsif ($p1 =~ /^spam_scan: (.*)$/) {
        #if ($1 =~ /^not wasting time on SA, message longer than/ ) {
        #TD spam_scan: not wasting time on SA, message longer than 409600 bytes: 1326+4115601
        # this causes duplicate counts, and the subsequent Passed/Blocked log line
        # will have "Hits: -," whereby sabypassed is incremented.
        #$Totals{'sabypassed'}++;
        #}
        # ignore other spam_scan lines
    }

    # WARN:
    elsif (($reason) = ($p1 =~ /^WARN: MIME::Parser error: (.*)$/)) {
        # WARN: MIME::Parser error: unexpected end of header
        $Totals{'mimeerror'}++;
        next unless ($Collecting{'mimeerror'});
        $Counts{'mimeerror'}{$reason}++;
    }
    elsif ($p1 =~ /^WARN: address modified \((\w+)\): <(.*?)> -> <(.*)>$/) {
        #TD WARN: address modified (sender): <root> -> <root@>
        #TD WARN: address modified (recip): <root> -> <root@>
        #TD WARN: address modified (recip): <postmaster> -> <postmaster@>
        #TD WARN: address modified (recip): <"test@example.com"@> -> <"teszt@example.com">
        #TD WARN: address modified (sender): <fr\344om@sample.net> -> <"fr\344om"@sample.net>
        $Totals{'warningaddressmodified'}++;
        next unless ($Collecting{'warningaddressmodified'});
        $Counts{'warningaddressmodified'}{$1 eq 'sender' ? "Sender address" : "Recipient address"}{"$2 -> $3"}++;
    }

    # NOTICE:
    elsif ($p1 =~ /^NOTICE: (.*)$/) {
        # uninteresting
        #TD NOTICE: reconnecting in response to: err=2006, HY000, DBD::mysql::st execute failed: MySQL server has gone away at (eval 71) line 166, <GEN168> line 4.
        next if ($1 =~ /^Disconnected from SQL server/); # redundant
        next if ($1 =~ /^do_search: trying again: LDAP_OPERATIONS_ERROR/);
        next if ($1 =~ /^reconnecting in response to: /);

        if ($1 =~ /^Not sending DSN, spam level ([\d.]+ )?exceeds DSN cutoff level/) {
            #TD NOTICE: Not sending DSN, spam level exceeds DSN cutoff level for all recips, mail intentionally dropped
            $Totals{'dsnsuppressed'}++;
            $Counts{'dsnsuppressed'}{'DSN cutoff exceeded'}++;
        }
        elsif ($1 =~ /^Not sending DSN to believed-to-be-faked sender/) {
            #TD NOTICE: Not sending DSN to believed-to-be-faked sender <user@example.com>, mail containing VIRUS intentionally dropped
            $Totals{'dsnsuppressed'}++;
            $Counts{'dsnsuppressed'}{'Sender likely faked'}++;
        }
        elsif ($1 =~ /^DSN contains [^;]+; bounce is not bounc[ai]ble, mail intentionally dropped/) {
            $Totals{'dsnsuppressed'}++;
            $Counts{'dsnsuppressed'}{'Not bounceable'}++;
        }
        elsif ($1 =~ /^UNABLE TO SEND DSN to /) {
            #TD NOTICE: UNABLE TO SEND DSN to <user@example.com>: 554 5.7.1 Failed, id=19838-01, from MTA([127.0.0.1]:10025): 554 5.7.1 <user@example.com>: Recipient address rejected: Access denied
            $Totals{'dsnsuppressed'}++;
            $Counts{'dsnsuppressed'}{'Unable to send'}++;
        }
        elsif ($1 =~ /^Skipping (?:bad|extra) output from file\(1\)/) {
            #TD NOTICE: Skipping extra output from file(1): blah
            #TD NOTICE: Skipping bad output from file(1) at [1, p002], got: blah
            $Totals{'fileoutputskipped'}++;
        }
        elsif (($p1) = ($1 =~ /^Virus scanning skipped: (.*)$/)) {
            #TD NOTICE: Virus scanning skipped: Maximum number of files (1500) exceeded at (eval 57) line 1283, <GEN212> line 1501.
            $Totals{'virusscanskipped'}++;
            next unless ($Collecting{'virusscanskipped'});
            $Counts{'virusscanskipped'}{strip_trace($p1)}++;
        }
        else {
            inc_unmatched('NOTICE');
            next;
        }
    }

    # INFO:
    elsif ($p1 =~ /^INFO: (.*)$/) {
        next if ($1 =~ /^unfolded \d+ illegal all-whitespace continuation line/);
        next if ($1 =~ /^removed bare CR/);

        if ($1 =~ /^truncat(ed|ing)/) {
            #TD INFO: truncating long header field (len=2639): X-Spam-Report: =?iso-8859-1?Q?=0A=0A*__1=2E7_SUBJECT=5FENCODED=5FTWICE_Subject=3A_MIME_e?= =?iso-885...
            #TD INFO: truncated 1 header line(s) longer than 998 characters
            $Totals{'truncatedheader'}++;
        }
        elsif ($1 =~ /^no existing header field 'Subject', inserting it/) {
            $Totals{'nosubject'}++;
        }
        elsif (my ($savers1, $savers2, $item) = ($1 =~ /^(?:SA version: ([^,]+), ([^,]+), )?no optional modules: (.+)$/)) {
            #TD INFO: SA version: 3.1.8, 3.001008, no optional modules: DBD::mysql Mail::SpamAssassin::Plugin::DKIM Mail::SpamAssassin::Plugin::URIDetail Error
            next unless ($Opts{'startinfo'});
            if ($savers1 ne '') {
                $StartInfo{'sa_version'} = "$savers1 ($savers2)";
            }
            foreach my $code (split / /, $item) {
                $StartInfo{'Code'}{'Not loaded'}{$code} = "";
            }
        }
        elsif (my ($name) = ($1 =~ /^(unknown banned table name \S+), .+$/)) {
            #TD INFO: unknown banned table name 1, recip=r@example.com
            $Totals{'warning'}++;
            next unless ($Collecting{'warning'});
            $Counts{'warning'}{ucfirst $name}++;
        }
        else {
            inc_unmatched('INFO');
            next;
        }
    }
    elsif (($action, $reason, $from, $to) = ($p1 =~ /^DSN: NOTIFICATION: Action:([^,]+), ([^,]+), <(.*?)> -> <(.*?)>/)) {
        #TD DSN: NOTIFICATION: Action:failed, LOCAL 554 Banned, <from@example.net> -> <to@example.com>
        #TD DSN: NOTIFICATION: Action:delayed, LOCAL 454 Banned, <from@example.com> -> <to@example.net>

        $Totals{'dsnnotification'}++;
        next unless ($Collecting{'dsnnotification'});
        $Counts{'dsnnotification'}{$action}{$reason}{"$from -> $to"}++;
    }
    elsif (($item, $from, $to) = ($p1 =~ /^Quarantined message release(?: \([^)]+\))?: ([^ ]+) <(.*?)> -> (.+)$/) or
        ($item, $from, $to) = ($p1 =~ /^Quarantine release ([^ ]+): overriding recips <([^>]*)> by (.+)$/)) {
        #TD Quarantine release arQcr95dNHaW: overriding recips <TO@EXAMPLE.COM> by <to@example.com>
        #TD Quarantined message release: hiyPJOsD2m9Z <from@sample.net> -> <to@example.com>
        #TD Quarantined message release: hiyPJOsD2m9Z <> -> <to@recipient.maildir>,<anyone@example.com>
        # 2.6+
        #TD Quarantined message release (miscategorized): Iu6+0u1voOA <from@example.com> -> <to@example.net>
        $Totals{'released'}++;
        next unless ($Collecting{'released'});
        $from = '<>' if ($from eq '');
        $to =~ s/[<>]//g;
        $Counts{'released'}{"\L$from"}{$to}{$item}++;
    }
    elsif ($p1 =~ /^Quarantine release ([^:]+): missing X-Quarantine-ID$/) {
        #TD Quarantine release 7ejEBC7MThSc: missing X-Quarantine-ID
        $Totals{'warningnoquarantineid'}++;
        next unless ($Collecting{'warningnoquarantineid'});
        $Counts{'warningnoquarantineid'}{$1}++;
    }
    elsif (($stage, $reason) = ($p1 =~ /^Negative SMTP resp\S* +to ([^:]+): *(.*)$/)) {
        #TD Negative SMTP response to data-dot (<u@example.com>): 550 5.7.1 Header Spam Rule 4
        $Totals{'smtpresponse'}++;
        next unless ($Collecting{'smtpresponse'});
        $Counts{'smtpresponse'}{'Negative response'}{$stage}{$reason}++;
    }
    elsif (($stage, $reason) = ($p1 =~ /^smtp resp to ([^:]+): *(.*)$/)) {
        #TD smtp resp to NOOP (idle 4799.4 s): 421 4.4.2 nops.overtops.org Error: timeout exceeded
        #TD smtp resp to MAIL (pip): 250 2.1.0 Ok
        $Totals{'smtpresponse'}++;
        next unless ($Collecting{'smtpresponse'});
        $stage =~ s/ [\d.]+ s//;
        $Counts{'smtpresponse'}{'Response'}{$stage}{$reason}++;
    }
    elsif (($item) = ($p1 =~ /^response to RCPT TO for <([^>]*)>: "501 Bad address syntax"/)) {
        #TD response to RCPT TO for <""@example.com>: "501 Bad address syntax"
        $Totals{'badaddress'}++;
        next unless ($Collecting{'badaddress'});
        $Counts{'badaddress'}{$item}++;
    }

    # do_unip: archive extraction
    elsif ($p1 =~ s/^do_unzip: \S+, //) {
        $Totals{'archiveextract'}++;
        next unless ($Collecting{'archiveextract'});

        if ($p1 =~ s/^\d+ members are encrypted, //) {
            #TD do_unzip: p003, 4 members are encrypted, none extracted, archive retained
            $Counts{'archiveextract'}{'Encrypted'}{$p1}++;

        }
        elsif ($p1 =~ /^zero length members, archive retained/) {
            #TD do_unzip: p002, zero length members, archive retained
            $Counts{'archiveextract'}{'Empty member'}{''}++;

        }
        elsif ($p1 =~ s/^unsupported compr\. method: //) {
            #TD do_unzip: p003, unsupported compr. method: 99
            $Counts{'archiveextract'}{'Unsupported compression'}{$p1}++;
        }
        else {
            $Counts{'archiveextract'}{'*unknown'}{$p1}++;
        }
    }

    # do_cabextract: archive extraction
    elsif ($p1 =~ s/^do_cabextract: //) {
        #TD do_cabextract: can't parse toc line:  File size | Date       Time     | Name
        #TD do_cabextract: can't parse toc line: All done, no errors.
        $Totals{'archiveextract'}++;
        next unless ($Collecting{'archiveextract'});

        if ($p1 =~ /^([^:]+):\s*(.*)/) {
            $Counts{'archiveextract'}{"\u$1"}{$2}++;
        }
        else {
            $Counts{'archiveextract'}{$p1}{''}++;
        }
    }
    elsif ($p1 =~ /^(?:\(!\) *)?SA TIMED OUT,/) {
        $Totals{'satimeout'}++;
    }
    elsif ($p1 =~ /^mangling (.*)$/) {
        $p1 = $1;
        if ($p1 =~ /^by (.+?) failed: (.+?), mail will pass unmodified$/) {
            #TD mangling by altermime failed: SomeText, mail will pass unmodified
            $Totals{'defangerror'}++;
            next unless ($Collecting{'defangerror'});
            $Counts{'defangerror'}{$1}{$2}++;
        }
        # other mangle message skipped
        else {
            #TD mangling YES: 1 (orig: 1), discl_allowed=0, <from@example.com> -> <to@sample.net>
            #TD mangling by built-in defanger: 1, <user@example.com>
            next;
        }
    }
    elsif ($p1 =~ /^DEFANGING MAIL: (.+)$/) {
        # log_level 1
        #TD DEFANGING MAIL: WARNING: possible mail bomb, NOT CHECKED FOR VIRUSES:\n  Exceeded storage quota 5961070 bytes by d...
        #TD DEFANGING MAIL: WARNING: bad headers - Improper use of control character (char 0D hex): To: <to@example.com\\r>,\\n\\t<to@example.com>
        # could use instead...
        #do_log(1,"mangling by %s (%s) done, new size: %d, orig %d bytes", $actual_mail_mangle, $mail_mangle, $repl_size, $msginfo->msg_size);
        $Totals{'defanged'}++;
        next unless ($Collecting{'defanged'});
        $Counts{'defanged'}{$1}++;
    }
    elsif ($p1 =~ /^PenPalsSavedFromKill [-.\d]+,/) {
        #TD PenPalsSavedFromKill 8.269-3.160, <ulyanov@steelpro.com.ua> -> <recipient1@recipientdomain.com>
        $Totals{'penpalsaved'}++;
    }

    # I don't know how many variants of time outs there are... I suppose we'll fix as we go
    elsif (($p1 =~ /^\(!+\)([^ ]*) is taking longer than \d+ s and will be killed/) or
        ($p1 =~ /^\(!+\)(.*) av-scanner FAILED: timed out/) or
        ($p1 =~ /^(?:\(!+\))?(.*): timed out/)) {
        #TD (!)/usr/local/bin/uvscan is taking longer than 10 s and will be killed
        #TD (!!)NAI McAfee AntiVirus (uvscan) av-scanner FAILED: timed out
        #TD ClamAV-clamd: timed out, retrying (1)
        #TD (!)Sophie: timed out, retrying (2)

        $Totals{'avtimeout'}++;
        next unless ($Collecting{'avtimeout'});
        $Counts{'avtimeout'}{$1}++;
    }
    elsif (($p2) = ($p1 =~ /SMTP shutdown: (.*)$/)) {
        # log level -1
        #TD SMTP shutdown: Error writing a SMTP response to the socket: Broken pipe at (eval 49) line 836, <GEN232> line 51.
        #TD SMTP shutdown: tempdir is to be PRESERVED: /var/amavis/tmp/amavis-20070704T095350-13145
        strip_trace($p2);
        if ($p2 =~ /^tempdir is to be PRESERVED: (.*)\/([^\/]+)$/) {
            $Totals{'tmppreserved'}++;
            $Counts{'tmppreserved'}{$1}{$2}++ if ($Collecting{'tmppreserved'});
            $p2 = "Preserved tempdir in $1";
        }
        $Totals{'warningsmtpshutdown'}++;
        next unless ($Collecting{'warningsmtpshutdown'});
        $Counts{'warningsmtpshutdown'}{ucfirst($p2)}++;
    }
    elsif (($p1 =~ /PRESERVING EVIDENCE in (.*)\/([^\/]+)$/) or
        ($p1 =~ /tempdir is to be PRESERVED: (.*)\/([^\/]+)$/)) {
        #TD (!)TempDir removal: tempdir is to be PRESERVED: /var/amavis/tmp/amavis-20080110T173606-05767
        # log level -1
        #TD PRESERVING EVIDENCE in /var/amavis/tmp/amavis-20070704T111558-14883
        $Totals{'tmppreserved'}++;
        next unless ($Collecting{'tmppreserved'});
        $Counts{'tmppreserved'}{$1}{$2}++;
    }
    elsif ($p1 =~ /^Open relay\? Nonlocal recips but not originating/) {
        $Totals{'warningsecurity'}++;
        $Counts{'warningsecurity'}{$p1}++ if ($Collecting{'warningsecurity'});
    }

    # keep before general warnings below, so sadiag gets first crack at log
    # lines beginning with "(!) ...".
    elsif ($p1 =~ /^(?:\(!+\))?\!?SA (warn|info|error): (.*)$/) {
        #TD SA warn: FuzzyOcr: Cannot find executable for gocr
        my ($level, $msg) = ($1, $2);

        # XXX later, maybe break out stats on FuzzyOcr
        # skip "image too small" for now
        if ($msg =~ /^FuzzyOcr: Skipping .+, image too small$/) {
            #TD SA warn: FuzzyOcr: Skipping ocrad, image too small
            #TD SA warn: FuzzyOcr: Skipping ocrad-decolorize, image too small
            #$Counts{'sadiags'}{'fuzzyocr'}{'image too small'}++;
            next;
        }
        elsif ($msg =~ /dns: \[\.\.\.\]/) {
            #TD SA info: dns: [...] ;; ADDITIONAL SECTION (1 record)
            next;
        }
        # canonicalize some PIDs and IDs
        elsif ($msg =~ s/^pyzor: \[\d+\] error/pyzor: [<PID>] error/) {
            #TD SA info: pyzor: [11550] error: TERMINATED, signal 15 (000f)
        }
        elsif ($msg =~ /dns: no likely matching queries for id \d+/) {
            $msg =~ s/\d+/<ID>/;
        }
        elsif ($msg =~ /dns: no callback for id \d+/) {
            $msg =~ s/\d+.*$/<ID>.../;
        }

        # report other SA warn's
        $Totals{'sadiags'}++;
        next unless ($Collecting{'sadiags'});
        $Counts{'sadiags'}{ucfirst($level)}{$msg}++;
    }

    # catchall for most other warnings
    elsif (($p1 =~ /^\(!+\)/) or
        ($p1 =~ /^TROUBLE/) or
        ($p1 =~ /Can't (?:connect to UNIX|send to) socket/) or
        ($p1 =~ /: Empty result from /) or
        ($p1 =~ /: Error reading from socket: Connection reset by peer/) or
        ($p1 =~ /open\(.*\): Permission denied/) or
        ($p1 =~ /^_?WARN: /) or
        ($p1 =~ /Can't send SIG \d+ to process \[\d+\]: Operation not permitted/) or
        ($p1 =~ /(policy protocol: INVALID(?: AM\.PDP)? ATTRIBUTE LINE: .*)$/) or
        ($p1 =~ /(DKIM signature verification disabled, corresponding features not available. If not intentional.*)$/)
    ) {
        #TD (!)loading policy bank "AM.PDP-SOCK": unknown field "0"
        #TD (!!)policy_server FAILED: SQL quarantine code not enabled at (eval 37) line 306, <GEN6> line 4.
        #TD (!!)policy_server FAILED: Can't open file /var/spool/amavis/quarantine/spam-CFJYXmeS+FLy: Permission denied at (eval 37) line 330, <GEN28> line 5.
        #TD ClamAV-clamd: Empty result from /var/run/clamav/clamd, retrying (1)
        #TDdcc open(/var/dcc/map): Permission denied
        #TD TROUBLE in check_mail:  FAILED: Died at /usr/sbin/amavisd-maia line 2872, <GEN4> line 22.
        #TD TROUBLE in check_mail: spam_scan FAILED: DBD::mysql::st execute failed: MySQL server has gone away at /usr/sbin/amavisd-maia line 3786, <GEN4> line 3036.
        #TD TROUBLE in process_request: DBD::mysql::st execute failed: MySQL server has gone away at (eval 35) line 258, <GEN18> line 3.
        #TD TROUBLE in process_request: DBD::mysql::st execute failed: Lost connection to MySQL server during query at (eval 35) line 258, <GEN3> line 3.
        #TD TROUBLE in process_request: Can't call method "disconnect" on an undefined value at /usr/sbin/amavisd-maia line 2895, <GEN4> line 22.
        #TD TROUBLE: recipient not done: <to@example.com> smtp response ...
        #TD (!!)TROUBLE in process_request: Can't create file /var/amavis/tmp/amavis-98/email.txt: File exists at /usr/local/sbin/amavisd line 4774, <GEN12> line 4.
        #TD TROUBLE: lookup table is an unknown object: object ...
        #TD (!) policy protocol: INVALID ATTRIBUTE LINE: /var/spool/courier/tmp/114528/D967099\n
        #TD (!) policy protocol: INVALID AM.PDP ATTRIBUTE LINE: /var/spool/courier/tmp/114528/D967099\n
        #TD _WARN: bayes: cannot open bayes databases /var/spool/amavis/.spamassassin/bayes_* R/W: lock failed: Interrupted system call\n

        $p1 =~ s/^\(!+\)s*//;

        if ($p1 =~ /^WARN: (Using cpio instead of pax .*)$/) {
            #TD (!)WARN: Using cpio instead of pax can be a security risk; please add:  $pax='pax';  to amavisd.conf and check that the pax(1) utility is available on the system!
            $Totals{'warningsecurity'}++;
            $Counts{'warningsecurity'}{$1}++ if ($Collecting{'warningsecurity'});
            next;
        }

        $p1 =~ s/, retrying\s+\(\d+\)$//;
        strip_trace($p1);

        # canonicalize variations of the same message
        $p1 =~ s/^run_av \(([^,]+), built-in i\/f\)/$1/;
        $p1 =~ s/ av-scanner FAILED: CODE\(0x[^)]+\)/:/;
        $p1 =~ s/^(.+: Too many retries to talk to \S+) .*/$1/;

        if (($p1 =~ /(\S+): Can't (?:connect|send) to (?:UNIX )?(.*)$/) or
            ($p1 =~ /(\S+): (Too many retries to talk to .*)$/)) {

            #TD (!)ClamAV-clamd: Can't connect to UNIX socket /var/run/clamav/clamd.socket: No such file or directory, retrying (2)
            #TD (!)ClamAV-clamd: Can't connect to UNIX socket /var/run/clamav/clamd: Connection refused, retrying (2)
            #TD ClamAV-clamd: Can't connect to UNIX socket /var/run/clamav/clamd: Connection refused, retrying (1)
            #TD ClamAV-clamd: Can't send to socket /var/run/clamav/clamd: Transport endpoint is not connected, retrying (1)
            #TD Sophie: Can't send to socket /var/run/sophie: Transport endpoint is not connected, retrying (1)
            #TD (!)run_av (Sophie, built-in i/f): Too many retries to talk to /var/run/sophie (timed out) at (eval 55) line 310, <GEN16> line 16.
            #TD (!)run_av (ClamAV-clamd, built-in i/f): Too many retries to talk to /var/run/clamav/clamd.socket (Can't connect to UNIX socket /var/run/clamav/clamd.socket: No such file or directory) at (eval 52) line 310.
            #TD (!!)ClamAV-clamd av-scanner FAILED: CODE(0x804fa08) Too many retries to talk to /var/run/clamav/clamd.socket (Can't connect to UNIX socket /var/run/clamav/clamd.socket: No such file or directory) at (eval 52) line 310. at (eval 52) line 511.
            #TD (!!)Sophie av-scanner FAILED: CODE(0x814fd24) Too many retries to talk to /var/run/sophie (timed out) at (eval 55) line 310, <GEN16> line 16. at (eval 55) line 511, <GEN16> line 16.

            $Totals{'avconnectfailure'}++;
            $Counts{'avconnectfailure'}{$1}{ucfirst($2)}++ if ($Collecting{'avconnectfailure'});
            next;
        }

        # simplify or canonicalize variations of the same message
        $p1 =~ s/^TROUBLE(:| in) //;
        $p1 =~ s/^_?WARN: //;
        $p1 =~ s/Can't create file \S+: (.+)$/Can't create file: $1/;
        $p1 =~ s/Can't send SIG \d+ to process \[\d+\]/Can't send SIG to process/;

        $Totals{'warning'}++;
        next unless ($Collecting{'warning'});
        $Counts{'warning'}{$p1}++;
    }

    # Begin forced warnings: Keep this code below warning catchall
    elsif ($p1 =~ /^lookup_sql: /) {
        #TD lookup_sql: 2006, MySQL server has gone away
        $Totals{'warningsql'}++;
        next unless ($Collecting{'warningsql'});
        $Counts{'warningsql'}{'SQL died'}++;

    }
    elsif (($reason, $item) = ($p1 =~ /^connect_to_sql: ([^']+) '\S+': (.*?)(?: \(\d+\))?$/) or
        ($item, $reason) = ($p1 =~ /^lookup_sql_field\((.*)\) \(WARN: (no such field in the SQL table)\)/)) {
        #TD connect_to_sql: unable to connect to DSN 'DBI:mysql:maia:sqlhost1.example.com': Lost connection to MySQL server during query
        #TD connect_to_sql: unable to connect to DSN 'DBI:mysql:maia:sqlhost2.example.com': Can't connect to MySQL server on 'sqlhost2.example.com' (111)
        #TD lookup_sql_field(id) (WARN: no such field in the SQL table), "from@example.com" result=undef
        $Totals{'warningsql'}++;
        next unless ($Collecting{'warningsql'});
        $Counts{'warningsql'}{ucfirst("$reason: $item")}++;
    }
    # End forced warnings

    # panic
    elsif (($p2) = ($p1 =~ /^(?:\(!\)\s*)?PANIC, (.*)$/)) {
        #TD PANIC, PANIC, SA produced a clone process of [19122], TERMINATING CLONE [19123]

        $Totals{'panic'}++;
        next unless ($Collecting{'panic'});
        $Counts{'panic'}{$p2}++;

    }

    # fatal
    elsif ($p1 =~ /^Requesting process rundown after fatal error$/) {
        #TD Requesting process rundown after fatal error
        $Totals{'fatal'}++;
        next unless ($Collecting{'fatal'});
        $Counts{'fatal'}{$p1}++;

        # DCC
    }
    elsif (($reason) = ($p1 =~ /^(missing message body; fatal error)/) or
        ($reason) = ($p1 =~ /^(try to start dccifd)/)) {
        $Totals{'dccerror'}++;
        next unless ($Collecting{'dccerror'});
        $Counts{'dccerror'}{ucfirst($reason)}++;
    }
    elsif ($p1 =~ /^continue not asking DCC \d+ seconds after failure/) {
        $Totals{'dccerror'}++;
        next unless ($Collecting{'dccerror'});
        $Counts{'dccerror'}{'Continue not asking DCC after failure'}++;
    }
    elsif ($p1 =~ /^no DCC answer from (\S+) after \d+ ms$/) {
        $Totals{'dccerror'}++;
        next unless ($Collecting{'dccerror'});
        $Counts{'dccerror'}{"No answer from $1"}++;
    }
    elsif (($reason, $from, $to) = ($p1 =~ /^skip local delivery\((\d+)\): <(.*?)> -> <(.*?)>$/)) {
        $Totals{'localdeliveryskipped'}++;
        next unless ($Collecting{'localdeliveryskipped'});
        $from = '<>' if ($from eq '');
        $reason = $reason == 1 ? "No localpart" : $reason == 2 ? "Local alias is null" : "Other";
        $Counts{'localdeliveryskipped'}{$reason}{$from}{$to}++;
    }

    # hard and soft whitelisted/blacklisted
    elsif ($p1 =~ /^wbl: (.*)$/) {
        # ignore wbl entries, can't think of good way to reliably summarize.
        # and 'black or whitelisted by all' makes using by-white or -black list
        # groupings impossible
        next;
=cut
      $p1 = $1;

      # TD wbl: black or whitelisted by all recips
      next if ($p1 =~ /^black or whitelisted/); # not clear how to report this, so skip
      next if ($p1 =~ /^checking sender/);                    # ll 4
      next if ($p1 =~ /^(LDAP) query keys/);                  # ll 5
      next if ($p1 =~ /^(LDAP) recip/);                       # ll 5
      next if ($p1 =~ /^recip <[^>]*> (?:black|white)listed sender/);  # ll 5

      # lookup order: SQL, LDAP, static
      if ($p1 =~ s/^\(SQL\) recip <[^>]*>//) {
         next if ($p1 =~ /^, \S+ matches$/);                  # ll 5
         next if ($p1 =~ /^, rid=/);                          # ll 4
         next if ($p1 =~ /^ is neutral to sender/);           # ll 5
         next if ($p1 =~ /^ (?:white|black)listed sender </); # ll 5
         # ll -1
         #wbl: (SQL) recip <%s> whitelisted sender <%s>, '.  unexpected wb field value
      }
      #ll2
      # wbl: (SQL) soft-(white|black)listed (%s) sender <%s> => <%s> (rid=%s)',  $val, $sender, $recip, $user_id);
      # multiple senders: message sender, then "from", etc.
      #ll2
      # wbl: soft-(white|black)listed (%s) sender <%s> => <%s>,

      #TD wbl: whitelisted sender <sender@example.com>
      #TD wbl: soft-whitelisted (-3) sender <from@example.com> => <to@sample.net>, recip_key="."
      #TD wbl: whitelisted by user@example.com, but not by all, sender <bounces@example.net>, <user@example.org>
      # wbl: (whitelisted|blacklisted|black or whitelisted by all recips|(white|black)listed by xxx,yyy,... but not by all) sender %s

      if ($p1 =~ /^(?:\(SQL\) )?(?:(soft)-)?((?:white|black)listed)(?: \([^)]+\))? sender <([^>]*)>/) {
         my ($type,$list,$sender) = ($1,$2,$3);
         $Totals{$list}++;       next unless ($Collecting{$list});
         $type = $type ? 'Soft' : 'Hard' ;
         my ($localpart, $domainpart) = split (/@/, lc $sender);
         ($localpart, $domainpart) = ($sender, '*unspecified')   if ($domainpart eq '');
         $Counts{$list}{$type}{$domainpart}{$localpart}++;
      }
      else {
         inc_unmatched('wbl');
         next;
      }
=cut
    }

    # XXX: WHITELISTED or BLACKLISTED should be caught in SPAM tag above
    elsif (($p1 =~ /^white_black_list: whitelisted sender/) or
        ($p1 =~ /.* WHITELISTED/)) {
        $Totals{'whitelisted'}++;

    }
    elsif (($p1 =~ /^white_black_list: blacklisted sender/) or
        ($p1 =~ /.* BLACKLISTED/)) {
        $Totals{'blacklisted'}++;

    }
    elsif ($p1 =~ /^Turning AV infection into a spam report: score=([^,]+), (.+)$/) {
        #TD Turning AV infection into a spam report: score=4.1, AV:Sanesecurity.ScamL.375.UNOFFICIAL=4.1
        #TD Turning AV infection into a spam report: score=3.4, AV:Sanesecurity.Phishing.Cur.180.UNOFFICIAL=3.1,AV:Sanesecurity.Phishing.Cur.180.UNOFFICIAL=3.4
        #BAT.Backdoor.Poisonivy.E178-SecuriteInfo.com

        next unless ($Collecting{'malwaretospam'});
        #my $score_max = $1;
        my @list = split(/,/, $2);
        @list = unique_list(\@list);
        foreach (@list) {
            my ($name, $score) = split(/=/, $_);
            $name =~ s/^AV://;
            my $type = $name =~ s/\.UNOFFICIAL$// ? 'Unofficial' : 'Official';
            # strip trailing numeric variant (...Phishing.Cur.863)
            my $variant = $name =~ s/([.-]\d+)$// ? $1 : '*invariant';
            $Counts{'malwaretospam'}{$type}{$name}{$variant}{$score}++
        }

        # The virus_scan line reports only the one virus name when more than one scanner detects a virus.
        # Use instead the ask_av and run_av lines (see below)
        #
        #} elsif ( my ($malware, $scanners) = ($p1 =~ /virus_scan: \(([^)]+)\), detected by \d+ scanners: (.*)$/ )) {
        #TD virus_scan: (HTML.Phishing.Bank-43), detected by 1 scanners: ClamAV-clamd
        #TD virus_scan: (Worm.SomeFool.D, Worm.SomeFool.D), detected by 1 scanners: ClamAV-clamd
        #TD virus_scan: (Trojan.Downloader.Small-9993), detected by 2 scanners: ClamAV-clamd, NAI McAfee AntiVirus (uvscan)
        #   foreach (split /, /, $scanners) {
        #      #$Totals{'malwarebyscanner'}++;       # No summary output: redundant w/malwarepassed,malwareblocked}
        #      $Counts{'malwarebyscanner'}{"$_"}{$malware}++;
        #   }

    }
    elsif ($p1 =~ /^(?:ask_av|run_av) (.*)$/) {
        next unless ($Collecting{'malwarebyscanner'});

        if (my ($scanner, $name) = ($1 =~ /^\((.+)\):(?: [^:]+)? INFECTED: ([^,]+)/)) {
            #TD ask_av (ClamAV-clamd): /var/amavis/tmp/amavis-20070830T070403-13776/parts INFECTED: Email.Malware.Sanesecurity.07082700
            #TD run_av (NAI McAfee AntiVirus (uvscan)): INFECTED: W32/Zhelatin.gen!eml, W32/Zhelatin.gen!eml
            my $type = $name =~ s/\.UNOFFICIAL$// ? 'Unofficial' : 'Official';
            my $variant = '';
            if ($name =~ s/([.-]\d+)$//) { # strip trailing numeric variant (...Phishing.Cur.863)
                $variant = $1;
            }
            $Counts{'malwarebyscanner'}{$scanner}{$type}{$name}{$variant}++;
        }
        # currently ignoring other ask_av or run_av lines
    }

    # Extra Modules loaded at runtime
    #TD extra modules loaded after daemonizing/chrooting: Mail/SPF/Query.pm
    elsif (($item) = ($p1 =~ /^extra modules loaded(?: after daemonizing(?:\/chrooting)?)?: (.+)$/)) {
        #TD extra modules loaded: PerlIO.pm, PerlIO/scalar.pm
        foreach my $code (split /, /, $item) {
            #TD extra modules loaded: unicore/lib/gc_sc/Digit.pl, unicore/lib/gc_sc/SpacePer.pl
            # avoid useless reporting of pseudo-modules which can't be pre-loaded once
            unless ($code =~ m#^unicore/lib/#) {
                $Totals{'extramodules'}++;
                $Counts{'extramodules'}{$code}++ if ($Collecting{'extramodules'});
            }
        }

        # Timing report
    }
    elsif (my ($total, $report) = ($p1 =~ /^(?:size: \d+, )?TIMING \[total (\d+) ms(?:, [^]]+)?\] - (.+)$/)) {
        next if ($report =~ /^got data/); # skip amavis release timing
        #TD TIMING [total 5808 ms] - SMTP greeting: 5 (0%)0, SMTP LHLO: 1 (0%)0, SMTP pre-MAIL: 2 (0%)0, SMTP pre-DATA-flush: 5 (0%)0, SMTP DATA: 34 (1%)1, check_init: 1 (0%)1
        # older format, maia mailguard
        #TD TIMING [total 3795 ms] - SMTP EHLO: 1 (0%), SMTP pre-MAIL: 0 (0%), maia_read_system_config: 1 (0%), maia_get_mysql_size_limit: 0 (0%), SA check: 3556 (94%), rundown: 0 (0%)
        # v2.8.1
        # .... size: 3815, TIMING [total 1901 ms, cpu 657 ms] - ...


        # Timing line is incomplete - let's report it
        if ($p1 !~ /\d+ \(\d+%\)\d+$/ and $p1 !~ /\d+ \(\d+%\)$/) {
            inc_unmatched('timing');
            next;
        }

        if ($Opts{'timings'}) {
            my @pairs = split(/[,:] /, $report);
            while (my ($key, $value) = @pairs) {
                #4 (0%)0
                my ($ms) = ($value =~ /^([\d.]+) /);
                # maintain a per-test list of timings
                push @{$Timings{$key}}, $ms;
                shift @pairs;
                shift @pairs;
            }
            push @TimingsTotals, $total;
        }

    }
    elsif ((($total, $report) = ($p1 =~ /^TIMING-SA total (\d+) ms - (.+)$/)) or
        (($total, $report) = ($p1 =~ /^TIMING-SA \[total (\d+) ms, cpu \d+ ms\] - (.+)$/))) {
        #TIMING-SA [total 3219 ms, cpu 432 ms] - parse: 6 (0.2%), ext
        #TD TIMING-SA total 5478 ms - parse: 1.69 (0.0%), extract_message_metadata: 16 (0.3%), get_uri_detail_list: 2 (0.0%), tests_pri_-1000: 25 (0.4%), tests_pri_-950: 0.67 (0.0%), tests_pri_-900: 0.83 (0.0%), tests_pri_-400: 19 (0.3%), check_bayes: 17 (0.3%), tests_pri_0: 5323 (97.2%), check_spf: 12 (0.2%), poll_dns_idle: 0.81 (0.0%), check_dkim_signature: 1.50 (0.0%), check_razo r2: 5022 (91.7%), check_dcc: 192 (3.5%), check_pyzor: 0.02 (0.0%), tests_pri_500: 9 (0.2%), tests_pri_1000: 24 (0.4%), total_awl: 23 (0.4%), check_awl: 10 (0.2%), update_awl: 8 (0.1%), learn: 36 (0.7%), get_report: 1.77 (0.0%)

        # Timing line is incomplete - let's report it
        if ($p1 !~ /[\d.]+ \([\d.]+%\)[\d.]+$/ and $p1 !~ /[\d.]+ \([\d.]+%\)$/) {
            inc_unmatched('timing-sa');
            next;
        }
        if ($Opts{'sa_timings'}) {
            my @pairs = split(/[,:] /, $report);
            while (my ($key, $value) = @pairs) {
                #4 (0%)0
                my ($ms) = ($value =~ /^([\d.]+) /);
                # maintain a per-SA test list of timings
                push @{$TimingsSA{$key}}, $ms;
                shift @pairs;
                shift @pairs;
            }
            push @TimingsSATotals, $total;
        }

        # Bounce killer: 2.6+
    }
    elsif ($p1 =~ /^bounce (.*)$/) {
        #TD bounce killed, <user@example.com> -> <to@example.net>, from: user@example.com, message-id: <CA8E335-CC-2EFB@example.com>, return-path: <user@example.com>
        #TD bounce rescued by domain, <user@example.com> -> <to@example.net>, from: user@example.com, message-id: <CA8E335-CC-2EFB@example.com>, return-path: <user@example.com>
        #TD bounce rescued by originating, <user@example.com> -> <to@example.net>, from: user@example.com, message-id: <CA8E335-CC-2EFB@example.com>, return-path: <user@example.com>
        #TD bounce rescued by: pen pals disabled, <user@example.com> -> <to@example.net>, from: user@example.com, message-id: <CA8E335-CC-2EFB@example.com>, return-path: <user@example.com>
        $p2 = $1;

        if ($p2 =~ /^killed, <(.+?)> -> /) {
            $Totals{'bouncekilled'}++;
            $Counts{'bouncekilled'}{$1 eq '' ? '<>' : $1}++ if ($Collecting{'bouncekilled'});
        }
        elsif ($p2 =~ /^rescued by ([^,]+), <(.+?)> -> /) {
            # note: ignores "rescued by: pen pals disabled"
            $Totals{'bouncerescued'}++;
            $Counts{'bouncerescued'}{'By ' . $1}{$2 eq '' ? '<>' : $2}++ if ($Collecting{'bouncerescued'});
        }
        elsif ($p2 =~ /^unverifiable, <(.+?)> -> /) {
            # note: ignores "rescued by: pen pals disabled"
            $Totals{'bounceunverifiable'}++;
            $Counts{'bounceunverifiable'}{$1 eq '' ? '<>' : $1}++ if ($Collecting{'bounceunverifiable'});
        }
        #TD bounce unverifiable, <postmaster@nurturegood.com> -> <dave@davewolloch.com>
        #TD bounce unverifiable, <> -> <Dave@davewolloch.com>
    }

    # Decoders
    elsif (my ($suffix, $info) = ($p1 =~ /^Internal decoder for (\.\S*)\s*(?:\(([^)]*)\))?$/)) {
        #TD Internal decoder for .gz   (backup, not used)
        #TD Internal decoder for .zip
        next unless ($Opts{'startinfo'});
        $StartInfo{'Decoders'}{'Internal'}{$suffix} = $info;
    }
    elsif (($suffix, $decoder) = ($p1 =~ /^No decoder for\s+(\.\S*)\s*(?:tried:\s+(.*))?$/)) {
        #TD No decoder for       .tnef tried: tnef
        # older
        #TD No decoder for       .doc
        next unless ($Opts{'startinfo'});
        $StartInfo{'Decoders'}{'None'}{$suffix} = "tried: " . ($decoder ? $decoder : "unknown");
    }
    elsif (($suffix, $decoder) = ($p1 =~ /^Found decoder for\s+(\.\S*)\s+at\s+(.*)$/)) {
        #TD Found decoder for    .bz2  at /usr/bin/bzip2 -d
        #TD Found decoder for    .bz2  at /usr/bin/7za (backup, not used)
        next unless ($Opts{'startinfo'});
        $StartInfo{'Decoders'}{'External'}{$suffix} = exists $StartInfo{'Decoders'}{'External'}{$suffix} ?
            join '; ', $StartInfo{'Decoders'}{'External'}{$suffix}, $decoder : $decoder;
    }

    # AV Scanners
    elsif (my ($tier, $scanner, $location) = ($p1 =~ /^Found (primary|secondary) av scanner (.+) at (.+)$/)) {
        #TD Found primary av scanner NAI McAfee AntiVirus (uvscan) at /usr/local/bin/uvscan
        #TD Found secondary av scanner ClamAV-clamscan at /usr/local/bin/clamscan
        next unless ($Opts{'startinfo'});
        $StartInfo{'AVScanner'}{"\u$tier"}{$scanner} = $location;

    }
    elsif (($tier, $scanner, $location) = ($p1 =~ /^No (primary|secondary) av scanner: (.+)$/)) {
        #TD No primary av scanner: CyberSoft VFind
        next unless ($Opts{'startinfo'});
        $StartInfo{'AVScanner'}{"\u$tier (not found)"}{$scanner} = '';

    }
    elsif ((($tier, $scanner) = ($p1 =~ /^Using internal av scanner code for \(([^)]+)\) (.+)$/)) or
        (($tier, $scanner) = ($p1 =~ /^Using (.*) internal av scanner code for (.+)$/))) {
        #TD Using internal av scanner code for (primary) ClamAV-clamd
        #TD Using primary internal av scanner code for ClamAV-clamd
        next unless ($Opts{'startinfo'});
        $StartInfo{'AVScanner'}{"\u$tier internal"}{$scanner} = '';

        # (Un)Loaded code, protocols, etc.
    }
    elsif (my ($code, $loaded) = ($p1 =~ /^(\S+)\s+(?:proto? |base |protocol )?\s*(?:code)?\s+((?:NOT )?loaded)$/)) {
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{"\u\L$loaded"}{$code} = "";

    }
    elsif (my ($module, $vers) = ($p1 =~ /^Module (\S+)\s+(.+)$/)) {
        #TD Module Amavis::Conf        2.086
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{'Loaded'}{$module} = $vers;

    }
    elsif (($module, my $families) = ($p1 =~ /^socket module (\S+),\s+(.+)$/)) {
        #TD socket module IO::Socket::IP, protocol families available: INET, INET6
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{'Loaded'}{$module} = $families;

    }
    elsif (($code, $location) = ($p1 =~ /^Found \$(\S+)\s+at\s+(.+)$/)) {
        #TD Found $file            at /usr/bin/file
        #TD Found $uncompress at /usr/bin/gzip -d
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{'Loaded'}{$code} = $location;

    }
    elsif (($code, $location) = ($p1 =~ /^No \$(\S+),\s+not using it/)) {
        #TD No $dspam,             not using it
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{'Not loaded'}{$code} = $location;

    }
    elsif (($code, $location) = ($p1 =~ /^No ext program for\s+([^,]+), (tried: .+)/)) {
        #TD No ext program for   .kmz, tried: 7za, 7z
        #TD No ext program for   .F, tried: unfreeze, freeze -d, melt, fcat
        next unless ($Opts{'startinfo'});
        $StartInfo{'Code'}{'Not found'}{$code} = $location;

    }
    elsif ($p1 =~ /^starting\.\s+(.+) at \S+ (?:amavisd-new-|Maia Mailguard )([^,]+),/) {
        #TD starting.  /usr/local/sbin/amavisd at mailhost.example.com amavisd-new-2.5.0 (20070423), Unicode aware, LANG="C"
        #TD starting.  /usr/sbin/amavisd-maia at vwsw02.eon.no Maia Mailguard 1.0.2, Unicode aware, LANG=en_US.UTF-8
        next unless ($Opts{'startinfo'});
        %StartInfo = () if !exists $StartInfo{'Logging'};
        $StartInfo{'ampath'} = $1;
        $StartInfo{'amversion'} = $2;

    }
    elsif ($p1 =~ /^config files read: (.*)$/) {
        #TD config files read: /etc/amavisd.conf, /etc/amavisd-overrides.conf
        next unless ($Opts{'startinfo'});
        $StartInfo{'Configs'} = "$1";

    }
    elsif ($p1 =~ /^Creating db in ([^;]+); [^,]+, (.*)$/) {
        #TD Creating db in /var/spool/amavis/db/; BerkeleyDB 0.31, libdb 4.4
        next unless ($Opts{'startinfo'});
        $StartInfo{'db'} = "$1\t($2)";

    }
    elsif ($p1 =~ /^BerkeleyDB-based Amavis::Cache not available, using memory-based local cache$/) {
        #TD BerkeleyDB-based Amavis::Cache not available, using memory-based local cache
        next unless ($Opts{'startinfo'});
        $StartInfo{'db'} = "BerkeleyDB\t(memory-based cache: Amavis::Cache unavailable)";

    }
    elsif (my ($log) = ($p1 =~ /^logging initialized, log (level \d+, (?:STDERR|syslog: \S+))/)) {
        next unless ($Opts{'startinfo'});
        %StartInfo = (); # first amavis log entry, clear out previous start info
        $StartInfo{'Logging'} = $log;

    }
    elsif (($p1 =~ /^(:?perl=[^,]*, )?user=([^,]*), EUID: (\d+) [(](\d+)[)];\s+group=([^,]*), EGID: ([\d ]+)[(]([\d ]+)[)]/)) {
        # uninteresting...
        #next unless ($Opts{'startinfo'});
        #$StartInfo{'IDs'}{'user'} = $1;
        #$StartInfo{'IDs'}{'euid'} = $2;
        #$StartInfo{'IDs'}{'uid'} = $3;
        #$StartInfo{'IDs'}{'group'} = $4;
        #$StartInfo{'IDs'}{'egid'} = $5;
        #$StartInfo{'IDs'}{'gid'} = $6;
    }
    elsif ($p1 =~ /^after_chroot_init: EUID: (\d+) [(](\d+)[)]; +EGID: ([\d ]+)[(]([\d ]+)[)]/) {
        #TD after_chroot_init: EUID: 999 (999);  EGID: 54322 54322 54322 (54322 54322 54322)
        # uninteresting...

    }
    elsif ($p1 =~ /^SpamAssassin debug facilities: (.*)$/) {
        next unless ($Opts{'startinfo'});
        $StartInfo{'sa_debug'} = $1;

        # amavis >= 2.6.3
    }
    elsif ($p1 =~ /^SpamAssassin loaded plugins: (.*)$/) {
        #TD SpamAssassin loaded plugins: AWL, AutoLearnThreshold, Bayes, BodyEval, Check, DCC, DKIM, DNSEval, HTMLEval, HTTPSMismatch, Hashcash, HeaderEval, ImageInfo, MIMEEval, MIMEHeader, Pyzor, Razor2, RelayEval, ReplaceTags, SPF, SpamCop, URIDNSBL, URIDetail, URIEval, VBounce, WLBLEval, WhiteListSubject
        next unless ($Opts{'startinfo'});
        map {$StartInfo{'SAPlugins'}{'Loaded'}{$_} = ''} split(/, /, $1);

    }
    elsif (($p2) = ($p1 =~ /^Net::Server: (.*)$/)) {
        next unless ($Opts{'startinfo'});
        if ($p2 =~ /^.*starting! pid\((\d+)\)/) {
            #TD Net::Server: 2007/05/02-11:05:24 Amavis (type Net::Server::PreForkSimple) starting! pid(4405)
            $StartInfo{'Server'}{'pid'} = $1;
        }
        elsif ($p2 =~ /^Binding to UNIX socket file (.*) using/) {
            #TD Net::Server: Binding to UNIX socket file /var/spool/amavis/amavisd.sock using SOCK_STREAM
            $StartInfo{'Server'}{'socket'} = $1;
        }
        elsif ($p2 =~ /^Binding to TCP port (\d+) on host (.*)$/) {
            #TD Net::Server: Binding to TCP port 10024 on host 127.0.0.1
            $StartInfo{'Server'}{'ip'} = "$2:$1";
        }
        elsif ($p2 =~ /^Setting ([ug]id) to "([^"]+)"$/) {
            $StartInfo{'Server'}{$1} = $2;
            #TD Net::Server: Setting gid to "91 91"
            #TD Net::Server: Setting uid to "91"
        }
        # skip others
    }

    # higher debug level or rare messages skipped last
    elsif (!check_ignore_list($p1, @ignore_list_final)) {
        inc_unmatched('final');
    }
}

########################################
# Final tabulations, and report printing


# spamblocked includes spamdiscarded; adjust here
$Totals{'spamblocked'} -= $Totals{'spamdiscarded'};


#Totals: Blocked/Passed totals
$Totals{'totalblocked'} += $Totals{$_} foreach (
    qw(
        malwareblocked
        bannednameblocked
        uncheckedblocked
        spamblocked
        spamdiscarded
        spammyblocked
        badheaderblocked
        oversizedblocked
        mtablocked
        cleanblocked
        tempfailblocked
        otherblocked
    ));

$Totals{'totalpassed'} += $Totals{$_} foreach (
    qw(
        malwarepassed
        bannednamepassed
        uncheckedpassed
        spampassed
        spammypassed
        badheaderpassed
        oversizedpassed
        mtapassed
        cleanpassed
        tempfailpassed
        otherpassed
    ));

# Priority: VIRUS BANNED UNCHECKED SPAM SPAMMY BADH OVERSIZED MTA CLEAN
#Totals: Ham/Spam

$Totals{'totalmalware'} += $Totals{$_} foreach (
    qw(malwarepassed malwareblocked));

$Totals{'totalbanned'} += $Totals{$_} foreach (
    qw(bannednamepassed bannednameblocked));

$Totals{'totalunchecked'} += $Totals{$_} foreach (
    qw(uncheckedpassed uncheckedblocked));

$Totals{'totalspammy'} += $Totals{$_} foreach (
    qw(spammypassed spammyblocked));

$Totals{'totalbadheader'} += $Totals{$_} foreach (
    qw(badheaderpassed badheaderblocked));

$Totals{'totaloversized'} += $Totals{$_} foreach (
    qw(oversizedpassed oversizedblocked));

$Totals{'totalmta'} += $Totals{$_} foreach (
    qw(mtapassed mtablocked));

$Totals{'totalclean'} += $Totals{$_} foreach (
    qw(cleanpassed cleanblocked));

$Totals{'totalother'} += $Totals{$_} foreach (
    qw(tempfailpassed tempfailblocked otherpassed otherblocked));

$Totals{'totalspam'} += $Totals{$_} foreach (
    qw(spampassed spamblocked spamdiscarded totalspammy));

# everything lower priority than SPAMMY is considered HAM
$Totals{'totalham'} += $Totals{$_} foreach (
    qw(totalbadheader totaloversized totalmta totalclean));

$Totals{'totalmsgs'} += $Totals{$_} foreach (
    qw(totalmalware totalbanned totalunchecked totalspam totalham totalother));

# Print the summary report if any key has non-zero data.
# Note: must explicitly check for any non-zero data,
# as Totals always has some keys extant.
#
if ($Opts{'summary'}) {
    for (keys %Totals) {
        if ($Totals{$_}) {
            print_summary_report(@Sections);
            last;
        }
    }
}

# Print the detailed report, if detail is sufficiently high
#
if ($Opts{'detail'} >= 5) {
    print_detail_report(@Sections);
    printAutolearnReport;
    printSpamScorePercentilesReport;
    printSpamScoreFrequencyReport;
    printSARulesReport;
    printTimingsReport("Scan Timing Percentiles", \%Timings, \@TimingsTotals, $Opts{'timings'});
    printTimingsReport("SA Timing Percentiles", \%TimingsSA, \@TimingsSATotals, 0 - $Opts{'sa_timings'});
    printStartupInfoReport if ($Opts{'detail'} >= 10);
}

#{
#use Data::Dumper;
#print Dumper(\%p0ftags);
#print Dumper($Counts{'p0f'});
#}

# Finally, print any unmatched lines
#
print_unmatched_report();

# Evaluates a given line against the list of ignore patterns.
#
sub check_ignore_list($ \@) {
    my ($line, $listref) = @_;

    foreach (@$listref) {
        return 1 if $line =~ /$_/;
    }

    return 0;
}


# Spam score percentiles report
#
=pod
   ==================================================================================
   Spam Score Percentiles        0%       50%       90%       95%       98%      100%
   ----------------------------------------------------------------------------------
   Score Spam (100)           6.650    21.906    34.225    36.664    38.196    42.218
   Score Ham (1276)         -17.979    -2.599     0.428     2.261     3.472     6.298
   ==================================================================================
=cut
sub printSpamScorePercentilesReport {
    return unless ($Opts{'score_percentiles'} and keys %SpamScores);

    #printf "Scores $_ (%d): @{$SpamScores{$_}}\n", scalar @{$SpamScores{$_}} foreach keys %SpamScores;
    my (@p, @sorted);
    my @percents = split /[\s,]+/, $Opts{'score_percentiles'};
    my $myfw2 = $fw2 - 1;

    print "\n", $sep1 x $fw1, $sep1 x $fw2 x @percents;
    printf "\n%-${fw1}s" . "%${myfw2}s%%" x @percents, "Spam Score Percentiles", @percents;
    print "\n", $sep2 x $fw1, $sep2 x $fw2 x @percents;

    foreach my $ccat (keys %SpamScores) {
        @sorted = sort {$a <=> $b} @{$SpamScores{$ccat}};
        @p = get_percentiles(@sorted, @percents);
        printf "\n%-${fw1}s" . "%${fw2}.3f" x scalar(@p), "Score \u$ccat (" . scalar(@sorted) . ')', @p;
    }

    print "\n", $sep1 x $fw1, $sep1 x $fw2 x @percents, "\n";
}

# Spam score frequency report
#
=pod
   ======================================================================================================
   Spam Score Frequency      <= -10     <= -5      <= 0      <= 5     <= 10     <= 20     <= 30      > 30
   ------------------------------------------------------------------------------------------------------
   Hits (1376)                   29       168       921       170        29        33         1        25
   Percent of Hits            2.11%    12.21%    66.93%    12.35%     2.11%     2.40%     0.07%     1.82%
   ======================================================================================================
=cut
sub printSpamScoreFrequencyReport {
    return unless ($Opts{'score_frequencies'} and keys %SpamScores);

    my @scores = ();
    push @scores, @{$SpamScores{$_}} foreach (keys %SpamScores);
    my $nscores = scalar @scores;

    my @sorted = sort {$a <=> $b} @scores;
    my @buckets = sort {$a <=> $b} split /[\s,]+/, $Opts{'score_frequencies'};
    push @buckets, $buckets[-1] + 1;
    #print "Scores: @sorted\n";

    my @p = get_frequencies(@sorted, @buckets);

    my @ranges = (0) x @buckets;
    my $last = @buckets - 1;
    $ranges[0] = sprintf "%${fw2}s", " <= $buckets[0]";
    $ranges[-1] = sprintf "%${fw2}s", " > $buckets[-2]";
    for my $i (1 .. @buckets - 2) {
        $ranges[$i] = sprintf "%${fw2}s", " <= $buckets[$i]";
    }

    print "\n", $sep1 x $fw1, $sep1 x $fw2 x @buckets;
    printf "\n%-${fw1}s" . "%-${fw2}s" x @buckets, "Spam Score Frequency", @ranges;
    print "\n", $sep2 x $fw1, $sep2 x $fw2 x @buckets;
    printf "\n%-${fw1}s" . "%${fw2}d" x scalar(@p), "Hits ($nscores)", @p;
    my $myfw2 = $fw2 - 1;
    printf "\n%-${fw1}s" . "%${myfw2}.2f%%" x scalar(@p), "Percent of Hits", map {($_ / $nscores) * 100.0;} @p;
    print "\n", $sep1 x $fw1, $sep1 x $fw2 x @buckets, "\n";
}

# SpamAssassin rules report
#
=pod
   ===========================================================================
   SpamAssassin Rule Hits: Spam
   ---------------------------------------------------------------------------
   Rank     Hits    % Msgs   % Spam    % Ham      Score Rule
   ----     ----    ------   ------    -----      ----- ----
      1       44    81.48%   93.62%    0.00%      1.961 URIBL_BLACK
      2       44    81.48%   93.62%   14.29%      0.001 HTML_MESSAGE
      3       42    77.78%   89.36%    0.00%      2.857 URIBL_JP_SURBL
      4       38    70.37%   80.85%   14.29%      2.896 RCVD_IN_XBL
      5       37    68.52%   78.72%    0.00%      2.188 RCVD_IN_BL_SPAMCOP_NET
   ...
   ===========================================================================

   ===========================================================================
   SpamAssassin Rule Hits: Ham
   ---------------------------------------------------------------------------
   Rank     Hits    % Msgs   % Spam    % Ham      Score Rule
   ----     ----    ------   ------    -----      ----- ----
      1        5     9.26%    2.13%   71.43%      0.001 STOX_REPLY_TYPE
      2        4     7.41%    0.00%   57.14%     -0.001 SPF_PASS
      3        4     7.41%    6.38%   57.14%          - AWL
      4        1     1.85%    0.00%   14.29%      0.303 TVD_RCVD_SINGLE
      5        1     1.85%   25.53%   14.29%        0.1 RDNS_DYNAMIC
   ...
   ===========================================================================
=cut
sub printSARulesReport {
    return unless (keys %{$Counts{'sarules'}});

    our $maxlen = 0;

    sub getSAHitsReport($ $) {
        my ($type, $topn) = @_;
        my $i = 1;
        my @report = ();

        return if ($topn eq '0'); # topn can be numeric, or the string "all"

        for (sort {$Counts{'sarules'}{$type}{$b} <=> $Counts{'sarules'}{$type}{$a}} keys %{$Counts{'sarules'}{$type}}) {

            # only show top n lines; all when topn is "all"
            if ($topn ne 'all' and $i > $topn) {
                push @report, "...\n";
                last;
            }
            my $n = $Counts{'sarules'}{$type}{$_};
            my $nham = $Counts{'sarules'}{'Ham'}{$_};
            my $nspam = $Counts{'sarules'}{'Spam'}{$_};
            # rank, count, % msgs, % spam, % ham
            push @report, sprintf "%4d %8d   %6.2f%%  %6.2f%%  %6.2f%%     %s\n",
                $i++,
                $n,
                $Totals{'totalmsgs'} == 0 ? 0 : 100.0 * $n / $Totals{'totalmsgs'},
                $Totals{'totalspam'} == 0 ? 0 : 100.0 * $nspam / $Totals{'totalspam'},
                $Totals{'totalham'} == 0 ? 0 : 100.0 * $nham / $Totals{'totalham'},
                $_;
            my $len = length($report[-1]) - 1;
            $maxlen = $len if ($len > $maxlen);
        }

        if (scalar @report) {
            print "\n", $sep1 x $maxlen, "\n";
            print "SpamAssassin Rule Hits: $type\n";
            print $sep2 x $maxlen, "\n";
            print "Rank     Hits    % Msgs   % Spam    % Ham      Score Rule\n";
            print "----     ----    ------   ------    -----      ----- ----\n";
            print @report;
            print $sep1 x $maxlen, "\n";
        }
    }

    my ($def_limit_spam, $def_limit_ham) = split /[\s,]+/, $Defaults{'sarules'};
    my ($limit_spam, $limit_ham) = split /[\s,]+/, $Opts{'sarules'};
    $limit_spam = $def_limit_spam if $limit_spam eq '';
    $limit_ham = $def_limit_ham if $limit_ham eq '';

    getSAHitsReport('Spam', $limit_spam);
    getSAHitsReport('Ham', $limit_ham);
}

# Autolearn report, only available if enabled in amavis $log_templ template
#
=pod
   ======================================================================
   Autolearn          Msgs      Spam       Ham   % Msgs   % Spam    % Ham
   ----------------------------------------------------------------------
   Spam                 36        36         0   66.67%   76.60%    0.00%
   Ham                   2         0         2    3.70%    0.00%   28.57%
   No                    7         4         3   12.96%    8.51%   42.86%
   Disabled              6         6         0   11.11%   12.77%    0.00%
   Failed                2         1         1    3.70%    2.13%   14.29%
   ----------------------------------------------------------------------
   Totals               53        47         6   98.15%  100.00%   85.71%
   ======================================================================
=cut
sub printAutolearnReport {
    #print "printAutolearnReport:\n"    if ($Opts{'debug'});
    return unless (keys %{$Counts{'autolearn'}});

    our $maxlen = 0;
    our ($nhamtotal, $nspamtotal);

    sub getAutolearnReport($) {
        my ($type) = @_;
        my @report = ();

        #  SA 2.5/2.6 : ham/spam/no
        #  SA 3.0+    : ham/spam/no/disabled/failed/unavailable
        for (qw(spam ham no disabled failed unavailable)) {

            next unless (exists $Counts{'autolearn'}{'Spam'}{$_} or exists $Counts{'autolearn'}{'Ham'}{$_});
            #print "printAutolearnReport: type: $_\n"    if ($Opts{'debug'});

            my $nham = exists $Counts{'autolearn'}{'Ham'}{$_} ? $Counts{'autolearn'}{'Ham'}{$_} : 0;
            my $nspam = exists $Counts{'autolearn'}{'Spam'}{$_} ? $Counts{'autolearn'}{'Spam'}{$_} : 0;
            my $nboth = $nham + $nspam;
            $nhamtotal += $nham;
            $nspamtotal += $nspam;
            # type, nspam, nham, % msgs, % spam, % ham
            push @report, sprintf "%-13s %9d %9d %9d  %6.2f%%  %6.2f%%  %6.2f%%\n",
                ucfirst $_,
                $nspam + $nham,
                $nspam,
                $nham,
                $Totals{'totalmsgs'} == 0 ? 0 : 100.0 * $nboth / $Totals{'totalmsgs'},
                $Totals{'totalspam'} == 0 ? 0 : 100.0 * $nspam / $Totals{'totalspam'},
                $Totals{'totalham'} == 0 ? 0 : 100.0 * $nham / $Totals{'totalham'};

            my $len = length($report[-1]) - 1;
            $maxlen = $len if ($len > $maxlen);
        }
        return @report;
    }

    my @report_spam = getAutolearnReport('Spam');

    if (scalar @report_spam) {
        print "\n", $sep1 x $maxlen, "\n";
        print "Autolearn          Msgs      Spam       Ham   % Msgs   % Spam    % Ham\n";
        print $sep2 x $maxlen, "\n";
        print @report_spam;
        print $sep2 x $maxlen, "\n";

        printf "%-13s %9d %9d %9d  %6.2f%%  %6.2f%%  %6.2f%%\n",
            'Totals',
            $nspamtotal + $nhamtotal,
            $nspamtotal,
            $nhamtotal,
            $Totals{'totalmsgs'} == 0 ? 0 : 100.0 * ($nspamtotal + $nhamtotal) / $Totals{'totalmsgs'},
            $Totals{'totalspam'} == 0 ? 0 : 100.0 * $nspamtotal / $Totals{'totalspam'},
            $Totals{'totalham'} == 0 ? 0 : 100.0 * $nhamtotal / $Totals{'totalham'};
        print $sep1 x $maxlen, "\n";
    }
}


# Timings percentiles report, used for amavis message scanning and spamassassin timings
=pod
   ========================================================================================================================
   Scan Timing Percentiles       % Time    Total (ms)        0%        5%       25%       50%       75%       95%      100%
   ------------------------------------------------------------------------------------------------------------------------
   AV-scan-2 (3)                 69.23%       7209.00   2392.00   2393.50   2399.50   2407.00   2408.50   2409.70   2410.00
   SA check (2)                  19.74%       2056.00    942.00    950.60    985.00   1028.00   1071.00   1105.40   1114.00
   SMTP DATA (3)                  5.49%        572.00    189.00    189.20    190.00    191.00    191.50    191.90    192.00
   AV-scan-1 (3)                  0.82%         85.00     11.00     12.60     19.00     27.00     37.00     45.00     47.00
   ...
   ------------------------------------------------------------------------------------------------------------------------
   Total                                     10413.00   2771.00   2867.10   3251.50   3732.00   3821.00   3892.20   3910.00
   ========================================================================================================================

   ========================================================================================================================
   SA Timing Percentiles         % Time    Total (ms)        0%        5%       25%       50%       75%       95%      100%
   ------------------------------------------------------------------------------------------------------------------------
   tests_pri_0 (1)               97.17%       5323.00   5323.00   5323.00   5323.00   5323.00   5323.00   5323.00   5323.00
   check_razor2 (1)              91.68%       5022.00   5022.00   5022.00   5022.00   5022.00   5022.00   5022.00   5022.00
   check_dcc (1)                  3.50%        192.00    192.00    192.00    192.00    192.00    192.00    192.00    192.00
   learn (1)                      0.66%         36.00     36.00     36.00     36.00     36.00     36.00     36.00     36.00
   tests_pri_-1000 (1)            0.46%         25.00     25.00     25.00     25.00     25.00     25.00     25.00     25.00
   ...
   ------------------------------------------------------------------------------------------------------------------------
   Total                                      5478.00   5478.00   5478.00   5478.00   5478.00   5478.00   5478.00   5478.00
   ========================================================================================================================
=cut
sub printTimingsReport($$$$) {
    my ($title, $timingsref, $totalsref, $cutoff) = @_;
    my @tkeys = keys %$timingsref;
    return unless scalar @tkeys;

    my (@p, @sorted, %perkey_totals, @col_subtotals);
    my ($pcnt, $max_pcnt, $max_rows, $time_total_actual, $time_total_hypo, $subtotal_pcnt);
    my @percents = split /[\s,]+/, $Opts{'timings_percentiles'};
    my $header_footer = $sep1 x 50 . ($sep1 x 10) x @percents;
    my $header_end = $sep2 x 50 . ($sep2 x 10) x @percents;
    my $title_width = '-28';

    print "\n$header_footer\n";
    printf "%${title_width}s  %6s %13s" . " %8s%%" x @percents, $title, "% Time", "Total (ms)", @percents;
    print "\n$header_end\n";

    # Sum the total time for each timing key
    foreach my $key (@tkeys) {
        foreach my $timeval (@{$$timingsref{$key}}) {
            $perkey_totals{$key} += $timeval;
        }
    }

    # Sum total time spent scanning
    map {$time_total_actual += $_} @$totalsref;

    # cutoff value used to limit the number of rows of output
    #   positive cutoff is a percentage of cumulative time
    #   negative cutoff limits number of rows
    if ($cutoff >= 0) {
        $max_pcnt = $cutoff != 100 ? $cutoff : 150; # 150% avoids roundoff errors
    }
    else {
        $max_rows = -$cutoff;
    }
    my $rows = 0;
    # sort each timing key's values, required to compute the list of percentiles
    for (sort {$perkey_totals{$b} <=> $perkey_totals{$a}} @tkeys) {
        last if (($max_rows and $rows >= $max_rows) or ($max_pcnt and $subtotal_pcnt >= $max_pcnt));

        $pcnt = ($perkey_totals{$_} / $time_total_actual) * 100,
            @sorted = sort {$a <=> $b} @{$$timingsref{$_}};
        @p = get_percentiles(@sorted, @percents);

        $subtotal_pcnt += $pcnt;
        printf "%${title_width}s %6.2f%% %13.2f" . " %9.2f" x scalar(@p) . "\n",
            $_ . ' (' . scalar(@{$$timingsref{$_}}) . ')', # key ( number of elements )
            $pcnt,                                         # percent of total time
            #$perkey_totals{$_} / 1000,                     # total time for this test
            $perkey_totals{$_}, # total time for this test
            #map {$_ / 1000} @p;                            # list of percentiles
            @p; # list of percentiles
        $rows++;
    }
    print "...\n" if ($rows != scalar @tkeys);

    print "$header_end\n";
    # actual total time as reported by amavis
    @sorted = sort {$a <=> $b} @$totalsref;
    @p = get_percentiles(@sorted, @percents);
    printf "%${title_width}s         %13.2f" . " %9.2f" x scalar(@p) . "\n",
        'Total',
        #$time_total_actual / 1000,
        $time_total_actual,
        #map {$_ / 1000} @p;
        @p;

    print "$header_footer\n";
}

# Most recent startup info report
#
sub printStartupInfoReport {

    return unless (keys %StartInfo);

    sub print2col($ $) {
        my ($label, $val) = @_;
        printf "%-50s %s\n", $label, $val;
    }

    print "\nAmavis Startup\n";

    print2col("    Amavis", $StartInfo{'ampath'}) if (exists $StartInfo{'ampath'});
    print2col("        Version", $StartInfo{'amversion'}) if (exists $StartInfo{'amversion'});
    print2col("        PID", $StartInfo{'Server'}{'pid'}) if (exists $StartInfo{'Server'}{'pid'});
    print2col("        Socket", $StartInfo{'Server'}{'socket'}) if (exists $StartInfo{'Server'}{'socket'});
    print2col("        TCP port", $StartInfo{'Server'}{'ip'}) if (exists $StartInfo{'Server'}{'ip'});
    print2col("        UID", $StartInfo{'Server'}{'uid'}) if (exists $StartInfo{'Server'}{'uid'});
    print2col("        GID", $StartInfo{'Server'}{'gid'}) if (exists $StartInfo{'Server'}{'gid'});
    print2col("        Logging", $StartInfo{'Logging'}) if (exists $StartInfo{'Logging'});
    print2col("        Configuration Files", $StartInfo{'Configs'}) if (exists $StartInfo{'Configs'});
    print2col("    SpamAssassin", $StartInfo{'sa_version'}) if (exists $StartInfo{'sa_version'});
    print2col("    SpamAssassin Debug Facilities", $StartInfo{'sa_debug'}) if (exists $StartInfo{'sa_debug'});
    print2col("    Database", $StartInfo{'db'}) if (exists $StartInfo{'db'});
    #if (keys %{$StartInfo{'IDs'}}) {
    #   print "    Process startup user/group:\n";
    #   print "        User:  $StartInfo{'IDs'}{'user'}, EUID: $StartInfo{'IDs'}{'euid'}, UID: $StartInfo{'IDs'}{'uid'}\n";
    #   print "        Group: $StartInfo{'IDs'}{'group'}, EGID: $StartInfo{'IDs'}{'egid'}, GID: $StartInfo{'IDs'}{'gid'}\n";
    #}

    sub print_modules($ $) {
        my ($key, $label) = @_;
        print "    $label\n";
        foreach (sort keys %{$StartInfo{$key}}) {
            print "        $_\n";
            foreach my $module (sort keys %{$StartInfo{$key}{$_}}) {
                if ($StartInfo{$key}{$_}{$module}) {
                    print2col("            " . $module, $StartInfo{$key}{$_}{$module});
                }
                else {
                    print2col("            " . $module, "");
                }
            }
        }
    };
    print_modules('AVScanner', 'Antivirus scanners');
    print_modules('Code', 'Code, modules and external programs');
    print_modules('Decoders', 'Decoders');
    print_modules('SAPlugins', 'SpamAssassin plugins');
}

# Initialize the Getopts option list.  Requires the Section table to
# be built already.
#
sub init_getopts_table() {
    print "init_getopts_table: enter\n" if $Opts{'debug'} & D_ARGS;

    init_getopts_table_common(@supplemental_reports);

    add_option('first_recip_only!');
    add_option('show_first_recip_only=i', sub {
        $Opts{'first_recip_only'} = $_[1];
        1;
    });
    add_option('startinfo!');
    add_option('show_startinfo=i', sub {
        $Opts{'startinfo'} = $_[1];
        1;
    });
    add_option('by_ccat_summary!');
    add_option('show_by_ccat_summary=i', sub {
        $Opts{'by_ccat_summary'} = $_[1];
        1;
    });
    add_option('noscore_percentiles', \&triway_opts);
    add_option('score_percentiles=s', \&triway_opts);
    add_option('noscore_frequencies', \&triway_opts);
    add_option('score_frequencies=s', \&triway_opts);
    add_option('nosa_timings', sub {
        $Opts{'sa_timings'} = 0;
        1;
    });
    add_option('sa_timings=i');
    add_option('sa_timings_percentiles=s');
    add_option('notimings', sub {
        $Opts{'timings'} = 0;
        1;
    });
    add_option('timings=i');
    add_option('timings_percentiles=s');
    add_option('nosarules', \&triway_opts);
    add_option('sarules=s', \&triway_opts);
    #add_option ('nop0f',                     \&triway_opts);
    #add_option ('p0f=s',                     \&triway_opts);
    add_option('autolearn!');
    add_option('show_autolearn=i', sub {
        $Opts{'autolearn'} = $_[1];
        1;
    });
}

# Builds the entire @Section table used for data collection
#
# Each Section entry has as many as six fields:
#
#   1. Section array reference
#   2. Key to %Counts, %Totals accumulator hashes, and %Collecting hash
#   3. Output in Detail report? (must also a %Counts accumulator)
#   4. Numeric output format specifier for Summary report
#   5. Section title for Summary and Detail reports
#   6. A hash to a divisor used to calculate the percentage of a total for that key
#
# Use begin_section_group/end_section_group to create groupings around sections.
#
# Sections can be freely reordered if desired, but maintain proper group nesting.
#
sub build_sect_table() {
    print "build_sect_table: enter\n" if $Opts{'debug'} & D_SECT;
    my $S = \@Sections;

    # References to these are used in the Sections table below; we'll predeclare them.
    $Totals{'totalmsgs'} = 0;

    # Place configuration and critical errors first

    #    SECTIONREF, NAME,                 DETAIL, FMT, TITLE,                             DIVISOR
    begin_section_group($S, 'warnings');
    add_section($S, 'fatal', 1, 'd', '*Fatal');
    add_section($S, 'panic', 1, 'd', '*Panic');
    add_section($S, 'warningsecurity', 1, 'd', '*Warning: Security risk');
    add_section($S, 'avtimeout', 1, 'd', '*Warning: Virus scanner timeout');
    add_section($S, 'avconnectfailure', 1, 'd', '*Warning: Virus scanner connection failure');
    add_section($S, 'warningsmtpshutdown', 1, 'd', '*Warning: SMTP shutdown');
    add_section($S, 'warningsql', 1, 'd', '*Warning: SQL problem');
    add_section($S, 'warningaddressmodified', 1, 'd', '*Warning: Email address modified');
    add_section($S, 'warningnoquarantineid', 1, 'd', '*Warning: Message missing X-Quarantine-ID header');
    add_section($S, 'warning', 1, 'd', 'Miscellaneous warnings');
    end_section_group($S, 'warnings');

    begin_section_group($S, 'scanned', "\n");
    add_section($S, 'totalmsgs', 0, 'd', [ 'Total messages scanned', '-' ], \$Totals{'totalmsgs'});
    add_section($S, 'bytesscanned', 0, 'Z', 'Total bytes scanned'); # Z means print scaled as in 1k, 1m, etc.
    end_section_group($S, 'scanned', $sep1);

    # Blocked / Passed
    # Priority: VIRUS BANNED UNCHECKED SPAM SPAMMY BADH OVERSIZED MTA CLEAN
    begin_section_group($S, 'passblock', "\n");
    begin_section_group($S, 'blocked', "\n");
    add_section($S, 'totalblocked', 0, 'd', [ 'Blocked', '-' ], \$Totals{'totalmsgs'});
    add_section($S, 'malwareblocked', 1, 'd', '  Malware blocked', \$Totals{'totalmsgs'});
    add_section($S, 'bannednameblocked', 1, 'd', '  Banned name blocked', \$Totals{'totalmsgs'});
    add_section($S, 'uncheckedblocked', 1, 'd', '  Unchecked blocked', \$Totals{'totalmsgs'});
    add_section($S, 'spamblocked', 1, 'd', '  Spam blocked', \$Totals{'totalmsgs'});
    add_section($S, 'spamdiscarded', 0, 'd', '  Spam discarded (no quarantine)', \$Totals{'totalmsgs'});
    add_section($S, 'spammyblocked', 1, 'd', '  Spammy blocked', \$Totals{'totalmsgs'});
    add_section($S, 'badheaderblocked', 1, 'd', '  Bad header blocked', \$Totals{'totalmsgs'});
    add_section($S, 'oversizedblocked', 1, 'd', '  Oversized blocked', \$Totals{'totalmsgs'});
    add_section($S, 'mtablocked', 1, 'd', '  MTA blocked', \$Totals{'totalmsgs'});
    add_section($S, 'cleanblocked', 1, 'd', '  Clean blocked', \$Totals{'totalmsgs'});
    add_section($S, 'tempfailblocked', 1, 'd', '  Tempfail blocked', \$Totals{'totalmsgs'});
    add_section($S, 'otherblocked', 1, 'd', '  Other blocked', \$Totals{'totalmsgs'});
    end_section_group($S, 'blocked');

    begin_section_group($S, 'passed', "\n");
    add_section($S, 'totalpassed', 0, 'd', [ 'Passed', '-' ], \$Totals{'totalmsgs'});
    add_section($S, 'malwarepassed', 1, 'd', '  Malware passed', \$Totals{'totalmsgs'});
    add_section($S, 'bannednamepassed', 1, 'd', '  Banned name passed', \$Totals{'totalmsgs'});
    add_section($S, 'uncheckedpassed', 1, 'd', '  Unchecked passed', \$Totals{'totalmsgs'});
    add_section($S, 'spampassed', 1, 'd', '  Spam passed', \$Totals{'totalmsgs'});
    add_section($S, 'spammypassed', 1, 'd', '  Spammy passed', \$Totals{'totalmsgs'});
    add_section($S, 'badheaderpassed', 1, 'd', '  Bad header passed', \$Totals{'totalmsgs'});
    add_section($S, 'oversizedpassed', 1, 'd', '  Oversized passed', \$Totals{'totalmsgs'});
    add_section($S, 'mtapassed', 1, 'd', '  MTA passed', \$Totals{'totalmsgs'});
    add_section($S, 'cleanpassed', 1, 'd', '  Clean passed', \$Totals{'totalmsgs'});
    add_section($S, 'tempfailpassed', 1, 'd', '  Tempfail passed', \$Totals{'totalmsgs'});
    add_section($S, 'otherpassed', 1, 'd', '  Other passed', \$Totals{'totalmsgs'});
    end_section_group($S, 'passed');
    end_section_group($S, 'passblock', $sep1);

    if ($Opts{'by_ccat_summary'}) {
        # begin level 1 group
        begin_section_group($S, 'by_ccat', "\n");

        # begin level 2 groupings
        begin_section_group($S, 'malware', "\n"); # level 2
        add_section($S, 'totalmalware', 0, 'd', [ 'Malware', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'malwarepassed', 0, 'd', '  Malware passed', \$Totals{'totalmsgs'});
        add_section($S, 'malwareblocked', 0, 'd', '  Malware blocked', \$Totals{'totalmsgs'});
        end_section_group($S, 'malware');

        begin_section_group($S, 'banned', "\n");
        add_section($S, 'totalbanned', 0, 'd', [ 'Banned', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'bannednamepassed', 0, 'd', '  Banned file passed', \$Totals{'totalmsgs'});
        add_section($S, 'bannednameblocked', 0, 'd', '  Banned file blocked', \$Totals{'totalmsgs'});
        end_section_group($S, 'banned');

        begin_section_group($S, 'unchecked', "\n");
        add_section($S, 'totalunchecked', 0, 'd', [ 'Unchecked', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'uncheckedpassed', 0, 'd', '  Unchecked passed', \$Totals{'totalmsgs'});
        add_section($S, 'uncheckedblocked', 0, 'd', '  Unchecked blocked', \$Totals{'totalmsgs'});
        end_section_group($S, 'unchecked');

        begin_section_group($S, 'spam', "\n");
        add_section($S, 'totalspam', 0, 'd', [ 'Spam', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'spammypassed', 0, 'd', '  Spammy passed', \$Totals{'totalmsgs'});
        add_section($S, 'spammyblocked', 0, 'd', '  Spammy blocked', \$Totals{'totalmsgs'});
        add_section($S, 'spampassed', 0, 'd', '  Spam passed', \$Totals{'totalmsgs'});
        add_section($S, 'spamblocked', 0, 'd', '  Spam blocked', \$Totals{'totalmsgs'});
        add_section($S, 'spamdiscarded', 0, 'd', '  Spam discarded (no quarantine)', \$Totals{'totalmsgs'});
        end_section_group($S, 'spam');

        begin_section_group($S, 'ham', "\n");
        add_section($S, 'totalham', 0, 'd', [ 'Ham', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'badheaderpassed', 0, 'd', '  Bad header passed', \$Totals{'totalmsgs'});
        add_section($S, 'badheaderblocked', 0, 'd', '  Bad header blocked', \$Totals{'totalmsgs'});
        add_section($S, 'oversizedpassed', 0, 'd', '  Oversized passed', \$Totals{'totalmsgs'});
        add_section($S, 'oversizedblocked', 0, 'd', '  Oversized blocked', \$Totals{'totalmsgs'});
        add_section($S, 'mtapassed', 0, 'd', '  MTA passed', \$Totals{'totalmsgs'});
        add_section($S, 'mtablocked', 0, 'd', '  MTA blocked', \$Totals{'totalmsgs'});
        add_section($S, 'cleanpassed', 0, 'd', '  Clean passed', \$Totals{'totalmsgs'});
        add_section($S, 'cleanblocked', 0, 'd', '  Clean blocked', \$Totals{'totalmsgs'});
        end_section_group($S, 'ham');

        begin_section_group($S, 'other', "\n");
        add_section($S, 'totalother', 0, 'd', [ 'Other', '-' ], \$Totals{'totalmsgs'});
        add_section($S, 'tempfailpassed', 0, 'd', '  Tempfail passed', \$Totals{'totalmsgs'});
        add_section($S, 'tempfailblocked', 0, 'd', '  Tempfail blocked', \$Totals{'totalmsgs'});
        add_section($S, 'otherpassed', 0, 'd', '  Other passed', \$Totals{'totalmsgs'});
        add_section($S, 'otherblocked', 0, 'd', '  Other blocked', \$Totals{'totalmsgs'});
        end_section_group($S, 'other');
        # end level 2 groupings

        # end level 1 group
        end_section_group($S, 'by_ccat', $sep1);
    }

    begin_section_group($S, 'misc', "\n");
    add_section($S, 'virusscanskipped', 1, 'd', 'Virus scan skipped');
    add_section($S, 'sabypassed', 0, 'd', 'SpamAssassin bypassed');
    add_section($S, 'satimeout', 0, 'd', 'SpamAssassin timeout');
    add_section($S, 'released', 1, 'd', 'Released from quarantine');
    add_section($S, 'defanged', 1, 'd', 'Defanged');
    add_section($S, 'truncatedheader', 0, 'd', 'Truncated headers > 998 characters');
    add_section($S, 'truncatedmsg', 0, 'd', 'Truncated message passed to SpamAssassin');
    add_section($S, 'tagged', 0, 'd', 'Spam tagged');
    add_section($S, 'smtpresponse', 1, 'd', 'SMTP response');
    add_section($S, 'badaddress', 1, 'd', 'Bad address syntax');
    add_section($S, 'fakesender', 1, 'd', 'Fake sender');
    add_section($S, 'archiveextract', 1, 'd', 'Archive extraction problem');
    add_section($S, 'dsnsuppressed', 1, 'd', 'DSN suppressed');
    add_section($S, 'dsnnotification', 1, 'd', 'DSN notification (debug supplemental)');
    add_section($S, 'bouncekilled', 1, 'd', 'Bounce killed');
    add_section($S, 'bouncerescued', 1, 'd', 'Bounce rescued');
    add_section($S, 'bounceunverifiable', 1, 'd', 'Bounce unverifiable');
    add_section($S, 'nosubject', 0, 'd', 'Subject header inserted');
    add_section($S, 'whitelisted', 1, 'd', 'Whitelisted');
    add_section($S, 'blacklisted', 1, 'd', 'Blacklisted');
    add_section($S, 'penpalsaved', 1, 'd', 'Penpals saved from kill');
    add_section($S, 'tmppreserved', 1, 'd', 'Preserved temporary directory');
    add_section($S, 'dccerror', 1, 'd', 'DCC error');
    add_section($S, 'mimeerror', 1, 'd', 'MIME error');
    add_section($S, 'defangerror', 1, 'd', 'Defang error');
    add_section($S, 'badheadersupp', 1, 'd', 'Bad header (debug supplemental)');
    add_section($S, 'fileoutputskipped', 0, 'd', 'File(1) output skipped');
    add_section($S, 'localdeliveryskipped', 1, 'd', 'Local delivery skipped');
    add_section($S, 'extramodules', 1, 'd', 'Extra code modules loaded at runtime');
    add_section($S, 'malwarebyscanner', 1, 'd', 'Malware by scanner');
    add_section($S, 'malwaretospam', 1, 'd', 'Malware to spam conversion');
    add_section($S, 'contenttype', 1, 'd', 'Content types');
    add_section($S, 'bayes', 1, 'd', 'Bayes probability');
    add_section($S, 'p0f', 1, 'd', 'p0f fingerprint');
    add_section($S, 'sadiags', 1, 'd', 'SpamAssassin diagnostics');
    end_section_group($S, 'misc');

    print "build_sect_table: exit\n" if $Opts{'debug'} & D_SECT;
}

# XXX create array of defaults for detail <5, 5-9, >10
sub init_defaults() {
    map {$Opts{$_} = $Defaults{$_} unless exists $Opts{$_}} keys %Defaults;
    if (!$Opts{'standalone'}) {
        # LOGWATCH these take affect if no env present (eg. nothing in conf file)
        #  0 to 4 nostartinfo, notimings,   nosarules,        score_frequencies=0,        score_percentiles=0,      noautolearn
        #  5 to 9 nostartinfo, timings=95,  sarules = 20 20,  score_frequencies=defaults, score_percentiles=defaults, autolearn
        # 10 +    startinfo,   timings=100, sarules = all all score_frequencies=defaults, score_percentiles=defaults, autolearn

        if ($Opts{'detail'} < 5) {
            # detail 0 to 4, disable all supplemental reports
            $Opts{'autolearn'} = 0;
            #$Opts{'p0f'}               = 0;
            $Opts{'timings'} = 0;
            $Opts{'sa_timings'} = 0;
            $Opts{'sarules'} = 0;
            $Opts{'startinfo'} = 0;
            $Opts{'score_frequencies'} = '';
            $Opts{'score_percentiles'} = '';
        }
        elsif ($Opts{'detail'} < 10) { # detail 5 to 9, disable startinfo report
            $Opts{'startinfo'} = 0;
        }
        else {
            # detail 10 and up, full reports
            #$Opts{'p0f'}              = 'all all';
            $Opts{'timings'} = 100;
            $Opts{'sa_timings'} = 100;
            $Opts{'sarules'} = 'all all';
        }
    }
}

# Return a usage string,  built from:
#  arg1 +
#  $usage_str +
#  a string built from each usable entry in the @Sections table.
#
sub usage($) {
    my $ret = "";
    $ret = "@_\n" if ($_[0]);
    $ret .= $usage_str;
    my ($name, $desc);
    foreach my $sect (get_usable_sectvars(@Sections, 0)) {
        $name = lc $sect->{NAME};
        $desc = $sect->{TITLE};
        $ret .= sprintf "   --%-38s%s\n", "$name" . ' LEVEL', "$desc";
    }
    $ret .= "\n";
    return $ret;
}

sub strip_trace($) {
    # at (eval 37) line 306, <GEN6> line 4.
    # at /usr/sbin/amavisd-maia line 2895, <GEN4> line 22.
    #$_[0] =~ s/ at \(.+\) line \d+(?:, \<GEN\d+\> line \d+)?\.$//;
    #$_[0] =~ s/ at (\S+) line \d+(?:, \<GEN\d+\> line \d+)?\.$/: $1/;
    while ($_[0] =~ s/ at (?:\(eval \d+\)|\S+) line \d+(?:, \<GEN\d+\> line \d+)?\.//) {
        ;
    }
    #print "strip_trace: \"$_[0]\"\n";
    return $_[0];
}

# Getopt helper, sets an option in Opts hash to one of three
# values: its default, the specified value, or 0 if the option
# was the "no" prefixed variant.
#
sub triway_opts($ $) {
    my ($opt, $val) = @_;

    print "triway_opts: OPT: $opt, VAL: $val\n" if $Opts{'debug'} & D_ARGS;
    die "Option \"--${opt}\" requires an argument" if ($val =~ /^--/);

    if ($opt =~ s/^no//i) {
        $Opts{$opt} = 0;
    }
    elsif ('default' =~ /^${val}$/i) {
        $Opts{$opt} = $Defaults{$opt};
    }
    else {
        $Opts{$opt} = $val;
    }
}

exit(0);

# vi: shiftwidth=3 tabstop=3 syntax=perl et
