sub indirect_extractor {
    my %hash = (@_);
    return $hash{$hash{superkey}};
}

sub extractor {
    my %hash = @_;
    return main::->indirect_extractor(69, @_, superkey => $hash{$hash{key}});
}
