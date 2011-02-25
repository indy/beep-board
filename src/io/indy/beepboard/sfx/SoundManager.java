package io.indy.beepboard.sfx;

import android.util.Log;
import java.util.Comparator;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.Context;

public class SoundManager
{
    private static final String TAG = "SoundManager";

    private static final int MAX_STREAMS = 8;
    private static final int MAX_SOUNDS = 32;
    private static final SoundComparator sSoundComparator = new SoundComparator();

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MUSIC = 3;

    private Context mContext;
    private SoundPool mSoundPool;
    private FixedSizeArray<Sound> mSounds;
    private Sound mSearchDummy;
    private boolean mSoundEnabled;


    public SoundManager(Context context)
    {
        mContext = context;
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSounds = new FixedSizeArray<Sound>(MAX_SOUNDS, sSoundComparator);
        mSearchDummy = new Sound();
    }

    public void reset() {
        mSoundPool.release();
        mSounds.clear();
        mSoundEnabled = true;
    }

    public Sound load(int resource) {
        final int index = findSound(resource);
        Sound result = null;
        if (index < 0) {
            // new sound.
            result = new Sound();
            result.resource = resource;
            result.soundId = mSoundPool.load(mContext, resource, 1);
            mSounds.add(result);
            mSounds.sort(false);
        } else {
            result = mSounds.get(index);
        }
        return result;
    }

    synchronized public final int play(Sound sound, boolean loop, int priority) {
    	int stream = -1;
    	if (mSoundEnabled) {
    		stream = mSoundPool.play(sound.soundId, 1.0f, 1.0f, priority, loop ? -1 : 0, 1.0f);
    	}
    	return stream;
    }

    synchronized public final int play(Sound sound, boolean loop, int priority, float volume, float rate) {
    	int stream = -1;
    	if (mSoundEnabled) {
    		stream = mSoundPool.play(sound.soundId, volume, volume, priority, loop ? -1 : 0, rate);
    	}

    	return stream;
    }

    public final void stop(int stream) {
        mSoundPool.stop(stream);
    }

    public final void pause(int stream) {
        mSoundPool.pause(stream);
    }

    public final void resume(int stream) {
       mSoundPool.resume(stream);
    }

    private final int findSound(int resource) {
        mSearchDummy.resource = resource;
        return mSounds.find(mSearchDummy, false);
    }

    synchronized public final void setSoundEnabled(boolean soundEnabled) {
        mSoundEnabled = soundEnabled;
    }

    public final boolean getSoundEnabled() {
        return mSoundEnabled;
    }

    public class Sound {
        public int resource;
        public int soundId;
    }

    /** Comparator for sounds. */
    private final static class SoundComparator implements Comparator<Sound> {
        public int compare(final Sound object1, final Sound object2) {
            int result = 0;
            if (object1 == null && object2 != null) {
                result = 1;
            } else if (object1 != null && object2 == null) {
                result = -1;
            } else if (object1 != null && object2 != null) {
                result = object1.resource - object2.resource;
            }
            return result;
        }
    }
}

