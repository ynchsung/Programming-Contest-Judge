package Judge;

import java.util.List;
import java.util.ArrayList;
import java.lang.ProcessBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JudgeUnit {
    private final String sourceCodeFileType;
    private final String sandboxPath;
    private final List<String> compileArg;

    public JudgeUnit(String sourceCodeFileType, String sandboxPath, List<String> compileArg) {
        this.sourceCodeFileType = sourceCodeFileType;
        this.sandboxPath = sandboxPath;
        this.compileArg = compileArg;
    }

    private void resetFile() {
        File file;
        file = new File(String.format("%s/exec.%s", this.sandboxPath, this.sourceCodeFileType));
        file.delete();
        file = new File(String.format("%s/exec", this.sandboxPath));
        file.delete();
        file = new File(String.format("%s/participant.out", this.sandboxPath));
        file.delete();
    }

    private boolean writeCode(String sourceCode) {
        try {
            File file = new File(String.format("%s/exec.%s", this.sandboxPath, this.sourceCodeFileType));
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sourceCode);
            fileWriter.close();
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean compileCode() {
        List<String> prepare = new ArrayList<String>(this.compileArg);
        prepare.add(String.format("%s/exec.%s", this.sandboxPath, this.sourceCodeFileType));
        prepare.add(String.format("-o"));
        prepare.add(String.format("%s/exec", this.sandboxPath));

        ProcessBuilder pb = new ProcessBuilder(prepare);
        try {
            Process p = pb.start();
            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                return false;
            }
            return (p.exitValue() == 0);
        }
        catch (IOException e) {
            return false;
        }
    }

    private boolean runCode(String inputPathName, int timeLimit, int memoryLimit) {
        ProcessBuilder pb = new ProcessBuilder("./isolate", "--run", "--meta=sandbox_meta", String.format("--stdin=%s", inputPathName), "--stdout=./participant.out", String.format("--time=%d", timeLimit), String.format("--mem=%d", memoryLimit), "--", "./exec");
        try {
            Process p = pb.start();
            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                return false;
            }
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean judgeAnswer(String pathName1, String pathName2, String judgeMethodPath) {
        ProcessBuilder pb = new ProcessBuilder("diff", "-b", pathName1, pathName2);
        if (judgeMethodPath.equals(""))
            pb = new ProcessBuilder("diff", "-b", pathName1, pathName2);
        else
            pb = new ProcessBuilder(String.format("./%s", judgeMethodPath), pathName1, pathName2);

        try {
            Process p = pb.start();
            try {
                p.waitFor();
            }
            catch (InterruptedException e) {
                return false;
            }
            return (p.exitValue() == 0);
        }
        catch (IOException e) {
            return false;
        }
    }

    public String judge(String sourceCode, int timeLimit, int memoryLimit, String inputPathName, String outputPathName, String judgeMethodPath) {
        String result = "";

        resetFile();
        if (!writeCode(sourceCode))
            result = "SE";
        else if (!compileCode())
            result = "CE";
        else if (!runCode(inputPathName, timeLimit, memoryLimit))
            result = "SE";
        else {
            // TODO: parse sandbox meta file
            if (false /* TLE, RE, MLE */) ;
            else if (!judgeAnswer(String.format("%s/participant.out", this.sandboxPath), outputPathName, judgeMethodPath))
                result = "WA";
            else
                result = "AC";
        }
        resetFile();

        return result;
    }

    // test
    /*
    public static void main(String[] argv) {
        List<String> cpp = new ArrayList<String>();
        cpp.add("g++");
        cpp.add("-O2");
        cpp.add("-std=c++11");
        cpp.add("--static");

        JudgeUnit jj = new JudgeUnit("cpp", "./box/0/box", cpp);
        System.out.println(jj.judge("#include <cstdio>\n#include <cstdlib>\n\nusing namespace std;\n\nint main() {\n    int cases;\n    scanf(\"%d\", &cases);\n    while (cases--) {\n        int a, b;\n        scanf(\"%d%d\", &a, &b);\n        printf(\"%d\\n\", a + b);\n    }\n    return 0;\n}", 1, 65536, "test/pa.in", "answer/pa.out", ""));
    }
    */
}
