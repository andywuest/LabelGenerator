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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ItemFetchJob extends AbstractJob {
	
	public ItemFetchJob(Collection collection) throws IllegalArgumentException {
		this(collection, null);
	}

	public ItemFetchJob(Collection collection, Session session) throws IllegalArgumentException {
		super(session);

		if (!collection.isValid())
			throw new IllegalArgumentException("Collection is not valid");

		mCollection = collection;
	}

	public List<Item> getFetchedItems() {
		return mFetchedItems;
	}

	public void addItemFetchListener(ItemFetchListener listener) {
		if (mFetchListeners == null) {
			mFetchListeners = new HashSet<ItemFetchListener>();
		}

		mFetchListeners.add(listener);
	}

	public void removeItemFetchListener(ItemFetchListener listener) {
		mFetchListeners.remove(listener);
	}

	protected void doRun() throws Exception {
		synchronized (mSession) {
			Command select = new Command(mSession.getNextTag(), "SELECT", new Object[] { "SILENT",
					new Long(mCollection.getId()) });

			CommandOutputStream out = mSession.getCommandOutputStream();
			ResponseInputStream in = mSession.getResponseInputStream();

			out.writeCommand(select);
			Response response = in.readResponse();
			if (!response.isOK())
				throw new ProtocolException(response);

			Command fetchAll = new Command(mSession.getNextTag(), "FETCH", new Object[] { new char[] { '1', ':', '*',
					' ', '(', ')' } });
			out.writeCommand(fetchAll);

			response = in.readResponse();
			while (!response.isTagged()) {
				Item item = Item.fromResponse(response);

				if (mFetchedItems == null) {
					mFetchedItems = new LinkedList<Item>();
				}

				mFetchedItems.add(item);
				notifyFetchListeners(item);

				response = in.readResponse();
			}

			if (!response.isOK())
				throw new ProtocolException(response);
		}
	}

	protected void notifyFetchListeners(Item item) {
		if (mFetchListeners != null) {
			Iterator<ItemFetchListener> it = mFetchListeners.iterator();
			while (it.hasNext()) {
				it.next().itemFetched(item);
			}
		}
	}

	protected Collection mCollection;

	protected List<Item> mFetchedItems;
	protected Set<ItemFetchListener> mFetchListeners;
}
