package asm;

import asm.transformers.RendererInjector;
import main.DevEngine;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class externalLaunchwrapper {

    public static void main(String[] args) throws Exception {
        System.out.println("Running game from External LaunchWrapper\n");

        //Manipulate bytecode of MasterRenderer class
        Instrumentation inst = agent.getInstrument();
        inst.redefineClasses(new ClassDefinition(Class.forName("engine.graphics.MasterRenderer"), RendererInjector.modifyMasterRenderer()));

        //Initialize the target program
        DevEngine.main(args);
    }

}