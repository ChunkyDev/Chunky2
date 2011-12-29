package org.getchunky.chunkyimpl.persistence;

import org.getchunky.chunkyapi.persistence.PersistanceInterface;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.sql.Timestamp;

@MappedSuperclass
public class ChunkyPersistable implements PersistanceInterface {

    @Id
    Integer id;

    @Version
    Timestamp lastUpdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
