package io.github.najkouxgod.brokenjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;

import com.badlogic.gdx.graphics.Color;

public class DrivingScreen implements Screen {

    private ModelBatch modelBatch;
    private Environment environment;

    private SceneAsset mapAsset;
    private Scene mapScene;
    private SceneManager sceneManager;
    private Instructor instructor;
    private Cockpit cockpit;
    private Car car;
    private DriverCamera driverCamera;

    @Override
    public void show() {

        modelBatch = new ModelBatch();

        environment = new Environment();

        environment.set(
                new ColorAttribute(
                        ColorAttribute.AmbientLight,
                        0.65f,
                        0.65f,
                        0.65f,
                        1f));

        environment.add(
                new DirectionalLight().set(
                        0.8f,
                        0.8f,
                        0.8f,
                        -1f,
                        -0.8f,
                        -0.2f));

        car = new Car();
        driverCamera = new DriverCamera();
        cockpit = new Cockpit();
        instructor = new Instructor();

        mapAsset = new GLBLoader().load(
                Gdx.files.internal("maps/map.glb"));

        mapScene = new Scene(mapAsset.scene);

        sceneManager = new SceneManager();
        sceneManager.addScene(mapScene);

        sceneManager.setCamera(driverCamera.getCamera());

        DirectionalLightEx sun = new DirectionalLightEx();
        sun.direction.set(-1f, -3f, -1f).nor();
        sun.color.set(Color.WHITE);

        sceneManager.environment.add(sun);
        sceneManager.setAmbientLight(0.6f);

        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {

        handleCursor();

        car.update(delta);

        driverCamera.update(car, delta);
        cockpit.update(car);
        instructor.update(car);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        Gdx.gl.glClearColor(
                0.4f,
                0.65f,
                0.9f,
                1f);

        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                        | GL20.GL_DEPTH_BUFFER_BIT);

        // BLENDER MAP
        sceneManager.update(delta);
        sceneManager.render();

        // YOUR NORMAL LIBGDX MODELS
        modelBatch.begin(driverCamera.getCamera());

        for (var instance : cockpit.getInstances()) {
            modelBatch.render(instance, environment);
        }

        for (var instance : instructor.getInstances()) {
            modelBatch.render(instance, environment);
        }

        modelBatch.end();
    }

    private void handleCursor() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.input.setCursorCatched(false);
        }

        if (Gdx.input.justTouched() && !Gdx.input.isCursorCatched()) {
            Gdx.input.setCursorCatched(true);
        }
    }

    @Override
    public void resize(int width, int height) {
        driverCamera.resize(width, height);

        if (sceneManager != null) {
            sceneManager.updateViewport(width, height);
        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        cockpit.dispose();
        instructor.dispose();
        if (sceneManager != null) {
            sceneManager.dispose();
        }

        if (mapAsset != null) {
            mapAsset.dispose();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
