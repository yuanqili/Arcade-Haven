package experiment;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class Echo implements Runnable {

    private final JFrame f = new JFrame();
    private final JTextField tf = new JTextField(25);
    private final JTextArea ta = new JTextArea(15, 25);
    private final JButton send = new JButton("Send");

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 12345;

    private volatile PrintWriter out;
    private Scanner in;

    private Thread thread;

    private Kind kind;

    public Echo(Kind kind) {
        this.kind = kind;

        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        f.setTitle("experiment.Echo " + kind);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getRootPane().setDefaultButton(send);
        f.add(tf, BorderLayout.CENTER);
        f.add(new JScrollPane(ta), BorderLayout.NORTH);
        f.add(send, BorderLayout.SOUTH);
        f.setLocation(kind.offset, 300);
        f.pack();

        send.addActionListener(e -> {
            String s = tf.getText();
            if (out != null) out.println(s);
            display(s);
            tf.setText("");
        });

        thread = new Thread(this, kind.toString());
    }

    public void start() {
        f.setVisible(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Socket socket;
            if (kind == Kind.Client) {
                socket = new Socket(HOST, PORT);
            } else {
                ServerSocket ss = new ServerSocket(PORT);
                socket = ss.accept();
            }
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            display("Connected");
            while (true)
                display(in.nextLine());
        } catch (Exception e) {
            display(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void display(final String s) {
        EventQueue.invokeLater(() -> ta.append(s + "\u23CE\n"));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Echo(Kind.Server).start();
            new Echo(Kind.Client).start();
        });
    }
}

enum Kind {
    Client(100, "Trying"),
    Server(500, "Awaiting");

    public int offset;
    public String activity;

    Kind(int offset, String activity) {
        this.offset = offset;
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "experiment.Kind{" +
                "offset=" + offset +
                ", activity='" + activity + '\'' +
                '}';
    }
}