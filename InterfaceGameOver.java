import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class InterfaceGameOver extends InterfaceUtilisateur {

  private Font small;
  private FontMetrics metrics;

  private long actualMillis, dernierMillis;

  public InterfaceGameOver(ManagerJeu jeu) {
    super(jeu);
    small = jeu.getPoliceTitre();
    metrics = jeu.getMetrics(small);

    actualMillis = System.currentTimeMillis();
    dernierMillis = actualMillis;
  }//fin "InterfaceGameOver"

  public void actualiser() {
    actualMillis = System.currentTimeMillis();
    if (actualMillis - dernierMillis >= 5000 || ManagerClavier.estPressee(KeyEvent.VK_S) || ManagerClavier.estPressee(KeyEvent.VK_ESCAPE)) {
      jeu.quitterGameOver();
    }
  }//fin "actualiser"

  public void dessiner(Graphics g, int largeur, int hauteur) {
    Graphics2D g2d = (Graphics2D)g;
    String s = "GAME OVER!";

    g2d.setColor(new Color(20,80,25));
    g2d.fillRect((largeur - metrics.stringWidth(s)) / 2, hauteur / 2 - metrics.getHeight(), metrics.stringWidth(s), metrics.getHeight());
    g2d.setColor(Color.black);
    g2d.drawRect((largeur - metrics.stringWidth(s)) / 2, hauteur / 2 - metrics.getHeight(), metrics.stringWidth(s), metrics.getHeight());

    g2d.setColor(Color.red);
    g2d.setFont(small);
    g2d.drawString(s, (largeur - metrics.stringWidth(s)) / 2, hauteur / 2);
  }//fin "dessiner"
}
