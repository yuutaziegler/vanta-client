/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.system.MemoryUtil
 */
package wtf.opal.utility.misc.system;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

@Environment(value=EnvType.CLIENT)
public final class IOUtility {
    private IOUtility() {
    }

    public static ByteBuffer ioResourceToByteBuffer(InputStream inputStream, int bufferSize) {
        try {
            ByteBuffer buffer;
            block11: {
                if (inputStream != null) {
                    try (ReadableByteChannel rbc = Channels.newChannel(inputStream);){
                        buffer = BufferUtils.createByteBuffer((int)bufferSize);
                        while (true) {
                            int bytes;
                            if ((bytes = rbc.read(buffer)) == -1) {
                                break block11;
                            }
                            if (buffer.remaining() != 0) continue;
                            buffer = IOUtility.resizeBuffer(buffer, buffer.capacity() * 3 / 2);
                        }
                    }
                }
                throw new IllegalArgumentException("InputStream cannot be null");
            }
            buffer.flip();
            return MemoryUtil.memSlice((ByteBuffer)buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer((int)newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}

