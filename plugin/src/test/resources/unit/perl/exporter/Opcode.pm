package Opcode 1.64;

use strict;

use Carp;
use Exporter 'import';
use XSLoader;

sub opset (;@);
sub opset_to_hex ($);
sub opdump (;$);
use subs our @EXPORT_OK = qw(
        opset opset_to_hex opdump
);
