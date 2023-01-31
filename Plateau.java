import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.TimerTask;

import java.util.*;

import java.io.*;


public class Plateau extends JPanel implements Serializable {
    private Case cases[][];

    private int nbMonstre;

    private Random random;

    private boolean finis;

    private int largeur, hauteur;

    private Partie partie;

    public Plateau() {
      this(16, 16, 3, 3, 88, 0);
    }

    public Plateau(int hauteur, int largeur, int monstres, int diamants, int glaces, int seedAleatoire) {
        initialisation(hauteur, largeur, monstres, diamants, glaces, seedAleatoire);
    }

    public Plateau(Case[][] cases) {
      this.cases = cases;
      random = new Random(0);
    }

    private void initialisation(int hauteur, int largeur, int monstres, int diamants, int glaces, int seedAleatoire) {
        setVisible(true);
        setSize(400, 400);

        this.largeur = largeur;
        this.hauteur = hauteur;

        if (seedAleatoire != 0)
          random = new Random(seedAleatoire);
        else
          random = new Random();

        cases = new Case[largeur][hauteur];

        nbMonstre = monstres;

        initNiveau(diamants, glaces);
    }//if initialisation

    private void initNiveau(int nombreDiamants, int nombreGlace) {                                                 //Sert a initialiser les positions pour le debut
        construirePlateau(nombreDiamants, nombreGlace);                                                    //ou quand on aligne les trois diamants
        finis = false;
    }//fin initNiveau

    private void construirePlateau(int nombreDiamants, int nombreGlace) {                                          //Definit les types des cases de bases
      int x = 0;
      int y = 0;
      for(x = 0; x < getLargeur(); x++) {
        for(y = 0; y < getHauteur(); y++) {
          if((x == 0) || (x == getLargeur() - 1) || (y == 0) || (y == getHauteur() - 1))
            cases[x][y] = new Mur(x, y);
          else
            cases[x][y] = new Sol(x, y);
          }
      }
      randomPlateau(nombreDiamants, nombreGlace);
      //ajout des monstres
      for(int i = 0; i < nbMonstre; i++)
      {
        do {
          x = random.nextInt(getLargeur());
          y = random.nextInt(getHauteur());
        }
        while (cases[x][y] != null && !cases[x][y].isType("Sol") && !cases[x][y].isType("Glace"));                                          //cherche une case vide
        cases[x][y] = new Monstre(x, y);
      }
    }//fin "construirePlateau"

    private void randomPlateau(int nombreDiamants, int nombreGlace) {
      int x, y;
      int nbGlacesAct = 0;
      while (nbGlacesAct < nombreGlace) {
        insererRandom(new Glace(0, 0), "Sol", 1);
        nbGlacesAct++;
      }

      int nbDiamantsAct = 0;
      while(nbDiamantsAct < nombreDiamants) {                                   //defnit une position aléatoire pour un diamant
          insererRandom(new Diamant(0, 0), "Sol,Glace", 2);
          nbDiamantsAct++;
      }//fin while (nombre de Diamants)
    }//fin randomPlateau


    public static Plateau fromBytes(byte[] bytes) {
      Plateau p = null;
      ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
      ObjectInput in = null;
      try {
        in = new ObjectInputStream(bis);
        p = new Plateau((Case[][])in.readObject());
      }
      catch (Exception ex) {
          System.out.println(ex);
      }
      finally {
        try {
          if (in != null) {
            in.close();
          }
        }
        catch (IOException ex) {

        }
      }
      return p;
    }

    public static byte[] toBytes(Plateau plateau) {
      byte[] array = null;

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      try {
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(plateau.getCases());
        out.flush();

        array = bos.toByteArray();
        bos.close();
      }
      catch(Exception ex) {
          System.out.println(ex);
      }
      finally {
        try {
          if (bos != null)
            bos.close();
        }
        catch (Exception ex) {

        }
      }
      return array;
    }


    public void setPartie(Partie partie) {
      this.partie = partie;
    }

    public Partie getPartie() {
      return partie;
    }

    public Case[][] getCases() {
      return cases;
    }

    public void setCaseSol(int x, int y) {
      cases[x][y] = new Sol(x, y);
    }

    public Case getCase(int x, int y) {
      if (x < 0 || x >= getLargeur() || y < 0 || y >= getHauteur())
        return null;
      return cases[x][y];
    }

    public int getLargeur() {
      return largeur;
    }
    public int getHauteur() {
      return hauteur;
    }

    private void labyComplete() {                                               //Se declenche quand on aligne les trois diamants
      finis = true;
    }

