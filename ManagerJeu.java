
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ManagerJeu
{
  public enum Etat
  {
    Menu, Jeu, Pause, GameOver, Gagner, Leaderboard;
  }

  private Etat etat;

  private String pseudo;
  private int maxScore;
  private int score;

  private class FenetreJeu extends JFrame {
    private Etat etat;
    private Partie partie;
    private InterfaceUtilisateur interfaceUtilisateur;

    private BufferedImage bi;
    private Insets insets;

    private BufferedImage biPlateau;
    private Insets insetsPlateau;

    private ManagerClavier managerClavier;

    public FenetreJeu() {
      setSize(408, 460);                                 //D'une certaine taille
      setDefaultCloseOperation(EXIT_ON_CLOSE);           //Qui se ferme quand on clique sur la croix
      setLocationRelativeTo(null);                       //On lui permet d'être deplacer
      setResizable(false);                               //On l'empeche modifier la taille sinon les images ne seront plus adaptes
      setTitle("Pengo");                                 //On lui donne un titre

      managerClavier = new ManagerClavier();
      addKeyListener(managerClavier);

      setFocusable(true);
      setBackground(Color.black);

      setLayout(new BorderLayout());

      bi = new BufferedImage(getLargeur(), getHauteur(), BufferedImage.TYPE_INT_RGB);
      insets = getInsets();
    }

    public int getLargeur() {
      return getWidth();
    }

    public int getHauteur() {
      return getHeight();
    }

    public void setEtat(Etat etat) {
      this.etat = etat;
    }

    public void setPartie(Partie partie) {
      this.partie = partie;
      if (this.partie != null)
        remove(this.partie.getPlateau());
      JPanel plateau = partie.getPlateau();
      add(plateau, BorderLayout.CENTER);

      biPlateau = new BufferedImage(plateau.getWidth(), plateau.getHeight(), BufferedImage.TYPE_INT_RGB);
      insetsPlateau = plateau.getInsets();
    }

    public void setInterface(InterfaceUtilisateur interfaceUtilisateur) {
      this.interfaceUtilisateur = interfaceUtilisateur;
      repaint();
    }

    public void paint(Graphics graphics) {                                             //Dessine la fenêtre
      Graphics g = bi.getGraphics();

      super.paint(g);

      if (partie != null && etat != Etat.Menu && etat != Etat.Leaderboard) {
        JPanel plateau = partie.getPlateau();
        biPlateau = new BufferedImage(plateau.getWidth(), plateau.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics gPlateau = biPlateau.getGraphics();
        Graphics2D g2d = (Graphics2D)gPlateau;
        g2d.setColor(Color.cyan);
        g2d.fillRect(0, 0, plateau.getWidth(), plateau.getHeight());

        partie.getPlateau().dessiner(gPlateau);

        g.drawImage(biPlateau, insets.left, 25, plateau);
      }
      if (interfaceUtilisateur != null) {
        interfaceUtilisateur.dessiner(g, getLargeur(), getHauteur());
      }
      graphics.drawImage(bi, insets.left, insets.top, this);
    }

    public void actionPerformed(ActionEvent e) {                                //Redessinne la fenêtre a chaque action
      boucleJeu();
      repaint();
    }//Fin actionPerformed
  }

  private volatile FenetreJeu fenetre;

  private Partie partie;
  private InterfaceUtilisateur interfaceUtilisateur;

  private int delaiEntreActualisations;
  private long actualMillis;
  private long dernierMillis;

  private boolean quit;

  public ManagerJeu(int delaiEntreActualisationsEnMillis) {
    EventQueue.invokeLater(() -> {
        fenetre = new FenetreJeu();
        fenetre.setVisible(true);
    });
    delaiEntreActualisations = delaiEntreActualisationsEnMillis;
    actualMillis = System.currentTimeMillis();
    dernierMillis = actualMillis;

    quit = false;

    pseudo = "";//"Pingouin";
    maxScore = 0;
    score = 0;
  }

  public void demarrer() {
    while (fenetre == null) {
      try {
      Thread.sleep(10);
    } catch (Exception exc) {}
    }                                                                           //on boucle en attendant l'initialisation de la fenetre dans son thread

    setInterfaceMenu();
    boucleJeu();
  }

  public void lancer(Partie partie) {
    this.partie = partie;
    setInterfaceJeu();
    fenetre.setPartie(partie);
  }

  private void boucleJeu() {
    while (!quit) {
      if (partie != null) {
        while (!partie.estFinie()) {
          actualMillis = System.currentTimeMillis();
          if (ManagerClavier.estTapee(KeyEvent.VK_ESCAPE)) {
            if (etat == Etat.Jeu)
              setInterfacePause(); //on inverse les etats entre pause et jeu quand on presse la touche escape
            else
              setInterfaceJeu();
          }

          if (etat == Etat.Jeu && actualMillis - dernierMillis >= delaiEntreActualisations) { //Actualise seulement si on est en jeu et qu'on a attendu assez de temps
            interfaceUtilisateur.actualiser();
            partie.actualiser();
            partie.reduireScore();
            fenetre.repaint();
            dernierMillis = actualMillis;
          }
          else if (etat != Etat.Jeu) {
            dernierMillis = actualMillis;
          }

          ManagerClavier.actualiser();
        }
        setMaxScore(partie.getScore());
        if (partie.estPerdue()) {
          setInterfaceGameOver();
          interfaceUtilisateur.actualiser();
          partie = null;
        }
        else if(partie.estGagner()){
          setInterfaceGagner(getScore());
          interfaceUtilisateur.actualiser();
          partie = null;
        }
        ManagerClavier.actualiser();
      }
      else {
        while (partie == null) {
          interfaceUtilisateur.actualiser();
          fenetre.repaint();

          ManagerClavier.actualiser();
        }
      }
    }
  }

  public Partie getPartie() { return partie; }

  public void repaint() {
    fenetre.repaint();
  }

  public void continuerPartie() {
    setInterfaceJeu();
  }

  public void relancerPartie() {
    lancer(partie);
  }

  public void quitterPause() {
    setInterfaceMenu();
  }

  public void quitterGameOver() {
    setInterfaceMenu();
  }//fin "quitterGameOver"

  public void allerLeaderboard(LeaderboardClient client) {
    setInterfaceLeaderboard(client);
  }

  public void quitterLeaderboard() {
    setInterfaceMenu();
  }

  public int getScore() {
    if (partie == null)
      return score;
    score = partie.getScore();
    return partie.getScore();
  }//fin "getScore"

  public void setMaxScore(int score) {
    if (score > maxScore)
      maxScore = score;
  }

  public int getMaxScore() {
    return maxScore;
  }

  public String getPseudo() {
      return pseudo;
  }

  public void setPseudo(String pseudo) {
      this.pseudo = pseudo;
  }

  public Font getPoliceTitre() {
    return new Font("Monospaced", Font.BOLD, 24);
  }//fin "getPoliceTitre"

  public Font getPoliceBasique() {
    return new Font("Monospaced", Font.BOLD, 16);
  }//fin "getPoliceBasique"

  public FontMetrics getMetrics(Font police) {
    return fenetre.getFontMetrics(police);
  }//fin "getMetrics"

  private void setInterfacePause() {
    setEtat(Etat.Pause);
    interfaceUtilisateur = new InterfacePause(this);
    fenetre.setInterface(interfaceUtilisateur);
  }//fin "setInterfacePause"

  private void setInterfaceMenu() {
    setEtat(Etat.Menu);
    interfaceUtilisateur = new InterfaceMenu(this);
    partie = null;
    fenetre.setInterface(interfaceUtilisateur);
  }//fin "setInterfaceMenu"

  private void setInterfaceGagner(int score) {
    setEtat(Etat.Gagner);
    interfaceUtilisateur = new InterfaceGagner(this, score);
    fenetre.setInterface(interfaceUtilisateur);
  }//fin "setInterfaceGagner"

  private void setInterfaceGameOver() {
    setEtat(Etat.GameOver);
    interfaceUtilisateur = new InterfaceGameOver(this);
    fenetre.setInterface(interfaceUtilisateur);
  }//fin "setInterfaceGameOver()"

  private void setInterfaceJeu() {
    setEtat(Etat.Jeu);
    interfaceUtilisateur = new InterfaceJeu(this);
    fenetre.setInterface(interfaceUtilisateur);
  }//fin "setInterfaceJeu"

  private void setInterfaceLeaderboard(LeaderboardClient client) {
    setEtat(Etat.Leaderboard);
    interfaceUtilisateur = new InterfaceLeaderboard(this, client);
    partie = null;
    fenetre.setInterface(interfaceUtilisateur);
  }

  private void setEtat(Etat etat) {
    this.etat = etat;
    fenetre.setEtat(etat);
  }//fin "setEtat"
}
