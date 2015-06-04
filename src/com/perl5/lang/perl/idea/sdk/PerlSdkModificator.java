package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkModificator implements SdkModificator {
    @Override
    public String getName() {
            return "Perl5Modificator";
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public String getHomePath() {
        return null;
    }

    @Override
    public void setHomePath(String s) {

    }

    @Nullable
    @Override
    public String getVersionString() {
        return "5";
    }

    @Override
    public void setVersionString(String s) {

    }

    @Override
    public SdkAdditionalData getSdkAdditionalData() {
        return null;
    }

    @Override
    public void setSdkAdditionalData(SdkAdditionalData sdkAdditionalData) {

    }

    @Override
    public VirtualFile[] getRoots(OrderRootType orderRootType) {
        return new VirtualFile[0];
    }

    @Override
    public void addRoot(VirtualFile virtualFile, OrderRootType orderRootType) {

    }

    @Override
    public void removeRoot(VirtualFile virtualFile, OrderRootType orderRootType) {

    }

    @Override
    public void removeRoots(OrderRootType orderRootType) {

    }

    @Override
    public void removeAllRoots() {

    }

    @Override
    public void commitChanges() {

    }

    @Override
    public boolean isWritable() {
        return false;
    }
}
