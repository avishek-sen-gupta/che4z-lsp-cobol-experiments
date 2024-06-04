package poc.common.flowchart;

public class CobolInterpreter {
    public CobolInterpreter scope(ChartNode scope) {
        return this;
    }

    public void execute(ChartNode node) {
        System.out.println("Executing " + node.getClass().getSimpleName() + node.label());
    }

    public void enter(ChartNode node) {
        System.out.println("Entering " + node.getClass().getSimpleName() + node.label());
    }

    public void exit(ChartNode node) {
        System.out.println("Exiting " + node.getClass().getSimpleName() + node.label());
    }
}
