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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item extends Entity {

	private String mMimeType;
	private int mRevision;

	private static final Pattern mURIPattern = Pattern.compile(".*item=(\\d+).*");
	private static final Pattern mURIMimePattern = Pattern.compile(".*type=([a-z]+/[a-z]*).*");

	public Item() {
	}

	public void setMimeType(String mimeType) {
		mMimeType = mimeType;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public static Item fromURI(URI uri) throws URISyntaxException {
		if (!uri.getScheme().equals("akonadi"))
			throw new URISyntaxException(uri.getScheme(), "Not an akonadi: URI");

		Matcher matcher = mURIPattern.matcher(uri.getQuery());
		if (!matcher.matches()) {
			throw new URISyntaxException(uri.getQuery(), "Query does not match pattern \"item=id\"");
		}

		long id = 0;
		try {
			id = Long.parseLong(matcher.group(1));
		} catch (NumberFormatException e) {
			e.printStackTrace(System.err);
			throw new URISyntaxException(uri.getQuery(), "Item ID is not of type long");
		}

		Item item = new Item(id);

		matcher = mURIMimePattern.matcher(uri.getQuery());
		if (matcher.matches()) {
			item.setMimeType(matcher.group(1));
		}

		return item;
	}

	public void setRevision(int revision) {
		mRevision = revision;
	}

	public int getRevision() {
		return mRevision;
	}

	public URI getURI() {
		try {
			return new URI("akonadi", null, null, "collection=" + getId(), null);
		} catch (URISyntaxException e) {
			e.printStackTrace(System.err);
		}

		return null;
	}

	public URI getURIWithMimeType() {
		try {
			return new URI("akonadi", null, null, "collection=" + getId() + ",type=" + mMimeType, null);
		} catch (URISyntaxException e) {
			e.printStackTrace(System.err);
		}

		return null;
	}

	protected Item(long id) {
		super(id);
	}

	protected static Item fromResponse(Response response) throws ProtocolException {
		if (response.getEntityId() < 0)
			throw new ProtocolException(response);

		Item item = new Item(response.getEntityId());

		ArrayList<CharSequence> parameters = new ArrayList<CharSequence>(16);
		ParenthesisParser.parseParenthesizedList(response.getResponseMessage(), parameters, 0);

		Iterator<CharSequence> it = parameters.iterator();
		while (it.hasNext()) {
			CharSequence key = it.next();
			if (key.equals("UID")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for item UID missing in response '"
							+ response.getResponseMessage() + "'");

				CharSequence value = it.next();
				try {
					long id = Long.parseLong(value.toString());
					if (id < 0)
						throw new ProtocolException("Value for item UID is not a valid ID but '" + value + "'");

					item.mId = id;
				} catch (NumberFormatException e) {
					e.printStackTrace(System.err);
					throw new ProtocolException("Value for item UID is not a long integer but '" + value + "'");
				}
			} else if (key.equals("REMOTEID")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for item REMOTEID missing in response '"
							+ response.getResponseMessage() + "'");

				item.setRemoteId(it.next().toString());
			} else if (key.equals("MIMETYPE")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for item MIMETYPE missing in response '"
							+ response.getResponseMessage() + "'");

				item.setMimeType(it.next().toString());
			} else if (key.equals("REV")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for item REV missing in response '"
							+ response.getResponseMessage() + "'");

				CharSequence value = it.next();
				try {
					int revision = Integer.parseInt(value.toString());
					if (revision < 0)
						throw new ProtocolException("Value for item REV is not a valid revision but '" + value + "'");

					item.setRevision(revision);
				} catch (NumberFormatException e) {
					e.printStackTrace(System.err);
					throw new ProtocolException("Value for item REV is not an integer but '" + value + "'");
				}
			}
		}

		return item;
	}

}
