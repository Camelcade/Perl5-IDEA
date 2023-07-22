package CGI::Push;

my $appease_cpants_kwalitee = q/
use strict;
use warnings;
#/;

$CGI::Push::VERSION = '4.57';
use CGI;
use CGI::Util 'rearrange';
@ISA = ('CGI');

$CGI::DefaultClass = 'CGI::Push';

# add do_push() and push_delay() to exported tags
push(@{$CGI::EXPORT_TAGS{':standard'}}, 'do_push', 'push_delay');

sub do_push {
    my ($self, @p) = CGI::self_or_default(@_);

    # unbuffer output
    $| = 1;
    srand;
    my ($random) = sprintf("%08.0f", rand() * 1E8);
    my ($boundary) = "----=_NeXtPaRt$random";

    my (@header);
    my ($type, $callback, $delay, $last_page, $cookie, $target, $expires, $nph, @other) = rearrange([ TYPE, NEXT_PAGE, DELAY, LAST_PAGE, [ COOKIE, COOKIES ], TARGET, EXPIRES, NPH ], @p);
    $type = 'text/html' unless $type;
    $callback = \&simple_counter unless $callback && ref($callback) eq 'CODE';
    $delay = 1 unless defined($delay);
    $self->push_delay($delay);
    $nph = 1 unless defined($nph);

    my (@o);
    foreach (@other) {push(@o, split("="));}
    push(@o, '-Target' => $target) if defined($target);
    push(@o, '-Cookie' => $cookie) if defined($cookie);
    push(@o, '-Type' => "multipart/x-mixed-replace;boundary=\"$boundary\"");
    push(@o, '-Server' => "CGI.pm Push Module") if $nph;
    push(@o, '-Status' => '200 OK');
    push(@o, '-nph' => 1) if $nph;
    print $self->header(@o);

    $boundary = "$CGI::CRLF--$boundary";

    print "WARNING: YOUR BROWSER DOESN'T SUPPORT THIS SERVER-PUSH TECHNOLOGY.${boundary}$CGI::CRLF";

    my (@contents) = &$callback($self, ++$COUNTER);

    # now we enter a little loop
    while (1) {
        print "Content-type: ${type}$CGI::CRLF$CGI::CRLF" unless $type =~ /^dynamic|heterogeneous$/i;
        print @contents;
        @contents = &$callback($self, ++$COUNTER);
        if ((@contents) && defined($contents[0])) {
            print "${boundary}$CGI::CRLF";
            do_sleep($self->push_delay()) if $self->push_delay();
        }
        else {
            if ($last_page && ref($last_page) eq 'CODE') {
                print "${boundary}$CGI::CRLF";
                do_sleep($self->push_delay()) if $self->push_delay();
                print "Content-type: ${type}$CGI::CRLF$CGI::CRLF" unless $type =~ /^dynamic|heterogeneous$/i;
                print &$last_page($self, $COUNTER);
            }
            print "${boundary}--$CGI::CRLF";
            last;
        }
    }
    print "WARNING: YOUR BROWSER DOESN'T SUPPORT THIS SERVER-PUSH TECHNOLOGY.$CGI::CRLF";
}

sub simple_counter {
    my ($self, $count) = @_;
    return $self->start_html("CGI::Push Default Counter"),
        $self->h1("CGI::Push Default Counter"),
        "This page has been updated ", $self->strong($count), " times.",
        $self->hr(),
        $self->a({ '-href' => 'http://www.genome.wi.mit.edu/ftp/pub/software/WWW/cgi_docs.html' }, 'CGI.pm home page'),
        $self->end_html;
}

sub do_sleep {
    my $delay = shift;
    if (($delay >= 1) && ($delay !~ /\./)) {
        sleep($delay);
    }
    else {
        select(undef, undef, undef, $delay);
        return $delay;
    }
}

sub push_delay {
    my ($self, $delay) = CGI::self_or_default(@_);
    return defined($delay) ? $self->{'.delay'} =
        $delay : $self->{'.delay'};
}

1;

=head1 NAME

CGI::Push - Simple Interface to Server Push

=head1 SYNOPSIS

    use strict;
    use warnings;

    use CGI::Push qw(:standard);

    do_push(
        -next_page => \&next_page,
        -last_page => \&last_page,
        -delay     => 0.5
    );

    sub next_page {
        my($q,$counter) = @_;
        return undef if $counter >= 10;
        ....
    }

    sub last_page {
        my($q,$counter) = @_;
        return ...
    }

=head1 DESCRIPTION

CGI::Push is a subclass of the CGI object created by CGI.pm.  It is
specialized for server push operations, which allow you to create
animated pages whose content changes at regular intervals.

