sub something {
    my $scalar = 1;
    my @array = (1, 2, 3);
    my %hash = (4, 5, 6);
    my $string = "test string";
    my $scalar_ref = \$string;
    my $array_ref = \@array;
    my $hash_ref = \%hash;
    print 'done';
}

BEGIN{
    print 'begin block';
}

print 42;
something();
something();
something();
