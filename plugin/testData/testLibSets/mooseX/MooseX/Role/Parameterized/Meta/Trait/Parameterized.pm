package MooseX::Role::Parameterized::Meta::Trait::Parameterized;
# ABSTRACT: trait for parameterized roles

our $VERSION = '1.11';

use Moose::Role;
use MooseX::Role::Parameterized::Parameters;
use Moose::Util 'find_meta';
use namespace::autoclean;

has genitor => (
    is       => 'ro',
    does     => 'MooseX::Role::Parameterized::Meta::Trait::Parameterizable',
    required => 1,
);

has parameters => (
    is  => 'rw',
    isa => 'MooseX::Role::Parameterized::Parameters',
);

around reinitialize => sub {
    my $orig = shift;
    my $class = shift;
    my ($pkg) = @_;
    my $meta = blessed($pkg) ? $pkg : find_meta($pkg);

    my $genitor = $meta->genitor;
    my $parameters = $meta->parameters;

    my $new = $class->$orig(
        @_,
        (defined($genitor) ? (genitor => $genitor) : ()),
        (defined($parameters) ? (parameters => $parameters) : ()),
    );
    # in case the role metaclass was reinitialized
    $MooseX::Role::Parameterized::CURRENT_METACLASS = $new;
    return $new;
};

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Role::Parameterized::Meta::Trait::Parameterized - trait for parameterized roles

=head1 VERSION

version 1.11

=head1 DESCRIPTION

This is the trait for parameterized roles; that is, parameterizable roles with
their parameters bound. All this actually provides is a place to store the
L<MooseX::Role::Parameterized::Parameters> object as well as the
L<MooseX::Role::Parameterized::Meta::Role::Parameterizable> object that
generated this role object.

=head1 ATTRIBUTES

=for stopwords genitor metaobject

=head2 genitor

Returns the L<MooseX::Role::Parameterized::Meta::Role::Parameterizable>
metaobject that generated this role.

=head2 parameters

Returns the L<MooseX::Role::Parameterized::Parameters> object that represents
the specific parameter values for this parameterized role.

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Role-Parameterized>
(or L<bug-MooseX-Role-Parameterized@rt.cpan.org|mailto:bug-MooseX-Role-Parameterized@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Shawn M Moore <code@sartak.org>

=head1 COPYRIGHT AND LICENSE

This software is copyright (c) 2008 by Shawn M Moore.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
