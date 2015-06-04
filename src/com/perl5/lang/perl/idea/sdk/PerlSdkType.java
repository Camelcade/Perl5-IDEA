package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.*;
import com.perl5.PerlIcons;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkType extends SdkType {

    private String binaryFile = "";

    public PerlSdkType() {
        super("Perl5");
    }

    @NotNull
    @Override
    public String getName() {
        return "Perl5";
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {

    }

    @Override
    public String getPresentableName() {
        return "Perl5 SDK";
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        if (OSUtil.isLinux() || OSUtil.isLinux()) {
            return "/usr/";
        }
        return System.getenv("PERL_HOME");
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return "Perl 5 SDK (" + sdkHome + ")";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
        return null;
    }

    @Override
    public boolean isValidSdkHome(String sdkHome) {
        if (!(sdkHome.endsWith("/") && sdkHome.endsWith("\\"))) {
            sdkHome += File.separator;
        }
        if (OSUtil.isKnown()) {
            try {
                File f = new File(executablePath(sdkHome));
                this.binaryFile = f.getCanonicalPath();
                return f.exists();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String executablePath(String sdkHome) {
        if (OSUtil.isLinux() || OSUtil.isMacOS()) {
            return sdkHome + "bin/perl";
        } else if (OSUtil.isWindows()) {
            return sdkHome + "bin/perl.exe";
        }
        throw new RuntimeException("Unknown OS");
    }

    @Override
    public Icon getIcon() {
        return PerlIcons.PERL_LANGUAGE;
    }

    @Nullable
    @Override
    public String getVersionString(@NotNull Sdk sdk) {
        return "5";//todo: get from perl exact version
    }


}
