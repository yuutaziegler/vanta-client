/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.opengl.GL
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL33
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

@Environment(value=EnvType.CLIENT)
public final class GLUtility {
    private static final int[] lastActiveTexture = new int[1];
    private static final int[] lastProgram = new int[1];
    private static final int[] lastTexture = new int[1];
    private static final int[] lastSampler = new int[1];
    private static final int[] lastArrayBuffer = new int[1];
    private static final int[] lastVertexArrayObject = new int[1];
    private static final int[] lastPolygonMode = new int[2];
    private static final int[] lastViewport = new int[4];
    private static final int[] lastScissorBox = new int[4];
    private static final int[] lastBlendSrcRgb = new int[1];
    private static final int[] lastBlendDstRgb = new int[1];
    private static final int[] lastBlendSrcAlpha = new int[1];
    private static final int[] lastBlendDstAlpha = new int[1];
    private static final int[] lastBlendEquationRgb = new int[1];
    private static final int[] lastBlendEquationAlpha = new int[1];
    private static boolean lastEnableBlend;
    private static boolean lastEnableCullFace;
    private static boolean lastEnableDepthTest;
    private static boolean lastEnableStencilTest;
    private static boolean lastEnableScissorTest;
    private static boolean lastEnablePrimitiveRestart;
    private static boolean lastDepthMask;
    private static int glVersion;

    private GLUtility() {
    }

    public static void setup() {
        int[] major = new int[1];
        int[] minor = new int[1];
        GL30.glGetIntegerv((int)33307, (int[])major);
        GL30.glGetIntegerv((int)33308, (int[])minor);
        glVersion = major[0] * 100 + minor[0] * 10;
    }

    public static void push() {
        if (glVersion == -1) {
            throw new IllegalStateException("GlStateUtility.setup(glVersion) must be called before push/pop!");
        }
        GL30.glGetIntegerv((int)34016, (int[])lastActiveTexture);
        GL30.glActiveTexture((int)33984);
        GL30.glGetIntegerv((int)35725, (int[])lastProgram);
        GL30.glGetIntegerv((int)32873, (int[])lastTexture);
        if (glVersion >= 330 || GL.getCapabilities().GL_ARB_sampler_objects) {
            GL30.glGetIntegerv((int)35097, (int[])lastSampler);
        }
        GL30.glGetIntegerv((int)34964, (int[])lastArrayBuffer);
        GL30.glGetIntegerv((int)34229, (int[])lastVertexArrayObject);
        if (glVersion >= 200) {
            GL30.glGetIntegerv((int)2880, (int[])lastPolygonMode);
        }
        GL30.glGetIntegerv((int)2978, (int[])lastViewport);
        GL30.glGetIntegerv((int)3088, (int[])lastScissorBox);
        GL30.glGetIntegerv((int)32969, (int[])lastBlendSrcRgb);
        GL30.glGetIntegerv((int)32968, (int[])lastBlendDstRgb);
        GL30.glGetIntegerv((int)32971, (int[])lastBlendSrcAlpha);
        GL30.glGetIntegerv((int)32970, (int[])lastBlendDstAlpha);
        GL30.glGetIntegerv((int)32777, (int[])lastBlendEquationRgb);
        GL30.glGetIntegerv((int)34877, (int[])lastBlendEquationAlpha);
        lastEnableBlend = GL30.glIsEnabled((int)3042);
        lastEnableCullFace = GL30.glIsEnabled((int)2884);
        lastEnableDepthTest = GL30.glIsEnabled((int)2929);
        lastEnableStencilTest = GL30.glIsEnabled((int)2960);
        lastEnableScissorTest = GL30.glIsEnabled((int)3089);
        if (glVersion >= 310) {
            lastEnablePrimitiveRestart = GL30.glIsEnabled((int)36765);
        }
        lastDepthMask = GL30.glGetBoolean((int)2930);
    }

    public static void pop() {
        if (glVersion == -1) {
            throw new IllegalStateException("GlStateUtility.setup(glVersion) must be called before push/pop!");
        }
        GL30.glUseProgram((int)lastProgram[0]);
        GL30.glBindTexture((int)3553, (int)lastTexture[0]);
        if (glVersion >= 330 || GL.getCapabilities().GL_ARB_sampler_objects) {
            GL33.glBindSampler((int)0, (int)lastSampler[0]);
        }
        GL30.glActiveTexture((int)lastActiveTexture[0]);
        GL30.glBindVertexArray((int)lastVertexArrayObject[0]);
        GL30.glBindBuffer((int)34962, (int)lastArrayBuffer[0]);
        GL30.glBlendEquationSeparate((int)lastBlendEquationRgb[0], (int)lastBlendEquationAlpha[0]);
        GL30.glBlendFuncSeparate((int)lastBlendSrcRgb[0], (int)lastBlendDstRgb[0], (int)lastBlendSrcAlpha[0], (int)lastBlendDstAlpha[0]);
        GLUtility.setGlState(3042, lastEnableBlend);
        GLUtility.setGlState(2884, lastEnableCullFace);
        GLUtility.setGlState(2929, lastEnableDepthTest);
        GLUtility.setGlState(2960, lastEnableStencilTest);
        GLUtility.setGlState(3089, lastEnableScissorTest);
        if (glVersion >= 310) {
            GLUtility.setGlState(36765, lastEnablePrimitiveRestart);
        }
        if (glVersion >= 200) {
            GL30.glPolygonMode((int)1032, (int)lastPolygonMode[0]);
        }
        GL30.glViewport((int)lastViewport[0], (int)lastViewport[1], (int)lastViewport[2], (int)lastViewport[3]);
        GL30.glScissor((int)lastScissorBox[0], (int)lastScissorBox[1], (int)lastScissorBox[2], (int)lastScissorBox[3]);
        GL30.glDepthMask((boolean)lastDepthMask);
    }

    private static void setGlState(int capability, boolean enabled) {
        if (enabled) {
            GL30.glEnable((int)capability);
        } else {
            GL30.glDisable((int)capability);
        }
    }

    static {
        glVersion = -1;
    }
}

