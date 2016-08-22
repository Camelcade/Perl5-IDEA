#!/usr/bin/perl

my $ns = { };
my $result = {
    ns => $ns
};

{
    my $package_name = $ARGV[0] || exit;

    my @old_ns = keys %::;
    my $features_ref;
    my $my_package_name = 'MyScanningPackage';
    eval "{package $my_package_name;use $package_name;\$features_ref=get_features();}";
    my @new_ns = keys %::;
    my %hash;
    @hash{@new_ns} = @new_ns;
    delete @hash{@old_ns};
    @new_ns = grep {/::$/} sort keys %hash;

    require mro;

    foreach my $new_space (@new_ns)
    {
        process_namespace($new_space)
    }

    # checking features
    my @features = grep {$features_ref->{$_}} keys %$features_ref;
    if( @features )
    {
        require feature;
        my %features = reverse %feature::feature;
        @features = sort grep $_, map {$features{$_}} @features;
        $ns->{$package_name}->{features} = [@features] if @features;
    }

    # introspecting parents
    my $isa = get_parents($my_package_name);
    if( $isa )
    {
        $ns->{$package_name}->{sets_parent} = $isa;
    }

    # check $^WARNINGS_BITS, check $^H, check %^H,
    # we could use Moose introspection for Moose parents

    require JSON::XS;
    print JSON::XS->new()->pretty( 1 )->encode( $result );

}

sub get_features
{
    return (caller(0))[10];
}
sub process_namespace
{
    my ($namespace) = @_;

    $namespace =~ s/::$//;

    my $data = $ns->{$namespace} = { };

    if (my $mro = mro::get_mro($namespace))
    {
        $data->{mro} = $mro if $mro ne 'dfs';
    }

    my $isa = get_parents($namespace);
    $data->{isa} = $isa if $isa;

    my $export = get_package_array($namespace, 'EXPORT', 1);
    $data->{export} = $export if $export;

    my $export_ok = get_package_array($namespace, 'EXPORT_OK', 1);
    $data->{export_ok} = $export_ok if $export_ok;

    {
        no strict 'refs';

        foreach my $subspace (grep { /::$/ } sort keys %{*{$namespace.'::'}{HASH}})
        {
            process_namespace($namespace.'::'.$subspace)
        }
    }
}

sub get_parents
{
    my ($namespace) = @_;
    my $isa = get_package_array($namespace, 'ISA', 0);
    return $isa if $isa;

    # checking MOP introspection
    my $meta_ref = *{$namespace.'::meta'}{CODE};
    if( $meta_ref )
    {
        if( my $meta_obj = $meta_ref->($namespace))
        {
            my @isa = ();
            if( UNIVERSAL::can($meta_obj, 'superclasses'))
            {
                push @isa, $meta_obj->superclasses();
            }
            if (UNIVERSAL::can($meta_obj, 'roles'))
            {
                push @isa, map {(split /\|/, $_->name)} @{$meta_obj->roles()};
            }
            return @isa ? \@isa : undef;
        }
    }
    return undef;
}

sub get_package_array
{
    my ($package, $array_name, $sort) = @_;
    no strict 'refs';
    my $array_ref = *{$package."::$array_name"}{ARRAY};
    if ($array_ref && ref $array_ref eq 'ARRAY' && @$array_ref)
    {
        @$array_ref = sort @$array_ref if $sort;
        return $array_ref;
    }
    return;
}