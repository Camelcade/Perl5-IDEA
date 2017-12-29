/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger.values;

import com.intellij.icons.AllIcons;
import com.intellij.xdebugger.frame.*;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlLayerDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlLayersDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PerlXLayersNamedValue extends XNamedValue {
  @NotNull
  private final PerlLayersDescriptor myLayersDescriptor;

  public PerlXLayersNamedValue(@NotNull PerlLayersDescriptor layersDescriptor) {
    super("IO Layers");
    myLayersDescriptor = layersDescriptor;
  }

  @Override
  public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {

    String value =
      Stream.of(myLayersDescriptor.getInput() == null ? null : "input", myLayersDescriptor.getOutput() == null ? null : "output")
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" & "));

    node.setPresentation(AllIcons.Windows.Restore, value, "", true);
  }

  @Override
  public void computeChildren(@NotNull XCompositeNode node) {
    XValueChildrenList childrenList = new XValueChildrenList();
    List<PerlLayerDescriptor> input = myLayersDescriptor.getInput();
    if (input != null) {
      childrenList.add(new PerlLayers("Input", AllIcons.ToolbarDecorator.Import, input));
    }
    List<PerlLayerDescriptor> output = myLayersDescriptor.getOutput();
    if (output != null) {
      childrenList.add(new PerlLayers("Output", AllIcons.ToolbarDecorator.Export, output));
    }
    node.addChildren(childrenList, true);
  }

  private static class PerlLayers extends XNamedValue {
    @NotNull
    private final List<PerlLayerDescriptor> myLayersDescriptors;
    @NotNull
    private final Icon myIcon;

    public PerlLayers(@NotNull String name,
                      @NotNull Icon icon,
                      @NotNull List<PerlLayerDescriptor> layersDescriptors) {
      super(name);
      myLayersDescriptors = layersDescriptors;
      myIcon = icon;
    }

    @Override
    public void computeChildren(@NotNull XCompositeNode node) {
      XValueChildrenList childrenList = new XValueChildrenList();
      int index = 0;
      for (PerlLayerDescriptor descriptor : myLayersDescriptors) {
        childrenList.add(new PerlLayer(index++, descriptor));
      }
      node.addChildren(childrenList, true);
    }

    @Override
    public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
      node.setPresentation(myIcon, Integer.toString(myLayersDescriptors.size()), "", true);
    }
  }

  private static class PerlLayer extends XNamedValue {
    @NotNull
    private final PerlLayerDescriptor myLayerDescriptor;

    public PerlLayer(int index, @NotNull PerlLayerDescriptor layerDescriptor) {
      super("[" + index + "]");
      myLayerDescriptor = layerDescriptor;
    }

    @Override
    public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
      node.setPresentation(null, myLayerDescriptor.getPresentableFlags(), myLayerDescriptor.getPresentableName(), false);
    }
  }
}
