package in.co.mrfoody.mrfoody.Catalog.catalogProduct;

/**
 * Created by om on 6/2/16.
 *
 * Allows you to retrieve information about the specified product image.
 */
public class catalogProductAttributeMediaInfo {
    String file;
    String label;
    String position;
    String exclude;
    String url;
    //private TypesOfImage types;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
