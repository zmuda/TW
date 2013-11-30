package pl.agh.student.mizmuda.activeobject.producersconsumers;


import org.apache.log4j.Logger;
import pl.agh.student.mizmuda.activeobject.core.ISubmittable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.Random;

public class ConsumeSubmittable implements ISubmittable<Integer> {
    private Logger logger = Logger.getLogger("ao");
    private final Random random = new Random(System.currentTimeMillis());
    private final Queue<Integer> buffer;
    private final Collection<Object> produced = new ArrayList<Object>();
    private final Collection<Object> consumed = new ArrayList<Object>();

    public ConsumeSubmittable(Queue<Integer> buffer) {
        this.buffer = buffer;
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
        return !buffer.isEmpty();
    }

    @Override
    public Integer execute() {
        /* keeping busy */
        int tmp = random.nextInt(100);
        while (tmp > 0) {
            tmp = random.nextInt(10);
        }
        Integer ret = buffer.poll();
        logger.info(this + " consumed " + ret);
        return ret;
    }

    @Override
    public void eventually() {
    }
}
