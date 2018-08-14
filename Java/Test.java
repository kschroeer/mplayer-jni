import java.util.Scanner;
import org.mplayer.MediaPlayer;

// Simple console player demo

public class Test {

  public static void main(String[] args) throws Exception {
    MediaPlayer player = new MediaPlayer();
    player.setFileName(args[0]);
    player.open();

    Scanner scanner = new Scanner(System.in);
    char c;

    do {
      System.out.println("a   pause\t\ts   stop");
      System.out.println("i   skip\t\tt   status");
      System.out.println("p   play\t\tw   rewind");
      System.out.println("r   resume\t\tx   exit");

      System.out.print(":");
      c = scanner.nextLine().charAt(0);
      System.out.println();

      switch (c) {
      case 'a':
        player.pauseOnly();
        break;
      case 'i':
        player.pause();
        player.setPosition(player.getPosition() + 30000);
        player.play();
        break;
      case 'p':
        player.play();
        break;
      case 'r':
        player.resume();
        break;
      case 's':
        player.stop();
        break;
      case 't':
        long pos = player.getPosition() / 1000;
        long len = player.getLength() / 1000;
        System.out.println(String.format(
          "%02d:%02d / %02d:%02d (%s)",
          pos / 60,
          pos % 60,
          len / 60,
          len % 60,
          player.getMode()
        ));
        System.out.println();
        break;
      case 'w':
        player.rewind();
        break;
      }
    } while (c != 'x');

    player.close();
  }

}
