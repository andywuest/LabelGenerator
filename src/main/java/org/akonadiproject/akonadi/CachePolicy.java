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
import java.util.Iterator;

public class CachePolicy {
	public CachePolicy() {
		mCacheTimeout = -1; // never
		mIntervalCheckTime = -1; // no interval checking
		mSyncOnDemand = false;
		mInheritFromParent = true;
	}

	public void setCacheTimeout(int timeout) {
		mCacheTimeout = timeout;
	}

	public int getCacheTimeout() {
		return mCacheTimeout;
	}

	public void setIntervalCheckTime(int checkTime) {
		mIntervalCheckTime = checkTime;
	}

	public int getIntervalCheckTime() {
		return mIntervalCheckTime;
	}

	public void setSyncOnDemand(boolean syncOnDemand) {
		mSyncOnDemand = syncOnDemand;
	}

	public boolean getSyncOnDemand() {
		return mSyncOnDemand;
	}

	public void setInheritFromParent(boolean inherit) {
		mInheritFromParent = inherit;
	}

	public boolean getInheritFromParent() {
		return mInheritFromParent;
	}

	public void setLocalParts(String[] parts) {
		mLocalParts = parts;
	}

	public String[] getLocalParts() {
		return mLocalParts;
	}

	public static CachePolicy parse(CharSequence input)
			throws ProtocolException {
		ArrayList<CharSequence> parameters = new ArrayList<CharSequence>(10);
		ParenthesisParser.parseParenthesizedList(input, parameters, 0);

		CachePolicy cachePolicy = new CachePolicy();

		Iterator<CharSequence> it = parameters.iterator();
		while (it.hasNext()) {
			CharSequence key = it.next();
			if (key.equals("INHERIT")) {
				if (!it.hasNext())
					throw new ProtocolException(
							"Value for cache policy INHERIT missing in response '"
									+ input + "'");

				CharSequence value = it.next();
				if (value.equals("false")) {
					cachePolicy.setInheritFromParent(false);
				} else if (!value.equals("true")) {
					throw new ProtocolException(
							"Value for cache policy INHERIT is neither true nor false but '"
									+ value + "'");
				}
			} else if (key.equals("INTERVAL")) {
				if (!it.hasNext())
					throw new ProtocolException(
							"Value for cache policy INTERVAL missing in response '"
									+ input + "'");

				CharSequence value = it.next();
				try {
					int interval = Integer.parseInt(value.toString());
					if (interval < -1)
						interval = -1;

					cachePolicy.setIntervalCheckTime(interval);
				} catch (NumberFormatException e) {
					e.printStackTrace(System.err);
					throw new ProtocolException(
							"Value for cache policy INTERVAL is not an integer but '"
									+ value + "'");
				}
			} else if (key.equals("CACHETIMEOUT")) {
				if (!it.hasNext())
					throw new ProtocolException(
							"Value for cache policy CACHETIMEOUT missing in response '"
									+ input + "'");

				CharSequence value = it.next();
				try {
					int timeout = Integer.parseInt(value.toString());
					if (timeout < -1)
						timeout = -1;

					cachePolicy.setCacheTimeout(timeout);
				} catch (NumberFormatException e) {
					e.printStackTrace(System.err);
					throw new ProtocolException(
							"Value for cache policy CACHETIMEOUT is not an integer but '"
									+ value + "'");
				}
			} else if (key.equals("SYNCONDEMAND")) {
				if (!it.hasNext())
					throw new ProtocolException(
							"Value for cache policy SYNCONDEMAND missing in response '"
									+ input + "'");

				CharSequence value = it.next();
				if (value.equals("true")) {
					cachePolicy.setSyncOnDemand(true);
				} else if (!value.equals("false")) {
					throw new ProtocolException(
							"Value for cache policy SYNCONDEMAND is neither true nor false but '"
									+ value + "'");
				}
			} else if (key.equals("LOCALPARTS")) {
				if (!it.hasNext())
					throw new ProtocolException(
							"Value for cache policy LOCALPARTS missing in response '"
									+ input + "'");

				ArrayList<CharSequence> partList = new ArrayList<CharSequence>(
						1);
				ParenthesisParser
						.parseParenthesizedList(it.next(), partList, 0);
				if (partList.size() > 0) {
					String[] parts = new String[partList.size()];
					Iterator<CharSequence> partListIt = partList.iterator();
					for (int index = 0; partListIt.hasNext(); ++index) {
						parts[index] = partListIt.next().toString();
					}
					cachePolicy.setLocalParts(parts);
				}
			}
		}

		return cachePolicy;
	}

	private int mCacheTimeout;
	private int mIntervalCheckTime;
	private boolean mSyncOnDemand;
	private boolean mInheritFromParent;
	private String[] mLocalParts;
}
