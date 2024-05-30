package poc.common.flowchart;

import com.google.common.collect.ImmutableList;

import java.io.IOException;

/*
These are all options to try:
------------------------------------
dot -Kneato -v5 -Tpng dotfile.dot -oflowchart-level5.png
dot -Kdot -v5 -Gsize=200,200\! -Goverlap=scale -Tpng -Gnslimit=2 -Gnslimit1=2 -Gmaxiter=2000 -Gsplines=line dotfile.dot -oflowchart-level5.png
dot -Kfdp -v5 -Goverlap=scale -Gsize=200,200\! -Tpng  dotfile.dot -oflowchart-level5.png
dot -Ktwopi -v5 -Gsize=200,200\! -Tpng  dotfile.dot -oflowchart-level5.png

This prints out all levels
----------------------------
dot -Kdot -v5 -Gsize=200,200\! -Goverlap=scale -Tpng -Gnslimit=4 -Gnslimit1=4 -Gmaxiter=2000 -Gsplines=line dotfile.dot -oflowchart-level5.png

 */

public class GraphGenerator {
    public void generateGraph(String dotFilePath, String graphOutputPath) throws IOException, InterruptedException {
        ImmutableList<String> graphGenerationCommand = ImmutableList.of("dot", "-Kdot", "-v5", "-Gsize=800,800\\!", "-Goverlap=scale", "-Tpng", "-Gnslimit=7", "-Gnslimit1=7", "-Gmaxiter=5000", "-Gsplines=ortho", dotFilePath, String.format("-o%s", graphOutputPath));
//        String graphGenerationCommand = String.format("dot -Kdot -v5 -Gsize=800,800\\! -Goverlap=scale -Tpng -Gnslimit=7 -Gnslimit1=7 -Gmaxiter=5000 -Gsplines=line %s -o%s", dotFilePath, graphOutputPath);
        Process p = new ProcessBuilder(graphGenerationCommand).inheritIO().start();
        int rc = p.waitFor();
//        Process pr = rt.exec(graphGenerationCommand);
        System.out.println("Completed graph generation");
    }
}
