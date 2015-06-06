package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdk implements Sdk
{

	@NonNls
	private String libPath;
	PerlSdkTypeId perlSdkTypeId = new PerlSdkTypeId();

	public PerlSdk(String libPath)
	{
		this.libPath = libPath;
		if (SystemInfo.isWindows)
		{
			this.libPath += "bin/perl.exe";
		} else // suppose its *nix
		{
			this.libPath += "bin/perl";
		}
	}

	@NotNull
	@Override
	public PerlSdkTypeId getSdkType()
	{
		return perlSdkTypeId;
	}

	@NotNull
	@Override
	public String getName()
	{
		return perlSdkTypeId.getName();
	}

	@Nullable
	@Override
	public String getVersionString()
	{
		return "123";
	}

	@Nullable
	@Override
	public String getHomePath()
	{
		return libPath;
	}

	@Nullable
	@Override
	public VirtualFile getHomeDirectory()
	{
		return VirtualFileManager.getInstance().refreshAndFindFileByUrl(libPath);
	}

	@NotNull
	@Override
	public RootProvider getRootProvider()
	{
		return new PerlRootProvider(this);
	}

	@NotNull
	@Override
	public SdkModificator getSdkModificator()
	{
		return new PerlSdkModificator();
	}

	@Nullable
	@Override
	public SdkAdditionalData getSdkAdditionalData()
	{
		return null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	@Nullable
	@Override
	public <T> T getUserData(@NotNull Key<T> key)
	{
		return null;
	}

	@Override
	public <T> void putUserData(@NotNull Key<T> key, @Nullable T t)
	{

	}
}