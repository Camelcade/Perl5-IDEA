package TAP::Formatter::Camelcade::MessageBuilder;
use strict;
use warnings FATAL => 'all';
use Time::HiRes qw( time );

# taken from TeamCity::Message
sub tc_timestamp {
    my $now = time;
    my ( $s, $mi, $h, $d, $mo, $y ) = ( gmtime($now) )[ 0 .. 5 ];

    my $float = ( $now - int($now) );
    return sprintf(
        '%4d-%02d-%02dT%02d:%02d:%02d.%03d',
        $y + 1900, $mo + 1, $d,
        $h, $mi, $s,

        # We only need 3 places of precision so if we multiply it by 1,000 we
        # can just treat it as an integer.
        $float * 1000,
    );
}

sub new {
    my $proto = shift;
    my $messageName = shift;
    my %kwargs = @_;
    $kwargs{messageName} = $messageName;
    $kwargs{timestamp} = tc_timestamp;
    return bless { %kwargs }, $proto;
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub set {
    my $self = shift;
    my %kwargs = @_;
    my @keys = keys %kwargs;
    @$self{@keys} = @kwargs{@keys};
    return $self;
}

sub testing_started {
    my $self = shift;
    $self->new('testingStarted')->print;
}

sub testing_finished {
    my $self = shift;
    $self->new('testingFinished')->print;
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_count {
    my $self = shift;
    my $count = shift // 0;
    return $self->new('testCount', count => $count);
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub custom_status {
    my $self = shift;
    # type, testStarted, testFailed, testsCategory, count
    return $self->new('customProgressStatus');
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub message {
    my $self = shift;
    my $text = shift;
    my $status = shift;
    return $self->new('message', text => $text, status => $status); # could be ERROR
}

sub error{
    my $self = shift;
    my $text = join "\n", @_;
    $self->message($text, 'ERROR')->print;
}

sub warning{
    my $self = shift;
    my $text = join "\n", @_;
    $self->message($text, 'WARNING')->print;
}

sub output {
    my $self = shift;
    my $text = join "\n", @_;
    $self->message($text, 'NORMAL')->print;
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub stderr {
    my $self = shift;
    my $test_name = shift;
    my $text = shift;
    return $self->new('testStdErr', out => "$text\n", name => $test_name);
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_reporter_attached {
    my $self = shift;
    # durationStrategy
    return $self->new('enteredTheMatrix');
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub suite_tree_started {
    my $self = shift;
    my $name = shift;
    return $self->new('suiteTreeStarted',
        name         => $name,
        nodeId       => "",
        locationHint => $name,
        metainfo     => "",
        parentNodeId => ""
    );
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_suite_started {
    my $self = shift;
    my $name = shift;
    my $location_hint = shift || $name;
    return $self->new('testSuiteStarted',
        name         => $name,
        locationHint => $location_hint
    );
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_suite_finished {
    my $self = shift;
    my $name = shift;
    return $self->new('testSuiteFinished',
        name         => $name,
        locationHint => $name
    );
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub suite_tree_ended {
    my $self = shift;
    my $name = shift;
    return $self->new('suiteTreeEnded',
        name         => $name,
        nodeId       => $name,
        locationHint => "",
        metainfo     => "",
        parentNodeId => ""
    );
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub suite_tree_node {
    my $self = shift;
    my $name = shift;
    # locationHint, parentNodeId, nodeId, metainfo
    return $self->new('suiteTreeStarted', name => escape($name));
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub build_tree_ended {
    my $self = shift;
    return $self->new('treeEnded');
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub root_presentation {
    my $self = shift;
    # comment, location
    return $self->new('rootName');
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_custom {
    my $self = shift;
    my $type = shift;
    return $self->new('customProgressStatus', type => $type, @_);
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_started {
    my $self = shift;
    my $name = shift;
    return $self->new('testStarted', name => $name, @_);
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_failed {
    my $self = shift;
    my $name = shift;
    my $message = shift;
    return $self->new('testFailed', name => $name, message => $message, @_);
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub test_finished {
    my $self = shift;
    my $name = shift;
    my $duration = shift // 0;
    return $self->new('testFinished', name => $name, duration => $duration, @_);
}

sub escape {
    my $text = shift;
    if (ref $text) {
        $text = shift;
    }
    $text =~ s{(['\n\r|\[\]])}/
            if ($1 eq "\n") {
                "|n"
            }
            elsif ($1 eq "\r") {
                "|r"
            }
            else {
                "|$1"
            }
    /ges;
    return $text;
}

sub as_string {
    my $self = shift;

    my @message = ('##teamcity[');
    my %params = (%$self);
    push @message, $params{messageName};
    delete $params{messageName};

    my $data = join ' ', map {"$_='${\escape($params{$_})}'"} grep {!ref $params{$_}} sort keys %params;
    push @message, " $data" if $data;

    push @message, "]\n";
    return join '', @message;
}

sub print {
    my $self = shift;
    # print STDERR "DATA: " . substr $self->as_string, 1;
    print $self->as_string;
}

1;