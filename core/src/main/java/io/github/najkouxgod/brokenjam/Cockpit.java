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

public class Cockpit {

    private final List<Model> models = new ArrayList<>();
    private final List<ModelInstance> instances = new ArrayList<>();

    private final ModelInstance dashboard;
    private final ModelInstance leftPillar;
    private final ModelInstance rightPillar;
    private final ModelInstance roofStrip;

    private final ModelInstance steeringWheel;
    private final ModelInstance steeringColumn;

    public Cockpit() {
        ModelBuilder builder = new ModelBuilder();

        Material dashboardMaterial = new Material(
            ColorAttribute.createDiffuse(
                new Color(0.12f, 0.12f, 0.12f, 1f)
            )
        );

        Material pillarMaterial = new Material(
            ColorAttribute.createDiffuse(
                new Color(0.18f, 0.18f, 0.18f, 1f)
            )
        );

        Material steeringMaterial = new Material(
            ColorAttribute.createDiffuse(Color.BLACK)
        );

        // Instrumentpanel
        Model dashboardModel = builder.createBox(
            2.2f,
            0.40f,
            0.45f,
            dashboardMaterial,
            Usage.Position | Usage.Normal
        );

        // Vindrutestolpar
        Model pillarModel = builder.createBox(
            0.12f,
            1.1f,
            0.12f,
            pillarMaterial,
            Usage.Position | Usage.Normal
        );

        // Överkant på vindrutan
        Model roofStripModel = builder.createBox(
            2.2f,
            0.15f,
            0.15f,
            pillarMaterial,
            Usage.Position | Usage.Normal
        );

        // Tillfällig "ratt".
        // En cylinder är egentligen fel form,
        // men duger för att testa perspektivet.
        Model steeringWheelModel = builder.createCylinder(
            0.48f,
            0.08f,
            0.48f,
            20,
            steeringMaterial,
            Usage.Position | Usage.Normal
        );

        Model steeringColumnModel = builder.createBox(
            0.10f,
            0.10f,
            0.45f,
            steeringMaterial,
            Usage.Position | Usage.Normal
        );

        models.add(dashboardModel);
        models.add(pillarModel);
        models.add(roofStripModel);
        models.add(steeringWheelModel);
        models.add(steeringColumnModel);

        dashboard = new ModelInstance(dashboardModel);

        leftPillar = new ModelInstance(pillarModel);
        rightPillar = new ModelInstance(pillarModel);

        roofStrip = new ModelInstance(roofStripModel);

        steeringWheel = new ModelInstance(steeringWheelModel);
        steeringColumn = new ModelInstance(steeringColumnModel);

        instances.add(dashboard);
        instances.add(leftPillar);
        instances.add(rightPillar);
        instances.add(roofStrip);
        instances.add(steeringWheel);
        instances.add(steeringColumn);
    }

    public void update(Car car) {

        Matrix4 carTransform = new Matrix4()
            .idt()
            .translate(car.getPosition())
            .rotate(Vector3.Y, -car.getYaw());

        // Instrumentpanel framför föraren
        dashboard.transform
            .set(carTransform)
            .translate(0f, 0.78f, -0.95f);

        // Vindrutestolpar
        leftPillar.transform
            .set(carTransform)
            .translate(-1.05f, 1.40f, -0.78f);

        rightPillar.transform
            .set(carTransform)
            .translate(1.05f, 1.40f, -0.78f);

        // Övre kanten av vindrutan
        roofStrip.transform
            .set(carTransform)
            .translate(0f, 1.92f, -0.78f);

        // Sverige = vänsterstyrd bil.
        steeringWheel.transform
            .set(carTransform)
            .translate(-0.45f, 1.08f, -0.55f)
            .rotate(Vector3.X, 78f);

        steeringColumn.transform
            .set(carTransform)
            .translate(-0.45f, 0.98f, -0.72f);
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
