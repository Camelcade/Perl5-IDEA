/* eslint-disable */
var entities = require('@jetbrains/youtrack-scripting-api/entities');

exports.rule = entities.Issue.onChange({
  title: "Stub Index Points To A File",
  guard: function (ctx) {
    const issue = ctx.issue;
    return issue.is(ctx.State, ctx.State.Submitted) || issue.is(ctx.State, ctx.State.Open);
  },
  action: function (ctx) {
    const patterns = [
      ['java.lang.Throwable: Stub index points to a file (file =', 'CAMELCADE-833'],
      ['com.intellij.diagnostic.PluginException: Non-idempotent computation: it returns', 'CAMELCADE-2253'],
      ['com.intellij.diagnostic.PluginException: Cannot load class com.perl5.lang.pod.idea.editor.PodSpellCheckingStrategy',
        'CAMELCADE-24430'],
      ['com.intellij.diagnostic.PluginException: Cannot load class com.perl5.lang.perl.idea.editor.PerlSpellCheckingStrategy',
        'CAMELCADE-24430'],
      ['com.intellij.util.io.CorruptedException: Storage corrupted', 'CAMELCADE-5481'],
      ['com.intellij.psi.stubs.UpToDateStubIndexMismatch: PSI and index do not match.', 'CAMELCADE-13188'],
      [function (issue) {
        return issue.description.includes('Read access is allowed from inside read-action only') &&
            issue.description.includes('PerlXNamedValue.computeInlineDebuggerData');
      }, 'CAMELCADE-24513'],
      ['psiElement is not instance of requiredClass', 'CAMELCADE-13188'],
      ['unable to get stub builder for file with file =', 'CAMELCADE-2086'],
      ['VfsData$FileAlreadyCreatedException: fileId', 'CAMELCADE-19432'],
      ['Exception: Outdated stub in index: file:', 'CAMELCADE-22803'],
      ['AssertionError: No ID found for serializer ', 'CAMELCADE-7959'],
      ['Throwable: Stub ids not found for key in index = ', 'CAMELCADE-2978'],
      [function (issue) {
        return issue.description.includes('IOException: new record') &&
            issue.description.includes('has non-empty fields:');
      }, 'CAMELCADE-20492'],
      [function (issue) {
        return issue.description.includes('java.lang.StringIndexOutOfBoundsException') &&
            issue.description.includes('JSEmbeddedContentElementType.parseContents');
      }, 'CAMELCADE-21684'],
      [function (issue) {
        return issue.description.includes('Unable to find control flow scope for') &&
            issue.description.includes('.tt2');
      }, 'CAMELCADE-2683'],
    ];

    let issue = ctx.issue;
    let description = issue.description;

    for (const [pattern, targetIssueId] of patterns) {
      if (typeof pattern === 'function' && pattern(issue) || description.includes(pattern)) {
        let targetIssue = entities.Issue.findById(targetIssueId);
        if (!targetIssue) {
          console.error(`Issue ${targetIssueId} not found`);
          break;
        }
        issue.links.duplicates.add(targetIssue);
        issue.fields.State = ctx.State.Duplicate;
        console.log(`Linking ${issue.id} to ${targetIssue.id}`);
        break;
      }
    }

  },
  requirements: {
    State: {
      name: "State",
      type: entities.State.fieldType,
      Submitted: {name: "Submitted"},
      Open: {name: "Open"},
      Duplicate: {name: "Duplicate"}
    }
  }
});