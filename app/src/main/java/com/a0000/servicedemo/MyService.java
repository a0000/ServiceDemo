package com.a0000.servicedemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("----onCreate--------");
    }

    private int ii = 0;
    private long start = System.currentTimeMillis();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String format = String.format("----onStartCommand--------  %7d --  %s", (System.currentTimeMillis() - start), new Date().toLocaleString());
//        System.out.println(format);
//        start = System.currentTimeMillis();
        //write2SDCard(format);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        wakeLock.acquire();

        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String format = String.format("----onStartCommand--------  %7d --  %s", (System.currentTimeMillis() - start), new Date().toLocaleString());
                System.out.println(format);
                start = System.currentTimeMillis();
                write2SDCard("service.txt", format);
            }
        }, 1, 12, TimeUnit.SECONDS);
        return super.onStartCommand(intent, flags, startId);
    }

    public static void write2SDCard(String fileName, String s) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String sdCard = Environment.getExternalStorageDirectory().getPath();
                String path = sdCard+"/小组饭/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                path += fileName;
                System.out.println("---------- " + path);
                FileWriter fw = new FileWriter(path, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.append(s);
                bw.newLine();
                bw.close();
                fw.close();
            }
        } catch (Exception e) {
            System.out.println("------------ " +e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exec.shutdownNow();
        System.out.println("----onDestroy--------");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("------onUnbind------");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println("----onRebind--------");
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("------onBind------");
        return null;
    }

    //此方法是为了可以在Acitity中获得服务的实例
    class ServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
