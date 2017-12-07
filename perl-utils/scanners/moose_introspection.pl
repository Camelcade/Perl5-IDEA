package MyMoose::Role;
use Moose::Role;

#has mojo1 => sub {};
#has 'mojo2';

# has moose1 => (); # error, no is

has moose2 => (
        is => 'rw'
    );
has moose3 => (
        is => 'ro'
    );

has moose4 => (
        accessor => 'moose4_accessor'
    );

has moose5 => (
        reader => 'moose5_reader'
    );

has moose51 => (
        is     => 'rw',
        reader => 'moose51_reader'
    );

has moose6 => (
        writer => 'moose6_writer'
    );

has moose7 => (
        predicate => 'moose7_predicate'
    );

has moose8 => (
        clearer => 'moose8_clearer'
    );

has moose9 => (
        is     => 'rw',
        writer => 'moose9_writer',
        isa    => 'Some::Foo::Bar'
    );

has moose10 => (
        is   => 'rw',
        #        isa  => 'Some::Foo::Bar',
        does => 'Some::Foo::Role'
    );

has mooose11 => (
        is       => 'rw',
        required => 1
    );

has moose12 => (
        is      => 'rw',
        handles => {
            moose12_delegate1 => 'moose9',
            moose12_delegate2 => 'moose8',
        }
    );

has moose13 => (
        is      => 'rw',
        handles => [ qw/moose2 moose3/ ]
    );

package MyOtherMoose;
use Moose;
with 'MyMoose::Role';

has '+moose12' => (

    );


package main;
use B::Deparse qw//;
use Sub::Identify qw//;
use v5.10;

my $deparser = B::Deparse->new();

{
    no strict 'refs';
    foreach my $key (sort keys %::MyOtherMoose::)
    {
        my $coderef = *{$::MyOtherMoose::{$key}}{CODE};
        if ($coderef) {
            my $target_name = Sub::Identify::sub_fullname( $coderef );
            say sprintf "# %s\nsub %s %s\n", $target_name, $key, $deparser->coderef2text($coderef);
        }
    }
}
