package org.getchunky.chunkyapi.object;

import org.getchunky.chunkyimpl.persistence.ChunkyPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractChunkyObject extends ChunkyPersistable implements ChunkyObject {

    String name;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
