/***************************************************************************
 *   Copyright (C) 2009 by Kevin Krammer <kevin.krammer@gmx.at>            *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
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

import java.util.List;

public class ParenthesisParser {
	public static int parseParenthesizedList(CharSequence input,
			List<CharSequence> result, int start) throws ProtocolException {
		result.clear();

		int index = start + 1;
		while (index < input.length()) {
			switch (input.charAt(index)) {
			case '(': {
				int end = getClosingParenthesis(input, index);
				result.add(input.subSequence(index, end + 1));
				index = end;
				break;
			}

			case ' ':
			case ')':
				++index;
				break;

			default:
				index = parseQuotedString(input, result, index);
				break;
			}
		}

		return input.length();
	}

	public static int parseQuotedString(CharSequence input,
			List<CharSequence> result, int start) throws ProtocolException {
		int subSeqStart = -1;
		boolean inQuotes = false;
		for (int index = start; index < input.length(); ++index) {
			switch (input.charAt(index)) {
			case ' ':
				if (subSeqStart != -1 && !inQuotes) {
					result.add(input.subSequence(subSeqStart, index));
					return index + 1;
				}
				break;

			case '\"':
				if (index > 0 && input.charAt(index - 1) == '\\') {
					if (subSeqStart == -1) {
						subSeqStart = index;
					}
					break;
				}
				inQuotes = !inQuotes;
				if (inQuotes && subSeqStart == -1) {
					subSeqStart = index + 1;
				}
				if (!inQuotes) {
					if (subSeqStart == -1)
						throw new ProtocolException(
								"No string value after index " + start
										+ " in input '" + input + "'");

					result.add(input.subSequence(subSeqStart, index));
					return index + 1;
				}
				break;

			case '(': // fall through
				if (!inQuotes) {
					if (subSeqStart == -1)
						throw new ProtocolException(
								"No string value after index " + start
										+ " in input '" + input + "'");

					CharSequence subSeq = input.subSequence(subSeqStart, index);
					System.err.println("subSeq: start=" + subSeqStart
							+ ", end=" + index);
					result.add(subSeq);
					return index;
				}
				break;

			default:
				if (subSeqStart == -1) {
					subSeqStart = index;
				}
				break;
			}
		}

		if (subSeqStart == -1)
			throw new ProtocolException("No string value after index " + start
					+ " in input '" + input + "'");

		result.add(input.subSequence(subSeqStart, input.length() - 1));
		return input.length();
	}

	public static int getClosingParenthesis(CharSequence input,
			int openingParenthesis) throws ProtocolException {
		boolean inQuotes = false;
		int level = 1;

		for (int index = openingParenthesis + 1; index < input.length(); ++index) {
			switch (input.charAt(index)) {
			case '\"':
				if (index > 0 && input.charAt(index - 1) == '\\')
					break;
				inQuotes = !inQuotes;
				break;

			case '(':
				if (!inQuotes) {
					++level;
				}
				break;

			case ')':
				if (!inQuotes) {
					--level;
				}
				if (level == 0)
					return index;
				break;
			}
		}

		throw new ProtocolException("No closing parenthesis for opening at "
				+ openingParenthesis + " in \'" + input + "'");
	}
}
