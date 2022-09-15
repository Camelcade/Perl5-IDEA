package MooseX::ClassAttribute::Trait::Application;

use strict;
use warnings;

our $VERSION = '0.29';

use namespace::autoclean;
use Moose::Role;

after apply_attributes => sub {
    shift->_apply_class_attributes(@_);
};

1;

# ABSTRACT: A trait that supports role application for roles with class attributes

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Trait::Application - A trait that supports role application for roles with class attributes

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This trait is used to allow the application of roles containing class
attributes.

=head1 BUGS

See L<MooseX::ClassAttribute> for details.

Bugs may be submitted through L<the RT bug tracker|http://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-ClassAttribute>
(or L<bug-moosex-classattribute@rt.cpan.org|mailto:bug-moosex-classattribute@rt.cpan.org>).

I am also usually active on IRC as 'drolsky' on C<irc://irc.perl.org>.

=head1 AUTHOR

Dave Rolsky <autarch@urth.org>

=head1 COPYRIGHT AND LICENCE

This software is Copyright (c) 2016 by Dave Rolsky.

This is free software, licensed under:

  The Artistic License 2.0 (GPL Compatible)

=cut
