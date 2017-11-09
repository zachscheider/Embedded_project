package com.bignerdranch.android.smartlockdemo;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.bignerdranch.android.smartlockdemo.R.id.image;
import static com.bignerdranch.android.smartlockdemo.R.id.imageView2;


public class MainActivity extends AppCompatActivity {

    ImageView image;
    TextView status;
    boolean flag = true;
    boolean flag2 = true;

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    public String motor = "1";

    public void connectPi(){
        try {
            UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
                System.out.println("connected?");
                //status.setText(mmDevice.getName());
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendBtMsg(String msg2send){
        System.out.println("sendBtMsg!");
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        //UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

        try {
            //mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            //status.setText(mmDevice.getName());
            //mmSocket.connect();

            //if (!mmSocket.isConnected()){
               // mmSocket.connect();
                //System.out.println("connected?");
                //status.setText(mmDevice.getName());
            //}

           String msg = msg2send;
            //msg += "\n";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
            System.out.println("done trying...");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
////////////////////////////////////////////////////////////////////////////////////////////////////////
        image = (ImageView) findViewById(R.id.imageView2);
        status = (TextView) findViewById(R.id.status);

        final Handler handler = new Handler();
        Log.d("MyApp","Here1");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //connectPi();
        final class workerThread implements Runnable {

            private String btMsg;

            public workerThread(String msg) {
                btMsg = msg;
                System.out.println("new thread!");
            }

            public void run()
            {
                Log.d("MyApp","Now here2");
                sendBtMsg(btMsg);
                /*
                while(!Thread.currentThread().isInterrupted())
                {
                    System.out.println("in while!");
                    int bytesAvailable;
                    boolean workDone = false;

                    try {



                        final InputStream mmInputStream;
                        System.out.println("after input");
                        System.out.println(mmSocket);

                        mmInputStream = mmSocket.getInputStream();

                        System.out.println("after mmsocket");
                        bytesAvailable = mmInputStream.available();
                        System.out.println(bytesAvailable);

                        //mmSocket.close();

                        if(bytesAvailable > 0)
                        {

                            //System.out.println("in the if");
                            byte[] packetBytes = new byte[bytesAvailable];
                            Log.e("Aquarium recv bt","bytes available");
                            byte[] readBuffer = new byte[1024];
                            mmInputStream.read(packetBytes);

                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    //The variable data now contains our full command
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //myLabel.setText(data);
                                        }
                                    });
                                    System.out.println("work done");
                                    workDone = true;

                                    break;


                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }

                            if (workDone == true){
                               // mmSocket.close();
                                break;
                            }


                        }
                        break;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }*/
            }
        };
        //(new Thread(new workerThread("1"))).start();

        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Perform action on temp button click

                if(flag2) {
                    //(new Thread(new workerThread(motor))).start();
                    flag2 = false;
                }

                switch(v.getId()){
                    case R.id.imageView2:{
                        if(flag)
                        {
                            image.setImageResource(R.drawable.redlock);
                            status.setText("Locked");
                            //motor = "1";
                            (new Thread(new workerThread("1"))).start();
                            flag=false;
                        }
                        else
                        {
                            image.setImageResource(R.drawable.unlocked);
                            status.setText("Unlocked");
                            //motor = "0";
                            (new Thread(new workerThread("0"))).start();
                            flag=true;
                        }
                        return;
                    }
                }

            }
        });
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("raspberrypi")) //Note, you will need to change this to match the name of your device
                {
                    //Log.e("Aquarium",device.getName());
                    //myLabel.setText(device.getName());
                    mmDevice = device;
                    connectPi();
                    break;
                }
            }
        }
    }
/*
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView2:{
                if(flag)
                {
                    image.setImageResource(R.drawable.redlock);
                    status.setText("Locked");
                    (new Thread(new workerThread("1"))).start();
                    flag=false;
                }
                else
                {
                    image.setImageResource(R.drawable.unlocked);
                    status.setText("Unlocked");
                    (new Thread(new workerThread("0"))).start();
                    flag=true;
                }
                return;
            }
        }
    }
    */
////////////////////////////////////////////////////////////////////////////////////////////////////////


}
