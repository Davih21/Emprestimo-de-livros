package com.exampledv.livraria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.browser.customtabs.CustomTabsIntent;

public class PesquisaEmprestar extends FullScreenActivity {

    private WebView webView;
    private final Handler handler = new Handler();
    private final Runnable returnToMain = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(PesquisaEmprestar.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Encerra a atividade atual
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_emprestar);

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

//        webView.loadUrl("https://www.mynps.com.br/public-survey/683375b6-6474-4738-9378-d448bc69aca6/");
        // URL que será carregada
        String url = "https://www.mynps.com.br/public-survey/683375b6-6474-4738-9378-d448bc69aca6/";

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
