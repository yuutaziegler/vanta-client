/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_2172
 *  net.minecraft.class_2561
 *  net.minecraft.class_2568
 *  net.minecraft.class_2568$class_10613
 *  net.minecraft.class_5250
 */
package wtf.opal.client.command.impl.module;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_5250;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.repository.BindRepository;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.client.command.Command;
import wtf.opal.client.command.arguments.BindArgumentType;
import wtf.opal.client.command.arguments.ConfigArgumentType;
import wtf.opal.client.command.arguments.ModuleArgumentType;
import wtf.opal.client.feature.module.Module;
import wtf.opal.utility.data.Config;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class BindCommand
extends Command {
    private static final BindRepository BIND_REPOSITORY = OpalClient.getInstance().getBindRepository();

    public BindCommand() {
        super("bind", "Sets the keybind of specified action to the specified key.", "b");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(BindCommand.literal("list").executes(context -> {
            class_5250 text = class_2561.method_43470((String)"\u00a7lBinds \u00a7r").method_27692(class_124.field_1054).method_10852((class_2561)class_2561.method_43470((String)("(" + BIND_REPOSITORY.getBindingService().getBindingMap().size() + "): ")).method_27692(class_124.field_1080));
            BIND_REPOSITORY.getBindingService().getBindingMap().asMap().forEach((key, bindables) -> {
                class_5250 keyText = class_2561.method_43470((String)("\u2022 " + BIND_REPOSITORY.getNameFromInteger((Integer)key.first))).method_27692(class_124.field_1080);
                List<String> names = bindables.stream().map(bindable -> {
                    String string;
                    if (bindable instanceof Module) {
                        Module module = (Module)bindable;
                        string = String.valueOf(class_124.field_1080) + "\u2022 Module: " + String.valueOf(class_124.field_1054) + module.getName();
                    } else if (bindable instanceof Config) {
                        Config config = (Config)bindable;
                        string = String.valueOf(class_124.field_1080) + "\u2022 Config: " + String.valueOf(class_124.field_1054) + config.getName();
                    } else {
                        string = null;
                    }
                    return string;
                }).filter(Objects::nonNull).toList();
                if (!names.isEmpty()) {
                    keyText.method_10862(keyText.method_10866().method_10949((class_2568)new class_2568.class_10613((class_2561)class_2561.method_43470((String)String.join((CharSequence)"\n", names)).method_27692(class_124.field_1080))));
                }
                text.method_27693("\n").method_10852((class_2561)keyText);
            });
            ChatUtility.display((class_2561)text);
            return 1;
        }));
        builder.then(BindCommand.literal("module").then(BindCommand.argument("module_name", ModuleArgumentType.create()).then(BindCommand.argument("bind", BindArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module_name", Module.class);
            String bind = (String)context.getArgument("bind", String.class);
            String bindName = bind.toUpperCase();
            if (bindName.equals("CLEAR")) {
                BIND_REPOSITORY.getBindingService().clearBindings(module);
                ChatUtility.print("Binds for \u00a7l" + module.getName() + "\u00a77 have been cleared!");
                return 1;
            }
            Integer bindCode = BIND_REPOSITORY.getNamedBindingMap().get(bindName);
            if (bindCode == null) {
                ChatUtility.error("Invalid bind: \u00a7l" + bindName);
                return 1;
            }
            if (bindCode < 10) {
                BIND_REPOSITORY.getBindingService().register(bindCode, module, InputType.MOUSE);
            } else {
                BIND_REPOSITORY.getBindingService().register(bindCode, module, InputType.KEYBOARD);
            }
            ChatUtility.print("Set \u00a7l" + module.getName() + "\u00a77 bind to \u00a7l" + bindName + "\u00a77!");
            return 1;
        }))));
        builder.then(BindCommand.literal("config").then(BindCommand.argument("config_name", ConfigArgumentType.create()).then(BindCommand.argument("bind", BindArgumentType.create()).executes(context -> {
            String configName = ((String)context.getArgument("config_name", String.class)).toLowerCase();
            String bind = (String)context.getArgument("bind", String.class);
            String bindName = bind.toUpperCase();
            Config tempConfigObj = new Config(configName);
            if (bindName.equals("CLEAR")) {
                List<Config> configsToClear = BIND_REPOSITORY.getBindingService().getBindingMap().values().stream().filter(bindable -> {
                    Config c;
                    return bindable instanceof Config && (c = (Config)bindable).getName().equalsIgnoreCase(configName);
                }).map(Config.class::cast).toList();
                configsToClear.forEach(BIND_REPOSITORY.getBindingService()::clearBindings);
                ChatUtility.print("Binds for " + configName + " have been cleared!");
                return 1;
            }
            Integer bindCode = BIND_REPOSITORY.getNamedBindingMap().get(bindName);
            if (bindCode == null) {
                ChatUtility.error("Invalid bind: \u00a7l" + bindName);
                return 1;
            }
            if (bindCode < 10) {
                BIND_REPOSITORY.getBindingService().register(bindCode, tempConfigObj, InputType.MOUSE);
            } else {
                BIND_REPOSITORY.getBindingService().register(bindCode, tempConfigObj, InputType.KEYBOARD);
            }
            ChatUtility.print("Set " + configName + "'s bind to \u00a7l" + bindName + "\u00a77!");
            return 1;
        }))));
    }
}

