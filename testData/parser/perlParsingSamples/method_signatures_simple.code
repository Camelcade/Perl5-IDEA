#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

# -- a basic class -- #
package User;
use Method::Signatures::Simple;

method new ($class: $name, $email) {
    my $user = {
        id    => new_id( 42 ),
        name  => $name,
        email => $email,
    };
    bless $user, $class;
}

func new_id ($seed) {
    state $id = $seed;
    $id++;
}

method name  { $self->{name};  }
method email { $self->{email}; }
1;


# -- other features -- #
# attributes
method foo : lvalue { $self->{foo} }

# change invocant name
method foo ($bar) { $this->bar( $bar ) }
method bar ($class: $bar) { $class->baz( $bar ) }
