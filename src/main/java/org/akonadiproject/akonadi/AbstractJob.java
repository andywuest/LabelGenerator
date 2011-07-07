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
import java.util.Set;

public abstract class AbstractJob {

	protected Session mSession;

	protected Exception mException;

	protected Set<JobResultListener> mResultListeners;

	public AbstractJob(Session session) {
		mSession = session;
		if (mSession == null) {
			mSession = Session.getDefaultSession();
		}
	}

	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			mException = e;
		}
		notifyResultListeners();
	}

	public Exception getException() {
		return mException;
	}

	public void addJobResultListener(JobResultListener listener) {
		if (mResultListeners == null) {
			mResultListeners = new HashSet<JobResultListener>();
		}

		mResultListeners.add(listener);
	}

	public void removeJobResultListener(JobResultListener listener) {
		mResultListeners.remove(listener);
	}

	protected abstract void doRun() throws Exception;

	protected void notifyResultListeners() {
		if (mResultListeners != null) {
			Iterator<JobResultListener> it = mResultListeners.iterator();
			while (it.hasNext()) {
				JobResultListener listener = it.next();
				listener.result(this);
			}
		}
	}

}
