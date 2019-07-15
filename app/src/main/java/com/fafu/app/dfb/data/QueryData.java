package com.fafu.app.dfb.data;

import androidx.annotation.NonNull;

public class QueryData {
    private String aid;
    private String account;
    private String room;
    private String floorId;
    private String floor;
    private String areaId;
    private String area;
    private String buildingId;
    private String building;

    public QueryData() {
        clear();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void clear() {
        this.aid = "";
        this.room = "";
        this.floorId = "";
        this.floor = "";
        this.areaId = "";
        this.area = "";
        this.buildingId = "";
        this.building = "";
    }

    @NonNull
    public String toJsonString() {
        return "{\"aid\":\"" + aid + "\"," +
                "\"account\":\"" + account + "\"," +
                "\"room\":{\"roomid\":\"" + room + "\",\"room\":\"" + account + "\"}," +
                "\"floor\":{\"floorid\":\"" + floorId + "\",\"floor\":\"" + floor + "\"}," +
                "\"area\":{\"area\":\"" + areaId + "\",\"areaname\":\"" + area + "\"}," +
                "\"building\":{\"buildingid\":\"" + buildingId + "\",\"building\":\"" + building + "\"}}";
    }
}
