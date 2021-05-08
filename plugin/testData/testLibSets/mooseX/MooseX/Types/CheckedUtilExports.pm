package MooseX::Types::CheckedUtilExports;
# ABSTRACT: Wrap L<Moose::Util::TypeConstraints> to be safer for L<MooseX::Types>

our $VERSION = '0.50';

use strict;
use warnings;
use Moose::Util::TypeConstraints ();
use Moose::Exporter;
use Carp 'carp';
use Sub::Install;
use namespace::autoclean;

my $StringFoundMsg =
    q{WARNING: String found where Type expected (did you use a => instead of a , ?)};

my @exports = qw/type subtype maybe_type duck_type enum coerce from as/;

#pod =head1 DESCRIPTION
#pod
#pod Prevents errors like:
#pod
#pod     subtype Foo =>
#pod     ...
#pod
#pod Which should be written as:
#pod
#pod     subtype Foo,
#pod     ...
#pod
#pod When using L<MooseX::Types>. Exported by that module.
#pod
#pod Exports checked versions of the following subs:
#pod
#pod C<type> C<subtype> C<maybe_type> C<duck_type> C<enum> C<coerce> C<from> C<as>
#pod
#pod While C<class_type> and C<role_type> will also register the type in the library.
#pod
#pod From L<Moose::Util::TypeConstraints>. See that module for syntax.
#pod
#pod =for Pod::Coverage class_type role_type
#pod
#pod =cut

for my $export (@exports) {
    no strict 'refs';

    Sub::Install::install_sub({
        into => __PACKAGE__,
        as   => $export,
        code => sub {
            my $caller = shift;

            local $Carp::CarpLevel = $Carp::CarpLevel + 1;

            carp $StringFoundMsg
                unless ref($_[0]) ||
                    $_[0] =~ /\b::\b/ || # qualified type
                    $caller->get_registered_class_type($_[0]) ||
                    $caller->get_registered_role_type($_[0]);

            goto &{"Moose::Util::TypeConstraints::$export"};
        }
    });
}

Moose::Exporter->setup_import_methods(
    with_caller => [ @exports, 'class_type', 'role_type' ]
);

sub class_type {
    my $caller = shift;

    $caller->register_class_type(
        Moose::Util::TypeConstraints::class_type(@_)
    );
}

sub role_type($;$) {
    my ($caller, $name, $opts) = @_;

    $caller->register_role_type(
        Moose::Util::TypeConstraints::role_type($name, $opts)
    );
}

#pod =head1 SEE ALSO
#pod
#pod L<MooseX::Types>
#pod
#pod =cut

1;

__END__

=pod

=encoding UTF-8

=head1 NAME

MooseX::Types::CheckedUtilExports - Wrap L<Moose::Util::TypeConstraints> to be safer for L<MooseX::Types>

=head1 VERSION

version 0.50

=head1 DESCRIPTION

Prevents errors like:

    subtype Foo =>
    ...

Which should be written as:

    subtype Foo,
    ...

When using L<MooseX::Types>. Exported by that module.

Exports checked versions of the following subs:

C<type> C<subtype> C<maybe_type> C<duck_type> C<enum> C<coerce> C<from> C<as>

While C<class_type> and C<role_type> will also register the type in the library.

From L<Moose::Util::TypeConstraints>. See that module for syntax.

=for Pod::Coverage class_type role_type

=head1 SEE ALSO

L<MooseX::Types>

=head1 SUPPORT

Bugs may be submitted through L<the RT bug tracker|https://rt.cpan.org/Public/Dist/Display.html?Name=MooseX-Types>
(or L<bug-MooseX-Types@rt.cpan.org|mailto:bug-MooseX-Types@rt.cpan.org>).

There is also a mailing list available for users of this distribution, at
L<http://lists.perl.org/list/moose.html>.

There is also an irc channel available for users of this distribution, at
L<C<#moose> on C<irc.perl.org>|irc://irc.perl.org/#moose>.

=head1 AUTHOR

Robert "phaylon" Sedlacek <rs@474.at>

=head1 COPYRIGHT AND LICENCE

This software is copyright (c) 2007 by Robert "phaylon" Sedlacek.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut
