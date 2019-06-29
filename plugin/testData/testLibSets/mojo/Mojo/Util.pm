package Mojo::Util;
use Mojo::Base -strict;

use Carp qw(carp croak);
use Data::Dumper ();
use Digest::MD5 qw(md5 md5_hex);
use Digest::SHA qw(hmac_sha1_hex sha1 sha1_hex);
use Encode 'find_encoding';
use Exporter 'import';
use Getopt::Long 'GetOptionsFromArray';
use IO::Compress::Gzip;
use IO::Poll qw(POLLIN POLLPRI);
use IO::Uncompress::Gunzip;
use List::Util 'min';
use MIME::Base64 qw(decode_base64 encode_base64);
use Pod::Usage 'pod2usage';
use Sub::Util 'set_subname';
use Symbol 'delete_package';
use Time::HiRes        ();
use Unicode::Normalize ();

# Check for monotonic clock support
use constant MONOTONIC =>
  eval { !!Time::HiRes::clock_gettime(Time::HiRes::CLOCK_MONOTONIC()) };

# Punycode bootstring parameters
use constant {
  PC_BASE         => 36,
  PC_TMIN         => 1,
  PC_TMAX         => 26,
  PC_SKEW         => 38,
  PC_DAMP         => 700,
  PC_INITIAL_BIAS => 72,
  PC_INITIAL_N    => 128
};

# To generate a new HTML entity table run this command
# perl examples/entities.pl
my %ENTITIES;
for my $line (split "\n", join('', <DATA>)) {
  next unless $line =~ /^(\S+)\s+U\+(\S+)(?:\s+U\+(\S+))?/;
  $ENTITIES{$1} = defined $3 ? (chr(hex $2) . chr(hex $3)) : chr(hex $2);
}
close DATA;

# Characters that should be escaped in XML
my %XML = (
  '&'  => '&amp;',
  '<'  => '&lt;',
  '>'  => '&gt;',
  '"'  => '&quot;',
  '\'' => '&#39;'
);

# "Sun, 06 Nov 1994 08:49:37 GMT" and "Sunday, 06-Nov-94 08:49:37 GMT"
my $EXPIRES_RE = qr/(\w+\W+\d+\W+\w+\W+\d+\W+\d+:\d+:\d+\W*\w+)/;

