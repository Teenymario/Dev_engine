package asm.transformers;

import org.objectweb.asm.*;
import java.io.IOException;

public class RendererInjector {
    public static byte[] modifyMasterRenderer() throws IOException {
        // Load the bytecode of the MasterRenderer class
        ClassReader classReader = new ClassReader("engine.graphics.MasterRenderer");
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);

        // Create a ClassVisitor to modify the bytecode
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("render".equals(name) && "(Lengine/objects/Light;)V".equals(descriptor)) {
                    // Insert println statement at the beginning of the render method
                    return new MethodVisitor(Opcodes.ASM7, methodVisitor) {
                        @Override
                        public void visitCode() {
                            mv.visitCode();
                            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitLdcInsn("Running update test: ");
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                            super.visitCode();
                        }
                    };
                }
                return methodVisitor;
            }
        };

        // Perform bytecode transformation
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        return classWriter.toByteArray();
    }
}
