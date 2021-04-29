package Moose::Meta::Role::Application::ToClass;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use List::Util 'first';
use Moose::Util 'throw_exception';
use Scalar::Util 'weaken';

use parent 'Moose::Meta::Role::Application';

__PACKAGE__->meta->add_attribute('role' => (
    reader => 'role',
    Class::MOP::_definition_context(),
));

__PACKAGE__->meta->add_attribute('class' => (
    accessor => 'class',
    Class::MOP::_definition_context(),
));

sub apply {
    my ($self, $role, $class) = @_;

    # We need weak_ref in CMOP :(
    weaken($self->{role}  = $role);
    weaken($self->{class} = $class);

    $self->SUPER::apply($role, $class);

    $class->add_role($role);
    $class->add_role_application($self);
}

sub check_role_exclusions {
    my ($self, $role, $class) = @_;
    if ($class->excludes_role($role->name)) {
        throw_exception( ConflictDetectedInCheckRoleExclusionsInToClass => class_name => $class->name,
                                                                           role_name  => $role->name,
                       );
    }
    foreach my $excluded_role_name ($role->get_excluded_roles_list) {
        if ($class->does_role($excluded_role_name)) {
            throw_exception( ClassDoesTheExcludedRole => role_name          => $role->name,
                                                         excluded_role_name => $excluded_role_name,
                                                         class_name         => $class->name,
                           );
        }
    }
}

sub check_required_methods {
    my ($self, $role, $class) = @_;

    my @missing;
    my @is_attr;

    # NOTE:
    # we might need to move this down below the
    # the attributes so that we can require any
    # attribute accessors. However I am thinking
    # that maybe those are somehow exempt from
    # the require methods stuff.
    foreach my $required_method ($role->get_required_method_list) {
        my $required_method_name = $required_method->name;

        if (!$class->find_method_by_name($required_method_name)) {

            next if $self->is_aliased_method($required_method_name);

            push @missing, $required_method;
        }
    }

    return unless @missing;

    my $error = '';

    @missing = sort { $a->name cmp $b->name } @missing;
    my @conflicts = grep { $_->isa('Moose::Meta::Role::Method::Conflicting') } @missing;

    if (@conflicts) {
        my $conflict = $conflicts[0];
        my $roles = $conflict->roles_as_english_list;

        my @same_role_conflicts = grep { $_->roles_as_english_list eq $roles } @conflicts;

        throw_exception( MethodNameConflictInRoles => conflict   => \@same_role_conflicts,
                                                      class_name => $class->name
                       );
    }
    elsif (@missing) {
        if (my $meth = first { $class->name->can($_) } @missing) {
            throw_exception( RequiredMethodsImportedByClass => class_name      => $class->name,
                                                               role_name       => $role->name,
                                                               missing_methods => \@missing,
                                                               imported_method => $meth
                           );
        }
        else {
            throw_exception( RequiredMethodsNotImplementedByClass => class_name      => $class->name,
                                                                     role_name       => $role->name,
                                                                     missing_methods => \@missing,
                           );
        }
    }
}

sub check_required_attributes {

}

sub apply_attributes {
    my ($self, $role, $class) = @_;

    foreach my $attribute_name ($role->get_attribute_list) {
        # it if it has one already
        if ($class->has_attribute($attribute_name) &&
            # make sure we haven't seen this one already too
            $class->get_attribute($attribute_name) != $role->get_attribute($attribute_name)) {
            next;
        }
        else {
            $class->add_attribute(
                $role->get_attribute($attribute_name)->attribute_for_class
            );
        }
    }
}

sub apply_methods {
    my ( $self, $role, $class ) = @_;

    foreach my $method ( $role->_get_local_methods ) {
        my $method_name = $method->name;

        next if $method->isa('Class::MOP::Method::Meta');

        unless ( $self->is_method_excluded($method_name) ) {

            my $class_method = $class->get_method($method_name);

            next if $class_method && $class_method->body != $method->body;

            $class->add_method(
                $method_name,
                $method,
            );
        }

        next unless $self->is_method_aliased($method_name);

        my $aliased_method_name = $self->get_method_aliases->{$method_name};

        my $class_method = $class->get_method($aliased_method_name);

        if ( $class_method && $class_method->body != $method->body ) {
            throw_exception( CannotCreateMethodAliasLocalMethodIsPresentInClass => aliased_method_name => $aliased_method_name,
                                                                                   method              => $method,
                                                                                   role_name           => $role->name,
                                                                                   class_name          => $class->name,
                           );
        }

        $class->add_method(
            $aliased_method_name,
            $method,
        );
    }

    # we must reset the cache here since
    # we are just aliasing methods, otherwise
    # the modifiers go wonky.
    $class->reset_package_cache_flag;
}

sub apply_override_method_modifiers {
    my ($self, $role, $class) = @_;
    foreach my $method_name ($role->get_method_modifier_list('override')) {
        # it if it has one already then ...
        if ($class->has_method($method_name)) {
            next;
        }
        else {
            # if this is not a role, then we need to
            # find the original package of the method
            # so that we can tell the class were to
            # find the right super() method
            my $method = $role->get_override_method_modifier($method_name);
            my ($package) = Class::MOP::get_code_info($method);
            # if it is a class, we just add it
            $class->add_override_method_modifier($method_name, $method, $package);
        }
    }
}

sub apply_method_modifiers {
    my ($self, $modifier_type, $role, $class) = @_;
    my $add = "add_${modifier_type}_method_modifier";
    my $get = "get_${modifier_type}_method_modifiers";
    foreach my $method_name ($role->get_method_modifier_list($modifier_type)) {
        $class->$add(
            $method_name,
            $_
        ) foreach $role->$get($method_name);
    }
}

1;

# ABSTRACT: Compose a role into a class

__END__

=pod

=head1 DESCRIPTION

=head2 METHODS

=over 4

=item B<new>

=item B<meta>

=item B<apply>

=item B<check_role_exclusions>

=item B<check_required_methods>

=item B<check_required_attributes>

=item B<apply_attributes>

=item B<apply_methods>

=item B<apply_method_modifiers>

=item B<apply_override_method_modifiers>

=back

=head1 BUGS

See L<Moose/BUGS> for details on reporting bugs.

=cut
