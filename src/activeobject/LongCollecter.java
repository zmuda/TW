package activeobject;

import java.util.LinkedList;

public class LongCollecter {
    private LinkedList<Long> results = new LinkedList<Long>();

    public void submit(Long result) {
        synchronized (results) {
            results.add(result);
        }
    }

    public double getMean() {
        synchronized (results) {
            double mean = 0;
            for (long l : results) {
                mean += l;
            }
            mean /= results.size();
            return mean;
        }
    }
}
