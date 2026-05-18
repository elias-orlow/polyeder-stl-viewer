package org.alegroup.polyederstlviewer.View.MainWindow;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class RenderingViewController {

    @FXML
    private AnchorPane rootPane;

    private SubScene subScene;

    // Für Mouse-Delta (FIX #3)
    private double lastMouseX;
    private double lastMouseY;

    public void initialize() {

        Group root3D = new Group();

        // Licht
        AmbientLight ambientLight = new AmbientLight(Color.rgb(80, 80, 80));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(300);
        pointLight.setTranslateZ(300);
        root3D.getChildren().addAll(ambientLight, pointLight);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);

        // Wir schauen von unten nach oben hoch auf die Objekte
        camera.setTranslateZ(-500);

        // Test-Box mit Material (sonst: grauer Default)
        Box box = new Box(20, 1, 1);
        box.setTranslateX(20);
        Box box2 = new Box(1, 20, 1);
        box2.setTranslateY(20);
        Box box3 = new Box(1, 1, 20);
        box3.setTranslateZ(20);
        PhongMaterial material = new PhongMaterial();
        PhongMaterial material2 = new PhongMaterial();
        PhongMaterial material3 = new PhongMaterial();
        material.setDiffuseColor(Color.CORNFLOWERBLUE);
        material2.setDiffuseColor(Color.RED);
        material3.setDiffuseColor(Color.GREEN);
        material.setSpecularColor(Color.WHITE);
        material2.setSpecularColor(Color.WHITE);
        material3.setSpecularColor(Color.WHITE);
        box.setMaterial(material);
        box2.setMaterial(material2);
        box3.setMaterial(material3);
        root3D.getChildren().addAll(box, box2, box3);

        // SubScene
        subScene = new SubScene(root3D, 100, 100, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        subScene.setFill(Color.rgb(40, 40, 40));

        subScene.widthProperty().bind(rootPane.widthProperty());
        subScene.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(subScene);

        // Rotation
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        root3D.getTransforms().addAll(rotateX, rotateY);

        subScene.setOnMousePressed(e -> {
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;

            rotateY.setAngle(rotateY.getAngle() - dx * 0.3);
            rotateX.setAngle(rotateX.getAngle() + dy * 0.3);

            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        //Zoom
        subScene.setOnScroll(e -> {
            camera.setTranslateZ(camera.getTranslateZ() + e.getDeltaY() * 0.5);
        });
    }
}