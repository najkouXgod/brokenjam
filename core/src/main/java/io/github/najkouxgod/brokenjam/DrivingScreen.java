package io.github.najkouxgod.brokenjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class DrivingScreen implements Screen {

    private ModelBatch modelBatch;
    private Environment environment;

    private Instructor instructor;
    private Cockpit cockpit;
    private Car car;
    private DriverCamera driverCamera;
    private TestWorld world;

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
                1f
            )
        );

        environment.add(
            new DirectionalLight().set(
                0.8f,
                0.8f,
                0.8f,
                -1f,
                -0.8f,
                -0.2f
            )
        );

        car = new Car();
        driverCamera = new DriverCamera();
        world = new TestWorld();
        cockpit = new Cockpit();
        instructor = new Instructor();

        Gdx.input.setCursorCatched(true);
    }
@Override
public void render(float delta) {

    handleCursor();

    car.update(delta);

    driverCamera.update(car);
    cockpit.update(car);
    instructor.update(car);

    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

    Gdx.gl.glClearColor(
        0.4f,
        0.65f,
        0.9f,
        1f
    );

    Gdx.gl.glClear(
        GL20.GL_COLOR_BUFFER_BIT
            | GL20.GL_DEPTH_BUFFER_BIT
    );

    modelBatch.begin(driverCamera.getCamera());

    for (var instance : world.getInstances()) {
        modelBatch.render(instance, environment);
    }

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
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        world.dispose();
        cockpit.dispose();
        instructor.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
