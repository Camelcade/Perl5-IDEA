/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.project;

import com.intellij.notification.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.psi.PsiManager;
import com.intellij.util.FileContentUtil;
import com.perl5.lang.perl.idea.completion.util.PerlStringCompletionUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.05.2015.
 */
public class Perl5ProjectComponent implements ProjectComponent {
  private Project myProject;
  private VirtualFileListener myChangeListener;
  //	private PsiTreeChangeListener myPsiTreeChangeListener;

  public Perl5ProjectComponent(Project project) {
    myProject = project;
  }


  public void initComponent() {
    // TODO: insert component initialization logic here
/*
                if (myChangeListener == null)
		{
			myChangeListener = new PerlVirtualFileListener(myProject);
			VirtualFileManager.getInstance().addVirtualFileListener(myChangeListener);
		}
*/
  }

  public void disposeComponent() {
    // TODO: insert component disposal logic here
    //		System.out.println("Unregistered listener");
    //		VirtualFileManager.getInstance().removeVirtualFileListener(myChangeListener);

  }

  @NotNull
  public String getComponentName() {
    return "PerlProjectComponent";
  }

  public void projectOpened() {
    PerlStringCompletionUtil.HASH_INDEXES_CACHE.clear();
    PerlStringCompletionUtil.HEREDOC_OPENERS_CACHE.clear();

    PerlApplicationSettings settings = PerlApplicationSettings.getInstance();
    if (settings.shouldShowAnnounce()) {
      settings.setAnnounceShown();
      NotificationGroup group = new NotificationGroup("PERL5_GROUP", NotificationDisplayType.STICKY_BALLOON, true);
      Notification notification = group.createNotification(
        "Perl5 plugin updated to version " + PerlPluginUtil.getPluginVersion(),
        "<p>This is a beta version of perl plugin for 2017.1 EAP products.</p><br/>" +
        "<p>Full list of changes and fixes may be found on the <a href=\"https://plugins.jetbrains.com/plugin/7796\">Plugin's page</a> in JetBrains repository.</p><br/>" +
        "<p>Don't hesitate to report bugs and request new features to <a href=\"https://github.com/hurricup/Perl5-IDEA/issues\">our tracker</a>.</p><br/>" +
        "<p>If you find this plugin helpful, you can support it using <a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HJCUADZKY5G7E\">PayPal</a>.</p><br/>" +
        "<p>Have a nice coding and debugging!</p><br/>",
        NotificationType.INFORMATION,
        new NotificationListener.UrlOpeningListener(false)
      );
      Notifications.Bus.notify(notification);
    }

    StartupManager.getInstance(myProject).runWhenProjectIsInitialized(FileContentUtil::reparseOpenedFiles);
    PsiManager.getInstance(myProject).addPsiTreeChangeListener(new Perl5HeredocInjectionsWatcher(), myProject);

    // called when project is opened
    //		myPsiTreeChangeListener = new ClassAccessorPsiTreeChangeListener();
    //		PsiManager.getInstance(myProject).addPsiTreeChangeListener(myPsiTreeChangeListener);
  }

  public void projectClosed() {
    PerlRemoteFileSystem.getInstance().dropFiles();

    // called when project is being closed
    //		PsiManager.getInstance(myProject).removePsiTreeChangeListener(myPsiTreeChangeListener);
  }
}
