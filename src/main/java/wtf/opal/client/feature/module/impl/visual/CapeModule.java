/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class CapeModule
extends Module {
    private final ModeProperty<CapeType> type = new ModeProperty<CapeType>("Type", CapeType.TERENTX);

    public CapeModule() {
        super("Cape", "Gives you a cape of your choosing.", ModuleCategory.VISUAL);
        this.addProperties(this.type);
    }

    public CapeType getType() {
        return (CapeType)((Object)this.type.getValue());
    }

    @Override
    public String getSuffix() {
        return this.getType().toString();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum CapeType {
        TERENTX("TerentX"),
        COBALT("Cobalt"),
        MIGRATOR("Migrator"),
        MINECON_2011("Minecon 2011"),
        MINECON_2012("Minecon 2012"),
        MINECON_2013("Minecon 2013"),
        MINECON_2015("Minecon 2015"),
        MINECON_2016("Minecon 2016"),
        MOJANG_STUDIOS("Mojang Studios"),
        MOJANG("Mojang");

        private final String name;
        private final String slug;
        private final class_2960 identifier;

        private CapeType(String name) {
            this.name = name;
            this.slug = name.replace(' ', '-').toLowerCase();
            this.identifier = class_2960.method_60655((String)"terentx", (String)("capes/" + this.slug + ".png"));
        }

        public String getSlug() {
            return this.slug;
        }

        public class_2960 getIdentifier() {
            return this.identifier;
        }

        public String toString() {
            return this.name;
        }

        public static CapeType fromSlug(String slug) {
            for (CapeType type : CapeType.values()) {
                if (!type.slug.equals(slug)) continue;
                return type;
            }
            return null;
        }
    }
}

