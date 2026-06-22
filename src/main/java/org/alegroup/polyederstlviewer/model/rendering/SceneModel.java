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
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON
 */
public class SceneModel {

    private static SceneModel INSTANCE;

    private Group objectsGroup;
    private PerspectiveCamera camera;

    private double lastMouseX;
    private double lastMouseY;
    private final List<TransformationState> undoStack = new ArrayList<>();
    private final List<TransformationState> redoStack = new ArrayList<>();

    private Group cameraPivot;
    private Rotate orbitX;
    private Rotate orbitY;

    private Group gridGroup;
    private Group axesGroup;
    private MeshView currentMeshView;

    private SubScene subScene;

    private PhongMaterial shadedMaterial;
    private PhongMaterial solidMaterial;

    private SubScene makeNewSubScene(){

        // creates a new basic scene
        // Group containing lights etc
        Group standardGroup = new Group();
        camera = new PerspectiveCamera(true);


        // Licht
        AmbientLight ambientLight = new AmbientLight(Color.rgb(80, 80, 80));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(300);
        pointLight.setTranslateZ(300);
        standardGroup.getChildren().addAll(ambientLight, pointLight);

        // Rotation
        // Kamera-Rig
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateZ(-500);  // Abstand vom Ursprung

        orbitX = new Rotate(-20, Rotate.X_AXIS); // leichte Draufsicht als Start
        orbitY = new Rotate(0,  Rotate.Y_AXIS);

        // Group containing the camera
        cameraPivot = new Group(camera);
        cameraPivot.getTransforms().addAll(orbitY, orbitX); // Y zuerst!

        Group sceneRoot = new Group(standardGroup, cameraPivot, this.objectsGroup);

        subScene = new SubScene(sceneRoot, 100, 100, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Diese zwei Zeilen fehlen!
        subScene.setFill(Color.rgb(40, 40, 40));

        subScene.setOnMousePressed(e -> {
            saveState();

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
            saveState();
            double newZ = camera.getTranslateZ() + e.getDeltaY() * 0.5;
            camera.setTranslateZ(Math.min(-10, newZ)); // nie ins Objekt rein
        });

        // Grid
        renderSceneElements(standardGroup, camera);

        return subScene;
    }

    public SceneModel(){
        this.objectsGroup = new Group();

        solidMaterial = new PhongMaterial();
        solidMaterial.setDiffuseColor(Color.LIGHTGRAY);
        solidMaterial.setSpecularColor(Color.BLACK);

        shadedMaterial = new PhongMaterial();
        shadedMaterial.setDiffuseColor(Color.LIGHTGRAY);
        shadedMaterial.setSpecularColor(Color.WHITE);
        shadedMaterial.setSpecularPower(128);
    }

    public static SceneModel getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SceneModel();
        }

