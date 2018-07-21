package in.dropcodes.npuser.Model;

public class MainModel {

    String name,area,image,uid;

    public MainModel(){

    }

    public MainModel(String name, String area, String image, String uid) {
        this.name = name;
        this.area = area;
        this.image = image;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
