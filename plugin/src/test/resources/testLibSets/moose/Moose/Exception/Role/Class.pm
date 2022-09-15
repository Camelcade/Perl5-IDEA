package Moose::Exception::Role::Class;
our $VERSION = '2.2101';

use Moose::Role;

has 'class_name' => (
    is            => 'ro',
    isa           => 'Str',
    required      => 1,
    documentation => "This attribute can be used for fetching metaclass instance:\n".
                     "    my \$metaclass_instance = Moose::Util::find_meta( \$exception->class_name );\n",
);

1;
