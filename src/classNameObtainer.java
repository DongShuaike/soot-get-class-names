import soot.*;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.*;

import static soot.SootClass.SIGNATURES;

public class classNameObtainer {


    private String resultPath = "/home/dsk/Tools/Lab/iot-hidden-service-mobile/results";
    private String jarsPath = "/home/dsk/Android/Sdk/platforms";
    private String jarv28Path = "/home/dsk/Android/Sdk/platforms/android-28/android.jar";
    private String jarv21Path = "/home/dsk/Android/Sdk/platforms/android-21/android.jar";
    private String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
    private String rtPath = "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar";

    private List<String> getClassNames(){
        List<String> lst = new ArrayList<>();
        //Options.v().set_exclude(excludePackageList); //no effect
        //for(SootClass clazz: Scene.v().getApplicationClasses()){
        for(SootClass clazz: Scene.v().getClasses()){
        //for (Iterator<SootClass> scIt = Scene.v().getApplicationClasses().snapshotIterator(); scIt.hasNext();){
            //SootClass sc = scIt.next();
            String className = clazz.getName();
            if (!(Pattern.matches("java\\..*", className) ||
                    Pattern.matches("android\\..*", className) ||
                    Pattern.matches("sun\\..*", className) ||
                    Pattern.matches("javax\\..*", className) ||
                    Pattern.matches("com\\.google\\..*", className) ||
                    Pattern.matches("jdk\\..*", className) ||
                    Pattern.matches("org\\.apache\\..*", className)
            )){
                if(Pattern.matches("com\\.sonos\\..*", className)){
                    System.out.println("Found !!! "+className);
                }
                //System.out.println("\t"+clazz.getName());
                lst.add(clazz.getName());
            }
        }
        return lst;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Scene.v().getSootClassPath());
        String folderPath = "/home/dsk/Documents/Experiments/iot_apps";
        System.out.println(args);
        String f = args[0];
        String fPath = folderPath+"/"+f;
        classNameObtainer g = new classNameObtainer();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_force_android_jar(g.jarv21Path);
        Options.v().set_soot_classpath(g.jarv21Path);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(fPath));

        try {
            Scene.v().loadNecessaryClasses();
            List<String> lst = g.getClassNames();
            String resPath = g.resultPath+"/"+f+".txt";
            FileWriter fileWriter = new FileWriter(resPath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(String e: lst){
                printWriter.print(e+System.lineSeparator());
            }
        } catch(RuntimeException e){
            System.out.println("RuntimeException" + e);
            System.out.println("\t"+fPath);
        }
    }
}