        return INSTANCE;
    }

    private void renderSceneElements(Group root, PerspectiveCamera camera) {
        renderGrid(camera);
        renderAxes(camera);

        root.getChildren().addAll(gridGroup, axesGroup);
    }

    private void renderGrid(PerspectiveCamera camera) {

        gridGroup = new Group();

        final int SIZE = 250;
        final int MINOR = 5;
        final int MAJOR = 50;
        final double THICKNESS = 0.0006;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(0.1);

        for (int i = -SIZE; i <= SIZE; i += MINOR) {

            boolean isMajor = (i % MAJOR == 0);

            if (i != 0) {
                Box lx = new Box(1, 1, SIZE * 2.0);
                lx.setTranslateX(i);
                lx.setMaterial(isMajor
                        ? flatColor(Color.rgb(110,110,110))
                        : flatColor(Color.rgb(60,60,60)));
                thicknessUpdaters.add(() -> {
                    lx.setWidth(t.get());
                    lx.setHeight(t.get());
                });
                gridGroup.getChildren().add(lx);
            }

            if (i != 0) {
                Box lz = new Box(SIZE * 2.0, 1, 1);
                lz.setTranslateZ(i);
                lz.setMaterial(isMajor
                        ? flatColor(Color.rgb(110,110,110))
                        : flatColor(Color.rgb(60,60,60)));
                thicknessUpdaters.add(() -> {
                    lz.setHeight(t.get());
                    lz.setDepth(t.get());
                });
                gridGroup.getChildren().add(lz);
            }
        }

        camera.translateZProperty().addListener((obs, old, z) -> {
            t.set(Math.abs(z.doubleValue()) * THICKNESS);
            thicknessUpdaters.forEach(Runnable::run);
        });

        t.set(Math.abs(camera.getTranslateZ()) * THICKNESS);
        thicknessUpdaters.forEach(Runnable::run);
    }

    private void renderAxes(PerspectiveCamera camera) {

        axesGroup = new Group();

        final int SIZE = 250;
        final double THICKNESS = 0.0006;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(0.1);

        // X-Achse (rot)
        Box xAxis = new Box(SIZE * 2.0, 1, 1);
        xAxis.setMaterial(flatColor(Color.RED));
        thicknessUpdaters.add(() -> {
            xAxis.setHeight(t.get());
            xAxis.setDepth(t.get());
        });
        axesGroup.getChildren().add(xAxis);

        // Z-Achse (blau)
        Box zAxis = new Box(1, 1, SIZE * 2.0);
        zAxis.setMaterial(flatColor(Color.DODGERBLUE));
        thicknessUpdaters.add(() -> {
            zAxis.setWidth(t.get());
            zAxis.setHeight(t.get());
        });
        axesGroup.getChildren().add(zAxis);

        // Y-Achse (grün)
        Box yAxis = new Box(1, SIZE * 2.0, 1);
        yAxis.setMaterial(flatColor(Color.LIMEGREEN));
        thicknessUpdaters.add(() -> {
            yAxis.setWidth(t.get());
            yAxis.setDepth(t.get());
        });
        axesGroup.getChildren().add(yAxis);

        camera.translateZProperty().addListener((obs, old, z) -> {
            t.set(Math.abs(z.doubleValue()) * THICKNESS);
            thicknessUpdaters.forEach(Runnable::run);
        });

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

        setSubScene(makeNewSubScene());
        subScene.widthProperty().bind(rootPane.widthProperty());
        subScene.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(subScene);
    }

    public void renderPolyhedron(Polyhedron poly) {

        objectsGroup.getChildren().clear();

        TriangleMesh mesh = poly.toTriangleMesh();
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(shadedMaterial);
        currentMeshView = meshView;
        currentMeshView.setScaleX(20);
        currentMeshView.setScaleY(20);
        currentMeshView.setScaleZ(20);

        objectsGroup.getChildren().add(meshView);
    }


    public void clearScene() {
        objectsGroup.getChildren().clear();
    }

    public void resetCamera() {
        // Kamera zurücksetzen
    }

    public void resetObjectTransform() {
        saveState(); // Zustand vorher speichern

        // Objekt zurücksetzen
        objectsGroup.setTranslateX(0);
        objectsGroup.setTranslateY(0);
        objectsGroup.setTranslateZ(0);
        objectsGroup.setRotate(0);

        // Kamera-Pivot zurücksetzen
        cameraPivot.setTranslateX(0);
        cameraPivot.setTranslateY(0);
        cameraPivot.setTranslateZ(0);

        // Orbit-Rotation zurücksetzen
        orbitX.setAngle(-20);
        orbitY.setAngle(0);

        // Kamera-Zoom zurücksetzen
        camera.setTranslateZ(-500);
    }


    public void setGridVisible(boolean visible) {
        gridGroup.setVisible(visible);
    }

    public void setAxesVisible(boolean visible) {
        axesGroup.setVisible(visible);
    }

    public void setRenderModeSolid() {
        if (currentMeshView == null) return;

        currentMeshView.setDrawMode(DrawMode.FILL);
        currentMeshView.setMaterial(solidMaterial);
    }

    public void setRenderModeShaded() {
        if (currentMeshView == null) return;

        currentMeshView.setDrawMode(DrawMode.FILL);
        currentMeshView.setMaterial(shadedMaterial);
    }

    public void exportScreenshot() {
        if (subScene == null) {
            System.err.println("No SubScene available for screenshot.");
            return;
        }

        WritableImage image = subScene.snapshot(new SnapshotParameters(),null);

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Screenshot");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );

        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("Screenshot saved: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveState() {
        undoStack.add(captureCurrentState());
        redoStack.clear();
    }


    public void undo() {
        if (undoStack.isEmpty()) return;

        redoStack.add(captureCurrentState());
        TransformationState prev = undoStack.remove(undoStack.size() - 1);
        applyState(prev);
    }


    public void redo() {
        if (redoStack.isEmpty()) return;

        undoStack.add(captureCurrentState());
        TransformationState next = redoStack.remove(redoStack.size() - 1);
        applyState(next);
    }


    private TransformationState captureCurrentState() {
        return new TransformationState(
                objectsGroup.getTranslateX(),
                objectsGroup.getTranslateY(),
                objectsGroup.getTranslateZ(),
                objectsGroup.getRotate(),

                cameraPivot.getTranslateX(),
                cameraPivot.getTranslateY(),
                cameraPivot.getTranslateZ(),

                orbitX.getAngle(),
                orbitY.getAngle(),

                camera.getTranslateZ()
        );
    }


    private void applyState(TransformationState s) {

        objectsGroup.setTranslateX(s.objX);
        objectsGroup.setTranslateY(s.objY);
        objectsGroup.setTranslateZ(s.objZ);
        objectsGroup.setRotate(s.objRotate);

        cameraPivot.setTranslateX(s.camX);
        cameraPivot.setTranslateY(s.camY);
        cameraPivot.setTranslateZ(s.camZ);

        orbitX.setAngle(s.orbitX);
        orbitY.setAngle(s.orbitY);

        camera.setTranslateZ(s.camZoom);
    }

    public void setSubScene (SubScene subScene)
    {
        this.subScene = subScene;
    }
}
