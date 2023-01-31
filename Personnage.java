import javax.swing.ImageIcon;

import java.awt.Graphics;

public abstract class Personnage implements Case {                 //Classe qui represente chaque personnage
  
    protected int posX, posY;               //Avec leurs positions
    protected int dirX, dirY;               //Et leurs directions

    private Case[][] plateau;

    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public int getDirX() {
        return dirX;
    }
    public int getDirY() {
        return dirY;
    }

    public void setPosX(int x) {
        posX =  x;
    }
    public void setPosY(int y) {
        posY = y;
    }
    public void setDirX(int x) {
        dirX = x;
    }
    public void setDirY(int y) {
        dirY = y;
    }

    public void init(int x, int y) {
        posX = x;
        posY = y;
        dirX = 0;
        dirY = 0;
        plateau = null;
    }

    public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) {
      return false;
    }

    public boolean isType(String str) {
      return str.equals("Personnage");
    }

    public void actualiser(Plateau p) {
      action();
    }

    public void actualiserEtatJeu(Plateau p) { //Méthode appelée par la partie pour donner l'état actuel du plateau aux personnages après mouvements
      plateau = p.getCases();
    }

    public abstract void action();
}
