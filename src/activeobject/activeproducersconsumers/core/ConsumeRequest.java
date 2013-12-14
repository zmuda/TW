package activeobject.activeproducersconsumers.core;


import activeobject.TaskDurations;

import java.util.Queue;

public class ConsumeRequest<T> implements IMethodRequest<T> {
    //private Logger logger = Logger.getLogger("activeobject - jednoelemoentowo");
    private Queue<T> buffer;

    public ConsumeRequest(Queue<T> buffer) {
        this.buffer = buffer;
    }

    @Override
    public boolean guard() {
        return buffer.size() > 0;
    }

    @Override
    public T execute() throws InterruptedException {
        //logger.info("consumes");
        T ret = null;
        ret = buffer.poll();
        TaskDurations.waitForItemToConsume();
        //logger.info("Consumed");
        //logger.info("\t>>>: " + buffer.size());
        return ret;
    }
}
