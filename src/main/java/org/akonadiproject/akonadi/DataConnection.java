/***************************************************************************
 *   Copyright (C) 2009 by Kevin Krammer <kevin.krammer@gmx.at>            *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2.1 of the  *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/

package org.akonadiproject.akonadi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.freedesktop.xdg.BaseDirs;

import cx.ath.matthew.unix.UnixSocket;

public class DataConnection {
	public static final String CONNECTIONRC_FILE = "akonadi/akonadiconnectionrc";
	public static final String SOCKET_PATH = "/akonadi/akonadiserver.socket";

	private String mSocketPath;
	private UnixSocket mSocket;

	public DataConnection() throws IOException {
		BaseDirs baseDirs = new BaseDirs();

		mSocketPath = baseDirs.getUserPath(BaseDirs.Resource.DATA)
				+ SOCKET_PATH;
		try {
			File file = baseDirs.findResourceFile(BaseDirs.Resource.CONFIG,
					CONNECTIONRC_FILE);
			Settings connectionSettings = new Settings(file);

			String method = connectionSettings.getValue("Data/Method", null);
			if (method.equals("UnixPath")) {
				mSocketPath = connectionSettings.getValue("Data/UnixPath",
						mSocketPath);
			}
		} catch (FileNotFoundException exception) {
			/* TODO: use some logging facility */
			System.err
					.println("No valid Akonadi connection settings file found, using defaults");
			exception.printStackTrace(System.err);
		}

		/* TODO: use some logging facility */
		System.out
				.println("Creating Akonadi data connection at " + mSocketPath);

		mSocket = new UnixSocket(mSocketPath);
	}

	public boolean isConnected() {
		return mSocket.isConnected();
	}

	public void connect() throws IOException {
		if (!mSocket.isConnected()) {
			mSocket.connect(mSocketPath);
		}
	}

	public void close() throws IOException {
		mSocket.close();
	}

	public InputStream getInputStream() {
		return mSocket.getInputStream();
	}

	public OutputStream getOutputStream() {
		return mSocket.getOutputStream();
	}

}
