package at.tugraz.ist.musicdroid;

import java.io.File;
import java.io.IOException;
import java.lang.Object;
import java.util.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import at.tugraz.ist.musicdroid.common.Constants;

public class PianoActivity extends Activity implements OnTouchListener {
	private HorizontalScrollView scroll;
	private ImageView piano;

	private DrawTonesView toneView;
	private Button playMidi;
	private Button stopMidi;

	private MidiPlayer midiplayer;
	private NoteMapper mapper;
	private SoundPlayer soundplayer;
	static final int SEMIQUAVER = 4;
	static final int QUAVER = 8;
	static final int CROTCHET = 16;
	static final int MINIM = 32;
	static final int SEMIBREVE = 64;
	private String ActivityName = "PianoActivity";
	private Context context;
	private boolean[] buttonStates = null;
	private ArrayList<Integer> midi_values;
	private int sizeofMidiValues = 0;

	private boolean[] newButtonStates = new boolean[Constants.NUMBER_PIANO_BUTTONS];
	ProgressDialog progress;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.piano);
		context = this.getApplicationContext();
		soundplayer = new SoundPlayer(context);
		progress = ProgressDialog.show(this, ActivityName,
				getString(R.string.piano_loading_sounds), true);
		new Thread(new Runnable() {
			public void run() {
				soundplayer.initSoundpool();
				createMidiSounds();
				runOnUiThread(new Runnable() {
					public void run() {
						progress.dismiss();
					}
				});
			}
		}).start();

		init();
		piano.setOnTouchListener(this);
		buttonStates = new boolean[Constants.NUMBER_PIANO_BUTTONS];
		midi_values = new ArrayList<Integer>(12);

	}

	// @Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction();
		boolean isDownAction = (action & 0x5) == 0x5
				|| action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE;
		int mapped_key = 0;
		int y = piano.getHeight();
		for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {

			if (event.getY(touchIndex) > 2 * y / 3) {
				int index = 0;
				mapped_key = mapper.getWhiteKeyFromPosition(round(event
						.getX(touchIndex)));
				index = mapped_key - 35;
				newButtonStates[index] = isDownAction;

			} else {
				mapped_key = mapper.getBlackKeyFromPosition(round(event
						.getX(touchIndex)));
				if (mapped_key >= 0) {
					int index = 0;
					index = mapped_key - 35;
					newButtonStates[index] = isDownAction;

				} else {
					Log.e("nokey", String.valueOf(mapped_key));
				}
			}

			if (touchIndex == (event.getPointerCount() - 1)) {
				if ((isDownAction == true) & event.getPointerCount() > 1) {
					int toneSize = toneView.getTonesSize();
					toneView.deleteElement(toneSize - 1);
				}
				organizeAction();
			}

		}

		return true;
	}

	private void organizeAction() {
		midi_values.clear();
		for (int index = 0; index < newButtonStates.length; index++) {
			if (newButtonStates[index] == true) {
				midi_values.add(index + 35);
			}
		}
		sizeofMidiValues = midi_values.size();
		Log.e("organizeAction", "" + midi_values.size());
		for (int i = 0; i < sizeofMidiValues; i++) {
			Log.d("Value " + i, " " + midi_values.get(i));
		}

		for (int index = 0; index < newButtonStates.length; index++) {

			if (buttonStates[index] != newButtonStates[index]) {
				int midivalue = 0;
				buttonStates[index] = newButtonStates[index];
				midivalue = index + 35;
				toggleSound(midivalue, newButtonStates[index]);
			}

		}
		newButtonStates = new boolean[Constants.NUMBER_PIANO_BUTTONS];

	}

	@SuppressWarnings("unchecked")
	private void toggleSound(int midivalue, boolean down) {

		if (down) {
			if (!soundplayer.isNotePlaying(midivalue)) {
				soundplayer.playNote(midivalue);

				Object test = new ArrayList<Integer>(12);
				test = midi_values.clone();
				toneView.addElement((ArrayList<Integer>) test);

			}
		} else {
			soundplayer.stopNote(midivalue);

		}
	}

	@SuppressWarnings("deprecation")
	private void init() {
		scroll = (HorizontalScrollView) findViewById(R.id.scrollView);
		piano = (ImageView) findViewById(R.id.piano);
		stopMidi = (Button) findViewById(R.id.StopMidiButton);
		stopMidi.setEnabled(false);
		playMidi = (Button) findViewById(R.id.PlayMidiButton);
		scroll.setId(2);

		LinearLayout layout = (LinearLayout) findViewById(R.id.parentLayout);

		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		int width = display.getWidth();
		layout.setLayoutParams(new RelativeLayout.LayoutParams(width,
				height / 2));

		int radius = getResources().getInteger(R.integer.radius);
		int topline = getResources().getInteger(R.integer.topmarginlines);
		toneView = new DrawTonesView(this, R.drawable.violine, radius, topline,
				true);
		toneView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 280));
		layout.addView(toneView, 0);
		midiplayer = new MidiPlayer(toneView, context);

		mapper = new NoteMapper();

		scroll.postDelayed(new Runnable() {
			public void run() {
				int x = piano.getWidth();
				float white_key_width = (x / (float) 35);
				float position = white_key_width * 10;

				float black_key_width = (x / (float) 60.0);
				String pos = "" + x;
				Log.e("position", pos);

				scroll.scrollTo(round(position), 0);
				mapper.initializeWhiteKeyMap(white_key_width);
				mapper.initializeBlackKeyMap(black_key_width);
			}
		}, 100L);

		playMidi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				midiplayer.writeToMidiFile();
				midiplayer.playMidiFile();
				stopMidi.setEnabled(true);

			}

		});

		stopMidi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				midiplayer.stopMidiPlayer();

			}

		});

	}

	private int round(float val) {
		val += 0.5;
		return (int) val;
	}

	private String getMidiNote(int value) {
		switch (value) {
		case 0:
			return "C";
		case 1:
			return "Cis";
		case 2:
			return "D";
		case 3:
			return "Dis";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "Fis";
		case 7:
			return "G";
		case 8:
			return "Gis";
		case 9:
			return "A";
		case 10:
			return "Ais";
		case 11:
			return "H";

		}

		return "W";

	}

	private boolean createMidiSounds() {
		boolean success = false;
		File directory;
		String midi_note;
		String file_name;
		String path;
		int midi_value = 0;
		int counter = 0;
		directory = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "records" + File.separator
				+ "piano_midi_sounds");
		if (!directory.exists()) {

			directory.mkdirs();
		}
		counter = 0;
		for (midi_value = 36; midi_value < 96; midi_value++) {

			midi_note = getMidiNote(counter);
			file_name = midi_note + "_" + midi_value + ".mid";
			path = directory.getAbsolutePath() + File.separator + file_name;
			File file = new File(path);
			if (!file.exists()) {

				MidiFile midiFile = new MidiFile();
				midiFile.noteOn(0, midi_value, 127);
				midiFile.noteOff(SEMIBREVE, midi_value);

				try {
					midiFile.writeToFile(path);
				} catch (IOException e) {

					finish();
				}
			} else {
				// at the moment, do nothing
			}

			counter++;
			if (counter == 12)
				counter = 0;

			soundplayer.setSoundpool(path);
		}
		return success;
	}

}
