package view;

import controller.DataBase;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.User;
import view.model.CircleButton;

public class LeaderBoardsMenuController {
    private final controller.LeaderBoardsMenuController controller = new controller.LeaderBoardsMenuController();

    private int levelToSortBY = 0;
    private VBox usersVbox;

    public BorderPane buildRankingTable() {
        BorderPane borderPane = getBorderPane();
        usersVbox = getVBox();
        insertUsersToTable(usersVbox);
        borderPane.setCenter(usersVbox);
        borderPane.setBottom(new HBox(getToggleButton(), getBackButton()));

        return borderPane;
    }

    private VBox getVBox() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.valueOf("CENTER"));
        vBox.setMaxWidth(450);
        return vBox;
    }

    private BorderPane getBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setMinHeight(700);
        borderPane.setMaxHeight(700);
        borderPane.setMaxWidth(600);
        borderPane.setMinWidth(600);
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }

    private void insertUsersToTable(VBox vBox) {
        User[] sortedRanks = controller.getSortedRanks(levelToSortBY);

        for (int index = 0; index < Math.min(controller.getLeaderBoarsSize(), sortedRanks.length); index++) {
            HBox hBox = getIndexHbox(index);
            Label number = getIndexLabel(index);
            Circle circle = getAvatarCircle(sortedRanks[index], 30);
            Label name = getNameLabel(sortedRanks[index].getUsername());
            Label highscore = getNumberLabel(sortedRanks[index].getHighScore());

            hBox.getChildren().add(number);
            hBox.getChildren().add(circle);
            hBox.getChildren().add(name);
            hBox.getChildren().add(highscore);
            vBox.getChildren().add(hBox);
        }
    }

    private HBox getIndexHbox(int index) {
        HBox hBox = new HBox();
        hBox.setMinHeight(65);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.valueOf("CENTER"));
        hBox.setStyle("-fx-background-color: #eeeddf");
        if (index == 0)
            hBox.setStyle("-fx-background-color: gold");
        if (index == 1)
            hBox.setStyle("-fx-background-color: silver");
        if (index == 2)
            hBox.setStyle("-fx-background-color: #cd7532");

        return hBox;
    }

    public Circle getAvatarCircle(User user, int radius) {
        Circle circle = CircleButton.getCircleIcon(new ImagePattern(user.getAvatar()), radius);

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Circle secondary = new Circle();
                secondary.setFill(new ImagePattern(user.getAvatar()));
                secondary.setRadius(radius);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("user info");
                alert.setGraphic(secondary);
                alert.setHeaderText(user.getUsername());
                alert.setContentText("highscore: " + user.getHighScore()
                        + "\n\ntime: " + user.getTime()
                        + "\n\nlevel: " + user.getLevel());
                alert.showAndWait();
            }
        });

        return circle;
    }

    private Label getNumberLabel(int num) {
        Label number = new Label(Integer.toString(num));
        number.setMinWidth(100);
        number.setAlignment(Pos.valueOf("CENTER"));
        number.setStyle("-fx-font-size: xx-large");

        return number;
    }

    private Label getIndexLabel(int index) {
        Label number = new Label(Integer.toString(index + 1));
        number.setMinWidth(30);
        number.setAlignment(Pos.valueOf("CENTER"));
        number.setStyle("-fx-font-size: xx-large");

        return number;
    }

    private Label getNameLabel(String input) {
        Label label = new Label(input);
        label.setMinWidth(120);
        label.setAlignment(Pos.valueOf("CENTER_LEFT"));
        label.setStyle("-fx-font-size: large");

        return label;
    }

    private Button getBackButton() {
        Button button = new Button("Back");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(LoginMenu.getStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return button;
    }

    private Button getToggleButton() {
        Button button = new Button("all");
        button.setMaxWidth(30);
        button.setMinWidth(30);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                levelToSortBY++;
                levelToSortBY %= 4;
                button.setText((levelToSortBY == 0) ? "all" : Integer.toString(levelToSortBY));

                usersVbox.getChildren().clear();
                insertUsersToTable(usersVbox);
            }
        });

        return button;
    }
}