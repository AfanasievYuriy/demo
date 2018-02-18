public enum ActionType {
    IN("вход"),
    OUT("выход");

    private String representation;
    ActionType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    static ActionType parseActionType(String representation) {
        return representation.equals(IN.representation) ? IN : OUT;
    }
}
