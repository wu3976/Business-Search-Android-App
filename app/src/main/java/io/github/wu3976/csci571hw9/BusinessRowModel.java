package io.github.wu3976.csci571hw9;

public class BusinessRowModel {
    public String serialnum;
    public String img_src;
    public String name;
    public String rating;
    public String distance;
    public String business_id;

    public BusinessRowModel(String serialnum, String img_src, String name, String rating,
                            String distance, String business_id) {
        this.serialnum = serialnum;
        this.img_src = img_src;
        this.name = name;
        this.rating = rating;
        this.distance = distance;
        this.business_id = business_id;
    }
}
