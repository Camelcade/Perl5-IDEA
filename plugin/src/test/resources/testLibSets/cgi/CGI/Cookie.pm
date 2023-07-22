package CGI::Cookie;

use strict;
use warnings;

our $VERSION = '4.56';

use CGI::Util qw(rearrange unescape escape);
use overload '""' => \&as_string, 'cmp' => \&compare, 'fallback' => 1;

my $PERLEX = 0;
# Turn on special checking for ActiveState's PerlEx
$PERLEX++ if defined($ENV{'GATEWAY_INTERFACE'}) && $ENV{'GATEWAY_INTERFACE'} =~ /^CGI-PerlEx/;

# Turn on special checking for mod_perl
# PerlEx::DBI tries to fool DBI by setting MOD_PERL
my $MOD_PERL = 0;
if (exists $ENV{MOD_PERL} && !$PERLEX) {
    if (exists $ENV{MOD_PERL_API_VERSION} && $ENV{MOD_PERL_API_VERSION} == 2) {
        $MOD_PERL = 2;
        require Apache2::RequestUtil;
        require APR::Table;
    }
    else {
        $MOD_PERL = 1;
        require Apache;
    }
}

# fetch a list of cookies from the environment and
# return as a hash.  the cookies are parsed as normal
# escaped URL data.
sub fetch {
    my $class = shift;
    my $raw_cookie = get_raw_cookie(@_) or return;
    return $class->parse($raw_cookie);
}

# Fetch a list of cookies from the environment or the incoming headers and
# return as a hash. The cookie values are not unescaped or altered in any way.
sub raw_fetch {
    my $class = shift;
    my $raw_cookie = get_raw_cookie(@_) or return;
    my %results;
    my ($key, $value);

    my @pairs = split("[;,] ?", $raw_cookie);
    for my $pair (@pairs) {
        $pair =~ s/^\s+|\s+$//g; # trim leading trailing whitespace
        my ($key, $value) = split "=", $pair;

        $value = defined $value ? $value : '';
        $results{$key} = $value;
    }
    return wantarray ? %results : \%results;
}

sub get_raw_cookie {
    my $r = shift;
    $r ||= eval {$MOD_PERL == 2 ?
        Apache2::RequestUtil->request() :
        Apache->request} if $MOD_PERL;

    return $r->headers_in->{'Cookie'} if $r;

    die "Run $r->subprocess_env; before calling fetch()"
        if $MOD_PERL and !exists $ENV{REQUEST_METHOD};

    return $ENV{HTTP_COOKIE} || $ENV{COOKIE};
}

sub parse {
    my ($self, $raw_cookie) = @_;
    return wantarray ? () : {} unless $raw_cookie;

    my %results;

    my @pairs = split("[;,] ?", $raw_cookie);
    for (@pairs) {
        s/^\s+//;
        s/\s+$//;

        my ($key, $value) = split("=", $_, 2);

        # Some foreign cookies are not in name=value format, so ignore
        # them.
        next if !defined($value);
        my @values = ();
        if ($value ne '') {
            @values = map unescape($_), split(/[&;]/, $value . '&dmy');
            pop @values;
        }
        $key = unescape($key);
        # A bug in Netscape can cause several cookies with same name to
        # appear.  The FIRST one in HTTP_COOKIE is the most recent version.
        $results{$key} ||= $self->new(-name => $key, -value => \@values);
    }
    return wantarray ? %results : \%results;
}

sub new {
    my ($class, @params) = @_;
    $class = ref($class) || $class;
    # Ignore mod_perl request object--compatibility with Apache::Cookie.
    shift if ref $params[0]
        && eval {$params[0]->isa('Apache::Request::Req') || $params[0]->isa('Apache')};
    my ($name, $value, $path, $domain, $secure, $expires, $max_age, $httponly, $samesite, $priority)
        = rearrange(
        [
            'NAME', [ 'VALUE', 'VALUES' ],
            'PATH', 'DOMAIN',
            'SECURE', 'EXPIRES',
            'MAX-AGE', 'HTTPONLY', 'SAMESITE',
            'PRIORITY',
        ],
        @params
    );
    return undef unless defined $name and defined $value;
    my $self = {};
    bless $self, $class;
    $self->name($name);
    $self->value($value);
    $path ||= "/";
    $self->path($path) if defined $path;
    $self->domain($domain) if defined $domain;
    $self->secure($secure) if defined $secure;
    $self->expires($expires) if defined $expires;
    $self->max_age($max_age) if defined $max_age;
    $self->httponly($httponly) if defined $httponly;
    $self->samesite($samesite) if defined $samesite;
    $self->priority($priority) if defined $priority;
    return $self;
}

