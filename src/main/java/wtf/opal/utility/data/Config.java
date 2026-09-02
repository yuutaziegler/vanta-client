/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.data;

import java.util.Date;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.binding.IBindable;

@Environment(value=EnvType.CLIENT)
public final class Config
implements IBindable {
    private final String name;
    private String description;
    private boolean pinned;
    private Date updatedAt;

    public Config(String name) {
        this.name = name;
    }

    public Config(String name, String description, boolean pinned, Date updatedAt) {
        this.name = name;
        this.description = description;
        this.pinned = pinned;
        this.updatedAt = updatedAt;
    }

    @Override
    public void onBindingInteraction() {
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}

