package Foo::Bar;

our $var;

say $var;
say ${var};
say $Foo::Bar::var;
say ${Foo::Bar::var};
say "some $var test";
say "some $Foo::Bar::var test";
say "some ${var} test";
say "some ${Foo::Bar::var} test";
say /asdf$var asdf/;
say /asdf$Foo::Bar::var asdf/;
say /asdf${var}asdf/;
say /asdf${Foo::Bar::var}asdf/;
say <<EOM
say $var;
say $Foo::Bar::var;
say ${var};
say ${Foo::Bar::v<caret>ar};
EOM

