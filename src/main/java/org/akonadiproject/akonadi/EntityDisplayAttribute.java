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

import java.util.ArrayList;
import java.util.List;

public class EntityDisplayAttribute extends Attribute {

	protected String mDisplayName;
	protected String mIconName;

	public EntityDisplayAttribute() {
	}

	public EntityDisplayAttribute(String displayName, String iconName) {
		mDisplayName = displayName;
		mIconName = iconName;
	}

	public void setDisplayName(String displayName) {
		mDisplayName = displayName;
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public void setIconName(String iconName) {
		mIconName = iconName;
	}

	public String getIconName() {
		return mIconName;
	}

	public String getType() {
		return "ENTITYDISPLAY";
	}

	public void parse(CharSequence input) throws ProtocolException {
		// FIXME: needs to be treated correctly in ParenthesisParser
		final StringBuilder builder = new StringBuilder(input);
		int i = 1;
		while (i < builder.length()) {
			switch (builder.charAt(i)) {
			case '\\':
			case '"':
			case ')':
			case '(':
				if (builder.charAt(i - 1) == '\\') {
					builder.delete(i - 1, i);
				} else {
					++i;
				}
				break;
			default:
				++i;
				break;
			}
		}

		List<CharSequence> names = new ArrayList<CharSequence>(4);
		ParenthesisParser.parseParenthesizedList(builder, names, 0);
		if (names.size() != 4)
			throw new ProtocolException("Value for attribute ENTITYDISPLAY is not a list of two strings, but "
					+ names.size() + ": input='" + input + "'");

		setDisplayName(names.get(0).toString());
		setIconName(names.get(1).toString());
	}

	public byte[] serialized() {
		return null;
	}

	public Object clone() {
		return new EntityDisplayAttribute(mDisplayName, mIconName);
	}

}
