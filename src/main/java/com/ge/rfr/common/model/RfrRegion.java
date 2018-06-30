package com.ge.rfr.common.model;

public enum RfrRegion {
	ASIA("Asia"),
	EUROPE("Europe"),
	INDIA("India"),
	LATIN_AMERICA("Latin America"),
	MEA("MEA"),
	NORTH_AMERICA("North America");
	
	private String region;
	
	RfrRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
