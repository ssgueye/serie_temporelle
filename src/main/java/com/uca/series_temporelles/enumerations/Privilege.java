package com.uca.series_temporelles.enumerations;

public enum Privilege {
    WRITE_READ("WR"),
    READONLY("R");

    private final String privilegeAcronym;

    Privilege(String privilegeAcronym) {
        this.privilegeAcronym = privilegeAcronym;
    }

    public String getPrivilegeAcronym() {
        return privilegeAcronym;
    }

    
}
