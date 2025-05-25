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

package com.perl5.lang.perl.cpanminus.runAnything;

import com.intellij.ide.actions.runAnything.activity.RunAnythingProviderBase;
import com.intellij.ide.actions.runAnything.items.RunAnythingHelpItem;
import com.intellij.ide.actions.runAnything.items.RunAnythingItem;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

public class PerlCpanminusRunAnythingProvider extends RunAnythingProviderBase<List<PerlCpanminusRunAnythingProvider.CommandElement>> {
  private static final Logger LOG = Logger.getInstance(PerlCpanminusRunAnythingProvider.class);
  private static final String GENERAL_KEY = "general";
  private static final String COMPLETION_GROUP_NAME_KEY = "completionGroupName";
  private static final String HELP_GROUP_NAME_KEY = "helpGroupName";
  private static final String COMMAND_KEY = "command";
  private static final String OPTIONS_FIRST_KEY = "optionsFirst";

  private static final String OPTIONS_KEY = "options";
  private static final String OPTION_KEY = "option";

  private static final String COMMANDS_KEY = "commands";

  private static final String VALUE_KEY = "value";
  private static final String ALIAS_KEY = "alias";
  private static final String PARAMETER_PLACEHOLDER_KEY = "parameterPlaceholder";
  private static final String DESCRIPTION_KEY = "description";

  private final @NotNull String myCommand;
  private final @NotNull String myHelpGroupName;
  private final @NotNull String myCompletionGroupName;
  private final @NotNull Set<String> myCommands;
  private final @NotNull Set<OptionDescriptor> myOptionDescriptors;
  private final @NotNull Set<CommandDescriptor> myCommandDescriptors;
  private final @NotNull Map<String, OptionDescriptor> myAllOptionsAndCommands;

  private final boolean myIsOptionsFirst;

  public PerlCpanminusRunAnythingProvider() {
    this("runAnything/cli/cpanm.xml");
  }

