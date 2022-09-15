package MooseX::ClassAttribute::Meta::Role::Attribute;

use strict;
use warnings;

our $VERSION = '0.29';

use namespace::autoclean;
use Moose;
use List::Util 1.45 'uniq';

extends 'Moose::Meta::Role::Attribute';

sub new {
    my ($class, $name, %options) = @_;

    $options{traits} = [
        uniq(@{$options{traits} || []}),
        'MooseX::ClassAttribute::Trait::Attribute'
    ];

    return $class->SUPER::new($name, %options);
}

1;

# ABSTRACT: An attribute metaclass for class attributes in roles

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::ClassAttribute::Meta::Role::Attribute - An attribute metaclass for class attributes in roles

=head1 VERSION

version 0.29

=head1 DESCRIPTION

This class overrides L<Moose::Meta::Role::Attribute> to support class
attribute declaration in roles.

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
