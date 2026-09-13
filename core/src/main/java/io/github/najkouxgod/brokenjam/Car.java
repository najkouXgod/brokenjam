package com.niko.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Car {

    private static final float ACCELERATION = 8f;
    private static final float MAX_FORWARD_SPEED = 12f;
    private static final float MAX_REVERSE_SPEED = 4f;

    private static final float TURN_SPEED = 90f;
    private static final float DRAG = 5f;

    private final Vector3 position = new Vector3(0f, 0f, 15f);

    // 0 grader = framåt längs -Z
    private float yaw = 0f;
    private float speed = 0f;

    public void update(float delta) {
        float throttle = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            throttle += 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            throttle -= 1f;
        }

        // Gas / broms / back
        if (throttle != 0f) {
            speed += throttle * ACCELERATION * delta;
        } else {
            // Naturlig avmattning när man släpper gasen
            if (speed > 0f) {
                speed = Math.max(0f, speed - DRAG * delta);
            } else if (speed < 0f) {
                speed = Math.min(0f, speed + DRAG * delta);
            }
        }

        speed = MathUtils.clamp(
            speed,
            -MAX_REVERSE_SPEED,
            MAX_FORWARD_SPEED
        );

        float steering = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            steering -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            steering += 1f;
        }

        // Ju snabbare bilen går desto mer får styrningen effekt.
        // Negativ speed gör även att styrningen vänds när man backar.
        float speedFactor = speed / MAX_FORWARD_SPEED;

        yaw += steering * TURN_SPEED * speedFactor * delta;

        // Bilens framåtriktning
        float forwardX = MathUtils.sinDeg(yaw);
        float forwardZ = -MathUtils.cosDeg(yaw);

        position.x += forwardX * speed * delta;
        position.z += forwardZ * speed * delta;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getSpeed() {
        return speed;
    }
}
