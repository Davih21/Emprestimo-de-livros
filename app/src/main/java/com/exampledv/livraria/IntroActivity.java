package com.exampledv.livraria;

import static androidx.core.view.WindowCompat.getInsetsController;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.exampledv.livraria.ui.auth.Login2Activity;
import com.exampledv.livraria.ui.auth.Login3Activity;
import com.exampledv.livraria.ui.auth.LoginActivity;
import com.exampledv.livraria.ui.client.ClienteActivity;
import com.topwise.cloudpos.aidl.camera.AidlCameraScanCode;
import com.topwise.cloudpos.aidl.camera.AidlDecodeCallBack;
import com.topwise.cloudpos.aidl.camera.DecodeMode;
import com.topwise.cloudpos.aidl.camera.DecodeParameter;
import com.topwise.cloudpos.aidl.system.AidlSystem;
import com.topwise.cloudpos.service.DeviceServiceManager;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button pegarLivroBtn, devolverLivroBtn, logginBtn;

    AidlSystem sytem;
    private SoundPool mSoundPool;
    private int mSuccessSound;
    AidlCameraScanCode mDecodeManager;
    private boolean isDecoding = false;
    private Handler handler1 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        //Full Screen
        WindowInsetsControllerCompat windowInsetsControllerCompat = getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        mDecodeManager = com.exampledv.livraria.ConfigScann.DeviceServiceManager.getInstance().getCameraManager();
        sytem = DeviceServiceManager.getInstance().getSystemManager();

        pegarLivroBtn = findViewById(R.id.pegarLivroBtn);
        devolverLivroBtn = findViewById(R.id.devolverLivroBtn);
        logginBtn = findViewById(R.id.logginBtn);


        pegarLivroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, ClienteActivity.class);
                startActivity(intent);
            }
        });

        devolverLivroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, Login3Activity.class);
                startActivity(intent);
            }
        });



        logginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });

//        // Timer para voltar automaticamente à tela principal
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler1.removeCallbacksAndMessages(null); // Cancelar o timer
//                Intent intent = new Intent(IntroActivity.this, MainActivity.class); // ou MainActivity.class
//                startActivity(intent);
//                finish();
//            }
//        }, 60000);

    }



    @Override
    public void onClick(View v) {

    }
}