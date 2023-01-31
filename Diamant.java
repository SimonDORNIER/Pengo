import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Diamant implements Case {

  protected int posX;
  protected int posY;

  protected static Image diamant;

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

  public Diamant(int x, int y) {
    init(x, y);
    if (diamant == null)
      diamant = new ImageIcon("images/diamant.png").getImage();
  }//fin "Diamant"

  public void actualiser(Plateau plateau) {
    Case cases[][] = plateau.getCases();
    int largeur = plateau.getLargeur(), hauteur = plateau.getHauteur();
    if (posX - 2 >= 0) {
      if (cases[posX - 2][posY].isType("Diamant") && cases[posX - 1][posY].isType("Diamant"))
      {
        plateau.diamantsAlignes();
      }
    }
    if (posX + 2 < largeur) {
      if (cases[posX + 2][posY].isType("Diamant") && cases[posX + 1][posY].isType("Diamant"))
      {
        plateau.diamantsAlignes();
      }
    }

    if (posY - 2 >= 0) {
      if (cases[posX][posY - 2].isType("Diamant") && cases[posX][posY - 1].isType("Diamant"))
      {
        plateau.diamantsAlignes();
      }
    }
    if (posY + 2 < hauteur) {
      if (cases[posX][posY + 2].isType("Diamant") && cases[posX][posY + 1].isType("Diamant"))
      {
        plateau.diamantsAlignes();
      }
    }
  }//fin "actualiser"

  public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) { //retourne true si l'entité entrante peut remplacer l'actuelle
    if (!entrant.isType("Pingouin"))
      return false;                                                             //n'accepte pas l'interaction si ca n'est pas un pingouin
    return plateau.echangerCases(posX - deplacementX, posY - deplacementY, posX, posY);
  }//fin "interaction"

  public boolean isType(String str) {                                   //retourne true si le type entré correspond au type de l'instance actuelle
    return str.equals("Diamant");
  }

  public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {
    g2d.drawImage(diamant, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
  }

  public Case cloner() {
    return new Diamant(posX, posY);
  }
}
