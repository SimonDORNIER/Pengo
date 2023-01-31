import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sol implements Case {

  protected int posX;
  protected int posY;

  public int getPosX() {
      return posX;
  }
  public int getPosY() {
      return posY;
  }

  public void setPosX(int x) {
      posX =  x;
  }
  public void setPosY(int y) {
      posY = y;
  }

  public void init(int x, int y) {
    posX = x;
    posY = y;
  }

  public Sol(int x, int y) {
    init(x, y);
  }

  public void actualiser(Plateau plateau) { }

  public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) { //retourne true si l'entité entrante peut remplacer l'actuelle
    return true;
  }

  public boolean isType(String str) {                                   //retourne true si le type entré correspond au type de l'instance actuelle
    return str.equals("Sol");
  }

  public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {  }

  public Case cloner() {
    return new Sol(posX, posY);
  }
}