sub as_string {
    my $self = shift;
    return "" unless $self->name;

    no warnings; # some things may be undefined, that's OK.

    my $name = escape($self->name);
    my $value = join "&", map {escape($_)} $self->value;
    my @cookie = ("$name=$value");

    push @cookie, "domain=" . $self->domain if $self->domain;
    push @cookie, "path=" . $self->path if $self->path;
    push @cookie, "expires=" . $self->expires if $self->expires;
    push @cookie, "max-age=" . $self->max_age if $self->max_age;
    push @cookie, "secure" if $self->secure;
    push @cookie, "HttpOnly" if $self->httponly;
    push @cookie, "SameSite=" . $self->samesite if $self->samesite;
    push @cookie, "Priority=" . $self->priority if $self->priority;

    return join "; ", @cookie;
}

sub compare {
    my ($self, $value) = @_;
    return "$self" cmp $value;
}

sub bake {
    my ($self, $r) = @_;

    $r ||= eval {
        $MOD_PERL == 2
            ? Apache2::RequestUtil->request()
            : Apache->request
    } if $MOD_PERL;
    if ($r) {
        $r->err_headers_out->add('Set-Cookie' => $self->as_string);
    }
    else {
        require CGI;
        print CGI::header(-cookie => $self);
    }

}

# accessors
sub name {
    my ($self, $name) = @_;
    $self->{'name'} = $name if defined $name;
    return $self->{'name'};
}

sub value {
    my ($self, $value) = @_;
    if (defined $value) {
        my @values
            = ref $value eq 'ARRAY' ? @$value
            : ref $value eq 'HASH' ? %$value
            : ($value);
        $self->{'value'} = [ @values ];
    }
    return wantarray ? @{$self->{'value'}} : $self->{'value'}->[0];
}

sub domain {
    my ($self, $domain) = @_;
    $self->{'domain'} = lc $domain if defined $domain;
    return $self->{'domain'};
}

sub secure {
    my ($self, $secure) = @_;
    $self->{'secure'} = $secure if defined $secure;
    return $self->{'secure'};
}

sub expires {
    my ($self, $expires) = @_;
    $self->{'expires'} = CGI::Util::expires($expires, 'cookie') if defined $expires;
    return $self->{'expires'};
}

sub max_age {
    my ($self, $max_age) = @_;
    $self->{'max-age'} = CGI::Util::expire_calc($max_age) - time() if defined $max_age;
    return $self->{'max-age'};
}

sub path {
    my ($self, $path) = @_;
    $self->{'path'} = $path if defined $path;
    return $self->{'path'};
}

sub httponly {
    # HttpOnly
    my ($self, $httponly) = @_;
    $self->{'httponly'} = $httponly if defined $httponly;
    return $self->{'httponly'};
}

my %_legal_samesite = (Strict => 1, Lax => 1, None => 1);
sub samesite {
    # SameSite
    my $self = shift;
    my $samesite = ucfirst lc +shift if @_; # Normalize casing.
    $self->{'samesite'} = $samesite if $samesite and $_legal_samesite{$samesite};
    return $self->{'samesite'};
}

my %_legal_priority = (Low => 1, Medium => 1, High => 1);
sub priority {
    my $self = shift;
    my $priority = ucfirst lc +shift if @_;
    if ($priority && $_legal_priority{$priority}) {
        $self->{'priority'} = $priority;
    }
    return $self->{'priority'};
}

1;

=head1 NAME

CGI::Cookie - Interface to HTTP Cookies

=head1 SYNOPSIS

    use CGI qw/:standard/;
    use CGI::Cookie;

    # Create new cookies and send them
    $cookie1 = CGI::Cookie->new(-name=>'ID',-value=>123456);
    $cookie2 = CGI::Cookie->new(-name=>'preferences',
                               -value=>{ font => Helvetica,
                                         size => 12 } 
                               );
    print header(-cookie=>[$cookie1,$cookie2]);

    # fetch existing cookies
    %cookies = CGI::Cookie->fetch;
    $id = $cookies{'ID'}->value;

    # create cookies returned from an external source
    %cookies = CGI::Cookie->parse($ENV{COOKIE});

=head1 DESCRIPTION

CGI::Cookie is an interface to HTTP/1.1 cookies, a mechanism
that allows Web servers to store persistent information on
the browser's side of the connection.  Although CGI::Cookie is
intended to be used in conjunction with CGI.pm (and is in fact used by
it internally), you can use this module independently.

For full information on cookies see 

    https://tools.ietf.org/html/rfc6265

=head1 USING CGI::Cookie

