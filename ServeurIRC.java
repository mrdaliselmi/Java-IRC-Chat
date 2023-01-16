import java.net.*;
import java.io.*;
import java.util.*;

public class ServeurIRC {
    Vector<ThreadClient> V;

    public static void main(String args[]) {
        int port = 1973;
        if (args.length == 1)
            port = Integer.parseInt(args[0]);
        new ServeurIRC(port);
    }

    public ServeurIRC(int port) {
        V = new Vector<ThreadClient>();
        ServerSocket server = null;
        System.out.println("Serveur ecoute sur le port :" + port);
        try {
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String name = in.readLine();
                in.readLine();
                System.out.println("Arrivee d'un client");
                ThreadClient neo = new ThreadClient(socket, this, name);
                neo.start();
            }
        } catch (Exception e) {
            try {
                server.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.err.println(e);
        }
    }

    synchronized public void EnvoyerATous(String s) {
        for (int i = 0; i < V.size(); i++) {
            ThreadClient c = (ThreadClient) V.elementAt(i);
            c.Envoyer(s);
        }
    }

    public void ajouterClient(ThreadClient c) {
        V.addElement(c);
    }

    synchronized public void EnvoyerListeClients(PrintWriter out) {
        for (ThreadClient t : V) {
            out.println(t.getNom());
        }
    }

    synchronized public void SupprimerClient(ThreadClient c) {
        V.removeElement(c);
    }
}