package xyz.przemyk.cms;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ISkyRenderHandler;

import java.util.Random;

public class CMSSkyRenderHandler implements ISkyRenderHandler {
    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation DYSON_SPHERE_LOCATION = new ResourceLocation(CMSMod.MODID, "textures/environment/dyson_sphere.png");

    private VertexBuffer starBuffer;
    private VertexBuffer skyBuffer;
    private VertexBuffer darkBuffer;

    public CMSSkyRenderHandler() {
        // star buffer
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        if (this.starBuffer != null) {
            this.starBuffer.close();
        }

        this.starBuffer = new VertexBuffer();
        this.drawStars(bufferbuilder);
        bufferbuilder.end();
        this.starBuffer.upload(bufferbuilder);

        //sky buffer
        if (this.skyBuffer != null) {
            this.skyBuffer.close();
        }

        this.skyBuffer = new VertexBuffer();
        buildSkyDisc(bufferbuilder, 16.0F);
        this.skyBuffer.upload(bufferbuilder);

        // dark buffer
        if (this.darkBuffer != null) {
            this.darkBuffer.close();
        }

        this.darkBuffer = new VertexBuffer();
        buildSkyDisc(bufferbuilder, -16.0F);
        this.darkBuffer.upload(bufferbuilder);
    }

    private static void buildSkyDisc(BufferBuilder p_172948_, float p_172949_) {
        float f = Math.signum(p_172949_) * 512.0F;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        p_172948_.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        p_172948_.vertex(0.0D, p_172949_, 0.0D).endVertex();

        for(int i = -180; i <= 180; i += 45) {
            p_172948_.vertex(f * Mth.cos((float)i * ((float)Math.PI / 180F)), p_172949_, 512.0F * Mth.sin((float)i * ((float)Math.PI / 180F))).endVertex();
        }

        p_172948_.end();
    }

    private void drawStars(BufferBuilder bufferBuilder) {
        Random random = new Random(10842L);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for(int i = 0; i < 1500; ++i) {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + random.nextFloat() * 0.1F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for(int j = 0; j < 4; ++j) {
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }

    @Override
    public void render(int ticks, float partialTick, PoseStack poseStack, ClientLevel level, Minecraft minecraft) {
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 pos = camera.getPosition();
        boolean fog = level.effects().isFoggyAt(Mth.floor(pos.x()), Mth.floor(pos.y())) || minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        if (!fog) {
            FogType fogtype = camera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA) {
                if (camera.getEntity() instanceof LivingEntity livingentity && livingentity.hasEffect(MobEffects.BLINDNESS)) {
                    return;
                }

                RenderSystem.disableTexture();
                Vec3 vec3 = level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
                float f10 = (float)vec3.x;
                float f = (float)vec3.y;
                float f1 = (float)vec3.z;
                FogRenderer.levelFogColor();
                BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                RenderSystem.depthMask(false);
                RenderSystem.setShaderColor(f10, f, f1, 1.0F);
                ShaderInstance shaderinstance = RenderSystem.getShader();
                skyBuffer.drawWithShader(poseStack.last().pose(), poseStack.last().pose(), shaderinstance);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
                if (afloat != null) {
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.disableTexture();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    poseStack.pushPose();
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                    float f2 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(f2));
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                    float f3 = afloat[0];
                    float f4 = afloat[1];
                    float f5 = afloat[2];
                    Matrix4f matrix4f = poseStack.last().pose();
                    bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f3, f4, f5, afloat[3]).endVertex();

                    for(int j = 0; j <= 16; ++j) {
                        float f6 = (float)j * ((float)Math.PI * 2F) / 16.0F;
                        float f7 = Mth.sin(f6);
                        float f8 = Mth.cos(f6);
                        bufferbuilder.vertex(matrix4f, f7 * 120.0F, f8 * 120.0F, -f8 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                    }

                    bufferbuilder.end();
                    BufferUploader.end(bufferbuilder);
                    poseStack.popPose();
                }

                RenderSystem.enableTexture();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                poseStack.pushPose();
                float alphaFromRain = 1.0F - level.getRainLevel(partialTick);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaFromRain);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
                Matrix4f matrix4f1 = poseStack.last().pose();
                float f12 = 30.0F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SUN_LOCATION);
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
                bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
                bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
                bufferbuilder.end();
                BufferUploader.end(bufferbuilder);

                RenderSystem.disableBlend();
                float satelliteSize = 2.0f;
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.vertex(matrix4f1, -satelliteSize, 100.0F, -satelliteSize).color(0.0f, 1.0f, 1.0f, 1.0f).endVertex();
                bufferbuilder.vertex(matrix4f1, satelliteSize, 100.0F, -satelliteSize).color(0.0f, 1.0f, 1.0f, 1.0f).endVertex();
                bufferbuilder.vertex(matrix4f1, satelliteSize, 100.0F, satelliteSize).color(0.0f, 1.0f, 1.0f, 1.0f).endVertex();
                bufferbuilder.vertex(matrix4f1, -satelliteSize, 100.0F, satelliteSize).color(0.0f, 1.0f, 1.0f, 1.0f).endVertex();
                bufferbuilder.end();
                BufferUploader.end(bufferbuilder);
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaFromRain);

//                    RenderSystem.disableBlend();
//                    RenderSystem.setShaderTexture(0, DYSON_SPHERE_LOCATION);
//                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
//                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
//                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
//                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
//                    bufferbuilder.end();
//                    BufferUploader.end(bufferbuilder);
//                    RenderSystem.enableBlend();

                f12 = 20.0F;
                RenderSystem.setShaderTexture(0, MOON_LOCATION);
                int k = level.getMoonPhase();
                int l = k % 4;
                int i1 = k / 4 % 2;
                float f13 = (float)(l) / 4.0F;
                float f14 = (float)(i1) / 2.0F;
                float f15 = (float)(l + 1) / 4.0F;
                float f16 = (float)(i1 + 1) / 2.0F;
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
                bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
                bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
                bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
                bufferbuilder.end();
                BufferUploader.end(bufferbuilder);
                RenderSystem.disableTexture();
                float f9 = level.getStarBrightness(partialTick) * alphaFromRain;
                if (f9 > 0.0F) {
                    RenderSystem.setShaderColor(f9, f9, f9, f9);
                    FogRenderer.setupNoFog();
                    starBuffer.drawWithShader(poseStack.last().pose(), RenderSystem.getProjectionMatrix(), GameRenderer.getPositionShader());
                    FogRenderer.setupFog(camera, FogRenderer.FogMode.FOG_SKY, f, fog, partialTick);
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                poseStack.popPose();
                RenderSystem.disableTexture();
                RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                double d0 = minecraft.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
                if (d0 < 0.0D) {
                    poseStack.pushPose();
                    poseStack.translate(0.0D, 12.0D, 0.0D);
                    darkBuffer.drawWithShader(poseStack.last().pose(), poseStack.last().pose(), shaderinstance);
                    poseStack.popPose();
                }

                if (level.effects().hasGround()) {
                    RenderSystem.setShaderColor(f10 * 0.2F + 0.04F, f * 0.2F + 0.04F, f1 * 0.6F + 0.1F, 1.0F);
                } else {
                    RenderSystem.setShaderColor(f10, f, f1, 1.0F);
                }

                RenderSystem.enableTexture();
                RenderSystem.depthMask(true);
            }
        }
    }
}
