/*
 * VantaClickGUIScreen - the new Right-Shift module menu.
 *
 * Complete rewrite: liquid-glass window (VantaGlass), draggable + resizable
 * (scale buttons / Ctrl+Scroll), working search, category rail, module
 * toggles, keybind selection and full property editing (boolean, number
 * sliders, mode cycling, color picker).  Never renders an opaque backdrop,
 * so the screen can never appear "black / intunecat".
 */
package wtf.opal.client.screen.vanta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.client.binding.repository.BindRepository;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.ClickGUIModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.ColorProperty;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.VantaGlass;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.misc.Multithreading;

@Environment(value = EnvType.CLIENT)
public final class VantaClickGUIScreen extends class_437 {
    private static final int ACCENT = 0xFF4C8DFF;
    private static final int TEXT = 0xFFF4F7FF;
    private static final int TEXT_DIM = 0xFF9AA7C2;
    private static final int ROW_BG = 0x18FFFFFF;
    private static final int ROW_HOVER = 0x26FFFFFF;
    private static final int ROW_ON = 0x334C8DFF;

    private static float winX = -1.0f;
    private static float winY = -1.0f;
    private static float guiScale = 1.0f;
    private static float scrollTarget = 0.0f;
    private float scrollSmooth = 0.0f;

    private ModuleCategory selectedCategory = null;
    private String search = "";
    private boolean searchFocused = false;

    private Module bindingModule = null;
    private Module expandedModule = null;
    private ColorProperty openColor = null;

    private NumberProperty draggingNumber = null;
    private float dragTrackX;
    private float dragTrackW;
    private boolean draggingHue = false;
    private boolean draggingSV = false;
    private ColorProperty dragColor = null;
    private float dragPickerX;
    private float dragPickerY;
    private float dragPickerW;
    private float dragPickerH;
    private boolean draggingWindow = false;
    private float dragOffX;
    private float dragOffY;

    public VantaClickGUIScreen() {
        super(class_2561.method_43470("Vanta ClickGUI"));
    }

    /* ============================== layout ============================== */

    private float s() {
        return guiScale;
    }

    private float winW() {
        return 560.0f * guiScale;
    }

    private float winH() {
        return 380.0f * guiScale;
    }

    private float railW() {
        return 118.0f * guiScale;
    }

    /* ============================== render ============================== */

    @Override
    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        float screenW = Constants.mc.method_22683().method_4486();
        float screenH = Constants.mc.method_22683().method_4502();
        float w = Math.min(winW(), screenW - 16.0f);
        float h = Math.min(winH(), screenH - 16.0f);
        if (winX < 0.0f || winY < 0.0f) {
            winX = (screenW - w) / 2.0f;
            winY = (screenH - h) / 2.0f;
        }
        this.pollDrags(mouseX, mouseY);
        winX = Math.max(0.0f, Math.min(screenW - w, winX));
        winY = Math.max(0.0f, Math.min(screenH - h, winY));
        this.scrollSmooth += (scrollTarget - this.scrollSmooth) * 0.35f;

        float s = this.s();
        NVGTextRenderer bold = FontRepository.getFont("inter-bold");
        NVGTextRenderer med = FontRepository.getFont("inter-medium");
        NVGTextRenderer reg = FontRepository.getFont("inter-regular");

        boolean frameStarted = NVGRenderer.beginFrame();

        // ---- glass window
        VantaGlass.panel(winX, winY, w, h, 12.0f * s, 0.14f);

        // ---- title bar
        float titleH = 34.0f * s;
        NVGImageRenderer logo = ImageRepository.getImage("logo.png");
        if (logo != null) {
            logo.drawImage(winX + 10.0f * s, winY + 8.0f * s, titleH - 16.0f * s, titleH - 16.0f * s);
        }
        bold.drawString("VANTA", winX + 30.0f * s, winY + 12.5f * s, 11.0f * s, TEXT);
        long active = OpalClient.getInstance().getModuleRepository().getModules().stream().filter(Module::isEnabled).count();
        String stats = active + " active";
        med.drawString(stats, winX + 78.0f * s, winY + 14.5f * s, 7.0f * s, TEXT_DIM);

