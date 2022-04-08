package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec2;

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
    public void render(PoseStack matrixStack, float partialTicks) {
        renderOrbit(matrixStack);
        matrixStack.pushPose();
        Vec2 pos = getRelativePos();
        matrixStack.translate(pos.x, pos.y, 0);

        matrixStack.pushPose();
        matrixStack.scale(size, size, size);
        screen.blit(matrixStack, -4, -4, textureX, textureY, 8, 8);
        matrixStack.popPose();

        for (GuiAstroObject satellite : satellites) {
            satellite.render(matrixStack, partialTicks);
        }
        matrixStack.popPose();
    }

    protected Vec2 getRelativePos() {
        float ticks = (screen.ticks + startingTicks) % orbitTime;
        float sideTicks = ticks % sideTime;
        float scaledSidePos = sideTicks / sideTime * 2 * orbitDistance - orbitDistance;

        if (ticks < sideTime) {
            return new Vec2(orbitDistance, -scaledSidePos);
        }

        if (sideTime <= ticks && ticks < sideTime * 2) {
            return new Vec2(-scaledSidePos, -orbitDistance);
        }

        if (sideTime * 2 <= ticks && ticks < sideTime * 3) {
            return new Vec2(-orbitDistance, scaledSidePos);
        }

        return new Vec2(scaledSidePos, orbitDistance);
    }

    protected Vec2 getAbsolutePos() {
        if (parentObject == null) {
            return getRelativePos();
        }
        Vec2 parentPos = parentObject.getAbsolutePos();
        Vec2 relativePos = getRelativePos();
        return new Vec2(parentPos.x + relativePos.x, parentPos.y + relativePos.y);
    }

    protected void renderOrbit(PoseStack matrixStack) {
        RenderSystem.disableTexture();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.lineWidth(1.0F);
        bufferbuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.last().pose();
        bufferbuilder.vertex(matrix4f, -orbitDistance, orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.vertex(matrix4f, orbitDistance, orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.vertex(matrix4f, orbitDistance, -orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        bufferbuilder.vertex(matrix4f, -orbitDistance, -orbitDistance, 0).color(0.2F, 0.2F, 1F, 1F).endVertex();
        tessellator.end();
        RenderSystem.enableTexture();
    }

    public boolean renderHoveredTooltip(PoseStack matrixStack, int mouseX, int mouseY, double centerX, double centerY, double scale) {
        Vec2 pos = getRelativePos();
        centerX += pos.x * scale;
        centerY += pos.y * scale;
        double radius = 4 * scale * this.size;
        if (mouseX > centerX - radius && mouseX < centerX + radius && mouseY > centerY - radius && mouseY < centerY + radius) {
            screen.renderTooltip(matrixStack, new TextComponent(name), mouseX, mouseY);
            return true;
        }

        for (GuiAstroObject satellite : satellites) {
            if (satellite.renderHoveredTooltip(matrixStack, mouseX, mouseY, centerX, centerY, scale)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unused")
    public boolean mouseClicked(double mouseX, double mouseY, int button, double centerX, double centerY, double scale) {
        Vec2 pos = getRelativePos();
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
        Vec2 pos = getAbsolutePos();
        screen.posX = -pos.x * screen.scale;
        screen.posY = -pos.y * screen.scale;
    }

    @SuppressWarnings("unused")
    public void renderSelected(PoseStack matrixStack, float partialTicks, int guiLeft, int guiTop) {
//        screen.renderTooltip(matrixStack,
//                Collections.singletonList(new TextComponent(name)),
//                guiLeft + ControlStationScreen.DISPLAY_X - 8,
//                guiTop + ControlStationScreen.DISPLAY_Y + ControlStationScreen.DISPLAY_HEIGHT,
//                screen.width,
//                screen.height,
//                -1,
//                GuiUtils.DEFAULT_BACKGROUND_COLOR,
//                0xFF5DD6F5,
//                (0xFF5DD6F5 & 0xFEFEFE) >> 1 | 0xFF5DD6F5 & 0xFF000000,
//                Minecraft.getInstance().font);
        screen.renderTooltip(matrixStack, new TextComponent(name), guiLeft + ControlStationScreen.DISPLAY_X - 8,
                guiTop + ControlStationScreen.DISPLAY_Y + ControlStationScreen.DISPLAY_HEIGHT);
    }
}
