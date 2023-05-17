package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;

public class LeaderBoardsMenu extends Application {
    private final LeaderBoardsMenuController controller = new LeaderBoardsMenuController();

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = controller.buildRankingTable();

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.show();
    }
}
