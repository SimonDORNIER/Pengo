import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class InterfaceGagner extends InterfaceUtilisateur {

  private Font small;
  private FontMetrics metrics;
  private int scoreFinal;
  private Font f;

  private String pseudo;

  public InterfaceGagner(ManagerJeu jeu, int scoreFinal) {
    super(jeu);
    f = jeu.getPoliceBasique();
    metrics = jeu.getMetrics(f);
    this.scoreFinal = scoreFinal;
    pseudo = "";
    ManagerClavier.getText();
  }//jeu "InterfaceGagner"

  public void actualiser() {
    pseudo += ManagerClavier.getText();
    if (ManagerClavier.estTapee(KeyEvent.VK_BACK_SPACE)) {
      pseudo = pseudo.substring(0, pseudo.length() - 1);
    }
    //if (ManagerClavier.estTapee(KeyEvent.VK_SPACE)) {
    //  pseudo += " ";
    //}
    if (ManagerClavier.estPressee(KeyEvent.VK_ENTER)) {
      jeu.setPseudo(pseudo);
      System.out.println(pseudo);
      jeu.quitterGameOver();
    }
  }

  public void dessiner(Graphics g, int largeur, int hauteur) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.setFont(f);
    g2d.setColor(new Color(0, 0, 0));
    String s = "Score Final: " + scoreFinal;
    g2d.drawString(s, largeur / 2, hauteur - 25);

    s = "Ton pseudo : ";
    g2d.drawString(s, (largeur - metrics.stringWidth(s)) / 2, hauteur / 2 - metrics.getHeight() / 2);
    g2d.drawString(pseudo, (largeur - metrics.stringWidth(pseudo)) / 2, hauteur / 2 + metrics.getHeight() / 2);
  }//fin "dessiner"
}
