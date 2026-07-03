package org.alegroup.polyederstlviewer.model.rendering;

import javafx.beans.property.*;
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
import org.alegroup.polyederstlviewer.constants.SceneModelConstants;
import org.alegroup.polyederstlviewer.model.client.RotateObjectJSON;
import org.alegroup.polyederstlviewer.model.client.TranslateObjectJSON;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
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
    private Rotate rotateX;
    private Rotate rotateY;
    private Rotate rotateZ;

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

    public ObjectProperty<STLParseResult> poly = new SimpleObjectProperty<>();

    private SubScene makeNewSubScene(){

        // creates a new basic scene
        // Group containing lights etc
        Group standardGroup = new Group();
        camera = new PerspectiveCamera(SceneModelConstants.CAMERA_FIXED_EYE_AT_ZERO);


        // Licht
        AmbientLight ambientLight = new AmbientLight(Color.rgb(SceneModelConstants.AMBIENT_LIGHT_RGB_VALUE, SceneModelConstants.AMBIENT_LIGHT_RGB_VALUE, SceneModelConstants.AMBIENT_LIGHT_RGB_VALUE));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(SceneModelConstants.POINT_LIGHT_TRANSLATE_X);
        pointLight.setTranslateY(SceneModelConstants.POINT_LIGHT_TRANSLATE_Y);
        pointLight.setTranslateZ(SceneModelConstants.POINT_LIGHT_TRANSLATE_Z);
        standardGroup.getChildren().addAll(ambientLight, pointLight);

        // Rotation
        // Kamera-Rig
        camera.setNearClip(SceneModelConstants.CAMERA_NEAR_CLIP);
        camera.setFarClip(SceneModelConstants.CAMERA_FAR_CLIP);
        camera.setTranslateZ(SceneModelConstants.CAMERA_INITIAL_TRANSLATE_Z);  // Abstand vom Ursprung

        orbitX = new Rotate(SceneModelConstants.ORBIT_X_INITIAL_ANGLE, Rotate.X_AXIS); // leichte Draufsicht als Start
        orbitY = new Rotate(SceneModelConstants.ORBIT_Y_INITIAL_ANGLE,  Rotate.Y_AXIS);

        // Group containing the camera
        cameraPivot = new Group(camera);
        cameraPivot.getTransforms().addAll(orbitY, orbitX); // Y zuerst!

        Group sceneRoot = new Group(standardGroup, cameraPivot, this.objectsGroup);

        subScene = new SubScene(sceneRoot, SceneModelConstants.SUBSCENE_INITIAL_WIDTH, SceneModelConstants.SUBSCENE_INITIAL_HEIGHT, SceneModelConstants.SUBSCENE_DEPTH_BUFFER_ENABLED, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Diese zwei Zeilen fehlen!
        subScene.setFill(Color.rgb(SceneModelConstants.SUBSCENE_BACKGROUND_RGB_VALUE, SceneModelConstants.SUBSCENE_BACKGROUND_RGB_VALUE, SceneModelConstants.SUBSCENE_BACKGROUND_RGB_VALUE));

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

                orbitY.setAngle(orbitY.getAngle() + dx * SceneModelConstants.ORBIT_ROTATION_SPEED);

                // Vertikale Rotation auf ±89° begrenzen → kein Überschlag
                double newX = orbitX.getAngle() - dy * SceneModelConstants.ORBIT_ROTATION_SPEED;
                orbitX.setAngle(Math.max(SceneModelConstants.ORBIT_X_MIN_ANGLE, Math.min(SceneModelConstants.ORBIT_X_MAX_ANGLE, newX)));

                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }

            if (e.isSecondaryButtonDown()) {
                double dx = e.getSceneX() - lastMouseX;
                double dy = e.getSceneY() - lastMouseY;

                double panSpeed = Math.abs(camera.getTranslateZ()) * SceneModelConstants.PAN_SPEED_FACTOR;

                // Kamera-eigene Achsen in Weltkoordinaten umrechnen
                Point3D right = cameraPivot.localToParent(SceneModelConstants.UNIT_COORDINATE, SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE)
                        .subtract(cameraPivot.localToParent(SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE));
                Point3D up    = cameraPivot.localToParent(SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.UNIT_COORDINATE, SceneModelConstants.ZERO_COORDINATE)
                        .subtract(cameraPivot.localToParent(SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE));

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
            double newZ = camera.getTranslateZ() + e.getDeltaY() * SceneModelConstants.SCROLL_ZOOM_FACTOR;
            camera.setTranslateZ(Math.min(SceneModelConstants.CAMERA_MIN_TRANSLATE_Z, newZ)); // nie ins Objekt rein
        });

        // Grid
        renderSceneElements(standardGroup, camera);

        return subScene;
    }

    public SceneModel(){

        this.objectsGroup = new Group();
        this.rotateX = new Rotate(SceneModelConstants.NO_ROTATION_ANGLE, Rotate.X_AXIS);
        this.rotateY = new Rotate(SceneModelConstants.NO_ROTATION_ANGLE, Rotate.Y_AXIS);
        this.rotateZ = new Rotate(SceneModelConstants.NO_ROTATION_ANGLE, Rotate.Z_AXIS);
        this.objectsGroup.getTransforms().addAll(rotateX, rotateY, rotateZ);

        solidMaterial = new PhongMaterial();
        solidMaterial.setDiffuseColor(Color.LIGHTGRAY);
        solidMaterial.setSpecularColor(Color.BLACK);

        shadedMaterial = new PhongMaterial();
        shadedMaterial.setDiffuseColor(Color.LIGHTGRAY);
        shadedMaterial.setSpecularColor(Color.WHITE);
        shadedMaterial.setSpecularPower(SceneModelConstants.SHADED_MATERIAL_SPECULAR_POWER);
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

        final int SIZE = SceneModelConstants.GRID_SIZE;
        final int MINOR = SceneModelConstants.GRID_MINOR_STEP;
        final int MAJOR = SceneModelConstants.GRID_MAJOR_STEP;
        final double THICKNESS = SceneModelConstants.GRID_THICKNESS_FACTOR;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(SceneModelConstants.INITIAL_LINE_THICKNESS);

        for (int i = -SIZE; i <= SIZE; i += MINOR) {

            boolean isMajor = (i % MAJOR == SceneModelConstants.ORIGIN_INDEX);

            if (i != SceneModelConstants.ORIGIN_INDEX) {
                Box lx = new Box(SceneModelConstants.BOX_THIN_SIDE, SceneModelConstants.BOX_THIN_SIDE, SIZE * SceneModelConstants.FULL_LENGTH_MULTIPLIER);
                lx.setTranslateX(i);
                lx.setMaterial(isMajor
                        ? flatColor(Color.rgb(SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE))
                        : flatColor(Color.rgb(SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE)));
                thicknessUpdaters.add(() -> {
                    lx.setWidth(t.get());
                    lx.setHeight(t.get());
                });
                gridGroup.getChildren().add(lx);
            }

            if (i != SceneModelConstants.ORIGIN_INDEX) {
                Box lz = new Box(SIZE * SceneModelConstants.FULL_LENGTH_MULTIPLIER, SceneModelConstants.BOX_THIN_SIDE, SceneModelConstants.BOX_THIN_SIDE);
                lz.setTranslateZ(i);
                lz.setMaterial(isMajor
                        ? flatColor(Color.rgb(SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MAJOR_COLOR_RGB_VALUE))
                        : flatColor(Color.rgb(SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE, SceneModelConstants.GRID_MINOR_COLOR_RGB_VALUE)));
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

        final int SIZE = SceneModelConstants.GRID_SIZE;
        final double THICKNESS = SceneModelConstants.GRID_THICKNESS_FACTOR;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(SceneModelConstants.INITIAL_LINE_THICKNESS);

        // X-Achse (rot)
        Box xAxis = new Box(SIZE * SceneModelConstants.FULL_LENGTH_MULTIPLIER, SceneModelConstants.BOX_THIN_SIDE, SceneModelConstants.BOX_THIN_SIDE);
        xAxis.setMaterial(flatColor(Color.RED));
        thicknessUpdaters.add(() -> {
            xAxis.setHeight(t.get());
            xAxis.setDepth(t.get());
        });
        axesGroup.getChildren().add(xAxis);

        // Z-Achse (blau)
        Box zAxis = new Box(SceneModelConstants.BOX_THIN_SIDE, SceneModelConstants.BOX_THIN_SIDE, SIZE * SceneModelConstants.FULL_LENGTH_MULTIPLIER);
        zAxis.setMaterial(flatColor(Color.DODGERBLUE));
        thicknessUpdaters.add(() -> {
            zAxis.setWidth(t.get());
            zAxis.setHeight(t.get());
        });
        axesGroup.getChildren().add(zAxis);

        // Y-Achse (grün)
        Box yAxis = new Box(SceneModelConstants.BOX_THIN_SIDE, SIZE * SceneModelConstants.FULL_LENGTH_MULTIPLIER, SceneModelConstants.BOX_THIN_SIDE);
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
        WritableImage img = new WritableImage(SceneModelConstants.FLAT_COLOR_IMAGE_WIDTH, SceneModelConstants.FLAT_COLOR_IMAGE_HEIGHT);
        img.getPixelWriter().setColor(SceneModelConstants.FLAT_COLOR_PIXEL_X, SceneModelConstants.FLAT_COLOR_PIXEL_Y, color);

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

    public void renderPolyhedron(STLParseResult poly) {

        objectsGroup.getChildren().clear();
        this.poly.set(poly);

        TriangleMesh mesh = poly.getPolyhedron().toTriangleMesh();
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(shadedMaterial);
        currentMeshView = meshView;
        currentMeshView.setScaleX(SceneModelConstants.DEFAULT_MESH_SCALE);
        currentMeshView.setScaleY(SceneModelConstants.DEFAULT_MESH_SCALE);
        currentMeshView.setScaleZ(SceneModelConstants.DEFAULT_MESH_SCALE);

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
        objectsGroup.setTranslateX(SceneModelConstants.RESET_TRANSLATE_VALUE);
        objectsGroup.setTranslateY(SceneModelConstants.RESET_TRANSLATE_VALUE);
        objectsGroup.setTranslateZ(SceneModelConstants.RESET_TRANSLATE_VALUE);

        // Kamera-Pivot zurücksetzen
        cameraPivot.setTranslateX(SceneModelConstants.RESET_TRANSLATE_VALUE);
        cameraPivot.setTranslateY(SceneModelConstants.RESET_TRANSLATE_VALUE);
        cameraPivot.setTranslateZ(SceneModelConstants.RESET_TRANSLATE_VALUE);

        // Orbit-Rotation zurücksetzen
        orbitX.setAngle(SceneModelConstants.ORBIT_X_INITIAL_ANGLE);
        orbitY.setAngle(SceneModelConstants.ORBIT_Y_INITIAL_ANGLE);

        // Kamera-Zoom zurücksetzen
        camera.setTranslateZ(SceneModelConstants.CAMERA_INITIAL_TRANSLATE_Z);
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
            System.err.println(SceneModelConstants.NO_SUBSCENE_SCREENSHOT_MESSAGE);
            return;
        }

        WritableImage image = subScene.snapshot(new SnapshotParameters(),null);

        FileChooser chooser = new FileChooser();
        chooser.setTitle(SceneModelConstants.SCREENSHOT_FILE_CHOOSER_TITLE);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(SceneModelConstants.PNG_IMAGE_DESCRIPTION, SceneModelConstants.PNG_EXTENSION_PATTERN)
        );

        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), SceneModelConstants.PNG_FORMAT_NAME, file);
                System.out.println(SceneModelConstants.SCREENSHOT_SAVED_MESSAGE_PREFIX + file.getAbsolutePath());
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
        TransformationState prev = undoStack.remove(undoStack.size() - SceneModelConstants.LAST_STACK_INDEX_OFFSET);
        applyState(prev);
    }


    public void redo() {
        if (redoStack.isEmpty()) return;

        undoStack.add(captureCurrentState());
        TransformationState next = redoStack.remove(redoStack.size() - SceneModelConstants.LAST_STACK_INDEX_OFFSET);
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

    public void translateObject(TranslateObjectJSON translateObject){

        if(this.objectsGroup.getChildren().isEmpty() || translateObject == null){
            return;
        }

        if(translateObject.getTranslateX() == SceneModelConstants.RESET_TRANSLATE_VALUE && translateObject.getTranslateY() == SceneModelConstants.RESET_TRANSLATE_VALUE && translateObject.getTranslateZ() == SceneModelConstants.RESET_TRANSLATE_VALUE){
            resetObjectTransform();
        }else{
            this.objectsGroup.setTranslateX(this.objectsGroup.getTranslateX() + translateObject.getTranslateX());
            this.objectsGroup.setTranslateY(this.objectsGroup.getTranslateY() + translateObject.getTranslateY());
            this.objectsGroup.setTranslateZ(this.objectsGroup.getTranslateZ() + translateObject.getTranslateZ());
        }
    }

    public void rotateObject(RotateObjectJSON rotateObject){

        if(this.objectsGroup.getChildren().isEmpty() || rotateObject == null){
            return;
        }

        if(rotateObject.getRotateX() == SceneModelConstants.NO_ROTATION_ANGLE && rotateObject.getRotateY() == SceneModelConstants.NO_ROTATION_ANGLE && rotateObject.getRotateZ() == SceneModelConstants.NO_ROTATION_ANGLE){
            this.rotateX.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
            this.rotateY.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
            this.rotateZ.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
        }else{

            this.rotateX.setAngle(rotateX.getAngle() + rotateObject.getRotateX());
            this.rotateY.setAngle(rotateY.getAngle() + rotateObject.getRotateY());
            this.rotateZ.setAngle(rotateZ.getAngle() + rotateObject.getRotateZ());
        }
    }

    public void zoomBy(double delta) {
        double newZ = camera.getTranslateZ() + delta * SceneModelConstants.PROGRAMMATIC_ZOOM_FACTOR;
        camera.setTranslateZ(Math.min(SceneModelConstants.CAMERA_MIN_TRANSLATE_Z, newZ));
    }

    public void setPolyColor(Color color) {
        if (currentMeshView == null) return;

        shadedMaterial.setDiffuseColor(color);
        solidMaterial.setDiffuseColor(color);

        currentMeshView.setMaterial(shadedMaterial);
    }
}

