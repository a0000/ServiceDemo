package com.a0000.servicedemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    /** 参数设置 */
    MyService countService;
    private PendingIntent pendingIntent;
    private final int HEARTBEAT_INTERVAL = 2 * 1000;

    private long start = System.currentTimeMillis();
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
//        wakeLock.acquire();
//
//        exec.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                String format = String.format("----Activity--------  %7d --  %s", (System.currentTimeMillis() - start), new Date().toLocaleString());
//                System.out.println(format);
//                start = System.currentTimeMillis();
//                MyService.write2SDCard("activity.txt", format);
//            }
//        }, 1, 12, TimeUnit.SECONDS);
    }

    public void onstart(View v) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void onstop(View v) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void onbind(View v) {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void onunbind(View v) {
        unbindService(conn);
    }

    public void scheduleHeartbeat(View v) {
        scheduleHeartbeat(HEARTBEAT_INTERVAL);
    }
    public void cancelHeartbeatTimer(View v) {
        cancelHeartbeatTimer();
    }
    private void scheduleHeartbeat(int seconds){
        if (pendingIntent == null) {
            Intent intent = new Intent(this, MyService.class);
            pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            if (pendingIntent == null) {
                return;
            }
        }

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 12000, pendingIntent);
    }
    private void cancelHeartbeatTimer() {
        if (pendingIntent == null) {
            return;
        }
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            countService = ((MyService.ServiceBinder) service).getService();

        }

        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            countService = null;
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("=======onDestroy=======");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);//true表示在任何一个activity下都可以退出，false表示只能在根activity下退出
            return true;//true则表示监听被跳出，不再执行其他监听事件了
        }
        return false;
    }
}
