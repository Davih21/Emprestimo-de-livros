package com.exampledv.livraria;

import static androidx.core.view.WindowCompat.getInsetsController;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private CarouselAdapter adapter;
    private List<Integer> images;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Full screen
        WindowInsetsControllerCompat windowInsetsControllerCompat = getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());


        viewPager = findViewById(R.id.viewPager);

        // Lista de imagens
        images = new ArrayList<>();
        images.add(R.drawable.img1);
        images.add(R.drawable.img2);
        images.add(R.drawable.img3);
        images.add(R.drawable.img4);
        images.add(R.drawable.img5);

        // Configurar o adaptador
        adapter = new CarouselAdapter(this, images);
        viewPager.setAdapter(adapter);

        // Configurar o carrossel automático
        setupAutoScroll();
    }

    private void setupAutoScroll() {
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItem = (currentItem + 1) % images.size(); // Avança para o próximo item ou volta ao primeiro
                viewPager.setCurrentItem(currentItem, true); // Com animação
                handler.postDelayed(this, 9000); // Executa novamente após 3 segundos
            }
        };
        handler.postDelayed(runnable, 3000); // Inicia o ciclo
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Pausa o auto-scroll quando a Activity é pausada
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 9000); // Retoma o auto-scroll quando a Activity é retomada
    }
}
