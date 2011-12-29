package org.getchunky.chunkyapi.persistence;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public interface PersistanceInterface {

    public Integer getId();

    public void setId(Integer id);

    public Timestamp getLastUpdate();

    public void setLastUpdate(Timestamp lastUpdate);
}
