package org.getchunky.chunkyapi.object;

import org.getchunky.chunkyapi.persistence.PersistableInterface;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface ChunkyIdentity extends PersistableInterface {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);
}
