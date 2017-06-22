#!/usr/bin/perl
package ClassAccessor;
use strict;
use warnings FATAL => 'all';

use base qw(Class::Accessor);
ClassAccessor->mk_accessors(qw(lib_simple_rw1 lib_simple_other_rw ));
ClassAccessor->mk_ro_accessors(qw(lib_simple_ro1 lib_simple_other_ro ));
ClassAccessor->mk_wo_accessors(qw(lib_simple_wo1 lib_simple_other_wo));
__PACKAGE__->mk_accessors(qw( lib_simple_third_rw));
__PACKAGE__->mk_ro_accessors(qw( lib_simple_third_ro));
__PACKAGE__->mk_wo_accessors(qw( lib_simple_third_wo));
ClassAccessor->follow_best_practice;
ClassAccessor->mk_accessors(qw(lib_fbp_rw1 ))->mk_accessors(qw(lib_fbp_rw2 ));
ClassAccessor->mk_ro_accessors(qw(lib_fbp_ro1 ))->mk_ro_accessors(qw(lib_fbp_ro2 ));
ClassAccessor->mk_wo_accessors(qw(lib_fbp_wo1 ))->mk_wo_accessors(qw(lib_fbp_wo2 ));
__PACKAGE__->mk_accessors(qw(lib_fbp_rw3));
__PACKAGE__->mk_ro_accessors(qw(lib_fbp_ro3));
__PACKAGE__->mk_wo_accessors(qw(lib_fbp_wo3));
ClassAccessor->follow_best_practice();
ClassAccessor->mk_ro_accessors();
ClassAccessor->mk_wo_accessors();
ClassAccessor->mk_accessors();
