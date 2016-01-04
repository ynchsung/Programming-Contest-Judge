package Judge;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.ProcessBuilder;

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
        file = new File("sandbox_meta");
        file.delete();
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
        catch (Exception e) {
            e.printStackTrace();
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
                e.printStackTrace();
                return false;
            }
            return (p.exitValue() == 0);
        }
        catch (Exception e) {
            e.printStackTrace();
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
                e.printStackTrace();
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String parseSandboxMetaFile() {
        Map<String, String> info = new HashMap<String, String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("sandbox_meta"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                if (tokens.length != 2)
                    continue;
                info.put(tokens[0], tokens[1]);
            }
            br.close();
        }
        catch (Exception e) {
            return "SE";
        }
        if (Integer.valueOf(info.getOrDefault("exitsig", "0")) != 0)
            return "RE";
        else if (info.getOrDefault("status", "").equals("TO"))
            return "TLE";
        else
            return "";
    }

    private boolean judgeAnswer(String pathName1, String pathName2, String judgeMethodPath) {
        ProcessBuilder pb;
        if (judgeMethodPath.equals(""))
            pb = new ProcessBuilder("diff", "-b", pathName1, pathName2);
        else
            pb = new ProcessBuilder(String.format("./%s", judgeMethodPath), pathName1, pathName2);

        try {
            Process p = pb.start();
            try {
                p.waitFor();
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return (p.exitValue() == 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String judge(String sourceCode, int timeLimit, int memoryLimit, String inputPathName, String outputPathName, String judgeMethodPath) {
        String result;

        resetFile();
        if (!writeCode(sourceCode))
            result = "SE";
        else if (!compileCode())
            result = "CE";
        else if (!runCode(inputPathName, timeLimit, memoryLimit))
            result = "SE";
        else {
            String tmp = parseSandboxMetaFile();
            if (!tmp.equals(""))
                return tmp;
            else if (!judgeAnswer(String.format("%s/participant.out", this.sandboxPath), outputPathName, judgeMethodPath))
                result = "WA";
            else
                result = "AC";
        }
        resetFile();
        return result;
    }
}
