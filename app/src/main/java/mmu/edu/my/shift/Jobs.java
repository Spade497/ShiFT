package mmu.edu.my.shift;

public class Jobs {
    String id, name, state, district, desc, wage, info;

    public Jobs () {}

    public Jobs(String id, String name, String state, String district, String desc, String wage, String info) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.district = district;
        this.desc = desc;
        this.wage = wage;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
