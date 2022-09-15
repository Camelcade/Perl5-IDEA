use vars qw/
    $scalar
        @array
        %hash
/;

package Foo::Bar;

our $scalar2;
our @array2;
our %hash2;

#@deprecated
our $scalarvalue = 42;
our @arrayvalue = (something => 64);
our %hashvalue = (love => 42);
