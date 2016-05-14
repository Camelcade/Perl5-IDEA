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

import com.intellij.openapi.vfs.DeprecatedVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.testFramework.LightVirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hurricup on 14.05.2016.
 * Clone of mock file system
 */
public class PerlRemoteFileSystem extends DeprecatedVirtualFileSystem
{
	public static final String PROTOCOL = "perl5_remote";
	public static final String PROTOCOL_PREFIX = "perl5_remote://";
	private Map<String, VirtualFile> virtualFilesMap = new THashMap<String, VirtualFile>();

	@Override
	@Nullable
	public VirtualFile findFileByPath(@NotNull String path)
	{
		return virtualFilesMap.get(path);
	}

	public void dropCache()
	{
		virtualFilesMap.clear();
	}

	public void registerRemoteFile(String fileName, String filePath, String fileSource)
	{
		LightVirtualFile newVirtualFile = new PerlRemoteVirtualFile(fileName, filePath, fileSource);
		virtualFilesMap.put(fileName, newVirtualFile);
		virtualFilesMap.put(filePath, newVirtualFile);
	}

//	@Nullable
//	public VirtualFile getForeignVirtualFileByName(String fileName)
//	{
//		return virtualFilesMap.get(fileName);
//
	// this may be reused later
//		if( sourcesMap.containsKey(fileName))
//		{
//		}
//		final Semaphore responseSemaphore = new Semaphore();
//		responseSemaphore.down();
//
//		final String[] response = new String[]{null};
//
//		PerlDebuggingTransactionHandler perlDebuggingTransactionHandler = new PerlDebuggingTransactionHandler()
//		{
//			@Override
//			public void run(JsonObject eventObject, JsonDeserializationContext jsonDeserializationContext)
//			{
//				response[0] = eventObject.getAsJsonPrimitive("data").getAsString();
//				responseSemaphore.up();
//			}
//		};
//
//		sendCommandAndGetResponse("get_source", new PerlSourceRequestDescriptor(fileName), perlDebuggingTransactionHandler);
//		responseSemaphore.waitFor(10000);
//
//		sourcesMap.put(fileName, response[0]);
//		return response[0];
//	}


	@Override
	@NotNull
	public String getProtocol()
	{
		return PROTOCOL;
	}

	@Override
	public void refresh(boolean asynchronous)
	{
	}

	@Override
	public void deleteFile(Object requestor, @NotNull VirtualFile vFile) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@NotNull
	@Override
	public VirtualFile copyFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent, @NotNull final String copyName) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void renameFile(Object requestor, @NotNull VirtualFile vFile, @NotNull String newName) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@NotNull
	@Override
	public VirtualFile createChildFile(Object requestor, @NotNull VirtualFile vDir, @NotNull String fileName) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	@NotNull
	public VirtualFile createChildDirectory(Object requestor, @NotNull VirtualFile vDir, @NotNull String dirName) throws IOException
	{
		throw new IOException();
	}

	@Override
	public VirtualFile refreshAndFindFileByPath(@NotNull String path)
	{
		return findFileByPath(path);
	}

	public class PerlRemoteVirtualFile extends LightVirtualFile
	{
		private final String myPath;

		public PerlRemoteVirtualFile(@NotNull String name, String path, String text)
		{
			super(name, PerlFileType.INSTANCE, text);
			myPath = path;
		}

		@Override
		@NotNull
		public VirtualFileSystem getFileSystem()
		{
			return PerlRemoteFileSystem.this;
		}

		@Override
		public boolean isDirectory()
		{
			return false;
		}

		@NotNull
		@Override
		public String getPath()
		{
			return myPath;
		}
	}
}
