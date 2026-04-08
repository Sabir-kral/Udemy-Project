package org.apache.maven.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MavenWrapperExecutor {

    public void execute(String[] args) throws IOException {
        // Maven home qovluğunu tap
        File mavenHome = getMavenHome();

        // Maven komandasını icra et
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(buildCommand(mavenHome, args));
        builder.inheritIO();

        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            System.exit(exitCode);
        } catch (InterruptedException e) {
            throw new IOException("Maven execution interrupted", e);
        }
    }

    private File getMavenHome() throws IOException {
        // Burada lazımi logic var, əgər Maven yoxdursa, download edə bilər
        // Sadələşdirilmiş nümunə üçün, sadəcə cari dir qaytarırıq
        return new File(System.getProperty("user.dir"));
    }

    private String[] buildCommand(File mavenHome, String[] args) {
        String mvnExecutable = mavenHome.getAbsolutePath() + "/bin/mvn"; // Linux/Mac
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            mvnExecutable += ".cmd";
        }
        String[] command = new String[args.length + 1];
        command[0] = mvnExecutable;
        System.arraycopy(args, 0, command, 1, args.length);
        return command;
    }
}