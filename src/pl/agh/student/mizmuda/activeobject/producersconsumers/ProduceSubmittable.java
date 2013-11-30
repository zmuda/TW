package pl.agh.student.mizmuda.activeobject.producersconsumers;


import org.apache.log4j.Logger;
import pl.agh.student.mizmuda.activeobject.core.ISubmittable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.Random;

public class ProduceSubmittable implements ISubmittable<Integer> {
    private Logger logger = Logger.getLogger("ao");
    private final Random random = new Random(System.currentTimeMillis());
    private final Queue<Integer> buffer;
    private final int bufferSize;
    private final Collection<Object> produced = new ArrayList<Object>();
    private final Collection<Object> consumed = new ArrayList<Object>();

    public ProduceSubmittable(Queue<Integer> buffer, int bufferSize) {
        this.buffer = buffer;
        this.bufferSize = bufferSize;
        consumed.add(buffer);
    }

    @Override
    public Collection<Object> resourcesProduced() {
        return produced;
    }

    @Override
    public Collection<Object> resourcesConsumed() {
        return consumed;
    }

    @Override
    public boolean test() {
        return buffer.size() < bufferSize;
    }

    @Override
    public Integer execute() {
        /* keeping busy */
        int tmp = random.nextInt(100);
        while (tmp > 0) {
            tmp = random.nextInt(100);
        }
        int ret = random.nextInt();
        buffer.add(ret);
        logger.info(this + " produced " + ret);
        return ret;
    }

    @Override
    public void eventually() {
    }
}
