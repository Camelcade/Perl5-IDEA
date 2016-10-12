use strict;
use v5.10;

my $unary = 'write|values|umask|ucfirst|uc|telldir|tell|study|stat|srand|sqrt|sleep|sin|shift|setservent|setprotoent|setnetent|sethostent|rmdir|rewinddir|reset|ref|readpipe|readlink|readline|readdir|rand|quotemeta|prototype|pop|ord|oct|lstat|log|localtime|length|lcfirst|lc|keys|int|hex|gmtime|getsockname|getpwuid|getpwnam|getprotobyname|getpgrp|getpeername|getnetbyname|gethostbyname|getgrnam|getgrgid|getc|fileno|fc|exp|exit|exists|evalbytes|eof|each|defined|cos|closedir|close|chroot|chr|chdir|caller|alarm|abs';
my $acceptors = 'write|truncate|telldir|tell|syswrite|sysseek|sysread|sysopen|stat|select|seekdir|seek|rewinddir|readdir|read|opendir|open|lstat|ioctl|getc|flock|fileno|fcntl|eof|closedir|close|chdir|binmode';

my @unary = split /\|/, $unary;
my @acceptors = split /\|/, $acceptors;

my %unary;
@unary{@unary} = @unary;
my %acceptors;
@acceptors{@acceptors} = @acceptors;

my %unary_acceptors;

for my $acceptor (@acceptors)
{
	if(exists $unary{$acceptor}){
		$unary_acceptors{$acceptor} = $acceptor;	
		delete $unary{$acceptor};
		delete $acceptors{$acceptor};
	}
}

say "NAMED_UNARY_OPERATORS = ".join '|', map "\"$_\"", reverse sort keys %unary;
say "BARE_HANDLE_ACCEPTORS = ".join '|', map "\"$_\"", reverse sort keys %acceptors;
say "NAMED_UNARY_BARE_HANDLE_ACCEPTORS = ".join '|', map "\"$_\"", reverse sort keys %unary_acceptors;
