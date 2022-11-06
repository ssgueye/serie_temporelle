package com.uca.series_temporelles.enumerations;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Permission {
    @JsonProperty("WRITE_READ")
    WRITE_READ("WR"),
    @JsonProperty("READONLY")
    READONLY("R");

    private String privilegeAcronym;

    Permission(String privilegeAcronym) {
        this.privilegeAcronym = privilegeAcronym;
    }

    public String getPrivilegeAcronym() {
        return privilegeAcronym;
    }


}
