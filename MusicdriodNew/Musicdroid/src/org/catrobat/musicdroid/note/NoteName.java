/*******************************************************************************
 * Catroid: An on-device visual programming system for Android devices
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
 *  http://www.catroid.org/catroid/licenseadditionalterm
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.catrobat.musicdroid.note;

public enum NoteName {
	// C3 = c'(http://img.docstoccdn.com/thumb/orig/28179105.png)
	C1(36), C1S(37), D1(38), D1S(39), E1(40), F1(41), F1S(42), G1(43), G1S(44), A1(45), A1S(46), B1(47), C2(48), C2S(49), D2(
			50), D2S(51), E2(52), F2(53), F2S(54), G2(55), G2S(56), A2(57), A2S(58), B2(59), C3(60), C3S(61), D3(62), D3S(
			63), E3(64), F3(65), F3S(66), G3(67), G3S(68), A3(69), A3S(70), B3(71), C4(72), C4S(73), D4(74), D4S(75), E4(
			76), F4(77), F4S(78), G4(79), G4S(80), A4(81), A4S(82), B4(83), C5(84), C5S(85), D5(86), D5S(87), E5(88), F5(
			89), F5S(90), G5(91), G5S(92), A5(93), A5S(94), B5(95), C6(96), C6S(97), D6(98), D6S(99), E6(100), F6(101), F6S(
			102), G6(103), G6S(104), A6(105), A6S(106), B6(107);

	private int midi;
	private final static int NUMBER_OF_HALF_TONE_STEPS_PER_OCTAVE = 12;
	private final static int[] SIGNED_HALF_TONE_MODULOS = { 1, 3, 6, 8, 10 };
	public final static int[] FS_OR_CS_MODULOS = { 1, 6 };
	public final static int[] DS_OR_AS_MODULOS = { 3, 10 };

	private NoteName(int midi) {
		this.midi = midi;
	}

	public int getMidi() {
		return midi;
	}

	public NoteName next() {
		int index = this.ordinal() + 1;

		if (index >= values().length) {
			index--;
		}

		return values()[index];
	}

	public NoteName previous() {
		int index = this.ordinal() - 1;

		if (index < 0) {
			index++;
		}

		return values()[index];
	}

	public boolean isSigned() {
		int modValue = midi % NUMBER_OF_HALF_TONE_STEPS_PER_OCTAVE;
		for (int signedModValue : SIGNED_HALF_TONE_MODULOS) {
			if (modValue == signedModValue) {
				return true;
			}
		}
		return false;
	}

	public static int calculateDistance(NoteName name1, NoteName name2) {
		return name2.midi - name1.midi;
	}

	public static NoteName getNoteNameFromMidiValue(int midiValue) {
		NoteName[] noteNames = NoteName.values();

		for (int i = 0; i < noteNames.length; i++) {
			if (noteNames[i].getMidi() == midiValue) {
				return noteNames[i];
			}
		}

		return C3;
	}

	public static int calculateDistanceInHalfNotelineDistances(NoteName name1, NoteName name2) {
		int distance = calculateDistance(name1, name2);

		boolean isDownGoing = distance > 0;

		NoteName smallNote;
		NoteName largeNote;

		if (isDownGoing) {
			smallNote = name1;
			largeNote = name2;
		} else {
			smallNote = name2;
			largeNote = name1;
		}
		int calculatedDistance = 0;
		if (smallNote.getMidi() != largeNote.getMidi()) {

			if (smallNote.isSigned()) {
				calculatedDistance = 1;
			} else if (largeNote.isSigned()) {
				calculatedDistance = -1;
			}
			for (; smallNote.getMidi() != largeNote.getMidi(); smallNote = smallNote.next()) {

				if (!smallNote.isSigned()) {
					calculatedDistance++;
				}
			}
		}
		return (isDownGoing ? calculatedDistance : calculatedDistance * (-1));
	}

	public boolean isBlackKeyWithNoDirectLeftNeighbour() {
		int modValue = midi % NUMBER_OF_HALF_TONE_STEPS_PER_OCTAVE;
		for (int fs_cs_index : FS_OR_CS_MODULOS) {
			if (modValue == fs_cs_index) {
				return true;
			}
		}
		return false;
	}

	public boolean isBlackKeyWithNoDirectRightNeighbour() {
		int modValue = midi % NUMBER_OF_HALF_TONE_STEPS_PER_OCTAVE;
		for (int ds_as_index : DS_OR_AS_MODULOS) {
			if (modValue == ds_as_index) {
				return true;
			}
		}
		return false;
	}

	public static NoteName getNextWhiteKeyNoteName(NoteName noteName) {
		noteName = noteName.next();
		if (noteName.isSigned()) {
			noteName = noteName.next();
		}
		return noteName;
	}

	public static NoteName getNextBlackKeyNoteName(NoteName noteName) {
		do {
			noteName = noteName.next();
		} while (!noteName.isSigned());
		return noteName;
	}

	public static NoteName getFirstNoteOfOctave(int octave) {
		int startNote = 36;
		for (int octaveCount = 1; octaveCount < octave; octaveCount++) {
			startNote += 12;
		}
		return getNoteNameFromMidiValue(startNote);
	}
}
