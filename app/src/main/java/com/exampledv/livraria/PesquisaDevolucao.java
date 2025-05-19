package com.exampledv.livraria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PesquisaDevolucao extends FullScreenActivity {

    private WebView webView;
    private final Handler handler = new Handler();

        private final Runnable returnToMain = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PesquisaDevolucao.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Encerra a atividade atual
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_pesquisa_devolucao);

            webView = findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();

            webView.setInitialScale(100);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            String url = "https://www.mynps.com.br//public-survey/099bee61-033b-4a58-b6e8-836d3472aec3/";

            // Cria o Custom Tabs com barra de ação colorida
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setToolbarColor(getResources().getColor(R.color.azul2)) // ou a cor que preferir
                    .build();

            customTabsIntent.launchUrl(this, Uri.parse(url));

            // Inicia o timer de 5 minutos
            handler.postDelayed(returnToMain, 1 * 60 * 1000);
//        handler.postDelayed(returnToMain, 1000);
        }

        private void enterFullScreenMode() {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        @Override
        public void onBackPressed() {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            // Cancela o timer se a atividade for destruída antes do tempo
            handler.removeCallbacks(returnToMain);
        }
    }