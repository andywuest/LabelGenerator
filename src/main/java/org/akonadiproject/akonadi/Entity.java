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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Entity {

	public void setRemoteId(String remoteId) {
		mRemoteId = remoteId;
	}

	public String getRemoteId() {
		return mRemoteId;
	}

	public boolean isValid() {
		return mId >= 0;
	}

	public long getId() {
		return mId;
	}

	public boolean hasAttribute(String type) {
		if (mAttributes != null) {
			return mAttributes.containsKey(type);
		}

		return false;
	}

	public void addAttribute(Attribute attribute) {
		if (mAttributes == null) {
			mAttributes = new HashMap<String, Attribute>();
		}

		mAttributes.put(attribute.getType(), attribute);
	}

	public void removeAttribute(String type) {
		if (mAttributes != null) {
			mAttributes.remove(type);
		}
	}

	public List<Attribute> getAllAttributes() {
		if (mAttributes != null) {
			List<Attribute> list = new LinkedList<Attribute>();
			list.addAll(mAttributes.values());
			return list;
		}

		return null;
	}

	public Attribute getAttribute(String type) {
		if (mAttributes != null) {
			return mAttributes.get(type);
		}

		return null;
	}

	public <T extends Attribute> T getOrCreateAttribute(Class classT) throws InstantiationException,
			IllegalAccessException {
		T t = (T) classT.newInstance();

		if (hasAttribute(t.getType())) {
			return (T) getAttribute(t.getType());
		}

		addAttribute(t);
		return t;
	}

	protected Entity() {
		mId = -1;
	}

	protected Entity(long id) {
		mId = id;
	}

	protected long mId;
	protected String mRemoteId;

	protected HashMap<String, Attribute> mAttributes;
}
