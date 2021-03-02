package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
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
    private final int orbitTime;
    private final int sideTime;
    private final int startingTicks;
    public GuiAstroObject parentObject;

    public GuiAstroObject(int orbitDistance, ControlStationScreen screen, String name, int textureX, int textureY, float size, int orbitTime, int startingTicks) {
        this.orbitDistance = orbitDistance;
        this.screen = screen;
        this.name = name;
        this.textureX = textureX;
        this.textureY = textureY;
        this.size = size;
        this.orbitTime = orbitTime;
        this.sideTime = orbitTime / 4;
        this.startingTicks = startingTicks;
    }

    public GuiAstroObject addSatellite(GuiAstroObject satellite) {
        satellites.add(satellite);
        satellite.parentObject = this;
        return this;
    }

    @SuppressWarnings("unused")
    public void render(MatrixStack matrixStack, float partialTicks) {
        renderOrbit(matrixStack);
        matrixStack.push();
        Vector2f pos = getRelativePos();
        matrixStack.translate(pos.x, pos.y, 0);

        matrixStack.push();
        matrixStack.scale(size, size, size);
        screen.blit(matrixStack, -4, -4, textureX, textureY, 8, 8);
        matrixStack.pop();

        for (GuiAstroObject satellite : satellites) {
            satellite.render(matrixStack, partialTicks);
        }
        matrixStack.pop();
    }

    protected Vector2f getRelativePos() {
        float ticks = (screen.ticks + startingTicks) % orbitTime;
        float sideTicks = ticks % sideTime;
        float scaledSidePos = sideTicks / sideTime * 2 * orbitDistance - orbitDistance;

        if (ticks < sideTime) {
            return new Vector2f(orbitDistance, -scaledSidePos);
        }

        if (sideTime <= ticks && ticks < sideTime * 2) {
            return new Vector2f(-scaledSidePos, -orbitDistance);
        }

        if (sideTime * 2 <= ticks && ticks < sideTime * 3) {
            return new Vector2f(-orbitDistance, scaledSidePos);
        }

        return new Vector2f(scaledSidePos, orbitDistance);
    }

    protected Vector2f getAbsolutePos() {
        if (parentObject == null) {
            return getRelativePos();
        }
        Vector2f parentPos = parentObject.getAbsolutePos();
        Vector2f relativePos = getRelativePos();
        return new Vector2f(parentPos.x + relativePos.x, parentPos.y + relativePos.y);
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

    public boolean renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY, double centerX, double centerY, double scale) {
        Vector2f pos = getRelativePos();
        centerX += pos.x * scale;
        centerY += pos.y * scale;
        double radius = 4 * scale * this.size;
        if (mouseX > centerX - radius && mouseX < centerX + radius && mouseY > centerY - radius && mouseY < centerY + radius) {
            screen.renderTooltip(matrixStack, new StringTextComponent(name), mouseX, mouseY);
            return true;
        }

        for (GuiAstroObject satellite : satellites) {
            if (satellite.renderHoveredTooltip(matrixStack, mouseX, mouseY, centerX, centerY, scale)) {
                return true;
            }
        }

        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, double centerX, double centerY, double scale) {
        Vector2f pos = getRelativePos();
        centerX += pos.x * scale;
        centerY += pos.y * scale;
        double radius = 4 * scale * this.size;
        if (mouseX > centerX - radius && mouseX < centerX + radius && mouseY > centerY - radius && mouseY < centerY + radius) {
            screen.selectedObject = this;
            return true;
        }

        for (GuiAstroObject satellite : satellites) {
            if (satellite.mouseClicked(mouseX, mouseY, button, centerX, centerY, scale)) {
                return true;
            }
        }

        return false;
    }

    public void selectedTick() {
        Vector2f pos = getAbsolutePos();
        screen.posX = -pos.x * screen.scale;
        screen.posY = -pos.y * screen.scale;
    }
}
