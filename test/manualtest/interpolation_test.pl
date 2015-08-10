#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

our $test;
my $abc = 'abc scalar';
my @abc = ('abc array');
my %abc = (key => 'abc hash');
my $abc_ref = \'scalar_ref';
my $abc_aref = ['array_ref'];

say "test $abc test";
say "test @abc test";
say "test %abc test";
say "test *test test";
say "test $#abc test";
say "test $abc[0] test";
say "test $abc{key} test";
say "test $abc{'key'} test";
say "test $$abc_ref test";
say "test @$abc_aref test";

say "test ${abc} test";
say "test @{abc} test";
say "test %{abc} test";
say "test *{test} test";
say "test $#{abc} test";
say "test ${abc}[0] test";
say "test ${abc}{key} test";
say "test ${abc}{'key'} test";
say "test ${${abc_ref}} test";
say "test @{${abc_aref}} test";
say "This is an auto-completion $UNIV  test"
