package albumappnew;

/**
 * Created by girish on 06/10/16.
 */

public class AlbumAppMain {

    public static void main(String[] args) {
        final AlbumAppManager albumAppManager = new AlbumAppManager();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                albumAppManager.createAndShowGUI();

            }
        });
    }

}
