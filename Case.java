import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

public interface Case extends Serializable {
  public void init(int x, int y);
  //force à utiliser le système de coordonnees au moins à l'initialisation

  public int getPosX();
  public int getPosY();

  public void setPosX(int x);
  public void setPosY(int y);

  public void actualiser(Plateau plateau);

  public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY);
  //retourne true si l'entité entrante peut remplacer l'actuelle

  public boolean isType(String str);
  //retourne true si le type entré correspond au type de l'instance actuelle

  public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase);

  public Case cloner();
}
