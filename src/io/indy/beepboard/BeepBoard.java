package io.indy.beepboard;

import android.app.Activity;
import android.os.Bundle;

public class BeepBoard extends Activity
{
    private static final String TAG = "BeepBoard";
    GLView view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        view = new GLView(this);
        setContentView(view);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        view.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        view.onResume();
    }
}
