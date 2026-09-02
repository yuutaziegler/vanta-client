/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.internal.LinkedTreeMap
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.ibm.icu.impl.Pair;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.BindingService;
import wtf.opal.client.binding.IBindable;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.UnknownModuleException;
import wtf.opal.client.feature.module.impl.visual.ClickGUIModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.utility.data.Config;
import wtf.opal.utility.data.serializer.PairSerializer;

@Environment(value=EnvType.CLIENT)
public final class SaveUtility {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Pair.class, (Object)new PairSerializer()).excludeFieldsWithoutExposeAnnotation().create();
    private static final BindingService BINDING_SERVICE = OpalClient.getInstance().getBindRepository().getBindingService();

    private SaveUtility() {
    }

    public static void saveBindings() {
        try {
            if (!Constants.DIRECTORY.exists()) {
                Constants.DIRECTORY.mkdir();
            }
            File file = new File(Constants.DIRECTORY, "bindings.json");
            JsonArray bindingsArray = new JsonArray();
            for (Pair binding : BINDING_SERVICE.getBindingMap().keySet()) {
                JsonObject bindingJson = new JsonObject();
                bindingJson.addProperty("keyCode", (Number)binding.first);
                JsonArray bindablesArray = new JsonArray();
                for (IBindable bindable : BINDING_SERVICE.getBindingMap().get((Object)binding)) {
                    if (bindable instanceof Module) {
                        Module module = (Module)bindable;
                        JsonObject moduleJson = new JsonObject();
                        moduleJson.addProperty("module", module.getId());
                        bindablesArray.add((JsonElement)moduleJson);
                        continue;
                    }
                    if (!(bindable instanceof Config)) continue;
                    Config config = (Config)bindable;
                    JsonObject configJson = new JsonObject();
                    configJson.addProperty("config", config.getName());
                    bindablesArray.add((JsonElement)configJson);
                }
                bindingJson.add("bindables", (JsonElement)bindablesArray);
                bindingsArray.add((JsonElement)bindingJson);
            }
            Files.writeString(file.toPath(), (CharSequence)GSON.toJson((JsonElement)bindingsArray), new OpenOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBindings() {
        try (FileReader reader = new FileReader(new File(Constants.DIRECTORY, "bindings.json"));){
            JsonArray bindingsArray = JsonParser.parseReader((Reader)reader).getAsJsonArray();
            for (JsonElement bindingElement : bindingsArray) {
                JsonObject bindingJson = bindingElement.getAsJsonObject();
                int keyCode = bindingJson.get("keyCode").getAsInt();
                InputType inputType = keyCode < 10 ? InputType.MOUSE : InputType.KEYBOARD;
                JsonArray bindablesArray = bindingJson.getAsJsonArray("bindables");
                for (JsonElement bindableElement : bindablesArray) {
                    JsonObject bindableJson = bindableElement.getAsJsonObject();
                    if (bindableJson.has("module")) {
                        String moduleID = bindableJson.get("module").getAsString();
                        Module module = OpalClient.getInstance().getModuleRepository().getModule(moduleID);
                        BINDING_SERVICE.register(keyCode, module, inputType);
                        continue;
                    }
                    if (!bindableJson.has("config")) continue;
                    String configName = bindableJson.get("config").getAsString();
                    Config config = new Config(configName);
                    BINDING_SERVICE.register(keyCode, config, inputType);
                }
            }
        }
        catch (IOException | UnknownModuleException e) {
            e.printStackTrace();
        }
        try {
            ClickGUIModule clickGui = OpalClient.getInstance().getModuleRepository().getModule(ClickGUIModule.class);
            if (clickGui != null && BINDING_SERVICE.getKeyFromBindable(clickGui).isEmpty()) {
                BINDING_SERVICE.register(344, clickGui, InputType.KEYBOARD);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void saveConfig(String name) {
    }

    public static boolean loadConfig(String jsonString) {
        try {
            List jsonModules = (List)GSON.fromJson(jsonString, List.class);
            for (Object jsonModuleObj : jsonModules) {
                LinkedTreeMap jsonModule = (LinkedTreeMap)jsonModuleObj;
                String jsonModuleID = (String)jsonModule.get((Object)"name");
                Boolean jsonEnabled = (Boolean)jsonModule.get((Object)"enabled");
                Boolean jsonVisible = (Boolean)jsonModule.get((Object)"visible");
                List jsonProperties = (List)jsonModule.get((Object)"properties");
                for (Module clientModule : OpalClient.getInstance().getModuleRepository().getModules()) {
                    if (!jsonModuleID.equals(clientModule.getId())) continue;
                    if (jsonEnabled != null && jsonEnabled.booleanValue() != clientModule.isEnabled()) {
                        clientModule.setEnabled(jsonEnabled);
                    }
                    if (jsonVisible != null && jsonVisible.booleanValue() != clientModule.isVisible()) {
                        clientModule.setVisible(jsonVisible);
                    }
                    for (Object jsonPropertyObj : jsonProperties) {
                        LinkedTreeMap jsonProperty = (LinkedTreeMap)jsonPropertyObj;
                        String propertyName = (String)jsonProperty.get((Object)"name");
                        Object propertyValue = jsonProperty.get((Object)"value");
                        for (Property<?> clientProperty : clientModule.getPropertyList()) {
                            if (!propertyName.equals(clientProperty.getId())) continue;
                            clientProperty.applyValue(propertyValue);
                        }
                    }
                }
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

