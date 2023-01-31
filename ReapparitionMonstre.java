public class ReapparitionMonstre extends Thread {
  private int delai;
  private Plateau plateau;

  public ReapparitionMonstre(int delaiEnMillis, Plateau plateau) {
    delai = delaiEnMillis;
    this.plateau = plateau;
    start();
  }

  public void run() {
    try {
      Thread.sleep(delai);
    } catch (Exception ex) { }
    plateau.insererRandom(new Monstre(0, 0));
  }
}
