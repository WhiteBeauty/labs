package com.karjakina.labs.entity;

public class BuildingEntity {

    private Integer id;
    private String address;
    private int floors;

    public BuildingEntity() {
    }

    public BuildingEntity(String address, int floors) {
        this.address = address;
        this.floors = floors;
    }

    public BuildingEntity(Integer id, String address, int floors) {
        this.id = id;
        this.address = address;
        this.floors = floors;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public int getFloors() {

        return floors;
    }

    public void setFloors(int floors) {

        this.floors = floors;
    }

    @Override
    public String toString() {
        return "BuildingEntity{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", floors=" + floors +
                '}';
    }
}
