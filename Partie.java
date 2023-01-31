import java.util.*;

public class Partie {
  private ArrayList<Personnage> personnages;
  private int nombrePersonnages;
  private Plateau plateau;

  private boolean lancee, finie, perdue, gagner;

  private int score;

  public Partie(ArrayList<Pingouin> pingouins, Plateau plateau, int scoreInitial) {
    this.plateau = plateau;
    this.plateau.setPartie(this);
    for (int i = 0; i < pingouins.size(); i++) {
      plateau.insererRandom(pingouins.get(i));
    }
    personnages = plateau.getPersonnages();
    lancee = false;
    finie = false;
    gagner = false;
    perdue = false;
    score = scoreInitial;
    actualiserEtatJeuPersonnages();
  }//fin "Partie"

  public void actualiser() {
    ArrayList<Personnage> personnages = plateau.getPersonnages();
    nombrePersonnages = personnages.size();
    for (int i = 0; i < nombrePersonnages; i++) {
      personnages.get(i).action();
    }
    plateau.actualiser();
    int nombreJoueurs = 0;
    int nombreMorts = 0;
    for (int i = 0; i < nombrePersonnages; i++) {
      if (personnages.get(i).isType("Pingouin")) {
        nombreJoueurs++;
        if (((Pingouin)personnages.get(i)).getMort()) {
          if (!((Pingouin)personnages.get(i)).enleverUneVie()){
            nombreMorts++;
          }
          ((Pingouin)personnages.get(i)).setMort(false);
          plateau.setCaseSol(((Pingouin)personnages.get(i)).getPosX(), ((Pingouin)personnages.get(i)).getPosY());
          plateau.insererRandom((Pingouin)personnages.get(i));
        }
      }
    }
    if (nombreMorts == nombreJoueurs) {
      finie = true;
      perdue = true;
    }
    else {
      if (plateau.isPartieFinie()) {
        score += 1000;
        gagner = true;
        finie = true;
      }
      else {
        //Si la partie n'est pas finie, on actualise les personnages pour qu'ils aient le temps de choisir une action pour la boucle d'après
        actualiserEtatJeuPersonnages();
      }
    }
  }//fin "actualiser"

  public Plateau getPlateau() {
    return plateau;
  }

  public int getScore() {
    return score;
  }

  public void addScore(int score) {
    this.score += score;
  }

  public void reduireScore() {
    this.score--;
  }

  private void actualiserEtatJeuPersonnages() {
    Plateau plateau = getPlateau();
    for (int i = 0; i < nombrePersonnages; i++) {
      personnages.get(i).actualiserEtatJeu(plateau);
    }
  }

  public boolean estFinie() {
    return finie;
  }

  public boolean estPerdue() {
    return perdue;
  }

  public boolean estGagner() {
    return gagner;
  }
}