You provide CGI::Push with a pointer to a subroutine that will draw
one page.  Every time your subroutine is called, it generates a new
page.  The contents of the page will be transmitted to the browser
in such a way that it will replace what was there beforehand.  The
technique will work with HTML pages as well as with graphics files, 
allowing you to create animated GIFs.

Only Netscape Navigator supports server push.  Internet Explorer
browsers do not.

=head1 USING CGI::Push

CGI::Push adds one new method to the standard CGI suite, do_push().
When you call this method, you pass it a reference to a subroutine
that is responsible for drawing each new page, an interval delay, and
an optional subroutine for drawing the last page.  Other optional
parameters include most of those recognized by the CGI header()
method.

You may call do_push() in the object oriented manner or not, as you
prefer:

    use CGI::Push;
    $q = CGI::Push->new;
    $q->do_push(-next_page=>\&draw_a_page);

        -or-

    use CGI::Push qw(:standard);
    do_push(-next_page=>\&draw_a_page);

Parameters are as follows:

=over 4

=item -next_page

    do_push(-next_page=>\&my_draw_routine);

This required parameter points to a reference to a subroutine responsible for
drawing each new page.  The subroutine should expect two parameters
consisting of the CGI object and a counter indicating the number
of times the subroutine has been called.  It should return the
contents of the page as an B<array> of one or more items to print.  
It can return a false value (or an empty array) in order to abort the
redrawing loop and print out the final page (if any)

    sub my_draw_routine {
        my($q,$counter) = @_;
        return undef if $counter > 100;
        ...
    }

You are of course free to refer to create and use global variables
within your draw routine in order to achieve special effects.

=item -last_page

This optional parameter points to a reference to the subroutine
responsible for drawing the last page of the series.  It is called
after the -next_page routine returns a false value.  The subroutine
itself should have exactly the same calling conventions as the
-next_page routine.

=item -type

This optional parameter indicates the content type of each page.  It
defaults to "text/html".  Normally the module assumes that each page
is of a homogeneous MIME type.  However if you provide either of the
magic values "heterogeneous" or "dynamic" (the latter provided for the
convenience of those who hate long parameter names), you can specify
the MIME type -- and other header fields -- on a per-page basis.  See 
"heterogeneous pages" for more details.

=item -delay

This indicates the delay, in seconds, between frames.  Smaller delays
refresh the page faster.  Fractional values are allowed.

B<If not specified, -delay will default to 1 second>

=item -cookie, -target, -expires, -nph

These have the same meaning as the like-named parameters in
CGI::header().

If not specified, -nph will default to 1 (as needed for many servers, see below).

=back

=head2 Heterogeneous Pages

Ordinarily all pages displayed by CGI::Push share a common MIME type.
However by providing a value of "heterogeneous" or "dynamic" in the
do_push() -type parameter, you can specify the MIME type of each page
on a case-by-case basis.  

If you use this option, you will be responsible for producing the
HTTP header for each page.  Simply modify your draw routine to
look like this:

    sub my_draw_routine {
        my($q,$counter) = @_;
        return header('text/html'),   # note we're producing the header here
        ....
    }

You can add any header fields that you like, but some (cookies and
status fields included) may not be interpreted by the browser.  One
interesting effect is to display a series of pages, then, after the
last page, to redirect the browser to a new URL.  Because redirect() 
does b<not> work, the easiest way is with a -refresh header field,
as shown below:

    sub my_draw_routine {
        my($q,$counter) = @_;
        return undef if $counter > 10;
        return header('text/html'),   # note we're producing the header here
        ...
    }

    sub my_last_page {
        return header(-refresh=>'5; URL=http://somewhere.else/finished.html',
                      -type=>'text/html'),
        ...
    }

=head2 Changing the Page Delay on the Fly

If you would like to control the delay between pages on a page-by-page
basis, call push_delay() from within your draw routine.  push_delay()
takes a single numeric argument representing the number of seconds you
wish to delay after the current page is displayed and before
displaying the next one.  The delay may be fractional.  Without
parameters, push_delay() just returns the current delay.

=head1 INSTALLING CGI::Push SCRIPTS

Server push scripts must be installed as no-parsed-header (NPH)
scripts in order to work correctly on many servers.  On Unix systems,
this is most often accomplished by prefixing the script's name with "nph-".  
Recognition of NPH scripts happens automatically with WebSTAR and 
Microsoft IIS.  Users of other servers should see their documentation
for help.

Apache web server from version 1.3b2 on does not need server
push scripts installed as NPH scripts: the -nph parameter to do_push()
may be set to a false value to disable the extra headers needed by an
NPH script.

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
Copyright 1995-1998, Lincoln D. Stein.  All rights reserved.  

=head1 BUGS

This section intentionally left blank.

=head1 SEE ALSO

L<CGI::Carp>, L<CGI>

=cut

