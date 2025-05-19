package com.exampledv.livraria;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.topwise.cloudpos.aidl.printer.Align;
import com.topwise.cloudpos.aidl.printer.ImageUnit;
import com.topwise.cloudpos.aidl.printer.PrintTemplate;
import com.topwise.cloudpos.aidl.printer.TextUnit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CupomNaTelaEmprestimo extends FullScreenActivity {

    private static final String URL_PESQUISA = "https://www.mynps.com.br//public-survey/683375b6-6474-4738-9378-d448bc69aca6/";
    private static final int QR_CODE_SIZE = 350;
    private static final int LOGO_WIDTH = 400;
    private static final int LOGO_HEIGHT = 200;

    private Context mContext;
    private ImageView comprovanteImageView;

    private Button btnFinalizarEmprestimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom_na_tela_emprestimo);

        btnFinalizarEmprestimo = findViewById(R.id.btnFinalizarEmprestimo);
        mContext = this;
        comprovanteImageView = findViewById(R.id.comprovanteImageView);
        mostrarCupomNaTela(); // Mostra o comprovante na tela


        btnFinalizarEmprestimo.setOnClickListener(v -> {
            Intent intent = new Intent(CupomNaTelaEmprestimo.this, PesquisaEmprestar.class);
            startActivity(intent);
        });
    }

    private void mostrarCupomNaTela() {
        String dataAtual = getFormattedDate("dd/MM/yyyy");
        String horaAtual = getFormattedDate("HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 20);
        String dataFinal = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());

        String nome = getIntent().getExtras().getString("nome");
        String setor = getIntent().getExtras().getString("setor");

        String cupomText = montarCupom(dataAtual, horaAtual, dataFinal, nome, setor);

        PrintTemplate template = PrintTemplate.getInstance();
        template.init(mContext);
        template.clear();

        template.add(new TextUnit("\n\n\n"));
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        template.add(new ImageUnit(Bitmap.createScaledBitmap(logo, LOGO_WIDTH, LOGO_HEIGHT, false), LOGO_WIDTH, LOGO_HEIGHT));

        template.add(new TextUnit(cupomText, 26, Align.LEFT));

        Bitmap qrCode = generateQRCode(URL_PESQUISA, QR_CODE_SIZE, QR_CODE_SIZE);
        template.add(new ImageUnit(qrCode, QR_CODE_SIZE, QR_CODE_SIZE));

        Bitmap cupomBitmap = template.getPrintBitmap();
        comprovanteImageView.setImageBitmap(cupomBitmap);

    }

    private String montarCupom(String dataAtual, String horaAtual, String dataFinal, String nome, String setor) {
        return  "-----------------------------\n" +
                " COMPROVANTE DE EMPRÉSTIMO\n" +
                "-----------------------------\n" +
                "Data: " + dataAtual + "\n" +
                "Hora de retirada: " + horaAtual + "\n" +
                "-----------------------------\n" +
                "TEMPO DE EMPRÉSTIMO:\n" +
                dataAtual + "  a  " + dataFinal + "\n" +
                "-----------------------------\n" +
                "Nome: " + nome + "\n" +
                "SETOR: " + setor + "\n" +
                "-----------------------------\n" +
                "POR FAVOR, RESPONDA A PESQUISA" +
                " LENDO O QR CODE ABAIXO\n" +
                "-----------------------------";
    }

    private String getFormattedDate(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }

    public Bitmap generateQRCode(String text, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            Bitmap qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return qrBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap rotateBitmap(Bitmap original, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
        original.recycle();
        return rotatedBitmap;
    }
}