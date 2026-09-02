/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.internal.LinkedTreeMap
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 */
package wtf.opal.client.feature.module.property.impl;

import com.google.gson.internal.LinkedTreeMap;
import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;

@Environment(value=EnvType.CLIENT)
public final class ScreenPositionProperty
extends Property<Pair<Float, Float>> {
    private float startX;
    private float startY;
    private float width;
    private float height;
    private boolean dragging;

    public ScreenPositionProperty(String name, float relativeX, float relativeY) {
        super(name);
        this.setValue(Pair.of((Object)Float.valueOf(relativeX), (Object)Float.valueOf(relativeY)));
    }

    public ScreenPositionProperty(String name, ModuleMode<?> parent, float relativeX, float relativeY) {
        super(name, parent);
        this.setValue(Pair.of((Object)Float.valueOf(relativeX), (Object)Float.valueOf(relativeY)));
    }

    @Override
    public void applyValue(Object propertyValue) {
        if (propertyValue instanceof LinkedTreeMap) {
            LinkedTreeMap propertyObj = (LinkedTreeMap)propertyValue;
            if (propertyObj.isEmpty()) {
                return;
            }
            Double relativeX = (Double)propertyObj.get((Object)"x");
            Double relativeY = (Double)propertyObj.get((Object)"y");
            if (relativeX == null || relativeY == null) {
                return;
            }
            this.setValue(Pair.of((Object)Float.valueOf(relativeX.floatValue()), (Object)Float.valueOf(relativeY.floatValue())));
        }
    }

    public float getRelativeX() {
        return ((Float)((Pair)this.getValue()).first).floatValue();
    }

    public float getRelativeY() {
        return ((Float)((Pair)this.getValue()).second).floatValue();
    }

    public float getScaledX() {
        float relativeX = this.getRelativeX();
        float actualX = relativeX * (float)Constants.mc.method_22683().method_4486();
        if (relativeX > 0.5f) {
            return actualX - this.width;
        }
        return actualX;
    }

    public float getScaledY() {
        return this.getRelativeY() * (float)Constants.mc.method_22683().method_4502();
    }

    public void _setRelativeX(float relativeX) {
        this.setValue(Pair.of((Object)Float.valueOf(relativeX), (Object)Float.valueOf(this.getRelativeY())));
    }

    public void _setRelativeY(float relativeY) {
        this.setValue(Pair.of((Object)Float.valueOf(this.getRelativeX()), (Object)Float.valueOf(relativeY)));
    }

    public void setRelativeX(float scaledX) {
        int scaledWidth = Constants.mc.method_22683().method_4486();
        float relativeX = scaledX / (float)scaledWidth;
        if (relativeX > 0.5f) {
            relativeX += this.width / (float)scaledWidth;
        }
        this._setRelativeX(relativeX);
    }

    public void setRelativeY(float scaledY) {
        float relativeY = scaledY / (float)Constants.mc.method_22683().method_4502();
        this._setRelativeY(relativeY);
    }

    public void snapToGrid() {
        class_1041 window = Constants.mc.method_22683();
        int scaledWidth = window.method_4486();
        int scaledHeight = window.method_4502();
        float relativeX = this.getRelativeX();
        float relativeY = this.getRelativeY();
        float relativeWidth = this.width / (float)scaledWidth;
        float relativeHeight = this.height / (float)scaledHeight;
        float halfWidth = relativeWidth / 2.0f;
        float halfHeight = relativeHeight / 2.0f;
        if (relativeX < 0.01f) {
            this._setRelativeX(0.0f);
        } else if (relativeX > 0.99f) {
            this._setRelativeX(1.0f);
        } else if (relativeX + halfWidth > 0.49f && relativeX + halfWidth < 0.51f) {
            this._setRelativeX(0.5f - halfWidth);
        }
        if (relativeY < 0.01f) {
            this._setRelativeY(0.0f);
        } else if (relativeY + relativeHeight > 0.99f) {
            this._setRelativeY(1.0f - relativeHeight);
        } else if (relativeY + halfHeight > 0.49f && relativeY + halfHeight < 0.51f) {
            this._setRelativeY(0.5f - halfHeight);
        }
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public float getStartX() {
        return this.startX > (float)Constants.mc.method_22683().method_4486() / 2.0f ? this.startX - this.width : this.startX;
    }

    public float getStartY() {
        return this.startY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }
}

