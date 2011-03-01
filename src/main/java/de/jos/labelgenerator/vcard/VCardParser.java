package de.jos.labelgenerator.vcard;

import info.ineighborhood.cardme.engine.VCardEngine;
import info.ineighborhood.cardme.io.CompatibilityMode;
import info.ineighborhood.cardme.io.VCardWriter;
import info.ineighborhood.cardme.vcard.VCard;
import info.ineighborhood.cardme.vcard.VCardImpl;
import info.ineighborhood.cardme.vcard.features.NameFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Copyright (c) 2004, Neighborhood Technologies
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Neighborhood Technologies nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * 
 * @author George El-Haddad
 * 
 *         Mar 10, 2010
 * 
 *         A Test class to see how to use the VCardEngine.
 * 
 */
public class VCardParser {

	private File[] vcardFiles = null;
	private VCardEngine vcardEngine = null;

	/**
	 * Creates a new TestParser.
	 */
	public VCardParser() {
		vcardEngine = new VCardEngine();
	}

	/**
	 * Sets the compatibility mode.
	 * 
	 * @param compatMode
	 */
	public void setCompatibilityMode(CompatibilityMode compatMode) {
		vcardEngine.setCompatibilityMode(compatMode);
	}

	/**
	 * "C:\Users\andy\workspace\labelgenerator\src\main\resources\vcards\_Vision
	 * iT media GmbH.vcf"
	 * 
	 * @param fileName
	 * @return
	 */
	public VCard getVCard(String fileName) {
		VCard vcard = null;
		try {
			vcard = vcardEngine.parse(new File(fileName));
		} catch (IOException ioe) {
			System.err.println("Could not read vcard file: " + fileName);
			ioe.printStackTrace();
		}
		return vcard;
	}

	/**
	 * Retrieve all VCard files and then parse them.
	 * 
	 * @return {@link List}<VCard>
	 */
	public List importVCards() {
		List vcards = new ArrayList();
		vcardFiles = getFiles();
		for (int i = 0; i < vcardFiles.length; i++) {
			try {
				VCard vcard = vcardEngine.parse(vcardFiles[i]);
				vcards.add(vcard);
			} catch (IOException ioe) {
				System.err.println("Could not read vcard file: " + vcardFiles[i].getAbsolutePath());
				ioe.printStackTrace();
			}
		}

		return vcards;
	}

	/**
	 * Opens a file chooser dialog to select VCard files.
	 * 
	 * @return {@link File}[]
	 */
	private File[] getFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select VCards");
		chooser.setCurrentDirectory(new File(System.getProperties().getProperty("user.home")));
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".vcf") || f.isDirectory();
			}

			public @Override
			String getDescription() {

				return "VCard Files";
			}
		});

		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.CANCEL_OPTION) {
			return null;
		}

		try {
			File[] files = chooser.getSelectedFiles(); // get the file
			return files;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Warning! Could not load the file(s)!", "Warning!",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	/**
	 * This is the main method. Here the TestParses in created and initialized.
	 * A VCardWriter is created to write the imported vcards to the System.out
	 * so we can see if everything got imported and written correctly.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		VCardParser testParser = new VCardParser();
		testParser.setCompatibilityMode(CompatibilityMode.RFC2426);
		List vcards = testParser.importVCards();
		VCardWriter writer = new VCardWriter();

		for (int i = 0; i < vcards.size(); i++) {
			VCardImpl vcard = (VCardImpl) vcards.get(i);
			writer.setVCard(vcard);
			String vstring = writer.buildVCardString();

			NameFeature name = vcard.getName();
			System.out.println(name.getFamilyName());

			if (writer.hasErrors()) {
				List errors = vcard.getErrors();
				for (int j = 0; j < errors.size(); j++) {
					System.out.println(errors.get(j));
				}
			}

			System.out.println(vstring);
		}

		System.out.println("\n-- END --");
	}
}