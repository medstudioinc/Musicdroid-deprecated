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
package org.catrobat.musicdroid.note;

import org.catrobat.musicdroid.note.symbol.AbstractSymbol;
import org.catrobat.musicdroid.note.symbol.BreakSymbol;
import org.catrobat.musicdroid.note.symbol.NoteSymbol;

import java.util.LinkedList;
import java.util.List;

public class MockDataFactory {

	private static final String PROJECT_NAME = "TestMidi";

	private MockDataFactory() {
	}

	public static Project createProject() {
		Project project = new Project(PROJECT_NAME, 120);
		Track track1 = createTrack(Instrument.GUNSHOT);
		Track track2 = createTrack(Instrument.WHISTLE);

		project.addTrack(track1);
		project.addTrack(track2);

		return project;
	}

	public static Track createTrack(Instrument instrument) {
		Track track = new Track(instrument);

		long tick = 0;

		track.addNoteEvent(tick, new NoteEvent(NoteName.C2, true));

		long quarterDuration = NoteLength.QUARTER.getTickDuration();
		tick += quarterDuration;

		track.addNoteEvent(tick, new NoteEvent(NoteName.C2, false));
		track.addNoteEvent(tick, new NoteEvent(NoteName.C1, true));
		track.addNoteEvent(tick, new NoteEvent(NoteName.D1, true));

		tick += quarterDuration;

		track.addNoteEvent(tick, new NoteEvent(NoteName.C1, false));
		track.addNoteEvent(tick, new NoteEvent(NoteName.D1, false));

		long breakDuration = quarterDuration;
		tick += breakDuration;

		track.addNoteEvent(tick, new NoteEvent(NoteName.E1, true));
		track.addNoteEvent(tick, new NoteEvent(NoteName.F1, true));

		long quarterEightDuration = quarterDuration + NoteLength.SIXTEENTH.getTickDuration();
		tick += quarterEightDuration;

		track.addNoteEvent(tick, new NoteEvent(NoteName.E1, false));
		track.addNoteEvent(tick, new NoteEvent(NoteName.F1, false));

		return track;
	}

	public static List<AbstractSymbol> createSymbols() {
		List<AbstractSymbol> symbols = new LinkedList<AbstractSymbol>();

		NoteSymbol symbol1 = new NoteSymbol(new NoteLength[] { NoteLength.QUARTER }, new NoteName[] { NoteName.C2 });
		NoteSymbol symbol2 = new NoteSymbol(new NoteLength[] { NoteLength.QUARTER }, new NoteName[] { NoteName.C1,
				NoteName.D1 });
		BreakSymbol symbol3 = new BreakSymbol(new NoteLength[] { NoteLength.QUARTER });
		NoteSymbol symbol4 = new NoteSymbol(new NoteLength[] { NoteLength.QUARTER, NoteLength.SIXTEENTH },
				new NoteName[] { NoteName.E1, NoteName.F1 });

		symbols.add(symbol1);
		symbols.add(symbol2);
		symbols.add(symbol3);
		symbols.add(symbol4);

		return symbols;
	}
}