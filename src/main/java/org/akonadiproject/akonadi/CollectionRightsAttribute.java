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

public class CollectionRightsAttribute extends Attribute {
	public CollectionRightsAttribute() {
		this(Collection.READ_ONLY);
	}

	public CollectionRightsAttribute(int rights) {
		mRights = rights;
	}

	public void setRights(int rights) {
		mRights = rights;
	}

	public int getRights() {
		return mRights;
	}

	public String getType() {
		return "AccessRights";
	}

	public void parse(CharSequence input) throws ProtocolException {
		mRights = 0;

		if (input.length() == 0) {
			mRights = Collection.READ_ONLY;
		} else {
			for (int index = 0; index < input.length(); ++index) {
				switch (input.charAt(index)) {
				case 'a':
					mRights |= Collection.ALL_RIGHTS;
					break;
				case 'w':
					mRights |= Collection.CAN_MODIFY_ITEM;
					break;
				case 'c':
					mRights |= Collection.CAN_CREATE_ITEM;
					break;
				case 'd':
					mRights |= Collection.CAN_DELETE_ITEM;
					break;
				case 'W':
					mRights |= Collection.CAN_MODIFY_COLLECTION;
					break;
				case 'C':
					mRights |= Collection.CAN_CREATE_COLLECTION;
					break;
				case 'D':
					mRights |= Collection.CAN_DELETE_COLLECTION;
					break;
				default:
					throw new ProtocolException("Input '" + input
							+ "' is not valid AccessRights attribute data");
				}
			}
		}
	}

	public byte[] serialized() {
		if (mRights == Collection.ALL_RIGHTS) {
			return new byte[] { 'a' };
		}

		StringBuilder buffer = new StringBuilder(6);

		if ((mRights & Collection.CAN_MODIFY_ITEM) != 0) {
			buffer.append('w');
		}
		if ((mRights & Collection.CAN_CREATE_ITEM) != 0) {
			buffer.append('c');
		}
		if ((mRights & Collection.CAN_DELETE_ITEM) != 0) {
			buffer.append('d');
		}
		if ((mRights & Collection.CAN_MODIFY_COLLECTION) != 0) {
			buffer.append('W');
		}
		if ((mRights & Collection.CAN_CREATE_COLLECTION) != 0) {
			buffer.append('C');
		}
		if ((mRights & Collection.CAN_DELETE_COLLECTION) != 0) {
			buffer.append('D');
		}

		return buffer.toString().getBytes();
	}

	public Object clone() {
		return new CollectionRightsAttribute(mRights);
	}

	protected int mRights;
}
