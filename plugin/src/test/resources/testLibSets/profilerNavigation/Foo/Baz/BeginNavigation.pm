package Foo::Baz::BeginNavigation;

use strict;
use warnings FATAL => 'all';

say 'hi';

BEGIN{
    say 'some';
}


BEGIN{
    use Foo::Bar;
}
