package rename;

import com.intellij.testFramework.fixtures.CodeInsightTestUtil;
import com.perl5.lang.perl.idea.refactoring.rename.PerlMemberInplaceRenameHandler;
import org.jetbrains.annotations.NotNull;

public class PerlInlineRenameTest extends PerlRenameTest {
  @Override
  protected void doRenameAtCaret(@NotNull String newName) {
    CodeInsightTestUtil.doInlineRename(new PerlMemberInplaceRenameHandler(), newName, myFixture);
  }
}
