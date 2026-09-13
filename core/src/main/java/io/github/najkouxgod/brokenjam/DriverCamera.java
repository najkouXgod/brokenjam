package io.github.najkouxgod.brokenjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class DriverCamera {

    private static final float MOUSE_SENSITIVITY = 0.18f;

    private static final float MAX_HEAD_YAW = 100f;
    private static final float MIN_HEAD_PITCH = -45f;
    private static final float MAX_HEAD_PITCH = 50f;

    // Hur snabbt kameran hinner ikapp musens mål.
    private static final float LOOK_SMOOTHING = 18f;

    private final PerspectiveCamera camera;

    // Där musen vill att huvudet ska vara.
    private float targetYaw = 0f;
    private float targetPitch = 0f;

    // Där huvudet faktiskt befinner sig.
    private float currentYaw = 0f;
    private float currentPitch = 0f;

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

    public void update(Car car, float delta) {

        if (Gdx.input.isCursorCatched()) {

            float mouseX = MathUtils.clamp(
                Gdx.input.getDeltaX(),
                -25,
                25
            );

            float mouseY = MathUtils.clamp(
                Gdx.input.getDeltaY(),
                -25,
                25
            );

            targetYaw += mouseX * MOUSE_SENSITIVITY;
            targetPitch -= mouseY * MOUSE_SENSITIVITY;

            targetYaw = MathUtils.clamp(
                targetYaw,
                -MAX_HEAD_YAW,
                MAX_HEAD_YAW
            );

            targetPitch = MathUtils.clamp(
                targetPitch,
                MIN_HEAD_PITCH,
                MAX_HEAD_PITCH
            );
        }

        // Frame-rate independent smoothing.
        float smoothing =
            1f - (float)Math.exp(-LOOK_SMOOTHING * delta);

        currentYaw = MathUtils.lerp(
            currentYaw,
            targetYaw,
            smoothing
        );

        currentPitch = MathUtils.lerp(
            currentPitch,
            targetPitch,
            smoothing
        );

        Vector3 carPosition = car.getPosition();

        float driverOffset = -0.45f;

        float rightX = MathUtils.cosDeg(car.getYaw());
        float rightZ = MathUtils.sinDeg(car.getYaw());

        camera.position.set(
            carPosition.x + rightX * driverOffset,
            carPosition.y + 1.35f,
            carPosition.z + rightZ * driverOffset
        );

        float totalYaw = car.getYaw() + currentYaw;

        float cosPitch = MathUtils.cosDeg(currentPitch);

        camera.direction.set(
            MathUtils.sinDeg(totalYaw) * cosPitch,
            MathUtils.sinDeg(currentPitch),
            -MathUtils.cosDeg(totalYaw) * cosPitch
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
