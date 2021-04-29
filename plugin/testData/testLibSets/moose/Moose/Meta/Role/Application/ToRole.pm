package Moose::Meta::Role::Application::ToRole;
our $VERSION = '2.2101';

use strict;
use warnings;
use metaclass;

use parent 'Moose::Meta::Role::Application';

use Moose::Util 'throw_exception';

sub apply {
    my ($self, $role1, $role2) = @_;
    $self->SUPER::apply($role1, $role2);
    $role2->add_role($role1);
}

sub check_role_exclusions {
    my ($self, $role1, $role2) = @_;
    if ( $role2->excludes_role($role1->name) ) {
        throw_exception( ConflictDetectedInCheckRoleExclusions => role_name          => $role2->name,
                                                                  excluded_role_name => $role1->name,
                       );
    }
    foreach my $excluded_role_name ($role1->get_excluded_roles_list) {
        if ( $role2->does_role($excluded_role_name) ) {
            throw_exception( RoleDoesTheExcludedRole => role_name          => $role2->name,
                                                        excluded_role_name => $excluded_role_name,
                                                        second_role_name   => $role1->name,
                           );
        }
        $role2->add_excluded_roles($excluded_role_name);
    }
}

sub check_required_methods {
    my ($self, $role1, $role2) = @_;
    foreach my $required_method ($role1->get_required_method_list) {
        my $required_method_name = $required_method->name;

        next if $self->is_aliased_method($required_method_name);

        $role2->add_required_methods($required_method)
            unless $role2->find_method_by_name($required_method_name);
    }
}

sub check_required_attributes {

}

sub apply_attributes {
    my ($self, $role1, $role2) = @_;
    foreach my $attribute_name ($role1->get_attribute_list) {
        # it if it has one already
        if ($role2->has_attribute($attribute_name) &&
            # make sure we haven't seen this one already too
            $role2->get_attribute($attribute_name) != $role1->get_attribute($attribute_name)) {

            my $role2_name = $role2->name;

            throw_exception( AttributeConflictInRoles => role_name        => $role1->name,
                                                         second_role_name => $role2->name,
                                                         attribute_name   => $attribute_name
                           );
        }
        else {
            $role2->add_attribute(
                $role1->get_attribute($attribute_name)->clone
            );
        }
    }
}

sub apply_methods {
    my ( $self, $role1, $role2 ) = @_;
    foreach my $method ( $role1->_get_local_methods ) {

        my $method_name = $method->name;

        next if $method->isa('Class::MOP::Method::Meta');

        unless ( $self->is_method_excluded($method_name) ) {

            my $role2_method = $role2->get_method($method_name);
            if (   $role2_method
                && $role2_method->body != $method->body ) {

                # method conflicts between roles used to result in the method
                # becoming a requirement but now are permitted just like
                # for classes, hence no code in this branch anymore.
            }
            else {
                $role2->add_method(
                    $method_name,
                    $method,
                );
            }
        }

        next unless $self->is_method_aliased($method_name);

        my $aliased_method_name = $self->get_method_aliases->{$method_name};

        my $role2_method = $role2->get_method($aliased_method_name);

        if (   $role2_method
            && $role2_method->body != $method->body ) {

            throw_exception( CannotCreateMethodAliasLocalMethodIsPresent => aliased_method_name     => $aliased_method_name,
                                                                            method                  => $method,
                                                                            role_name               => $role2->name,
                                                                            role_being_applied_name => $role1->name,
                           );
        }

        $role2->add_method(
            $aliased_method_name,
            $role1->get_method($method_name)
        );

        if ( !$role2->has_method($method_name) ) {
            $role2->add_required_methods($method_name)
                unless $self->is_method_excluded($method_name);
        }
    }
}

sub apply_override_method_modifiers {
    my ($self, $role1, $role2) = @_;
    foreach my $method_name ($role1->get_method_modifier_list('override')) {
        # it if it has one already then ...
        if ($role2->has_method($method_name)) {
            # if it is being composed into another role
            # we have a conflict here, because you cannot
            # combine an overridden method with a locally
            # defined one
            throw_exception( OverrideConflictInComposition => role_name               => $role2->name,
                                                              role_being_applied_name => $role1->name,
                                                              method_name             => $method_name
                           );
        }
        else {
            # if we are a role, we need to make sure
            # we don't have a conflict with the role
            # we are composing into
            if ($role2->has_override_method_modifier($method_name) &&
                $role1->get_override_method_modifier($method_name) != $role2->get_override_method_modifier($method_name)) {

                throw_exception( OverrideConflictInComposition => role_name               => $role2->name,
                                                                  role_being_applied_name => $role1->name,
                                                                  method_name             => $method_name,
                                                                  two_overrides_found     => 1
                               );
            }
            else {
                # if there is no conflict,
                # just add it to the role
                $role2->add_override_method_modifier(
                    $method_name,
                    $role1->get_override_method_modifier($method_name)
                );
            }
        }
    }
}

sub apply_method_modifiers {
    my ($self, $modifier_type, $role1, $role2) = @_;
    my $add = "add_${modifier_type}_method_modifier";
    my $get = "get_${modifier_type}_method_modifiers";
    foreach my $method_name ($role1->get_method_modifier_list($modifier_type)) {
        $role2->$add(
            $method_name,
            $_
        ) foreach $role1->$get($method_name);
    }
}

1;

# ABSTRACT: Compose a role into another role

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