        // scale buttons
        float btnY = winY + 9.0f * s;
        float minusX = winX + w - 96.0f * s;
        float plusX = winX + w - 74.0f * s;
        float hudX = winX + w - 52.0f * s;
        float closeX = winX + w - 26.0f * s;
        this.miniButton(minusX, btnY, 16.0f * s, 16.0f * s, "-", mouseX, mouseY, med, s);
        this.miniButton(plusX, btnY, 16.0f * s, 16.0f * s, "+", mouseX, mouseY, med, s);
        this.miniButton(hudX, btnY, 24.0f * s, 16.0f * s, "HUD", mouseX, mouseY, med, s);
        this.miniButton(closeX, btnY, 16.0f * s, 16.0f * s, "x", mouseX, mouseY, med, s);

        // ---- left rail (categories)
        float railX = winX;
        float railTop = winY + titleH;
        NVGRenderer.roundedRect(railX, railTop, railW(), h - titleH, 12.0f * s, 0x12000000);
        float catY = railTop + 8.0f * s;
        this.categoryButton(railX + 8.0f * s, catY, railW() - 16.0f * s, 22.0f * s, "All", this.selectedCategory == null, mouseX, mouseY, med, s);
        catY += 26.0f * s;
        List<ModuleCategory> cats = this.availableCategories();
        for (ModuleCategory cat : cats) {
            this.categoryButton(railX + 8.0f * s, catY, railW() - 16.0f * s, 22.0f * s, cat.getName(), this.selectedCategory == cat, mouseX, mouseY, med, s);
            catY += 26.0f * s;
        }

        // ---- content area
        float contentX = winX + railW();
        float contentW = w - railW();
        float searchH = 24.0f * s;
        float searchX = contentX + 10.0f * s;
        float searchY = winY + titleH + 8.0f * s;
        float searchW = contentW - 20.0f * s;
        boolean searchHover = HoverUtility.isHovering(searchX, searchY, searchW, searchH, mouseX, mouseY);
        NVGRenderer.roundedRect(searchX, searchY, searchW, searchH, 6.0f * s, this.searchFocused ? 0x304C8DFF : (searchHover ? ROW_HOVER : ROW_BG));
        NVGRenderer.roundedRectOutline(searchX, searchY, searchW, searchH, 6.0f * s, 1.0f, this.searchFocused ? VantaGlass.accent() : 0x22FFFFFF);
        String shown = this.search.isEmpty() && !this.searchFocused ? "Search modules..."
                : this.search + (this.searchFocused && System.currentTimeMillis() % 1000L > 500L ? "_" : "");
        med.drawString(shown, searchX + 8.0f * s, searchY + 8.0f * s, 7.5f * s, this.search.isEmpty() && !this.searchFocused ? TEXT_DIM : TEXT);

        float listTop = searchY + searchH + 8.0f * s;
        float listBottom = winY + h - 8.0f * s;
        float listH = listBottom - listTop;

        NVGRenderer.scissor(contentX, listTop, contentW, listH, () -> {
            float y = listTop - this.scrollSmooth;
            for (Module module : this.visibleModules()) {
                y = this.renderModuleRow(module, y, contentX + 8.0f * s, contentW - 16.0f * s, mouseX, mouseY, bold, med, reg, s, listTop, listBottom);
            }
            if (this.visibleModules().isEmpty()) {
                med.drawString("No modules found.", contentX + 14.0f * s, listTop + 10.0f * s, 8.0f * s, TEXT_DIM);
            }
        });

        // scrollbar
        float contentHeight = this.contentHeight(s);
        if (contentHeight > listH) {
            float maxScroll = contentHeight - listH;
            float trackH = listH;
            float thumbH = Math.max(24.0f * s, trackH * (listH / contentHeight));
            float thumbY = listTop + (this.scrollSmooth / maxScroll) * (trackH - thumbH);
            NVGRenderer.roundedRect(contentX + contentW - 4.0f * s, listTop, 2.5f * s, trackH, 1.25f * s, 0x18FFFFFF);
            NVGRenderer.roundedRect(contentX + contentW - 4.0f * s, thumbY, 2.5f * s, thumbH, 1.25f * s, 0xCC4C8DFF);
        }

