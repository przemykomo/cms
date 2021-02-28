package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class GuiAstroObject {

    public final String name;

    private final ArrayList<GuiAstroObject> satellites = new ArrayList<>();
    private final int orbitDistance;
    private final ControlStationScreen screen;
    private final int textureX;
    private final int textureY;
    private final float size;

    public GuiAstroObject(int orbitDistance, ControlStationScreen screen, String name, int textureX, int textureY, float size) {
        this.orbitDistance = orbitDistance;
        this.screen = screen;
        this.name = name;
        this.textureX = textureX;
        this.textureY = textureY;
        this.size = size;
    }

    public GuiAstroObject addSatellite(GuiAstroObject satellite) {
        satellites.add(satellite);
        return this;
    }

    public void render(MatrixStack matrixStack, float partialTicks) {
        matrixStack.push();
        matrixStack.translate(orbitDistance, 0, 0);

        matrixStack.push();
        matrixStack.scale(size, size, size);
        screen.blit(matrixStack, -4, -4, textureX, textureY, 8, 8);
        matrixStack.pop();

        for (GuiAstroObject satellite : satellites) {
            satellite.render(matrixStack, partialTicks);
        }
        matrixStack.pop();
    }

    public boolean renderHoveredTooltip(MatrixStack matrixStack, int x, int y, double centerX, double centerY, double scale) {
        centerX += orbitDistance * scale;
        double radius = 4 * scale * this.size;
        if (x > centerX - radius && x < centerX + radius && y > centerY - radius && y < centerY + radius) {
            screen.renderTooltip(matrixStack, new StringTextComponent(name), x, y);
            return true;
        }

        for (GuiAstroObject satellite : satellites) {
            if (satellite.renderHoveredTooltip(matrixStack, x, y, centerX, centerY, scale)) {
                return true;
            }
        }

        return false;
    }
}
