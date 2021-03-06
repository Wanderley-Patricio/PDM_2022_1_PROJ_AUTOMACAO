//HOME O LABORATÓRIO EQUIPE PROJETOS PUBLICAÇÕES CURSOS PARCEIROS TUTORIAIS BLOG
//Tutorial Android – Comunicação Bluetooth

package br.edu.ifpe.tads.pdm.pdm_2022_1_automacao_esp32_esp8266;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class ConexaoBluetooth {
    private static ConexaoBluetooth conexao; //objeto que referencia a classe
    private final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter(); //acessa o dispositivo bluetooth de um celular genérico
    private BluetoothDevice BTDevice; //todo acesso ao módulo bluetooth utilizado no tutorial será feito a partir deste objeto.
    private final BluetoothSocket BTSocket; // garante a instância da rede bluetooth entre o celular e o módulo
    private BufferedReader mBufferedReader = null; //lê as comunicações
    private final UUID activeUUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //ID padrão da porta serial.
    private boolean isConnected = true; // variável de controle da conexão
    private final int REQUEST_ENABLE_BT = 1; // Constante padrão para requisição de permissão de acesso ao bluetooth em tempo de execução.

    public BufferedReader getmBufferedReader() {
        return mBufferedReader;
    }

    private ConexaoBluetooth(BluetoothDevice device) {
        BTDevice = device;
        BluetoothSocket temp = null;

        try
        {
            temp = BTDevice.createRfcommSocketToServiceRecord(activeUUID);
        }
        catch (IOException io)
        {
            Log.i("LOG", "Erro IO");
        }
        BTSocket = temp;

        if(BTAdapter.isDiscovering())
            BTAdapter.cancelDiscovery();

        try {

            InputStream aStream = null;
            InputStreamReader aReader = null;
            BTSocket.connect();

            aStream = BTSocket.getInputStream();
            aReader = new InputStreamReader( aStream );
            mBufferedReader = new BufferedReader( aReader );
        }catch (IOException e) {
            isConnected = false;
            return;
        }
    }

    public static ConexaoBluetooth getInstance(BluetoothDevice d, boolean subrescrever) {
        if (conexao == null)
            conexao = new ConexaoBluetooth(d);
        else
        if(subrescrever)
        {
            conexao = new ConexaoBluetooth(d);
            Log.i( "ConexaoBluetooth","Sobrescreveu a conexão");
        }
        return conexao;
    }

    public boolean isConnected () {
        return BTAdapter.isEnabled();
    }


    public BluetoothSocket getConection() {
        return BTSocket;
    }

    public boolean finish() {
        try {
            BTSocket.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
