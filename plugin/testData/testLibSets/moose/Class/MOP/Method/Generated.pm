package Class::MOP::Method::Generated;
our $VERSION = '2.2101';

use strict;
use warnings;

use Eval::Closure;

use parent 'Class::MOP::Method';

## accessors

sub new {
    $_[0]->_throw_exception( CannotCallAnAbstractBaseMethod => package_name => __PACKAGE__ );
}

sub _initialize_body {
    $_[0]->_throw_exception( NoBodyToInitializeInAnAbstractBaseClass => package_name => __PACKAGE__ );
}

sub _generate_description {
    my ( $self, $context ) = @_;
    $context ||= $self->definition_context;

    my $desc = "generated method";
    my $origin = "unknown origin";

    if (defined $context) {
        if (defined $context->{description}) {
            $desc = $context->{description};
        }

        if (defined $context->{file} || defined $context->{line}) {
            $origin = "defined at "
                    . (defined $context->{file}
                        ? $context->{file} : "<unknown file>")
                    . " line "
                    . (defined $context->{line}
                        ? $context->{line} : "<unknown line>");
        }
    }

    return "$desc ($origin)";
}

sub _compile_code {
    my ( $self, @args ) = @_;
    unshift @args, 'source' if @args % 2;
    my %args = @args;

    my $context = delete $args{context};
    my $environment = $self->can('_eval_environment')
        ? $self->_eval_environment
        : {};

    return eval_closure(
        environment => $environment,
        description => $self->_generate_description($context),
        %args,
    );
}

1;

# ABSTRACT: Abstract base class for generated methods

__END__

=pod

=head1 DESCRIPTION

This is a C<Class::MOP::Method> subclass which is subclassed by
C<Class::MOP::Method::Accessor> and
C<Class::MOP::Method::Constructor>.

It is not intended to be used directly.

=cut
