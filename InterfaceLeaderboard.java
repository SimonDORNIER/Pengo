import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class InterfaceLeaderboard extends InterfaceUtilisateur {
  private LeaderboardClient client;

  private String leaderboard;

  private long actualMillis, dernierMillis;

  private Font font;
  private FontMetrics metrics;

  public InterfaceLeaderboard(ManagerJeu jeu, LeaderboardClient client) {
    super(jeu);
    this.client = client;
    leaderboard = "";

    actualMillis = System.currentTimeMillis();
    dernierMillis = actualMillis;

    font = jeu.getPoliceTitre();
    metrics = jeu.getMetrics(font);
  }//fin "InterfaceLeaderboard"


  public void actualiser() {
    actualMillis = System.currentTimeMillis();
    if (actualMillis - dernierMillis > 1000) {
      leaderboard = client.getLeaderboard(false);
      dernierMillis = actualMillis;
    }
    if (ManagerClavier.estPressee(KeyEvent.VK_ESCAPE)) {
      System.out.println("Quitter leaderboard");
      jeu.quitterLeaderboard();
    }
  }//fin "actualiser"

  public void dessiner(Graphics g, int largeur, int hauteur) {
    String[] infos = leaderboard.split(" ");

    Graphics2D g2d = (Graphics2D)g;

    g2d.setColor(Color.yellow);
    g2d.fillRect(10, 10, largeur - 20, hauteur - 20);

    g2d.setColor(Color.black);
    g2d.setFont(font);
    for (int i = 0; i < infos.length - 1; i += 2) {
      g2d.drawString(infos[i], 20, (i / 2) * metrics.getHeight() + 70);
      g2d.drawString(infos[i + 1], metrics.stringWidth(infos[i]) + 30, (i / 2) * metrics.getHeight() + 70);
    }
  }//fin "dessiner"
}
