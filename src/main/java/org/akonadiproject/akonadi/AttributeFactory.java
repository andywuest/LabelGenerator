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

import org.akonadiproject.akonadi.Attribute;
import org.akonadiproject.akonadi.CollectionRightsAttribute;
import org.akonadiproject.akonadi.EntityDisplayAttribute;
import org.akonadiproject.akonadi.ProtocolException;

import java.util.HashMap;

public class AttributeFactory {
	public static void registerAttribute(Attribute attribute) {
		synchronized (mInstance) {
			if (!mInstance.mPrototypes.containsKey(attribute.getType())) {
				mInstance.mPrototypes.put(attribute.getType(), attribute);
			}
		}
	}

	public static Attribute createAttribute(String type) {
		synchronized (mInstance) {
			Attribute prototype = mInstance.mPrototypes.get(type);
			if (prototype == null)
				return mInstance.createDefaultAttribute(type);

			return (Attribute) prototype.clone();
		}
	}

	private AttributeFactory() {
		mPrototypes = new HashMap<String, Attribute>();

		Attribute attribute = new CollectionRightsAttribute();
		mPrototypes.put(attribute.getType(), attribute);

		attribute = new EntityDisplayAttribute();
		mPrototypes.put(attribute.getType(), attribute);
	}

	private DefaultAttribute createDefaultAttribute(String type) {
		return new DefaultAttribute(type);
	}

	private HashMap<String, Attribute> mPrototypes;

	private static AttributeFactory mInstance = new AttributeFactory();

	private class DefaultAttribute extends Attribute {
		public DefaultAttribute(String type) {
			mType = type;
		}

		public String getType() {
			return mType;
		}

		public void parse(CharSequence input) throws ProtocolException {
			mData = input.toString();
		}

		public byte[] serialized() {
			return mData.getBytes();
		}

		public Object clone() {
			DefaultAttribute attribute = new DefaultAttribute(mType);
			attribute.mData = mData;
			return attribute;
		}

		private String mType;
		private String mData;
	}
}
