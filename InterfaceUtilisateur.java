import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class InterfaceUtilisateur {
  protected ManagerJeu jeu;

  public InterfaceUtilisateur(ManagerJeu jeu) {
    this.jeu = jeu;
  }

  public abstract void actualiser();

  public abstract void dessiner(Graphics g, int largeur, int hauteur);
}
