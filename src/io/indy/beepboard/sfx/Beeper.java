package io.indy.beepboard.sfx;

import android.util.Log;

import io.indy.beepboard.sfx.SoundManager.Sound;

public class Beeper
{
    private static final String TAG = "Beeper";

    private SoundManager soundManager;
    private final Sound[] sound;

    public Beeper(SoundManager sm, int[] resources)
    {
        soundManager = sm;
        sound = new Sound[resources.length];

        for(int i=0;i<resources.length;i++) {
            sound[i] = soundManager.load(resources[i]);
        }
    }

    public void beep(int row)
    {
        if(row >= sound.length) {
            row = sound.length - 1;
        }
        soundManager.play(sound[row], false, 1, 0.1f, 1.0f);
    }


}

