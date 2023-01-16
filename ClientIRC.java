import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class ClientIRC extends Frame implements Runnable, ActionListener {
    String nom;
    TextArea Output;
    TextField Input;
    Socket socket = null;
    BufferedReader in;
    PrintWriter out;

    public ClientIRC(InetAddress hote, int port, String s) {
        super(s);
        nom = s;
        // mise en forme de la fenetre (frame)
        setSize(500, 700);
        setLayout(new BorderLayout());
        add(Output = new TextArea(), BorderLayout.CENTER);
        Output.setEditable(false);
        add(Input = new TextField(), BorderLayout.SOUTH);
        Input.addActionListener(this);
        pack();
        setVisible(true);
        Input.requestFocus();
        // ajout d'un window adapter pour reagir si on ferme la fenetre
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        try {
            socket = new Socket(hote, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        out.println(nom);
        while (true) {
            try {
                String msg = in.readLine();
                Output.append(msg + "\n");
            } catch (IOException e) {
                System.out.println("error :" + e);
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Input) {
            String phrase = Input.getText();
            out.println(nom + "> " + phrase);
            // efface la zone de saisie
            Input.setText("");
        }
    }

    protected void finalize() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            InetAddress hote = InetAddress.getLocalHost();
            int port = 1973;
            ClientIRC chatwindow = new ClientIRC(hote, port, args[0]);
        } catch (UnknownHostException e) {
        }
    }
}