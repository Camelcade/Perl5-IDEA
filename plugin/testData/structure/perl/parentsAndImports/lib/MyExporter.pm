package MyExporter;

our @EXPORT = qw/
    $exported_scalar
    @exported_array
    %exported_hash
    *exported_glob
    exported_declaration
    exported_definition
    exported_method
    exported_func
/;

our ($exported_scalar, @exported_array, %exported_hash);
sub exported_declaration;
sub exported_definition {}
method exported_method() {}
func exported_func() {}
*exported_glob = sub {}