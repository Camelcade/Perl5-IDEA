package Foo::Bar;

our $NewName;

say $NewName;
say ${NewName};
say $Foo::Bar::NewName;
say ${Foo::Bar::NewName};
say "some $NewName test";
say "some $Foo::Bar::NewName test";
say "some ${NewName} test";
say "some ${Foo::Bar::NewName} test";
say /asdf$NewName asdf/;
say /asdf$Foo::Bar::NewName asdf/;
say /asdf${NewName}asdf/;
say /asdf${Foo::Bar::NewName}asdf/;
say <<EOM
say $NewName;
say $Foo::Bar::NewName;
say ${NewName};
say ${Foo::Bar::NewName};
EOM

