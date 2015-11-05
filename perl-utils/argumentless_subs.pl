while(<>)
{
    print "\"$_\",\n" if
        s/\s+\(\)\s*//
    ;
}