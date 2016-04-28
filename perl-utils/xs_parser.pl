#!/usr/bin/perl
use DynaLoader;
#use attributes();

my $frames = [ ];


#foreach my $module (qw/DynaLoader/)
#{
#    my $before = get_typeglobs_snapshot();
#    eval "use $module;";
#    my $after = get_typeglobs_snapshot();
#
#    push @$frames, {
#            module => $module,
#            frames => [],
#            diff => get_snapshots_diff($before, $after),
#        };
#}

my $MODULE_OF_INTEREST = $ARGV[0];
print STDERR "Trying to catch XSubs names from $MODULE_OF_INTEREST\n";
my $dl_install_xsub = \&DynaLoader::dl_install_xsub;

my $current_frame = $frames;

*DynaLoader::dl_install_xsub = sub {
    my $bootstrap = $_[0];

    if ($bootstrap =~ /^(.+?)::bootstrap/)
    {
        my $module = $1;
        print STDERR "Setting up $bootstrap\n";

        my $original_bootstrap = eval {$dl_install_xsub->( @_ )};
        print STDERR "Real $bootstrap is $original_bootstrap\n";

        *{$bootstrap} = sub {
            my $frame = {
                module => $module,
                frames => [ ],
            };
            printf STDERR "Bootstrapping $module\n";
            my $snapshot_before = get_typeglobs_snapshot();
            my $current_frame_backup = $current_frame;
            push @{$current_frame}, $frame;

            $current_frame = $frame->{frames};
            my $bootstrap_result = eval {$original_bootstrap->( @_ )};
            print STDERR "Done...\n";
            my $snapshot_after = get_typeglobs_snapshot();

            $frame->{diff} = get_snapshots_diff( $snapshot_before, $snapshot_after );
            printf STDERR "Typeglob diff size: %s\n", scalar @{$frame->{diff}};

            $current_frame = $current_frame_backup;
            return $bootstrap_result;
        };

        printf STDERR "Fake $bootstrap is %s\n", \&{$bootstrap};
        return \&{$bootstrap};
    }
    else
    {
        print STDERR "$bootstrap is not looks like a bootstrap\n";
        return $dl_install_xsub->( @_ );
    }
};

my @modules = qw/
    attributes
    /;

# JSON::XS
#attributes
#    Encode
#        Encode::Unicode
#        Encode::Byte
#Tk
#    Data::Dumper

foreach my $module (@modules)
{
    eval "use $module;";
    if (my $e = $@)
    {
        die $e;
    }
}

*DynaLoader::dl_install_xsub = \&$dl_install_xsub;

require Data::Dumper;
print Data::Dumper::Dumper( $frames );

sub get_snapshots_diff
{
    my ($snapshot_before, $snapshot_after) = @_;
    my %map = ();
    @map{@$snapshot_after} = @$snapshot_after;
    delete @map{@$snapshot_before};
    return [ keys %map ];
}

sub get_typeglobs_snapshot
{
    my $namespace = shift // "";
    my $recursion_map = shift // { qw/::main/ };

    $namespace =~ s/::$//;

    return [ ] if (exists $recursion_map->{$namespace});
    $recursion_map->{$namespace} = 1;

    my $result = [ ];

    foreach my $name (keys %{"${namespace}::"})
    {
        my $canonical_name = "${namespace}::$name";
        if ($name =~ /::$/)
        {
            push @{$result}, @{get_typeglobs_snapshot( $canonical_name, $recursion_map )};
        }
        else
        {
            push @{$result}, $canonical_name;
        }
    }

    return $result;
}
