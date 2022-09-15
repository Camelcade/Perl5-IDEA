package Class::MOP::MiniTrait;
our $VERSION = '2.2101';

use strict;
use warnings;

use Module::Runtime 'use_package_optimistically';

sub apply {
    my ( $to_class, $trait ) = @_;

    for ( grep { !ref } $to_class, $trait ) {
        use_package_optimistically($_);
        $_ = Class::MOP::Class->initialize($_);
    }

    for my $meth ( grep { $_->package_name ne 'UNIVERSAL' } $trait->get_all_methods ) {
        my $meth_name = $meth->name;
        next if index($meth_name, '__') == 0;   # skip private subs

        if ( $to_class->find_method_by_name($meth_name) ) {
            $to_class->add_around_method_modifier( $meth_name, $meth->body );
        }
        else {
            $to_class->add_method( $meth_name, $meth->clone );
        }
    }
}

# We can't load this with use, since it may be loaded and used from Class::MOP
# (via Class::MOP::Class, etc). However, if for some reason this module is loaded
# _without_ first loading Class::MOP we need to require Class::MOP so we can
# use it and Class::MOP::Class.
require Class::MOP;

1;

# ABSTRACT: Extremely limited trait application

__END__

=pod

=head1 DESCRIPTION

This package provides a single function, C<apply>, which does a half-assed job
of applying a trait to a class. It exists solely for use inside Class::MOP and
L<Moose> core classes.

=cut
