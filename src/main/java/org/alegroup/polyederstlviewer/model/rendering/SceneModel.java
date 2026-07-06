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
 * Represents the central 3D scene model of the STL viewer.
 * <p>
 * This class manages the rendered STL object, camera movement, grid and axes,
 * object transformations, undo and redo states, render modes, screenshots,
 * and programmatic object manipulation.
 * <p>
 * The class is implemented as a singleton so that the application uses one
 * shared scene model instance.
 *
 * @precondition none
 * @postcondition A central scene model for 3D rendering is available
 */
public class SceneModel
{

    /**
     * Singleton instance of the scene model.
     */
    private static SceneModel INSTANCE;

    /**
     * Group containing all currently rendered 3D objects.
     */
    private Group objectsGroup;

    /**
     * Rotation transform around the X axis for the rendered object.
     */
    private Rotate rotateX;

    /**
     * Rotation transform around the Y axis for the rendered object.
     */
    private Rotate rotateY;

    /**
     * Rotation transform around the Z axis for the rendered object.
     */
    private Rotate rotateZ;

    /**
     * Perspective camera used to view the 3D scene.
     */
    private PerspectiveCamera camera;

    /**
     * Last stored mouse X position used for dragging and orbiting.
     */
    private double lastMouseX;

    /**
     * Last stored mouse Y position used for dragging and orbiting.
     */
    private double lastMouseY;

    /**
     * Stack containing previous transformation states for undo operations.
     */
    private final List<TransformationState> undoStack = new ArrayList<>();

    /**
     * Stack containing reverted transformation states for redo operations.
     */
    private final List<TransformationState> redoStack = new ArrayList<>();

    /**
     * Pivot group used to move and rotate the camera around the scene.
     */
    private Group cameraPivot;

    /**
     * Orbit rotation around the X axis for camera movement.
     */
    private Rotate orbitX;

    /**
     * Orbit rotation around the Y axis for camera movement.
     */
    private Rotate orbitY;

    /**
     * Group containing the grid elements.
     */
    private Group gridGroup;

    /**
     * Group containing the coordinate axes.
     */
    private Group axesGroup;

    /**
     * Currently rendered mesh view of the STL object.
     */
    private MeshView currentMeshView;

    /**
     * SubScene used for displaying the 3D content.
     */
    private SubScene subScene;

    /**
     * Material used for shaded rendering.
     */
    private PhongMaterial shadedMaterial;

    /**
     * Material used for solid rendering.
     */
    private PhongMaterial solidMaterial;

    /**
     * Observable property containing the currently parsed STL result.
     */
    public ObjectProperty<STLParseResult> poly = new SimpleObjectProperty<>();

