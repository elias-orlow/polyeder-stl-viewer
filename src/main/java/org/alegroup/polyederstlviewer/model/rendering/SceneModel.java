package org.alegroup.polyederstlviewer.model.rendering;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;

import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON
 */
public class SceneModel {

    private static SceneModel INSTANCE;

    private Group objectsGroup;

    private double lastMouseX;
    private double lastMouseY;

    private SubScene makeNewSubScene(){

        // creates a new basic scene
        // Group containing lights etc
        Group standardGroup = new Group();

        // Licht
        AmbientLight ambientLight = new AmbientLight(Color.rgb(80, 80, 80));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(300);
        pointLight.setTranslateZ(300);
        standardGroup.getChildren().addAll(ambientLight, pointLight);

        // Rotation
        // Kamera-Rig
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateZ(-500);  // Abstand vom Ursprung

        Rotate orbitX = new Rotate(-20, Rotate.X_AXIS); // leichte Draufsicht als Start
        Rotate orbitY = new Rotate(0,  Rotate.Y_AXIS);

        // Group containing the camera
        Group cameraPivot = new Group(camera);
        cameraPivot.getTransforms().addAll(orbitY, orbitX); // Y zuerst!

        Group sceneRoot = new Group(standardGroup, cameraPivot, this.objectsGroup);

        SubScene subScene = new SubScene(sceneRoot, 100, 100, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Diese zwei Zeilen fehlen!
        subScene.setFill(Color.rgb(40, 40, 40));

        subScene.setOnMousePressed(e -> {
            // Mouse: Orbit
            if(e.isMiddleButtonDown()){
                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }

            // Mouse: drag
            if(e.isSecondaryButtonDown()){
                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }
        });

        subScene.setOnMouseDragged(e -> {
            // Mouse: Orbit
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

            if (e.isSecondaryButtonDown()) {
                double dx = e.getSceneX() - lastMouseX;
                double dy = e.getSceneY() - lastMouseY;

                double panSpeed = Math.abs(camera.getTranslateZ()) * 0.0005;

                // Kamera-eigene Achsen in Weltkoordinaten umrechnen
                Point3D right = cameraPivot.localToParent(1, 0, 0)
                        .subtract(cameraPivot.localToParent(0, 0, 0));
                Point3D up    = cameraPivot.localToParent(0, 1, 0)
                        .subtract(cameraPivot.localToParent(0, 0, 0));

                // Pivot entlang dieser Achsen verschieben
                cameraPivot.setTranslateX(cameraPivot.getTranslateX() - dx * panSpeed * right.getX() - dy * panSpeed * up.getX());
                cameraPivot.setTranslateY(cameraPivot.getTranslateY() - dx * panSpeed * right.getY() - dy * panSpeed * up.getY());
                cameraPivot.setTranslateZ(cameraPivot.getTranslateZ() - dx * panSpeed * right.getZ() - dy * panSpeed * up.getZ());

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
        renderGrid(standardGroup, camera);

        return subScene;
    }

    public SceneModel(){
        this.objectsGroup = new Group();
    }

    public static SceneModel getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SceneModel();
        }

        return INSTANCE;
    }

    private void renderGrid(Group group, PerspectiveCamera camera) {

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

    public void addSubSceneToPane(AnchorPane rootPane){

        SubScene subScene = makeNewSubScene();
        subScene.widthProperty().bind(rootPane.widthProperty());
        subScene.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(subScene);
    }

    public void renderObject(){

        // clear first, only ever render a single object
        this.objectsGroup.getChildren().clear();

        Box box = new Box(20, 20, 20);
        this.objectsGroup.getChildren().add(box);
    }

    public void renderPolyhedron(Polyhedron poly) {

        // 1. Gruppe leeren
        this.objectsGroup.getChildren().clear();

        // 2. Polyhedron → TriangleMesh
        TriangleMesh mesh = poly.toTriangleMesh();

        // 3. MeshView erzeugen
        MeshView meshView = new MeshView(mesh);
        // Scale
        meshView.setScaleX(10);
        meshView.setScaleY(10);
        meshView.setScaleZ(10);

        // Optional: Material
        PhongMaterial mat = new PhongMaterial(Color.LIGHTGRAY);
        meshView.setMaterial(mat);

        // Optional: Kantendarstellung
        meshView.setDrawMode(DrawMode.FILL);

        // 4. Mesh in die Szene einfügen
        this.objectsGroup.getChildren().add(meshView);
    }

}
