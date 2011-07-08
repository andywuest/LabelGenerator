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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandOutputStream extends FilterOutputStream {

	private static final Logger LOGGER = Logger.getLogger(CommandOutputStream.class.getName());

	private ResponseInputStream mResponseStream;

	CommandOutputStream(OutputStream parentStream, ResponseInputStream responseStream) {
		super(parentStream);
		mResponseStream = responseStream;
	}

	public void writeCommand(Command command) throws ProtocolException, IOException {
		LOGGER.log(Level.INFO, command.toString());
		command.writeToStream(mResponseStream, out);
	}

}
