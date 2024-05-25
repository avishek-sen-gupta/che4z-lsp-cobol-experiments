package org.flowchart;

public interface FlowchartBuilder {
    ChartNode run(String dotFilePath, int maxLevel);
}
