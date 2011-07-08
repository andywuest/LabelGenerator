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

import java.io.IOException;

public class Session {

	private String mSessionId;
	private DataConnection mConnection;

	private long mTagCounter;

	private CommandOutputStream mOutputStream;
	private ResponseInputStream mInputStream;

	private static Session mDefaultSession;

	public Session(String sessionId) throws IOException, ProtocolException {
		mSessionId = sessionId;

		mConnection = new DataConnection();

		mInputStream = new ResponseInputStream(mConnection.getInputStream());
		mOutputStream = new CommandOutputStream(mConnection.getOutputStream(), mInputStream);

		login();
	}

	public String getNextTag() {
		return Long.toString(mTagCounter++);
	}

	public CommandOutputStream getCommandOutputStream() {
		return mOutputStream;
	}

	public ResponseInputStream getResponseInputStream() {
		return mInputStream;
	}

	public static void createDefaultSession(String sessionId) throws IOException, ProtocolException {
		// TODO: exception when mDefaultSession != null?

		mDefaultSession = new Session(sessionId);
	}

	public static Session getDefaultSession() {
		return mDefaultSession;
	}

	private void login() throws IOException, ProtocolException {
		Response response = mInputStream.readResponse();
		if (!response.isOK())
			throw new ProtocolException(response);

		Command command = new Command(getNextTag(), "LOGIN", new Object[] { mSessionId });
		mOutputStream.writeCommand(command);

		response = mInputStream.readResponse();
		// TODO: better exception
		if (!response.isOK())
			throw new ProtocolException(response);
	}

}
