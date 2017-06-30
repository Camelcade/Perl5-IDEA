# Moose::after
sub after {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::around
sub around {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::augment
sub augment {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::before
sub before {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Scalar::Util::blessed
sub blessed ($) ;

# Carp::confess
sub confess {
    package Carp;
    BEGIN {${^WARNING_BITS} = "\x54\x55\x55\x55\x55\x55\x55\x55\x55\x55\x55\x54\x40\x55\x55\x55\x15"}
    use strict;
    die longmess( @_ );
}

# Moose::extends
sub extends {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::has
sub has {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::inner
sub inner {
    package Moose;
    use warnings;
    use strict;
    my $pkg = caller();
    our (%INNER_BODY, %INNER_ARGS);
    if (my $body = $INNER_BODY{$pkg}) {
        my (@args) = @{$INNER_ARGS{$pkg};};
        local $INNER_ARGS{$pkg};
        local $INNER_BODY{$pkg};
        return &$body( @args );
    }
    else {
        return;
    }
}

# MyOtherMoose::meta
sub meta {
    package Class::MOP::Method::Meta;
    use warnings;
    use strict;
    ;
    '???';
    $metaclass->initialize( blessed( $_[0] ) || $_[0] );
}

# MyOtherMoose::mooose11
sub mooose11 {
    package Eval::Closure::Sandbox_154;
    use warnings;
    use strict;
    if (@_ > 1) {
        if (@_ < 2) {
            die &Module::Runtime::use_module( 'Moose::Exception::AttributeIsRequired' )->new( 'attribute_name', 'mooose11',
                    'class_name', $class_name, 'attribute_init_arg', 'mooose11' );
        }
        $_[0]{'mooose11'} = $_[1];
    }
    return $_[0]{'mooose11'};
}

# MyOtherMoose::moose10
sub moose10 {
    package Eval::Closure::Sandbox_171;
    use warnings;
    use strict;
    if (@_ > 1) {
        unless (do {
            Moose::Util::does_role( $_[1], 'Some::Foo::Role' )
        }) {
            my $msg = do {
                local $_ = $_[1];
                &$type_message( $_[1] )
            };
            die &Module::Runtime::use_module( 'Moose::Exception::ValidationFailedForInlineTypeConstraint' )->new(
                    'type_constraint_message', $msg, 'class_name', $class_name, 'attribute_name', 'moose10', 'value', $_[1] );
        }
        $_[0]{'moose10'} = $_[1];
    }
    return $_[0]{'moose10'};
}

# MyOtherMoose::moose12
sub moose12 {
    package Eval::Closure::Sandbox_176;
    use warnings;
    use strict;
    if (@_ > 1) {
        $_[0]{'moose12'} = $_[1];
    }
    return $_[0]{'moose12'};
}

# MyOtherMoose::moose12_delegate1
sub moose12_delegate1 {
    package Eval::Closure::Sandbox_177;
    use warnings;
    use strict;
    my $self = shift();
    my $proxy = $self->moose12;
    if (not defined $proxy) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotDefined' )->new( 'method',
                $self->meta->find_method_by_name( 'moose12_delegate1' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose12' ));
    }
    elsif (ref $proxy and not &Scalar::Util::blessed( $proxy )) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotAnObject' )->new( 'method',
                $self->meta->find_method_by_name( 'moose12_delegate1' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose12' ), 'given_value', $proxy );
    }
    return $proxy->moose9( @_ );
}

# MyOtherMoose::moose12_delegate2
sub moose12_delegate2 {
    package Eval::Closure::Sandbox_178;
    use warnings;
    use strict;
    my $self = shift();
    my $proxy = $self->moose12;
    if (not defined $proxy) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotDefined' )->new( 'method',
                $self->meta->find_method_by_name( 'moose12_delegate2' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose12' ));
    }
    elsif (ref $proxy and not &Scalar::Util::blessed( $proxy )) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotAnObject' )->new( 'method',
                $self->meta->find_method_by_name( 'moose12_delegate2' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose12' ), 'given_value', $proxy );
    }
    return $proxy->moose8( @_ );
}

# MyOtherMoose::moose13
sub moose13 {
    package Eval::Closure::Sandbox_173;
    use warnings;
    use strict;
    if (@_ > 1) {
        $_[0]{'moose13'} = $_[1];
    }
    return $_[0]{'moose13'};
}

# MyOtherMoose::moose2
sub moose2 {
    package Eval::Closure::Sandbox_153;
    use warnings;
    use strict;
    if (@_ > 1) {
        $_[0]{'moose2'} = $_[1];
    }
    return $_[0]{'moose2'};
}

# MyOtherMoose::moose3
sub moose3 {
    package Eval::Closure::Sandbox_172;
    use warnings;
    use strict;
    if (@_ > 1) {
        die &Module::Runtime::use_module( 'Moose::Exception::CannotAssignValueToReadOnlyAccessor' )->new( 'class_name',
                ref $_[0], 'value', $_[1], 'attribute_name', 'moose3' );
    }
    return $_[0]{'moose3'};
}

# MyOtherMoose::moose4_accessor
sub moose4_accessor {
    package Eval::Closure::Sandbox_160;
    use warnings;
    use strict;
    if (@_ > 1) {
        $_[0]{'moose4'} = $_[1];
    }
    return $_[0]{'moose4'};
}

# MyOtherMoose::moose51
sub moose51 {
    package Eval::Closure::Sandbox_165;
    use warnings;
    use strict;
    if (@_ > 1) {
        $_[0]{'moose51'} = $_[1];
    }
    return $_[0]{'moose51'};
}

# MyOtherMoose::moose51_reader
sub moose51_reader {
    package Eval::Closure::Sandbox_166;
    use warnings;
    use strict;
    if (@_ > 1) {
        die &Module::Runtime::use_module( 'Moose::Exception::CannotAssignValueToReadOnlyAccessor' )->new( 'class_name',
                ref $_[0], 'value', $_[1], 'attribute_name', 'moose51' );
    }
    return $_[0]{'moose51'};
}

# MyOtherMoose::moose5_reader
sub moose5_reader {
    package Eval::Closure::Sandbox_158;
    use warnings;
    use strict;
    if (@_ > 1) {
        die &Module::Runtime::use_module( 'Moose::Exception::CannotAssignValueToReadOnlyAccessor' )->new( 'class_name',
                ref $_[0], 'value', $_[1], 'attribute_name', 'moose5' );
    }
    return $_[0]{'moose5'};
}

# MyOtherMoose::moose6
sub moose6 {
    package Eval::Closure::Sandbox_174;
    use warnings;
    use strict;
    my $self = shift();
    my $proxy = $self->moose13;
    if (not defined $proxy) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotDefined' )->new( 'method',
                $self->meta->find_method_by_name( 'moose6' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose13' ));
    }
    elsif (ref $proxy and not &Scalar::Util::blessed( $proxy )) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotAnObject' )->new( 'method',
                $self->meta->find_method_by_name( 'moose6' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose13' ), 'given_value', $proxy );
    }
    return $proxy->moose6( @_ );
}

# MyOtherMoose::moose6_writer
sub moose6_writer {
    package Eval::Closure::Sandbox_168;
    use warnings;
    use strict;
    $_[0]{'moose6'} = $_[1];
}

# MyOtherMoose::moose7
sub moose7 {
    package Eval::Closure::Sandbox_175;
    use warnings;
    use strict;
    my $self = shift();
    my $proxy = $self->moose13;
    if (not defined $proxy) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotDefined' )->new( 'method',
                $self->meta->find_method_by_name( 'moose7' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose13' ));
    }
    elsif (ref $proxy and not &Scalar::Util::blessed( $proxy )) {
        die &Module::Runtime::use_module( 'Moose::Exception::AttributeValueIsNotAnObject' )->new( 'method',
                $self->meta->find_method_by_name( 'moose7' ), 'instance', $self, 'attribute',
                $self->meta->find_attribute_by_name( 'moose13' ), 'given_value', $proxy );
    }
    return $proxy->moose7( @_ );
}

# MyOtherMoose::moose7_predicate
sub moose7_predicate {
    package Eval::Closure::Sandbox_159;
    use warnings;
    use strict;
    exists $_[0]{'moose7'};
}

# MyOtherMoose::moose8_clearer
sub moose8_clearer {
    package Eval::Closure::Sandbox_167;
    use warnings;
    use strict;
    delete $_[0]{'moose8'};
}

# MyOtherMoose::moose9
sub moose9 {
    package Eval::Closure::Sandbox_163;
    use warnings;
    use strict;
    if (@_ > 1) {
        die &Module::Runtime::use_module( 'Moose::Exception::CannotAssignValueToReadOnlyAccessor' )->new( 'class_name',
                ref $_[0], 'value', $_[1], 'attribute_name', 'moose9' );
    }
    return $_[0]{'moose9'};
}

# MyOtherMoose::moose9_writer
sub moose9_writer {
    package Eval::Closure::Sandbox_164;
    use warnings;
    use strict;
    unless (do {
        $_[1]->isa( 'Some::Foo::Bar' ) if &Scalar::Util::blessed( $_[1] )
    }) {
        my $msg = do {
            local $_ = $_[1];
            &$type_message( $_[1] )
        };
        die &Module::Runtime::use_module( 'Moose::Exception::ValidationFailedForInlineTypeConstraint' )->new(
                'type_constraint_message', $msg, 'class_name', $class_name, 'attribute_name', 'moose9', 'value', $_[1] );
    }
    $_[0]{'moose9'} = $_[1];
}

# Moose::override
sub override {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

# Moose::super
sub super {
    package Moose;
    use warnings;
    use strict;
    if (@_) {
        carp( 'Arguments passed to super() are ignored' );
    }
    return if defined $SUPER_PACKAGE and $SUPER_PACKAGE ne caller();
    return unless $SUPER_BODY;
    &$SUPER_BODY( @SUPER_ARGS );
}

# Moose::with
sub with {
    package Moose::Exporter;
    use warnings;
    use strict;
    my (@curry) = &$extra( @ex_args );
    return &$sub( @curry, @_ );
}

