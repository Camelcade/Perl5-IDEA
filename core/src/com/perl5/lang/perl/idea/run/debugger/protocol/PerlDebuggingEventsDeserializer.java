/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger.protocol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugThread;

import java.lang.reflect.Type;


public class PerlDebuggingEventsDeserializer implements JsonDeserializer<PerlDebuggingEvent> {
  private final PerlDebugThread myPerlDebugThread;

  public PerlDebuggingEventsDeserializer(PerlDebugThread perlDebugThread) {
    myPerlDebugThread = perlDebugThread;
  }

  @Override
  public PerlDebuggingEvent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
    throws JsonParseException {
    String event = jsonElement.getAsJsonObject().getAsJsonPrimitive("event").getAsString();

    PerlDebuggingEvent eventObject = null;

    if (StringUtil.isNotEmpty(event)) {
      if (StringUtil.equals(event, "STOP")) {
        PerlDebuggingEventStop newEvent = new PerlDebuggingEventStop();

        newEvent.setFrames(
          (PerlStackFrameDescriptor[])jsonDeserializationContext.deserialize(
            jsonElement.getAsJsonObject().getAsJsonArray("data"), PerlStackFrameDescriptor[].class
          ));

        eventObject = newEvent;
      }
      else if (StringUtil.equals(event, "BREAKPOINT_REACHED")) {
        eventObject = jsonDeserializationContext.deserialize(
          jsonElement.getAsJsonObject().getAsJsonObject("data"), PerlDebuggingEventBreakpointReached.class);
      }
      else if (StringUtil.equals(event, "BREAKPOINT_SET")) {
        eventObject = jsonDeserializationContext.deserialize(
          jsonElement.getAsJsonObject().getAsJsonObject("data"), PerlDebuggingEventBreakpointSet.class);
      }
      else if (StringUtil.equals(event, "BREAKPOINT_DENIED")) {
        eventObject = jsonDeserializationContext.deserialize(
          jsonElement.getAsJsonObject().getAsJsonObject("data"), PerlDebuggingEventBreakpointDenied.class);
      }
      else if (StringUtil.equals(event, "READY")) {
        eventObject = new PerlDebuggingEventReady();
        ((PerlDebuggingEventReady)eventObject).version = jsonElement.getAsJsonObject().getAsJsonPrimitive("version").getAsString();
      }
      else if (StringUtil.equals(event, "LOADED_FILES_DELTA")) {
        eventObject = jsonDeserializationContext.deserialize(
          jsonElement.getAsJsonObject().getAsJsonObject("data"), PerlDebuggingEventLoadedFiles.class);
      }
      else if (StringUtil.equals(event, "RESPONSE")) {
        int transactionId = jsonElement.getAsJsonObject().getAsJsonPrimitive("transactionId").getAsInt();
        PerlDebuggingTransactionHandler transactionHandler = myPerlDebugThread.getTransactionHandler(transactionId);

        if (transactionHandler == null) {
          throw new RuntimeException("Missing transaction handler for transaction " + transactionId);
        }

        transactionHandler.run(jsonElement.getAsJsonObject(), jsonDeserializationContext);

        eventObject = new PerlDebuggingEventDumb();
      }
      else {
        System.err.println("Unhandled event in request: " + jsonElement.getAsString());
      }
    }
    else {
      System.err.println("Empty event in request: " + jsonElement.getAsString());
    }

    return eventObject;
  }
}
