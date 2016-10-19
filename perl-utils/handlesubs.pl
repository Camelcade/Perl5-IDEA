use strict;
use v5.10;

# builtins http://perldoc.perl.org/perlfunc.html
# original list taken from http://www.perlmonks.org/?node_id=1131277
my @allknown = qw(chomp chop chr crypt fc hex index lc lcfirst length oct ord pack reverse rindex sprintf substr uc ucfirst fc pos quotemeta split study abs atan2 cos exp hex int log oct rand sin sqrt srand each keys pop push shift splice unshift values join reverse unpack delete each exists keys values binmode close closedir dbmclose dbmopen die eof fileno flock format getc print printf read readdir readline rewinddir say seek seekdir select syscall sysread sysseek syswrite tell telldir truncate warn write pack read syscall sysread sysseek syswrite unpack vec chdir chmod chown chroot fcntl glob ioctl link lstat mkdir open opendir readlink rename rmdir stat symlink sysopen umask unlink utime break caller continue die dump evalbytes exit goto last next redo return wantarray caller import defined formline lock prototype reset scalar alarm exec fork getpgrp getppid getpriority kill pipe readpipe setpgrp setpriority sleep system times wait waitpid bless dbmclose dbmopen package ref tie tied untie accept bind connect getpeername getsockname getsockopt listen recv send setsockopt shutdown socket socketpair msgctl msgget msgrcv msgsnd semctl semget semop shmctl shmget shmread shmwrite endgrent endhostent endnetent endpwent getgrent getgrgid getgrnam getlogin getpwent getpwnam getpwuid setgrent setpwent endprotoent endservent gethostbyaddr gethostbyname gethostent getnetbyaddr getnetbyname getnetent getprotobyname getprotobynumber getprotoent getservbyname getservbyport getservent sethostent setnetent setprotoent setservent gmtime localtime time times);
my @unary = qw(write values umask ucfirst uc telldir tell study stat srand sqrt sleep sin shift setservent setprotoent setnetent sethostent rmdir rewinddir reset ref readpipe readlink readline readdir rand quotemeta prototype pop ord oct lstat log localtime length lcfirst lc keys int hex gmtime getsockname getpwuid getpwnam getprotobyname getpgrp getpeername getnetbyname gethostbyname getgrnam getgrgid getc fileno fc exp exit exists evalbytes eof each defined cos closedir close chroot chr chdir caller alarm abs);
my @acceptors = qw(write truncate telldir tell syswrite sysseek sysread sysopen stat select seekdir seek rewinddir readdir read opendir open lstat ioctl getc flock fileno fcntl eof closedir close chdir binmode';'write truncate telldir tell syswrite sysseek sysread sysopen stat select seekdir seek rewinddir readdir read opendir open lstat ioctl getc flock fileno fcntl eof closedir close chdir binmode);
my @keywords = qw(my our local state elsif else given default continue format sub package use no require undef switch case print printf say grep map sort do eval goto redo next last return y tr qr qw qq qx q m s);
my @extkeywords = qw(method func try catch finally);
my @moose = qw(inner with extends meta override around super augment after before has);
my @operators = qw(x not and or xor lt gt le ge eq ne cmp);
my @argumentless = qw(wantarray wait times time setpwent setgrent getservent getpwent getprotoent getppid getnetent getlogin gethostent getgrent fork endservent endpwent endprotoent endnetent endhostent endgrent dump break);

my %unary;
@unary{@unary} = @unary;
my %acceptors;
@acceptors{@acceptors} = @acceptors;
my %allknown;
@allknown{@allknown} = @allknown;
my %argumentless;
@argumentless{@argumentless} = @argumentless;

delete @allknown{@unary,@acceptors,@operators,@argumentless,@moose,@extkeywords,@keywords};
delete @argumentless{@keywords,@moose,@extkeywords,@operators};

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
say "LIST_OPERATORS = ".join '|',  map "\"$_\"", reverse sort keys %allknown;
say "NAMED_ARGUMENTLESS = ".join '|',  map "\"$_\"", reverse sort keys %argumentless;
