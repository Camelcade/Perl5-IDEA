sub something {
    my $scalar = 1;
    if ($scalar > 2) {
        print "false";
    }
    elsif ($scalar < 2) {
        print 'true';
    }
    DB::enable_profile();
    print 'done';
    DB::disable_profile();
}

BEGIN{
    print 'begin block';
}

print 42;
something();

END{
    print 'this is the end';
}