        if (frameStarted) {
            NVGRenderer.endFrameAndReset(true);
        }
    }

    private float renderModuleRow(Module module, float y, float x, float w, int mouseX, int mouseY,
                                  NVGTextRenderer bold, NVGTextRenderer med, NVGTextRenderer reg,
                                  float s, float listTop, float listBottom) {
        float rowH = 26.0f * s;
        boolean expanded = this.expandedModule == module;
        boolean binding = this.bindingModule == module;
        boolean hover = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + rowH && mouseY >= listTop - rowH && mouseY <= listBottom;

        int bg = module.isEnabled() ? ROW_ON : (hover ? ROW_HOVER : ROW_BG);
        NVGRenderer.roundedRect(x, y, w, rowH, 6.0f * s, bg);
        if (module.isEnabled()) {
            NVGRenderer.roundedRect(x, y, 3.0f * s, rowH, 2.0f * s, ACCENT);
        }
        bold.drawString(module.getName(), x + 10.0f * s, y + 9.0f * s, 8.5f * s, module.isEnabled() ? TEXT : 0xFFD5DCEC);

        // bind label
        String bindLabel = this.bindLabel(module);
        if (binding) {
            bindLabel = "[press key]";
        }
        if (bindLabel != null) {
            float bw = med.getStringWidth(bindLabel, 6.0f * s) + 8.0f * s;
            NVGRenderer.roundedRect(x + 10.0f * s, y + 17.0f * s, bw, 8.5f * s, 2.5f * s, binding ? 0x554C8DFF : 0x22000000);
            med.drawString(bindLabel, x + 14.0f * s, y + 18.5f * s, 6.0f * s, binding ? TEXT : TEXT_DIM);
        }

        // expand chevron
        boolean hasProps = !this.editableProperties(module).isEmpty();
        float chevX = x + w - 62.0f * s;
        if (hasProps) {
            med.drawString(expanded ? "-" : "+", chevX + 4.0f * s, y + 8.0f * s, 10.0f * s, hover ? TEXT : TEXT_DIM);
        }

        // toggle pill
        float pillW = 26.0f * s;
        float pillH = 14.0f * s;
        float pillX = x + w - pillW - 8.0f * s;
        float pillY = y + (rowH - pillH) / 2.0f;
        NVGRenderer.roundedRect(pillX, pillY, pillW, pillH, pillH / 2.0f, module.isEnabled() ? ACCENT : 0x4020242E);
        float thumbX = module.isEnabled() ? pillX + pillW - pillH + 1.5f * s : pillX + 1.5f * s;
        NVGRenderer.roundedRect(thumbX, pillY + 1.5f * s, pillH - 3.0f * s, pillH - 3.0f * s, (pillH - 3.0f * s) / 2.0f, TEXT);

        y += rowH + 3.0f * s;

        if (expanded) {
            List<Property<?>> props = this.editableProperties(module);
            for (Property<?> prop : props) {
                y = this.renderProperty(module, prop, y, x + 8.0f * s, w - 16.0f * s, 0, mouseX, mouseY, med, reg, s, listTop, listBottom);
            }
            y += 4.0f * s;
        }
        return y;
    }

    private float renderProperty(Module module, Property<?> prop, float y, float x, float w, int depth,
                                 int mouseX, int mouseY, NVGTextRenderer med, NVGTextRenderer reg,
                                 float s, float listTop, float listBottom) {
        if (prop.isHidden()) {
            return y;
        }
        if (prop instanceof GroupProperty) {
            GroupProperty group = (GroupProperty) group(prop);
            float gh = 20.0f * s;
            boolean gHover = HoverUtility.isHovering(x + depth * 10.0f * s, y, w - depth * 10.0f * s, gh, mouseX, mouseY);
            NVGRenderer.roundedRect(x + depth * 10.0f * s, y, w - depth * 10.0f * s, gh, 5.0f * s, gHover ? ROW_HOVER : 0x10000000);
            med.drawString((group.isEnabled() ? "v " : "> ") + group.getName(), x + 8.0f * s + depth * 10.0f * s, y + 7.0f * s, 7.0f * s, TEXT_DIM);
            y += gh;
            if (group.isEnabled()) {
                for (Property<?> child : group.getPropertyList()) {
                    y = this.renderProperty(module, child, y, x + 6.0f * s, w - 12.0f * s, depth + 1, mouseX, mouseY, med, reg, s, listTop, listBottom);
                }
            }
            return y;
        }

        float ph = 19.0f * s;
        float indent = depth * 10.0f * s;
        boolean hover = HoverUtility.isHovering(x + indent, y, w - indent, ph, mouseX, mouseY);
        NVGRenderer.roundedRect(x + indent, y, w - indent, ph, 5.0f * s, hover ? 0x1AFFFFFF : 0x00000000);

        float textX = x + 8.0f * s + indent;
        if (prop instanceof BooleanProperty) {
            BooleanProperty bp = (BooleanProperty) prop;
            med.drawString(bp.getName(), textX, y + 6.0f * s, 7.0f * s, TEXT);
            float pillW = 22.0f * s;
            float pillH = 12.0f * s;
            float pillX = x + w - pillW - 6.0f * s;
            float pillY = y + (ph - pillH) / 2.0f;
            NVGRenderer.roundedRect(pillX, pillY, pillW, pillH, pillH / 2.0f, bp.getValue() ? ACCENT : 0x4020242E);
            float thumbX = bp.getValue() ? pillX + pillW - pillH + 1.5f * s : pillX + 1.5f * s;
            NVGRenderer.roundedRect(thumbX, pillY + 1.5f * s, pillH - 3.0f * s, pillH - 3.0f * s, (pillH - 3.0f * s) / 2.0f, TEXT);
        } else if (prop instanceof NumberProperty) {
            NumberProperty np = (NumberProperty) prop;
            med.drawString(np.getName(), textX, y + 5.0f * s, 7.0f * s, TEXT);
            String val = formatNumber(np.getValue()) + np.getSuffix();
            float vw = med.getStringWidth(val, 6.5f * s);
            med.drawString(val, x + w - vw - 8.0f * s, y + 5.0f * s, 6.5f * s, TEXT_DIM);
            float trackX = textX;
            float trackW = w - (textX - x) - 12.0f * s;
            float trackY = y + 13.5f * s;
            NVGRenderer.roundedRect(trackX, trackY, trackW, 3.0f * s, 1.5f * s, 0x3020242E);
            double frac = (np.getValue() - np.getMinValue()) / Math.max(1.0E-6, np.getMaxValue() - np.getMinValue());
            frac = Math.max(0.0, Math.min(1.0, frac));
            float fillW = (float) (trackW * frac);
            NVGRenderer.roundedRect(trackX, trackY, Math.max(3.0f * s, fillW), 3.0f * s, 1.5f * s, ACCENT);
            NVGRenderer.roundedRect(trackX + fillW - 3.0f * s, trackY - 2.0f * s, 6.0f * s, 7.0f * s, 3.0f * s, TEXT);
        } else if (prop instanceof ModeProperty) {
            ModeProperty<?> mp = (ModeProperty) prop;
            med.drawString(mp.getName(), textX, y + 6.0f * s, 7.0f * s, TEXT);
            String val = String.valueOf(mp.getValue());
            float vw = med.getStringWidth(val, 6.5f * s) + 14.0f * s;
            float vx = x + w - vw - 6.0f * s;
            NVGRenderer.roundedRect(vx, y + 3.0f * s, vw, 12.0f * s, 4.0f * s, hover ? 0x334C8DFF : ROW_BG);
            med.drawString(val, vx + 7.0f * s, y + 6.5f * s, 6.5f * s, TEXT);
        } else if (prop instanceof ColorProperty) {
            ColorProperty cp = (ColorProperty) prop;
            med.drawString(cp.getName(), textX, y + 6.0f * s, 7.0f * s, TEXT);
            int col = cp.getValue() | 0xFF000000;
            float swW = 26.0f * s;
            float swH = 12.0f * s;
            float swX = x + w - swW - 6.0f * s;
            float swY = y + 3.0f * s;
            NVGRenderer.roundedRect(swX, swY, swW, swH, 4.0f * s, col);
            NVGRenderer.roundedRectOutline(swX, swY, swW, swH, 4.0f * s, 1.0f, 0x55FFFFFF);
            if (this.openColor == cp) {
                y += ph;
                y = this.renderColorPicker(cp, y, textX, 150.0f * s, mouseX, mouseY, s);
                return y;
            }
        } else {
            med.drawString(prop.getName(), textX, y + 6.0f * s, 7.0f * s, TEXT_DIM);
        }
        return y + ph + 1.0f * s;
    }

    private float renderColorPicker(ColorProperty cp, float y, float x, float w, int mouseX, int mouseY, float s) {
        float[] hsb = cp.getHSB();
        float boxH = 46.0f * s;
        // saturation/brightness box
        int satCol = java.awt.Color.HSBtoRGB(hsb[0], 1.0f, 1.0f) | 0xFF000000;
        NVGRenderer.roundedRectGradient(x, y, w, boxH, 5.0f * s, 0xFFFFFFFF, satCol, 0.0f);
        NVGRenderer.roundedRectGradient(x, y, w, boxH, 5.0f * s, 0x00000000, 0xFF000000, 90.0f);
        float selX = x + hsb[1] * w;
        float selY = y + (1.0f - hsb[2]) * boxH;
        NVGRenderer.roundedRectOutline(selX - 2.5f * s, selY - 2.5f * s, 5.0f * s, 5.0f * s, 2.5f * s, 1.5f, 0xFFFFFFFF);
        // hue bar
        float hueY = y + boxH + 6.0f * s;
        float hueH = 8.0f * s;
        for (int i = 0; i < 6; i++) {
            int c1 = java.awt.Color.HSBtoRGB(i / 6.0f, 1.0f, 1.0f) | 0xFF000000;
            int c2 = java.awt.Color.HSBtoRGB((i + 1) / 6.0f, 1.0f, 1.0f) | 0xFF000000;
            NVGRenderer.roundedRectGradient(x + i * (w / 6.0f), hueY, w / 6.0f + 1.0f, hueH, 0.0f, c1, c2, 0.0f);
        }
        NVGRenderer.roundedRectOutline(x, hueY, w, hueH, 2.0f * s, 1.0f, 0x55FFFFFF);
        float hueX = x + hsb[0] * w;
        NVGRenderer.roundedRect(hueX - 1.5f * s, hueY - 2.0f * s, 3.0f * s, hueH + 4.0f * s, 1.5f * s, TEXT);
        return hueY + hueH + 6.0f * s;
    }

    /* ============================== helpers ============================ */

    private void miniButton(float x, float y, float w, float h, String label, int mouseX, int mouseY, NVGTextRenderer med, float s) {
        boolean hover = HoverUtility.isHovering(x, y, w, h, mouseX, mouseY);
        NVGRenderer.roundedRect(x, y, w, h, 4.0f * s, hover ? 0x554C8DFF : 0x22000000);
        float tw = med.getStringWidth(label, 7.0f * s);
        med.drawString(label, x + (w - tw) / 2.0f, y + h / 2.0f - 4.0f * s, 7.0f * s, TEXT);
    }

    private void categoryButton(float x, float y, float w, float h, String label, boolean active, int mouseX, int mouseY, NVGTextRenderer med, float s) {
        boolean hover = HoverUtility.isHovering(x, y, w, h, mouseX, mouseY);
        NVGRenderer.roundedRect(x, y, w, h, 6.0f * s, active ? 0x554C8DFF : (hover ? ROW_HOVER : 0x00000000));
        if (active) {
            NVGRenderer.roundedRect(x, y + 4.0f * s, 2.5f * s, h - 8.0f * s, 1.0f * s, TEXT);
        }
        med.drawString(label, x + 10.0f * s, y + 7.5f * s, 7.5f * s, active ? TEXT : (hover ? 0xFFD5DCEC : TEXT_DIM));
    }

    private List<ModuleCategory> availableCategories() {
        List<ModuleCategory> out = new ArrayList<>();
        for (ModuleCategory cat : ModuleCategory.VALUES) {
            if (cat == ModuleCategory.MISC) {
                continue;
            }
            boolean has = OpalClient.getInstance().getModuleRepository().getModules().stream().anyMatch(m -> m.getCategory() == cat);
            if (has) {
                out.add(cat);
            }
        }
        return out;
    }

    private List<Module> visibleModules() {
        List<Module> out = new ArrayList<>();
        String q = this.search.toLowerCase();
        for (Module m : OpalClient.getInstance().getModuleRepository().getModules()) {
            if (this.selectedCategory != null && m.getCategory() != this.selectedCategory) {
                continue;
            }
            if (!q.isEmpty() && !m.getName().toLowerCase().contains(q) && !(m.getDescription() != null && m.getDescription().toLowerCase().contains(q))) {
                continue;
            }
            out.add(m);
        }
        out.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return out;
    }

    private List<Property<?>> editableProperties(Module module) {
        List<Property<?>> out = new ArrayList<>();
        for (Property<?> p : module.getPropertyList()) {
            if (p instanceof GroupProperty || p instanceof BooleanProperty || p instanceof NumberProperty || p instanceof ModeProperty || p instanceof ColorProperty) {
                out.add(p);
            }
        }
        return out;
    }

    private float contentHeight(float s) {
        float y = 0.0f;
        for (Module module : this.visibleModules()) {
            y += 29.0f * s;
            if (this.expandedModule == module) {
                y += this.propertiesHeight(module, s);
            }
        }
        return y;
    }

    private float propertiesHeight(Module module, float s) {
        float y = 0.0f;
        for (Property<?> prop : this.editableProperties(module)) {
            y += this.propertyHeight(prop, s, 0);
        }
        return y + 4.0f * s;
    }

    private float propertyHeight(Property<?> prop, float s, int depth) {
        if (prop.isHidden()) {
            return 0.0f;
        }
        if (prop instanceof GroupProperty) {
            GroupProperty group = (GroupProperty) g(prop);
            float h = 20.0f * s;
            if (group.isEnabled()) {
                for (Property<?> c : group.getPropertyList()) {
                    h += this.propertyHeight(c, s, depth + 1);
                }
            }
            return h;
        }
        float h = 19.0f * s;
        if (prop instanceof ColorProperty && this.openColor == prop) {
            h += 60.0f * s;
        }
        return h;
    }

    private static GroupProperty g(Property<?> p) {
        return (GroupProperty) p;
    }

    private static String formatNumber(double v) {
        if (v == Math.rint(v)) {
            return String.valueOf((int) v);
        }
        return String.format("%.2f", v);
    }

    private String bindLabel(Module module) {
        BindRepository repo = OpalClient.getInstance().getBindRepository();
        var key = repo.getBindingService().getKeyFromBindable(module);
        if (key.isPresent()) {
            String name = repo.getNameFromInteger(key.get().first);
            return name == null ? ("KEY " + key.get().first) : name;
        }
        return null;
    }

    /* ============================== input ============================== */

    @Override
    public boolean method_25402(class_11909 click, boolean doubled) {
        double mx = click.comp_4798();
        double my = click.comp_4799();
        int button = click.method_74245();
        float s = this.s();
        float w = Math.min(winW(), Constants.mc.method_22683().method_4486() - 16.0f);
        float h = Math.min(winH(), Constants.mc.method_22683().method_4502() - 16.0f);

        // bind listening: any click binds a mouse button
        if (this.bindingModule != null) {
            BindRepository repo = OpalClient.getInstance().getBindRepository();
            repo.getBindingService().clearBindings(this.bindingModule);
            repo.getBindingService().register(button, this.bindingModule, InputType.MOUSE);
            this.bindingModule = null;
            return true;
        }

        // title bar buttons
        float btnY = winY + 9.0f * s;
        if (this.hit(mx, my, winX + w - 96.0f * s, btnY, 16.0f * s, 16.0f * s)) {
            guiScale = Math.max(0.6f, guiScale - 0.1f);
            return true;
        }
        if (this.hit(mx, my, winX + w - 74.0f * s, btnY, 16.0f * s, 16.0f * s)) {
            guiScale = Math.min(1.6f, guiScale + 0.1f);
            return true;
        }
        if (this.hit(mx, my, winX + w - 52.0f * s, btnY, 24.0f * s, 16.0f * s)) {
            this.method_25419();
            OpalClient.getInstance().getModuleRepository().getModule(wtf.opal.client.feature.module.impl.visual.UIEditorModule.class).setEnabled(true);
            return true;
        }
        if (this.hit(mx, my, winX + w - 26.0f * s, btnY, 16.0f * s, 16.0f * s)) {
            this.method_25419();
            return true;
        }

        // title bar drag
        if (button == 0 && my >= winY && my <= winY + 34.0f * s && mx >= winX && mx <= winX + w - 100.0f * s) {
            this.draggingWindow = true;
            this.dragOffX = (float) mx - winX;
            this.dragOffY = (float) my - winY;
            return true;
        }

        // search box
        float searchH = 24.0f * s;
        float searchX = winX + railW() + 10.0f * s;
        float searchY = winY + 34.0f * s + 8.0f * s;
        float searchW = w - railW() - 20.0f * s;
        this.searchFocused = this.hit(mx, my, searchX, searchY, searchW, searchH);
        if (this.searchFocused) {
            return true;
        }

        // categories
        float catY = winY + 34.0f * s + 8.0f * s;
        if (button == 0 && this.hit(mx, my, winX + 8.0f * s, catY, railW() - 16.0f * s, 22.0f * s)) {
            this.selectedCategory = null;
            return true;
        }
        catY += 26.0f * s;
        for (ModuleCategory cat : this.availableCategories()) {
            if (button == 0 && this.hit(mx, my, winX + 8.0f * s, catY, railW() - 16.0f * s, 22.0f * s)) {
                this.selectedCategory = cat;
                scrollTarget = 0.0f;
                return true;
            }
            catY += 26.0f * s;
        }

        // module rows
        float listTop = searchY + searchH + 8.0f * s;
        float listBottom = winY + h - 8.0f * s;
        if (my >= listTop && my <= listBottom) {
            float y = listTop - this.scrollSmooth;
            float rowX = winX + railW() + 8.0f * s;
            float rowW = w - railW() - 16.0f * s;
            for (Module module : this.visibleModules()) {
                float rowH = 26.0f * s;
                if (my >= y && my <= y + rowH && mx >= rowX && mx <= rowX + rowW) {
                    if (button == 1) {
                        this.bindingModule = module;
                        return true;
                    }
                    if (button == 0) {
                        boolean hasProps = !this.editableProperties(module).isEmpty();
                        float chevX = rowX + rowW - 62.0f * s;
                        if (hasProps && mx >= chevX - 4.0f * s && mx <= chevX + 18.0f * s) {
                            this.expandedModule = this.expandedModule == module ? null : module;
                            this.openColor = null;
                            return true;
                        }
                        module.toggle();
                        return true;
                    }
                }
                y += rowH + 3.0f * s;
                if (this.expandedModule == module) {
                    y = this.clickProperties(module, y, rowX + 8.0f * s, rowW - 16.0f * s, mx, my, button, s, listTop, listBottom);
                }
            }
        }
        return true;
    }

    private float clickProperties(Module module, float y, float x, float w, double mx, double my, int button, float s, float listTop, float listBottom) {
        for (Property<?> prop : this.editableProperties(module)) {
            y = this.clickProperty(prop, y, x, w, 0, mx, my, button, s);
        }
        return y + 4.0f * s;
    }

    private float clickProperty(Property<?> prop, float y, float x, float w, int depth, double mx, double my, int button, float s) {
        if (prop.isHidden()) {
            return y;
        }
        if (prop instanceof GroupProperty) {
            GroupProperty group = (GroupProperty) g(prop);
            float gh = 20.0f * s;
            y += gh;
            if (group.isEnabled()) {
                for (Property<?> child : group.getPropertyList()) {
                    y = this.clickProperty(child, y, x + 6.0f * s, w - 12.0f * s, depth + 1, mx, my, button, s);
                }
            }
            return y;
        }

        float ph = 19.0f * s;
        float indent = depth * 10.0f * s;
        float textX = x + 8.0f * s + indent;
        if (my >= y && my <= y + ph && mx >= x + indent && mx <= x + w) {
            if (prop instanceof BooleanProperty && button == 0) {
                ((BooleanProperty) prop).toggle();
            } else if (prop instanceof ModeProperty && button == 0) {
                ((ModeProperty<?>) prop).cycle(true);
            } else if (prop instanceof ModeProperty && button == 1) {
                ((ModeProperty<?>) prop).cycle(false);
            } else if (prop instanceof NumberProperty && button == 0) {
                NumberProperty np = (NumberProperty) prop;
                this.draggingNumber = np;
                this.dragTrackX = textX;
                this.dragTrackW = w - (textX - x) - 12.0f * s;
                this.updateNumber(np, mx);
            } else if (prop instanceof ColorProperty && button == 0) {
                ColorProperty cp = (ColorProperty) prop;
                this.openColor = this.openColor == cp ? null : cp;
            }
        }
        y += ph + 1.0f * s;
        if (prop instanceof ColorProperty && this.openColor == prop) {
            ColorProperty cp = (ColorProperty) prop;
            float boxW = 150.0f * s;
            float boxH = 46.0f * s;
            float hueY = y + 6.0f * s;
            if (button == 0 && mx >= textX && mx <= textX + boxW) {
                this.dragColor = cp;
                this.dragPickerX = textX;
                this.dragPickerY = y;
                this.dragPickerW = boxW;
                this.dragPickerH = boxH;
                if (my >= y && my <= y + boxH) {
                    this.draggingSV = true;
                    this.updateSV(cp, mx, my, textX, y, boxW, boxH);
                } else if (my >= hueY && my <= hueY + 8.0f * s) {
                    this.draggingHue = true;
                    this.updateHue(cp, mx, textX, boxW);
                }
            }
            y = hueY + 8.0f * s + 6.0f * s;
        }
        return y;
    }

    private void updateNumber(NumberProperty np, double mx) {
        double frac = (mx - this.dragTrackX) / this.dragTrackW;
        frac = Math.max(0.0, Math.min(1.0, frac));
        double range = np.getMaxValue() - np.getMinValue();
        double value = np.getMinValue() + frac * range;
        double inc = np.getIncrement();
        if (inc > 0.0) {
            value = Math.round(value / inc) * inc;
        }
        value = Math.max(np.getMinValue(), Math.min(np.getMaxValue(), value));
        np.setValue(value);
    }

    private void updateHue(ColorProperty cp, double mx, float x, float w) {
        float hue = (float) Math.max(0.0, Math.min(1.0, (mx - x) / w));
        cp.setHue(hue);
        cp.updateValue();
    }

    private void updateSV(ColorProperty cp, double mx, double my, float x, float y, float w, float h) {
        float sat = (float) Math.max(0.0, Math.min(1.0, (mx - x) / w));
        float bri = (float) Math.max(0.0, Math.min(1.0, 1.0 - (my - y) / h));
        cp.setSaturation(sat);
        cp.setBrightness(bri);
        cp.updateValue();
    }

    @Override
    public boolean method_25406(class_11909 click) {
        this.draggingWindow = false;
        this.draggingNumber = null;
        this.draggingHue = false;
        this.draggingSV = false;
        this.dragColor = null;
        return true;
    }

    /* Called from render(): keeps sliders / color pickers / window alive
       while the mouse button is held (Screen only fires click + release). */
    private void pollDrags(int mouseX, int mouseY) {
        if (this.draggingWindow) {
            winX = mouseX - this.dragOffX;
            winY = mouseY - this.dragOffY;
        }
        if (this.draggingNumber != null) {
            this.updateNumber(this.draggingNumber, mouseX);
        }
        if (this.dragColor != null && this.draggingSV) {
            this.updateSV(this.dragColor, mouseX, mouseY, this.dragPickerX, this.dragPickerY, this.dragPickerW, this.dragPickerH);
        }
        if (this.dragColor != null && this.draggingHue) {
            this.updateHue(this.dragColor, mouseX, this.dragPickerX, this.dragPickerW);
        }
    }

    @Override
    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isCtrlDown()) {
            guiScale = Math.max(0.6f, Math.min(1.6f, guiScale - (float) verticalAmount * 0.08f));
            return true;
        }
        float s = this.s();
        float listH = Math.min(winH(), Constants.mc.method_22683().method_4502() - 16.0f) - 34.0f * s - 24.0f * s - 24.0f * s;
        float contentHeight = this.contentHeight(s);
        float maxScroll = Math.max(0.0f, contentHeight - listH);
        scrollTarget = Math.max(0.0f, Math.min(maxScroll, scrollTarget - (float) verticalAmount * 24.0f * s));
        return true;
    }

    @Override
    public boolean method_25404(class_11908 keyInput) {
        int key = keyInput.comp_4795();
        if (this.bindingModule != null) {
            BindRepository repo = OpalClient.getInstance().getBindRepository();
            repo.getBindingService().clearBindings(this.bindingModule);
            if (key != 256) {
                repo.getBindingService().register(key, this.bindingModule, InputType.KEYBOARD);
            }
            this.bindingModule = null;
            return true;
        }
        if (this.searchFocused) {
            if (key == 259) {
                if (!this.search.isEmpty()) {
                    this.search = this.search.substring(0, this.search.length() - 1);
                }
            } else if (key == 257 || key == 256) {
                this.searchFocused = false;
            }
            return true;
        }
        if (key == 256 || key == 344) {
            this.method_25419();
            return true;
        }
        return true;
    }

    @Override
    public boolean method_25400(class_11905 charInput) {
        if (this.searchFocused) {
            char c = (char) charInput.comp_4793();
            if (c >= ' ' && c != 127) {
                this.search += c;
                scrollTarget = 0.0f;
            }
            return true;
        }
        return true;
    }

    @Override
    public void method_25419() {
        if (this.bindingModule != null) {
            return;
        }
        this.bindingModule = null;
        Multithreading.schedule(() -> OpalClient.getInstance().getModuleRepository().getModule(ClickGUIModule.class).setEnabled(false), 100L, TimeUnit.MILLISECONDS);
        if (this.field_22787 != null) {
            this.field_22787.method_1507(null);
        }
    }

    @Override
    public boolean method_25421() {
        return false;
    }

    private boolean hit(double mx, double my, float x, float y, float w, float h) {
        return HoverUtility.isHovering(x, y, w, h, mx, my);
    }

    private static boolean isCtrlDown() {
        long handle = Constants.mc.method_22683().method_4490();
        return org.lwjgl.glfw.GLFW.glfwGetKey(handle, org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL) == 1
                || org.lwjgl.glfw.GLFW.glfwGetKey(handle, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL) == 1;
    }
}
