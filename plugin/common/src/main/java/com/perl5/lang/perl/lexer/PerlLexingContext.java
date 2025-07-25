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

package com.perl5.lang.perl.lexer;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlLexingContext {
  private final @Nullable Project myProject;

  private final boolean myEnforceSubLexing;
  private final boolean myWithTryCatch;
  private final char myOpenChar;

  private PerlLexingContext(@Nullable Project project,
                            boolean enforceSubLexing,
                            boolean withTryCatch,
                            char openChar) {
    myProject = project;
    myEnforceSubLexing = enforceSubLexing;
    myWithTryCatch = withTryCatch;
    myOpenChar = openChar;
  }

  public @Nullable Project getProject() {
    return myProject;
  }

  public boolean isEnforceSubLexing() {
    return myEnforceSubLexing;
  }

  public boolean isWithTryCatch() {
    return myWithTryCatch;
  }

  public char getOpenChar() {
    return myOpenChar;
  }

  public @NotNull PerlLexingContext withEnforcedSublexing(boolean enforceSubLexing) {
    return new PerlLexingContext(myProject, enforceSubLexing, myWithTryCatch, myOpenChar);
  }

  public @NotNull PerlLexingContext withTryCatchSyntax(boolean withTryCatch) {
    return new PerlLexingContext(myProject, myEnforceSubLexing, withTryCatch, myOpenChar);
  }

  public @NotNull PerlLexingContext withOpenChar(char openChar) {
    return new PerlLexingContext(myProject, myEnforceSubLexing, myWithTryCatch, openChar);
  }

  public static @NotNull PerlLexingContext create(@Nullable Project project) {
    return new PerlLexingContext(project, false, false, (char)0);
  }
}
