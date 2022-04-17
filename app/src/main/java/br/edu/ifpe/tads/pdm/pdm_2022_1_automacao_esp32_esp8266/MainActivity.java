//HOME O LABORATÓRIO EQUIPE PROJETOS PUBLICAÇÕES CURSOS PARCEIROS TUTORIAIS BLOG
//Tutorial Android – Comunicação Bluetooth
package br.edu.ifpe.tads.pdm.pdm_2022_1_automacao_esp32_esp8266;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BA;
    private final String nomeDispositivo = "ESP32_WPSN_v1"; //Mude beMyEyes para o nome do seu módulo Bluetooth.
    private final int REQUEST_ENABLE_BT = 1; // Código padrão para o requerimento em tempo de execuxão.
    private ConexaoBluetooth conexao;
    private IntentFilter it = null;
    private final String[] PermissionsLocation = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}; //Array de permissões relacionadas ao Bluetooth no Android 6.0 ou maior
    private final int ResquestLocationId = 0; // Código padrão para o requerimento em tempo de execução.

//    Button btn_ok = findViewById(R.id.btn_ok);
//    EditText et_saida = findViewById(R.id.et_saida);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //=========================================================================================
        System.out.println("Entrei no onCreate");

        while (true) {
            it = new IntentFilter(); // Instancia o filtro declarado logo após o onCreate().
            it.addAction(BluetoothDevice.ACTION_FOUND);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(mReceiver, it); // Registra um Receiver para o App.
            break;
        }
        BA = BluetoothAdapter.getDefaultAdapter();
        BtEnable();

//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                et_saida.setText("xxx");
//            }
//        });

    }

    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Quando a ação "discover" achar um dispositivo
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    if (device.getName().trim().equals(nomeDispositivo)) {
                        conexao = ConexaoBluetooth.getInstance(device, true);
                        if (conexao.isConnected()) {
                            Toast.makeText(MainActivity.this, "Conectado ao " + device.getName(), Toast.LENGTH_SHORT).show();
                            changeActivity(); // chama a ReceivingData
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    };

    private void changeActivity() {

        Intent i = new Intent(this, ReceivingData.class);
        startActivity(i);
    }


    public void BtEnable() {
        //liga o bluetooth
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, REQUEST_ENABLE_BT);
            Toast.makeText(MainActivity.this, "Bluetooth Ligado", Toast.LENGTH_SHORT).show();

        } else {
            lookFor();
        }
        // Essa if em especial, verifica se a versão Android é 6.0 ou maior, pois caso seja, uma permissão para localização, além das relacionadas ao Bluetooth, sao necessárias.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PermissionsLocation, ResquestLocationId);
            }
        }
    }

    protected void lookFor() {// Procura por dispositivos
        if (BA.startDiscovery()) {
        } else
            ;
    }
}