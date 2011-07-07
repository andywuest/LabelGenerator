/***************************************************************************
 *   Copyright (C) 2009 by Kevin Krammer <kevin.krammer@gmx.at>            *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
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

import org.akonadiproject.akonadi.Collection;
import org.akonadiproject.akonadi.CollectionFetchJob;
import org.akonadiproject.akonadi.Item;
import org.akonadiproject.akonadi.ItemFetchJob;
import org.akonadiproject.akonadi.Session;

import java.util.Iterator;
import java.util.List;

public class Walk {
	public Walk() {
	}

	public void walkSubTree(Collection collection, String prefix)
			throws Exception {
		CollectionFetchJob job = new CollectionFetchJob(collection,
				CollectionFetchJob.Mode.FirstLevel);

		job.run();

		if (job.getException() != null)
			throw job.getException();

		List<Collection> collections = job.getFetchedCollections();
		if (collections != null) {
			String nextPrefix = prefix + "    ";
			String itemPrefix = prefix + "      ";
			Iterator<Collection> it = collections.iterator();
			while (it.hasNext()) {
				Collection col = it.next();
				printCollection(col, prefix);

				walkItems(col, itemPrefix);

				System.out.println();

				walkSubTree(col, nextPrefix);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Session.createDefaultSession("Akonadi-Java-Walk");
		Walk walk = new Walk();
		walk.walkSubTree(Collection.ROOT, "");
	}

	private void walkItems(Collection collection, String prefix)
			throws Exception {
		ItemFetchJob job = new ItemFetchJob(collection);
		job.run();

		if (job.getException() != null)
			throw job.getException();

		List<Item> items = job.getFetchedItems();
		if (items != null) {
			Iterator<Item> it = items.iterator();
			while (it.hasNext()) {
				printItem(it.next(), prefix);
			}
		}
	}

	private void printCollection(Collection collection, String prefix) {
		String name = collection.getName();
		try {
			EntityDisplayAttribute attribute = collection
					.getOrCreateAttribute(EntityDisplayAttribute.class);
			if (attribute.getDisplayName() != null) {
				name = attribute.getDisplayName();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		System.out.println(prefix + name);
		System.out.println(prefix + "    " + collection.getId() + " ("
				+ collection.getRemoteId() + ")");

		String[] mimeTypes = collection.getContentMimeTypes();
		if (mimeTypes != null) {
			for (int index = 0; index < mimeTypes.length; ++index) {
				System.out.println(prefix + "    " + mimeTypes[index]);
			}
		}
	}

	private void printItem(Item item, String prefix) {
		System.out.println(prefix + item.getId() + " (" + item.getRemoteId()
				+ ") " + item.getMimeType());
	}
}