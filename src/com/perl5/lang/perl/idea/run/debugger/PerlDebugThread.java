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

package com.perl5.lang.perl.idea.run.debugger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ByteArrayList;
import com.intellij.xdebugger.XDebugSession;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakPointDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEvent;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEventReady;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEventsDeserializer;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugThread extends Thread
{
	private final Gson myGson;
	private XDebugSession mySession;
	private Socket mySocket;
	private OutputStream myOutputStream;
	private InputStream myInputStream;
	private Semaphore myResponseSemaphore = new Semaphore();
	private boolean myWaitForResponse = false;
	private byte[] myResponseBuffer;
	private boolean myStop = false;
	private List<PerlLineBreakPointDescriptor> breakpointsDescriptorsQueue = new CopyOnWriteArrayList<PerlLineBreakPointDescriptor>();
	private boolean isReady = false;

	public PerlDebugThread(XDebugSession session)
	{
		super("PerlDebugThread");
		mySession = session;
		myGson = createGson();
	}

	public void queueLineBreakpointDescriptor(PerlLineBreakPointDescriptor descriptor)
	{
		if (descriptor != null)
		{
			breakpointsDescriptorsQueue.add(descriptor);
			if (isReady && mySession.isPaused())
			{
				sendQueuedBreakpoints();
			}
		}
	}

	protected void sendQueuedBreakpoints()
	{
		sendString("b " + new Gson().toJson(breakpointsDescriptorsQueue) + "\n");
		breakpointsDescriptorsQueue.clear();
	}

	@Override
	public void run()
	{
		try
		{
			mySocket = new Socket("localhost", 12345);
			myOutputStream = mySocket.getOutputStream();
			myInputStream = mySocket.getInputStream();

			ByteArrayList response = new ByteArrayList();

			while (!myStop)
			{
				response.clear();

				// reading bytes
				while (myInputStream != null)
				{
					byte newByte = (byte) myInputStream.read();
					if (newByte == '\n')
					{
						break;
					}
					else if (newByte == -1)
					{
						return;
					}
					else
					{
						response.add(newByte);
					}
				}

				if (myWaitForResponse)
				{
					myResponseBuffer = response.toNativeArray();
					myWaitForResponse = false;
					myResponseSemaphore.up();
				}
				else
				{
					processResponse(response);
				}
			}

		} catch (IOException e)
		{
		}
	}

	private void processResponse(ByteArrayList responseBytes)
	{
		String response = new String(responseBytes.toNativeArray(), CharsetToolkit.UTF8_CHARSET);
		PerlDebuggingEvent newEvent = myGson.fromJson(response, PerlDebuggingEvent.class);

		if (newEvent != null)
		{
			if (newEvent instanceof PerlDebuggingEventReady)
			{
				isReady = true;
				sendQueuedBreakpoints();
			}
			else
			{
				newEvent.setDebugSession(mySession);
				newEvent.setDebugThread(this);
				newEvent.doWork();
			}
		}
	}

	public void sendString(String string)
	{
		if (mySocket == null)
			return;

		try
		{
			myOutputStream.write(string.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String sendCommandAndGetResponse(String command, Object data)
	{
		return sendStringAndGetResponse(command + " " + new Gson().toJson(data) + "\n");
	}

	@Nullable
	public String sendStringAndGetResponse(String string)
	{
		if (mySocket == null)
			return null;

		myResponseSemaphore.down();
		myWaitForResponse = true;

		String response = null;
		try
		{
			myResponseBuffer = null;
			myOutputStream.write(string.getBytes());
			System.err.println("Sent and waiting:" + string + Hex.encodeHexString(string.getBytes()));
			myResponseSemaphore.waitFor(5000);
			myWaitForResponse = false;
			myResponseSemaphore.tryUp();

			if (myResponseBuffer == null)
			{
				response = "";
				System.err.println("No response, timeoout");
			}
			else
			{
				response = new String(myResponseBuffer, CharsetToolkit.UTF8_CHARSET);
				System.err.println("Got response");
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return response;
	}

	public Socket getSocket()
	{
		return mySocket;
	}

	public void setStop()
	{
		myStop = true;
		try
		{
			if (myInputStream != null)
			{
				myInputStream.close();
				myInputStream = null;
			}
		} catch (IOException e)
		{
		}
		try
		{
			if (myOutputStream != null)
			{
				myOutputStream.close();
				myOutputStream = null;
			}
		} catch (IOException e)
		{
		}
		try
		{
			if (mySocket != null)
			{
				mySocket.close();
				mySocket = null;
			}
		} catch (IOException e)
		{
		}

	}

	protected Gson createGson()
	{
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(PerlDebuggingEvent.class, new PerlDebuggingEventsDeserializer());
		return builder.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
	}

}
