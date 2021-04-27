package Test::Namespace;
use Sub::Identify;

# put the module you are introspecting here:
my $package = shift @ARGV || die "Pass package name as an argument";

my $old_warning_bits = ${^WARNING_BITS};
my $old_strict_bits = $^H;
eval "require $package";
$package->import();
my $warning_changed = ${^WARNING_BITS} eq $old_warning_bits ? 'no' : 'yes';
my $strict_changed = $^H eq $old_strict_bits ? 'no' : 'yes';

print "use $package\n";
print "use strict: $strict_changed\n";
print "use warnings: $warning_changed\n";
print "\nparents:\n";
our @ISA;
print "\t$_\n" for @ISA;
print "\n";

print "Subs:\n";
for my $sub (sort keys %Test::Namespace::) {
    no strict 'refs';
    if (my $coderef = *{__PACKAGE__ . "::$sub"}{CODE}) {
        my $fqn = Sub::Identify::sub_fullname $coderef;
        my ($file, $line) = Sub::Identify::get_code_location $coderef;
        $file //= 'undefined';
        $line //= 'undefined';
        print "\t$sub\t=> $fqn ($file, $line)\n";
    }
}
