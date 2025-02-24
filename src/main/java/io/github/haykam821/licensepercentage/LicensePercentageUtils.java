package io.github.haykam821.licensepercentage;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public final class LicensePercentageUtils {
	private static final String COMPARISON_LICENSE = "arr";
	private static final String TEXT_LICENSE = COMPARISON_LICENSE.toUpperCase(Locale.ROOT);
	private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.00");
	private static final Pattern FABRIC_LIBRARY_PATTERN = Pattern.compile("^fabric-.*(-v\\d+)$");

	private static boolean isLibrary(ModMetadata metadata) {
		// Overrides
		if (metadata.getId().equals("fabricloader")) return true;
		if (metadata.getId().equals("fabric")) return true;
		if (metadata.getId().equals("fabric-api-base")) return true;
		if (metadata.getId().equals("fabric-renderer-indigo")) return true;
		
		// Patterns
		if (metadata.getName().endsWith(" API")) return true;
		if (FABRIC_LIBRARY_PATTERN.matcher(metadata.getId()).matches()) return true;

		// Custom values
		if (metadata.containsCustomValue("modmenu:api") && metadata.getCustomValue("modmenu:api").getAsBoolean()) return true;
		if (metadata.containsCustomValue("fabric-loom:generated") && metadata.getCustomValue("fabric-loom:generated").getAsBoolean()) return true;

		return false;
	}

	public static double getLicensePercentage() {
		return FabricLoader.getInstance().getAllMods().stream().filter(container -> {
			return !isLibrary(container.getMetadata());
		}).mapToDouble(container -> {
			return container.getMetadata().getLicense().stream().mapToInt(license -> {
				return license.toLowerCase(Locale.ROOT).equals(COMPARISON_LICENSE) ? 1 : 0;
			}).average().orElse(1);
		}).average().orElse(1);
	}

	public static Text getLicensePercentageText() {
		return new TranslatableText("text.licensepercentage.license_percentage", PERCENTAGE_FORMAT.format(getLicensePercentage() * 100), TEXT_LICENSE);
	}
}
