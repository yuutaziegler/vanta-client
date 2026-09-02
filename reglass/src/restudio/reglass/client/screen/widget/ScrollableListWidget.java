/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  net.minecraft.class_1041
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675
 *  net.minecraft.class_437
 *  net.minecraft.class_6382
 */
package restudio.reglass.client.screen.widget;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1041;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import net.minecraft.class_6382;
import restudio.reglass.client.screen.widget.ClickableEntryWidget;

public class ScrollableListWidget<E extends Entry<E>>
extends ClickableEntryWidget<class_437> {
    protected final int itemHeight;
    private int verticalPadding = 0;
    private final List<E> entries = Lists.newArrayList();
    private final Set<E> selectedEntries = Sets.newHashSet();
    private double scrollAmount;

    public ScrollableListWidget(class_437 screen, int x, int y, int width, int height, int itemHeight) {
        super(screen, x, y, width, height, (class_2561)class_2561.method_43473());
        this.itemHeight = itemHeight;
    }

    public void setVerticalPadding(int padding) {
        this.verticalPadding = padding;
    }

    public int getVerticalPadding() {
        return this.verticalPadding;
    }

    public void addEntry(E entry) {
        this.entries.add(entry);
    }

    public void clearEntries() {
        this.entries.forEach(Entry::close);
        this.entries.clear();
        this.selectedEntries.clear();
    }

    public List<E> getEntries() {
        return this.entries;
    }

    public Set<E> getSelectedEntries() {
        return this.selectedEntries;
    }

    public void setSelected(E entry) {
        if (!class_3675.method_15987((class_1041)class_310.method_1551().method_22683(), (int)341)) {
            this.selectedEntries.clear();
        }
        if (this.selectedEntries.contains(entry)) {
            this.selectedEntries.remove(entry);
        } else {
            this.selectedEntries.add(entry);
        }
    }

    public void method_48579(class_332 context, int mouseX, int mouseY, float delta) {
        context.method_44379(this.method_46426(), this.method_46427(), this.method_46426() + this.field_22758, this.method_46427() + this.field_22759);
        int top = this.method_46427() - (int)this.scrollAmount + this.verticalPadding;
        for (int i = 0; i < this.entries.size(); ++i) {
            Entry entry = (Entry)this.entries.get(i);
            int entryY = top + i * (this.itemHeight + this.verticalPadding);
            if (entryY + this.itemHeight < this.method_46427() || entryY > this.method_46427() + this.field_22759) continue;
            boolean isHovered = this.method_25405(mouseX, mouseY) && mouseY >= entryY && mouseY < entryY + this.itemHeight;
            entry.render(context, i, this.method_46426(), entryY, this.field_22758, this.itemHeight, mouseX, mouseY, isHovered, delta);
        }
        context.method_44380();
    }

    public boolean method_25402(class_11909 button, boolean isDouble) {
        if (this.method_25405(button.comp_4799(), button.comp_4799())) {
            int top = this.method_46427() - (int)this.scrollAmount + this.verticalPadding;
            for (int i = 0; i < this.entries.size(); ++i) {
                Entry entry = (Entry)this.entries.get(i);
                int entryY = top + i * (this.itemHeight + this.verticalPadding);
                if (!(button.comp_4799() >= (double)entryY) || !(button.comp_4799() < (double)(entryY + this.itemHeight)) || !entry.mouseClicked(button)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.method_25405(mouseX, mouseY)) {
            this.scrollAmount -= verticalAmount * ((double)this.itemHeight / 2.0);
            this.scrollAmount = class_3532.method_15350((double)this.scrollAmount, (double)0.0, (double)Math.max(0, this.getMaxScroll()));
            return true;
        }
        return false;
    }

    private int getMaxScroll() {
        return this.entries.size() * (this.itemHeight + this.verticalPadding) - this.verticalPadding - this.field_22759;
    }

    public void method_25365(boolean focused) {
    }

    public boolean method_25370() {
        return false;
    }

    protected void method_47399(class_6382 builder) {
    }

    public static abstract class Entry<E extends Entry<E>> {
        protected int x;
        protected int y;
        protected int width;
        protected int height;

        public Entry(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void render(class_332 context, int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public abstract boolean mouseClicked(class_11909 var1);

        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
        }

        public void close() {
        }
    }
}

