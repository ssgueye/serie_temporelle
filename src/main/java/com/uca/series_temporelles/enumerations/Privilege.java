package com.uca.series_temporelles.enumerations;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Privilege {
    @JsonProperty("WRITE_READ")
    WRITE_READ("WR"),
    @JsonProperty("READONLY")
    READONLY("R");

    private String privilegeAcronym;

    Privilege(String privilegeAcronym) {
        this.privilegeAcronym = privilegeAcronym;
    }

    public String getPrivilegeAcronym() {
        return privilegeAcronym;
    }


}
