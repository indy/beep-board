/*

   Copyright 2011 Inderjit Gill (email@indy.io)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

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

    private static final int MAX_STREAMS = 16;
    private static final int MAX_SOUNDS = 32;

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MUSIC = 3;

    private Context mContext;
    private SoundPool mSoundPool;
    private Sound mSearchDummy;
    private boolean mSoundEnabled;


    public SoundManager(Context context)
    {
        mContext = context;
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSearchDummy = new Sound();
    }

    public void reset() {
        mSoundPool.release();
        mSoundEnabled = true;
    }

    public Sound load(int resource) {
        Sound result = null;
        result = new Sound();
        result.resource = resource;
        result.soundId = mSoundPool.load(mContext, resource, 1);
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
}

