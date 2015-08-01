package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlRootProvider implements RootProvider
{
	private final PerlSdk perlSdk;

	public PerlRootProvider(PerlSdk perlSdk)
	{
		this.perlSdk = perlSdk;
	}

	@NotNull
	@Override
	public String[] getUrls(OrderRootType orderRootType)
	{
		return new String[0];
	}

	@NotNull
	@Override
	public VirtualFile[] getFiles(@NotNull OrderRootType orderRootType)
	{
		String baseFolder = perlSdk.getHomePath();
		VirtualFileManager.getInstance().refreshAndFindFileByUrl(baseFolder);
		return new VirtualFile[0];
	}

	@Override
	public void addRootSetChangedListener(@NotNull RootSetChangedListener rootSetChangedListener)
	{

	}

	@Override
	public void addRootSetChangedListener(@NotNull RootSetChangedListener rootSetChangedListener, @NotNull Disposable disposable)
	{

	}

	@Override
	public void removeRootSetChangedListener(@NotNull RootSetChangedListener rootSetChangedListener)
	{

	}
}
