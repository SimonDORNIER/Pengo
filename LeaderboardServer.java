import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.*;

public class LeaderboardServer extends Thread
{
  public static int port = 2345;
  public static String address = "127.0.0.1";

  private int playerNumb;
  private ArrayList<String> playerNames;
  private ArrayList<Integer> playerScores;
  private ArrayList<Integer> playerScoresToNameIndexes;

  public LeaderboardServer() {
    this(address, port);
  }

  public LeaderboardServer(String address, int port)
  {
    if (address != null)
      this.address = address;
    if (port > 0)
      this.port = port;

    playerNumb = 0;
    playerNames = new ArrayList<String>();
    playerScores = new ArrayList<Integer>();
    playerScoresToNameIndexes = new ArrayList<Integer>();
    start();
  }

  public void run()
  {
    while (true)
    {
      try{
        DatagramSocket server = new DatagramSocket(port);

        while (true)
        {
          byte[] client = new byte[128];
          DatagramPacket packet = new DatagramPacket(client, client.length, InetAddress.getByName(address), port);

          server.receive(packet);

          String[] request = new String(packet.getData()).split(" ");
          if (request[0].equals("score"))
          {
            byte[] buffer = playerScores.get(playerScoresToNameIndexes.indexOf(playerNames.indexOf(request[1]))).toString().getBytes();
            DatagramPacket scorePacket = new DatagramPacket(
              buffer,
              buffer.length,
              packet.getAddress(),
              packet.getPort());
            server.send(scorePacket);
          }
          if (request[0].equals("leaderboard"))
          {
            System.out.println("Demande leaderboard!");
            byte[] buffer = null;
            if (request.length > 1 || request[1].equals("byScore"))
              buffer = ToString((LeaderboardSort)new SortByScore()).getBytes();
            else if (request.length > 1 && request[1].equals("byName"))
              buffer = ToString((LeaderboardSort)new SortByName()).getBytes();

            DatagramPacket scorePacket = new DatagramPacket(
              buffer,
              buffer.length,
              packet.getAddress(),
              packet.getPort());
            server.send(scorePacket);
          }
          if (request[0].equals("add") || request[0].equals("set") || request[0].equals("reset"))
          {
            if (request.length > 1)
            {
              String name = request[1];
              if (request.length > 2)
              {
                System.out.println("Demande d'ajout de " + request[1] + " " + request[2]);
                addPlayer(name, Integer.parseInt(request[2]));
              }
              else
              {
                System.out.println("Demande d'ajout de " + request[1] + "!");
                addPlayer(name, 0);
              }
            }
            byte[] buffer = new String("ok").getBytes();
            DatagramPacket reponse = new DatagramPacket(
              buffer,
              buffer.length,
              packet.getAddress(),
              packet.getPort());
            server.send(reponse);
          }
        }
      }
      catch (Exception e)
      {
        System.out.println("Erreur server !\n" + e.toString());
      }
      try {
        System.out.println("Tentative de relancement du serveur");
        Thread.sleep(500);
        System.out.println("Tentative de relancement du serveur.");
        Thread.sleep(500);
        System.out.println("Tentative de relancement du serveur..");
        Thread.sleep(500);
        System.out.println("Tentative de relancement du serveur...");
        Thread.sleep(500);
      }
      catch (Exception e) {

      }
    }
  }

  private void addPlayer(String name, int score)
  {
    System.out.println("Pseudo " + name + " score : " + score);
    if (!playerNames.contains(name))
    {
      playerNames.add(name);
      playerScores.add(score);
      playerScoresToNameIndexes.add(playerNumb);
      playerNumb++;
    }
    else
    {
      updatePlayer(name, score);
    }
  }

  private void updatePlayer(String name, int score)
  {
    int scoreAct = playerScores.get(playerScoresToNameIndexes.indexOf(playerNames.indexOf(name)));
    if (scoreAct < score)
      playerScores.set(playerScoresToNameIndexes.indexOf(playerNames.indexOf(name)), score);
  }

  private void removePlayer(String name, int score)
  {
    if (!playerNames.contains(name))
      return;
    playerScores.remove(playerNames.indexOf(name));
    playerScoresToNameIndexes.remove(playerNames.indexOf(name));
    playerNames.remove(name);
  }

  public String ToString(LeaderboardSort sort)
  {
    sortPlayers(sort);
    String s = "";
    for (int i = 0; i < playerScores.size(); i++)
    {
      s += playerNames.get(playerScoresToNameIndexes.get(i)) + " " + playerScores.get(i) + " ";
    }

    return s;
  }

  private void sortPlayers(LeaderboardSort sort)
  {
    sort.Sort(playerNames, playerScores, playerScoresToNameIndexes);
  }

  private interface LeaderboardSort
  {
    public void Sort(ArrayList<String> names, ArrayList<Integer> scores, ArrayList<Integer> scoresToNameIndexes);
  }

  public class SortByScore implements LeaderboardSort
  {
    public void Sort(ArrayList<String> names, ArrayList<Integer> scores, ArrayList<Integer> scoresToNameIndexes)
    {
      for (int i = 0; i < names.size(); i++)
      {
        for (int j = i; j >= 1; j--)
        {
          if (playerScores.get(j - 1) > playerScores.get(j))
            continue;
          int s = playerScores.get(j - 1);
          playerScores.set(j - 1, playerScores.get(j));
          playerScores.set(j, s);

          s = playerScoresToNameIndexes.get(j - 1);
          playerScoresToNameIndexes.set(j - 1, playerScoresToNameIndexes.get(j));
          playerScoresToNameIndexes.set(j, s);
        }
      }
    }
  }
  public class SortByName implements LeaderboardSort
  {
    public void Sort(ArrayList<String> names, ArrayList<Integer> scores, ArrayList<Integer> scoresToNameIndexes)
    {
      for (int i = 0; i < names.size(); i++)
      {
        for (int j = i; j >= 1; j--)
        {
          if (names.get(j - 1).compareTo(names.get(j)) >= 0)
            continue;
          int s = playerScores.get(j - 1);
          playerScores.set(j - 1, playerScores.get(j));
          playerScores.set(j, s);

          s = playerScoresToNameIndexes.get(j - 1);
          playerScoresToNameIndexes.set(j - 1, playerScoresToNameIndexes.get(j));
          playerScoresToNameIndexes.set(j, s);
        }
      }
    }
  }
}
