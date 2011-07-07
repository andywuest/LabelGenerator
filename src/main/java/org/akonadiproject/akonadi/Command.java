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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public class Command {

	private String mTag;
	private String mCommand;
	private Object[] mParameters;
	private boolean mLastWasLiteral;
	private Charset mCharset;

	public Command(String tag, String command) {
		this(tag, command, null);
	}

	public Command(String tag, String command, Object[] parameters) {
		this(tag, command, parameters, Charset.forName("ISO-8859-1"));
	}

	public Command(String tag, String command, Object[] parameters,
			Charset charset) {
		mTag = tag;
		mCommand = command;
		mParameters = parameters;
		mCharset = charset;
	}

	public String getTag() {
		return mTag;
	}

	protected void writeToStream(ResponseInputStream in, OutputStream out)
			throws ProtocolException, IOException {
		Object arg;
		out.write(mTag.getBytes("US-ASCII"));
		out.write(' ');
		out.write(mCommand.getBytes("US-ASCII"));
		// write arguments
		if (mParameters != null) {
			Iterator<Object> it = Arrays.asList(mParameters).iterator();
			while (it.hasNext()) {
				out.write(' ');
				arg = it.next();
				if (arg instanceof String) {
					writeString((String) arg, in, out);
				} else if (arg instanceof String[]) {
					writeStringArray((String[]) arg, in, out);
					/*
					 * } else if ( arg instanceof InputStream ) {
					 * writeInputStream( (InputStream) arg, in, out );
					 */
					/*
					 * } else if ( arg instanceof SearchKey ) { writeSearchKey(
					 * (SearchKey) arg, in, out );
					 */
				} else if (arg instanceof byte[]) {
					writeByteArray(((byte[]) arg), in, out);
				} else if (arg instanceof char[]) {
					writeCharArray(((char[]) arg), out);
					/*
					 * } else if ( arg instanceof Section ) { writeSection(
					 * (Section) arg, in, out );
					 */
				} else {
					writeCharArray(arg.toString().toCharArray(), out);
				}
			}
		}

		out.write('\r');
		out.write('\n');
		out.flush();
	}

	private void writeCharArray(char[] cs, OutputStream out)
			throws ProtocolException, IOException {
		for (int i = 0; i < cs.length; ++i) {
			out.write(cs[i]);
		}
	}

	private void writeByteArray(byte[] bs, ResponseInputStream in,
			OutputStream out) throws ProtocolException, IOException {
		out.write('{');
		out.write(Integer.toString(bs.length).getBytes(mCharset.name()));
		out.write('}');
		out.write('\r');
		out.write('\n');
		out.flush();

		Response response = in.readResponse();

		// TODO: better exception
		if (response.getResponseType() != Response.RESPONSE_CONTINUATION)
			throw new ProtocolException(response);

		out.write(bs);
		out.flush();
		mLastWasLiteral = true;
	}

	private void writeStringArray(String[] strings, ResponseInputStream in,
			OutputStream out) throws ProtocolException, IOException {
		out.write('(');
		if (strings.length > 0) {
			out.write(strings[0].getBytes(mCharset.name()));
			for (int i = 1; i < strings.length; ++i) {
				out.write(' ');
				writeString(strings[i], in, out);
			}
		}
		out.write(')');
		mLastWasLiteral = false;
	}

	private void writeString(String sequence, ResponseInputStream in,
			OutputStream out) throws ProtocolException, IOException {
		// check if the argument is 7-bit, " and \ safe
		boolean plainSafe = true;
		boolean quote = sequence.length() == 0;

		int i = 0;
		while (i < sequence.length() && plainSafe) {
			plainSafe &= sequence.charAt(i) < 128;
			plainSafe &= sequence.charAt(i) != '\"';
			plainSafe &= sequence.charAt(i) != '\\';
			plainSafe &= sequence.charAt(i) != '\0';
			plainSafe &= sequence.charAt(i) != '\r';
			plainSafe &= sequence.charAt(i) != '\n';

			quote |= sequence.charAt(i) == ' ';
			quote |= sequence.charAt(i) == '(';
			quote |= sequence.charAt(i) == ')';
			quote |= sequence.charAt(i) == '{';
			quote |= sequence.charAt(i) == '%';
			quote |= sequence.charAt(i) == '*';
			quote |= sequence.charAt(i) == ']';
			// quote |= sequence.charAt(i) == '/';

			++i;
		}

		// write as literal if not plain safe else just write the bytes
		if (plainSafe) {
			if (quote) {
				out.write('\"');
			}
			out.write(sequence.getBytes(mCharset.name()));
			if (quote) {
				out.write('\"');
			}
			mLastWasLiteral = false;
		} else {
			writeByteArray(sequence.getBytes(mCharset.name()), in, out);
		}
	}

}
