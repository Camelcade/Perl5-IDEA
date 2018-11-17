package MyTest::Exceptions;

use Exception::Class
    'Library::Exception',
    LibraryException => {},
    qw/Library::Exception2 LibraryException3/;

1;