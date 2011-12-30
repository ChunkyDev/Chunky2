package org.getchunky.chunkyapi.object;

import org.getchunky.chunky.persistence.ChunkyPersistable;

import javax.persistence.Entity;

@Entity
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