    public void actualiser() {
      for (int x = 0; x < getLargeur(); x++)
        for (int y = 0; y < getHauteur(); y++)
          cases[x][y].actualiser(this);
      ArrayList<Personnage> personnages = getPersonnages();
      for (int i = 0; i < personnages.size(); i++) {
        int dx = personnages.get(i).getDirX();
        int dy = personnages.get(i).getDirY();
        int x = personnages.get(i).getPosX();
        int y = personnages.get(i).getPosY();
        echangerCases(x + dx, y + dy, x, y);
      }
    }//fin "actualiser"

    public ArrayList<Personnage> getPersonnages() {
      ArrayList<Personnage> personnages = new ArrayList<Personnage>();
      for (int x = 0; x < getLargeur(); x++)
        for (int y = 0; y < getHauteur(); y++)
          if (cases[x][y].isType("Personnage"))
            personnages.add((Personnage)cases[x][y]);
      return personnages;
    }//fin "getPersonnages"

    public ArrayList<Pingouin> getPingouins() {
      ArrayList<Pingouin> pingouins = new ArrayList<Pingouin>();
      for (int x = 0; x < getLargeur(); x++)
        for (int y = 0; y < getHauteur(); y++)
          if (cases[x][y].isType("Pingouin"))
            pingouins.add((Pingouin)cases[x][y]);
      return pingouins;
    }//fin "getPingouins"

    public boolean echangerCases(int x, int y, int x2, int y2) {
      if (x >= 0 && x2 >= 0 && x < getLargeur() && x2 < getLargeur() &&
          y >= 0 && y2 >= 0 && y < getHauteur() && y2 < getHauteur()) {
            if (cases[x][y] != null && cases[x2][y2] != null && !cases[x][y].interaction(this, cases[x2][y2], x2 - x, y2 - y))
              return false;                                                     //interaction refusee, donc on ne peut pas echanger les cases
            Case c = cases[x][y];
            remplacer(x, y, cases[x2][y2]);
            remplacer(x2, y2, c);
            return true;
          }
      return false;
    }//fin "echangerCases"

    public void remplacer(int x, int y, Case parCase) {
      if (x < 0 || x >= getLargeur() || y < 0 || y >= getHauteur())
        return;
      cases[x][y] = parCase;
      cases[x][y].setPosX(x);
      cases[x][y].setPosY(y);
    }//fin "remplacer"

    public void insererRandom(Case caseAPlacer) {
      if (caseAPlacer == null)
        return;
      int x = 0, y = 0;
      do {
        x = (int)(1 + (random.nextDouble() * (getLargeur() - 1)));
        y = (int)(1 + (random.nextDouble() * (getHauteur() - 1)));
      } while (cases[x][y] != null && !cases[x][y].isType("Sol"));
      remplacer(x, y, caseAPlacer);
    }//fin "insererRandom"

    public void insererRandom(Case caseAPlacer, String remplacables, int margeMurs) {
      if (caseAPlacer == null)
        return;
      String[] casesRemplacables = remplacables.split(",");
      int x = 0, y = 0;
      boolean estRemplacable = false;
      do {
        x = (int)(margeMurs + (random.nextDouble() * (getLargeur() - margeMurs * 2)));
        y = (int)(margeMurs + (random.nextDouble() * (getHauteur() - margeMurs * 2)));
        estRemplacable = false;
        for (int i = 0; i < casesRemplacables.length; i++)
          estRemplacable |= cases[x][y] == null || cases[x][y].isType(casesRemplacables[i]);
      } while (!estRemplacable);
      remplacer(x, y, caseAPlacer);
    }//fin "insererRandom"

    public void diamantsAlignes() {                                             //methode appelee par les diamants pour dire qu'ils sont alignes
      labyComplete();
    }

    public boolean isPartieFinie() {
      return finis;
    }

    public void dessiner(Graphics g) {
      Graphics2D g2d = (Graphics2D)g;
      int largeurCase = getWidth() / getLargeur();
      int hauteurCase = getHeight() / getHauteur();
      String s = "\n";
      for (int i = 0; i < getLargeur(); i++) {
        for (int j = 0; j < getHauteur(); j++) {
          cases[i][j].dessiner(g2d, largeurCase, hauteurCase);
        }
      }
      for (int y = 0; y < getHauteur(); y++) {
        for (int x = 0; x < getLargeur(); x++) {
          s += (cases[x][y].isType("Sol") ? " "
            : cases[x][y].isType("Pingouin") ? "P"
            : cases[x][y].isType("Monstre") ? "M"
            : cases[x][y].isType("Diamant") ? "D"
            : cases[x][y].isType("Glace") ? "G" : "#");
        }
        s += "\n";
      }
    }//fin "dessiner"
}//Fin Plateau
