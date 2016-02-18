use v5.10;
if (my $abc = 'abc' && 0)
{
    say $abc;
}
elsif (my $cde = 'cde' && 0)
{
    say $cde;
}
else
{
    say $c<caret>de;
}
