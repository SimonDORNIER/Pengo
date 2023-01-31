import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InterfacePause extends InterfaceUtilisateur {

  private Font police;
  private FontMetrics metrics;

  public InterfacePause(ManagerJeu jeu) {
    super(jeu);
    police = jeu.getPoliceBasique();
    metrics = jeu.getMetrics(police);
  }//fin "InterfacePause"

  public void actualiser() {
      jeu.repaint();
    if (ManagerClavier.estPressee(KeyEvent.VK_ESCAPE))
      jeu.continuerPartie();
  }//fin "actualiser"

  public void dessiner(Graphics g, int largeur, int hauteur) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.setColor(Color.black);
    g2d.setFont(police);
    g2d.drawString("Continuer", (largeur - metrics.stringWidth("Continuer")) / 2, hauteur / 2);
  }//fin "actualiser"
}
