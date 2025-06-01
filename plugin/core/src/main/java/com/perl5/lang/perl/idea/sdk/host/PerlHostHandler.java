/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsActions.ActionText;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.perl5.lang.perl.idea.sdk.AbstractPerlHandler;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.PerlHandlerCollector;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Know how to work with different types of hosts
 */
public abstract class PerlHostHandler<Data extends PerlHostData<Data, Handler>, Handler extends PerlHostHandler<Data, Handler>>
  extends AbstractPerlHandler<Data, Handler> {
  private static final String TAG_NAME = "host";

  private static final PerlHandlerCollector<PerlHostHandler<?, ?>> EP = new PerlHandlerCollector<>("com.perl5.hostHandler");

  public PerlHostHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  /**
   * Chooses a path conforming with {@code pathPredicate} on user-selected host and passes selected path and host data
   * to the {@code selectionConsumer}
   *
   * @param defaultPathFunction function computing a default path from {@code hostData}
   * @param useDefaultIfExists  true if default path should be used silently if exists
   * @param nameValidator       restricts visible file names
   * @param pathValidator       validates a path selected by user and returns error message or null if everything is fine
   * @param selectionConsumer   a callback for selected result. Accepts path selected and the host data
   * @param disposable          session-bound things may be attached to this disposable, which is going to be disposed by parent configurable
   */
  public void chooseFileInteractively(@NotNull String dialogTitle,
                                      @Nullable Function<? super PerlHostData<?, ?>, ? extends File> defaultPathFunction,
                                      boolean useDefaultIfExists,
                                      @NotNull Predicate<? super String> nameValidator,
                                      @NotNull Function<? super String, String> pathValidator,
                                      @NotNull BiConsumer<? super String, ? super PerlHostData<?, ?>> selectionConsumer,
                                      @NotNull Disposable disposable) {
    Data hostData = createDataInteractively();
    if (hostData == null) {
      return;
    }
    VirtualFileSystem fileSystem = hostData.getFileSystem(disposable);
    File defaultPath = defaultPathFunction == null ? null : defaultPathFunction.apply(hostData);
    Consumer<String> resultConsumer = it -> selectionConsumer.accept(it, hostData);
    if (fileSystem != null) {
      chooseFileInteractively(
        dialogTitle, defaultPath, useDefaultIfExists, nameValidator, pathValidator, resultConsumer, hostData, fileSystem);
    }
    else {
      chooseFileInteractively(
        dialogTitle, defaultPath, useDefaultIfExists, nameValidator, pathValidator, resultConsumer, hostData);
    }
  }

  /**
   * Choose a file if host does not provide a file system
   */
  protected void chooseFileInteractively(@NotNull String dialogTitle,
                                         @Nullable File defaultPath,
                                         boolean useDefaultIfExists,
                                         @NotNull Predicate<? super String> nameValidator,
                                         @NotNull Function<? super String, String> pathValidator,
                                         @NotNull Consumer<? super String> selectionConsumer,
                                         @NotNull Data hostData) {
    Ref<String> pathRef = Ref.create();
    ApplicationManager.getApplication().invokeAndWait(
      () -> pathRef.set(Messages.showInputDialog(
        (Project)null, null, dialogTitle, null,
        defaultPath == null ? null : FileUtil.toSystemIndependentName(defaultPath.getPath()),
        new InputValidator() {
          @Override
          public boolean checkInput(String inputString) {
            if (StringUtil.isEmpty(inputString)) {
              return false;
            }
            return nameValidator.test(new File(inputString).getName());
          }

          @Override
          public boolean canClose(String inputString) {
            return StringUtil.isNotEmpty(inputString) && pathValidator.apply(inputString) == null;
          }
        })));
    String chosenPath = pathRef.get();
    if (StringUtil.isNotEmpty(chosenPath)) {
      selectionConsumer.accept(chosenPath);
    }
  }

  /**
   * Choose a file if host provides a file system
   */
  protected void chooseFileInteractively(@NotNull String dialogTitle,
                                         @Nullable File defaultPath,
                                         boolean useDefaultIfExists,
                                         @NotNull Predicate<? super String> nameValidator,
                                         @NotNull Function<? super String, String> pathValidator,
                                         @NotNull Consumer<? super String> selectionConsumer,
                                         @NotNull Data hostData,
                                         @NotNull VirtualFileSystem fileSystem) {
    VirtualFile defaultFile = defaultPath == null ? null : fileSystem.findFileByPath(defaultPath.getPath());

    if (useDefaultIfExists && defaultFile != null && defaultFile.exists() && !defaultFile.isDirectory()) {
      selectionConsumer.accept(defaultFile.getPath());
      return;
    }

    final var descriptor = createDescriptor(dialogTitle, nameValidator, pathValidator);
    customizeFileChooser(descriptor, fileSystem);
    Ref<String> pathRef = Ref.create();
    ApplicationManager.getApplication().invokeAndWait(() -> FileChooser.chooseFiles(descriptor, null, defaultFile, chosen -> {
      String selectedPath = chosen.getFirst().getPath();
      if (StringUtil.isEmpty(pathValidator.apply(selectedPath))) {
        pathRef.set(selectedPath);
      }
    }));
    if (!pathRef.isNull()) {
      selectionConsumer.accept(pathRef.get());
    }
  }

  private @NotNull FileChooserDescriptor createDescriptor(@NotNull String dialogTitle,
                                                          @NotNull Predicate<? super String> nameValidator,
                                                          @NotNull Function<? super String, String> pathValidator) {
    final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, isChooseFolders(), false, false, false, false) {
      @Override
      public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        return super.isFileVisible(file, showHiddenFiles) && (file.isDirectory() || nameValidator.test(file.getName()));
      }

      @Override
      public void validateSelectedFiles(VirtualFile[] files) throws Exception {
        if (files.length != 0) {
          String errorMessage = pathValidator.apply(files[0].getPath());
          if (StringUtil.isNotEmpty(errorMessage)) {
            throw new Exception(errorMessage);
          }
        }
      }
    };
    descriptor.setTitle(dialogTitle);
    return descriptor;
  }

  /**
   * @return true iff we should allow to choose folders - only case is local mac chooser
   */
  protected boolean isChooseFolders() {return false;}

  /**
   * host handler may customize a chooser descriptor if necessary, e.g. add fs roots
   */
  protected void customizeFileChooser(@NotNull FileChooserDescriptor descriptor, @NotNull VirtualFileSystem fileSystem) {
  }

  /**
   * Creates a necessary host data with possible interactions with user
   *
   * @return host data or null if user cancelled the process
   */
  protected abstract @Nullable Data createDataInteractively();

  /**
   * @return a menu item title for this handler, used in UI. E.g. new interpreter menu item
   */
  public abstract @NotNull @ActionText String getMenuItemTitle();

  /**
   * @return short lowercased name, for interpreters list
   */
  public abstract @NotNull String getShortName();

  /**
   * @return true iff this handler can be used in the application. E.g. has proper os, proper plugins, etc.
   */
  public abstract boolean isApplicable();

  /**
   * @return an OS handler for this host if it's available without data. E.g for local, wsl, docker cases
   */
  public abstract @Nullable PerlOsHandler getOsHandler();

  /**
   * @return true  iff it is a localhost handler. Despite docker and wsl are local as well, they considered as remote.
   */
  public abstract  boolean isLocal();

  @Override
  protected final @NotNull String getTagName() {
    return TAG_NAME;
  }

  /**
   * @return configurable for the {@code project} settings related to this host data type if any. E.g. docker has additional cli arguments on project level
   */
  public @Nullable UnnamedConfigurable getSettingsConfigurable(@NotNull Project project) {
    return null;
  }

  public abstract @Nullable Icon getIcon();

  public static @NotNull List<? extends PerlHostHandler<?, ?>> all() {
    return EP.getExtensionsList();
  }

  public static void forEach(@NotNull Consumer<? super PerlHostHandler<?, ?>> action) {
    all().forEach(action);
  }

  public static @NotNull Stream<? extends PerlHostHandler<?, ?>> stream() {
    return all().stream();
  }

  @Contract("null->null")
  public static @Nullable PerlHostHandler<?, ?> from(@Nullable Sdk sdk) {
    PerlHostData<?, ?> perlHostData = PerlHostData.from(sdk);
    return perlHostData == null ? null : perlHostData.getHandler();
  }

  /**
   * Attempts to load {@link PerlHostData} from the {@code parentElement}
   *
   * @return data read or null if EP not found
   */
  public static @Nullable PerlHostData<?, ?> load(@NotNull Element parentElement) {
    Element element = parentElement.getChild(TAG_NAME);
    if (element == null) {
      return null;
    }
    PerlHostHandler<?, ?> handler = EP.findSingle(element.getAttributeValue(ID_ATTRIBUTE));
    return handler != null ? handler.loadData(element) : null;
  }

  public static @NotNull PerlHostHandler<?, ?> getDefaultHandler() {
    return Objects.requireNonNull(EP.findSingle("localhost"), "Local handler MUST always present");
  }
}
