import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.*;

public class ManagerClavier extends KeyAdapter {
  private static volatile ArrayList<Integer> touchesPressees = new ArrayList<Integer>(Arrays.asList(-1));
  private static volatile ArrayList<Integer> touchesPresseesAvant = new ArrayList<Integer>(Arrays.asList(-1));
  private static String text = "";

  public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      char c = e.getKeyChar();
      if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
        text += c;
      }
      if (!touchesPressees.contains(key)) {
        touchesPressees.add(key);
      }
  }//fin keyPressed

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    synchronized (touchesPressees) {
      if (touchesPressees.contains(key)) {
        touchesPressees.remove(new Integer(key));
      }
    }
  }//fin keyReleased

  public static void actualiser() {
    touchesPresseesAvant.clear();
    int i = 0;
    synchronized (touchesPressees) {
      while (i < touchesPressees.size()) {
        touchesPresseesAvant.add(touchesPressees.get(i));
        i++;
      }
    }
  }

  public static boolean estPressee(int touche) {
    return touchesPressees.contains(touche);
  }

  public static boolean estTapee(int touche) {
    //System.out.println("pressee " + touchesPressees.contains(touche) + ", avant " + touchesPresseesAvant.contains(touche));
    return touchesPressees.contains(touche) && !touchesPresseesAvant.contains(touche);
  }

  public static boolean estRelachee(int touche) {
    return !estPressee(touche);
  }

  public static String getText() {
    String t = text;
    text = "";
    return t;
  }
}
