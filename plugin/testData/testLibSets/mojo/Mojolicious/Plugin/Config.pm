package Mojolicious::Plugin::Config;
use Mojo::Base 'Mojolicious::Plugin';

use Mojo::File 'path';
use Mojo::Util 'decode';

sub load { $_[0]->parse(decode('UTF-8', path($_[1])->slurp), @_[1, 2, 3]) }

sub parse {
  my ($self, $content, $file, $conf, $app) = @_;

  # Run Perl code in sandbox
  my $config = eval 'package Mojolicious::Plugin::Config::Sandbox; no warnings;'
    . "sub app; local *app = sub { \$app }; use Mojo::Base -strict; $content";
  die qq{Can't load configuration from file "$file": $@} if $@;
  die qq{Configuration file "$file" did not return a hash reference.\n}
    unless ref $config eq 'HASH';

  return $config;
}

sub register {
  my ($self, $app, $conf) = @_;

  # Override
  $app->defaults(config => $app->config);
  return $app->config if $app->config->{config_override};

  # Config file
  my $file = $conf->{file} || $ENV{MOJO_CONFIG};
  $file ||= $app->moniker . '.' . ($conf->{ext} || 'conf');

  # Mode specific config file
  my $mode = $file =~ /^(.*)\.([^.]+)$/ ? join('.', $1, $app->mode, $2) : '';

  my $home = $app->home;
  $file = $home->child($file) unless path($file)->is_abs;
  $mode = $home->child($mode) if $mode && !path($mode)->is_abs;
  $mode = undef unless $mode && -e $mode;

  # Read config file
  my $config = {};
  if (-e $file) { $config = $self->load($file, $conf, $app) }

  # Check for default and mode specific config file
  elsif (!$conf->{default} && !$mode) {
    die qq{Configuration file "$file" missing, maybe you need to create it?\n};
  }

  # Merge everything
  $config = {%$config, %{$self->load($mode, $conf, $app)}} if $mode;
  $config = {%{$conf->{default}}, %$config} if $conf->{default};
  return $app->config($config)->config;
}

1;

=encoding utf8

=head1 NAME

Mojolicious::Plugin::Config - Perl-ish configuration plugin

=head1 SYNOPSIS

  # myapp.conf (it's just Perl returning a hash)
  {
    # Just a value
    foo => "bar",

    # Nested data structures are fine too
    baz => ['♥'],

    # You have full access to the application
    music_dir => app->home->child('music')
  };

  # Mojolicious
  my $config = $app->plugin('Config');
  say $config->{foo};

  # Mojolicious::Lite
  my $config = plugin 'Config';
  say $config->{foo};

  # foo.html.ep
  %= $config->{foo}

  # The configuration is available application-wide
  my $config = app->config;
  say $config->{foo};

  # Everything can be customized with options
  my $config = plugin Config => {file => '/etc/myapp.stuff'};

=head1 DESCRIPTION

L<Mojolicious::Plugin::Config> is a Perl-ish configuration plugin.

The application object can be accessed via C<$app> or the C<app> function,
L<strict>, L<warnings>, L<utf8> and Perl 5.10 L<features|feature> are
automatically enabled. A default configuration filename in the application home
directory will be generated from the value of L<Mojolicious/"moniker">
(C<$moniker.conf>). You can extend the normal configuration file
C<$moniker.conf> with C<mode> specific ones like C<$moniker.$mode.conf>, which
will be detected automatically.

If the configuration value C<config_override> has been set in
L<Mojolicious/"config"> when this plugin is loaded, it will not do anything.

The code of this plugin is a good example for learning to build new plugins,
you're welcome to fork it.

See L<Mojolicious::Plugins/"PLUGINS"> for a list of plugins that are available
by default.

=head1 OPTIONS

L<Mojolicious::Plugin::Config> supports the following options.

=head2 default

  # Mojolicious::Lite
  plugin Config => {default => {foo => 'bar'}};

Default configuration, making configuration files optional.

=head2 ext

  # Mojolicious::Lite
  plugin Config => {ext => 'stuff'};

File extension for generated configuration filenames, defaults to C<conf>.

=head2 file

  # Mojolicious::Lite
  plugin Config => {file => 'myapp.conf'};
  plugin Config => {file => '/etc/foo.stuff'};

Path to configuration file, absolute or relative to the application home
directory, defaults to the value of the C<MOJO_CONFIG> environment variable or
C<$moniker.conf> in the application home directory.

=head1 METHODS

L<Mojolicious::Plugin::Config> inherits all methods from L<Mojolicious::Plugin>
and implements the following new ones.

=head2 load

  $plugin->load($file, $conf, $app);

Loads configuration file and passes the content to L</"parse">.

  sub load {
    my ($self, $file, $conf, $app) = @_;
    ...
    return $self->parse($content, $file, $conf, $app);
  }

=head2 parse

  $plugin->parse($content, $file, $conf, $app);

Parse configuration file.

  sub parse {
    my ($self, $content, $file, $conf, $app) = @_;
    ...
    return $hash;
  }

=head2 register

  my $config = $plugin->register(Mojolicious->new);
  my $config = $plugin->register(Mojolicious->new, {file => '/etc/app.conf'});

Register plugin in L<Mojolicious> application and merge configuration.

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut
