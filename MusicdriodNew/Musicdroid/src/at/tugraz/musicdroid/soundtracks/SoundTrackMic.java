package at.tugraz.musicdroid.soundtracks;

import at.tugraz.musicdroid.R;
import at.tugraz.musicdroid.SoundManager;
import at.tugraz.musicdroid.types.SoundType;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SoundTrackMic extends SoundTrack {
	
	public SoundTrackMic() {
		type = SoundType.MIC;
		name = "SoundfileMic";
		soundfileRawId = R.raw.test_wav;
		duration =  SoundManager.getSoundfileDuration(soundfileRawId);
		soundpoolId = SoundManager.loadSound(soundfileRawId);
	}
	
	public SoundTrackMic(SoundTrackMic stm)
	{
		Log.e("Calling copy constr", "mic");
	}
	/*
	private void init()
	{
        setBackgroundColor(getResources().getColor(R.color.sound_view_mic_color));
        super.setRessources(type.getImageResource(), name);
	} */

}