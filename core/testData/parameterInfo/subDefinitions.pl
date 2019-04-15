package Foo::Bar;

sub list_and_shifts {
    my ($var, undef, $othervar) = (@_, shift, shift);
}

sub list {
    my ($var, undef, $othervar) = @_;
}

sub separate_shifts {
    my $var = shift;
    shift;
    my $othervar = shift @_;
}

sub test_unpack_list {
    my ($var, undef, $othervar) = ($_[0], $_[1], 123);
}

sub single_shift {
    my $var = shift @$a;
}

sub shifts_and_list {
    my $var = shift;
    shift;
    my $othervar = @_;
}

sub shift_and_list_separate {
    my $var = shift;
    my (undef, $othervar) = @_;
}

sub shift_and_list {
    my ($var, undef, $othervar) = (shift, @_);
}

sub shifts {
    my ($var, undef, $othervar) = (shift, shift, shift);
}
