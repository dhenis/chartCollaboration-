package com.example.deni.chartcollaboration;

import android.view.View;

/**
 * Created by deni on 30/08/2018.
 */

interface PresenterOverrideTalkBackInterface {



    void DisableTouchForTalkBack(View view);

    //fix talk back read incorrectly

    String getConvertedDuration(long milliseconds);

    String DurationContentDescription(long milliseconds);
}
