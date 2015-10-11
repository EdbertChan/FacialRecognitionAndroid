package swipe.android.berkeleyfacial.model;

public class Data {

    private String imagePath;

    private String id;
    private boolean isrecommend;

    public Data(String imagePath, String id, boolean isrecommend) {
        this.imagePath = imagePath;
        this.id = id;
        this.isrecommend = isrecommend;
    }
public boolean isIsrecommend(){return isrecommend;};
    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

}