    /**
     * Creates and configures a new 3D subscene.
     * <p>
     * The subscene contains camera, lights, scene objects, grid, axes,
     * mouse controls for orbiting and panning, and scroll controls for zooming.
     *
     * @return the newly created subscene
     * @precondition objectsGroup != null
     * @postcondition A configured non-null SubScene is returned
     */
    private SubScene makeNewSubScene ()
    {

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
        orbitY = new Rotate(SceneModelConstants.ORBIT_Y_INITIAL_ANGLE, Rotate.Y_AXIS);

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
            if (e.isMiddleButtonDown())
            {
                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }

            // Mouse: drag
            if (e.isSecondaryButtonDown())
            {
                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }
        });

        subScene.setOnMouseDragged(e -> {
            // Mouse: Orbit
            if (e.isMiddleButtonDown())
            {
                double dx = e.getSceneX() - lastMouseX;
                double dy = e.getSceneY() - lastMouseY;

                orbitY.setAngle(orbitY.getAngle() + dx * SceneModelConstants.ORBIT_ROTATION_SPEED);

                // Vertikale Rotation auf ±89° begrenzen → kein Überschlag
                double newX = orbitX.getAngle() - dy * SceneModelConstants.ORBIT_ROTATION_SPEED;
                orbitX.setAngle(Math.max(SceneModelConstants.ORBIT_X_MIN_ANGLE, Math.min(SceneModelConstants.ORBIT_X_MAX_ANGLE, newX)));

                lastMouseX = e.getSceneX();
                lastMouseY = e.getSceneY();
            }

            if (e.isSecondaryButtonDown())
            {
                double dx = e.getSceneX() - lastMouseX;
                double dy = e.getSceneY() - lastMouseY;

                double panSpeed = Math.abs(camera.getTranslateZ()) * SceneModelConstants.PAN_SPEED_FACTOR;

                // Kamera-eigene Achsen in Weltkoordinaten umrechnen
                Point3D right = cameraPivot.localToParent(SceneModelConstants.UNIT_COORDINATE, SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE)
                        .subtract(cameraPivot.localToParent(SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.ZERO_COORDINATE));
                Point3D up = cameraPivot.localToParent(SceneModelConstants.ZERO_COORDINATE, SceneModelConstants.UNIT_COORDINATE, SceneModelConstants.ZERO_COORDINATE)
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

    /**
     * Creates a new SceneModel instance.
     * <p>
     * Initializes the object group, rotation transforms, solid material,
     * and shaded material.
     *
     * @precondition none
     * @postcondition A SceneModel instance with initialized groups, rotations and materials is created
     */
    public SceneModel ()
    {

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

    /**
     * Returns the singleton instance of the scene model.
     *
     * @return the shared SceneModel instance
     * @precondition none
     * @postcondition A non-null SceneModel instance is returned
     */
    public static SceneModel getInstance ()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new SceneModel();
        }

        return INSTANCE;
    }

    /**
     * Renders all static scene elements such as grid and axes.
     *
     * @param root   the root group where the scene elements are added
     * @param camera the camera used to adjust visual thickness dynamically
     * @precondition root != null AND camera != null
     * @postcondition Grid and axes are rendered and added to the root group
     */
    private void renderSceneElements (Group root, PerspectiveCamera camera)
    {
        renderGrid(camera);
        renderAxes(camera);

        root.getChildren().addAll(gridGroup, axesGroup);
    }

    /**
     * Creates and renders the grid of the 3D scene.
     * <p>
     * The grid line thickness is updated dynamically based on camera distance.
     *
     * @param camera the camera used to calculate dynamic line thickness
     * @precondition camera != null
     * @postcondition The grid group is created and filled with grid lines
     */
    private void renderGrid (PerspectiveCamera camera)
    {

        gridGroup = new Group();

        final int SIZE = SceneModelConstants.GRID_SIZE;
        final int MINOR = SceneModelConstants.GRID_MINOR_STEP;
        final int MAJOR = SceneModelConstants.GRID_MAJOR_STEP;
        final double THICKNESS = SceneModelConstants.GRID_THICKNESS_FACTOR;

        List<Runnable> thicknessUpdaters = new ArrayList<>();
        DoubleProperty t = new SimpleDoubleProperty(SceneModelConstants.INITIAL_LINE_THICKNESS);

        for (int i = -SIZE; i <= SIZE; i += MINOR)
        {

            boolean isMajor = (i % MAJOR == SceneModelConstants.ORIGIN_INDEX);

            if (i != SceneModelConstants.ORIGIN_INDEX)
            {
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

            if (i != SceneModelConstants.ORIGIN_INDEX)
            {
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

    /**
     * Creates and renders the coordinate axes of the 3D scene.
     * <p>
     * The axis thickness is updated dynamically based on camera distance.
     *
     * @param camera the camera used to calculate dynamic axis thickness
     * @precondition camera != null
     * @postcondition The axes group is created and filled with X, Y and Z axes
     */
    private void renderAxes (PerspectiveCamera camera)
    {

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

    /**
     * Creates a flat self-illuminated material with the given color.
     * <p>
     * The material uses a one-pixel self-illumination map so that the color is
     * not affected by lighting.
     *
     * @param color the color of the material
     * @return a flat PhongMaterial using the given color
     * @precondition color != null
     * @postcondition A non-null flat material is returned
     */
    private PhongMaterial flatColor (Color color)
    {
        WritableImage img = new WritableImage(SceneModelConstants.FLAT_COLOR_IMAGE_WIDTH, SceneModelConstants.FLAT_COLOR_IMAGE_HEIGHT);
        img.getPixelWriter().setColor(SceneModelConstants.FLAT_COLOR_PIXEL_X, SceneModelConstants.FLAT_COLOR_PIXEL_Y, color);

        PhongMaterial m = new PhongMaterial();
        m.setDiffuseColor(Color.BLACK);       // Kein diffuses Licht
        m.setSelfIlluminationMap(img);        // Farbe kommt nur hiervon
        return m;
    }

    /**
     * Creates a new subscene and adds it to the given root pane.
     * <p>
     * The subscene size is bound to the size of the root pane.
     *
     * @param rootPane the pane to which the subscene is added
     * @precondition rootPane != null
     * @postcondition The subscene is created, bound to the pane size and added to the pane
     */
    public void addSubSceneToPane (AnchorPane rootPane)
    {

        setSubScene(makeNewSubScene());
        subScene.widthProperty().bind(rootPane.widthProperty());
        subScene.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(subScene);
    }

    /**
     * Renders the given STL parse result as a polyhedron mesh.
     *
     * @param poly the parsed STL result to render
     * @precondition poly != null AND poly.getPolyhedron() != null
     * @postcondition The current object group contains the rendered mesh
     */
    public void renderPolyhedron (STLParseResult poly)
    {

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

    /**
     * Clears all currently rendered objects from the scene.
     *
     * @precondition objectsGroup != null
     * @postcondition The scene contains no rendered objects
     */
    public void clearScene ()
    {
        objectsGroup.getChildren().clear();
    }

    /**
     * Resets the camera.
     *
     * @precondition camera != null
     * @postcondition The camera is reset when reset logic is implemented
     */
    public void resetCamera ()
    {
        // Kamera zurücksetzen
    }

    /**
     * Resets object transformation, camera pivot, orbit rotation and zoom.
     * <p>
     * The previous transformation state is stored before resetting.
     *
     * @precondition objectsGroup != null AND cameraPivot != null AND orbitX != null AND orbitY != null AND camera != null
     * @postcondition Object and camera transformations are reset to their initial values
     */
    public void resetObjectTransform ()
    {
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

    /**
     * Sets the visibility of the grid.
     *
     * @param visible true if the grid should be visible, otherwise false
     * @precondition gridGroup != null
     * @postcondition The grid visibility is updated
     */
    public void setGridVisible (boolean visible)
    {
        gridGroup.setVisible(visible);
    }

    /**
     * Sets the visibility of the coordinate axes.
     *
     * @param visible true if the axes should be visible, otherwise false
     * @precondition axesGroup != null
     * @postcondition The axes visibility is updated
     */
    public void setAxesVisible (boolean visible)
    {
        axesGroup.setVisible(visible);
    }

    /**
     * Sets the current mesh render mode to solid.
     *
     * @precondition none
     * @postcondition The current mesh is rendered in solid mode if a mesh exists
     */
    public void setRenderModeSolid ()
    {
        if (currentMeshView == null)
        {
            return;
        }

        currentMeshView.setDrawMode(DrawMode.FILL);
        currentMeshView.setMaterial(solidMaterial);
    }

    /**
     * Sets the current mesh render mode to shaded.
     *
     * @precondition none
     * @postcondition The current mesh is rendered in shaded mode if a mesh exists
     */
    public void setRenderModeShaded ()
    {
        if (currentMeshView == null)
        {
            return;
        }

        currentMeshView.setDrawMode(DrawMode.FILL);
        currentMeshView.setMaterial(shadedMaterial);
    }

    /**
     * Exports a screenshot of the current subscene as a PNG image.
     *
     * @precondition none
     * @postcondition A screenshot is saved if a subscene exists and a file is selected
     */
    public void exportScreenshot ()
    {
        if (subScene == null)
        {
            System.err.println(SceneModelConstants.NO_SUBSCENE_SCREENSHOT_MESSAGE);
            return;
        }

        WritableImage image = subScene.snapshot(new SnapshotParameters(), null);

        FileChooser chooser = new FileChooser();
        chooser.setTitle(SceneModelConstants.SCREENSHOT_FILE_CHOOSER_TITLE);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(SceneModelConstants.PNG_IMAGE_DESCRIPTION, SceneModelConstants.PNG_EXTENSION_PATTERN)
        );

        File file = chooser.showSaveDialog(null);

        if (file != null)
        {
            try
            {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), SceneModelConstants.PNG_FORMAT_NAME, file);
                System.out.println(SceneModelConstants.SCREENSHOT_SAVED_MESSAGE_PREFIX + file.getAbsolutePath());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current transformation state to the undo stack.
     * <p>
     * The redo stack is cleared because a new state change invalidates previous redo states.
     *
     * @precondition cameraPivot != null AND orbitX != null AND orbitY != null AND camera != null
     * @postcondition The current transformation state is stored and the redo stack is cleared
     */
    private void saveState ()
    {
        undoStack.add(captureCurrentState());
        redoStack.clear();
    }

    /**
     * Restores the previous transformation state.
     *
     * @precondition none
     * @postcondition The previous transformation state is applied if the undo stack is not empty
     */
    public void undo ()
    {
        if (undoStack.isEmpty())
        {
            return;
        }

        redoStack.add(captureCurrentState());
        TransformationState prev = undoStack.remove(undoStack.size() - SceneModelConstants.LAST_STACK_INDEX_OFFSET);
        applyState(prev);
    }

    /**
     * Restores the next transformation state from the redo stack.
     *
     * @precondition none
     * @postcondition The next transformation state is applied if the redo stack is not empty
     */
    public void redo ()
    {
        if (redoStack.isEmpty())
        {
            return;
        }

        undoStack.add(captureCurrentState());
        TransformationState next = redoStack.remove(redoStack.size() - SceneModelConstants.LAST_STACK_INDEX_OFFSET);
        applyState(next);
    }

    /**
     * Captures the current transformation state of object, camera pivot,
     * orbit rotation and camera zoom.
     *
     * @return the current transformation state
     * @precondition objectsGroup != null AND cameraPivot != null AND orbitX != null AND orbitY != null AND camera != null
     * @postcondition A non-null transformation state is returned
     */
    private TransformationState captureCurrentState ()
    {
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

    /**
     * Applies the given transformation state to the object and camera.
     *
     * @param s the transformation state to apply
     * @precondition s != null AND objectsGroup != null AND cameraPivot != null AND orbitX != null AND orbitY != null AND camera != null
     * @postcondition Object and camera transformations match the given state
     */
    private void applyState (TransformationState s)
    {

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

    /**
     * Sets the current subscene.
     *
     * @param subScene the subscene to store
     * @precondition subScene != null
     * @postcondition The given subscene is stored as current subscene
     */
    public void setSubScene (SubScene subScene)
    {
        this.subScene = subScene;
    }

    /**
     * Translates the rendered object by the values stored in the given translation object.
     * <p>
     * If the translation values equal the reset values, the object transformation is reset.
     *
     * @param translateObject the translation data containing X, Y and Z translation values
     * @precondition none
     * @postcondition The object is translated, reset, or left unchanged if no object or translation data exists
     */
    public void translateObject (TranslateObjectJSON translateObject)
    {

        if (this.objectsGroup.getChildren().isEmpty() || translateObject == null)
        {
            return;
        }

        if (translateObject.getTranslateX() == SceneModelConstants.RESET_TRANSLATE_VALUE && translateObject.getTranslateY() == SceneModelConstants.RESET_TRANSLATE_VALUE && translateObject.getTranslateZ() == SceneModelConstants.RESET_TRANSLATE_VALUE)
        {
            resetObjectTransform();
        } else
        {
            this.objectsGroup.setTranslateX(this.objectsGroup.getTranslateX() + translateObject.getTranslateX());
            this.objectsGroup.setTranslateY(this.objectsGroup.getTranslateY() + translateObject.getTranslateY());
            this.objectsGroup.setTranslateZ(this.objectsGroup.getTranslateZ() + translateObject.getTranslateZ());
        }
    }

    /**
     * Rotates the rendered object by the values stored in the given rotation object.
     * <p>
     * If all rotation values equal the reset rotation value, the object rotation is reset.
     *
     * @param rotateObject the rotation data containing X, Y and Z rotation values
     * @precondition none
     * @postcondition The object is rotated, reset, or left unchanged if no object or rotation data exists
     */
    public void rotateObject (RotateObjectJSON rotateObject)
    {

        if (this.objectsGroup.getChildren().isEmpty() || rotateObject == null)
        {
            return;
        }

        if (rotateObject.getRotateX() == SceneModelConstants.NO_ROTATION_ANGLE && rotateObject.getRotateY() == SceneModelConstants.NO_ROTATION_ANGLE && rotateObject.getRotateZ() == SceneModelConstants.NO_ROTATION_ANGLE)
        {
            this.rotateX.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
            this.rotateY.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
            this.rotateZ.setAngle(SceneModelConstants.NO_ROTATION_ANGLE);
        } else
        {

            this.rotateX.setAngle(rotateX.getAngle() + rotateObject.getRotateX());
            this.rotateY.setAngle(rotateY.getAngle() + rotateObject.getRotateY());
            this.rotateZ.setAngle(rotateZ.getAngle() + rotateObject.getRotateZ());
        }
    }

    /**
     * Zooms the camera programmatically by the given delta value.
     *
     * @param delta the zoom delta value
     * @precondition camera != null
     * @postcondition The camera zoom value is updated
     */
    public void zoomBy (double delta)
    {
        double newZ = camera.getTranslateZ() + delta * SceneModelConstants.PROGRAMMATIC_ZOOM_FACTOR;
        camera.setTranslateZ(Math.min(SceneModelConstants.CAMERA_MIN_TRANSLATE_Z, newZ));
    }

    /**
     * Changes the color of the rendered polyhedron.
     *
     * @param color the new polyhedron color
     * @precondition color != null
     * @postcondition The current mesh material color is updated if a mesh exists
     */
    public void setPolyColor (Color color)
    {
        if (currentMeshView == null)
        {
            return;
        }

        shadedMaterial.setDiffuseColor(color);
        solidMaterial.setDiffuseColor(color);

        currentMeshView.setMaterial(shadedMaterial);
    }
}