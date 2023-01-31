import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class InterfaceJeu extends InterfaceUtilisateur {
  private ArrayList<Pingouin> pingouins;

  private Font f;

  public InterfaceJeu(ManagerJeu jeu) {
    super(jeu);
    pingouins = new ArrayList<Pingouin>();
    f = jeu.getPoliceBasique();
  }//fin InterfaceJeu

  public void actualiser() {
    pingouins = jeu.getPartie().getPlateau().getPingouins();
  }

  public void dessiner(Graphics g, int largeur, int hauteur) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.setFont(f);
    g2d.setColor(new Color(0, 0, 0));
    String s = "score: " + jeu.getScore();
    g2d.drawString(s, largeur / 2 + 75, hauteur - 17);

    for (int x = 0; x < pingouins.size(); x++) {
      for (int i = 0; i < pingouins.get(x).getVieRestante(); i++) {
          g2d.drawImage(Pingouin.getImagePingouinBasique(), i * 28 + 8, hauteur - 34, 28, 28, null);
      }
    }
  }//fin "dessiner"
}
