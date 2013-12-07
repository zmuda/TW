package activeobject.mointoractiveobject;

public enum ElementState {
    EMPTY("□"), FULL("■"), BEING_FILLED("◊"), BEING_EMPTIED("♦");

    ElementState(String s) {
        representation = s;
    }

    private String representation;

    public String getRepresentation() {
        return representation;
    }
}