package Devel::Cover::Report::Camelcade;
use strict;
use warnings FATAL => 'all';
use JSON;

sub report {
    my $self = shift;
    my $db = shift;
    my $options = shift;

    my $report = $db->cover;
    my $result = [];
    for my $file_name ($report->items) {
        next unless ($file_name);
        my $file_result = {
            name  => $file_name // "",
            lines => {}

        };
        push @$result, $file_result;
        my $file_data = $report->file($file_name);
        for my $criterion_name ($file_data->items) {
            my $criterion = $file_data->criterion($criterion_name);
            if ($criterion_name eq 'statement') {
                for my $location_id ($criterion->items) {
                    my $location_data = $criterion->location($location_id);
                    my $location_result = $file_result->{lines}{$location_id} //= {};
                    foreach my $element (@$location_data) {
                        $location_result->{data}++;
                        $location_result->{cover} += $element->covered // 0;
                        $location_result->{uncoverable} += $element->uncoverable // 0;
                    }
                }
            }
            elsif ($criterion_name eq 'time') {
                for my $location_id ($criterion->items) {
                    my $location_data = $criterion->location($location_id);
                    my $location_result = $file_result->{lines}{$location_id} //= {};
                    foreach my $element (@$location_data) {
                        $location_result->{time} += $element->covered // 0;
                    }
                }
            }
            #            else {
            #                print "  $criterion_name\n";
            #                for my $location_id ($criterion->items) {
            #                    my $location_data = $criterion->location($location_id);
            #                    print "    $location_id\n";
            #                    print "        $_\n" for @$location_data;
            #                }
            #            }
        }

    }
    print JSON->new()->pretty(0)->encode($result);
}

1;