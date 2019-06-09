<fold text='= POD block ='>=pod

    This is a doc

=cut</fold>


<fold text='# comments...'># something
# is here
# this
# test</fold>

<fold text='{code block}'>{
    say 'Something';
}</fold>
$a = <fold text='{hash}'>{
    test => 42,
    val => 69
}</fold>;
$a = <fold text='[array]'>[
    1,
    2,
    3,
    4,
]</fold>;
$a = <fold text='{strings list}'>qw/
    this
    is
    a
    test
    /</fold>;
$a = <fold text='(list)'>(1,
    2,
    3,
    4)</fold>;
use constant <fold text='{constants definitions}'>{
    TEST1 => 42,
    TEST2 => 69,
    TEST3 => 88,
}</fold>;
sub abc
<fold text='{code block}'>{
    my ($self) = @_;
    say "something";
}</fold>
$a = <<'TEXT';
<fold text='<< heredoc >>'>Single quoted
of thext
here</fold>
TEXT
$a = <<"TEXT";
<fold text='<< heredoc >>'>Single quoted of thext
here</fold>
TEXT
$a = <<`TEXT`;
<fold text='<< heredoc >>'>Single quoted
of thext
here</fold>
TEXT
<fold text='imports...'>use UNIVERSAL;
use UNIVERSAL;
use UNIVERSAL;
use UNIVERSAL;
use UNIVERSAL;
use UNIVERSAL;
use UNIVERSAL;</fold>
