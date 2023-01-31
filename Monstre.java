import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Monstre extends Personnage {     //Classe qui represente un monstre

    protected static Image imageMonstre;
    private int attenteAct;
    private int attente;

    public Monstre(int x, int y) {
      init(x, y);
      if (imageMonstre == null)
        imageMonstre = new ImageIcon("images/monstre.png").getImage();
      attenteAct = 0;
      attente = 3;
    }//fin "Monstre"

    public void action() {
      if (attenteAct < attente) {
        attenteAct++;
        setDirX(0);
        setDirY(0);
      }
      else {
        attenteAct = 0;
        int i = (int)(Math.random() * 4);
        switch (i) {
          case 0:
            setDirX(1);
            setDirY(0);
            break;
          case 1:
            setDirX(0);
            setDirY(-1);
            break;
          case 2:
            setDirX(-1);
            setDirY(0);
            break;
          case 3:
            setDirX(0);
            setDirY(1);
            break;
        }
      }
    }//fin "action"


    public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) {
      if (entrant.isType("Pingouin")) {
        return entrant.interaction(plateau, this, -deplacementX, -deplacementY);         //on retourne la question au pingouin afin que ça soit toujours à lui de trancher
      }
      if (entrant.isType("Glace")) {
        plateau.remplacer(posX, posY, new Sol(posX, posY));
        Partie p = plateau.getPartie();
        if (p != null) {
          p.addScore(100);
        }
        new ReapparitionMonstre(3000, plateau);
        return true;
      }
      return false;
    }//fin "interaction"

    public boolean isType(String str) {
      return str.equals("Monstre") || str.equals("Personnage");
    }

    public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {
      g2d.drawImage(imageMonstre, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
    }

    public Case cloner() {
      return new Monstre(posX, posY);
    }
}
