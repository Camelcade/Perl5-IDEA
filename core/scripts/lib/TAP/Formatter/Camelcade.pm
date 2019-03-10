package TAP::Formatter::Camelcade;
use strict;
use warnings FATAL => 'all';
use base 'TAP::Formatter::Base';
use TAP::Formatter::Camelcade::Session;
use TAP::Formatter::Camelcade::MessageBuilder;
use Cwd;

sub open_test{
    my ( $self, $test, $parser ) = @_;

    my $pwd = getcwd;

    my $location = "myfile://$test";
    $test =~ s/^$pwd[\\\/]//;

    my $session = TAP::Formatter::Camelcade::Session->new({
        name      => $test,
        formatter => $self,
        parser    => $parser,
        location  => $location
    });

    $session->header;

    return $session;
}

#@returns TAP::Formatter::Camelcade::MessageBuilder
sub builder{
    return 'TAP::Formatter::Camelcade::MessageBuilder';
}

sub _should_show_count {
    return 0;
}

#@override
sub _initialize {
    my ($self, $arg_for) = @_;
    builder->test_reporter_attached->print;
    $self->SUPER::_initialize($arg_for);
}

#@override
sub prepare {
    my ($self, @tests) = @_;
    $self->SUPER::prepare(@tests);
    builder->testing_started;
}

#@override
sub summary {
    my ($self, $aggregate, $interrupted) = @_;
    $self->SUPER::summary($aggregate, $interrupted);
    builder->testing_finished;
}

1;