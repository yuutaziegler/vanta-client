/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.graalvm.polyglot.Context
 *  org.graalvm.polyglot.Value
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.scripting;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.Nullable;
import wtf.opal.scripting.impl.ModuleScript;

@Environment(value=EnvType.CLIENT)
public class Script {
    private final String name;
    private final String version;
    private final List<String> authors;
    private ModuleScript module;
    private final Context context;

    public Script(String name, String version, List<String> authors, Context context) {
        this.name = name;
        this.version = version;
        this.authors = authors;
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    public void registerModule(Value config, Value callback) {
        String name = config.getMember("name").asString();
        String description = config.getMember("description").asString();
        this.module = new ModuleScript(name, description);
        callback.execute(new Object[]{this.module});
    }

    @Nullable
    public ModuleScript getModule() {
        return this.module;
    }
}

