package com.niko.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class DriverCamera {

    private static final float MOUSE_SENSITIVITY = 0.15f;

    private static final float MAX_HEAD_YAW = 100f;
    private static final float MIN_HEAD_PITCH = -45f;
    private static final float MAX_HEAD_PITCH = 50f;

    private final PerspectiveCamera camera;

    private float headYaw = 0f;
    private float headPitch = 0f;

    public DriverCamera() {
        camera = new PerspectiveCamera(
            70f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        camera.near = 0.05f;
        camera.far = 250f;

        camera.update();
    }

    public void update(Car car) {

        // Bara mouse-look när musen är fångad
        if (Gdx.input.isCursorCatched()) {

            float mouseX = Gdx.input.getDeltaX();
            float mouseY = Gdx.input.getDeltaY();

            headYaw -= mouseX * MOUSE_SENSITIVITY;
            headPitch -= mouseY * MOUSE_SENSITIVITY;

            headYaw = MathUtils.clamp(
                headYaw,
                -MAX_HEAD_YAW,
                MAX_HEAD_YAW
            );

            headPitch = MathUtils.clamp(
                headPitch,
                MIN_HEAD_PITCH,
                MAX_HEAD_PITCH
            );
        }

        Vector3 carPosition = car.getPosition();

        // Förarens huvudhöjd.
        // Sen kan vi lägga till offset åt vänster när cockpit finns.
        camera.position.set(
            carPosition.x,
            carPosition.y + 1.35f,
            carPosition.z
        );

        float totalYaw = car.getYaw() + headYaw;

        float cosPitch = MathUtils.cosDeg(headPitch);

        float directionX =
            MathUtils.sinDeg(totalYaw) * cosPitch;

        float directionY =
            MathUtils.sinDeg(headPitch);

        float directionZ =
            -MathUtils.cosDeg(totalYaw) * cosPitch;

        camera.direction.set(
            directionX,
            directionY,
            directionZ
        ).nor();

        camera.up.set(Vector3.Y);

        camera.update();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
}
