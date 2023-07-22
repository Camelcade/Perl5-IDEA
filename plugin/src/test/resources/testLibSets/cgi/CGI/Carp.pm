package CGI::Carp;

my $appease_cpants_kwalitee = q/
use strict;
use warnings;
#/;

=head1 NAME

B<CGI::Carp> - CGI routines for writing to the HTTPD (or other) error log

=head1 SYNOPSIS

    use CGI::Carp;

    croak "We're outta here!";
    confess "It was my fault: $!";
    carp "It was your fault!";   
    warn "I'm confused";
    die  "I'm dying.\n";

    use CGI::Carp qw(cluck);
    cluck "I wouldn't do that if I were you";

    use CGI::Carp qw(fatalsToBrowser);
    die "Fatal error messages are now sent to browser";

=head1 DESCRIPTION

CGI scripts have a nasty habit of leaving warning messages in the error
logs that are neither time stamped nor fully identified.  Tracking down
the script that caused the error is a pain.  This fixes that.  Replace
the usual

    use Carp;

with

    use CGI::Carp

The standard warn(), die (), croak(), confess() and carp() calls will
be replaced with functions that write time-stamped messages to the
HTTP server error log.

For example:

   [Fri Nov 17 21:40:43 1995] test.pl: I'm confused at test.pl line 3.
   [Fri Nov 17 21:40:43 1995] test.pl: Got an error message: Permission denied.
   [Fri Nov 17 21:40:43 1995] test.pl: I'm dying.

=head1 REDIRECTING ERROR MESSAGES

By default, error messages are sent to STDERR.  Most HTTPD servers
direct STDERR to the server's error log.  Some applications may wish
to keep private error logs, distinct from the server's error log, or
they may wish to direct error messages to STDOUT so that the browser
will receive them.

The C<carpout()> function is provided for this purpose.  Since
carpout() is not exported by default, you must import it explicitly by
saying

   use CGI::Carp qw(carpout);

The carpout() function requires one argument, a reference to an open
filehandle for writing errors.  It should be called in a C<BEGIN>
block at the top of the CGI application so that compiler errors will
be caught.  Example:

   BEGIN {
     use CGI::Carp qw(carpout);
     open(LOG, ">>/usr/local/cgi-logs/mycgi-log") or
       die("Unable to open mycgi-log: $!\n");
     carpout(LOG);
   }

carpout() does not handle file locking on the log for you at this
point.  Also, note that carpout() does not work with in-memory file
handles, although a patch would be welcome to address that.

The real STDERR is not closed -- it is moved to CGI::Carp::SAVEERR.
Some servers, when dealing with CGI scripts, close their connection to
the browser when the script closes STDOUT and STDERR.
CGI::Carp::SAVEERR is there to prevent this from happening
prematurely.

You can pass filehandles to carpout() in a variety of ways.  The "correct"
way according to Tom Christiansen is to pass a reference to a filehandle
GLOB:

    carpout(\*LOG);

