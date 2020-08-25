#!/usr/bin/perl
##########################################################
# This script is part of the Devel::NYTProf distribution
#
# This is a patched version which ignores errors and accepts the only argument: target file or directory
#
# Copyright, contact and other information can be found
# at the bottom of this file, or by going to:
# http://metacpan.org/release/Devel-NYTProf/
#
##########################################################

use warnings;
use strict;

use Devel::NYTProf::Core;
require Devel::NYTProf::Data;

our $VERSION = '6.06';

use Data::Dumper;
use Carp;

my $file_or_directory = shift @ARGV or 'die provide path for a directory or file';

my $last_subid = 0;
my %subname2id;

my %option;
my %attribute;


# We're building a tree structure from a stream of "subroutine returned" events.
# (We use these because the subroutine entry events don't have reliable
# value for the subroutine name, and obviously don't have timings.)
#
# Building a call tree from return events is a little tricky because they don't
# appear in natural order. The code can return from a call at any depth
# deeper than the last seen depth.
#
my $root = {};
my @stack = ($root);
my $total_in = 0;

my $callbacks = {
    OPTION    => sub {
        my (undef, $k, $v) = @_;
        $option{$k} = $v
    },
    ATTRIBUTE => sub {
        my (undef, $k, $v) = @_;
        $attribute{$k} = $v
    },
};
$callbacks->{SUB_RETURN} = \&on_sub_return_build_call_stack;
$callbacks->{all_loaded} = sub {
    output_call_path_hash(extract_call_path_hash($root));
};

my @files;
if (-d $file_or_directory) {
    @files = glob("$file_or_directory/*");
}
else {
    @files = ($file_or_directory);
}

foreach my $input (@files) {
    eval {
        Devel::NYTProf::Data->new({
            filename => $input,
            quiet    => 1,
            callback => $callbacks
        });
    };
    warn "Error processing $input: $@" if $@;
}
$callbacks->{all_loaded}->();

exit 0;


sub on_sub_entry_log {
    my (undef, $fid, $line) = @_;
    warn "> at $fid:$line\n";
}


sub on_sub_return_build_call_stack {
    # $retn_depth is the call stack depth of the sub call we're returning from
    my (undef, $retn_depth, undef, $excl_time, $subname) = @_;

    my $v = $excl_time;
    $total_in += $v;

    # normalize and merge sibling string evals by setting eval seqn to 0
    $subname =~ s/\( (\w*eval)\s\d+ \) (?= \[ .+? :\d+ \] )/($1 0)/gx;
    # assign an id to the subname for memory efficiency
    my $subid = $subname2id{$subname} ||= ++$last_subid;

    # Either...
    # a) we're returning from some sub deeper than the current stack
    #    in which case we push unnamed sub calls ("0") onto the stack
    #    till we get to the right depth, then fall through to:
    # b) we're returning from the sub on top of the stack.

    while (@stack <= $retn_depth) {
        # build out the tree if needed
        my $crnt_node = $stack[-1];
        die "panic" if $crnt_node->{0};
        push @stack, ($crnt_node->{0} = {});
    }

    # top of stack:  sub we're returning from
    # next on stack: sub that was the caller
    my $sub_return = pop @stack;
    my $sub_caller = $stack[-1] || die "panic";

    die "panic" unless $sub_return == $sub_caller->{0};
    delete $sub_caller->{0} or die "panic"; # == $sub_return

    # {
    #   0 - as-yet un-returned subs
    #   'v' - cumulative excl_time in this sub
    #   $subid1 => {...} # calls to $subid1 made by this sub
    #   $subid2 => {...}
    # }

    $sub_return->{v} += $v;
    _merge_sub_return_into_caller($sub_caller->{$subid} ||= {}, $sub_return);
}


# build hash of call paths ("subid;subid;subid" => value) from the call tree
sub extract_call_path_hash {
    my ($local_root) = @_;

    my %subid_call_path_hash;
    visit_nodes_depth_first($local_root, [], sub {
        my ($node, $path) = @_;
        $subid_call_path_hash{ join(";", @$path) } += $node->{v}
            if @$path;
        %$node = (); # reclaim memory as we go
    });
    return \%subid_call_path_hash;
}


sub output_call_path_hash {
    my ($subid_call_path_hash) = @_;

    # ensure subnames don't contain ";" or " "
    tr/; /??/ for values %subname2id;
    my %subid2name = reverse % subname2id;

    # output the totals without scaling, so they're in ticks_per_sec units
    my $val_scale_factor = 1; # ($opt_calls) ? 1 : 1_000_000 / $attribute{ticks_per_sec};
    my $val_format = ($val_scale_factor == 1) ? "%s %d\n" : "%s %.1f\n";
    my $total_out = 0;

    # output the subid_call_path_hash hash using subroutine names
    my @keys = keys %$subid_call_path_hash;
    for my $subidpath (@keys) {
        my @path = map {$subid2name{$_}} split ";", $subidpath;
        my $v = $subid_call_path_hash->{$subidpath};
        printf $val_format, join(";", @path), $v * $val_scale_factor;
        $total_out += $v;
    }

    warn "nytprofcalls inconsistency: total in $total_in doesn't match total out $total_out\n"
        if $total_in != $total_out;
}


sub _merge_sub_return_into_caller {
    my ($dest, $new, undef) = @_;
    $dest->{v} += delete $new->{v};
    while (my ($new_called_subid, $new_called_node) = each %$new) {
        if ($dest->{$new_called_subid}) {
            _merge_sub_return_into_caller($dest->{$new_called_subid}, $new_called_node);
        }
        else {
            $dest->{$new_called_subid} = $new_called_node;
        }
    }
}


sub visit_nodes_depth_first {
    # depth first
    my $node = shift;
    my $path = shift;
    my $sub = shift;

    push @$path, undef;
    while (my ($subid, $childnode) = each %$node) {
        next if $subid eq 'v';
        die "panic" if $subid eq '0';

        $path->[-1] = $subid;
        visit_nodes_depth_first($childnode, $path, $sub);
    }
    pop @$path;

    $sub->($node, $path);
}