  protected PerlCpanminusRunAnythingProvider(@NotNull String pathToConfig) {
    LOG.debug("Loading cli options from " + pathToConfig);
    ClassLoader classLoader = getClass().getClassLoader();
    final Element xmlElement;
    try {
      xmlElement = JDOMUtil.load(classLoader.getResourceAsStream(pathToConfig));
      if (xmlElement == null) {
        throw new RuntimeException("Error loading resources from " + classLoader + " " + pathToConfig);
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    Element generalElement = xmlElement.getChild(GENERAL_KEY);
    if (generalElement == null) {
      throw new RuntimeException("Missing general element in " + pathToConfig);
    }

    myCompletionGroupName = generalElement.getAttributeValue(COMPLETION_GROUP_NAME_KEY);
    if (myCompletionGroupName == null) {
      throw new RuntimeException("No completion group name: " + pathToConfig);
    }

    myHelpGroupName = generalElement.getAttributeValue(HELP_GROUP_NAME_KEY);
    myCommand = generalElement.getAttributeValue(COMMAND_KEY);
    if (myCommand == null) {
      throw new RuntimeException("No command in " + pathToConfig);
    }
    myIsOptionsFirst = Boolean.parseBoolean(generalElement.getAttributeValue(OPTIONS_FIRST_KEY));
    LOG.debug("Loading options for " + myCommand + " from " + pathToConfig);

    Map<String, OptionDescriptor> allOptionsMap = new HashMap<>();

    readOptions(xmlElement.getChild(OPTIONS_KEY), allOptionsMap);
    myOptionDescriptors = allOptionsMap.isEmpty() ? Collections.emptySet() :
                          Collections.unmodifiableSet(new HashSet<>(allOptionsMap.values()));
    LOG.debug("Found " + myOptionDescriptors.size() + " options");

    myCommands = readCommands(xmlElement.getChild(COMMANDS_KEY), allOptionsMap);
    Set<CommandDescriptor> commandDescriptors = new HashSet<>();
    ContainerUtil.filter(
      allOptionsMap.values(), it -> it instanceof CommandDescriptor).forEach(it -> commandDescriptors.add((CommandDescriptor)it));
    myCommandDescriptors = commandDescriptors.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(commandDescriptors);
    LOG.debug("Found " + myCommandDescriptors.size() + " commands");

    myAllOptionsAndCommands = allOptionsMap.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(allOptionsMap);
  }


  @Override
  public @NotNull RunAnythingItem getMainListItem(@NotNull DataContext dataContext, @NotNull List<CommandElement> value) {
    return new RunAnythingHelpItem(getItemPresentableText(value), getCommand(value), getItemDescription(value), getIcon(value));
  }

  protected @NotNull String getItemPresentableText(@NotNull List<CommandElement> value) {
    if (value.isEmpty()) {
      return getCommand();
    }
    CommandElement lastElement = ContainerUtil.getLastItem(value);
    if (lastElement == null) {
      return getCommand();
    }
    OptionDescriptor lastElementDescriptor = lastElement.descriptor;
    if (StringUtil.isEmpty(lastElementDescriptor.placeholder)) {
      return StringUtil.notNullize(lastElementDescriptor.value);
    }
    return lastElementDescriptor.value + " <" + lastElementDescriptor.placeholder + ">";
  }

  protected @Nullable String getItemDescription(@NotNull List<CommandElement> value) {
    if (value.isEmpty()) {
      return null;
    }
    CommandElement lastItem = ContainerUtil.getLastItem(value);
    if (lastItem == null) {
      return null;
    }

    OptionDescriptor optionDescriptor = lastItem.descriptor;
    return optionDescriptor.description;
  }

  /**
   * @return our cli model built from the pattern or null if it's empty or not our pattern
   */
  protected @Nullable List<CommandElement> buildModel(@NotNull String pattern) {
    // fixme more sophisticated splitting
    String[] patternChunks = pattern.trim().split("\\s+");
    if (patternChunks.length == 0) {
      return null;
    }
    if (!getCommand().equals(patternChunks[0])) {
      return null;
    }
    List<CommandElement> commandModel = new ArrayList<>();
    for (int i = 1; i < patternChunks.length; i++) {
      String patternChunk = patternChunks[i];
      CommandElement lastElement = ContainerUtil.getLastItem(commandModel);
      if (lastElement != null && lastElement.waitsParameter()) {
        lastElement.addParameter(patternChunk);
        continue;
      }
      CommandElement commandElement = new CommandElement(
        ObjectUtils.notNull(myAllOptionsAndCommands.get(patternChunk), OptionDescriptor.UNKNOWN));
      if (commandElement.isUnknown()) {
        commandElement.addParameter(patternChunk);
      }
      commandModel.add(commandElement);
    }

    if (myCommands.contains("")) {
      // repacking tail unknown items to the implicit command
      List<String> unknownCommands = new ArrayList<>();
      while (true) {
        CommandElement lastItem = ContainerUtil.getLastItem(commandModel);
        if (lastItem == null || !lastItem.isUnknown()) {
          break;
        }
        unknownCommands.addAll(0, lastItem.parameters);
        commandModel.removeLast();
      }
      if (!unknownCommands.isEmpty()) {
        CommandElement implicitCommandElement = new CommandElement(myAllOptionsAndCommands.get(""));
        implicitCommandElement.addParameters(unknownCommands);
        commandModel.add(implicitCommandElement);
      }
    }

    return commandModel;
  }

  @Override
  public @NotNull Collection<List<CommandElement>> getValues(@NotNull DataContext dataContext, @NotNull String pattern) {
    List<CommandElement> commandModel = buildModel(pattern);
    if (commandModel == null) {
      return Collections.emptyList();
    }

    List<CommandDescriptor> existingCommands = new ArrayList<>();
    List<OptionDescriptor> existingOptions = new ArrayList<>();
    commandModel.forEach(it -> {
      if (it.descriptor instanceof CommandDescriptor commandDescriptor) {
        existingCommands.add(commandDescriptor);
      }
      else {
        existingOptions.add(it.descriptor);
      }
    });

    CommandDescriptor effectiveCommand = existingCommands.isEmpty() ? null : ContainerUtil.getLastItem((existingCommands));
    existingCommands.remove(effectiveCommand);
    if (!existingCommands.isEmpty()) {
      //noinspection SuspiciousMethodCalls very rare case, doesn't matter
      commandModel.removeIf(it -> existingCommands.contains(it.descriptor));
    }

    Collection<OptionDescriptor> existingKnownOptions = ContainerUtil.intersection(existingOptions, myOptionDescriptors);

    Set<CommandDescriptor> suitableCommands = new HashSet<>();
    if (effectiveCommand != null) {
      suitableCommands.add(effectiveCommand);
    }
    else if (existingKnownOptions.isEmpty()) {
      suitableCommands.addAll(myCommandDescriptors);
    }
    else {
      suitableCommands.addAll(ContainerUtil.filter(myCommandDescriptors, it -> it.applicableOptions.containsAll(existingKnownOptions)));
    }

    Set<OptionDescriptor> suitableCommandsOptions = new HashSet<>();
    suitableCommands.forEach(it -> suitableCommandsOptions.addAll(it.applicableOptions));
    suitableCommandsOptions.removeAll(existingKnownOptions);

    List<List<CommandElement>> result = new ArrayList<>();

    if (effectiveCommand == null) {
      for (CommandDescriptor commandDescriptor : suitableCommands) {
        ArrayList<CommandElement> adjustedModel = new ArrayList<>(commandModel);
        adjustedModel.add(CommandElement.createBlank(commandDescriptor));
        result.add(adjustedModel);
      }
    }

    for (OptionDescriptor optionDescriptor : suitableCommandsOptions) {
      List<CommandElement> adjustedModel = new ArrayList<>(commandModel);
      adjustedModel.add(CommandElement.createBlank(optionDescriptor));
      result.add(adjustedModel);
    }

    return result;
  }

  @Override
  public @Nullable List<CommandElement> findMatchingValue(@NotNull DataContext dataContext, @NotNull String pattern) {
    return buildModel(pattern);
  }

  @Override
  public void execute(@NotNull DataContext dataContext, @NotNull List<CommandElement> value) {
    LOG.info("Running " + getCommand(value));
    Project project = CommonDataKeys.PROJECT.getData(dataContext);
    if (project == null) {
      LOG.warn("No project in context");
      return;
    }

    if (!PerlProjectManager.isPerlEnabled(project)) {
      LOG.warn("Perl is not configured for project " + project);
      return;
    }

    PerlRunUtil.runInConsole(new PerlCommandLine(getCommand(value)).withProject(project));
  }

  @Override
  public @Nullable String getCompletionGroupTitle() {
    return myCompletionGroupName;
  }

  @Override
  public @NotNull String getHelpGroupTitle() {
    return myHelpGroupName;
  }

  @Override
  public @Nullable Icon getIcon(@NotNull List<CommandElement> value) {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  public @NotNull Collection<RunAnythingHelpItem> getHelpItems() {
    List<RunAnythingHelpItem> result = new ArrayList<>();
    for (CommandDescriptor commandDescriptor : myCommandDescriptors) {
      CommandElement blankElement = CommandElement.createBlank(commandDescriptor);
      List<CommandElement> value = Collections.singletonList(blankElement);
      String fullCommand = getCommand(value);
      result.add(new RunAnythingHelpItem(fullCommand, fullCommand, commandDescriptor.description, getIcon(value)));
    }
    return result;
  }

  /**
   * @return command this provider serves
   */
  public @NotNull String getCommand() {
    return myCommand;
  }

  @Override
  public @NotNull String getCommand(@NotNull List<CommandElement> value) {
    List<String> result = new ArrayList<>();
    result.add(getCommand());
    List<CommandElement> commands = new ArrayList<>();
    List<CommandElement> options = new ArrayList<>();

    for (CommandElement element : value) {
      if (element.isCommand()) {
        commands.add(element);
      }
      else {
        options.add(element);
      }
    }
    if (myIsOptionsFirst) {
      options.forEach(it -> result.add(it.toString()));
      commands.forEach(it -> result.add(it.toString()));
    }
    else {
      commands.forEach(it -> result.add(it.toString()));
      options.forEach(it -> result.add(it.toString()));
    }

    return StringUtil.join(ContainerUtil.filter(result, StringUtil::isNotEmpty), " ");
  }

  @Override
  public @NotNull String getHelpCommand() {
    throw new RuntimeException("NYI");
  }

  @Override
  public @Nullable String getHelpCommandPlaceholder() {
    throw new RuntimeException("NYI");
  }

  @Override
  public @Nullable RunAnythingHelpItem getHelpItem(@NotNull DataContext dataContext) {
    return null;
  }

  @Override
  public @Nullable Icon getHelpIcon() {
    throw new RuntimeException("NYI");
  }

  private static void readOptions(@Nullable Element optionsElement,
                                  @NotNull Map<String, OptionDescriptor> allOptionsMap) {
    if (optionsElement == null) {
      return;
    }
    for (Element optionElement : optionsElement.getChildren(OPTION_KEY)) {
      String value = optionElement.getAttributeValue(VALUE_KEY);
      String description = optionElement.getAttributeValue(DESCRIPTION_KEY);
      String parameterPlaceholder = optionElement.getAttributeValue(PARAMETER_PLACEHOLDER_KEY);
      String alias = optionElement.getAttributeValue(ALIAS_KEY);
      if (StringUtil.isEmpty(value)) {
        LOG.warn("Element with empty value: " + optionElement.getText());
      }
      else if (allOptionsMap.containsKey(value)) {
        LOG.warn("Duplicate option: " + optionElement.getText());
      }
      else {
        OptionDescriptor optionDescriptor = new OptionDescriptor(value, parameterPlaceholder, description, alias);
        allOptionsMap.put(value, optionDescriptor);
        if (optionDescriptor.alias != null) {
          if (allOptionsMap.containsKey(optionDescriptor.alias)) {
            LOG.warn("Alias already in use: " + optionElement.getText());
          }
          else {
            allOptionsMap.put(optionDescriptor.alias, optionDescriptor);
          }
        }
      }
    }
  }

  private static @NotNull Set<String> readCommands(@Nullable Element commandsElement,
                                                   @NotNull Map<String, OptionDescriptor> allOptionsMap) {
    if (commandsElement == null) {
      return Collections.emptySet();
    }
    Set<String> commandsMap = new HashSet<>();
    for (Element commandElement : commandsElement.getChildren(COMMAND_KEY)) {
      String value = commandElement.getAttributeValue(VALUE_KEY);
      String description = commandElement.getAttributeValue(DESCRIPTION_KEY);
      String parameterPlaceholder = commandElement.getAttributeValue(PARAMETER_PLACEHOLDER_KEY);
      String alias = commandElement.getAttributeValue(ALIAS_KEY);
      Set<OptionDescriptor> applicableOptions = new HashSet<>();
      Element commandOptionsElement = commandElement.getChild(OPTIONS_KEY);
      if (commandOptionsElement != null) {
        commandOptionsElement.getChildren(OPTION_KEY).stream()
          .map(it -> it.getAttributeValue(VALUE_KEY))
          .filter(it -> !commandsMap.contains(it))
          .map(allOptionsMap::get)
          .filter(Objects::nonNull)
          .forEach(applicableOptions::add);
      }

      if (value == null) {
        LOG.warn("Missing command value: " + commandElement.getText());
      }
      else if (allOptionsMap.containsKey(value)) {
        LOG.warn("Duplicate option: " + commandElement.getText());
      }
      else {
        CommandDescriptor commandDescriptor = new CommandDescriptor(value, parameterPlaceholder, description, alias, applicableOptions);
        commandsMap.add(value);
        allOptionsMap.put(value, commandDescriptor);
        if (commandDescriptor.alias != null) {
          if (allOptionsMap.containsKey(commandDescriptor.alias)) {
            LOG.warn("Command alias already in use: " + commandElement.getText());
          }
          else {
            commandsMap.add(commandDescriptor.alias);
            allOptionsMap.put(commandDescriptor.alias, commandDescriptor);
          }
        }
      }
    }
    return commandsMap.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(commandsMap);
  }

  public static class OptionDescriptor {
    private static final OptionDescriptor UNKNOWN = new OptionDescriptor("", null, null, null);
    final @NotNull String value;
    final @Nullable String placeholder;
    final @Nullable String description;
    final @Nullable String alias;

    protected OptionDescriptor(@NotNull String value, @Nullable String placeholder, @Nullable String description, @Nullable String alias) {
      this.value = value;
      this.placeholder = StringUtil.nullize(placeholder);
      this.description = StringUtil.nullize(description);
      this.alias = StringUtil.nullize(alias);
    }

    public @NotNull String getPresentablePlaceholder() {
      return "<" + placeholder + ">";
    }

    public boolean isParametrized() {
      return getMaxArgumentsNumber() > 0;
    }

    public int getMaxArgumentsNumber() {
      return placeholder != null ? 1 : 0;
    }

    @Override
    public String toString() {
      return "Option: " + value;
    }
  }

  private static class CommandDescriptor extends OptionDescriptor {
    final @NotNull Set<OptionDescriptor> applicableOptions;

    public CommandDescriptor(@NotNull String value,
                             @Nullable String placeholder,
                             @Nullable String description,
                             @Nullable String alias,
                             @NotNull Set<OptionDescriptor> applicableOptions) {
      super(value, placeholder, description, alias);
      this.applicableOptions = applicableOptions;
    }

    /**
     * fixme this should be configurable via xml
     */
    @Override
    public int getMaxArgumentsNumber() {
      return placeholder != null ? Integer.MAX_VALUE : 0;
    }

    @Override
    public String toString() {
      return "Command: " + value;
    }
  }

  public static class CommandElement {
    final @NotNull OptionDescriptor descriptor;
    final @NotNull List<String> parameters = new ArrayList<>();

    private CommandElement(@NotNull OptionDescriptor descriptor) {
      this.descriptor = descriptor;
    }

    /**
     * Adds non-empty {@code parameter} to this command line element
     */
    public void addParameter(@Nullable String parameter) {
      if (StringUtil.isNotEmpty(parameter)) {
        parameters.add(parameter);
      }
    }

    public void addParameters(@NotNull Collection<String> parameters) {
      parameters.forEach(this::addParameter);
    }

    public boolean isParametrized() {
      return descriptor.isParametrized();
    }

    public boolean waitsParameter() {
      return isParametrized() && parameters.size() < descriptor.getMaxArgumentsNumber();
    }

    public boolean isCommand() {
      return descriptor instanceof CommandDescriptor;
    }

    public boolean isUnknown() {
      return descriptor == OptionDescriptor.UNKNOWN;
    }

    @Override
    public String toString() {
      return parameters.isEmpty() ? descriptor.value :
             descriptor.value.isEmpty() ? StringUtil.join(parameters, " ")
                                        : descriptor.value + " " + StringUtil.join(parameters, " ");
    }

    public static @NotNull CommandElement createBlank(@NotNull OptionDescriptor descriptor) {
      CommandElement result = new CommandElement(descriptor);
      if (descriptor.isParametrized()) {
        result.addParameter(descriptor.getPresentablePlaceholder().replace(' ', '_'));
      }
      return result;
    }
  }
}
