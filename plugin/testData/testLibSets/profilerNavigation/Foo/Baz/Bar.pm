package Foo::Bar;
use constant something => 42;

sub something {
    say 'hi';
}

use Try::Tiny;

try {
    "Something";
};

try {
    "otherthing"; # need to be long to shorten
    "otherthing";
    "otherthing";
    "otherthing";
    "otherthing";
    "otherthing";
    "otherthing";
};

