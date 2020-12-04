package io.github.haykam821.licensepercentage.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.licensepercentage.LicensePercentageUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
	@Unique
	private Text licensePercentageText;

	@Unique
	private int licensePercentageTextWidth;

	@Unique
	private int licensePercentageTextX;

	@Shadow
	@Final
	private boolean doBackgroundFade;

	@Shadow
	private long backgroundFadeStart;

	private TitleScreenMixin(Text title) {
		super(title);
	}

	@Unique
	private int getBackgroundFade() {
		if (!this.doBackgroundFade) return 255 << 24;
		return MathHelper.ceil(MathHelper.clamp((Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000 - 1, 0, 1) * 255) << 24;
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void initLicensePercentage(CallbackInfo ci) {
		this.licensePercentageText = LicensePercentageUtils.getLicensePercentageText();
		this.licensePercentageTextWidth = this.textRenderer.getWidth(this.licensePercentageText);
		this.licensePercentageTextX = this.width - this.licensePercentageTextWidth - 2;
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void renderLicensePercentage(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		int backgroundFade = this.getBackgroundFade();
		if ((backgroundFade & 0x4000000) != 0) {
			drawTextWithShadow(matrices, this.textRenderer, this.licensePercentageText, 2, this.height - 10 - this.textRenderer.fontHeight, 0xFFFFFF | backgroundFade);
		}
	}
}