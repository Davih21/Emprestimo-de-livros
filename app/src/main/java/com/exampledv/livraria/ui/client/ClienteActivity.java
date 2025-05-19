package com.exampledv.livraria.ui.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.exampledv.livraria.ConfigScann.DeviceServiceManager;
import com.exampledv.livraria.FullScreenActivity;
import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.exampledv.livraria.ui.client.adapter.LivroAdapter;
import com.exampledv.livraria.ui.adm.livros.models.Livro;
import com.topwise.cloudpos.aidl.camera.AidlCameraScanCode;
import com.topwise.cloudpos.aidl.camera.AidlDecodeCallBack;
import com.topwise.cloudpos.aidl.camera.DecodeMode;
import com.topwise.cloudpos.aidl.camera.DecodeParameter;
import com.topwise.cloudpos.aidl.system.AidlSystem;

import java.util.ArrayList;
import java.util.List;



public class ClienteActivity  extends FullScreenActivity {
    private EditText edtBuscar;
    private RecyclerView recyclerViewLivros;
    private LivroAdapter livroAdapter;
    private List<Livro> livroList;
    private DatabaseManager dbManager;

    private Context mContext = ClienteActivity.this;
    AidlSystem sytem;
    private SoundPool mSoundPool;
    private int mSuccessSound;
    AidlCameraScanCode mDecodeManager;
    private boolean isDecoding = false;
    private Handler handler1 = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        // Inicializando a camera
        mDecodeManager = DeviceServiceManager.getInstance().getCameraManager();
        sytem = DeviceServiceManager.getInstance().getSystemManager();

        edtBuscar = findViewById(R.id.edtBuscar);
        recyclerViewLivros = findViewById(R.id.recyclerViewLivros);

        dbManager = new DatabaseManager(this);
        livroList = new ArrayList<>();

        recyclerViewLivros.setLayoutManager(new GridLayoutManager(this, 2));
        livroAdapter = new LivroAdapter(this,livroList, new LivroAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Livro livro) {
                Intent intent = new Intent(ClienteActivity.this, DetalhesLivroActivity.class);
                intent.putExtra("livroId", livro.getId());
                startActivity(intent);
            }
        },false);
        recyclerViewLivros.setAdapter(livroAdapter);

        carregarLivros();

        edtBuscar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!TextUtils.isEmpty(charSequence)) {
                    buscarLivrosPorNome(charSequence.toString());
                } else {
                    carregarLivros();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
    }
    @SuppressLint("Range")
    private void carregarLivros() {
        livroList.clear();
        Cursor cursor = dbManager.buscarTodosLivros();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String autor = cursor.getString(cursor.getColumnIndex("autor"));
                boolean disponivel = cursor.getInt(cursor.getColumnIndex("disponivel")) == 1;
                String imagemUrl = cursor.getString(cursor.getColumnIndex("imagemUrl"));

                Livro livro = new Livro(id, titulo, autor, disponivel, imagemUrl);
                livroList.add(livro);
            } while (cursor.moveToNext());

            livroAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Nenhum livro encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    private void buscarLivrosPorNome(String nome) {
        livroList.clear();
        Cursor cursor = dbManager.buscarLivrosPorNome(nome);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String autor = cursor.getString(cursor.getColumnIndex("autor"));
                boolean disponivel = cursor.getInt(cursor.getColumnIndex("disponivel")) == 1;
                String imagemUrl = cursor.getString(cursor.getColumnIndex("imagemUrl"));

                Livro livro = new Livro(id, titulo, autor, disponivel, imagemUrl);
                livroList.add(livro);
            } while (cursor.moveToNext());

            livroAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Nenhum livro encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void startDecode() {
        if (!isDecoding) {
            try {

                //Inicializa o parametro do scan

                DecodeParameter decodeParameter = new DecodeParameter();
                decodeParameter
                        .setDecodeMode(DecodeMode.MODE_SINGLE_SCAN_CODE)
                        .setFlashLightTimeout(0xffffffff);
                mDecodeManager.startDecode(decodeParameter, new AidlDecodeCallBack.Stub() {
                    @Override
                    public void onResult(String s) throws RemoteException {
                        handleDecode(s);
                    }

                    @Override
                    public void onError(int i) throws RemoteException {
                    }
                });
                isDecoding = true;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDecode(String result) throws RemoteException {
        if (result != null) {
            playBeepSound();
            mDecodeManager.playDecodeSucLight();
            runOnUiThread(() -> {
                if (edtBuscar != null) {
                    edtBuscar.setText(result); // ou cleanedResult
                    Toast.makeText(mContext, "Código lido: " + result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "edtBuscar está null", Toast.LENGTH_LONG).show();
                }
            });


            handler1.postDelayed(this::restartDecode, 2000);
        }
    }


    private void initBeepSound() {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSuccessSound = mSoundPool.load(this, R.raw.beep1, 1);
    }

    private void playBeepSound() {
        mSoundPool.play(mSuccessSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }


    private void restartDecode() {
        stopDecode();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                startDecode();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBeepSound();
        startDecode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDecode();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void clearList() {
    }
    private void stopDecode() {
        try {
            mDecodeManager.stopDecode();
            isDecoding = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDecodeManager != null) {
            try {
                mDecodeManager.stopDecode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        isDecoding = false;
    }
}