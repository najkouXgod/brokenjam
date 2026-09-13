package com.niko.jam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestWorld {

    private final List<ModelInstance> instances = new ArrayList<>();
    private final List<Model> models = new ArrayList<>();

    public TestWorld() {
        ModelBuilder builder = new ModelBuilder();

        createGround(builder);
        createRoad(builder);
        createBuildings(builder);
    }

    private void createGround(ModelBuilder builder) {

        Model groundModel = builder.createBox(
            100f,
            0.1f,
            100f,
            new Material(
                ColorAttribute.createDiffuse(
                    new Color(0.3f, 0.5f, 0.25f, 1f)
                )
            ),
            Usage.Position | Usage.Normal
        );

        models.add(groundModel);

        ModelInstance ground = new ModelInstance(groundModel);
        ground.transform.setToTranslation(0f, -0.1f, 0f);

        instances.add(ground);
    }

    private void createRoad(ModelBuilder builder) {

        Model roadModel = builder.createBox(
            10f,
            0.12f,
            100f,
            new Material(
                ColorAttribute.createDiffuse(
                    new Color(0.15f, 0.15f, 0.15f, 1f)
                )
            ),
            Usage.Position | Usage.Normal
        );

        models.add(roadModel);

        ModelInstance road = new ModelInstance(roadModel);

        road.transform.setToTranslation(
            0f,
            0f,
            0f
        );

        instances.add(road);
    }

    private void createBuildings(ModelBuilder builder) {

        Model buildingModel = builder.createBox(
            8f,
            6f,
            8f,
            new Material(
                ColorAttribute.createDiffuse(Color.LIGHT_GRAY)
            ),
            Usage.Position | Usage.Normal
        );

        models.add(buildingModel);

        addBuilding(buildingModel, -10f, 3f, 5f);
        addBuilding(buildingModel, 10f, 3f, -10f);
        addBuilding(buildingModel, -12f, 3f, -25f);
        addBuilding(buildingModel, 11f, 3f, 25f);
    }

    private void addBuilding(
        Model model,
        float x,
        float y,
        float z
    ) {
        ModelInstance instance = new ModelInstance(model);

        instance.transform.setToTranslation(x, y, z);

        instances.add(instance);
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
