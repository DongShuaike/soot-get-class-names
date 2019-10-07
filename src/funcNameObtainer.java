import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class funcNameObtainer {
    /*
    * Unfortunately, all json-related libraries needed by us are regarded as phantom classes.
    * */

    private String resultPath = "/home/dsk/Tools/Lab/iot-hidden-service-mobile/results/funcNames";
    private String jarsPath = "/home/dsk/Android/Sdk/platforms";
    private String jarv28Path = "/home/dsk/Android/Sdk/platforms/android-28/android.jar";
    private String jarv21Path = "/home/dsk/Android/Sdk/platforms/android-21/android.jar";
    private String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
    private String rtPath = "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar";

    private List<SootClass> getClassList(){
        List<SootClass> lst = new ArrayList<>();
        //Options.v().set_exclude(excludePackageList); //no effect
        //for(SootClass clazz: Scene.v().getApplicationClasses()){
        for(SootClass clazz: Scene.v().getClasses()) {
            //for (Iterator<SootClass> scIt = Scene.v().getApplicationClasses().snapshotIterator(); scIt.hasNext();){
            //SootClass sc = scIt.next();
            String className = clazz.getName();
            /*if (!(Pattern.matches("java\\..*", className) ||
                    Pattern.matches("android\\..*", className) ||
                    Pattern.matches("sun\\..*", className) ||
                    Pattern.matches("javax\\..*", className) ||
                    Pattern.matches("com\\.google\\..*", className) ||
                    Pattern.matches("jdk\\..*", className) ||
                    Pattern.matches("org\\.apache\\..*", className)
            ))*/
            if ((Pattern.matches(".*minidev\\.json.*", className) ||
                    Pattern.matches(".*dropbox\\.core\\.json.*", className) ||
                    Pattern.matches(".*org\\.json.*", className) ||
                    Pattern.matches(".*amazonaws\\.util\\.json.*", className) ||
                    Pattern.matches(".*codehaus\\.jackson.*", className) ||
                    Pattern.matches(".*fasterxml\\.jackson.*", className) ||
                    Pattern.matches(".*alibaba\\.fastjson.*", className) ||
                    Pattern.matches(".*com\\.tplinkra\\.common\\.json\\.JsonUtils.*", className))) {
                System.out.println("\t"+clazz.getName());
                lst.add(clazz);
            }
        }
        return lst;
    }
    private List<String> getFuncNamefromClass(SootClass sc){
        List<SootMethod> lst = sc.getMethods();
        System.out.println(sc.isPhantom());
        //System.out.println(sc.getMethodCount());
        System.out.println(lst);
        List<String> funcNames = new ArrayList<>();
        for(SootMethod sm : lst){
            String full = "<"+sc.getName()+": "+sm.getSubSignature()+">";
            System.out.println(full);
            funcNames.add(full);
        }
        return funcNames;
    }
    public static void main(String[] args) throws IOException {
        System.out.println(Scene.v().getSootClassPath());
        String folderPath = "/home/dsk/Documents/Experiments/iot_apps";
        System.out.println(args);
        //String f = args[0];
        //String f = "com.sengled.stspeaker.apk";
        String f = "com.sentri.sentriapp.apk";
        String fPath = folderPath+"/"+f;
        funcNameObtainer g = new funcNameObtainer();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_force_android_jar(g.jarv21Path);
        //Options.v().set_soot_classpath(g.jarv21Path);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(fPath));

        try {
            Scene.v().loadNecessaryClasses();
            List<SootClass> lst = g.getClassList();
            for(SootClass sc : lst){
                g.getFuncNamefromClass(sc);
            }
            /*String resPath = g.resultPath+"/"+f+".txt";
            FileWriter fileWriter = new FileWriter(resPath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(String e: lst){
                printWriter.print(e+System.lineSeparator());
            }*/
        } catch(RuntimeException e){
            System.out.println("RuntimeException" + e);
            System.out.println("\t"+fPath);
        }
    }
}
