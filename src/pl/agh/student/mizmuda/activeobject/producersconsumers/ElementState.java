package pl.agh.student.mizmuda.activeobject.producersconsumers;

import java.util.Collection;

public enum ElementState {
    EMPTY("□"), FULL("■"), BEING_FILLED("◊"), BEING_EMPTIED("♦");

    ElementState(String s) {
        representation = s;
    }

    private String representation;

    private static String getRepresentation(ElementState e) {
        return e.representation;
    }

    public static String getStateString(Collection<ElementState> states) {
        StringBuffer res = new StringBuffer();
        res.append(" - occupation - ");
        for (ElementState e : states) {
            res.append(getRepresentation(e));
        }
        return res.toString();
    }
}