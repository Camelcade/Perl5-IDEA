#!/usr/bin/perl

my $ns = { };
my $result = {
    ns => $ns
};

{
    my $package_name = $ARGV[0] || exit;

    my @old_ns = keys %::;
    eval "use $package_name;";
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

    # check $^WARNINGS_BITS, check $^H, check %^H,
    # we could use Moose introspection for Moose parents

    require JSON::XS;
    print JSON::XS->new()->pretty( 1 )->encode( $result );

}

sub process_namespace
{
    my ($namespace) = @_;

    $namespace =~ s/::$//;

    my $data = $ns->{$namespace} = { };

    if (my $mro = mro::get_mro($namespace))
    {
        $data->{mro} = $mro;
    }

    my $isa = get_package_array($namespace, 'ISA', 0);
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