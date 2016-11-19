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

package markers;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.GutterMark;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.navigation.GotoRelatedItem;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.UsefulTestCase;

import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 19.11.2016.
 */
public class PerlGutterMarkersTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/markers/perl";
	}

	public void testSupermethods()
	{
		initWithFileSmart();
		List<GutterMark> allMarkers = myFixture.findAllGutters();
		String text = myFixture.getDocument(myFixture.getFile()).getText();
		StringBuilder b = new StringBuilder();
		for (GutterMark gutterMarker : allMarkers)
		{
			if (gutterMarker instanceof LineMarkerInfo.LineMarkerGutterIconRenderer)
			{
				LineMarkerInfo lineMarkerInfo = ((LineMarkerInfo.LineMarkerGutterIconRenderer) gutterMarker).getLineMarkerInfo();
				b.append(lineMarkerInfo.startOffset).append(" - ").append(lineMarkerInfo.endOffset).append(": ")
						.append('\'')
						.append(text.substring(lineMarkerInfo.startOffset, lineMarkerInfo.endOffset))
						.append('\'')
						.append(": ")
						.append(lineMarkerInfo.getLineMarkerTooltip())
						.append("\n");


				Collection<GotoRelatedItem> gotoRelatedItems = ((RelatedItemLineMarkerInfo) lineMarkerInfo).createGotoRelatedItems();
				b.append("Targets: ").append(gotoRelatedItems.size()).append("\n");

				for (GotoRelatedItem gotoRelatedItem : gotoRelatedItems)
				{
					PsiElement element = gotoRelatedItem.getElement();
					if (element != null)
					{
						b.append("    ")
								.append(element)
								.append(" in ")
								.append(element.getContainingFile().getName())
								.append(" at ")
								.append(element.getNavigationElement().getNode().getStartOffset())
								.append("\n")
						;

					}
				}

			}
			else
			{
				b.append(gutterMarker);
			}
			b.append("\n");
		}
		UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), b.toString());
	}


}
