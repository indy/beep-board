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

