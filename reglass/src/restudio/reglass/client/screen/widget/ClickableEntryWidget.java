/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_339
 */
package restudio.reglass.client.screen.widget;

import net.minecraft.class_2561;
import net.minecraft.class_339;

public abstract class ClickableEntryWidget<P>
extends class_339 {
    protected final P parent;

    public ClickableEntryWidget(P parent, int x, int y, int width, int height, class_2561 message) {
        super(x, y, width, height, message);
        this.parent = parent;
    }
}

