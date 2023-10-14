package me.infamous.mob_vote_five.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class MVKeyMapping extends KeyMapping{
    public static final String KICK_ARMADILLO_LOCALIZATION = "key.kick_armadillo";
    public static final String RETRACT_GRAPPLING_HOOK = "key.retract_grappling_hook";
    public static final MVKeyMapping KEY_KICK_ARMADILLO = new MVKeyMapping(KICK_ARMADILLO_LOCALIZATION, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KeyMapping.CATEGORY_GAMEPLAY);
    public static final MVKeyMapping KEY_RETRACT_GRAPPLING_HOOK = new MVKeyMapping(RETRACT_GRAPPLING_HOOK, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, KeyMapping.CATEGORY_GAMEPLAY);
    private boolean initialPress;
    private boolean held;
    private boolean initialRelease;

    public MVKeyMapping(String localization, InputConstants.Type type, int keyCode, String category) {
        super(localization, type, keyCode, category);
    }

    @Override
    public void setDown(boolean pValue) {
        boolean wasDown = this.isDown();
        super.setDown(pValue);
        this.initialPress = !wasDown && this.isDown();
        this.held = wasDown && this.isDown();
        this.initialRelease = wasDown && !this.isDown();
    }

    public boolean isInitialPress() {
        return this.initialPress;
    }

    public boolean isHeld() {
        return this.held;
    }

    public boolean isInitialRelease() {
        return this.initialRelease;
    }
}
