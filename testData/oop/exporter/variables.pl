package Foo;
our @EXPORT = qw/
    $MYVAR
    @MYARR
    %MYHASH
    &MYCODE
    SIMPLECODE
    /;

our @EXPORT_OK = ('$myvar', '@myarr', '%myhash', '&mycode', 'simplecode');