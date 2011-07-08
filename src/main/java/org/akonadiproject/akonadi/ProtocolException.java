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

public class ProtocolException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(Response response) {
		mResponse = response;
	}

	public Response getResponse() {
		return mResponse;
	}

	public String toString() {
		if (mResponse != null) {
			return "org.akonadiproject.akonadi.ProtocolException: " + mResponse.getResponseMessage();
		}

		return super.toString();
	}

	private Response mResponse;
}
