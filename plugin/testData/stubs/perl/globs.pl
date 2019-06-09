package Foo::Bar;

*someglob = sub {};
*main::someglob = sub {};
*otherglob{HASH} = ();
*thirdglob = *blaglob = *lastglob;