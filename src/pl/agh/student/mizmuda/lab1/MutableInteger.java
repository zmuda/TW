package pl.agh.student.mizmuda.lab1;

public class MutableInteger {
    public volatile int value;
    public void incrementModulo(int divisor){
        value++;
        value%=divisor;
    }
}
