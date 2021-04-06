package me.modione.sgplugin.base;

import me.modione.sgplugin.game.GameManager;
import me.modione.sgplugin.game.GameManager.GameState;
import org.bukkit.event.Listener;

public abstract class GamePhase implements Listener {
    protected final GameManager gameManager;

    public GamePhase(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void next() {
        this.onEnd();

    }

    public void cancel() {

    }

    public abstract GameState getState();

    public abstract void onStart();

    public abstract void onEnd();

    public abstract void onCancel();
}
