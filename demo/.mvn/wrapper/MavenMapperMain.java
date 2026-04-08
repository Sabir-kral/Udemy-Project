package org.apache.maven.wrapper;

import java.io.File;
import java.io.IOException;

public class MavenWrapperMain {
    public static void main(String[] args) throws IOException {
        MavenWrapperExecutor executor = new MavenWrapperExecutor();
        executor.execute(args);
    }
}