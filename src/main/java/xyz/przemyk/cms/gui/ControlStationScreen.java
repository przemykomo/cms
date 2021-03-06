package xyz.przemyk.cms.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.containers.ControlStationContainer;

public class ControlStationScreen extends ContainerScreen<ControlStationContainer> {

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

    public ControlStationScreen(ControlStationContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
    public void tick() {
        super.tick();
        ticks++;

        if (selectedObject != null) {
            selectedObject.selectedTick();
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (selectedObject != null) {
            selectedObject.renderSelected(matrixStack, partialTicks, guiLeft, guiTop);
        }
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @SuppressWarnings({"ConstantConditions", "deprecation"})
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        double scaleFactor = minecraft.getMainWindow().getGuiScaleFactor();
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        RenderSystem.enableScissor((int) ((double)(guiLeft + DISPLAY_X) * scaleFactor),
                (int) (minecraft.getMainWindow().getFramebufferHeight() - ((guiTop + DISPLAY_HEIGHT + DISPLAY_Y) * scaleFactor)),
                (int) (DISPLAY_WIDTH * scaleFactor),
                (int) (DISPLAY_HEIGHT * scaleFactor));
        matrixStack.push();
        matrixStack.translate(guiLeft + DISPLAY_CENTER_X + posX, guiTop + DISPLAY_CENTER_Y + posY, 0);
        matrixStack.scale((float) scale, (float) scale, (float) scale);
        sun.render(matrixStack, partialTicks);
        matrixStack.pop();
        RenderSystem.disableScissor();
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (isPointInRegion(DISPLAY_X, DISPLAY_Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, mouseX, mouseY)) {
            sun.renderHoveredTooltip(matrixStack, mouseX, mouseY, guiLeft + DISPLAY_CENTER_X + posX, guiTop + DISPLAY_CENTER_Y + posY, scale);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {}

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
        if (isPointInRegion(DISPLAY_X, DISPLAY_Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, mouseX, mouseY)) {
            posX += dragX;
            posY += dragY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (sun.mouseClicked(mouseX, mouseY, button, guiLeft + DISPLAY_CENTER_X + posX, guiTop + DISPLAY_CENTER_Y + posY, scale)) {
            return true;
        }
        selectedObject = null;
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
