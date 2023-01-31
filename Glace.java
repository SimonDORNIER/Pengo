import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Glace implements Case {

  protected int posX;
  protected int posY;

  protected static Image glace;

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

  public Glace(int x, int y) {
    init(x, y);
    if (glace == null)
      glace = new ImageIcon("images/glace.png").getImage();
  }//fin "Glace"

  public void actualiser(Plateau plateau) { }

  public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) { //retourne true si l'entité entrante peut remplacer l'actuelle
    if (entrant.isType("Glace"))
      return false;
    if (entrant.isType("Monstre"))
      return false;
    if (!entrant.isType("Pingouin"))
      return entrant.interaction(plateau, this, -deplacementX, -deplacementY);
    boolean result = false;
    int posX2 = posX - deplacementX, posY2 = posY - deplacementY;
    int dposX = posX, dposY = posY;
    Case nextCase = plateau.getCase(posX2, posY2);
    if (nextCase != null && nextCase.isType("Glace") || nextCase != null && nextCase.isType("Mur") || nextCase != null && nextCase.isType("Diamant"))
    {
      plateau.remplacer(posX, posY, new Sol(posX, posY));//destruction de la glace dans le cas où coincee contre une autre
      Partie p = plateau.getPartie();
      if (p != null) {
        p.addScore(10);
      }
      return true;
    }
    while (plateau.echangerCases(dposX, dposY, posX2, posY2)) {
      result = true;
      int x = posX; int y = posY;
      dposX = x - deplacementX;
      dposY = y - deplacementY;
      posX2 = x;
      posY2 = y;
    }
    return result;
  }//fin "interaction"

  public boolean isType(String str) {
    return str.equals("Glace");
  }
  //retourne true si le type entré correspond au type de l'instance actuelle
  
  public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {
    g2d.drawImage(glace, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
  }

  public Case cloner() {
    return new Glace(posX, posY);
  }
}
