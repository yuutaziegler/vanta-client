/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_32$class_5143
 *  net.minecraft.class_32$class_7410
 *  net.minecraft.class_33
 *  net.minecraft.class_332
 *  net.minecraft.class_34
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_370
 *  net.minecraft.class_410
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_524
 *  net.minecraft.class_525
 *  net.minecraft.class_5250
 *  net.minecraft.class_8579
 *  org.slf4j.Logger
 */
package restudio.reglass.client.screen.world;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_32;
import net.minecraft.class_33;
import net.minecraft.class_332;
import net.minecraft.class_34;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_370;
import net.minecraft.class_410;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_524;
import net.minecraft.class_525;
import net.minecraft.class_5250;
import net.minecraft.class_8579;
import org.slf4j.Logger;
import restudio.reglass.client.screen.widget.ScrollableListWidget;
import restudio.reglass.client.screen.widget.world.WorldListEntryWidget;

public class CustomWorldSelectScreen
extends class_437 {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final class_437 parent;
    private ScrollableListWidget<WorldListEntryWidget> worldList;
    private class_342 searchBox;
    private class_4185 playButton;
    private class_4185 createButton;
    private class_4185 editButton;
    private class_4185 deleteButton;

    public CustomWorldSelectScreen(class_437 parent) {
        super((class_2561)class_2561.method_43471((String)"selectWorld.title"));
        this.parent = parent;
    }

    protected void method_25426() {
        int listWidth = this.field_22789 - 150;
        this.worldList = new ScrollableListWidget(this, 20, 50, listWidth - 40, this.field_22790 - 100, 36);
        this.worldList.setVerticalPadding(5);
        this.searchBox = new class_342(this.field_22793, 20, 20, listWidth - 40, 20, (class_2561)class_2561.method_43471((String)"selectWorld.search"));
        this.searchBox.method_1863(this::filterWorlds);
        this.loadWorldList();
        this.method_37063((class_364)this.worldList);
        this.method_37063((class_364)this.searchBox);
        int buttonWidth = 120;
        this.playButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"selectWorld.select"), button -> this.play(this.getSelectedSummaries())).method_46434(listWidth, 50, buttonWidth, 20).method_46431();
        this.createButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"selectWorld.create"), button -> class_525.method_31130((class_310)this.field_22787, null)).method_46434(listWidth, 80, buttonWidth, 20).method_46431();
        this.editButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"selectWorld.edit"), button -> {
            Set<class_34> summaries = this.getSelectedSummaries();
            if (summaries.size() == 1) {
                class_34 summary = summaries.iterator().next();
                try {
                    class_32.class_5143 session = this.field_22787.method_1586().method_52236(summary.method_248());
                    this.field_22787.method_1507((class_437)class_524.method_54599((class_310)this.field_22787, (class_32.class_5143)session, saved -> {
                        if (saved) {
                            this.loadWorldList();
                        }
                        this.field_22787.method_1507((class_437)this);
                    }));
                }
                catch (IOException e) {
                    LOGGER.error("Failed to access world {}", (Object)summary.method_248(), (Object)e);
                    class_370.method_27023((class_310)this.field_22787, (String)summary.method_248());
                }
                catch (class_8579 e) {
                    LOGGER.warn("Failed to validate symlinks for world {}", (Object)summary.method_248(), (Object)e);
                    class_370.method_27023((class_310)this.field_22787, (String)summary.method_248());
                }
            }
        }).method_46434(listWidth, 100, buttonWidth, 20).method_46431();
        this.deleteButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"selectWorld.delete"), button -> this.delete(this.getSelectedSummaries())).method_46434(listWidth, 120, buttonWidth, 20).method_46431();
        this.method_37063((class_364)this.playButton);
        this.method_37063((class_364)this.createButton);
        this.method_37063((class_364)this.editButton);
        this.method_37063((class_364)this.deleteButton);
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), b -> this.field_22787.method_1507(this.parent)).method_46434(listWidth, this.field_22790 - 40, buttonWidth, 20).method_46431());
        this.updateButtonStates();
    }

    private void loadWorldList() {
        this.worldList.clearEntries();
        try {
            class_32.class_7410 levelList = this.field_22787.method_1586().method_235();
            List summaries = (List)this.field_22787.method_1586().method_43417(levelList).join();
            for (class_34 summary : summaries) {
                this.worldList.addEntry(new WorldListEntryWidget(this, summary, 0, 0, 30));
            }
        }
        catch (class_33 e) {
            LOGGER.error("Couldn't load worlds", (Throwable)e);
        }
    }

    private void filterWorlds(String filter) {
        this.loadWorldList();
        if (!filter.isEmpty()) {
            String lowerFilter = filter.toLowerCase();
            this.worldList.getEntries().removeIf(entry -> !entry.getSummary().method_252().toLowerCase().contains(lowerFilter) && !entry.getSummary().method_248().toLowerCase().contains(lowerFilter));
        }
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.method_25420(context, mouseX, mouseY, delta);
        this.worldList.method_25394(context, mouseX, mouseY, delta);
        this.searchBox.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 8, 0xFFFFFF);
        super.method_25394(context, mouseX, mouseY, delta);
        this.updateButtonStates();
    }

    private void updateButtonStates() {
        int selectionCount = this.worldList.getSelectedEntries().size();
        this.playButton.field_22763 = selectionCount == 1;
        this.editButton.field_22763 = selectionCount == 1;
        this.deleteButton.field_22763 = selectionCount > 0;
    }

    public ScrollableListWidget<WorldListEntryWidget> getList() {
        return this.worldList;
    }

    public void play(Set<class_34> summaries) {
        if (summaries.size() == 1) {
            class_34 summary = summaries.iterator().next();
            this.field_22787.method_41735().method_57784(summary.method_248(), () -> this.field_22787.method_1507((class_437)this));
        }
    }

    public void delete(Set<class_34> summaries) {
        if (summaries.isEmpty()) {
            return;
        }
        class_5250 title = class_2561.method_43471((String)"selectWorld.deleteQuestion");
        class_5250 message = class_2561.method_43469((String)"selectWorld.deleteWarning", (Object[])new Object[]{summaries.stream().map(class_34::method_252).collect(Collectors.joining(", "))});
        this.field_22787.method_1507((class_437)new class_410(confirmed -> {
            if (confirmed) {
                try {
                    for (class_34 summary : summaries) {
                        try {
                            class_32.class_5143 session = this.field_22787.method_1586().method_52236(summary.method_248());
                            try {
                                session.method_27015();
                            }
                            finally {
                                if (session == null) continue;
                                session.close();
                            }
                        }
                        catch (class_8579 e) {
                            LOGGER.warn("Failed to validate symlinks for world {}", (Object)summary.method_248(), (Object)e);
                        }
                    }
                }
                catch (IOException e) {
                    LOGGER.error("Failed to delete worlds", (Throwable)e);
                }
                this.loadWorldList();
            }
            this.field_22787.method_1507((class_437)this);
        }, (class_2561)title, (class_2561)message));
    }

    private Set<class_34> getSelectedSummaries() {
        return this.worldList.getSelectedEntries().stream().map(WorldListEntryWidget::getSummary).collect(Collectors.toSet());
    }

    public void method_25419() {
        this.field_22787.method_1507(this.parent);
    }
}

