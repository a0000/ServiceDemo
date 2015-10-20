package com.a0000.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ActionBroadCast extends BroadcastReceiver {
    public ActionBroadCast() {
    }

    private static long start = System.currentTimeMillis();
    @Override
    public void onReceive(Context context, Intent intent) {
        long l = System.currentTimeMillis();
        System.out.println("New Message !" + (start- l));
        Toast.makeText(context, "New Message !" + (start- l), Toast.LENGTH_SHORT).show();
        start = l;
    }
}
