/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  org.graalvm.polyglot.Context
 *  org.graalvm.polyglot.HostAccess
 *  org.graalvm.polyglot.Source
 *  org.graalvm.polyglot.Value
 *  org.graalvm.polyglot.io.IOAccess
 */
package wtf.opal.scripting.repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;
import wtf.opal.client.Constants;
import wtf.opal.scripting.Script;
import wtf.opal.scripting.impl.ModuleScript;
import wtf.opal.scripting.impl.proxy.ClientProxy;
import wtf.opal.scripting.impl.proxy.MovementProxy;
import wtf.opal.scripting.impl.proxy.RenderProxy;
import wtf.opal.scripting.impl.proxy.RotationProxy;

@Environment(value=EnvType.CLIENT)
public final class ScriptRepository {
    private final List<Script> scriptList = new ArrayList<Script>();

    public ScriptRepository() {
        this.loadScripts();
    }

    public int loadScripts() {
        this.scriptList.forEach(script -> {
            ModuleScript module = script.getModule();
            if (module != null) {
                module.setEnabled(false);
            }
            script.getContext().close();
        });
        this.scriptList.clear();
        File scriptsDir = new File(Constants.DIRECTORY, "scripts");
        File[] jsFiles = scriptsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".js"));
        if (jsFiles == null) {
            return 0;
        }
        for (File scriptFile : jsFiles) {
            Context ctx = Context.newBuilder((String[])new String[]{"js"}).allowHostAccess(HostAccess.ALL).allowHostClassLookup(name -> true).allowIO(IOAccess.ALL).allowCreateProcess(false).allowCreateThread(true).allowNativeAccess(false).build();
            try (FileReader reader = new FileReader(scriptFile);){
                ctx.getBindings("js").putMember("registerScript", args -> {
                    Value data = args[0];
                    String name = data.getMember("name").asString();
                    String ver = data.getMember("version").asString();
                    ArrayList<String> authors = new ArrayList<String>();
                    Value arr = data.getMember("authors");
                    int i = 0;
                    while ((long)i < arr.getArraySize()) {
                        authors.add(arr.getArrayElement((long)i).asString());
                        ++i;
                    }
                    return new Script(name, ver, authors, ctx);
                });
                ctx.getBindings("js").putMember("client", (Object)new ClientProxy());
                ctx.getBindings("js").putMember("renderer", (Object)new RenderProxy());
                ctx.getBindings("js").putMember("movement", (Object)new MovementProxy());
                ctx.getBindings("js").putMember("rotation", (Object)new RotationProxy());
                ctx.getBindings("js").putMember("Vec3d", class_243.class);
                ctx.getBindings("js").putMember("Vec3i", class_2382.class);
                ctx.getBindings("js").putMember("BlockPos", class_2338.class);
                ctx.getBindings("js").putMember("MathHelper", class_3532.class);
                ctx.getBindings("js").putMember("Hand", class_1268.class);
                ctx.getBindings("js").putMember("mc", (Object)Constants.mc);
                Source source = Source.newBuilder((String)"js", (Reader)reader, (String)scriptFile.getName()).build();
                ctx.eval(source);
                Value scriptValue = ctx.getBindings("js").getMember("script");
                if (scriptValue == null || scriptValue.isNull()) {
                    throw new IllegalStateException("Global 'script' was not defined in " + scriptFile.getName());
                }
                Script script2 = (Script)scriptValue.asHostObject();
                this.scriptList.add(script2);
            }
            catch (IOException e) {
                e.printStackTrace();
                ctx.close();
            }
        }
        return jsFiles.length;
    }

    public List<Script> getScriptList() {
        return this.scriptList;
    }
}

