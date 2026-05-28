package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class RenderingViewController {

    @FXML
    private AnchorPane rootPane;

    private SubScene subScene;

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


        // Rotation
        // Kamera-Rig
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateZ(-500);  // Abstand vom Ursprung

        Rotate orbitX = new Rotate(-20, Rotate.X_AXIS); // leichte Draufsicht als Start
        Rotate orbitY = new Rotate(0,  Rotate.Y_AXIS);

        Group cameraPivot = new Group(camera);
        cameraPivot.getTransforms().addAll(orbitY, orbitX); // Y zuerst!

        // Pivot zur Szene hinzufügen, NICHT root3D
        Group sceneRoot = new Group(root3D, cameraPivot);

        subScene = new SubScene(sceneRoot, 100, 100, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Diese zwei Zeilen fehlen!
        subScene.setFill(Color.rgb(40, 40, 40));
        subScene.widthProperty().bind(rootPane.widthProperty());
        subScene.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(subScene); // Das fehlt auch!

        // Mouse: Orbit
        subScene.setOnMousePressed(e -> {
            if(e.isMiddleButtonDown()){
                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }
        });

        subScene.setOnMouseDragged(e -> {
            if(e.isMiddleButtonDown()){
                double dx = e.getSceneX() - lastMouseX;
                double dy = e.getSceneY() - lastMouseY;

                orbitY.setAngle(orbitY.getAngle() + dx * 0.3);

                // Vertikale Rotation auf ±89° begrenzen → kein Überschlag
                double newX = orbitX.getAngle() - dy * 0.3;
                orbitX.setAngle(Math.max(-89, Math.min(89, newX)));

                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }
        });

        // Zoom: Kamera vor/zurück entlang ihrer Z-Achse
        subScene.setOnScroll(e -> {
            double newZ = camera.getTranslateZ() + e.getDeltaY() * 0.5;
            camera.setTranslateZ(Math.min(-10, newZ)); // nie ins Objekt rein
        });

        // Grid
        renderGrid(root3D, camera);
    }

    public void renderGrid(Group group, PerspectiveCamera camera) {

        final int SIZE = 250, MINOR = 5, MAJOR = 50;
        final double THICKNESS = 0.0006;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(0.1);

        for (int i = -SIZE; i <= SIZE; i += MINOR) {
            boolean isMajor   = (i % MAJOR == 0);
            boolean isOrigin  = (i == 0);

            // Linie parallel zur Z-Achse (verschoben auf X)
            Box lx = new Box(1, 1, SIZE * 2.0);
            lx.setTranslateX(i);
            lx.setMaterial(isOrigin ? flatColor(Color.RED)
                    : isMajor ? flatColor(Color.rgb(110, 110, 110))
                    : flatColor(Color.rgb(60,  60,  60)));
            thicknessUpdaters.add(() -> { lx.setWidth(t.get()); lx.setHeight(t.get()); });

            // Linie parallel zur X-Achse (verschoben auf Z)
            Box lz = new Box(SIZE * 2.0, 1, 1);
            lz.setTranslateZ(i);
            lz.setMaterial(isOrigin ? flatColor(Color.DODGERBLUE)
                    : isMajor ? flatColor(Color.rgb(110, 110, 110))
                    : flatColor(Color.rgb(60,  60,  60)));
            thicknessUpdaters.add(() -> { lz.setHeight(t.get()); lz.setDepth(t.get()); });

            group.getChildren().addAll(lx, lz);
        }

        // Y-Achse
        Box yAxis = new Box(1, SIZE * 2.0, 1);
        yAxis.setMaterial(flatColor(Color.LIMEGREEN));
        thicknessUpdaters.add(() -> { yAxis.setWidth(t.get()); yAxis.setDepth(t.get()); });
        group.getChildren().add(yAxis);

        // Thickness mit Kamerazoom koppeln

        camera.translateZProperty().addListener((obs, old, z) -> {
            t.set(Math.abs(z.doubleValue()) * THICKNESS);
            thicknessUpdaters.forEach(Runnable::run);
        });

        // NEU: Einmal beim Start ausführen, sonst bleiben Boxen auf width=1
        t.set(Math.abs(camera.getTranslateZ()) * THICKNESS);
        thicknessUpdaters.forEach(Runnable::run);
    }

    // 1x1-Pixel Self-Illumination = kein Lichteinfluss
    private PhongMaterial flatColor(Color color) {
        WritableImage img = new WritableImage(1, 1);
        img.getPixelWriter().setColor(0, 0, color);

        PhongMaterial m = new PhongMaterial();
        m.setDiffuseColor(Color.BLACK);       // Kein diffuses Licht
        m.setSelfIlluminationMap(img);        // Farbe kommt nur hiervon
        return m;
    }
}