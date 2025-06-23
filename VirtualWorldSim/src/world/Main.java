package world;

import gui.WorldFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(WorldFrame::new);
    }
}