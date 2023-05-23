package view;

import javafx.scene.media.Media;

public class TrackUtility {
    private static Media[] media = new Media[3];

    static {
        media[0] = new Media(LoginMenu.class.getResource("/assets/tracks/1.mp3").toExternalForm());
        media[1] = new Media(LoginMenu.class.getResource("/assets/tracks/2.mp3").toExternalForm());
        media[2] = new Media(LoginMenu.class.getResource("/assets/tracks/3.mp3").toExternalForm());
    }

    public static Media getMedia(int number) {
        return media[number - 1];
    }
}
