package pl.agh.student.mizmuda.lab0;

public class Incrementer implements Runnable{
    private Value value;
    private final int timesToIncrement;

    public Incrementer(Value value, int timesToIncrement) {
        this.value = value;
        this.timesToIncrement = timesToIncrement;
    }

    @Override
    public void run() {
        for(int i=0;i<timesToIncrement;i++){
            value.increment();
        }
    }
}
