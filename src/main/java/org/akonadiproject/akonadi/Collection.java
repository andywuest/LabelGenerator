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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Collection extends Entity {

	public static final Collection ROOT = new Collection(0);
	public static final String MIME_TYPE = "inode/directory";

	public static final int READ_ONLY = 0x0;
	public static final int ALL_RIGHTS = 0x3F;

	public static final int CAN_MODIFY_ITEM = 0x1;
	public static final int CAN_CREATE_ITEM = 0x2;
	public static final int CAN_DELETE_ITEM = 0x4;

	public static final int CAN_MODIFY_COLLECTION = 0x8;
	public static final int CAN_CREATE_COLLECTION = 0x10;
	public static final int CAN_DELETE_COLLECTION = 0x20;

	private String mName;
	private String[] mContentMimeTypes;
	private Collection mParent;
	private String mResourceId;
	private CachePolicy mCachePolicy;

	private static final Pattern mURIPattern = Pattern.compile(".*collection=(\\d+).*");

	public Collection() {
	}

	public Collection(long id) {
		super(id);
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setContentMimeTypes(String[] mimeTypes) {
		mContentMimeTypes = mimeTypes;
	}

	public String[] getContentMimeTypes() {
		return mContentMimeTypes;
	}

	public void setParentCollection(Collection parent) {
		mParent = parent;
	}

	public Collection getParentCollection() {
		return mParent;
	}

	public void setResource(String resourceId) {
		mResourceId = resourceId;
	}

	public String getResource() {
		return mResourceId;
	}

	public void setRights(int rights) {
		try {
			CollectionRightsAttribute attribute = getOrCreateAttribute(CollectionRightsAttribute.class);
			attribute.setRights(rights);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public int getRights() {
		try {
			CollectionRightsAttribute attribute = getOrCreateAttribute(CollectionRightsAttribute.class);
			return attribute.getRights();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return Collection.READ_ONLY;
	}

	public void setCachePolicy(CachePolicy cachePolicy) {
		mCachePolicy = cachePolicy;
	}

	public CachePolicy getCachePolicy() {
		return mCachePolicy;
	}

	public static Collection fromURI(URI uri) throws URISyntaxException {
		if (!uri.getScheme().equals("akonadi"))
			throw new URISyntaxException(uri.getScheme(), "Not an akonadi: URI");

		Matcher matcher = mURIPattern.matcher(uri.getQuery());
		if (!matcher.matches())
			throw new URISyntaxException(uri.getQuery(), "Query does not match pattern \"collection=id\"");

		long id = 0;
		try {
			id = Long.parseLong(matcher.group(1));
		} catch (NumberFormatException e) {
			e.printStackTrace(System.err);
			throw new URISyntaxException(uri.getQuery(), "Collection ID is not of type long");
		}

		return new Collection(id);
	}

	public URI getURI() {
		try {
			return new URI("akonadi", null, null, "collection=" + getId(), null);
		} catch (URISyntaxException e) {
			e.printStackTrace(System.err);
		}

		return null;
	}

	protected static Collection fromResponse(Response response) throws ProtocolException {
		if (response.getEntityId() < 0)
			throw new ProtocolException(response);

		Collection collection = new Collection(response.getEntityId());

		collection.setParentCollection(new Collection(response.getParentId()));

		List<CharSequence> parameters = new ArrayList<CharSequence>(16);
		ParenthesisParser.parseParenthesizedList(response.getResponseMessage(), parameters, 0);

		Iterator<CharSequence> it = parameters.iterator();
		while (it.hasNext()) {
			CharSequence key = it.next();
			if (key.equals("NAME")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection NAME missing in response '"
							+ response.getResponseMessage() + "'");

				collection.setName(it.next().toString());
			} else if (key.equals("RESOURCE")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection RESOURCE missing in response '"
							+ response.getResponseMessage() + "'");

				collection.setResource(it.next().toString());
			} else if (key.equals("REMOTEID")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection REMOTEID missing in response '"
							+ response.getResponseMessage() + "'");

				collection.setRemoteId(it.next().toString());
			} else if (key.equals("MIMETYPE")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection MIMETYPE missing in response '"
							+ response.getResponseMessage() + "'");

				List<CharSequence> mimeTypes = new ArrayList<CharSequence>(5);
				ParenthesisParser.parseParenthesizedList(it.next(), mimeTypes, 0);
				if (mimeTypes.size() > 0) {
					String[] types = new String[mimeTypes.size()];
					Iterator<CharSequence> mimeTypeIt = mimeTypes.iterator();
					for (int index = 0; mimeTypeIt.hasNext(); ++index) {
						types[index] = mimeTypeIt.next().toString();
					}
					collection.setContentMimeTypes(types);
				}
			} else if (key.equals("CACHEPOLICY")) {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection MIMETYPE missing in response '"
							+ response.getResponseMessage() + "'");

				collection.setCachePolicy(CachePolicy.parse(it.next()));
			} else {
				if (!it.hasNext())
					throw new ProtocolException("Value for collection attribute " + key + " missing in response '"
							+ response.getResponseMessage() + "'");

				try {
					Attribute attribute = AttributeFactory.createAttribute(key.toString());
					attribute.parse(it.next());
					collection.addAttribute(attribute);
				} catch (Exception e) {
					e.printStackTrace(System.err);
					throw new ProtocolException(response);
				}
			}
		}

		return collection;
	}

}
