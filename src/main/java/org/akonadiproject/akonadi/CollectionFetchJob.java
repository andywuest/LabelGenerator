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

public class CollectionFetchJob extends AbstractJob {
	public enum Mode {
		Base("X-AKLIST", "0"), FirstLevel("X-AKLIST", "1"), FirstLevelOnlySubscribed(
				"X-AKLSUB", "1"), Recursive("X-AKLIST", "INF"), RecursiveOnlySubscribed(
				"X-AKLSUB", "INF");

		Mode(String command, String depth) {
			mCommand = command;
			mParameters = new Object[3];
			mParameters[1] = depth;
			mParameters[2] = new char[] { '(', ')' };
		}

		public String getCommand() {
			return mCommand;
		}

		public Object[] getParameters() {
			return mParameters;
		}

		private String mCommand;
		private Object[] mParameters;
	}

	public CollectionFetchJob(Collection collection)
			throws IllegalArgumentException {
		this(collection, Mode.Base, null);
	}

	public CollectionFetchJob(Collection collection, Mode mode)
			throws IllegalArgumentException {
		this(collection, mode, null);
	}

	public CollectionFetchJob(Collection collection, Mode mode, Session session)
			throws IllegalArgumentException {
		super(session);

		if (!collection.isValid())
			throw new IllegalArgumentException("Collection is not valid");

		mMode = mode;
		mCollection = collection;
	}

	public List<Collection> getFetchedCollections() {
		return mFetchedCollections;
	}

	public void addCollectionFetchListener(CollectionFetchListener listener) {
		if (mFetchListeners == null) {
			mFetchListeners = new HashSet<CollectionFetchListener>();
		}

		mFetchListeners.add(listener);
	}

	public void removeCollectionFetchListener(CollectionFetchListener listener) {
		mFetchListeners.remove(listener);
	}

	protected void doRun() throws Exception {
		synchronized (mSession) {
			Object[] parameters = mMode.getParameters();
			parameters[0] = new Long(mCollection.getId());
			Command command = new Command(mSession.getNextTag(), mMode
					.getCommand(), parameters);

			mSession.getCommandOutputStream().writeCommand(command);

			ResponseInputStream in = mSession.getResponseInputStream();
			Response response = in.readResponse();
			while (!response.isTagged()) {
				try {
					Collection collection = Collection.fromResponse(response);
					if (collection.isValid()) {
						if (mFetchedCollections == null) {
							mFetchedCollections = new LinkedList<Collection>();
						}

						mFetchedCollections.add(collection);
						notifyFetchListeners(collection);
					}
				} catch (ProtocolException e) {
					e.printStackTrace(System.err);
				}

				response = in.readResponse();
			}

			if (!response.isOK())
				throw new ProtocolException(response);
		}
	}

	protected void notifyFetchListeners(Collection collection) {
		if (mFetchListeners != null) {
			Iterator<CollectionFetchListener> it = mFetchListeners.iterator();
			while (it.hasNext()) {
				it.next().collectionFetched(collection);
			}
		}
	}

	protected Mode mMode;
	protected Collection mCollection;

	protected List<Collection> mFetchedCollections;
	protected Set<CollectionFetchListener> mFetchListeners;
}