CGI::Cookie is object oriented.  Each cookie object has a name and a
value.  The name is any scalar value.  The value is any scalar or
array value (associative arrays are also allowed).  Cookies also have
several optional attributes, including:

=over 4

=item B<1. expiration date>

The expiration date tells the browser how long to hang on to the
cookie.  If the cookie specifies an expiration date in the future, the
browser will store the cookie information in a disk file and return it
to the server every time the user reconnects (until the expiration
date is reached).  If the cookie species an expiration date in the
past, the browser will remove the cookie from the disk file.  If the
expiration date is not specified, the cookie will persist only until
the user quits the browser.

=item B<2. domain>

This is a partial or complete domain name for which the cookie is 
valid.  The browser will return the cookie to any host that matches
the partial domain name.  For example, if you specify a domain name
of ".capricorn.com", then the browser will return the cookie to
Web servers running on any of the machines "www.capricorn.com", 
"ftp.capricorn.com", "feckless.capricorn.com", etc.  Domain names
must contain at least two periods to prevent attempts to match
on top level domains like ".edu".  If no domain is specified, then
the browser will only return the cookie to servers on the host the
cookie originated from.

=item B<3. path>

If you provide a cookie path attribute, the browser will check it
against your script's URL before returning the cookie.  For example,
if you specify the path "/cgi-bin", then the cookie will be returned
to each of the scripts "/cgi-bin/tally.pl", "/cgi-bin/order.pl", and
"/cgi-bin/customer_service/complain.pl", but not to the script
"/cgi-private/site_admin.pl".  By default, the path is set to "/", so
that all scripts at your site will receive the cookie.

=item B<4. secure flag>

If the "secure" attribute is set, the cookie will only be sent to your
script if the CGI request is occurring on a secure channel, such as SSL.

=item B<5. httponly flag>

If the "httponly" attribute is set, the cookie will only be accessible
through HTTP Requests. This cookie will be inaccessible via JavaScript
(to prevent XSS attacks).

This feature is supported by nearly all modern browsers.

See these URLs for more information:

    http://msdn.microsoft.com/en-us/library/ms533046.aspx
    http://www.browserscope.org/?category=security&v=top

=item B<6. samesite flag>

Allowed settings are C<Strict>, C<Lax> and C<None>.

As of June 2016, support is limited to recent releases of Chrome and Opera.

L<https://tools.ietf.org/html/draft-west-first-party-cookies-07>

=item B<7. priority flag>

Allowed settings are C<Low>, C<Medium> and C<High>.

Support is limited to recent releases of Chrome.

=back

=head2 Creating New Cookies

	my $c = CGI::Cookie->new(-name    =>  'foo',
                             -value   =>  'bar',
                             -expires =>  '+3M',
                           '-max-age' =>  '+3M',
                             -domain  =>  '.capricorn.com',
                             -path    =>  '/cgi-bin/database',
                             -secure  =>  1,
                             -samesite=>  "Lax",
                             -priority=>  "High",
	                    );

Create cookies from scratch with the B<new> method.  The B<-name> and
B<-value> parameters are required.  The name must be a scalar value.
The value can be a scalar, an array reference, or a hash reference.
(At some point in the future cookies will support one of the Perl
object serialization protocols for full generality).

B<-expires> accepts any of the relative or absolute date formats
recognized by CGI.pm, for example "+3M" for three months in the
future.  See CGI.pm's documentation for details.

B<-max-age> accepts the same data formats as B<< -expires >>, but sets a
relative value instead of an absolute like B<< -expires >>. This is intended to be
more secure since a clock could be changed to fake an absolute time. In
practice, as of 2011, C<< -max-age >> still does not enjoy the widespread support
that C<< -expires >> has. You can set both, and browsers that support
C<< -max-age >> should ignore the C<< Expires >> header. The drawback
to this approach is the bit of bandwidth for sending an extra header on each cookie.

B<-domain> points to a domain name or to a fully qualified host name.
If not specified, the cookie will be returned only to the Web server
that created it.

B<-path> points to a partial URL on the current server.  The cookie
will be returned to all URLs beginning with the specified path.  If
not specified, it defaults to '/', which returns the cookie to all
pages at your site.

B<-secure> if set to a true value instructs the browser to return the
cookie only when a cryptographic protocol is in use.

B<-httponly> if set to a true value, the cookie will not be accessible
via JavaScript.

B<-samesite> may be C<Lax>, C<Strict>, or C<None> and is an evolving part
of the standards for cookies. Please refer to current documentation
regarding it.

