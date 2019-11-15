package br.com.battlebits.battlecraft.status;


public enum StatusField {
    NAME("name"),
    WARP_STATUS("warpStatus");

    private String fieldName;

    private StatusField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return this.fieldName;
    }
}
