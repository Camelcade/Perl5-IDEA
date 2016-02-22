package Foo;

@EXPORT_OK = qw/this is the export_ok/;
@EXPORT = qw/this is the export/;

package Bar;

@EXPORT_OK = qw/visitor is wrong/;
@EXPORT = qw/visitor is wrong/;
