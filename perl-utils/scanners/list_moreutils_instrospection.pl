use strict;
use warnings FATAL => 'all';
use v5.10;

use List::MoreUtils;

say 'List<String> EXPORT_OK = Arrays.asList(';
say join ",\n", map {qq/"$_"/} sort @List::MoreUtils::EXPORT_OK;
say ');'