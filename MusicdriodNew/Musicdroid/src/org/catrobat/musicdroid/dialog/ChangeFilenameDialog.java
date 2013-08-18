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
package org.catrobat.musicdroid.dialog;

import org.catrobat.musicdroid.R;
import org.catrobat.musicdroid.recorder.AudioHandler;
import org.catrobat.musicdroid.tools.FileExtensionMethods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ChangeFilenameDialog extends DialogFragment {
	private static final String TAG = ChangeFilenameDialog.class.getSimpleName();
	private EditText editText = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.change_filename_menu, null);
		builder.setView(view);

		builder.setTitle(R.string.dialog_change_filename_title)
				.setNegativeButton(R.string.settings_button_apply,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AudioHandler.getInstance().setFilename(
										editText.getText().toString() + ".mp3");
							}
						})
				.setPositiveButton(R.string.settings_button_discard,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		AlertDialog dialog = builder.create();

		String filename = AudioHandler.getInstance().getFilename();
		editText = (EditText) view.findViewById(R.id.dialog_edittext);
		editText.setText(FileExtensionMethods.removeFileEnding(filename));

		return dialog;
	}
}
