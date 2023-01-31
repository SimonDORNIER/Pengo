import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class InterfaceMenu extends InterfaceUtilisateur {

  private Font small;
  private FontMetrics metrics;


  public InterfaceMenu(ManagerJeu jeu) {
    super(jeu);
    small = jeu.getPoliceBasique();
    metrics = jeu.getMetrics(small);
  }//fin "InterfaceMenu"

  public void actualiser() {
    if (ManagerClavier.estPressee(KeyEvent.VK_S)) {
      System.out.println("Lancement du jeu!");
      ArrayList<Pingouin> pingouins = new ArrayList<Pingouin>();
      pingouins.add(new Pingouin(0, 0));
      Plateau plateau = new Plateau();
      jeu.lancer(new Partie(pingouins, plateau, 5000));
    }
    else if (ManagerClavier.estPressee(KeyEvent.VK_SPACE)) {
      System.out.println("lancement du leaderboard");
      LeaderboardClient client = new LeaderboardClient(jeu.getPseudo(), jeu.getScore());
      if (client.pasDeServeur()) {
        System.out.println("Server go!");
        new LeaderboardServer();
        try {
          Thread.sleep(500);
        }
        catch (Exception ex) {

        }
        client = new LeaderboardClient(jeu.getPseudo(), jeu.getScore());
      }
      jeu.allerLeaderboard(client);
    }
  }//fin "actualiser"

  public void dessiner(Graphics g, int largeur, int hauteur) {
    Graphics2D g2d = (Graphics2D)g;
    String s1 = "Appuyer sur s pour start";
    String s2 = "Ou sur espace pour afficher les scores";

    g2d.setColor(new Color(0,191,255));
    g2d.fillRect((largeur - metrics.stringWidth(s2)) - 18, hauteur / 2 - metrics.getHeight(), metrics.stringWidth(s2) + 3, metrics.getHeight() * 2 + 5);
    g2d.setColor(Color.black);
    g2d.drawRect((largeur - metrics.stringWidth(s2)) - 18, hauteur / 2 - metrics.getHeight(), metrics.stringWidth(s2) + 3, metrics.getHeight() * 2 + 5);

    g2d.setColor(Color.black);
    g2d.setFont(small);
    g2d.drawString(s1, (largeur - metrics.stringWidth(s1)) / 2, hauteur / 2);
    g2d.drawString(s2, (largeur - metrics.stringWidth(s2)) / 2, hauteur / 2 + metrics.getHeight());
  }//fin "dessiner"
}
