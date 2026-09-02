/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_10799
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_34
 *  net.minecraft.class_5250
 *  org.slf4j.Logger
 */
package restudio.reglass.client.screen.widget.world;

import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Supplier;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_10799;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_34;
import net.minecraft.class_5250;
import org.slf4j.Logger;
import restudio.reglass.client.api.ReGlassApi;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.client.screen.widget.ScrollableListWidget;
import restudio.reglass.client.screen.world.CustomWorldSelectScreen;

public class WorldListEntryWidget
extends ScrollableListWidget.Entry<WorldListEntryWidget> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final class_2960 DEFAULT_ICON_ID = class_2960.method_60654((String)"textures/misc/unknown_server.png");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
    private final class_310 client;
    private final CustomWorldSelectScreen parent;
    private final class_34 summary;
    private final class_2960 iconId;
    private final WidgetStyle defaultStyle = new WidgetStyle().tint(0, 0.1f);
    private final WidgetStyle hoveredStyle = new WidgetStyle().tint(0xFFFFFF, 0.1f);
    private final WidgetStyle selectedStyle = new WidgetStyle().tint(0xFFFFFF, 0.2f);
    private class_1043 iconTexture;

    public WorldListEntryWidget(CustomWorldSelectScreen parent, class_34 summary, int x, int y, int height) {
        super(x, y, parent.field_22789 - 150 - 40, height);
        this.parent = parent;
        this.summary = summary;
        this.client = class_310.method_1551();
        String safeName = summary.method_248().toLowerCase().replaceAll("[^a-z0-9/._-]", "_");
        this.iconId = class_2960.method_60654((String)("world-select/icon/" + safeName));
        this.loadIcon();
    }

    private void loadIcon() {
        File iconFile = this.summary.method_27020().toFile();
        if (Files.isRegularFile(iconFile.toPath(), new LinkOption[0])) {
            try (InputStream inputStream = Files.newInputStream(iconFile.toPath(), new OpenOption[0]);){
                class_1011 image = class_1011.method_4309((InputStream)inputStream);
                if (this.iconTexture != null) {
                    this.iconTexture.close();
                }
                Supplier<String> nativeImageSupplier = () -> {
                    try {
                        return Files.readString(iconFile.toPath());
                    }
                    catch (IOException e) {
                        LOGGER.error("Failed to read world icon for {}", (Object)this.summary.method_248(), (Object)e);
                        return null;
                    }
                };
                this.iconTexture = new class_1043(nativeImageSupplier, image);
                this.client.method_1531().method_4616(this.iconId, (class_1044)this.iconTexture);
            }
            catch (Exception e) {
                LOGGER.error("Failed to load world icon for {}", (Object)this.summary.method_248(), (Object)e);
                this.iconTexture = null;
            }
        }
    }

    @Override
    public void render(class_332 context, int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        super.render(context, index, x, y, width, height, mouseX, mouseY, hovered, delta);
        boolean isSelected = this.parent.getList().getSelectedEntries().contains(this);
        WidgetStyle style = this.defaultStyle;
        if (isSelected) {
            style = this.selectedStyle;
        } else if (hovered) {
            style = this.hoveredStyle;
        }
        ReGlassApi.create(context).dimensions(x, y, width, height).cornerRadius(8.0f).style(style).hover(hovered ? 1.0f : 0.0f).focus(isSelected ? 1.0f : 0.0f).render();
        Object displayName = this.summary.method_252();
        Object name = this.summary.method_248();
        long lastPlayed = this.summary.method_249();
        if (lastPlayed != -1L) {
            name = (String)name + " (" + DATE_FORMAT.format(Instant.ofEpochMilli(lastPlayed)) + ")";
        }
        if (displayName == null || ((String)displayName).isEmpty()) {
            displayName = class_2561.method_43471((String)"selectWorld.world").getString() + " " + (index + 1);
        }
        class_5250 details = (class_5250)this.summary.method_27429();
        context.method_25303(this.client.field_1772, (String)displayName, x + 40, y + 2, -1);
        context.method_25303(this.client.field_1772, (String)name, x + 40, y + 10 + 3, -8355712);
        context.method_27535(this.client.field_1772, (class_2561)details, x + 40, y + 10 + 9 + 3, -8355712);
        class_2960 texture = this.iconTexture != null ? this.iconId : DEFAULT_ICON_ID;
        context.method_25290(class_10799.field_56883, texture, x + 2, y + 2, 0.0f, 0.0f, 32, 32, 32, 32);
    }

    @Override
    public boolean mouseClicked(class_11909 click) {
        if (this.isMouseOver(click.comp_4798(), click.comp_4799())) {
            this.parent.getList().setSelected(this);
            return true;
        }
        return false;
    }

    public class_34 getSummary() {
        return this.summary;
    }

    @Override
    public void close() {
        if (this.iconTexture != null) {
            this.client.method_1531().method_4615(this.iconId);
            this.iconTexture.close();
            this.iconTexture = null;
        }
    }
}

