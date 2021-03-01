package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

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
        renderOrbit(matrixStack);
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

    protected void renderOrbit(MatrixStack matrixStack) {
        RenderSystem.disableTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderSystem.lineWidth(1.0F);
        bufferbuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        bufferbuilder.pos(matrix4f, -orbitDistance, orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.pos(matrix4f, orbitDistance, orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.pos(matrix4f, orbitDistance, -orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.pos(matrix4f, -orbitDistance, -orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        tessellator.draw();
        RenderSystem.enableTexture();
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
