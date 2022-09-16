package Mojolicious::Plugins;
use Mojo::Base 'Mojo::EventEmitter';

use Mojo::Loader 'load_class';
use Mojo::Util 'camelize';

has namespaces => sub { ['Mojolicious::Plugin'] };

sub emit_chain {
  my ($self, $name, @args) = @_;

  my $wrapper;
  for my $cb (reverse @{$self->subscribers($name)}) {
    my $next = $wrapper;
    $wrapper = sub { $cb->($next, @args) };
  }

  !$wrapper ? return : return $wrapper->();
}

sub emit_hook {
  my $self = shift;
  for my $cb (@{$self->subscribers(shift)}) { $cb->(@_) }
  return $self;
}

sub emit_hook_reverse {
  my $self = shift;
  for my $cb (reverse @{$self->subscribers(shift)}) { $cb->(@_) }
  return $self;
}

sub load_plugin {
  my ($self, $name) = @_;

  # Try all namespaces and full module name
  my $suffix  = $name =~ /^[a-z]/ ? camelize $name : $name;
  my @classes = map {"${_}::$suffix"} @{$self->namespaces};
  for my $class (@classes, $name) { return $class->new if _load($class) }

  # Not found
  die qq{Plugin "$name" missing, maybe you need to install it?\n};
}

sub register_plugin {
  shift->load_plugin(shift)->register(shift, ref $_[0] ? $_[0] : {@_});
}

sub _load {
  my $module = shift;
  return $module->isa('Mojolicious::Plugin') unless my $e = load_class $module;
  ref $e ? die $e : return undef;
}

1;

=encoding utf8

=head1 NAME

Mojolicious::Plugins - Plugin manager

=head1 SYNOPSIS

  use Mojolicious::Plugins;

  my $plugins = Mojolicious::Plugins->new;
  push @{$plugins->namespaces}, 'MyApp::Plugin';

=head1 DESCRIPTION

L<Mojolicious::Plugins> is the plugin manager of L<Mojolicious>.

=head1 PLUGINS

The following plugins are included in the L<Mojolicious> distribution as
examples.

=over 2

=item L<Mojolicious::Plugin::Config>

Perl-ish configuration files.

=item L<Mojolicious::Plugin::DefaultHelpers>

General purpose helper collection, loaded automatically.

=item L<Mojolicious::Plugin::EPLRenderer>

Renderer for plain embedded Perl templates, loaded automatically.

=item L<Mojolicious::Plugin::EPRenderer>

Renderer for more sophisticated embedded Perl templates, loaded automatically.

=item L<Mojolicious::Plugin::HeaderCondition>

Route condition for all kinds of headers, loaded automatically.

=item L<Mojolicious::Plugin::JSONConfig>

JSON configuration files.

=item L<Mojolicious::Plugin::Mount>

Mount whole L<Mojolicious> applications.

=item L<Mojolicious::Plugin::TagHelpers>

Template specific helper collection, loaded automatically.

=back

=head1 EVENTS

L<Mojolicious::Plugins> inherits all events from L<Mojo::EventEmitter>.

=head1 ATTRIBUTES

L<Mojolicious::Plugins> implements the following attributes.

=head2 namespaces

  my $namespaces = $plugins->namespaces;
  $plugins       = $plugins->namespaces(['Mojolicious::Plugin']);

Namespaces to load plugins from, defaults to L<Mojolicious::Plugin>.

  # Add another namespace to load plugins from
  push @{$plugins->namespaces}, 'MyApp::Plugin';

=head1 METHODS

L<Mojolicious::Plugins> inherits all methods from L<Mojo::EventEmitter> and
implements the following new ones.

=head2 emit_chain

  $plugins->emit_chain('foo');
  $plugins->emit_chain(foo => 123);

Emit events as chained hooks.

=head2 emit_hook

  $plugins = $plugins->emit_hook('foo');
  $plugins = $plugins->emit_hook(foo => 123);

Emit events as hooks.

=head2 emit_hook_reverse

  $plugins = $plugins->emit_hook_reverse('foo');
  $plugins = $plugins->emit_hook_reverse(foo => 123);

Emit events as hooks in reverse order.

=head2 load_plugin

  my $plugin = $plugins->load_plugin('some_thing');
  my $plugin = $plugins->load_plugin('SomeThing');
  my $plugin = $plugins->load_plugin('MyApp::Plugin::SomeThing');

Load a plugin from the configured namespaces or by full module name.

=head2 register_plugin

  $plugins->register_plugin('some_thing', Mojolicious->new);
  $plugins->register_plugin('some_thing', Mojolicious->new, foo => 23);
  $plugins->register_plugin('some_thing', Mojolicious->new, {foo => 23});
  $plugins->register_plugin('SomeThing', Mojolicious->new);
  $plugins->register_plugin('SomeThing', Mojolicious->new, foo => 23);
  $plugins->register_plugin('SomeThing', Mojolicious->new, {foo => 23});
  $plugins->register_plugin('MyApp::Plugin::SomeThing', Mojolicious->new);
  $plugins->register_plugin(
    'MyApp::Plugin::SomeThing', Mojolicious->new, foo => 23);
  $plugins->register_plugin(
    'MyApp::Plugin::SomeThing', Mojolicious->new, {foo => 23});

Load a plugin from the configured namespaces or by full module name and run
C<register>, optional arguments are passed through.

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut
