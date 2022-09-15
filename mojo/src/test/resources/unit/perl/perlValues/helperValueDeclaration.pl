$self->helper(
    myhelper => sub {
        my %hash = @_;
        return $hash{$hash{key}};
    }
);