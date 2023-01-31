import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class LeaderboardClient
{
  private String adresseServeur;
  private int portServeur;

  private String nomJoueur;
  private int scoreJoueur;

  private DatagramSocket client;

  private volatile boolean pasDeServeur;

  public LeaderboardClient(String nomJoueur, int scoreJoueur) {
    this.nomJoueur = nomJoueur;
    this.scoreJoueur = scoreJoueur;
    this.adresseServeur = LeaderboardServer.address;
    this.portServeur = LeaderboardServer.port;
    pasDeServeur = true;
    initSocket();
  }

  public void initSocket() {
    client = null;
    try {
      client = new DatagramSocket();
      client.setSoTimeout(300);
      if (nomJoueur != null && !nomJoueur.equals("")) {
        //byte[] requete = new String("add " + nomJoueur + " " + scoreJoueur + " ").getBytes();
        //DatagramPacket packet = new DatagramPacket(requete, requete.length, InetAddress.getByName(adresseServeur), portServeur);
        //client.send(packet);
        demanderAuServeur("add " + nomJoueur + " " + scoreJoueur);

        byte[] reponse = new byte[128];
        DatagramPacket packet = new DatagramPacket(reponse, reponse.length);
        client.receive(packet);
        pasDeServeur = false;
        System.out.println("Demande d'ajout acceptee!");
      }
    }
    catch (UnknownHostException ex) {
      System.out.println("Pas de serveur!");
    }
    catch (Exception ex) {
      System.out.println("Erreur client !\n" + ex.toString());
    }
  }

  public boolean pasDeServeur() {
    return pasDeServeur;
  }

  private void demanderAuServeur(String s) {
    try {
      byte[] requete = new String(s + " ").getBytes();
      DatagramPacket packet = new DatagramPacket(requete, requete.length, InetAddress.getByName(adresseServeur), portServeur);
      client.send(packet);
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }

  public void setScore(int score) {
    if (score > scoreJoueur) {
      scoreJoueur = score;
      demanderAuServeur("set " + nomJoueur + " " + scoreJoueur);
    }
  }

  public String getLeaderboard(boolean trierParNom)
  {
    if (trierParNom)
      demanderAuServeur("leaderboard byName");
    else
      demanderAuServeur("leaderboard byScore");
    try {
      byte[] data = new byte[128];
      DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(adresseServeur), portServeur);
      client.receive(packet);
      System.out.println("Received packet ! " + new String(packet.getData()));
      return new String(packet.getData());
    }
    catch (Exception ex){
      System.out.println(ex.toString());
    }
    return "";
  }
}
