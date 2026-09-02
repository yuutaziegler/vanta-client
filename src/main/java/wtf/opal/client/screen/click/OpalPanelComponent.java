/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.screen.click;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.screen.click.IOpalComponent;
import wtf.opal.utility.render.ScreenPosition;

@Environment(value=EnvType.CLIENT)
public abstract class OpalPanelComponent
extends ScreenPosition
implements IOpalComponent {
}

