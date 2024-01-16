package com.redneckrobotics.pyFRC;

import com.redneckrobotics.util.Sketch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
 * PyFRC Server spec:
 * *LITTLE ENDIAN*
 * Packet:
 *  |id->uint8| |size->uint16| |data->bytes[size]|
 * Ping |0| |0| ||
 * Get  |1| |len| |name->utf-8|
 */

class AcceptThread implements Runnable {
    private Reference<ArrayList<Socket>> conns;
    private ServerSocket sock;

    public AcceptThread(Reference<ArrayList<Socket>> conns, ServerSocket sock) {
        this.conns = conns;
        this.sock = sock;
    }

    public void run() {
        for (;;) {
            try {
                conns.get().add(this.sock.accept());
            } catch (IOException e) {
                // TODO
            }
        }
    }
}
public class Server implements Sketch {
    public final short  port;

    private ServerSocket sock;
    private ArrayList<Socket> conns;
    private Thread thrd;
    private AcceptThread acceptThread;
    
    public Server(short port) {
        this.port = port;
    }

    public void setup() {
        try {
            this.sock = new ServerSocket(this.port);
            this.conns = new ArrayList<Socket>();

            this.acceptThread = new AcceptThread(new WeakReference<ArrayList<Socket>>(this.conns), this.sock);
            this.thrd = new Thread(this.acceptThread);
            this.thrd.start();
        } catch (IOException e) {
            // TODO
        }
    }
    private static byte[] read(InputStream is, int maxN) throws IOException {
        if (is.available() == 0 || maxN == 0) return new byte[0];
        return is.readNBytes(Math.min(maxN, is.available()));
    }
    private static short bytesToShort(byte[] byteArray) {
        if (byteArray.length < 2) return 0;
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Set byte order
        return buffer.getShort();
    }
    private static short readShort(InputStream is) throws IOException {
        return bytesToShort(read(is, 2));
    }
    private static String readString(InputStream is, short len) throws IOException {
        byte data[] = read(is, len);
        return new String(data, StandardCharsets.UTF_8);
    }
    private static void sendDouble(OutputStream os, double value) throws IOException {
        os.write(doubleToBytes(value));
    }
    private static byte[] doubleToBytes(double value) {
        long longValue = Double.doubleToLongBits(value);
        return longToBytes(longValue);
    }

    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8]; // Double.SIZE / Byte.SIZE
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }
    public void update() {
        try {
            for (Socket sock : this.conns) {
                InputStream  is = sock. getInputStream();
                OutputStream os = sock.getOutputStream();
                while (is.available() != 0) {
                    byte packId = is.readNBytes(1)[0];

                    switch (packId) {
                        case PacketType.PING: {
                            // Ping packet, right back at'em
                            short size = readShort(is);
                            read(is, size); // shouldn't be any data, but just in case...
                            os.write(
                                new byte[]{PacketType.PING, 0, 0});

                            System.out.println("Ping packet");
                        } break;
                        case PacketType.GET: {
                            short size = readShort(is);
                            String name = readString(is, size);

                            double data = SmartDashboard.getNumber(name, 0.0);
                            os.write(
                                new byte[]{PacketType.GET_RES, 0, 8});
                            sendDouble(os, data);

                            System.out.println("Get packet: " + name + " = " + Double.toString(data));
                        } break;
                            
                    }
                }
            }
        } catch (IOException e) {
            // TODO: java sucks
        }
    }
    public void teardown() {
        try {
            this.sock.close();
        } catch (IOException e) {
            // get rid of annoying error messages
        }
    }
}
