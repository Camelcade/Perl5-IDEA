package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlIcons;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkType extends SdkType
{

	private String perlExecutablePath = "";

	public PerlSdkType()
	{
		super("Perl5 SDK");
	}

	@NotNull
	public static PerlSdkType getInstance() {
		PerlSdkType instance = SdkType.findInstance(PerlSdkType.class);
		assert instance != null : "Make sure PerlSdkType is registered in plugin.xml";
		return instance;
	}

	@Override
	public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element)
	{

	}


	@Override
	public void setupSdkPaths(@NotNull Sdk sdk)
	{
		SdkModificator sdkModificator = sdk.getSdkModificator();

		List<String> perlLibPaths = readFromProgram("perl -le \"print $_ for @INC\"");

		for( String perlLibPath: perlLibPaths)
		{
			if( !".".equals(perlLibPath))
			{
				File libDir = new File(perlLibPath);

				if( libDir.exists() && libDir.isDirectory())
				{
					VirtualFile virtualDir = LocalFileSystem.getInstance().findFileByIoFile(libDir);
					if (virtualDir != null)
					{
						sdkModificator.addRoot(virtualDir, OrderRootType.SOURCES);
						sdkModificator.addRoot(virtualDir, OrderRootType.CLASSES);
					}
				}
			}
		}

		sdkModificator.commitChanges();
	}

	@Nullable
	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}


	@Override
	public String getPresentableName()
	{
		return "Perl5 SDK";
	}

	@Nullable
	@Override
	public String suggestHomePath()
	{
		String perlPath = askPerlForPath();
		if( perlPath != null )
			return perlPath;

		if (SystemInfo.isLinux || SystemInfo.isUnix || SystemInfo.isFreeBSD )
		{
			return "/usr/";
		}
		return System.getenv("PERL_HOME");
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return "Perl " + getPerlVersionString();
	}


	@Override
	public boolean isValidSdkHome(String sdkHome)
	{
		if (!(sdkHome.endsWith("/") && sdkHome.endsWith("\\")))
			sdkHome += File.separator;

		try
		{
			File f = new File(executablePath(sdkHome));
			this.perlExecutablePath = f.getCanonicalPath();
			return f.exists();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private String executablePath(String sdkHome)
	{
		if (SystemInfo.isWindows)
			return sdkHome + "bin/perl.exe";
		else
			return sdkHome + "bin/perl";
	}

	@Override
	public Icon getIcon()
	{
		return PerlIcons.PERL_LANGUAGE;
	}

	@Nullable
	@Override
	public String getVersionString(@NotNull Sdk sdk)
	{
		return getPerlVersionString();
	}


	Pattern perlVersionStringPattern = Pattern.compile("\\(([^)]+?)\\) built for (.+)");

	public String getPerlVersionString()
	{
		List<String> versionLines = readFromProgram(perlExecutablePath + " -v");

		if (versionLines.size() > 0)
		{
			Matcher m = perlVersionStringPattern.matcher(versionLines.get(0));

			if( m.find() )
				return m.group(1) + " (" + m.group(2) + ")";

			return "Unknown version";
		}

		throw new RuntimeException("Error getting perl version text");
	}


	public String askPerlForPath()
	{
		List<String> perlPathLines = readFromProgram("perl -le \"print $^X\"");

		if( perlPathLines.size() == 1)
		{
			int binIndex = perlPathLines.get(0).lastIndexOf("bin");
			if( binIndex > 0 )
				return perlPathLines.get(0).substring(0, binIndex);

		}
		return null;
	}

	public List<String> readFromProgram(String command)
	{
		try
		{
			List<String> result = new ArrayList<>();
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = in.readLine()) != null)
				if( !line.isEmpty() )
					result.add(line);

			in.close();
			return result;
		} catch (Exception e)
		{
			return null;
		}
	}


}
