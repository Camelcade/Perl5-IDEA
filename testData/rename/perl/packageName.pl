package Foo::Bar::Boo;

our $NewName;

say $NewName;
say ${NewName};
say $Foo::Bar::Boo::NewName;
say ${Foo::Bar::Boo::NewName};
say "some $NewName test";
say "some $Foo::Bar::Boo::NewName test";
say "some ${NewName} test";
say "some ${Foo::Bar::Boo::NewName} test";
say /asdf$NewName asdf/;
say /asdf$Foo::Bar::Boo::NewName asdf/;
say /asdf${NewName}asdf/;
say /asdf${Foo::Bar::Boo::NewName}asdf/;
say <<EOM;
say $NewName;
say $Foo::Bar::Boo::NewName;
say ${NewName};
say ${Foo::Bar::Boo::NewName};
EOM

say Foo::Bar::Boo->some;