For compatibility with Apache::Cookie, you may optionally pass in
a mod_perl request object as the first argument to C<new()>. It will
simply be ignored:

  my $c = CGI::Cookie->new($r,
                          -name    =>  'foo',
                          -value   =>  ['bar','baz']);

=head2 Sending the Cookie to the Browser

The simplest way to send a cookie to the browser is by calling the bake()
method:

  $c->bake;

This will print the Set-Cookie HTTP header to STDOUT using CGI.pm. CGI.pm
will be loaded for this purpose if it is not already. Otherwise CGI.pm is not
required or used by this module.

Under mod_perl, pass in an Apache request object:

  $c->bake($r);

If you want to set the cookie yourself, Within a CGI script you can send
a cookie to the browser by creating one or more Set-Cookie: fields in the
HTTP header.  Here is a typical sequence:

  my $c = CGI::Cookie->new(-name    =>  'foo',
                          -value   =>  ['bar','baz'],
                          -expires =>  '+3M');

  print "Set-Cookie: $c\n";
  print "Content-Type: text/html\n\n";

To send more than one cookie, create several Set-Cookie: fields.

If you are using CGI.pm, you send cookies by providing a -cookie
argument to the header() method:

  print header(-cookie=>$c);

Mod_perl users can set cookies using the request object's header_out()
method:

  $r->err_headers_out->add('Set-Cookie' => $c);

Internally, Cookie overloads the "" operator to call its as_string()
method when incorporated into the HTTP header.  as_string() turns the
Cookie's internal representation into an RFC-compliant text
representation.  You may call as_string() yourself if you prefer:

  print "Set-Cookie: ",$c->as_string,"\n";

=head2 Recovering Previous Cookies

	%cookies = CGI::Cookie->fetch;

B<fetch> returns an associative array consisting of all cookies
returned by the browser.  The keys of the array are the cookie names.  You
can iterate through the cookies this way:

	%cookies = CGI::Cookie->fetch;
	for (keys %cookies) {
	   do_something($cookies{$_});
        }

In a scalar context, fetch() returns a hash reference, which may be more
efficient if you are manipulating multiple cookies.

CGI.pm uses the URL escaping methods to save and restore reserved characters
in its cookies.  If you are trying to retrieve a cookie set by a foreign server,
this escaping method may trip you up.  Use raw_fetch() instead, which has the
same semantics as fetch(), but performs no unescaping.

You may also retrieve cookies that were stored in some external
form using the parse() class method:

       $COOKIES = `cat /usr/tmp/Cookie_stash`;
       %cookies = CGI::Cookie->parse($COOKIES);

If you are in a mod_perl environment, you can save some overhead by
passing the request object to fetch() like this:

   CGI::Cookie->fetch($r);

If the value passed to parse() is undefined, an empty array will returned in list
context, and an empty hashref will be returned in scalar context.

=head2 Manipulating Cookies

Cookie objects have a series of accessor methods to get and set cookie
attributes.  Each accessor has a similar syntax.  Called without
arguments, the accessor returns the current value of the attribute.
Called with an argument, the accessor changes the attribute and
returns its new value.

=over 4

=item B<name()>

Get or set the cookie's name.  Example:

	$name = $c->name;
	$new_name = $c->name('fred');

=item B<value()>

Get or set the cookie's value.  Example:

	$value = $c->value;
	@new_value = $c->value(['a','b','c','d']);

B<value()> is context sensitive.  In a list context it will return
the current value of the cookie as an array.  In a scalar context it
will return the B<first> value of a multivalued cookie.

=item B<domain()>

Get or set the cookie's domain.

=item B<path()>

Get or set the cookie's path.

=item B<expires()>

Get or set the cookie's expiration time.

=item B<max_age()>

Get or set the cookie's max_age value.

=back


=head1 AUTHOR INFORMATION

The CGI.pm distribution is copyright 1995-2007, Lincoln D. Stein. It is
distributed under the Artistic License 2.0. It is currently
maintained by Lee Johnson with help from many contributors.

Address bug reports and comments to: https://github.com/leejo/CGI.pm/issues

The original bug tracker can be found at: https://rt.cpan.org/Public/Dist/Display.html?Queue=CGI.pm

When sending bug reports, please provide the version of CGI.pm, the version of
Perl, the name and version of your Web server, and the name and version of the
operating system you are using.  If the problem is even remotely browser
dependent, please provide information about the affected browsers as well.

=head1 BUGS

This section intentionally left blank.

=head1 SEE ALSO

L<CGI::Carp>, L<CGI>

L<RFC 2109|http://www.ietf.org/rfc/rfc2109.txt>, L<RFC 2695|http://www.ietf.org/rfc/rfc2965.txt>

=cut
