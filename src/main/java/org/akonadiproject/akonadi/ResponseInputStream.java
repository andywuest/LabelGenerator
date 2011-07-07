/***************************************************************************
 *   Copyright (C) 2009 by Kevin Krammer <kevin.krammer@gmx.at>            *
 *                                                                         *
 *   Based on code from the Ristretto Mail API                             *
 *   http://freshmeat.net/projects/ristretto/                              *
 *   Copyright (C) 2004 by  Frederik Dietz <fdietz@users.sourceforge.net>  *
 *   Copyright (C) 2004 by  Timo Stich <tstich@users.sourceforge.net>      *
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.regex.Matcher;

public class ResponseInputStream extends FilterInputStream {
	public ResponseInputStream(InputStream parentStream) {
		super(parentStream);

		mLineBuffer = new StringBuffer();
	}

	public Response readResponse() throws IOException, ProtocolException {
		readLine();
		// System.err.println( "mLineBuffer: " + mLineBuffer );
		Response result = Response.parse(mLineBuffer);
		parseResponseMessage(result);
		return result;
	}

	private void parseResponseMessage(Response response)
			throws ProtocolException, IOException {
		// is there a literal in the answer
		Matcher literalMatcher = response.literalMatcher();
		literalMatcher.reset(response.mResponseMessage);
		int literalIndex = 0;
		while (literalMatcher.find()) {
			// read literal from inputstream
			int literalSize = Integer.parseInt(literalMatcher.group(1));

			// Cleanup literals to make the {N} to increase for every literal
			response.mResponseMessage = response.mResponseMessage.substring(0,
					literalMatcher.start())
					+ '{' + (literalIndex++) + '}';

			// assign literal to response
			response.addLiteral(readLiteral(literalSize));

			// read rest in response and remove the trailing CRLF
			readLine();
			String restResponse = mLineBuffer.toString();
			restResponse = restResponse.substring(0, restResponse.length() - 2);
			response.appendResponseText(restResponse);

			// Could there be another Literal?
			if (restResponse.length() > 3) {
				literalMatcher.reset(response.mResponseMessage);
			}
		}
	}

	private void readLine() throws IOException {
		// Clear the buffer
		mLineBuffer.delete(0, mLineBuffer.length());

		int read = in.read();
		// read until CRLF
		while (read != '\r' && read != -1) {
			mLineBuffer.append((char) read);
			read = in.read();
		}
		mLineBuffer.append((char) read);

		// read the LF
		read = in.read();
		if (read != '\n')
			throw new IOException();
		mLineBuffer.append((char) read);
	}

	private CharSequence readLiteral(int literalSize) throws IOException {
		CharBuffer buffer = CharBuffer.allocate(literalSize);

		for (int i = 0; i < literalSize; ++i) {
			buffer.put((char) read());
		}

		return buffer;
	}

	private StringBuffer mLineBuffer;
}
