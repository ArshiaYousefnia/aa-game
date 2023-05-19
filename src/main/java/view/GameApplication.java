package view;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameApplication extends Application {
    private final GameApplicationController controller = new GameApplicationController();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = controller.buildApp();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
//    private static final double WIDTH = 500, HEIGHT = 400, EARTH_RADIUS = 150;
//    private Rotate earthRotate;
//    private Circle earth;
//    private Pane root;
//    private Line line;
//    private int omega = 6;
//    private Group group;
//    private ArrayList<Timeline> TimeLines = new ArrayList<>();
//    private Timeline timeline;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//    @Override
//    public void start(Stage stage) throws Exception {
//
//        earth = new Circle(30);
//        earth.setCenterX(100);
//        earth.setCenterY(100);
//
//        Circle earth2 = new Circle(15);
//        earth2.setCenterX(90);
//        earth2.setCenterY(90);
////        line = new Line();
////        line.setStartX(250);
////        line.setStartY(200);
////        line.setEndX(250);
////        line.setEndY(250);
//        group = new Group();
//        group.getChildren().add(earth2);
//        //group.getChildren().add(line);
//
//        earthRotate = getRotation(0);
//        group.getTransforms().add(earthRotate);
//        timeline = getTimeline(earthRotate, omega, 0);
//        timeline.play();
//        root = new Pane(group);
//        Scene scene = new Scene(root, WIDTH, HEIGHT);
//        stage.setScene(scene);
//        stage.show();
//
//        ((Rotate) earthRotate).angleProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                System.out.println(1233);
//                for (int i = 0; i< group.getChildren().size(); i++) {
//                    for (int j = i + 1 ; j < group.getChildren().size(); j++) {
//                        if (group.getChildren().get(i).intersects(group.getChildren().get(j).getBoundsInParent())) {
//                            System.out.println("collision!!!!");
//                        }
//                    }
//                }
//            }
//        });
//
//
//        earth2.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                System.out.println(((Rotate) earthRotate).getAngle());
//                Circle circle = new Circle(3);
//                circle.setCenterX(90);
//                circle.setCenterY(90);
//                timeline.pause();
//                omega *= -1;
//                group.getChildren().add(circle);
//                earthRotate = getRotation(0);
//                group.getTransforms().set(0, earthRotate);
//                timeline = getTimeline(earthRotate, omega, earthRotate.getAngle());
//                timeline.play();
//            }
//        });
//    }
//
////    private void animate(Circle circle) {
////        circle.setCenterX(250);
////        circle.setCenterY(250);
////        earthRotate = new Rotate(0, 250, 200);
////        circle.getTransforms().add(earthRotate);
////        group.getChildren().add(circle);
////        Timeline timeline = new Timeline(
////                new KeyFrame(Duration.ZERO, new KeyValue(earthRotate.angleProperty(), 0)),
////                new KeyFrame(Duration.seconds(7), new KeyValue(earthRotate.angleProperty(),  360)));
////        timeline.setCycleCount(Timeline.INDEFINITE);
////        TimeLines.add(timeline);
////        timeline.play();
////    }
//
//    private Timeline getTimeline(Rotate rotate, double omega, double angle) {
//        Timeline timeline1 = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), angle)),
//                new KeyFrame(Duration.seconds(Math.abs(omega)), new KeyValue(rotate.angleProperty(),  angle + (omega < 0 ? -1 : 1) * 360)));
//        timeline1.setCycleCount(Timeline.INDEFINITE);
//        return timeline1;
//    }
//
//    private Rotate getRotation(double angle) {
//        Rotate rotate = new Rotate(angle, 250, 200);
//        return rotate;
//    }
//}
