package com.fafu.app.elecbao.data;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.HashMap;
import java.util.Map;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class QueryData {
    @Id
    private String account;
    private String aid;
    private String room;
    private String floorId;
    private String floor;
    private String areaId;
    private String area;
    private String buildingId;
    private String building;

    public QueryData() {
        init();
    }

    @Generated(hash = 2145516929)
    public QueryData(String account, String aid, String room, String floorId, String floor, String areaId,
            String area, String buildingId, String building) {
        this.account = account;
        this.aid = aid;
        this.room = room;
        this.floorId = floorId;
        this.floor = floor;
        this.areaId = areaId;
        this.area = area;
        this.buildingId = buildingId;
        this.building = building;
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

    public void init() {
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
    private String toJsonString() {
        return "{\"aid\":\"" + aid + "\"," +
                "\"account\":\"" + account + "\"," +
                "\"room\":{\"roomid\":\"" + room + "\",\"room\":\"" + account + "\"}," +
                "\"floor\":{\"floorid\":\"" + floorId + "\",\"floor\":\"" + floor + "\"}," +
                "\"area\":{\"area\":\"" + areaId + "\",\"areaname\":\"" + area + "\"}," +
                "\"building\":{\"buildingid\":\"" + buildingId + "\",\"building\":\"" + building + "\"}}";
    }

    public Map<String, String> toFiledMap(Query type) {
        String query;
        String funname;
        switch (type) {
            case APPINFO: {
                query = "query_appinfo";
                funname = "synjones.onecard.query.appinfo";
                break;
            }
            case AREA: {
                query = "query_elec_area";
                funname = "synjones.onecard.query.elec.area";
                break;
            }
            case BUILDING: {
                query = "query_elec_building";
                funname = "synjones.onecard.query.elec.building";
                break;
            }
            case FLOOR: {
                query = "query_elec_floor";
                funname = "synjones.onecard.query.elec.floor";
                break;
            }
            case ROOM: {
                query = "query_elec_room";
                funname = "synjones.onecard.query.elec.room";
                break;
            }
            case ROOMINFO: {
                query = "query_elec_roominfo";
                funname = "synjones.onecard.query.elec.roominfo";
                break;
            }
            default:
                query = "";
                funname = "";
        }
        Map<String, String> filedMap = new HashMap<>();
        filedMap.put("jsondata", "{\"" + query + "\":" + toJsonString() + "}");
        filedMap.put("funname", funname);
        filedMap.put("json", "true");
        return filedMap;
    }

    public enum Query {
        APPINFO, AREA, BUILDING, FLOOR, ROOM, ROOMINFO
    }
}
