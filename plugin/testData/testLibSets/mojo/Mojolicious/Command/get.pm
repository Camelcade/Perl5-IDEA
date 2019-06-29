package Mojolicious::Command::get;
use Mojo::Base 'Mojolicious::Command';

use Mojo::DOM;
use Mojo::IOLoop;
use Mojo::JSON qw(to_json j);
use Mojo::JSON::Pointer;
use Mojo::URL;
use Mojo::UserAgent;
use Mojo::Util qw(decode encode getopt);
use Scalar::Util 'weaken';

has description => 'Perform HTTP request';
has usage       => sub { shift->extract_usage };

sub run {
  my ($self, @args) = @_;

  # Data from STDIN
  vec(my $r = '', fileno(STDIN), 1) = 1;
  my $in = !-t STDIN && select($r, undef, undef, 0) ? join '', <STDIN> : undef;

  my $ua = Mojo::UserAgent->new(ioloop => Mojo::IOLoop->singleton);
  my %form;
  getopt \@args,
    'C|charset=s' => \my $charset,
    'c|content=s' => \$in,
    'f|form=s'    => sub { _form(\%form) if $_[1] =~ /^(.+)=(\@?)(.+)$/ },
    'H|header=s'  => \my @headers,
    'i|inactivity-timeout=i' => sub { $ua->inactivity_timeout($_[1]) },
    'k|insecure'             => sub { $ua->insecure(1) },
    'M|method=s'             => \(my $method = 'GET'),
    'o|connect-timeout=i'    => sub { $ua->connect_timeout($_[1]) },
    'r|redirect'             => \my $redirect,
    'S|response-size=i'      => sub { $ua->max_response_size($_[1]) },
    'u|user=s'               => \my $user,
    'v|verbose'              => \my $verbose;

  @args = map { decode 'UTF-8', $_ } @args;
  die $self->usage unless my $url = shift @args;
  my $selector = shift @args;

  # Parse header pairs
  my %headers = map { /^\s*([^:]+)\s*:\s*(.*+)$/ ? ($1, $2) : () } @headers;

  # Detect proxy for absolute URLs
  $url !~ m!^/! ? $ua->proxy->detect : $ua->server->app($self->app);
  $url = Mojo::URL->new($url)->userinfo($user) if $user;
  $ua->max_redirects(10) if $redirect;

  my $buffer = '';
  $ua->on(
    start => sub {
      my ($ua, $tx) = @_;

      # Verbose
      weaken $tx;
      $tx->res->content->on(
        body => sub { warn _header($tx->req), _header($tx->res) })
        if $verbose;

      # Stream content (ignore redirects)
      $tx->res->content->unsubscribe('read')->on(
        read => sub {
          return if $redirect && $tx->res->is_redirect;
          defined $selector ? ($buffer .= pop) : print pop;
        }
      );
    }
  );

  # Switch to verbose for HEAD requests
  $verbose = 1 if $method eq 'HEAD';
  STDOUT->autoflush(1);
  my @content = %form ? (form => \%form) : defined $in ? ($in) : ();
  my $tx      = $ua->start($ua->build_tx($method, $url, \%headers, @content));
  my $res     = $tx->result;

  # JSON Pointer
  return undef unless defined $selector;
  return _json($buffer, $selector) if !length $selector || $selector =~ m!^/!;

  # Selector
  $charset //= $res->content->charset || $res->default_charset;
  _select($buffer, $selector, $charset, @args);
}

sub _form { push @{$_[0]{$1}}, $2 ? {file => $3} : $3 }

sub _header { $_[0]->build_start_line, $_[0]->headers->to_string, "\n\n" }

sub _json {
  return unless my $data = j(shift);
  return unless defined($data = Mojo::JSON::Pointer->new($data)->get(shift));
  _say(ref $data eq 'HASH' || ref $data eq 'ARRAY' ? to_json($data) : $data);
}

sub _say { length && say encode('UTF-8', $_) for @_ }

