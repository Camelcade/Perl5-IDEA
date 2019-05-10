package Foo::Bar;

Foo::Bar::->mk_ro_accessors('ro_one');
Foo::Bar::->mk_wo_accessors('wo_one');
Foo::Bar::->mk_accessors('rw_one');
Foo::Bar::->follow_best_practice;
Foo::Bar::->mk_ro_accessors('ro_one1');
Foo::Bar::->mk_wo_accessors('wo_one1');
Foo::Bar::->mk_accessors('rw_one1');
