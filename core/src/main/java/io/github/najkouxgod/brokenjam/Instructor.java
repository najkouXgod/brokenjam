package io.github.najkouxgod.brokenjam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Instructor {

    private final List<Model> models = new ArrayList<>();
    private final List<ModelInstance> instances = new ArrayList<>();

    private final ModelInstance torso;
    private final ModelInstance head;
    private final ModelInstance leftEye;
    private final ModelInstance rightEye;

    public Instructor() {
        ModelBuilder builder = new ModelBuilder();

        Material shirtMaterial = new Material(
            ColorAttribute.createDiffuse(new Color(0.15f, 0.25f, 0.65f, 1f))
        );

        Material skinMaterial = new Material(
            ColorAttribute.createDiffuse(new Color(0.85f, 0.65f, 0.5f, 1f))
        );

        Material eyeMaterial = new Material(
            ColorAttribute.createDiffuse(Color.BLACK)
        );

        Model torsoModel = builder.createBox(
            0.55f,
            0.75f,
            0.35f,
            shirtMaterial,
            Usage.Position | Usage.Normal
        );

        Model headModel = builder.createSphere(
            0.38f,
            0.42f,
            0.38f,
            16,
            16,
            skinMaterial,
            Usage.Position | Usage.Normal
        );

        Model eyeModel = builder.createSphere(
            0.05f,
            0.05f,
            0.05f,
            8,
            8,
            eyeMaterial,
            Usage.Position | Usage.Normal
        );

        models.add(torsoModel);
        models.add(headModel);
        models.add(eyeModel);

        torso = new ModelInstance(torsoModel);
        head = new ModelInstance(headModel);

        leftEye = new ModelInstance(eyeModel);
        rightEye = new ModelInstance(eyeModel);

        instances.add(torso);
        instances.add(head);
        instances.add(leftEye);
        instances.add(rightEye);
    }

    public void update(Car car) {

        Matrix4 carTransform = new Matrix4()
            .idt()
            .translate(car.getPosition())
            .rotate(Vector3.Y, -car.getYaw());

        // Passagerarsidan
        float x = 0.45f;

        torso.transform
            .set(carTransform)
            .translate(x, 0.85f, 0.05f);

        head.transform
            .set(carTransform)
            .translate(x, 1.42f, -0.02f);

        // Ögonen sitter på sidan av huvudet som pekar framåt (-Z)
        leftEye.transform
            .set(carTransform)
            .translate(x - 0.08f, 1.46f, -0.205f);

        rightEye.transform
            .set(carTransform)
            .translate(x + 0.08f, 1.46f, -0.205f);
    }

    public List<ModelInstance> getInstances() {
        return instances;
    }

    public void dispose() {
        for (Model model : models) {
            model.dispose();
        }
    }
}
