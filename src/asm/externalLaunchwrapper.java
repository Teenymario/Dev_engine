package asm;

import asm.transformers.RendererInjector;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

class Singleton {
    private static Singleton single_instance = new Singleton();
    public String x = "e";

    private Singleton() {}

    public static Singleton getInstance() {
        return single_instance;
    }
}

public class externalLaunchwrapper {

    public static void main(String[] args) throws Exception {
        System.out.println("Running game from External LaunchWrapper\n");

        System.out.println("Testing singleton class");

        //Manipulate bytecode of MasterRenderer class
        Instrumentation inst = agent.getInstrument();
        inst.redefineClasses(new ClassDefinition(Class.forName("engine.graphics.MasterRenderer"), RendererInjector.modifyMasterRenderer()));

        //Initialize the target program
        main.main.main(args);
    }

}