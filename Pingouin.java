import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Pingouin extends Personnage {                //Classe qui represente un pingouin
    private boolean mort;                                 //Avec ses variables
    private int vieRestante;
    private int directionRegardX;
    private int directionRegardY;

    private int keycodeHaut;
    private int keycodeBas;
    private int keycodeGauche;
    private int keycodeDroite;

    public boolean getMort() {
        return mort;
    }
    public int getVieRestante() {
        return vieRestante;
    }
    public int getDirectionRegardX() {
        return directionRegardX;
    }
    public int getDirectionRegardY() {
        return directionRegardY;
    }

    public void setMort(boolean b) {
        mort = b;
    }
    public boolean enleverUneVie() {
        vieRestante--;
        if (vieRestante == 0)
          return false;
        return true;
    }//fin "enleverUneVie"

    public void setDirectionRegardX(int r) {
        directionRegardX = r;
    }
    public void setDirectionRegardY(int r) {
        directionRegardY = r;
    }

    protected static Image pingHaut, pingBas, pingGauche, pingDroite;//On cree les variables en static pour qu'elles ne soient pas chargées pour chaque instance

    public static Image getImagePingouinBasique() { return pingDroite; }

    public Pingouin(int posX, int posY) {
      this(posX, posY, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    }

    public Pingouin(int posX, int posY, int keycodeHaut, int keycodeBas, int keycodeGauche, int keycodeDroite) {//Constructeur a une position donnee avec les touches donnees pour controler le pingouin
      init(posX, posY);
      mort = false;
      vieRestante = 3;
      directionRegardX = 0;
      directionRegardY = 0;
      if (pingHaut == null) {
        pingHaut = new ImageIcon("images/pingHaut.png").getImage();
        pingBas = new ImageIcon("images/pingBas.png").getImage();
        pingGauche = new ImageIcon("images/pingGauche.png").getImage();
        pingDroite = new ImageIcon("images/pingDroite.png").getImage();
      }
      this.keycodeHaut = keycodeHaut;
      this.keycodeBas = keycodeBas;
      this.keycodeGauche = keycodeGauche;
      this.keycodeDroite = keycodeDroite;
    }// fin "Pingouin"

    public boolean interaction(Plateau plateau, Case entrant, int deplacementX, int deplacementY) {
      if (entrant.isType("Monstre")) {
        mort = true;
      }
      return false;
    }//fin "interaction"

    public boolean isType(String str) {
      return str.equals("Pingouin") || str.equals("Personnage");
    }

    public void action() {
      setDirX(0);
      setDirY(0);
      if (ManagerClavier.estPressee(keycodeHaut)) {
        setDirX(0);
        setDirY(-1);
      }
      if (ManagerClavier.estPressee(keycodeBas)) {
        setDirX(0);
        setDirY(1);
      }
      if (ManagerClavier.estPressee(keycodeGauche)) {
        setDirX(-1);
        setDirY(0);
      }
      if (ManagerClavier.estPressee(keycodeDroite)) {
        setDirX(1);
        setDirY(0);
      }
    }//fin "action"

    public void dessiner(Graphics2D g2d, int largeurCase, int hauteurCase) {
      if (dirX == -1) {
        g2d.drawImage(pingGauche, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
      }
      else if (dirX == 1) {
        g2d.drawImage(pingDroite, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
      }
      else if (dirY == -1) {
        g2d.drawImage(pingHaut, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
      }
      else {
        g2d.drawImage(pingBas, posX * largeurCase, posY * hauteurCase, largeurCase, hauteurCase, null);
      }
    }//fin "dessiner"

    public Case cloner() {
      return new Pingouin(posX, posY, keycodeHaut, keycodeBas, keycodeGauche, keycodeGauche);
    }
}
