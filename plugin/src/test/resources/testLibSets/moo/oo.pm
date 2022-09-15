package oo;
use strict;
use warnings;

use Moo::_Utils qw(_load_module);

sub moo {
  print <<'EOMOO';
 ______
< Moo! >
 ------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
EOMOO
  exit 0;
}

my $package;
sub import {
  moo() if $0 eq '-';
  $package = $_[1] || 'Class';
  if ($package =~ s/^\+//) {
    _load_module($package);
  }
  my $line = (caller)[2] || 1;
  require Filter::Util::Call;
  my $done;
  Filter::Util::Call::filter_add(sub {
    if (!$done) {
      s{\A}{package $package;\nuse Moo;\n#line $line\n};
      $done = 1;
    }
    return Filter::Util::Call::filter_read();
  });
}

1;
__END__

=head1 NAME

oo - syntactic sugar for Moo oneliners

=head1 SYNOPSIS

  perl -Moo=Foo -e 'has bar => ( is => q[ro], default => q[baz] ); print Foo->new->bar'

  # loads an existing class and re-"opens" the package definition
  perl -Moo=+My::Class -e 'print __PACKAGE__->new->bar'

=head1 DESCRIPTION

oo.pm is a simple source filter that adds C<package $name; use Moo;> to the
beginning of your script, intended for use on the command line via the -M
option.

=head1 SUPPORT

See L<Moo> for support and contact information.

=head1 AUTHORS

See L<Moo> for authors.

=head1 COPYRIGHT AND LICENSE

See L<Moo> for the copyright and license.

=cut
