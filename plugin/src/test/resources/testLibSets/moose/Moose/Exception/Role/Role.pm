package Moose::Exception::Role::Role;
our $VERSION = '2.2101';

# use Moose::Util 'throw_exception';
use Moose::Role;

has 'role_name' => (
    is            => 'ro',
    isa           => 'Str',
    required      => 1,
    documentation => "This attribute can be used for fetching the class's metaclass instance:\n".
                     "    my \$metaclass_instance = Moose::Util::find_meta( \$exception->role_name );\n",

);

1;
