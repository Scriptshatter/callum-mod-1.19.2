package scriptshatter.callum.powers;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface TransparentConsumer extends VertexConsumer {

    @Override
    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Vec3f vec3f = new Vec3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        vec3f.transform(matrixEntry.getNormalMatrix());
        int j = js.length / 8;
        MemoryStack memoryStack = MemoryStack.stackPush();

        try {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for(int k = 0; k < j; ++k) {
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float o;
                float p;
                float q;
                float m;
                float n;
                if (useQuadColorData) {
                    float l = (float)(byteBuffer.get(12) & 255) / 255.0F;
                    m = (float)(byteBuffer.get(13) & 255) / 255.0F;
                    n = (float)(byteBuffer.get(14) & 255) / 255.0F;
                    o = l * fs[k] * red;
                    p = m * fs[k] * green;
                    q = n * fs[k] * blue;
                } else {
                    o = fs[k] * red;
                    p = fs[k] * green;
                    q = fs[k] * blue;
                }

                int r = is[k];
                m = byteBuffer.getFloat(16);
                n = byteBuffer.getFloat(20);
                Vector4f vector4f = new Vector4f(f, g, h, 1.0F);
                vector4f.transform(matrix4f);
                this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 0.5F, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
            }
        } catch (Throwable var33) {
            try {
                memoryStack.close();
            } catch (Throwable var32) {
                var33.addSuppressed(var32);
            }

            throw var33;
        }

        memoryStack.close();

    }

    default VertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
        vector4f.transform(matrix);
        return this.vertex((double)vector4f.getX(), (double)vector4f.getY(), (double)vector4f.getZ());
    }

    default VertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        Vec3f vec3f = new Vec3f(x, y, z);
        vec3f.transform(matrix);
        return this.normal(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }


}