sub _select {
  my ($buffer, $selector, $charset, @args) = @_;

  # Keep a strong reference to the root
  $buffer = decode($charset, $buffer) // $buffer if $charset;
  my $dom     = Mojo::DOM->new($buffer);
  my $results = $dom->find($selector);

  while (defined(my $command = shift @args)) {

    # Number
    ($results = $results->slice($command)) and next if $command =~ /^\d+$/;

    # Text
    return _say($results->map('text')->each) if $command eq 'text';

    # All text
    return _say($results->map('all_text')->each) if $command eq 'all';

    # Attribute
    return _say($results->map(attr => $args[0] // '')->each)
      if $command eq 'attr';

    # Unknown
    die qq{Unknown command "$command".\n};
  }

  _say($results->each);
}

1;

=encoding utf8

=head1 NAME

Mojolicious::Command::get - Get command

=head1 SYNOPSIS

  Usage: APPLICATION get [OPTIONS] URL [SELECTOR|JSON-POINTER] [COMMANDS]

    ./myapp.pl get /
    ./myapp.pl get -H 'Accept: text/html' /hello.html 'head > title' text
    ./myapp.pl get //sri:secr3t@/secrets.json /1/content
    mojo get mojolicious.org
    mojo get -v -r -o 25 -i 50 google.com
    mojo get -v -H 'Host: mojolicious.org' -H 'Accept: */*' mojolicious.org
    mojo get -u 'sri:s3cret' https://mojolicious.org
    mojo get mojolicious.org > example.html
    mojo get -M PUT mojolicious.org < example.html
    mojo get -f 'q=Mojolicious' -f 'size=5' https://metacpan.org/search
    mojo get -M POST -f 'upload=@example.html' mojolicious.org
    mojo get mojolicious.org 'head > title' text
    mojo get mojolicious.org .footer all
    mojo get mojolicious.org a attr href
    mojo get mojolicious.org '*' attr id
    mojo get mojolicious.org 'h1, h2, h3' 3 text
    mojo get https://fastapi.metacpan.org/v1/author/SRI /name
    mojo get -H 'Host: example.com' http+unix://%2Ftmp%2Fmyapp.sock/index.html

  Options:
    -C, --charset <charset>              Charset of HTML/XML content, defaults
                                         to auto-detection
    -c, --content <content>              Content to send with request
    -f, --form <name=value>              One or more form values and file
                                         uploads
    -H, --header <name:value>            One or more additional HTTP headers
    -h, --help                           Show this summary of available options
        --home <path>                    Path to home directory of your
                                         application, defaults to the value of
                                         MOJO_HOME or auto-detection
    -i, --inactivity-timeout <seconds>   Inactivity timeout, defaults to the
                                         value of MOJO_INACTIVITY_TIMEOUT or 20
    -k, --insecure                       Do not require a valid TLS certificate
                                         to access HTTPS sites
    -M, --method <method>                HTTP method to use, defaults to "GET"
    -m, --mode <name>                    Operating mode for your application,
                                         defaults to the value of
                                         MOJO_MODE/PLACK_ENV or "development"
    -o, --connect-timeout <seconds>      Connect timeout, defaults to the value
                                         of MOJO_CONNECT_TIMEOUT or 10
    -r, --redirect                       Follow up to 10 redirects
    -S, --response-size <size>           Maximum response size in bytes,
                                         defaults to 2147483648 (2GiB)
    -u, --user <userinfo>                Alternate mechanism for specifying
                                         colon-separated username and password
    -v, --verbose                        Print request and response headers to
                                         STDERR

=head1 DESCRIPTION

L<Mojolicious::Command::get> is a command line interface for
L<Mojo::UserAgent>.

This is a core command, that means it is always enabled and its code a good
example for learning to build new commands, you're welcome to fork it.

See L<Mojolicious::Commands/"COMMANDS"> for a list of commands that are
available by default.

=head1 ATTRIBUTES

L<Mojolicious::Command::get> performs requests to remote hosts or local
applications.

=head2 description

  my $description = $get->description;
  $get            = $get->description('Foo');

Short description of this command, used for the command list.

=head2 usage

  my $usage = $get->usage;
  $get      = $get->usage('Foo');

Usage information for this command, used for the help screen.

=head1 METHODS

L<Mojolicious::Command::get> inherits all methods from L<Mojolicious::Command>
and implements the following new ones.

=head2 run

  $get->run(@ARGV);

Run this command.

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut
