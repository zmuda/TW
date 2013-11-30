package pl.agh.student.mizmuda.activeobject.core;


import java.util.Collection;

public interface ISubmittable<T> {
    /**
     * determines cross-interests with tasks also declaring resource in this section
     * and share of interests with the other method from the family
     * <p/>
     * if there is buffer with resource, buffer object should be placed in implementation
     * if there is any counter etc., any object containing that value should be placed
     * <p/>
     * resource mentioned in returned collection can become unavailable due to execution
     * but 'space' may appear for tasks, which 'produce' (not consume) that resource
     *
     * @return number of that resources can be increased
     */
    public Collection<Object> resourcesProduced();

    /**
     * determines cross-interests with tasks also declaring resource in this section
     * and share of interests with the other method from the family
     * <p/>
     * if there is buffer with resource, buffer object should be placed in implementation
     * if there is any counter etc., any object containing that value should be placed
     * <p/>
     * resource mentioned in returned collection can become available due to execution
     * but 'space' may become unavailable for tasks, which 'produce' (not consume) that resource
     *
     * @return number of that resources can be reduced
     */
    public Collection<Object> resourcesConsumed();

    /**
     * guard
     * <p/>
     * if there is needed synchronization with external processes it should be done here
     * but finalized in 'eventually' method
     * <p/>
     * method is called to test the guard condition
     *
     * @return if guard is passed
     */
    public boolean test();

    /**
     * user code invoked after passing 'test'
     * parameters, context, etc. should be initialized in constructor
     *
     * @return Executor will wrap this value into future
     */
    T execute();

    /**
     * cleanup method
     * <p/>
     * is called after execution and negative-verified 'test' invocations
     * (after positive execution follows)
     */
    public void eventually();
}
