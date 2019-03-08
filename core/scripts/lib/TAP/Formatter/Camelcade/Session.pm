package TAP::Formatter::Camelcade::Session;
use strict;
use warnings FATAL => 'all';
use base 'TAP::Formatter::Session';
use TAP::Formatter::Camelcade::MessageBuilder;
use Time::HiRes qw/time/;

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub builder {
    return 'TAP::Formatter::Camelcade::MessageBuilder';
}

=pod

Invoked for every line of parsed result

=cut

sub result {
    my $self = shift;
    #@type TAP::Parser::Result
    my $result = shift;

    my $type = $result->type;
    if( $type eq 'plan'){
        $self->handle_plan($result);
    }
    elsif( $type eq 'pragma'){
        $self->handle_pragma($result);
    }
    elsif( $type eq 'test'){
        $self->handle_test($result);
    }
    elsif( $type eq 'comment'){
        $self->handle_comment($result);
    }
    elsif( $type eq 'bailout'){
        $self->handle_bailout($result);
    }
    elsif( $type eq 'version'){
        $self->handle_version($result);
    }
    elsif( $type eq 'unknown'){
        $self->handle_unknown($result);
    }
    elsif( $type eq 'yaml'){
        $self->handle_yaml($result);
    }
    else{
        die "Unknown type: $type";
    }
}

=pod

We are keeping last test failure to append comments into it

=cut

sub finish_last_test {
    my $self = shift;
    my $last_test = $self->get_last_test;
    if (defined $last_test) {
        $self->set_last_time;
        builder->test_finished(@$last_test{qw/name duration/})->print;
    }
    $self->{last_test} = undef;
}

sub set_last_test {
    my ($self, $test_name, $test_duration) = @_;
    $self->{last_test} = {
        name     => $test_name,
        duration => $test_duration
    };
}

sub set_last_time {
    my $self = shift;
    $self->{last_time} = time ;
}

sub get_last_time{
    my $self = shift;
    return $self->{last_time} // time;
}

sub get_last_test {
    my $self = shift;
    return $self->{last_test};
}

sub _compute_test_name {
    shift;
    #@type TAP::Parser::Result::Test
    my $result = shift;

    my $description = $result->description;
    my $test_name = $description eq q{} ? $result->explanation : $description;
    $test_name =~ s/^-\s//;
    $test_name = 'Unnamed test (#' . $result->number . ')' if $test_name eq q{};
    return $test_name;
}

sub handle_plan{
    my $self = shift;
    #@type TAP::Parser::Result::Plan
    my $result = shift;
    $self->finish_last_test;

    if ($result->directive eq 'SKIP') {
        builder->output($result->explanation);
    }
}

sub handle_pragma{
    my $self = shift;
    #@type TAP::Parser::Result::Pragma
    my $result = shift;
    $self->process_as_comment($result);
}

=pod

Invoked for every test result

=cut

sub handle_test{
    my $self = shift;
    #@type TAP::Parser::Result::Test
    my $result = shift;

    my $last_time = $self->get_last_time;

    my $test_name = $self->_compute_test_name($result);
    if ($self->is_subtest($test_name)) {
        $self->finish_subtest;
    }
    else {
        $self->finish_last_test;
        builder->test_started($test_name)->print;
        $self->set_last_test($test_name, int((time - $last_time)*1000));
        if (!$result->is_ok) {
            builder->test_failed($test_name, $result->explanation)->print;
        }
    }
}

sub handle_comment{
    my $self = shift;
    #@type TAP::Parser::Result::Comment
    my $result = shift;

    my $comment = $result->raw;

    if ($comment =~ /^\s*# Subtest: (.+)$/) {
        $self->start_subtest($1);
    }
    else {
        my $last_test = $self->get_last_test;
        if ($last_test) {
            builder->stderr($last_test->{name}, $comment)->print;
        }
        else {
            builder->warning($comment);
        }
    }
}

sub handle_bailout{
    my $self = shift;
    #@type TAP::Parser::Result::Bailout
    my $result = shift;
    $self->process_as_comment($result);
}

sub handle_version{
    my $self = shift;
    #@type TAP::Parser::Result::Version
    my $result = shift;
    $self->process_as_comment($result);
}

sub handle_unknown{
    my $self = shift;
    #@type TAP::Parser::Result::Unknown
    my $result = shift;
    my $raw = $result->raw;
    if ($self->in_in_subtest && $raw =~ /^\s*(((?:not )?ok) ([0-9]+)(?: (- .*))?)$/) {
        my $test_raw = $1;
        my $is_ok = $2;
        my $test_num = $3;
        my $description = $4 // "";
        $self->result(TAP::Parser::Result::Test->new({
            raw         => $test_raw,
            ok          => $is_ok,
            test_num    => $test_num,
            description => $description,
            type        => 'test',
            explanation => '',
            directive   => '',
        }));
    }
    elsif ($raw =~ /^\s+# Subtest: (.+)$/) {
        $self->start_subtest($1);
    }
    elsif( $raw =~ /^\s*\d+\.\.\d+$/){
        # ignore
    }
    else {
        $self->process_as_comment($result);
    }
}

#@returns TAP::Parser::Result::Comment
sub process_as_comment {
    my $self = shift;
    #@type TAP::Parser::Result
    my $result = shift;
    my $comment = $result->raw;
    $comment =~ s/^\s+//;
    # use Data::Dumper;    print STDERR "Handling: ".Dumper($result);
    $self->result(TAP::Parser::Result::Comment->new({
        raw     => $result->raw,
        type    => 'comment',
        comment => $comment
    }));
}

sub handle_yaml{
    my $self = shift;
    #@type TAP::Parser::Result::YAML
    my $result = shift;
    $self->process_as_comment($result);
}

sub close_test {
    my $self = shift;
    $self->finish_last_test;
    builder->test_suite_finished($self->name)->print;
}

#@override
sub _initialize {
    my ($self, $arg_for) = @_;

    $self->{location} = delete $$arg_for{location};

    $self->SUPER::_initialize($arg_for);

    # $arg_for has parser, name, formatter
    builder->test_suite_started($self->name, $self->{location})->print;
    $self->{subtests} = [];
    $self->set_last_time;
    return $self;
}


sub start_subtest {
    my $self = shift;
    my $subtest_name = shift;
    $self->finish_last_test;
    push @{$self->{subtests}}, $subtest_name;
    builder->test_suite_started($subtest_name)->print;
}

sub is_subtest {
    my $self = shift;
    my $test_name = shift;
    my $last_index = $#{$self->{subtests}};
    return $last_index > -1 && $self->{subtests}->[$last_index] eq $test_name;
}

sub in_in_subtest {
    my $self = shift;
    return scalar @{$self->{subtests}};
}

sub finish_subtest {
    my $self = shift;
    my $subtest_name = pop @{$self->{subtests}};
    return unless $subtest_name;
    $self->finish_last_test;
    builder->test_suite_finished($subtest_name)->print;
}

#@method
#@override
sub _should_show_count {
    0;
}

1;