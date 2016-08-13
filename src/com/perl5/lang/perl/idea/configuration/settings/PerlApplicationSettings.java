/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by hurricup on 16.04.2016.
 */
@State(
		name = "Perl5ApplicationSettings",
		storages = @Storage(id = "other", file = PerlPathMacros.PERL5_APP_SETTINGS_FILE)
)
public class PerlApplicationSettings implements PersistentStateComponent<PerlApplicationSettings>
{
	public String pluginVersion = "";
	public boolean popupShown = false;

	private static final String ourDefaultAnnotationsPath = PathManager.getConfigPath() + "/perl5/application.annotations";

	private String myAnnotationsPath = ourDefaultAnnotationsPath;

	@Transient
	private transient NullableLazyValue<VirtualFile> myAnnotationsLazyRoot;

	public static PerlApplicationSettings getInstance()
	{
		return ServiceManager.getService(PerlApplicationSettings.class);
	}

	@Nullable
	@Override
	public PerlApplicationSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(PerlApplicationSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}

	private boolean isVersionChanged()
	{
		return !StringUtil.equals(pluginVersion, PerlPluginUtil.getPluginVersion());
	}

	private void updateVersion()
	{
		String newVersion = PerlPluginUtil.getPluginVersion();
		if (newVersion != null)
		{
			pluginVersion = newVersion;
			popupShown = false;
		}
	}

	public void setAnnounceShown()
	{
		updateVersion();
		popupShown = true;
	}

	public boolean shouldShowAnnounce()
	{
		return isVersionChanged() || !popupShown;
	}

	@NotNull
	public String getAnnotationsPath()
	{
		return myAnnotationsPath;
	}

	public void setAnnotationsPath(@NotNull String annotationsPath)
	{
		myAnnotationsPath = annotationsPath;
		synchronized (this)
		{
			myAnnotationsLazyRoot = null;
		}
	}

	public void setDefaultAnnotationsPath()
	{
		setAnnotationsPath(ourDefaultAnnotationsPath);
	}

	@Nullable
	public synchronized VirtualFile getAnnotationsRoot()
	{
		if (myAnnotationsLazyRoot == null)
		{
			myAnnotationsLazyRoot = new NullableLazyValue<VirtualFile>()
			{
				@Nullable
				@Override
				protected VirtualFile compute()
				{
					File file = new File(myAnnotationsPath);
					if (file.exists())
					{
						if (file.isDirectory())
						{
							return VfsUtil.findFileByIoFile(file, true);
						}
					}
					else
					{
						if (file.mkdirs())
						{
							return VfsUtil.findFileByIoFile(file, true);
						}
					}
					return null;
				}
			};
		}
		return myAnnotationsLazyRoot.getValue();
	}
}
