package Foo::Bar;
has 'unnamed';
has constant_value => 'bingo';
has [ 'first_name', 'second_name' ] => 'multinamed';
has constant_sub => sub {
    'sub_result'
};
has [ 'first_sub', 'second_sub' ] => sub {
    'multi_sub_result'
};
