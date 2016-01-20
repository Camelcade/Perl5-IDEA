use strict;
use warnings FATAL => 'all';
use v5.10;

my $text = join '', <>;

$text =~ s/\(type == (\w+)\) \{\s+(.+?;)\s+\}/
    say <<"EOM";
if (name.equals("$1"))
    return new PerlElementTypeEx(name)
    {
    \@NotNull
    \@Override
    public PsiElement getPsiElement(\@NotNull ASTNode node)
    {
    $2
    }
    };
EOM
    /gsie;