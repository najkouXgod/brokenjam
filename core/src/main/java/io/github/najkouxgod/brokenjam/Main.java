package io.github.najkouxgod.brokenjam;

import com.badlogic.gdx.Game;
import com.niko.jam.DrivingScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new DrivingScreen());
    }
}
