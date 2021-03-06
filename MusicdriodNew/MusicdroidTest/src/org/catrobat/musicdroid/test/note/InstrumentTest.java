/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.musicdroid.test.note;

import android.test.AndroidTestCase;

import org.catrobat.musicdroid.note.Instrument;

public class InstrumentTest extends AndroidTestCase {

	public void testGetInstrumentFromProgram1() {
		Instrument expectedInstrument = Instrument.ACOUSTIC_GRAND_PIANO;
		assertGetInstrumentFromProgram(expectedInstrument.getProgram(), expectedInstrument);
	}

	public void testGetInstrumentFromProgram2() {
		Instrument expectedInstrument = Instrument.LEAD_5_CHARANG;
		assertGetInstrumentFromProgram(expectedInstrument.getProgram(), expectedInstrument);
	}

	public void testGetInstrumentFromProgram3() {
		Instrument expectedInstrument = Instrument.GUNSHOT;
		assertGetInstrumentFromProgram(expectedInstrument.getProgram(), expectedInstrument);
	}

	public void testGetInstrumentFromProgram4() {
		int invalidProgram = 128;
		assertGetInstrumentFromProgram(invalidProgram, Instrument.ACOUSTIC_GRAND_PIANO);
	}

	private void assertGetInstrumentFromProgram(int program, Instrument expectedInstrument) {
		Instrument actualInstrument = Instrument.getInstrumentFromProgram(program);
		assertEquals(expectedInstrument, actualInstrument);
	}
}
