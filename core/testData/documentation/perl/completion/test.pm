package test;

=head1 This is a documentation for test package

=head2 somesub()

This is a sub doc

=cut

#@method
sub somesub {

}

=method somemethod()()

This is a method doc

=cut

method somemethod {

}

=func somefunc()

This is a func doc

=cut

#@method
sub somefunc {

}

=attr someattr()

This is a attr doc

=cut

#@method
has someattr => (is => 'rw');