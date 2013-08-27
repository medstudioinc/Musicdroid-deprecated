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
package org.catrobat.musicdroid.recorder;

import android.os.Environment;

/**
 * @author matthias schlesinger
 * 
 */
public class RecordingSession {
	private String path = null;
	private String filename = null;

	public RecordingSession() {
		this.path = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.filename = "test.mp3";
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPathToFile() {
		return path + "/" + filename;
	}

	public String getFilename() {
		return filename;
	}

	public String getPath() {
		return path;
	}

}
