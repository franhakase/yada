package eu.franhakase.yada;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;


public class ClipToTranslateService extends Service implements View.OnTouchListener
{

    WindowManager.LayoutParams params;
    private WindowManager mWindowManager;
    private View mFloatingView, mTerminateServiceView;
    private int mWidth;
    private Point size;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private ImageView ivTerminate;
    private boolean bInRange = false;
    private boolean bAlreadyRunning = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        setTheme(R.style.AppTheme);
        //gerar a notificação do serviço
        NotificationManager mNotifyManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel("666", "ClipL", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Clip-To-Translate Service");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotifyManager.createNotificationChannel(notificationChannel);
            startForeground(666, gerarNotificacoesNew().setChannelId("666").build());
        }
        else
        {
            startForeground(666, gerarNotificacoesNew().build());
        }

        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        mWindowManager.removeView(mFloatingView);
        mWindowManager.removeView(mTerminateServiceView);
        super.onDestroy();
    }

    @SuppressLint("InflateParams")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        if(intent.getAction() != null && intent.getAction().equals(ServiceFlags.ACTION_STOP_FOREGROUND_SERVICE))
        {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }
        else if(intent.getAction() != null && intent.getAction().equals(ServiceFlags.ACTION_SHOW_HIDE_BUTTON))
        {
            if(mFloatingView != null)
            {
                int v = mFloatingView.getVisibility();
                mFloatingView.setVisibility(v == View.VISIBLE ? View.GONE : View.VISIBLE);
                return START_NOT_STICKY;
            }
        }
        else if(intent.getAction() != null && intent.getAction().equals(ServiceFlags.ACTION_START_FOREGROUND_SERVICE))
        {

            if(!bAlreadyRunning)
            {
                bAlreadyRunning = true;
                mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_translation_widget, null);
                mTerminateServiceView = LayoutInflater.from(this).inflate(R.layout.view_terminate_service_layout, null);

                mFloatingView.setVisibility(View.VISIBLE);
                params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE ,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.CENTER | Gravity.START;
                params.x = 0;
                params.y = 0;


                //getting windows services and adding the floating view to it
                mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE ,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                params2.gravity = Gravity.BOTTOM;
                params2.height = (Resources.getSystem().getDisplayMetrics().heightPixels / 4);

                ivTerminate = mTerminateServiceView.findViewById(R.id.ivTerminate);
                mWindowManager.addView(mTerminateServiceView, params2);
                mWindowManager.addView(mFloatingView, params);
                //getting the collapsed and expanded view from the floating view
                View collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);

                Display display = mWindowManager.getDefaultDisplay();
                size = new Point();
                display.getSize(size);

                final RelativeLayout layout = (RelativeLayout) collapsedView;
                ViewTreeObserver vto = layout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = layout.getMeasuredWidth();
                        mWidth = size.x - width;


                    }
                });
                XImageButton collapsed_iv = mFloatingView.findViewById(R.id.collapsed_iv);
                collapsed_iv.setOnTouchListener(this);
            }else
            {
                Toast.makeText(this, R.string.app_service_already_running, Toast.LENGTH_SHORT).show();
            }
            return START_NOT_STICKY;
        }
        return START_NOT_STICKY;

    }


    private Notification.Builder gerarNotificacoesNew()
    {
        //intents
        Intent iParar, iMostrarEsconder;
        //configuração geral da notificação
        Notification.Builder builder = new Notification.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(getResources().getString(R.string.app_service_enabled_header));
        builder.setContentText(getResources().getString(R.string.app_service_enabled_description));
        builder.setSmallIcon(R.drawable.ic_yada_notif);
        builder.setOngoing(true);
        //Ação 1: Parar o serviço[universal]
        iParar = new Intent(this, ClipToTranslateService.class);
        iParar.setAction(ServiceFlags.ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent piStop = PendingIntent.getService(this, 0, iParar, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action aPararServico = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_yada_notif), getResources().getString(R.string.app_service_stop_description), piStop).build();
        builder.addAction(aPararServico);
        iMostrarEsconder = new Intent(this, ClipToTranslateService.class);
        iMostrarEsconder.setAction(ServiceFlags.ACTION_SHOW_HIDE_BUTTON);
        PendingIntent piMostrarEsconder = PendingIntent.getService(this, 0, iMostrarEsconder, 0);
        Notification.Action aMostrarEsconder = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_yada_notif), getResources().getString(R.string.app_service_show_hide_description), piMostrarEsconder).build();
        builder.addAction(aMostrarEsconder);

        return builder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        float xDiff, yDiff;
        int middle = mWidth / 2;
        int[] seila = new int[2];
        int[] seila2 = new int[2];
        mFloatingView.getLocationOnScreen(seila);
        mTerminateServiceView.getLocationOnScreen(seila2);
        seila2[0] = mTerminateServiceView.getWidth()/2;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                mTerminateServiceView.setVisibility(View.VISIBLE);
                return true;

            case MotionEvent.ACTION_UP:
                mTerminateServiceView.setVisibility(View.INVISIBLE);
                if(bInRange)
                {
                    bInRange = false;
                    ImageViewCompat.setImageTintList(ivTerminate, null);
                    mFloatingView.setVisibility(View.GONE);
                    params.x = 0;
                    params.y = 0;
                    mWindowManager.updateViewLayout(mFloatingView, params);
                    return true;
                }
                float nearestXWall = params.x >= middle ? mWidth : 0;
                xDiff = event.getRawX() - initialTouchX;
                yDiff = event.getRawY() - initialTouchY;
                if ((Math.abs(xDiff) < 10) && (Math.abs(yDiff) < 10))
                {
                    Intent dialogIntent = new Intent(getBaseContext(), NewDialogTranslationActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                    view.performClick();
                    //mFloatingView.setVisibility(View.GONE);
                }else
                {
                    params.x = (int) nearestXWall;
                    mWindowManager.updateViewLayout(mFloatingView, params);
                }
                return true;
            case MotionEvent.ACTION_BUTTON_PRESS:
                return true;
            case MotionEvent.ACTION_MOVE:
                int xDiff2 = Math.round(event.getRawX() - initialTouchX);
                int yDiff2 = Math.round(event.getRawY() - initialTouchY);

                //Calculate the X and Y coordinates of the view.
                params.x = initialX + xDiff2;
                params.y = initialY + yDiff2;
                if(
                       seila[0] >= (seila2[0] - mFloatingView.getWidth())
                    && seila[0] <= (seila2[0] + mFloatingView.getWidth())
                    && seila[1] >= (seila2[1])
                    && seila[1] <= (seila2[1] + (mFloatingView.getHeight()*2))
                )
                {
                    if(!bInRange)
                    {
                        DrawableCompat.setTint(DrawableCompat.wrap(ivTerminate.getDrawable()), ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    }
                    bInRange = true;
                }else
                {
                    if(bInRange)
                    {
                        ImageViewCompat.setImageTintList(ivTerminate, null);
                    }
                    bInRange = false;
                }
                //Update the layout with new X & Y coordinates
                mWindowManager.updateViewLayout(mFloatingView, params);
                return true;
        }

        return false;
    }


}
