import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ThreadClient extends Thread {
    BufferedReader In;
    PrintWriter Out;
    ServeurIRC serveur;
    String nom;

    public ThreadClient(Socket socket, ServeurIRC s, String name) {
        super(name);
        nom = name;
        serveur = s;
        try {
            Out = new PrintWriter(socket.getOutputStream(), true);
            In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            serveur.ajouterClient(this);
            serveur.EnvoyerATous("Le client " + nom + " s'est connecte.\n");
            // serveur.EnvoyerListeClients(Out);
            while (true) {
                String reply = In.readLine();
                serveur.EnvoyerATous(reply);
            }
        } catch (IOException e) {
        }
    }

    public void Envoyer(String s) {
        Out.println(s);
        Out.flush();
    }

    public String getNom() {
        return nom;
    }
}