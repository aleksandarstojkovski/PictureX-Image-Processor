package ch.picturex.model;

import com.drew.metadata.Tag;

@SuppressWarnings("unused")

public class MetadataWrapper {

    private String name;
    private String value;
    private String type;

    public MetadataWrapper(Tag tag){
        this.name=tag.getTagName();
        this.value =tag.getDescription();
        this.type=tag.getDirectoryName();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