This looks weird to mere mortals however, so the following syntaxes are
accepted as well:

    carpout(LOG);
    carpout(main::LOG);
    carpout(main'LOG);
    carpout(\LOG);
    carpout(\'main::LOG');

    ... and so on

FileHandle and other objects work as well.

Use of carpout() is not great for performance, so it is recommended
for debugging purposes or for moderate-use applications.  A future
version of this module may delay redirecting STDERR until one of the
CGI::Carp methods is called to prevent the performance hit.

=head1 MAKING PERL ERRORS APPEAR IN THE BROWSER WINDOW

If you want to send fatal (die, confess) errors to the browser, import
the special "fatalsToBrowser" subroutine:

    use CGI::Carp qw(fatalsToBrowser);
    die "Bad error here";

Fatal errors will now be echoed to the browser as well as to the log.
CGI::Carp arranges to send a minimal HTTP header to the browser so
that even errors that occur in the early compile phase will be seen.
Nonfatal errors will still be directed to the log file only (unless
redirected with carpout).

Note that fatalsToBrowser may B<not> work well with mod_perl version 2.0
and higher.

=head2 Changing the default message

By default, the software error message is followed by a note to
contact the Webmaster by e-mail with the time and date of the error.
If this message is not to your liking, you can change it using the
set_message() routine.  This is not imported by default; you should
import it on the use() line:

    use CGI::Carp qw(fatalsToBrowser set_message);
    set_message("It's not a bug, it's a feature!");

You may also pass in a code reference in order to create a custom
error message.  At run time, your code will be called with the text
of the error message that caused the script to die.  Example:

    use CGI::Carp qw(fatalsToBrowser set_message);
    BEGIN {
       sub handle_errors {
          my $msg = shift;
          print "<h1>Oh gosh</h1>";
          print "<p>Got an error: $msg</p>";
      }
      set_message(\&handle_errors);
    }

In order to correctly intercept compile-time errors, you should call
set_message() from within a BEGIN{} block.

=head1 DOING MORE THAN PRINTING A MESSAGE IN THE EVENT OF PERL ERRORS

If fatalsToBrowser in conjunction with set_message does not provide 
you with all of the functionality you need, you can go one step 
further by specifying a function to be executed any time a script
calls "die", has a syntax error, or dies unexpectedly at runtime
with a line like "undef->explode();". 

    use CGI::Carp qw(set_die_handler);
    BEGIN {
       sub handle_errors {
          my $msg = shift;
          print "content-type: text/html\n\n";
          print "<h1>Oh gosh</h1>";
          print "<p>Got an error: $msg</p>";

          #proceed to send an email to a system administrator,
          #write a detailed message to the browser and/or a log,
          #etc....
      }
      set_die_handler(\&handle_errors);
    }

Notice that if you use set_die_handler(), you must handle sending
HTML headers to the browser yourself if you are printing a message.

If you use set_die_handler(), you will most likely interfere with 
the behavior of fatalsToBrowser, so you must use this or that, not 
both. 

Using set_die_handler() sets SIG{__DIE__} (as does fatalsToBrowser),
and there is only one SIG{__DIE__}. This means that if you are 
attempting to set SIG{__DIE__} yourself, you may interfere with 
this module's functionality, or this module may interfere with 
your module's functionality.

=head1 SUPPRESSING PERL ERRORS APPEARING IN THE BROWSER WINDOW

A problem sometimes encountered when using fatalsToBrowser is
when a C<die()> is done inside an C<eval> body or expression.
Even though the
fatalsToBrower support takes precautions to avoid this,
you still may get the error message printed to STDOUT.
This may have some undesirable effects when the purpose of doing the
eval is to determine which of several algorithms is to be used.

By setting C<$CGI::Carp::TO_BROWSER> to 0 you can suppress printing
the C<die> messages but without all of the complexity of using
C<set_die_handler>.  You can localize this effect to inside C<eval>
bodies if this is desirable: For example:

 eval {
   local $CGI::Carp::TO_BROWSER = 0;
   die "Fatal error messages not sent browser"
 }
 # $@ will contain error message


=head1 MAKING WARNINGS APPEAR AS HTML COMMENTS

It is also possible to make non-fatal errors appear as HTML comments
embedded in the output of your program.  To enable this feature,
export the new "warningsToBrowser" subroutine.  Since sending warnings
to the browser before the HTTP headers have been sent would cause an
error, any warnings are stored in an internal buffer until you call
the warningsToBrowser() subroutine with a true argument:

    use CGI::Carp qw(fatalsToBrowser warningsToBrowser);
    use CGI qw(:standard);
    print header();
    warningsToBrowser(1);

You may also give a false argument to warningsToBrowser() to prevent
warnings from being sent to the browser while you are printing some
content where HTML comments are not allowed:

    warningsToBrowser(0);    # disable warnings
    print "<script type=\"text/javascript\"><!--\n";
    print_some_javascript_code();
    print "//--></script>\n";
    warningsToBrowser(1);    # re-enable warnings

Note: In this respect warningsToBrowser() differs fundamentally from
fatalsToBrowser(), which you should never call yourself!

=head1 OVERRIDING THE NAME OF THE PROGRAM

CGI::Carp includes the name of the program that generated the error or
warning in the messages written to the log and the browser window.
Sometimes, Perl can get confused about what the actual name of the
executed program was.  In these cases, you can override the program
name that CGI::Carp will use for all messages.

The quick way to do that is to tell CGI::Carp the name of the program
in its use statement.  You can do that by adding
"name=cgi_carp_log_name" to your "use" statement.  For example:

    use CGI::Carp qw(name=cgi_carp_log_name);

.  If you want to change the program name partway through the program,
you can use the C<set_progname()> function instead.  It is not
exported by default, you must import it explicitly by saying

    use CGI::Carp qw(set_progname);

Once you've done that, you can change the logged name of the program
at any time by calling

    set_progname(new_program_name);

You can set the program back to the default by calling

    set_progname(undef);

Note that this override doesn't happen until after the program has
compiled, so any compile-time errors will still show up with the
non-overridden program name

=head1 TURNING OFF TIMESTAMPS IN MESSAGES

If your web server automatically adds a timestamp to each log line,
you may not need CGI::Carp to add its own. You can disable timestamping
by importing "noTimestamp":

    use CGI::Carp qw(noTimestamp);

Alternatively you can set C<$CGI::Carp::NO_TIMESTAMP> to 1.

Note that the name of the program is still automatically included in
the message.

=head1 GETTING THE FULL PATH OF THE SCRIPT IN MESSAGES

Set C<$CGI::Carp::FULL_PATH> to 1.

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

=head1 SEE ALSO

L<Carp>, L<CGI::Base>, L<CGI::BasePlus>, L<CGI::Request>,
L<CGI::MiniSvr>, L<CGI::Form>, L<CGI::Response>.

=cut

require 5.000;
use Exporter;
#use Carp;
BEGIN {
    require Carp;
    *CORE::GLOBAL::die = \&CGI::Carp::die;
}

use File::Spec;

@ISA = qw(Exporter);
@EXPORT = qw(confess croak carp);
@EXPORT_OK = qw(carpout fatalsToBrowser warningsToBrowser wrap noTimestamp set_message set_die_handler set_progname cluck ^name= die);

$main::SIG{__WARN__} = \&CGI::Carp::warn;

$CGI::Carp::VERSION = '4.57';
$CGI::Carp::CUSTOM_MSG = undef;
$CGI::Carp::DIE_HANDLER = undef;
$CGI::Carp::TO_BROWSER = 1;
$CGI::Carp::NO_TIMESTAMP = 0;
$CGI::Carp::FULL_PATH = 0;

# fancy import routine detects and handles 'errorWrap' specially.
sub import {
    my $pkg = shift;
    my (%routines);
    my (@name);
    if (@name = grep(/^name=/, @_)) {
        my ($n) = (split(/=/, $name[0]))[1];
        set_progname($n);
        @_ = grep(!/^name=/, @_);
    }

    grep($routines{$_}++, @_, @EXPORT);
    $WRAP++ if $routines{'fatalsToBrowser'} || $routines{'wrap'};
    $WARN++ if $routines{'warningsToBrowser'};
    my ($oldlevel) = $Exporter::ExportLevel;
    $Exporter::ExportLevel = 1;
    Exporter::import($pkg, keys %routines);
    $Exporter::ExportLevel = $oldlevel;
    $main::SIG{__DIE__} = \&CGI::Carp::die if $routines{'fatalsToBrowser'};
    $CGI::Carp::NO_TIMESTAMP = 1 if $routines{'noTimestamp'};
}

# These are the originals
sub realwarn {CORE::warn(@_);}
sub realdie {CORE::die(@_);}

sub id {
    my $level = shift;
    my ($pack, $file, $line, $sub) = caller($level);
    my ($dev, $dirs, $id) = File::Spec->splitpath($file);
    return ($file, $line, $id);
}

sub stamp {
    my $frame = 0;
    my ($id, $pack, $file, $dev, $dirs);
    if (defined($CGI::Carp::PROGNAME)) {
        $id = $CGI::Carp::PROGNAME;
    }
    else {
        do {
            $id = $file;
            ($pack, $file) = caller($frame++);
        } until !$file;
    }
    if (!$CGI::Carp::FULL_PATH) {
        ($dev, $dirs, $id) = File::Spec->splitpath($id);
    }
    return "$id: " if $CGI::Carp::NO_TIMESTAMP;
    my $time = scalar(localtime);
    return "[$time] $id: ";
}

sub set_progname {
    $CGI::Carp::PROGNAME = shift;
    return $CGI::Carp::PROGNAME;
}

sub warn {
    my $message = shift;
    my ($file, $line, $id) = id(1);
    $message .= " at $file line $line.\n" unless $message =~ /\n$/;
    _warn($message) if $WARN;
    my $stamp = stamp;
    $message =~ s/^/$stamp/gm;
    realwarn $message;
}

sub _warn {
    my $msg = shift;
    if ($EMIT_WARNINGS) {
        # We need to mangle the message a bit to make it a valid HTML
        # comment.  This is done by substituting similar-looking ISO
        # 8859-1 characters for <, > and -.  This is a hack.
        $msg =~ tr/<>-/\253\273\255/;
        chomp $msg;
        print STDOUT "<!-- warning: $msg -->\n";
    }
    else {
        push @WARNINGS, $msg;
    }
}


# The mod_perl package Apache::Registry loads CGI programs by calling
# eval.  These evals don't count when looking at the stack backtrace.
sub _longmess {
    my $message = Carp::longmess();
    $message =~ s,eval[^\n]+(ModPerl|Apache)/(?:Registry|Dispatch)\w*\.pm.*,,s
        if exists $ENV{MOD_PERL};
    return $message;
}

sub ineval {
    (exists $ENV{MOD_PERL} ? 0 : $^S) || _longmess() =~ /eval [\{\']/m
}

sub die {
    # if no argument is passed, propagate $@ like
    # the real die
    my ($arg, @rest) = @_ ? @_
        : $@ ? "$@\t...propagated"
        : "Died"
    ;

    &$DIE_HANDLER($arg, @rest) if $DIE_HANDLER;

    # the "$arg" is done on purpose!
    # if called as die( $object, 'string' ),
    # all is stringified, just like with
    # the real 'die'
    $arg = join '' => "$arg", @rest if @rest;

    my ($file, $line, $id) = id(1);

    $arg .= " at $file line $line.\n" unless ref $arg or $arg =~ /\n$/;

    realdie $arg if ineval();
    &fatalsToBrowser($arg) if ($WRAP and $CGI::Carp::TO_BROWSER);

    $arg =~ s/^/ stamp() /gme if $arg =~ /\n$/ or not exists $ENV{MOD_PERL};

    $arg .= "\n" unless $arg =~ /\n$/;

    realdie $arg;
}

sub set_message {
    $CGI::Carp::CUSTOM_MSG = shift;
    return $CGI::Carp::CUSTOM_MSG;
}

sub set_die_handler {

    my ($handler) = shift;

    #setting SIG{__DIE__} here is necessary to catch runtime
    #errors which are not called by literally saying "die",
    #such as the line "undef->explode();". however, doing this
    #will interfere with fatalsToBrowser, which also sets 
    #SIG{__DIE__} in the import() function above (or the 
    #import() function above may interfere with this). for
    #this reason, you should choose to either set the die
    #handler here, or use fatalsToBrowser, not both. 
    $main::SIG{__DIE__} = $handler;

    $CGI::Carp::DIE_HANDLER = $handler;

    return $CGI::Carp::DIE_HANDLER;
}

sub confess {CGI::Carp::die Carp::longmess @_;}
sub croak {CGI::Carp::die Carp::shortmess @_;}
sub carp {CGI::Carp::warn Carp::shortmess @_;}
sub cluck {CGI::Carp::warn Carp::longmess @_;}

# We have to be ready to accept a filehandle as a reference
# or a string.
sub carpout {
    my ($in) = @_;
    my ($no) = fileno(to_filehandle($in));
    realdie("Invalid filehandle $in\n") unless defined $no;

    open(SAVEERR, ">&STDERR");
    open(STDERR, ">&$no") or
        (print SAVEERR "Unable to redirect >&$no: $!\n" and exit(1));
}

sub warningsToBrowser {
    $EMIT_WARNINGS = @_ ? shift : 1;
    _warn(shift @WARNINGS) while $EMIT_WARNINGS and @WARNINGS;
}

# headers
sub fatalsToBrowser {
    my $msg = shift;

    $msg = "$msg" if ref $msg;

    $msg =~ s/&/&amp;/g;
    $msg =~ s/>/&gt;/g;
    $msg =~ s/</&lt;/g;
    $msg =~ s/"/&quot;/g;

    my ($wm) = $ENV{SERVER_ADMIN} ?
        qq[the webmaster (<a href="mailto:$ENV{SERVER_ADMIN}">$ENV{SERVER_ADMIN}</a>)] :
        "this site's webmaster";
    my ($outer_message) = <<END;
For help, please send mail to $wm, giving this error message 
and the time and date of the error.
END
    ;
    my $mod_perl = exists $ENV{MOD_PERL};

    if ($CUSTOM_MSG) {
        if (ref($CUSTOM_MSG) eq 'CODE') {
            print STDOUT "Content-type: text/html\n\n"
                unless $mod_perl;
            eval {
                &$CUSTOM_MSG($msg); # nicer to perl 5.003 users
            };
            if ($@) {print STDERR qq(error while executing the error handler: $@);}

            return;
        }
        else {
            $outer_message = $CUSTOM_MSG;
        }
    }

    my $mess = <<END;
<h1>Software error:</h1>
<pre>$msg</pre>
<p>
$outer_message
</p>
END
    ;

    if ($mod_perl) {
        my $r;
        if ($ENV{MOD_PERL_API_VERSION} && $ENV{MOD_PERL_API_VERSION} == 2) {
            $mod_perl = 2;
            require Apache2::RequestRec;
            require Apache2::RequestIO;
            require Apache2::RequestUtil;
            require APR::Pool;
            require ModPerl::Util;
            require Apache2::Response;
            $r = Apache2::RequestUtil->request;
        }
        else {
            $r = Apache->request;
        }
        # If bytes have already been sent, then
        # we print the message out directly.
        # Otherwise we make a custom error
        # handler to produce the doc for us.
        if ($r->bytes_sent) {
            $r->print($mess);
            $mod_perl == 2 ? ModPerl::Util::exit(0) : $r->exit;
        }
        else {
            # MSIE won't display a custom 500 response unless it is >512 bytes!
            if (defined($ENV{HTTP_USER_AGENT}) && $ENV{HTTP_USER_AGENT} =~ /MSIE/) {
                $mess = "<!-- " . (' ' x 513) . " -->\n$mess";
            }
            $r->custom_response(500, $mess);
        }
    }
    else {
        my $bytes_written = eval {tell STDOUT};
        if (defined $bytes_written && $bytes_written > 0) {
            print STDOUT $mess;
        }
        else {
            print STDOUT "Status: 500\n";
            print STDOUT "Content-type: text/html\n\n";
            # MSIE won't display a custom 500 response unless it is >512 bytes!
            if (defined($ENV{HTTP_USER_AGENT}) && $ENV{HTTP_USER_AGENT} =~ /MSIE/) {
                $mess = "<!-- " . (' ' x 513) . " -->\n$mess";
            }
            print STDOUT $mess;
        }
    }

    warningsToBrowser(1); # emit warnings before dying
}

# Cut and paste from CGI.pm so that we don't have the overhead of
# always loading the entire CGI module.
sub to_filehandle {
    my $thingy = shift;
    return undef unless $thingy;
    return $thingy if UNIVERSAL::isa($thingy, 'GLOB');
    return $thingy if UNIVERSAL::isa($thingy, 'FileHandle');
    if (!ref($thingy)) {
        my $caller = 1;
        while (my $package = caller($caller++)) {
            my ($tmp) = $thingy =~ /[\':]/ ? $thingy : "$package\:\:$thingy";
            return $tmp if defined(fileno($tmp));
        }
    }
    return undef;
}

1;
