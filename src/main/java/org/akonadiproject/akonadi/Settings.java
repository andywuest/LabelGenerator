/***************************************************************************
 *   Copyright (C) 2007, 2009 by Kevin Krammer <kevin.krammer@gmx.at>      *
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Settings {

	private File mFile = null;
	private Map<String, String> mValues;

	public Settings(String path) {
		this(new File(path));
	}

	public Settings(File file) {
		mFile = file;

		mValues = new java.util.HashMap<String, String>();

		try {
			parseFile();
		} catch (IOException e) {
			mValues.clear();
			e.printStackTrace(System.err);
		}
	}

	public String getValue(String key, String defaultValue) {
		String result = mValues.get(key);

		if (result == null)
			result = defaultValue;

		return result;
	}

	private void parseFile() throws IOException {
		FileReader fileReader = new FileReader(mFile);
		BufferedReader lineReader = new BufferedReader(fileReader);

		String group = null;
		String[] pair = null;

		for (String line = lineReader.readLine(); line != null; line = lineReader.readLine()) {
			if (line.length() == 0)
				continue;

			int indexOfHash = line.indexOf('#');
			if (indexOfHash != -1) {
				if (indexOfHash < 3)
					continue;

				line = line.substring(0, indexOfHash);
			}

			if (line.indexOf('[') != 0) {
				if (group == null)
					continue;

				if (pair == null)
					pair = new String[2];

				if (parseEntry(line, pair)) {
					mValues.put(group + '/' + pair[0], pair[1]);
				}
			} else {
				group = parseGroup(line);
			}
		}
	}

	private String parseGroup(String line) {
		int indexOfCloseBracket = line.indexOf(']', 1);
		if (indexOfCloseBracket != (line.length() - 1))
			return null;

		return line.substring(1, indexOfCloseBracket);
	}

	private boolean parseEntry(String line, String[] pair) {
		int indexOfEqual = line.indexOf('=');
		if (indexOfEqual < 1 || indexOfEqual == (line.length() - 1))
			return false;

		pair[0] = line.substring(0, indexOfEqual);
		pair[1] = line.substring(indexOfEqual + 1);

		return true;
	}

}
