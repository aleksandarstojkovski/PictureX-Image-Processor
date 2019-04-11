package ch.supsi;

import com.drew.metadata.Tag;

public class MetadataWrapper {

    String name;
    String value;
    String type;

    MetadataWrapper(Tag tag){
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
