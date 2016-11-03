package Foo::Bar::Moo;

our $NewName;

say $NewName;
say ${NewName};
say $Foo::Bar::Moo::NewName;
say ${Foo::Bar::Moo::NewName};
say "some $NewName test";
say "some $Foo::Bar::Moo::NewName test";
say "some ${NewName} test";
say "some ${Foo::Bar::Moo::NewName} test";
say /asdf$NewName asdf/;
say /asdf$Foo::Bar::Moo::NewName asdf/;
say /asdf${NewName}asdf/;
say /asdf${Foo::Bar::Moo::NewName}asdf/;
say <<EOM;
say $NewName;
say $Foo::Bar::Moo::NewName;
say ${NewName};
say ${Foo::Bar::Moo::NewName};
EOM

say Foo::Bar::Boo->some;