# HTML entities
my $ENTITY_RE = qr/&(?:\#((?:[0-9]{1,7}|x[0-9a-fA-F]{1,6}));|(\w+[;=]?))/;

# Encoding and pattern cache
my (%ENCODING, %PATTERN);

our @EXPORT_OK = (
  qw(b64_decode b64_encode camelize class_to_file class_to_path decamelize),
  qw(decode deprecated dumper encode extract_usage getopt gunzip gzip),
  qw(hmac_sha1_sum html_attr_unescape html_unescape md5_bytes md5_sum),
  qw(monkey_patch punycode_decode punycode_encode quote secure_compare),
  qw(sha1_bytes sha1_sum slugify split_cookie_header split_header steady_time),
  qw(tablify term_escape trim unindent unquote url_escape url_unescape),
  qw(xml_escape xor_encode)
);

# Aliases
monkey_patch(__PACKAGE__, 'b64_decode',    \&decode_base64);
monkey_patch(__PACKAGE__, 'b64_encode',    \&encode_base64);
monkey_patch(__PACKAGE__, 'hmac_sha1_sum', \&hmac_sha1_hex);
monkey_patch(__PACKAGE__, 'md5_bytes',     \&md5);
monkey_patch(__PACKAGE__, 'md5_sum',       \&md5_hex);
monkey_patch(__PACKAGE__, 'sha1_bytes',    \&sha1);
monkey_patch(__PACKAGE__, 'sha1_sum',      \&sha1_hex);

# Use a monotonic clock if possible
monkey_patch(__PACKAGE__, 'steady_time',
  MONOTONIC
  ? sub () { Time::HiRes::clock_gettime(Time::HiRes::CLOCK_MONOTONIC()) }
  : \&Time::HiRes::time);

sub camelize {
  my $str = shift;
  return $str if $str =~ /^[A-Z]/;

  # CamelCase words
  return join '::', map {
    join('', map { ucfirst lc } split '_')
  } split '-', $str;
}

sub class_to_file {
  my $class = shift;
  $class =~ s/::|'//g;
  $class =~ s/([A-Z])([A-Z]*)/$1 . lc $2/ge;
  return decamelize($class);
}

sub class_to_path { join '.', join('/', split(/::|'/, shift)), 'pm' }

sub decamelize {
  my $str = shift;
  return $str if $str !~ /^[A-Z]/;

  # snake_case words
  return join '-', map {
    join('_', map {lc} grep {length} split /([A-Z]{1}[^A-Z]*)/)
  } split '::', $str;
}

sub decode {
  my ($encoding, $bytes) = @_;
  return undef
    unless eval { $bytes = _encoding($encoding)->decode("$bytes", 1); 1 };
  return $bytes;
}

sub deprecated {
  local $Carp::CarpLevel = 1;
  $ENV{MOJO_FATAL_DEPRECATIONS} ? croak @_ : carp @_;
}

sub dumper {
  Data::Dumper->new([@_])->Indent(1)->Sortkeys(1)->Terse(1)->Useqq(1)->Dump;
}

sub encode { _encoding($_[0])->encode("$_[1]", 0) }

sub extract_usage {
  my $file = @_ ? "$_[0]" : (caller)[1];

  open my $handle, '>', \my $output;
  pod2usage -exitval => 'noexit', -input => $file, -output => $handle;
  $output =~ s/^.*\n|\n$//;
  $output =~ s/\n$//;

  return unindent($output);
}

sub getopt {
  my ($array, $opts) = map { ref $_[0] eq 'ARRAY' ? shift : $_ } \@ARGV, [];

  my $save = Getopt::Long::Configure(qw(default no_auto_abbrev no_ignore_case),
    @$opts);
  my $result = GetOptionsFromArray $array, @_;
  Getopt::Long::Configure($save);

  return $result;
}

sub gunzip {
  my $compressed = shift;
  IO::Uncompress::Gunzip::gunzip \$compressed, \my $uncompressed
    or croak "Couldn't gunzip: $IO::Uncompress::Gunzip::GzipError";
  return $uncompressed;
}

sub gzip {
  my $uncompressed = shift;
  IO::Compress::Gzip::gzip \$uncompressed, \my $compressed
    or croak "Couldn't gzip: $IO::Compress::Gzip::GzipError";
  return $compressed;
}

sub html_attr_unescape { _html(shift, 1) }
sub html_unescape      { _html(shift, 0) }

sub monkey_patch {
  my ($class, %patch) = @_;
  no strict 'refs';
  no warnings 'redefine';
  *{"${class}::$_"} = set_subname("${class}::$_", $patch{$_}) for keys %patch;
}

# Direct translation of RFC 3492
sub punycode_decode {
  my $input = shift;
  use integer;

  my ($n, $i, $bias, @output) = (PC_INITIAL_N, 0, PC_INITIAL_BIAS);

  # Consume all code points before the last delimiter
  push @output, split('', $1) if $input =~ s/(.*)\x2d//s;

  while (length $input) {
    my ($oldi, $w) = ($i, 1);

    # Base to infinity in steps of base
    for (my $k = PC_BASE; 1; $k += PC_BASE) {
      my $digit = ord substr $input, 0, 1, '';
      $digit = $digit < 0x40 ? $digit + (26 - 0x30) : ($digit & 0x1f) - 1;
      $i += $digit * $w;
      my $t = $k - $bias;
      $t = $t < PC_TMIN ? PC_TMIN : $t > PC_TMAX ? PC_TMAX : $t;
      last if $digit < $t;
      $w *= PC_BASE - $t;
    }

    $bias = _adapt($i - $oldi, @output + 1, $oldi == 0);
    $n += $i / (@output + 1);
    $i = $i % (@output + 1);
    splice @output, $i++, 0, chr $n;
  }

  return join '', @output;
}

# Direct translation of RFC 3492
sub punycode_encode {
  my $output = shift;
  use integer;

  my ($n, $delta, $bias) = (PC_INITIAL_N, 0, PC_INITIAL_BIAS);

  # Extract basic code points
  my @input = map {ord} split '', $output;
  $output =~ s/[^\x00-\x7f]+//gs;
  my $h = my $basic = length $output;
  $output .= "\x2d" if $basic > 0;

  for my $m (sort grep { $_ >= PC_INITIAL_N } @input) {
    next if $m < $n;
    $delta += ($m - $n) * ($h + 1);
    $n = $m;

    for my $c (@input) {

      if ($c < $n) { $delta++ }
      elsif ($c == $n) {
        my $q = $delta;

        # Base to infinity in steps of base
        for (my $k = PC_BASE; 1; $k += PC_BASE) {
          my $t = $k - $bias;
          $t = $t < PC_TMIN ? PC_TMIN : $t > PC_TMAX ? PC_TMAX : $t;
          last if $q < $t;
          my $o = $t + (($q - $t) % (PC_BASE - $t));
          $output .= chr $o + ($o < 26 ? 0x61 : 0x30 - 26);
          $q = ($q - $t) / (PC_BASE - $t);
        }

        $output .= chr $q + ($q < 26 ? 0x61 : 0x30 - 26);
        $bias  = _adapt($delta, $h + 1, $h == $basic);
        $delta = 0;
        $h++;
      }
    }

    $delta++;
    $n++;
  }

  return $output;
}

sub quote {
  my $str = shift;
  $str =~ s/(["\\])/\\$1/g;
  return qq{"$str"};
}

sub secure_compare {
  my ($one, $two) = @_;
  return undef if length $one != length $two;
  my $r = 0;
  $r |= ord(substr $one, $_) ^ ord(substr $two, $_) for 0 .. length($one) - 1;
  return $r == 0;
}

sub slugify {
  my ($value, $allow_unicode) = @_;

  if ($allow_unicode) {

    # Force unicode semantics by upgrading string
    utf8::upgrade($value = Unicode::Normalize::NFKC($value));
    $value =~ s/[^\w\s-]+//g;
  }
  else {
    $value = Unicode::Normalize::NFKD($value);
    $value =~ s/[^a-zA-Z0-9_\p{PosixSpace}-]+//g;
  }
  (my $new = lc trim($value)) =~ s/[-\s]+/-/g;

  return $new;
}

sub split_cookie_header { _header(shift, 1) }
sub split_header        { _header(shift, 0) }

sub tablify {
  my $rows = shift;

  my @spec;
  for my $row (@$rows) {
    for my $i (0 .. $#$row) {
      ($row->[$i] //= '') =~ s/[\r\n]//g;
      my $len = length $row->[$i];
      $spec[$i] = $len if $len >= ($spec[$i] // 0);
    }
  }

  my @fm = (map({"\%-${_}s"} @spec[0 .. $#spec - 1]), '%s');
  return join '', map { sprintf join('  ', @fm[0 .. $#$_]) . "\n", @$_ } @$rows;
}

sub term_escape {
  my $str = shift;
  $str =~ s/([\x00-\x09\x0b-\x1f\x7f\x80-\x9f])/sprintf '\\x%02x', ord $1/ge;
  return $str;
}

sub trim {
  my $str = shift;
  $str =~ s/^\s+//;
  $str =~ s/\s+$//;
  return $str;
}

sub unindent {
  my $str = shift;
  my $min = min map { m/^([ \t]*)/; length $1 || () } split "\n", $str;
  $str =~ s/^[ \t]{0,$min}//gm if $min;
  return $str;
}

sub unquote {
  my $str = shift;
  return $str unless $str =~ s/^"(.*)"$/$1/g;
  $str =~ s/\\\\/\\/g;
  $str =~ s/\\"/"/g;
  return $str;
}

sub url_escape {
  my ($str, $pattern) = @_;

  if ($pattern) {
    unless (exists $PATTERN{$pattern}) {
      (my $quoted = $pattern) =~ s!([/\$\[])!\\$1!g;
      $PATTERN{$pattern}
        = eval "sub { \$_[0] =~ s/([$quoted])/sprintf '%%%02X', ord \$1/ge }"
        or croak $@;
    }
    $PATTERN{$pattern}->($str);
  }
  else { $str =~ s/([^A-Za-z0-9\-._~])/sprintf '%%%02X', ord $1/ge }

  return $str;
}

sub url_unescape {
  my $str = shift;
  $str =~ s/%([0-9a-fA-F]{2})/chr hex $1/ge;
  return $str;
}

sub xml_escape {
  return $_[0] if ref $_[0] && ref $_[0] eq 'Mojo::ByteStream';
  my $str = shift // '';
  $str =~ s/([&<>"'])/$XML{$1}/ge;
  return $str;
}

sub xor_encode {
  my ($input, $key) = @_;

  # Encode with variable key length
  my $len    = length $key;
  my $buffer = my $output = '';
  $output .= $buffer ^ $key
    while length($buffer = substr($input, 0, $len, '')) == $len;
  return $output .= $buffer ^ substr($key, 0, length $buffer, '');
}

sub _adapt {
  my ($delta, $numpoints, $firsttime) = @_;
  use integer;

  $delta = $firsttime ? $delta / PC_DAMP : $delta / 2;
  $delta += $delta / $numpoints;
  my $k = 0;
  while ($delta > ((PC_BASE - PC_TMIN) * PC_TMAX) / 2) {
    $delta /= PC_BASE - PC_TMIN;
    $k += PC_BASE;
  }

  return $k + (((PC_BASE - PC_TMIN + 1) * $delta) / ($delta + PC_SKEW));
}

sub _encoding {
  $ENCODING{$_[0]} //= find_encoding($_[0]) // croak "Unknown encoding '$_[0]'";
}

sub _entity {
  my ($point, $name, $attr) = @_;

  # Code point
  return chr($point !~ /^x/ ? $point : hex $point) unless defined $name;

  # Named character reference
  my $rest = my $last = '';
  while (length $name) {
    return $ENTITIES{$name} . reverse $rest
      if exists $ENTITIES{$name}
      && (!$attr || $name =~ /;$/ || $last !~ /[A-Za-z0-9=]/);
    $rest .= $last = chop $name;
  }
  return '&' . reverse $rest;
}

# Supported on Perl 5.14+
sub _global_destruction {
  defined ${^GLOBAL_PHASE} && ${^GLOBAL_PHASE} eq 'DESTRUCT';
}

sub _header {
  my ($str, $cookie) = @_;

  my (@tree, @part);
  while ($str =~ /\G[,;\s]*([^=;, ]+)\s*/gc) {
    push @part, $1, undef;
    my $expires = $cookie && @part > 2 && lc $1 eq 'expires';

    # Special "expires" value
    if ($expires && $str =~ /\G=\s*$EXPIRES_RE/gco) { $part[-1] = $1 }

    # Quoted value
    elsif ($str =~ /\G=\s*("(?:\\\\|\\"|[^"])*")/gc) { $part[-1] = unquote $1 }

    # Unquoted value
    elsif ($str =~ /\G=\s*([^;, ]*)/gc) { $part[-1] = $1 }

    # Separator
    next unless $str =~ /\G[;\s]*,\s*/gc;
    push @tree, [@part];
    @part = ();
  }

  # Take care of final part
  return [@part ? (@tree, \@part) : @tree];
}

sub _html {
  my ($str, $attr) = @_;
  $str =~ s/$ENTITY_RE/_entity($1, $2, $attr)/geo;
  return $str;
}

sub _options {

  # Hash or name (one)
  return ref $_[0] eq 'HASH' ? (undef, %{shift()}) : @_ if @_ == 1;

  # Name and values (odd)
  return shift, @_ if @_ % 2;

  # Name and hash or just values (even)
  return ref $_[1] eq 'HASH' ? (shift, %{shift()}) : (undef, @_);
}

# This may break in the future, but is worth it for performance
sub _readable { !!(IO::Poll::_poll(@_[0, 1], my $m = POLLIN | POLLPRI) > 0) }

sub _stash {
  my ($name, $object) = (shift, shift);

  # Hash
  return $object->{$name} ||= {} unless @_;

  # Get
  return $object->{$name}{$_[0]} unless @_ > 1 || ref $_[0];

  # Set
  my $values = ref $_[0] ? $_[0] : {@_};
  @{$object->{$name}}{keys %$values} = values %$values;

  return $object;
}

sub _teardown {
  return unless my $class = shift;

  # @ISA has to be cleared first because of circular references
  no strict 'refs';
  @{"${class}::ISA"} = ();
  delete_package $class;
}

1;

=encoding utf8

=head1 NAME

Mojo::Util - Portable utility functions

=head1 SYNOPSIS

  use Mojo::Util qw(b64_encode url_escape url_unescape);

  my $str = 'test=23';
  my $escaped = url_escape $str;
  say url_unescape $escaped;
  say b64_encode $escaped, '';

=head1 DESCRIPTION

L<Mojo::Util> provides portable utility functions for L<Mojo>.

=head1 FUNCTIONS

L<Mojo::Util> implements the following functions, which can be imported
individually.

=head2 b64_decode

  my $bytes = b64_decode $b64;

Base64 decode bytes with L<MIME::Base64>.

=head2 b64_encode

  my $b64 = b64_encode $bytes;
  my $b64 = b64_encode $bytes, "\n";

Base64 encode bytes with L<MIME::Base64>, the line ending defaults to a newline.

=head2 camelize

  my $camelcase = camelize $snakecase;

Convert C<snake_case> string to C<CamelCase> and replace C<-> with C<::>.

  # "FooBar"
  camelize 'foo_bar';

  # "FooBar::Baz"
  camelize 'foo_bar-baz';

  # "FooBar::Baz"
  camelize 'FooBar::Baz';

=head2 class_to_file

  my $file = class_to_file 'Foo::Bar';

Convert a class name to a file.

  # "foo_bar"
  class_to_file 'Foo::Bar';

  # "foobar"
  class_to_file 'FOO::Bar';

  # "foo_bar"
  class_to_file 'FooBar';

  # "foobar"
  class_to_file 'FOOBar';

=head2 class_to_path

  my $path = class_to_path 'Foo::Bar';

Convert class name to path, as used by C<%INC>.

  # "Foo/Bar.pm"
  class_to_path 'Foo::Bar';

  # "FooBar.pm"
  class_to_path 'FooBar';

=head2 decamelize

  my $snakecase = decamelize $camelcase;

Convert C<CamelCase> string to C<snake_case> and replace C<::> with C<->.

  # "foo_bar"
  decamelize 'FooBar';

  # "foo_bar-baz"
  decamelize 'FooBar::Baz';

  # "foo_bar-baz"
  decamelize 'foo_bar-baz';

=head2 decode

  my $chars = decode 'UTF-8', $bytes;

Decode bytes to characters with L<Encode>, or return C<undef> if decoding
failed.

=head2 deprecated

  deprecated 'foo is DEPRECATED in favor of bar';

Warn about deprecated feature from perspective of caller. You can also set the
C<MOJO_FATAL_DEPRECATIONS> environment variable to make them die instead with
L<Carp>.

=head2 dumper

  my $perl = dumper {some => 'data'};

Dump a Perl data structure with L<Data::Dumper>.

=head2 encode

  my $bytes = encode 'UTF-8', $chars;

Encode characters to bytes with L<Encode>.

=head2 extract_usage

  my $usage = extract_usage;
  my $usage = extract_usage '/home/sri/foo.pod';

Extract usage message from the SYNOPSIS section of a file containing POD
documentation, defaults to using the file this function was called from.

  # "Usage: APPLICATION test [OPTIONS]\n"
  extract_usage;

  =head1 SYNOPSIS

    Usage: APPLICATION test [OPTIONS]

  =cut

=head2 getopt

  getopt
    'H|headers=s' => \my @headers,
    't|timeout=i' => \my $timeout,
    'v|verbose'   => \my $verbose;
  getopt $array,
    'H|headers=s' => \my @headers,
    't|timeout=i' => \my $timeout,
    'v|verbose'   => \my $verbose;
  getopt $array, ['pass_through'],
    'H|headers=s' => \my @headers,
    't|timeout=i' => \my $timeout,
    'v|verbose'   => \my $verbose;

Extract options from an array reference with L<Getopt::Long>, but without
changing its global configuration, defaults to using C<@ARGV>. The configuration
options C<no_auto_abbrev> and C<no_ignore_case> are enabled by default.

  # Extract "charset" option
  getopt ['--charset', 'UTF-8'], 'charset=s' => \my $charset;
  say $charset;

=head2 gunzip

  my $uncompressed = gunzip $compressed;

Uncompress bytes with L<IO::Compress::Gunzip>.

=head2 gzip

  my $compressed = gzip $uncompressed;

Compress bytes with L<IO::Compress::Gzip>.

=head2 hmac_sha1_sum

  my $checksum = hmac_sha1_sum $bytes, 'passw0rd';

Generate HMAC-SHA1 checksum for bytes with L<Digest::SHA>.

  # "11cedfd5ec11adc0ec234466d8a0f2a83736aa68"
  hmac_sha1_sum 'foo', 'passw0rd';

=head2 html_attr_unescape

  my $str = html_attr_unescape $escaped;

Same as L</"html_unescape">, but handles special rules from the
L<HTML Living Standard|https://html.spec.whatwg.org> for HTML attributes.

  # "foo=bar&ltest=baz"
  html_attr_unescape 'foo=bar&ltest=baz';

  # "foo=bar<est=baz"
  html_attr_unescape 'foo=bar&lt;est=baz';

=head2 html_unescape

  my $str = html_unescape $escaped;

Unescape all HTML entities in string.

  # "<div>"
  html_unescape '&lt;div&gt;';

=head2 md5_bytes

  my $checksum = md5_bytes $bytes;

Generate binary MD5 checksum for bytes with L<Digest::MD5>.

=head2 md5_sum

  my $checksum = md5_sum $bytes;

Generate MD5 checksum for bytes with L<Digest::MD5>.

  # "acbd18db4cc2f85cedef654fccc4a4d8"
  md5_sum 'foo';

=head2 monkey_patch

  monkey_patch $package, foo => sub {...};
  monkey_patch $package, foo => sub {...}, bar => sub {...};

Monkey patch functions into package.

  monkey_patch 'MyApp',
    one   => sub { say 'One!' },
    two   => sub { say 'Two!' },
    three => sub { say 'Three!' };

=head2 punycode_decode

  my $str = punycode_decode $punycode;

Punycode decode string as described in
L<RFC 3492|http://tools.ietf.org/html/rfc3492>.

  # "bücher"
  punycode_decode 'bcher-kva';

=head2 punycode_encode

  my $punycode = punycode_encode $str;

Punycode encode string as described in
L<RFC 3492|http://tools.ietf.org/html/rfc3492>.

  # "bcher-kva"
  punycode_encode 'bücher';

=head2 quote

  my $quoted = quote $str;

Quote string.

=head2 secure_compare

  my $bool = secure_compare $str1, $str2;

Constant time comparison algorithm to prevent timing attacks.

=head2 sha1_bytes

  my $checksum = sha1_bytes $bytes;

Generate binary SHA1 checksum for bytes with L<Digest::SHA>.

=head2 sha1_sum

  my $checksum = sha1_sum $bytes;

Generate SHA1 checksum for bytes with L<Digest::SHA>.

  # "0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"
  sha1_sum 'foo';

=head2 slugify

  my $slug = slugify $string;
  my $slug = slugify $string, $bool;

Returns a URL slug generated from the input string. Non-word characters are
removed, the string is trimmed and lowercased, and whitespace characters are
replaced by a dash. By default, non-ASCII characters are normalized to ASCII
word characters or removed, but if a true value is passed as the second
parameter, all word characters will be allowed in the result according to
unicode semantics.

  # "joel-is-a-slug"
  slugify 'Joel is a slug';

  # "this-is-my-resume"
  slugify 'This is: my - résumé! ☃ ';

  # "this-is-my-résumé"
  slugify 'This is: my - résumé! ☃ ', 1;

=head2 split_cookie_header

  my $tree = split_cookie_header 'a=b; expires=Thu, 07 Aug 2008 07:07:59 GMT';

Same as L</"split_header">, but handles C<expires> values from
L<RFC 6265|http://tools.ietf.org/html/rfc6265>.

=head2 split_header

   my $tree = split_header 'foo="bar baz"; test=123, yada';

Split HTTP header value into key/value pairs, each comma separated part gets
its own array reference, and keys without a value get C<undef> assigned.

  # "one"
  split_header('one; two="three four", five=six')->[0][0];

  # "two"
  split_header('one; two="three four", five=six')->[0][2];

  # "three four"
  split_header('one; two="three four", five=six')->[0][3];

  # "five"
  split_header('one; two="three four", five=six')->[1][0];

  # "six"
  split_header('one; two="three four", five=six')->[1][1];

=head2 steady_time

  my $time = steady_time;

High resolution time elapsed from an arbitrary fixed point in the past,
resilient to time jumps if a monotonic clock is available through
L<Time::HiRes>.

=head2 tablify

  my $table = tablify [['foo', 'bar'], ['baz', 'yada']];

Row-oriented generator for text tables.

  # "foo   bar\nyada  yada\nbaz   yada\n"
  tablify [['foo', 'bar'], ['yada', 'yada'], ['baz', 'yada']];

=head2 term_escape

  my $escaped = term_escape $str;

Escape all POSIX control characters except for C<\n>.

  # "foo\\x09bar\\x0d\n"
  term_escape "foo\tbar\r\n";

=head2 trim

  my $trimmed = trim $str;

Trim whitespace characters from both ends of string.

  # "foo bar"
  trim '  foo bar  ';

=head2 unindent

  my $unindented = unindent $str;

Unindent multi-line string.

  # "foo\nbar\nbaz\n"
  unindent "  foo\n  bar\n  baz\n";

=head2 unquote

  my $str = unquote $quoted;

Unquote string.

=head2 url_escape

  my $escaped = url_escape $str;
  my $escaped = url_escape $str, '^A-Za-z0-9\-._~';

Percent encode unsafe characters in string as described in
L<RFC 3986|http://tools.ietf.org/html/rfc3986>, the pattern used defaults to
C<^A-Za-z0-9\-._~>.

  # "foo%3Bbar"
  url_escape 'foo;bar';

=head2 url_unescape

  my $str = url_unescape $escaped;

Decode percent encoded characters in string as described in
L<RFC 3986|http://tools.ietf.org/html/rfc3986>.

  # "foo;bar"
  url_unescape 'foo%3Bbar';

=head2 xml_escape

  my $escaped = xml_escape $str;

Escape unsafe characters C<&>, C<E<lt>>, C<E<gt>>, C<"> and C<'> in string, but
do not escape L<Mojo::ByteStream> objects.

  # "&lt;div&gt;"
  xml_escape '<div>';

  # "<div>"
  use Mojo::ByteStream 'b';
  xml_escape b('<div>');

=head2 xor_encode

  my $encoded = xor_encode $str, $key;

XOR encode string with variable length key.

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut

__DATA__
Aacute; U+000C1
Aacute U+000C1
aacute; U+000E1
aacute U+000E1
Abreve; U+00102
abreve; U+00103
ac; U+0223E
acd; U+0223F
acE; U+0223E U+00333
Acirc; U+000C2
Acirc U+000C2
acirc; U+000E2
acirc U+000E2
acute; U+000B4
acute U+000B4
Acy; U+00410
acy; U+00430
AElig; U+000C6
AElig U+000C6
aelig; U+000E6
aelig U+000E6
af; U+02061
Afr; U+1D504
afr; U+1D51E
Agrave; U+000C0
Agrave U+000C0
agrave; U+000E0
agrave U+000E0
alefsym; U+02135
aleph; U+02135
Alpha; U+00391
alpha; U+003B1
Amacr; U+00100
amacr; U+00101
amalg; U+02A3F
AMP; U+00026
AMP U+00026
amp; U+00026
amp U+00026
And; U+02A53
and; U+02227
andand; U+02A55
andd; U+02A5C
andslope; U+02A58
andv; U+02A5A
ang; U+02220
ange; U+029A4
angle; U+02220
angmsd; U+02221
angmsdaa; U+029A8
angmsdab; U+029A9
angmsdac; U+029AA
angmsdad; U+029AB
angmsdae; U+029AC
angmsdaf; U+029AD
angmsdag; U+029AE
angmsdah; U+029AF
angrt; U+0221F
angrtvb; U+022BE
angrtvbd; U+0299D
angsph; U+02222
angst; U+000C5
angzarr; U+0237C
Aogon; U+00104
aogon; U+00105
Aopf; U+1D538
aopf; U+1D552
ap; U+02248
apacir; U+02A6F
apE; U+02A70
ape; U+0224A
apid; U+0224B
apos; U+00027
ApplyFunction; U+02061
approx; U+02248
approxeq; U+0224A
Aring; U+000C5
Aring U+000C5
aring; U+000E5
aring U+000E5
Ascr; U+1D49C
ascr; U+1D4B6
Assign; U+02254
ast; U+0002A
asymp; U+02248
asympeq; U+0224D
Atilde; U+000C3
Atilde U+000C3
atilde; U+000E3
atilde U+000E3
Auml; U+000C4
Auml U+000C4
auml; U+000E4
auml U+000E4
awconint; U+02233
awint; U+02A11
backcong; U+0224C
backepsilon; U+003F6
backprime; U+02035
backsim; U+0223D
backsimeq; U+022CD
Backslash; U+02216
Barv; U+02AE7
barvee; U+022BD
Barwed; U+02306
barwed; U+02305
barwedge; U+02305
bbrk; U+023B5
bbrktbrk; U+023B6
bcong; U+0224C
Bcy; U+00411
bcy; U+00431
bdquo; U+0201E
becaus; U+02235
Because; U+02235
because; U+02235
bemptyv; U+029B0
bepsi; U+003F6
bernou; U+0212C
Bernoullis; U+0212C
Beta; U+00392
beta; U+003B2
beth; U+02136
between; U+0226C
Bfr; U+1D505
bfr; U+1D51F
bigcap; U+022C2
bigcirc; U+025EF
bigcup; U+022C3
bigodot; U+02A00
bigoplus; U+02A01
bigotimes; U+02A02
bigsqcup; U+02A06
bigstar; U+02605
bigtriangledown; U+025BD
bigtriangleup; U+025B3
biguplus; U+02A04
bigvee; U+022C1
bigwedge; U+022C0
bkarow; U+0290D
blacklozenge; U+029EB
blacksquare; U+025AA
blacktriangle; U+025B4
blacktriangledown; U+025BE
blacktriangleleft; U+025C2
blacktriangleright; U+025B8
blank; U+02423
blk12; U+02592
blk14; U+02591
blk34; U+02593
block; U+02588
bne; U+0003D U+020E5
bnequiv; U+02261 U+020E5
bNot; U+02AED
bnot; U+02310
Bopf; U+1D539
bopf; U+1D553
bot; U+022A5
bottom; U+022A5
bowtie; U+022C8
boxbox; U+029C9
boxDL; U+02557
boxDl; U+02556
boxdL; U+02555
boxdl; U+02510
boxDR; U+02554
boxDr; U+02553
boxdR; U+02552
boxdr; U+0250C
boxH; U+02550
boxh; U+02500
boxHD; U+02566
boxHd; U+02564
boxhD; U+02565
boxhd; U+0252C
boxHU; U+02569
boxHu; U+02567
boxhU; U+02568
boxhu; U+02534
boxminus; U+0229F
boxplus; U+0229E
boxtimes; U+022A0
boxUL; U+0255D
boxUl; U+0255C
boxuL; U+0255B
boxul; U+02518
boxUR; U+0255A
boxUr; U+02559
boxuR; U+02558
boxur; U+02514
boxV; U+02551
boxv; U+02502
boxVH; U+0256C
boxVh; U+0256B
boxvH; U+0256A
boxvh; U+0253C
boxVL; U+02563
boxVl; U+02562
boxvL; U+02561
boxvl; U+02524
boxVR; U+02560
boxVr; U+0255F
boxvR; U+0255E
boxvr; U+0251C
bprime; U+02035
Breve; U+002D8
breve; U+002D8
brvbar; U+000A6
brvbar U+000A6
Bscr; U+0212C
bscr; U+1D4B7
bsemi; U+0204F
bsim; U+0223D
bsime; U+022CD
bsol; U+0005C
bsolb; U+029C5
bsolhsub; U+027C8
bull; U+02022
bullet; U+02022
bump; U+0224E
bumpE; U+02AAE
bumpe; U+0224F
Bumpeq; U+0224E
bumpeq; U+0224F
Cacute; U+00106
cacute; U+00107
Cap; U+022D2
cap; U+02229
capand; U+02A44
capbrcup; U+02A49
capcap; U+02A4B
capcup; U+02A47
capdot; U+02A40
CapitalDifferentialD; U+02145
caps; U+02229 U+0FE00
caret; U+02041
caron; U+002C7
Cayleys; U+0212D
ccaps; U+02A4D
Ccaron; U+0010C
ccaron; U+0010D
Ccedil; U+000C7
Ccedil U+000C7
ccedil; U+000E7
ccedil U+000E7
Ccirc; U+00108
ccirc; U+00109
Cconint; U+02230
ccups; U+02A4C
ccupssm; U+02A50
Cdot; U+0010A
cdot; U+0010B
cedil; U+000B8
cedil U+000B8
Cedilla; U+000B8
cemptyv; U+029B2
cent; U+000A2
cent U+000A2
CenterDot; U+000B7
centerdot; U+000B7
Cfr; U+0212D
cfr; U+1D520
CHcy; U+00427
chcy; U+00447
check; U+02713
checkmark; U+02713
Chi; U+003A7
chi; U+003C7
cir; U+025CB
circ; U+002C6
circeq; U+02257
circlearrowleft; U+021BA
circlearrowright; U+021BB
circledast; U+0229B
circledcirc; U+0229A
circleddash; U+0229D
CircleDot; U+02299
circledR; U+000AE
circledS; U+024C8
CircleMinus; U+02296
CirclePlus; U+02295
CircleTimes; U+02297
cirE; U+029C3
cire; U+02257
cirfnint; U+02A10
cirmid; U+02AEF
cirscir; U+029C2
ClockwiseContourIntegral; U+02232
CloseCurlyDoubleQuote; U+0201D
CloseCurlyQuote; U+02019
clubs; U+02663
clubsuit; U+02663
Colon; U+02237
colon; U+0003A
Colone; U+02A74
colone; U+02254
coloneq; U+02254
comma; U+0002C
commat; U+00040
comp; U+02201
compfn; U+02218
complement; U+02201
complexes; U+02102
cong; U+02245
congdot; U+02A6D
Congruent; U+02261
Conint; U+0222F
conint; U+0222E
ContourIntegral; U+0222E
Copf; U+02102
copf; U+1D554
coprod; U+02210
Coproduct; U+02210
COPY; U+000A9
COPY U+000A9
copy; U+000A9
copy U+000A9
copysr; U+02117
CounterClockwiseContourIntegral; U+02233
crarr; U+021B5
Cross; U+02A2F
cross; U+02717
Cscr; U+1D49E
cscr; U+1D4B8
csub; U+02ACF
csube; U+02AD1
csup; U+02AD0
csupe; U+02AD2
ctdot; U+022EF
cudarrl; U+02938
cudarrr; U+02935
cuepr; U+022DE
cuesc; U+022DF
cularr; U+021B6
cularrp; U+0293D
Cup; U+022D3
cup; U+0222A
cupbrcap; U+02A48
CupCap; U+0224D
cupcap; U+02A46
cupcup; U+02A4A
cupdot; U+0228D
cupor; U+02A45
cups; U+0222A U+0FE00
curarr; U+021B7
curarrm; U+0293C
curlyeqprec; U+022DE
curlyeqsucc; U+022DF
curlyvee; U+022CE
curlywedge; U+022CF
curren; U+000A4
curren U+000A4
curvearrowleft; U+021B6
curvearrowright; U+021B7
cuvee; U+022CE
cuwed; U+022CF
cwconint; U+02232
cwint; U+02231
cylcty; U+0232D
Dagger; U+02021
dagger; U+02020
daleth; U+02138
Darr; U+021A1
dArr; U+021D3
darr; U+02193
dash; U+02010
Dashv; U+02AE4
dashv; U+022A3
dbkarow; U+0290F
dblac; U+002DD
Dcaron; U+0010E
dcaron; U+0010F
Dcy; U+00414
dcy; U+00434
DD; U+02145
dd; U+02146
ddagger; U+02021
ddarr; U+021CA
DDotrahd; U+02911
ddotseq; U+02A77
deg; U+000B0
deg U+000B0
Del; U+02207
Delta; U+00394
delta; U+003B4
demptyv; U+029B1
dfisht; U+0297F
Dfr; U+1D507
dfr; U+1D521
dHar; U+02965
dharl; U+021C3
dharr; U+021C2
DiacriticalAcute; U+000B4
DiacriticalDot; U+002D9
DiacriticalDoubleAcute; U+002DD
DiacriticalGrave; U+00060
DiacriticalTilde; U+002DC
diam; U+022C4
Diamond; U+022C4
diamond; U+022C4
diamondsuit; U+02666
diams; U+02666
die; U+000A8
DifferentialD; U+02146
digamma; U+003DD
disin; U+022F2
div; U+000F7
divide; U+000F7
divide U+000F7
divideontimes; U+022C7
divonx; U+022C7
DJcy; U+00402
djcy; U+00452
dlcorn; U+0231E
dlcrop; U+0230D
dollar; U+00024
Dopf; U+1D53B
dopf; U+1D555
Dot; U+000A8
dot; U+002D9
DotDot; U+020DC
doteq; U+02250
doteqdot; U+02251
DotEqual; U+02250
dotminus; U+02238
dotplus; U+02214
dotsquare; U+022A1
doublebarwedge; U+02306
DoubleContourIntegral; U+0222F
DoubleDot; U+000A8
DoubleDownArrow; U+021D3
DoubleLeftArrow; U+021D0
DoubleLeftRightArrow; U+021D4
DoubleLeftTee; U+02AE4
DoubleLongLeftArrow; U+027F8
DoubleLongLeftRightArrow; U+027FA
DoubleLongRightArrow; U+027F9
DoubleRightArrow; U+021D2
DoubleRightTee; U+022A8
DoubleUpArrow; U+021D1
DoubleUpDownArrow; U+021D5
DoubleVerticalBar; U+02225
DownArrow; U+02193
Downarrow; U+021D3
downarrow; U+02193
DownArrowBar; U+02913
DownArrowUpArrow; U+021F5
DownBreve; U+00311
downdownarrows; U+021CA
downharpoonleft; U+021C3
downharpoonright; U+021C2
DownLeftRightVector; U+02950
DownLeftTeeVector; U+0295E
DownLeftVector; U+021BD
DownLeftVectorBar; U+02956
DownRightTeeVector; U+0295F
DownRightVector; U+021C1
DownRightVectorBar; U+02957
DownTee; U+022A4
DownTeeArrow; U+021A7
drbkarow; U+02910
drcorn; U+0231F
drcrop; U+0230C
Dscr; U+1D49F
dscr; U+1D4B9
DScy; U+00405
dscy; U+00455
dsol; U+029F6
Dstrok; U+00110
dstrok; U+00111
dtdot; U+022F1
dtri; U+025BF
dtrif; U+025BE
duarr; U+021F5
duhar; U+0296F
dwangle; U+029A6
DZcy; U+0040F
dzcy; U+0045F
dzigrarr; U+027FF
Eacute; U+000C9
Eacute U+000C9
eacute; U+000E9
eacute U+000E9
easter; U+02A6E
Ecaron; U+0011A
ecaron; U+0011B
ecir; U+02256
Ecirc; U+000CA
Ecirc U+000CA
ecirc; U+000EA
ecirc U+000EA
ecolon; U+02255
Ecy; U+0042D
ecy; U+0044D
eDDot; U+02A77
Edot; U+00116
eDot; U+02251
edot; U+00117
ee; U+02147
efDot; U+02252
Efr; U+1D508
efr; U+1D522
eg; U+02A9A
Egrave; U+000C8
Egrave U+000C8
egrave; U+000E8
egrave U+000E8
egs; U+02A96
egsdot; U+02A98
el; U+02A99
Element; U+02208
elinters; U+023E7
ell; U+02113
els; U+02A95
elsdot; U+02A97
Emacr; U+00112
emacr; U+00113
empty; U+02205
emptyset; U+02205
EmptySmallSquare; U+025FB
emptyv; U+02205
EmptyVerySmallSquare; U+025AB
emsp; U+02003
emsp13; U+02004
emsp14; U+02005
ENG; U+0014A
eng; U+0014B
ensp; U+02002
Eogon; U+00118
eogon; U+00119
Eopf; U+1D53C
eopf; U+1D556
epar; U+022D5
eparsl; U+029E3
eplus; U+02A71
epsi; U+003B5
Epsilon; U+00395
epsilon; U+003B5
epsiv; U+003F5
eqcirc; U+02256
eqcolon; U+02255
eqsim; U+02242
eqslantgtr; U+02A96
eqslantless; U+02A95
Equal; U+02A75
equals; U+0003D
EqualTilde; U+02242
equest; U+0225F
Equilibrium; U+021CC
equiv; U+02261
equivDD; U+02A78
eqvparsl; U+029E5
erarr; U+02971
erDot; U+02253
Escr; U+02130
escr; U+0212F
esdot; U+02250
Esim; U+02A73
esim; U+02242
Eta; U+00397
eta; U+003B7
ETH; U+000D0
ETH U+000D0
eth; U+000F0
eth U+000F0
Euml; U+000CB
Euml U+000CB
euml; U+000EB
euml U+000EB
euro; U+020AC
excl; U+00021
exist; U+02203
Exists; U+02203
expectation; U+02130
ExponentialE; U+02147
exponentiale; U+02147
fallingdotseq; U+02252
Fcy; U+00424
fcy; U+00444
female; U+02640
ffilig; U+0FB03
fflig; U+0FB00
ffllig; U+0FB04
Ffr; U+1D509
ffr; U+1D523
filig; U+0FB01
FilledSmallSquare; U+025FC
FilledVerySmallSquare; U+025AA
fjlig; U+00066 U+0006A
flat; U+0266D
fllig; U+0FB02
fltns; U+025B1
fnof; U+00192
Fopf; U+1D53D
fopf; U+1D557
ForAll; U+02200
forall; U+02200
fork; U+022D4
forkv; U+02AD9
Fouriertrf; U+02131
fpartint; U+02A0D
frac12; U+000BD
frac12 U+000BD
frac13; U+02153
frac14; U+000BC
frac14 U+000BC
frac15; U+02155
frac16; U+02159
frac18; U+0215B
frac23; U+02154
frac25; U+02156
frac34; U+000BE
frac34 U+000BE
frac35; U+02157
frac38; U+0215C
frac45; U+02158
frac56; U+0215A
frac58; U+0215D
frac78; U+0215E
frasl; U+02044
frown; U+02322
Fscr; U+02131
fscr; U+1D4BB
gacute; U+001F5
Gamma; U+00393
gamma; U+003B3
Gammad; U+003DC
gammad; U+003DD
gap; U+02A86
Gbreve; U+0011E
gbreve; U+0011F
Gcedil; U+00122
Gcirc; U+0011C
gcirc; U+0011D
Gcy; U+00413
gcy; U+00433
Gdot; U+00120
gdot; U+00121
gE; U+02267
ge; U+02265
gEl; U+02A8C
gel; U+022DB
geq; U+02265
geqq; U+02267
geqslant; U+02A7E
ges; U+02A7E
gescc; U+02AA9
gesdot; U+02A80
gesdoto; U+02A82
gesdotol; U+02A84
gesl; U+022DB U+0FE00
gesles; U+02A94
Gfr; U+1D50A
gfr; U+1D524
Gg; U+022D9
gg; U+0226B
ggg; U+022D9
gimel; U+02137
GJcy; U+00403
gjcy; U+00453
gl; U+02277
gla; U+02AA5
glE; U+02A92
glj; U+02AA4
gnap; U+02A8A
gnapprox; U+02A8A
gnE; U+02269
gne; U+02A88
gneq; U+02A88
gneqq; U+02269
gnsim; U+022E7
Gopf; U+1D53E
gopf; U+1D558
grave; U+00060
GreaterEqual; U+02265
GreaterEqualLess; U+022DB
GreaterFullEqual; U+02267
GreaterGreater; U+02AA2
GreaterLess; U+02277
GreaterSlantEqual; U+02A7E
GreaterTilde; U+02273
Gscr; U+1D4A2
gscr; U+0210A
gsim; U+02273
gsime; U+02A8E
gsiml; U+02A90
GT; U+0003E
GT U+0003E
Gt; U+0226B
gt; U+0003E
gt U+0003E
gtcc; U+02AA7
gtcir; U+02A7A
gtdot; U+022D7
gtlPar; U+02995
gtquest; U+02A7C
gtrapprox; U+02A86
gtrarr; U+02978
gtrdot; U+022D7
gtreqless; U+022DB
gtreqqless; U+02A8C
gtrless; U+02277
gtrsim; U+02273
gvertneqq; U+02269 U+0FE00
gvnE; U+02269 U+0FE00
Hacek; U+002C7
hairsp; U+0200A
half; U+000BD
hamilt; U+0210B
HARDcy; U+0042A
hardcy; U+0044A
hArr; U+021D4
harr; U+02194
harrcir; U+02948
harrw; U+021AD
Hat; U+0005E
hbar; U+0210F
Hcirc; U+00124
hcirc; U+00125
hearts; U+02665
heartsuit; U+02665
hellip; U+02026
hercon; U+022B9
Hfr; U+0210C
hfr; U+1D525
HilbertSpace; U+0210B
hksearow; U+02925
hkswarow; U+02926
hoarr; U+021FF
homtht; U+0223B
hookleftarrow; U+021A9
hookrightarrow; U+021AA
Hopf; U+0210D
hopf; U+1D559
horbar; U+02015
HorizontalLine; U+02500
Hscr; U+0210B
hscr; U+1D4BD
hslash; U+0210F
Hstrok; U+00126
hstrok; U+00127
HumpDownHump; U+0224E
HumpEqual; U+0224F
hybull; U+02043
hyphen; U+02010
Iacute; U+000CD
Iacute U+000CD
iacute; U+000ED
iacute U+000ED
ic; U+02063
Icirc; U+000CE
Icirc U+000CE
icirc; U+000EE
icirc U+000EE
Icy; U+00418
icy; U+00438
Idot; U+00130
IEcy; U+00415
iecy; U+00435
iexcl; U+000A1
iexcl U+000A1
iff; U+021D4
Ifr; U+02111
ifr; U+1D526
Igrave; U+000CC
Igrave U+000CC
igrave; U+000EC
igrave U+000EC
ii; U+02148
iiiint; U+02A0C
iiint; U+0222D
iinfin; U+029DC
iiota; U+02129
IJlig; U+00132
ijlig; U+00133
Im; U+02111
Imacr; U+0012A
imacr; U+0012B
image; U+02111
ImaginaryI; U+02148
imagline; U+02110
imagpart; U+02111
imath; U+00131
imof; U+022B7
imped; U+001B5
Implies; U+021D2
in; U+02208
incare; U+02105
infin; U+0221E
infintie; U+029DD
inodot; U+00131
Int; U+0222C
int; U+0222B
intcal; U+022BA
integers; U+02124
Integral; U+0222B
intercal; U+022BA
Intersection; U+022C2
intlarhk; U+02A17
intprod; U+02A3C
InvisibleComma; U+02063
InvisibleTimes; U+02062
IOcy; U+00401
iocy; U+00451
Iogon; U+0012E
iogon; U+0012F
Iopf; U+1D540
iopf; U+1D55A
Iota; U+00399
iota; U+003B9
iprod; U+02A3C
iquest; U+000BF
iquest U+000BF
Iscr; U+02110
iscr; U+1D4BE
isin; U+02208
isindot; U+022F5
isinE; U+022F9
isins; U+022F4
isinsv; U+022F3
isinv; U+02208
it; U+02062
Itilde; U+00128
itilde; U+00129
Iukcy; U+00406
iukcy; U+00456
Iuml; U+000CF
Iuml U+000CF
iuml; U+000EF
iuml U+000EF
Jcirc; U+00134
jcirc; U+00135
Jcy; U+00419
jcy; U+00439
Jfr; U+1D50D
jfr; U+1D527
jmath; U+00237
Jopf; U+1D541
jopf; U+1D55B
Jscr; U+1D4A5
jscr; U+1D4BF
Jsercy; U+00408
jsercy; U+00458
Jukcy; U+00404
jukcy; U+00454
Kappa; U+0039A
kappa; U+003BA
kappav; U+003F0
Kcedil; U+00136
kcedil; U+00137
Kcy; U+0041A
kcy; U+0043A
Kfr; U+1D50E
kfr; U+1D528
kgreen; U+00138
KHcy; U+00425
khcy; U+00445
KJcy; U+0040C
kjcy; U+0045C
Kopf; U+1D542
kopf; U+1D55C
Kscr; U+1D4A6
kscr; U+1D4C0
lAarr; U+021DA
Lacute; U+00139
lacute; U+0013A
laemptyv; U+029B4
lagran; U+02112
Lambda; U+0039B
lambda; U+003BB
Lang; U+027EA
lang; U+027E8
langd; U+02991
langle; U+027E8
lap; U+02A85
Laplacetrf; U+02112
laquo; U+000AB
laquo U+000AB
Larr; U+0219E
lArr; U+021D0
larr; U+02190
larrb; U+021E4
larrbfs; U+0291F
larrfs; U+0291D
larrhk; U+021A9
larrlp; U+021AB
larrpl; U+02939
larrsim; U+02973
larrtl; U+021A2
lat; U+02AAB
lAtail; U+0291B
latail; U+02919
late; U+02AAD
lates; U+02AAD U+0FE00
lBarr; U+0290E
lbarr; U+0290C
lbbrk; U+02772
lbrace; U+0007B
lbrack; U+0005B
lbrke; U+0298B
lbrksld; U+0298F
lbrkslu; U+0298D
Lcaron; U+0013D
lcaron; U+0013E
Lcedil; U+0013B
lcedil; U+0013C
lceil; U+02308
lcub; U+0007B
Lcy; U+0041B
lcy; U+0043B
ldca; U+02936
ldquo; U+0201C
ldquor; U+0201E
ldrdhar; U+02967
ldrushar; U+0294B
ldsh; U+021B2
lE; U+02266
le; U+02264
LeftAngleBracket; U+027E8
LeftArrow; U+02190
Leftarrow; U+021D0
leftarrow; U+02190
LeftArrowBar; U+021E4
LeftArrowRightArrow; U+021C6
leftarrowtail; U+021A2
LeftCeiling; U+02308
LeftDoubleBracket; U+027E6
LeftDownTeeVector; U+02961
LeftDownVector; U+021C3
LeftDownVectorBar; U+02959
LeftFloor; U+0230A
leftharpoondown; U+021BD
leftharpoonup; U+021BC
leftleftarrows; U+021C7
LeftRightArrow; U+02194
Leftrightarrow; U+021D4
leftrightarrow; U+02194
leftrightarrows; U+021C6
leftrightharpoons; U+021CB
leftrightsquigarrow; U+021AD
LeftRightVector; U+0294E
LeftTee; U+022A3
LeftTeeArrow; U+021A4
LeftTeeVector; U+0295A
leftthreetimes; U+022CB
LeftTriangle; U+022B2
LeftTriangleBar; U+029CF
LeftTriangleEqual; U+022B4
LeftUpDownVector; U+02951
LeftUpTeeVector; U+02960
LeftUpVector; U+021BF
LeftUpVectorBar; U+02958
LeftVector; U+021BC
LeftVectorBar; U+02952
lEg; U+02A8B
leg; U+022DA
leq; U+02264
leqq; U+02266
leqslant; U+02A7D
les; U+02A7D
lescc; U+02AA8
lesdot; U+02A7F
lesdoto; U+02A81
lesdotor; U+02A83
lesg; U+022DA U+0FE00
lesges; U+02A93
lessapprox; U+02A85
lessdot; U+022D6
lesseqgtr; U+022DA
lesseqqgtr; U+02A8B
LessEqualGreater; U+022DA
LessFullEqual; U+02266
LessGreater; U+02276
lessgtr; U+02276
LessLess; U+02AA1
lesssim; U+02272
LessSlantEqual; U+02A7D
LessTilde; U+02272
lfisht; U+0297C
lfloor; U+0230A
Lfr; U+1D50F
lfr; U+1D529
lg; U+02276
lgE; U+02A91
lHar; U+02962
lhard; U+021BD
lharu; U+021BC
lharul; U+0296A
lhblk; U+02584
LJcy; U+00409
ljcy; U+00459
Ll; U+022D8
ll; U+0226A
llarr; U+021C7
llcorner; U+0231E
Lleftarrow; U+021DA
llhard; U+0296B
lltri; U+025FA
Lmidot; U+0013F
lmidot; U+00140
lmoust; U+023B0
lmoustache; U+023B0
lnap; U+02A89
lnapprox; U+02A89
lnE; U+02268
lne; U+02A87
lneq; U+02A87
lneqq; U+02268
lnsim; U+022E6
loang; U+027EC
loarr; U+021FD
lobrk; U+027E6
LongLeftArrow; U+027F5
Longleftarrow; U+027F8
longleftarrow; U+027F5
LongLeftRightArrow; U+027F7
Longleftrightarrow; U+027FA
longleftrightarrow; U+027F7
longmapsto; U+027FC
LongRightArrow; U+027F6
Longrightarrow; U+027F9
longrightarrow; U+027F6
looparrowleft; U+021AB
looparrowright; U+021AC
lopar; U+02985
Lopf; U+1D543
lopf; U+1D55D
loplus; U+02A2D
lotimes; U+02A34
lowast; U+02217
lowbar; U+0005F
LowerLeftArrow; U+02199
LowerRightArrow; U+02198
loz; U+025CA
lozenge; U+025CA
lozf; U+029EB
lpar; U+00028
lparlt; U+02993
lrarr; U+021C6
lrcorner; U+0231F
lrhar; U+021CB
lrhard; U+0296D
lrm; U+0200E
lrtri; U+022BF
lsaquo; U+02039
Lscr; U+02112
lscr; U+1D4C1
Lsh; U+021B0
lsh; U+021B0
lsim; U+02272
lsime; U+02A8D
lsimg; U+02A8F
lsqb; U+0005B
lsquo; U+02018
lsquor; U+0201A
Lstrok; U+00141
lstrok; U+00142
LT; U+0003C
LT U+0003C
Lt; U+0226A
lt; U+0003C
lt U+0003C
ltcc; U+02AA6
ltcir; U+02A79
ltdot; U+022D6
lthree; U+022CB
ltimes; U+022C9
ltlarr; U+02976
ltquest; U+02A7B
ltri; U+025C3
ltrie; U+022B4
ltrif; U+025C2
ltrPar; U+02996
lurdshar; U+0294A
luruhar; U+02966
lvertneqq; U+02268 U+0FE00
lvnE; U+02268 U+0FE00
macr; U+000AF
macr U+000AF
male; U+02642
malt; U+02720
maltese; U+02720
Map; U+02905
map; U+021A6
mapsto; U+021A6
mapstodown; U+021A7
mapstoleft; U+021A4
mapstoup; U+021A5
marker; U+025AE
mcomma; U+02A29
Mcy; U+0041C
mcy; U+0043C
mdash; U+02014
mDDot; U+0223A
measuredangle; U+02221
MediumSpace; U+0205F
Mellintrf; U+02133
Mfr; U+1D510
mfr; U+1D52A
mho; U+02127
micro; U+000B5
micro U+000B5
mid; U+02223
midast; U+0002A
midcir; U+02AF0
middot; U+000B7
middot U+000B7
minus; U+02212
minusb; U+0229F
minusd; U+02238
minusdu; U+02A2A
MinusPlus; U+02213
mlcp; U+02ADB
mldr; U+02026
mnplus; U+02213
models; U+022A7
Mopf; U+1D544
mopf; U+1D55E
mp; U+02213
Mscr; U+02133
mscr; U+1D4C2
mstpos; U+0223E
Mu; U+0039C
mu; U+003BC
multimap; U+022B8
mumap; U+022B8
nabla; U+02207
Nacute; U+00143
nacute; U+00144
nang; U+02220 U+020D2
nap; U+02249
napE; U+02A70 U+00338
napid; U+0224B U+00338
napos; U+00149
napprox; U+02249
natur; U+0266E
natural; U+0266E
naturals; U+02115
nbsp; U+000A0
nbsp U+000A0
nbump; U+0224E U+00338
nbumpe; U+0224F U+00338
ncap; U+02A43
Ncaron; U+00147
ncaron; U+00148
Ncedil; U+00145
ncedil; U+00146
ncong; U+02247
ncongdot; U+02A6D U+00338
ncup; U+02A42
Ncy; U+0041D
ncy; U+0043D
ndash; U+02013
ne; U+02260
nearhk; U+02924
neArr; U+021D7
nearr; U+02197
nearrow; U+02197
nedot; U+02250 U+00338
NegativeMediumSpace; U+0200B
NegativeThickSpace; U+0200B
NegativeThinSpace; U+0200B
NegativeVeryThinSpace; U+0200B
nequiv; U+02262
nesear; U+02928
nesim; U+02242 U+00338
NestedGreaterGreater; U+0226B
NestedLessLess; U+0226A
NewLine; U+0000A
nexist; U+02204
nexists; U+02204
Nfr; U+1D511
nfr; U+1D52B
ngE; U+02267 U+00338
nge; U+02271
ngeq; U+02271
ngeqq; U+02267 U+00338
ngeqslant; U+02A7E U+00338
nges; U+02A7E U+00338
nGg; U+022D9 U+00338
ngsim; U+02275
nGt; U+0226B U+020D2
ngt; U+0226F
ngtr; U+0226F
nGtv; U+0226B U+00338
nhArr; U+021CE
nharr; U+021AE
nhpar; U+02AF2
ni; U+0220B
nis; U+022FC
nisd; U+022FA
niv; U+0220B
NJcy; U+0040A
njcy; U+0045A
nlArr; U+021CD
nlarr; U+0219A
nldr; U+02025
nlE; U+02266 U+00338
nle; U+02270
nLeftarrow; U+021CD
nleftarrow; U+0219A
nLeftrightarrow; U+021CE
nleftrightarrow; U+021AE
nleq; U+02270
nleqq; U+02266 U+00338
nleqslant; U+02A7D U+00338
nles; U+02A7D U+00338
nless; U+0226E
nLl; U+022D8 U+00338
nlsim; U+02274
nLt; U+0226A U+020D2
nlt; U+0226E
nltri; U+022EA
nltrie; U+022EC
nLtv; U+0226A U+00338
nmid; U+02224
NoBreak; U+02060
NonBreakingSpace; U+000A0
Nopf; U+02115
nopf; U+1D55F
Not; U+02AEC
not; U+000AC
not U+000AC
NotCongruent; U+02262
NotCupCap; U+0226D
NotDoubleVerticalBar; U+02226
NotElement; U+02209
NotEqual; U+02260
NotEqualTilde; U+02242 U+00338
NotExists; U+02204
NotGreater; U+0226F
NotGreaterEqual; U+02271
NotGreaterFullEqual; U+02267 U+00338
NotGreaterGreater; U+0226B U+00338
NotGreaterLess; U+02279
NotGreaterSlantEqual; U+02A7E U+00338
NotGreaterTilde; U+02275
NotHumpDownHump; U+0224E U+00338
NotHumpEqual; U+0224F U+00338
notin; U+02209
notindot; U+022F5 U+00338
notinE; U+022F9 U+00338
notinva; U+02209
notinvb; U+022F7
notinvc; U+022F6
NotLeftTriangle; U+022EA
NotLeftTriangleBar; U+029CF U+00338
NotLeftTriangleEqual; U+022EC
NotLess; U+0226E
NotLessEqual; U+02270
NotLessGreater; U+02278
NotLessLess; U+0226A U+00338
NotLessSlantEqual; U+02A7D U+00338
NotLessTilde; U+02274
NotNestedGreaterGreater; U+02AA2 U+00338
NotNestedLessLess; U+02AA1 U+00338
notni; U+0220C
notniva; U+0220C
notnivb; U+022FE
notnivc; U+022FD
NotPrecedes; U+02280
NotPrecedesEqual; U+02AAF U+00338
NotPrecedesSlantEqual; U+022E0
NotReverseElement; U+0220C
NotRightTriangle; U+022EB
NotRightTriangleBar; U+029D0 U+00338
NotRightTriangleEqual; U+022ED
NotSquareSubset; U+0228F U+00338
NotSquareSubsetEqual; U+022E2
NotSquareSuperset; U+02290 U+00338
NotSquareSupersetEqual; U+022E3
NotSubset; U+02282 U+020D2
NotSubsetEqual; U+02288
NotSucceeds; U+02281
NotSucceedsEqual; U+02AB0 U+00338
NotSucceedsSlantEqual; U+022E1
NotSucceedsTilde; U+0227F U+00338
NotSuperset; U+02283 U+020D2
NotSupersetEqual; U+02289
NotTilde; U+02241
NotTildeEqual; U+02244
NotTildeFullEqual; U+02247
NotTildeTilde; U+02249
NotVerticalBar; U+02224
npar; U+02226
nparallel; U+02226
nparsl; U+02AFD U+020E5
npart; U+02202 U+00338
npolint; U+02A14
npr; U+02280
nprcue; U+022E0
npre; U+02AAF U+00338
nprec; U+02280
npreceq; U+02AAF U+00338
nrArr; U+021CF
nrarr; U+0219B
nrarrc; U+02933 U+00338
nrarrw; U+0219D U+00338
nRightarrow; U+021CF
nrightarrow; U+0219B
nrtri; U+022EB
nrtrie; U+022ED
nsc; U+02281
nsccue; U+022E1
nsce; U+02AB0 U+00338
Nscr; U+1D4A9
nscr; U+1D4C3
nshortmid; U+02224
nshortparallel; U+02226
nsim; U+02241
nsime; U+02244
nsimeq; U+02244
nsmid; U+02224
nspar; U+02226
nsqsube; U+022E2
nsqsupe; U+022E3
nsub; U+02284
nsubE; U+02AC5 U+00338
nsube; U+02288
nsubset; U+02282 U+020D2
nsubseteq; U+02288
nsubseteqq; U+02AC5 U+00338
nsucc; U+02281
nsucceq; U+02AB0 U+00338
nsup; U+02285
nsupE; U+02AC6 U+00338
nsupe; U+02289
nsupset; U+02283 U+020D2
nsupseteq; U+02289
nsupseteqq; U+02AC6 U+00338
ntgl; U+02279
Ntilde; U+000D1
Ntilde U+000D1
ntilde; U+000F1
ntilde U+000F1
ntlg; U+02278
ntriangleleft; U+022EA
ntrianglelefteq; U+022EC
ntriangleright; U+022EB
ntrianglerighteq; U+022ED
Nu; U+0039D
nu; U+003BD
num; U+00023
numero; U+02116
numsp; U+02007
nvap; U+0224D U+020D2
nVDash; U+022AF
nVdash; U+022AE
nvDash; U+022AD
nvdash; U+022AC
nvge; U+02265 U+020D2
nvgt; U+0003E U+020D2
nvHarr; U+02904
nvinfin; U+029DE
nvlArr; U+02902
nvle; U+02264 U+020D2
nvlt; U+0003C U+020D2
nvltrie; U+022B4 U+020D2
nvrArr; U+02903
nvrtrie; U+022B5 U+020D2
nvsim; U+0223C U+020D2
nwarhk; U+02923
nwArr; U+021D6
nwarr; U+02196
nwarrow; U+02196
nwnear; U+02927
Oacute; U+000D3
Oacute U+000D3
oacute; U+000F3
oacute U+000F3
oast; U+0229B
ocir; U+0229A
Ocirc; U+000D4
Ocirc U+000D4
ocirc; U+000F4
ocirc U+000F4
Ocy; U+0041E
ocy; U+0043E
odash; U+0229D
Odblac; U+00150
odblac; U+00151
odiv; U+02A38
odot; U+02299
odsold; U+029BC
OElig; U+00152
oelig; U+00153
ofcir; U+029BF
Ofr; U+1D512
ofr; U+1D52C
ogon; U+002DB
Ograve; U+000D2
Ograve U+000D2
ograve; U+000F2
ograve U+000F2
ogt; U+029C1
ohbar; U+029B5
ohm; U+003A9
oint; U+0222E
olarr; U+021BA
olcir; U+029BE
olcross; U+029BB
oline; U+0203E
olt; U+029C0
Omacr; U+0014C
omacr; U+0014D
Omega; U+003A9
omega; U+003C9
Omicron; U+0039F
omicron; U+003BF
omid; U+029B6
ominus; U+02296
Oopf; U+1D546
oopf; U+1D560
opar; U+029B7
OpenCurlyDoubleQuote; U+0201C
OpenCurlyQuote; U+02018
operp; U+029B9
oplus; U+02295
Or; U+02A54
or; U+02228
orarr; U+021BB
ord; U+02A5D
order; U+02134
orderof; U+02134
ordf; U+000AA
ordf U+000AA
ordm; U+000BA
ordm U+000BA
origof; U+022B6
oror; U+02A56
orslope; U+02A57
orv; U+02A5B
oS; U+024C8
Oscr; U+1D4AA
oscr; U+02134
Oslash; U+000D8
Oslash U+000D8
oslash; U+000F8
oslash U+000F8
osol; U+02298
Otilde; U+000D5
Otilde U+000D5
otilde; U+000F5
otilde U+000F5
Otimes; U+02A37
otimes; U+02297
otimesas; U+02A36
Ouml; U+000D6
Ouml U+000D6
ouml; U+000F6
ouml U+000F6
ovbar; U+0233D
OverBar; U+0203E
OverBrace; U+023DE
OverBracket; U+023B4
OverParenthesis; U+023DC
par; U+02225
para; U+000B6
para U+000B6
parallel; U+02225
parsim; U+02AF3
parsl; U+02AFD
part; U+02202
PartialD; U+02202
Pcy; U+0041F
pcy; U+0043F
percnt; U+00025
period; U+0002E
permil; U+02030
perp; U+022A5
pertenk; U+02031
Pfr; U+1D513
pfr; U+1D52D
Phi; U+003A6
phi; U+003C6
phiv; U+003D5
phmmat; U+02133
phone; U+0260E
Pi; U+003A0
pi; U+003C0
pitchfork; U+022D4
piv; U+003D6
planck; U+0210F
planckh; U+0210E
plankv; U+0210F
plus; U+0002B
plusacir; U+02A23
plusb; U+0229E
pluscir; U+02A22
plusdo; U+02214
plusdu; U+02A25
pluse; U+02A72
PlusMinus; U+000B1
plusmn; U+000B1
plusmn U+000B1
plussim; U+02A26
plustwo; U+02A27
pm; U+000B1
Poincareplane; U+0210C
pointint; U+02A15
Popf; U+02119
popf; U+1D561
pound; U+000A3
pound U+000A3
Pr; U+02ABB
pr; U+0227A
prap; U+02AB7
prcue; U+0227C
prE; U+02AB3
pre; U+02AAF
prec; U+0227A
precapprox; U+02AB7
preccurlyeq; U+0227C
Precedes; U+0227A
PrecedesEqual; U+02AAF
PrecedesSlantEqual; U+0227C
PrecedesTilde; U+0227E
preceq; U+02AAF
precnapprox; U+02AB9
precneqq; U+02AB5
precnsim; U+022E8
precsim; U+0227E
Prime; U+02033
prime; U+02032
primes; U+02119
prnap; U+02AB9
prnE; U+02AB5
prnsim; U+022E8
prod; U+0220F
Product; U+0220F
profalar; U+0232E
profline; U+02312
profsurf; U+02313
prop; U+0221D
Proportion; U+02237
Proportional; U+0221D
propto; U+0221D
prsim; U+0227E
prurel; U+022B0
Pscr; U+1D4AB
pscr; U+1D4C5
Psi; U+003A8
psi; U+003C8
puncsp; U+02008
Qfr; U+1D514
qfr; U+1D52E
qint; U+02A0C
Qopf; U+0211A
qopf; U+1D562
qprime; U+02057
Qscr; U+1D4AC
qscr; U+1D4C6
quaternions; U+0210D
quatint; U+02A16
quest; U+0003F
questeq; U+0225F
QUOT; U+00022
QUOT U+00022
quot; U+00022
quot U+00022
rAarr; U+021DB
race; U+0223D U+00331
Racute; U+00154
racute; U+00155
radic; U+0221A
raemptyv; U+029B3
Rang; U+027EB
rang; U+027E9
rangd; U+02992
range; U+029A5
rangle; U+027E9
raquo; U+000BB
raquo U+000BB
Rarr; U+021A0
rArr; U+021D2
rarr; U+02192
rarrap; U+02975
rarrb; U+021E5
rarrbfs; U+02920
rarrc; U+02933
rarrfs; U+0291E
rarrhk; U+021AA
rarrlp; U+021AC
rarrpl; U+02945
rarrsim; U+02974
Rarrtl; U+02916
rarrtl; U+021A3
rarrw; U+0219D
rAtail; U+0291C
ratail; U+0291A
ratio; U+02236
rationals; U+0211A
RBarr; U+02910
rBarr; U+0290F
rbarr; U+0290D
rbbrk; U+02773
rbrace; U+0007D
rbrack; U+0005D
rbrke; U+0298C
rbrksld; U+0298E
rbrkslu; U+02990
Rcaron; U+00158
rcaron; U+00159
Rcedil; U+00156
rcedil; U+00157
rceil; U+02309
rcub; U+0007D
Rcy; U+00420
rcy; U+00440
rdca; U+02937
rdldhar; U+02969
rdquo; U+0201D
rdquor; U+0201D
rdsh; U+021B3
Re; U+0211C
real; U+0211C
realine; U+0211B
realpart; U+0211C
reals; U+0211D
rect; U+025AD
REG; U+000AE
REG U+000AE
reg; U+000AE
reg U+000AE
ReverseElement; U+0220B
ReverseEquilibrium; U+021CB
ReverseUpEquilibrium; U+0296F
rfisht; U+0297D
rfloor; U+0230B
Rfr; U+0211C
rfr; U+1D52F
rHar; U+02964
rhard; U+021C1
rharu; U+021C0
rharul; U+0296C
Rho; U+003A1
rho; U+003C1
rhov; U+003F1
RightAngleBracket; U+027E9
RightArrow; U+02192
Rightarrow; U+021D2
rightarrow; U+02192
RightArrowBar; U+021E5
RightArrowLeftArrow; U+021C4
rightarrowtail; U+021A3
RightCeiling; U+02309
RightDoubleBracket; U+027E7
RightDownTeeVector; U+0295D
RightDownVector; U+021C2
RightDownVectorBar; U+02955
RightFloor; U+0230B
rightharpoondown; U+021C1
rightharpoonup; U+021C0
rightleftarrows; U+021C4
rightleftharpoons; U+021CC
rightrightarrows; U+021C9
rightsquigarrow; U+0219D
RightTee; U+022A2
RightTeeArrow; U+021A6
RightTeeVector; U+0295B
rightthreetimes; U+022CC
RightTriangle; U+022B3
RightTriangleBar; U+029D0
RightTriangleEqual; U+022B5
RightUpDownVector; U+0294F
RightUpTeeVector; U+0295C
RightUpVector; U+021BE
RightUpVectorBar; U+02954
RightVector; U+021C0
RightVectorBar; U+02953
ring; U+002DA
risingdotseq; U+02253
rlarr; U+021C4
rlhar; U+021CC
rlm; U+0200F
rmoust; U+023B1
rmoustache; U+023B1
rnmid; U+02AEE
roang; U+027ED
roarr; U+021FE
robrk; U+027E7
ropar; U+02986
Ropf; U+0211D
ropf; U+1D563
roplus; U+02A2E
rotimes; U+02A35
RoundImplies; U+02970
rpar; U+00029
rpargt; U+02994
rppolint; U+02A12
rrarr; U+021C9
Rrightarrow; U+021DB
rsaquo; U+0203A
Rscr; U+0211B
rscr; U+1D4C7
Rsh; U+021B1
rsh; U+021B1
rsqb; U+0005D
rsquo; U+02019
rsquor; U+02019
rthree; U+022CC
rtimes; U+022CA
rtri; U+025B9
rtrie; U+022B5
rtrif; U+025B8
rtriltri; U+029CE
RuleDelayed; U+029F4
ruluhar; U+02968
rx; U+0211E
Sacute; U+0015A
sacute; U+0015B
sbquo; U+0201A
Sc; U+02ABC
sc; U+0227B
scap; U+02AB8
Scaron; U+00160
scaron; U+00161
sccue; U+0227D
scE; U+02AB4
sce; U+02AB0
Scedil; U+0015E
scedil; U+0015F
Scirc; U+0015C
scirc; U+0015D
scnap; U+02ABA
scnE; U+02AB6
scnsim; U+022E9
scpolint; U+02A13
scsim; U+0227F
Scy; U+00421
scy; U+00441
sdot; U+022C5
sdotb; U+022A1
sdote; U+02A66
searhk; U+02925
seArr; U+021D8
searr; U+02198
searrow; U+02198
sect; U+000A7
sect U+000A7
semi; U+0003B
seswar; U+02929
setminus; U+02216
setmn; U+02216
sext; U+02736
Sfr; U+1D516
sfr; U+1D530
sfrown; U+02322
sharp; U+0266F
SHCHcy; U+00429
shchcy; U+00449
SHcy; U+00428
shcy; U+00448
ShortDownArrow; U+02193
ShortLeftArrow; U+02190
shortmid; U+02223
shortparallel; U+02225
ShortRightArrow; U+02192
ShortUpArrow; U+02191
shy; U+000AD
shy U+000AD
Sigma; U+003A3
sigma; U+003C3
sigmaf; U+003C2
sigmav; U+003C2
sim; U+0223C
simdot; U+02A6A
sime; U+02243
simeq; U+02243
simg; U+02A9E
simgE; U+02AA0
siml; U+02A9D
simlE; U+02A9F
simne; U+02246
simplus; U+02A24
simrarr; U+02972
slarr; U+02190
SmallCircle; U+02218
smallsetminus; U+02216
smashp; U+02A33
smeparsl; U+029E4
smid; U+02223
smile; U+02323
smt; U+02AAA
smte; U+02AAC
smtes; U+02AAC U+0FE00
SOFTcy; U+0042C
softcy; U+0044C
sol; U+0002F
solb; U+029C4
solbar; U+0233F
Sopf; U+1D54A
sopf; U+1D564
spades; U+02660
spadesuit; U+02660
spar; U+02225
sqcap; U+02293
sqcaps; U+02293 U+0FE00
sqcup; U+02294
sqcups; U+02294 U+0FE00
Sqrt; U+0221A
sqsub; U+0228F
sqsube; U+02291
sqsubset; U+0228F
sqsubseteq; U+02291
sqsup; U+02290
sqsupe; U+02292
sqsupset; U+02290
sqsupseteq; U+02292
squ; U+025A1
Square; U+025A1
square; U+025A1
SquareIntersection; U+02293
SquareSubset; U+0228F
SquareSubsetEqual; U+02291
SquareSuperset; U+02290
SquareSupersetEqual; U+02292
SquareUnion; U+02294
squarf; U+025AA
squf; U+025AA
srarr; U+02192
Sscr; U+1D4AE
sscr; U+1D4C8
ssetmn; U+02216
ssmile; U+02323
sstarf; U+022C6
Star; U+022C6
star; U+02606
starf; U+02605
straightepsilon; U+003F5
straightphi; U+003D5
strns; U+000AF
Sub; U+022D0
sub; U+02282
subdot; U+02ABD
subE; U+02AC5
sube; U+02286
subedot; U+02AC3
submult; U+02AC1
subnE; U+02ACB
subne; U+0228A
subplus; U+02ABF
subrarr; U+02979
Subset; U+022D0
subset; U+02282
subseteq; U+02286
subseteqq; U+02AC5
SubsetEqual; U+02286
subsetneq; U+0228A
subsetneqq; U+02ACB
subsim; U+02AC7
subsub; U+02AD5
subsup; U+02AD3
succ; U+0227B
succapprox; U+02AB8
succcurlyeq; U+0227D
Succeeds; U+0227B
SucceedsEqual; U+02AB0
SucceedsSlantEqual; U+0227D
SucceedsTilde; U+0227F
succeq; U+02AB0
succnapprox; U+02ABA
succneqq; U+02AB6
succnsim; U+022E9
succsim; U+0227F
SuchThat; U+0220B
Sum; U+02211
sum; U+02211
sung; U+0266A
Sup; U+022D1
sup; U+02283
sup1; U+000B9
sup1 U+000B9
sup2; U+000B2
sup2 U+000B2
sup3; U+000B3
sup3 U+000B3
supdot; U+02ABE
supdsub; U+02AD8
supE; U+02AC6
supe; U+02287
supedot; U+02AC4
Superset; U+02283
SupersetEqual; U+02287
suphsol; U+027C9
suphsub; U+02AD7
suplarr; U+0297B
supmult; U+02AC2
supnE; U+02ACC
supne; U+0228B
supplus; U+02AC0
Supset; U+022D1
supset; U+02283
supseteq; U+02287
supseteqq; U+02AC6
supsetneq; U+0228B
supsetneqq; U+02ACC
supsim; U+02AC8
supsub; U+02AD4
supsup; U+02AD6
swarhk; U+02926
swArr; U+021D9
swarr; U+02199
swarrow; U+02199
swnwar; U+0292A
szlig; U+000DF
szlig U+000DF
Tab; U+00009
target; U+02316
Tau; U+003A4
tau; U+003C4
tbrk; U+023B4
Tcaron; U+00164
tcaron; U+00165
Tcedil; U+00162
tcedil; U+00163
Tcy; U+00422
tcy; U+00442
tdot; U+020DB
telrec; U+02315
Tfr; U+1D517
tfr; U+1D531
there4; U+02234
Therefore; U+02234
therefore; U+02234
Theta; U+00398
theta; U+003B8
thetasym; U+003D1
thetav; U+003D1
thickapprox; U+02248
thicksim; U+0223C
ThickSpace; U+0205F U+0200A
thinsp; U+02009
ThinSpace; U+02009
thkap; U+02248
thksim; U+0223C
THORN; U+000DE
THORN U+000DE
thorn; U+000FE
thorn U+000FE
Tilde; U+0223C
tilde; U+002DC
TildeEqual; U+02243
TildeFullEqual; U+02245
TildeTilde; U+02248
times; U+000D7
times U+000D7
timesb; U+022A0
timesbar; U+02A31
timesd; U+02A30
tint; U+0222D
toea; U+02928
top; U+022A4
topbot; U+02336
topcir; U+02AF1
Topf; U+1D54B
topf; U+1D565
topfork; U+02ADA
tosa; U+02929
tprime; U+02034
TRADE; U+02122
trade; U+02122
triangle; U+025B5
triangledown; U+025BF
triangleleft; U+025C3
trianglelefteq; U+022B4
triangleq; U+0225C
triangleright; U+025B9
trianglerighteq; U+022B5
tridot; U+025EC
trie; U+0225C
triminus; U+02A3A
TripleDot; U+020DB
triplus; U+02A39
trisb; U+029CD
tritime; U+02A3B
trpezium; U+023E2
Tscr; U+1D4AF
tscr; U+1D4C9
TScy; U+00426
tscy; U+00446
TSHcy; U+0040B
tshcy; U+0045B
Tstrok; U+00166
tstrok; U+00167
twixt; U+0226C
twoheadleftarrow; U+0219E
twoheadrightarrow; U+021A0
Uacute; U+000DA
Uacute U+000DA
uacute; U+000FA
uacute U+000FA
Uarr; U+0219F
uArr; U+021D1
uarr; U+02191
Uarrocir; U+02949
Ubrcy; U+0040E
ubrcy; U+0045E
Ubreve; U+0016C
ubreve; U+0016D
Ucirc; U+000DB
Ucirc U+000DB
ucirc; U+000FB
ucirc U+000FB
Ucy; U+00423
ucy; U+00443
udarr; U+021C5
Udblac; U+00170
udblac; U+00171
udhar; U+0296E
ufisht; U+0297E
Ufr; U+1D518
ufr; U+1D532
Ugrave; U+000D9
Ugrave U+000D9
ugrave; U+000F9
ugrave U+000F9
uHar; U+02963
uharl; U+021BF
uharr; U+021BE
uhblk; U+02580
ulcorn; U+0231C
ulcorner; U+0231C
ulcrop; U+0230F
ultri; U+025F8
Umacr; U+0016A
umacr; U+0016B
uml; U+000A8
uml U+000A8
UnderBar; U+0005F
UnderBrace; U+023DF
UnderBracket; U+023B5
UnderParenthesis; U+023DD
Union; U+022C3
UnionPlus; U+0228E
Uogon; U+00172
uogon; U+00173
Uopf; U+1D54C
uopf; U+1D566
UpArrow; U+02191
Uparrow; U+021D1
uparrow; U+02191
UpArrowBar; U+02912
UpArrowDownArrow; U+021C5
UpDownArrow; U+02195
Updownarrow; U+021D5
updownarrow; U+02195
UpEquilibrium; U+0296E
upharpoonleft; U+021BF
upharpoonright; U+021BE
uplus; U+0228E
UpperLeftArrow; U+02196
UpperRightArrow; U+02197
Upsi; U+003D2
upsi; U+003C5
upsih; U+003D2
Upsilon; U+003A5
upsilon; U+003C5
UpTee; U+022A5
UpTeeArrow; U+021A5
upuparrows; U+021C8
urcorn; U+0231D
urcorner; U+0231D
urcrop; U+0230E
Uring; U+0016E
uring; U+0016F
urtri; U+025F9
Uscr; U+1D4B0
uscr; U+1D4CA
utdot; U+022F0
Utilde; U+00168
utilde; U+00169
utri; U+025B5
utrif; U+025B4
uuarr; U+021C8
Uuml; U+000DC
Uuml U+000DC
uuml; U+000FC
uuml U+000FC
uwangle; U+029A7
vangrt; U+0299C
varepsilon; U+003F5
varkappa; U+003F0
varnothing; U+02205
varphi; U+003D5
varpi; U+003D6
varpropto; U+0221D
vArr; U+021D5
varr; U+02195
varrho; U+003F1
varsigma; U+003C2
varsubsetneq; U+0228A U+0FE00
varsubsetneqq; U+02ACB U+0FE00
varsupsetneq; U+0228B U+0FE00
varsupsetneqq; U+02ACC U+0FE00
vartheta; U+003D1
vartriangleleft; U+022B2
vartriangleright; U+022B3
Vbar; U+02AEB
vBar; U+02AE8
vBarv; U+02AE9
Vcy; U+00412
vcy; U+00432
VDash; U+022AB
Vdash; U+022A9
vDash; U+022A8
vdash; U+022A2
Vdashl; U+02AE6
Vee; U+022C1
vee; U+02228
veebar; U+022BB
veeeq; U+0225A
vellip; U+022EE
Verbar; U+02016
verbar; U+0007C
Vert; U+02016
vert; U+0007C
VerticalBar; U+02223
VerticalLine; U+0007C
VerticalSeparator; U+02758
VerticalTilde; U+02240
VeryThinSpace; U+0200A
Vfr; U+1D519
vfr; U+1D533
vltri; U+022B2
vnsub; U+02282 U+020D2
vnsup; U+02283 U+020D2
Vopf; U+1D54D
vopf; U+1D567
vprop; U+0221D
vrtri; U+022B3
Vscr; U+1D4B1
vscr; U+1D4CB
vsubnE; U+02ACB U+0FE00
vsubne; U+0228A U+0FE00
vsupnE; U+02ACC U+0FE00
vsupne; U+0228B U+0FE00
Vvdash; U+022AA
vzigzag; U+0299A
Wcirc; U+00174
wcirc; U+00175
wedbar; U+02A5F
Wedge; U+022C0
wedge; U+02227
wedgeq; U+02259
weierp; U+02118
Wfr; U+1D51A
wfr; U+1D534
Wopf; U+1D54E
wopf; U+1D568
wp; U+02118
wr; U+02240
wreath; U+02240
Wscr; U+1D4B2
wscr; U+1D4CC
xcap; U+022C2
xcirc; U+025EF
xcup; U+022C3
xdtri; U+025BD
Xfr; U+1D51B
xfr; U+1D535
xhArr; U+027FA
xharr; U+027F7
Xi; U+0039E
xi; U+003BE
xlArr; U+027F8
xlarr; U+027F5
xmap; U+027FC
xnis; U+022FB
xodot; U+02A00
Xopf; U+1D54F
xopf; U+1D569
xoplus; U+02A01
xotime; U+02A02
xrArr; U+027F9
xrarr; U+027F6
Xscr; U+1D4B3
xscr; U+1D4CD
xsqcup; U+02A06
xuplus; U+02A04
xutri; U+025B3
xvee; U+022C1
xwedge; U+022C0
Yacute; U+000DD
Yacute U+000DD
yacute; U+000FD
yacute U+000FD
YAcy; U+0042F
yacy; U+0044F
Ycirc; U+00176
ycirc; U+00177
Ycy; U+0042B
ycy; U+0044B
yen; U+000A5
yen U+000A5
Yfr; U+1D51C
yfr; U+1D536
YIcy; U+00407
yicy; U+00457
Yopf; U+1D550
yopf; U+1D56A
Yscr; U+1D4B4
yscr; U+1D4CE
YUcy; U+0042E
yucy; U+0044E
Yuml; U+00178
yuml; U+000FF
yuml U+000FF
Zacute; U+00179
zacute; U+0017A
Zcaron; U+0017D
zcaron; U+0017E
Zcy; U+00417
zcy; U+00437
Zdot; U+0017B
zdot; U+0017C
zeetrf; U+02128
ZeroWidthSpace; U+0200B
Zeta; U+00396
zeta; U+003B6
Zfr; U+02128
zfr; U+1D537
ZHcy; U+00416
zhcy; U+00436
zigrarr; U+021DD
Zopf; U+02124
zopf; U+1D56B
Zscr; U+1D4B5
zscr; U+1D4CF
zwj; U+0200D
zwnj; U+0200C
