package vm;

public class PassThrough implements CobolVmInstruction {
    @Override
    public CobolVmInstruction execute(NextExecution e) {
        return e.apply();
    }

    @Override
    public boolean apply(OldFlowControl flow) {
        return true;
    }
}
