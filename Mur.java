import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mur implements Case {

  protected int posX;
  protected int posY;

  protected static Image mur;

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

  public Mur(int x, int y) {
    init(x, y);
    if (mur == null)
      mur = new ImageIcon("images/mur.png").getImage();
  }

  public void actualiser(Plateau plateau) { }

  //retourne true si l'entité entrante peut remplacer l'actuelle
  public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) {
    return false;
  }

  //retourne true si le type entré correspond au type de l'instance actuelle
  public boolean isType(String str) {
    return str.equals("Mur");
  }

  public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {
    g2d.drawImage(mur, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
  }

  public Case cloner() {
    return new Mur(posX, posY);
  }
}
