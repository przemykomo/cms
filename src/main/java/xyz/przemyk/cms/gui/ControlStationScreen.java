package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.containers.ControlStationContainer;

public class ControlStationScreen extends AbstractContainerScreen<ControlStationContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(CMSMod.MODID, "textures/gui/control_station.png");
    public static final int DISPLAY_X = 8;
    public static final int DISPLAY_Y = 8;
    public static final int DISPLAY_WIDTH = 160;
    public static final int DISPLAY_HEIGHT = 150;
    public static final int DISPLAY_CENTER_X = DISPLAY_X + DISPLAY_WIDTH / 2;
    public static final int DISPLAY_CENTER_Y = DISPLAY_Y + DISPLAY_HEIGHT / 2;

    public int ticks;

    public double scale = 1.0;
    public double posX;
    public double posY;

    private final GuiAstroObject sun;
    public GuiAstroObject selectedObject;

    public ControlStationScreen(ControlStationContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        sun = new GuiAstroObject(0, this, "Sun", 176, 0, 3f, 4, 0)
                .addSatellite(new GuiAstroObject(40, this, "Mercury", 184, 0, 1f, 482, 30))
                .addSatellite(new GuiAstroObject(80, this, "Venus", 192, 0, 1f, 1230, 90))
                .addSatellite(new GuiAstroObject(120, this, "Earth", 200, 0, 1f, 2000, 40)
                    .addSatellite(new GuiAstroObject(15, this, "Moon", 200, 8, 0.5f, 146, 5)))
                .addSatellite(new GuiAstroObject(160, this, "Mars", 208, 0, 1f, 3760, 400))
                .addSatellite(new GuiAstroObject(200, this, "Jupiter", 216, 0, 1.5f, 23726, 243))
                .addSatellite(new GuiAstroObject(260, this, "Saturn", 224, 0, 1.5f, 58894, 1020)
                    .addSatellite(new GuiAstroObject(15, this, "Titan", 224, 8, 0.5f, 88, 12)))
                .addSatellite(new GuiAstroObject(300, this, "Uranus", 232, 0, 1.2f, 168034, 15))
                .addSatellite(new GuiAstroObject(350, this, "Neptune", 240, 0, 1.2f, 329582, 1400))
                .addSatellite(new GuiAstroObject(380, this, "Pluto", 248, 0, 0.7f, 495880, 890));
    }

    @Override
    protected void containerTick() {
        ticks++;

        if (selectedObject != null) {
            selectedObject.selectedTick();
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (selectedObject != null) {
            selectedObject.renderSelected(matrixStack, partialTicks, getGuiLeft(), getGuiTop());
        }
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_97788_, int p_97789_, int partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        double scaleFactor = minecraft.getWindow().getGuiScale();
        blit(poseStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

        RenderSystem.enableScissor((int) ((double)(getGuiLeft() + DISPLAY_X) * scaleFactor),
                (int) (minecraft.getWindow().getHeight() - ((getGuiTop() + DISPLAY_HEIGHT + DISPLAY_Y) * scaleFactor)),
                (int) (DISPLAY_WIDTH * scaleFactor),
                (int) (DISPLAY_HEIGHT * scaleFactor));
        poseStack.pushPose();
        poseStack.translate(getGuiLeft() + DISPLAY_CENTER_X + posX, getGuiTop() + DISPLAY_CENTER_Y + posY, 0);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        sun.render(poseStack, partialTicks);
        poseStack.popPose();
        RenderSystem.disableScissor();
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (isHovering(DISPLAY_X, DISPLAY_Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, mouseX, mouseY)) {
            sun.renderHoveredTooltip(matrixStack, mouseX, mouseY, getGuiLeft() + DISPLAY_CENTER_X + posX, getGuiTop() + DISPLAY_CENTER_Y + posY, scale);
        }
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {}

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        delta /= 4d;
        if ((scale >= 0.3F && scale <= 6.0F) ||(scale < 0.3F && delta > 0) || (scale > 6.0F && delta < 0)) {
            posX /= scale;
            posY /= scale;
            scale += delta;
            posX *= scale;
            posY *= scale;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isHovering(DISPLAY_X, DISPLAY_Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, mouseX, mouseY)) {
            posX += dragX;
            posY += dragY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (sun.mouseClicked(mouseX, mouseY, button, getGuiLeft() + DISPLAY_CENTER_X + posX, getGuiTop() + DISPLAY_CENTER_Y + posY, scale)) {
            return true;
        }
        selectedObject = null;
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
