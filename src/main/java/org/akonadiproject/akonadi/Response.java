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

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response {
	public static final int RESPONSE_STATUS = 0;
	public static final int RESPONSE_COLLECTION_DATA = 1;
	public static final int RESPONSE_ITEM_DATA = 2;
	public static final int RESPONSE_CONTINUATION = 3;

	public static Response parse(CharSequence input) throws ProtocolException {
		// System.err.println( "parse: " + input );
		Matcher matcher = mResponsePattern.matcher(input);

		// Is this a valid response?
		if (matcher.matches()) {
			Response result = new Response(input.toString());

			// what kind of? (untagged)
			if (matcher.group(2) != null) {
				// test for resp-cond (OK/BAD/NO)
				Matcher responseCode = mStatusResponsePattern.matcher(matcher.group(5));
				if (responseCode.matches()) {
					result.mResponseType = RESPONSE_STATUS;
					result.mResponseSubType = responseCode.group(1);

					// do we have a responseCode?
					if (responseCode.group(3) != null) {
						// TODO
						// result.mResponseTextCode =
						// ResponseTextCodeParser.parse( responseCode.group( 3 )
						// );
						result.mResponseTextCode = responseCode.group(3);
					}

					// set reponse message
					result.mResponseMessage = responseCode.group(4);

					return result;
				}

				// test for collection data
				Matcher collectionData = mCollectionDataPattern.matcher(matcher.group(5));
				if (collectionData.matches()) {
					result.mResponseType = RESPONSE_COLLECTION_DATA;
					result.mEntityId = Long.parseLong(collectionData.group(1));
					result.mParentId = Long.parseLong(collectionData.group(2));

					// set reponse message
					result.mResponseMessage = collectionData.group(3);

					return result;
				}

				// test for message-data (EXPUNGE|FECTH)
				Matcher itemData = mItemDataPattern.matcher(matcher.group(5));
				if (itemData.matches()) {
					result.mResponseType = RESPONSE_ITEM_DATA;

					result.mEntityId = Long.parseLong(itemData.group(1));
					result.mResponseSubType = itemData.group(2);

					result.mResponseMessage = itemData.group(3);

					return result;
				}

				// Not any of the above subtypes
				throw new ProtocolException("Unknown subytpe : " + result.getSource());
			}

			// what kind of? (tagged)
			if (matcher.group(3) != null) {
				result.mTag = matcher.group(3);

				// test for resp-cond (OK/BAD/NO)
				Matcher responseCode = mStatusResponsePattern.matcher(matcher.group(5));
				if (responseCode.matches()) {
					result.mResponseType = RESPONSE_STATUS;
					result.mResponseSubType = responseCode.group(1);

					// do we have a responseCode?
					if (responseCode.group(3) != null) {
						// TODO
						// result.mResponseTextCode =
						// ResponseTextCodeParser.parse( respCode.group( 3 ) );
						result.mResponseTextCode = responseCode.group(3);
					}

					// set reponse text
					result.mResponseMessage = responseCode.group(4);

					return result;
				}

				// Not any of the above subtypes
				throw new ProtocolException("Unknown subytpe :" + result.getSource());
			}

			// what kind of? (continuation)
			if (matcher.group(4) != null) {
				result.mResponseType = RESPONSE_CONTINUATION;

				Matcher responseContinuation = mResponseContinuationPattern.matcher(matcher.group(5));
				if (responseContinuation.matches()) {
					// do we have a responseCode?
					if (responseContinuation.group(2) != null) {
						// TODO
						// result.mResponseTextCode =
						// ResponseTextCodeParser.parse(
						// responseContinuation.group( 2 ) );
						result.mResponseTextCode = responseContinuation.group(2);
					}

					// set reponse text
					result.mResponseMessage = responseContinuation.group(3);

					return result;
				}
			}
		}

		throw new ProtocolException("Not a valid Akonadi response : " + input);
	}

	public boolean isTagged() {
		return mTag != null;
	}

	public String getTag() {
		return mTag;
	}

	public boolean isOK() {
		return mResponseSubType.equals("OK");
	}

	public boolean isNO() {
		return mResponseSubType.equals("NO");
	}

	public boolean isBAD() {
		return mResponseSubType.equals("BAD");
	}

	public int getResponseType() {
		return mResponseType;
	}

	public String getResponseSubType() {
		return mResponseSubType;
	}

	public String getResponseMessage() {
		return mResponseMessage;
	}

	public String getResponseTextCode() {
		return mResponseTextCode;
	}

	public long getEntityId() {
		return mEntityId;
	}

	public long getParentId() {
		return mParentId;
	}

	public String getSource() {
		StringBuffer cleanedUp = new StringBuffer(mSource.length());
		mLiteralMatcher.reset(mSource);

		while (mLiteralMatcher.find()) {
			mLiteralMatcher.appendReplacement(cleanedUp, getData(mLiteralMatcher.group()).toString());
		}

		mLiteralMatcher.appendTail(cleanedUp);

		return cleanedUp.toString();
	}

	public CharSequence getData(CharSequence data) {
		if (data.length() == 0)
			return data;

		mLiteralMatcher.reset(data);
		if (mLiteralMatcher.matches()) {
			return getLiteral(Integer.parseInt(mLiteralMatcher.group(1)));
		} else {
			// remove ""
			if (data.charAt(0) == '"') {
				return data.subSequence(1, data.length() - 1);
			} else {
				return data;
			}
		}
	}

	public CharSequence getLiteral(int index) {
		return mLiterals.get(index);
	}

	protected Response(String source) {
		mSource = source;
		mEntityId = -1;
		mParentId = -1;
	}

	protected void addLiteral(CharSequence literal) {
		if (mLiterals == null) {
			mLiterals = new LinkedList<CharSequence>();
		}
		mLiterals.add(literal);
	}

	protected void appendResponseText(String restResponse) {
		mSource += restResponse;
		mResponseMessage += restResponse;
	}

	protected Matcher literalMatcher() {
		return mLiteralMatcher;
	}

	protected String mSource;
	protected String mTag;
	protected int mResponseType;
	protected String mResponseSubType;
	protected String mResponseMessage;
	protected String mResponseTextCode;
	protected long mEntityId;
	protected long mParentId;
	protected List<CharSequence> mLiterals;

	// Pattern to classify the response in tagged/untagged/continuation
	// and to access the text of the reponse without the trailing CRLF
	private static final Pattern mResponsePattern = Pattern.compile("^((\\*)" + // group
			// 2
			// is
			// untagged
			"|([0-9a-zA-Z]+)" + // group 3 is the tag
			"|(\\+)) " + // group 4 is continuation
			"([^\r\n]*)\r?\n?"); // group 5 is the rest of the response without
	// a optional CRLF

	// Pattern to classify a status reponse.
	private static final Pattern mStatusResponsePattern = Pattern.compile("^(OK|BAD|NO) " + // group
																							// 1
																							// is
																							// the
																							// status
																							// type
			"(\\[(\\w+[^\\]]+)\\])?" + // group 3 is an optional text
			// code
			" ?([^\r\n]*)"); // group 4 is the message

	// Pattern to classify a collection data response.
	private static final Pattern mCollectionDataPattern = Pattern.compile("^(\\d+)" + // group1
																						// contains
																						// the
																						// collection
																						// id
			" (\\d+)" + // group2 contains the collection parent id
			" ?([^\r\n]*)"); // group3 contains the data

	// Pattern to classify a message data response.
	private static final Pattern mItemDataPattern = Pattern.compile("^(\\d+) " + // group1
			// contains
			// the
			// item
			// id
			"(EXPUNGE|FETCH)" + // group2 contains the command
			" ?([^\r\n]*)"); // group3 contains the data

	// Pattern to classify a continuation response.
	private static final Pattern mResponseContinuationPattern = Pattern.compile("^(\\[(\\w+[^\\]]+)\\] )?" + // group2
																												// contains
																												// optional
			// text code
			"([^\r\n]*)"); // group3 contains the message

	private static final Pattern mLiteralPattern = Pattern.compile("^\\{(\\d+)\\}$");
	private static final Matcher mLiteralMatcher = mLiteralPattern.matcher("");
}
