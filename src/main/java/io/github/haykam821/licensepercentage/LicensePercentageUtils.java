package io.github.haykam821.licensepercentage;

import java.text.DecimalFormat;
import java.util.Locale;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public final class LicensePercentageUtils {
	private static final String COMPARISON_LICENSE = "arr";
	private static final String TEXT_LICENSE = COMPARISON_LICENSE.toUpperCase(Locale.ROOT);
	private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.00");

	public static double getLicensePercentage() {
		return FabricLoader.getInstance().getAllMods().stream().mapToDouble(container -> {
			return container.getMetadata().getLicense().stream().mapToInt(license -> {
				return license.toLowerCase(Locale.ROOT).equals(COMPARISON_LICENSE) ? 1 : 0;
			}).average().orElse(1);
		}).average().orElse(1);
	}

	public static Text getLicensePercentageText() {
		return new TranslatableText("text.licensepercentage.license_percentage", PERCENTAGE_FORMAT.format(getLicensePercentage() * 100), TEXT_LICENSE);
	}
}
