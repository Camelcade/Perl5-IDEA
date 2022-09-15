use warnings FATAL => 'all';
use strict;
use Test::More;

is(<<EOM, <<EOM, 'Bootstrap');
teamcity[enteredTheMatrix timestamp='2019-03-10T12:56:49.973']
teamcity[testingStarted timestamp='2019-03-10T12:56:49.973']
teamcity[testSuiteStarted locationHint='myfile://testData/tests/is_failed_named.t' name='testData/tests/is_failed_named.t' timestamp='2019-03-10T12:56:49.973']
teamcity[testStarted name='with name' timestamp='2019-03-10T12:56:49.973']
teamcity[testFailed actual='Actual |' |n text' details='|n#   Failed test |'with name|'|n#   at testData/tests/is_failed_named.t line 5.|n#          got: |'Actual |' |n#  text|'|n#     expected: |'Expe|'cted|n# text|'' expected='Expe|'cted|ntext' message='' name='with name' timestamp='2019-03-10T12:56:49.973' type='comparisonFailure']
teamcity[testFinished duration='42' name='with name' timestamp='2019-03-10T12:56:49.973']
teamcity[message status='WARNING' text='# Looks like you failed 1 test of 1.' timestamp='2019-03-10T12:56:49.973']
teamcity[testSuiteFinished locationHint='testData/tests/is_failed_named.t' name='testData/tests/is_failed_named.t' timestamp='2019-03-10T12:56:49.973']

Test Summary Report
-------------------
testData/tests/is_failed_named.t (Wstat: 256 Tests: 1 Failed: 1)
  Failed test:  1
  Non-zero exit status: 1
Files=1, Tests=1, TEST_MODE_STATS;
Result: FAIL
teamcity[testingFinished timestamp='2019-03-10T12:56:49.973']
EOM
teamcity[enteredTheMatrix timestamp='2019-03-10T12:56:49.973']
teamcity[testingStarted timestamp='2019-03-10T12:56:49.973']
teamcity[testSuiteStarted locationHint='myfile://testData/tests/is_failed_named.t' name='testData/tests/is_failed_named.t' timestamp='2019-03-10T12:56:49.973']
teamcity[testStarted name='with name' timestamp='2019-03-10T12:56:49.973']
teamcity[testFailed actual='Actual |' |n text' details='|n#   Failed test |'with name|'|n#   at testData/tests/is_failed_named.t line 5.|n#          got: |'Actual |' |n#  text|'|n#     expected: |'Expe|'cted|n# text|'' expected='Expe|'cted|ntext' message='' name='with name' timestamp='2019-03-10T12:56:49.973' type='comparisonFailure']
teamcity[testFinished duration='42' name='with name' timestamp='2019-03-10T12:56:49.973']
teamcity[message status='WARNING' text='# Looks like you failed 1 test of 1.' timestamp='2019-03-10T12:56:49.973']
teamcity[testSuiteFinished locationHint='testData/tests/is_failed_named.t' name='testData/tests/is_failed_named.t' timestamp='2019-03-10T12:56:49.973']

Test Summary Report
-------------------
testData/tests/is_failed_named.t (Wstat: 256 Tests: 1 Failed: 1)
  Failed test:  1
  Non-zero exit status: 1
Files=1, Tests=1, TEST_MODE_STATS;
Result: FAIL
teamcity[testingFinished timestamp='2019-03-10T12:56:49.973']
EOM


done_testing;