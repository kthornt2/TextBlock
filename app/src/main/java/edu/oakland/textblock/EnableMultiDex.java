package edu.oakland.textblock;

/**
 * Created by Rem on 4/12/2017.
 */

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class EnableMultiDex extends MultiDexApplication {
    private static EnableMultiDex enableMultiDex;
    public static Context context;

    public EnableMultiDex(){
        enableMultiDex=this;
    }

    public static EnableMultiDex getEnableMultiDexApp() {
        return enableMultiDex;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}