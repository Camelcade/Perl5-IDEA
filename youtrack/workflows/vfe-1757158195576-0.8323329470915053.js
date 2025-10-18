/* eslint-disable */
var entities = require('@jetbrains/youtrack-scripting-api/entities');
var workflow = require('@jetbrains/youtrack-scripting-api/workflow');


exports.rule = entities.Issue.onChange({
                                         title: "PerlStrategy",
                                         guard: function (ctx) {
                                           const logger = new Logger(ctx.traceEnabled);


                                           // --- #1 checkMentionOfAnyWord ---
                                           const issue_0 = ctx.issue;

                                           function findWord_0(text) {
                                             return `com.intellij.diagnostic.PluginException: Cannot load class com.perl5.lang.perl.idea.editor.PerlSpellCheckingStrategy`
                                                 .split(',')
                                                 .map(s => s.trim())
                                                 .filter(s => s)
                                                 .some(word => text.includes(word));
                                           }

                                           function checkDescription_0() {
                                             return findWord_0(issue_0.description || '')
                                           }

                                           const checkMentionOfAnyWordFn_0 = () => {


                                             return (checkDescription_0())
                                           };

                                           // --- #2 checkField ---
                                           const issue_1 = ctx.issue;

                                           function checkFieldStateState_0() {
                                             const issueField = issue_1.fields["State"];

                                             return issue_1.is(ctx.StateState, ctx.StateState.Submitted);
                                           }

                                           const checkFieldFn_0 = () => {


                                             return checkFieldStateState_0()
                                           };


                                           try {
                                             return (
                                                 checkMentionOfAnyWordFn_0() &&
                                                 checkFieldFn_0()
                                             );
                                           }
                                           catch (err) {
                                             if (err?.message?.includes('has no value')) {
                                               logger.error('Failed to execute guard', err);
                                               return false;
                                             }
                                             throw err;
                                           }

                                         },
                                         action: function (ctx) {
                                           const logger = new Logger(ctx.traceEnabled);


                                           // --- #1 linkIssue ---
                                           const issue_2 = ctx.issue_2;

                                           function linkIssueduplicates_0() {
                                             return ctx.issue.links["duplicates"].add(issue_2);
                                           }


                                           const linkIssueFn_0 = () => {


                                             linkIssueduplicates_0();
                                           };

                                           linkIssueFn_0();


                                           // --- #2 changeIssueFieldValue ---
                                           const issue_3 = ctx.issue;
                                           logger.log("Mode: set");
                                           logger.log("Current value:", issue_3.fields['State']);

                                           const changeIssueFieldValueFn_0 = () => {


                                             issue_3.fields['State'] = ctx.StateState.Duplicate;
                                           };

                                           changeIssueFieldValueFn_0();

                                         },
                                         requirements: {
                                           issue_2: {
                                             type: entities.Issue,
                                             id: 'CAMELCADE-24430'
                                           },
                                           StateState: {
                                             name: "State",
                                             type: entities.State.fieldType,
                                             Submitted: {name: "Submitted"},
                                             Duplicate: {name: "Duplicate"}
                                           }
                                         }
                                       });

function Logger(useDebug = true) {
  return {
    log: (...args) => useDebug && console.log(...args),
    warn: (...args) => useDebug && console.warn(...args),
    error: (...args) => useDebug && console.error(...args)
  }
}