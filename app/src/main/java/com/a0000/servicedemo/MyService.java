package com.a0000.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
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
        System.out.println("----onStartCommand-------- " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
