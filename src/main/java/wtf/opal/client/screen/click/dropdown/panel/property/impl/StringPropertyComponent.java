/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_156
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_3544
 *  net.minecraft.class_3728$class_7279
 *  net.minecraft.class_5225
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_156;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3544;
import net.minecraft.class_3728;
import net.minecraft.class_5225;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.misc.HoverUtility;

@Environment(value=EnvType.CLIENT)
public final class StringPropertyComponent
extends PropertyPanel<StringProperty> {
    private boolean focused;
    private int selectionStart;
    private int selectionEnd;

    public StringPropertyComponent(StringProperty property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.setHeight(26.0f);
        super.render(context, mouseX, mouseY, delta);
        StringProperty property = (StringProperty)this.getProperty();
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        font.drawString(property.getName(), this.x + 5.0f, this.y + 8.5f, 7.0f, -1);
        NVGRenderer.roundedRectOutline(this.x + 5.0f, this.y + 13.0f, this.width - 10.0f, 10.0f, 2.5f, 1.5f, -11513776);
        NVGRenderer.roundedRect(this.x + 5.0f, this.y + 13.0f, this.width - 10.0f, 10.0f, 2.5f, -15132391);
        if (!((String)property.getValue()).isEmpty()) {
            String selectedText = this.getSelectedText((String)property.getValue());
            if (!selectedText.isEmpty() && this.focused) {
                NVGRenderer.rect(this.x + 7.0f, this.y + 20.0f - 5.5f, font.getStringWidth(selectedText, 7.0f), 7.0f, -14396782);
            }
            font.drawString((String)property.getValue(), this.x + 7.0f, this.y + 20.0f, 7.0f, -8355712);
            if (this.focused && selectedText.isEmpty() && Constants.mc.field_1724.field_6012 % 20 > 8) {
                NVGRenderer.rect(this.x + 7.5f + font.getStringWidth(((String)property.getValue()).substring(0, this.selectionStart), 7.0f), this.y + 20.0f - 5.5f, 0.5f, 7.0f, -1);
            }
        }
    }

    @Override
    public void init() {
        this.focused = false;
        DropdownClickGUI.typingString = false;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        DropdownClickGUI.typingString = this.focused = button == 0 && HoverUtility.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.focused) {
            NVGTextRenderer font;
            float charWidth;
            int index;
            double relativeX = mouseX - (double)(this.x + 7.0f);
            float cursorX = 0.0f;
            for (index = 0; index < ((String)((StringProperty)this.getProperty()).getValue()).length() && !(relativeX < (double)(cursorX + (charWidth = (font = FontRepository.getFont("productsans-medium")).getStringWidth(String.valueOf(((String)((StringProperty)this.getProperty()).getValue()).charAt(index)), 7.0f)) / 2.0f)); ++index) {
                cursorX += charWidth;
            }
            this.selectionEnd = this.selectionStart = Math.min(index, ((String)((StringProperty)this.getProperty()).getValue()).length());
        }
    }

    @Override
    public void keyPressed(class_11908 keyInput) {
        if (!this.focused) {
            return;
        }
        if (keyInput.method_74241()) {
            this.selectionEnd = 0;
            this.selectionStart = ((String)((StringProperty)this.getProperty()).getValue()).length();
            return;
        }
        if (keyInput.method_74242()) {
            Constants.mc.field_1774.method_1455(this.getSelectedText((String)((StringProperty)this.getProperty()).getValue()));
            return;
        }
        if (keyInput.method_74243()) {
            this.paste();
            return;
        }
        class_3728.class_7279 selectionType = keyInput.method_74240() ? class_3728.class_7279.field_38309 : class_3728.class_7279.field_38308;
        int keyCode = keyInput.comp_4795();
        if (keyCode == 259) {
            this.delete(-1, selectionType);
            return;
        }
        if (keyCode == 261) {
            this.delete(1, selectionType);
            return;
        }
        if (keyCode == 263) {
            this.moveCursor(-1, keyInput.method_74239(), selectionType);
            return;
        }
        if (keyCode == 262) {
            this.moveCursor(1, keyInput.method_74239(), selectionType);
            return;
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (!this.focused || !class_3544.method_57175((int)chr)) {
            return;
        }
        this.insert(Character.toString(chr));
    }

    private void updateSelectionRange(boolean shiftDown) {
        if (!shiftDown) {
            this.selectionEnd = this.selectionStart;
        }
    }

    public void moveCursor(int offset, boolean shiftDown) {
        this.selectionStart = class_156.method_27761((String)((String)((StringProperty)this.getProperty()).getValue()), (int)this.selectionStart, (int)offset);
        this.updateSelectionRange(shiftDown);
    }

    public void moveCursorPastWord(int offset, boolean shiftDown) {
        this.selectionStart = class_5225.method_27483((String)((String)((StringProperty)this.getProperty()).getValue()), (int)offset, (int)this.selectionStart, (boolean)true);
        this.updateSelectionRange(shiftDown);
    }

    public void paste() {
        this.insert(Constants.mc.field_1774.method_1460());
        this.selectionEnd = this.selectionStart;
    }

    public void moveCursor(int offset, boolean shiftDown, class_3728.class_7279 selectionType) {
        switch (selectionType) {
            case field_38308: {
                this.moveCursor(offset, shiftDown);
                break;
            }
            case field_38309: {
                this.moveCursorPastWord(offset, shiftDown);
            }
        }
    }

    private void insert(String insertion) {
        String originalString = (String)((StringProperty)this.getProperty()).getValue();
        if (this.selectionEnd != this.selectionStart) {
            originalString = this.deleteSelectedText(originalString);
        }
        this.selectionStart = class_3532.method_15340((int)this.selectionStart, (int)0, (int)originalString.length());
        String finishedString = new StringBuilder(originalString).insert(this.selectionStart, insertion).toString();
        ((StringProperty)this.getProperty()).setValue(finishedString);
        this.selectionEnd = this.selectionStart = Math.min(finishedString.length(), this.selectionStart + insertion.length());
    }

    public void delete(int offset, class_3728.class_7279 selectionType) {
        switch (selectionType) {
            case field_38308: {
                this.delete(offset);
                break;
            }
            case field_38309: {
                this.deleteWord(offset);
            }
        }
    }

    public void deleteWord(int offset) {
        int i = class_5225.method_27483((String)((String)((StringProperty)this.getProperty()).getValue()), (int)offset, (int)this.selectionStart, (boolean)true);
        this.delete(i - this.selectionStart);
    }

    public void delete(int offset) {
        if (!((String)((StringProperty)this.getProperty()).getValue()).isEmpty()) {
            String string;
            if (this.selectionEnd != this.selectionStart) {
                string = this.deleteSelectedText((String)((StringProperty)this.getProperty()).getValue());
            } else {
                int cursor = class_156.method_27761((String)((String)((StringProperty)this.getProperty()).getValue()), (int)this.selectionStart, (int)offset);
                int minCursor = Math.min(cursor, this.selectionStart);
                int maxCursor = Math.max(cursor, this.selectionStart);
                string = new StringBuilder((String)((StringProperty)this.getProperty()).getValue()).delete(minCursor, maxCursor).toString();
                if (offset < 0) {
                    this.selectionEnd = this.selectionStart = minCursor;
                }
            }
            ((StringProperty)this.getProperty()).setValue(string);
        }
    }

    private String deleteSelectedText(String string) {
        if (this.selectionEnd == this.selectionStart) {
            return string;
        }
        int minSelection = Math.min(this.selectionStart, this.selectionEnd);
        int maxSelection = Math.max(this.selectionStart, this.selectionEnd);
        this.selectionEnd = this.selectionStart = minSelection;
        return string.substring(0, minSelection) + string.substring(maxSelection);
    }

    private String getSelectedText(String string) {
        int minSelected = Math.min(this.selectionStart, this.selectionEnd);
        int maxSelected = Math.max(this.selectionStart, this.selectionEnd);
        return string.substring(minSelected, maxSelected);
    }
}

