package org.getchunky.chunky.persistence;

import com.avaje.ebean.EbeanServer;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunkyapi.persistence.Persistence;

public class EBeansPersistence implements Persistence {

    /* (non-Javadoc)
      * @see org.getchunky.chunkyapi.persistence.Persistence.#initialize
      */
    public void initialize() {
        EbeanServer db = Chunky.getPlugin().getDatabase();
        if (db == null)
            throw new NullPointerException("plugin.getDatabase() returned null EbeanServer!");

        /*
        // Check that our tables exist - if they don't, then install the database.
        try {
            db.find(Spawn.class).findRowCount();
        } catch (PersistenceException ex) {
            log.info("Installing database for "
                    + plugin.getPluginName()
                    + " due to first time usage");

            plugin.installDatabaseDDL();
            SqlUpdate update = db.createSqlUpdate("insert into hsp_version VALUES(1, 91)");
            update.execute();
        }

        try {
            upgradeDatabase();
        } catch(Exception e) { e.printStackTrace(); }*/
    }

    public void purgeCache() {
        // in theory we should pass this call through to the EBEAN server, but it doesn't
        // offer any support for this functionality.  So we do nothing.
    }

    private void upgradeDatabase() {/*
        int knownVersion = 80;		// assume current version to start

        EbeanServer db = plugin.getDatabase();

        Version versionObject = null;
        try {
            String q = "find version where id = 1";
            Query<Version> versionQuery = db.createQuery(Version.class, q);
            versionObject = versionQuery.findUnique();
        }
        // we ignore any exception here, we'll catch it below in the version checks
        catch(Exception e) {}

        if( versionObject == null ) {
            try {
                SqlUpdate update = db.createSqlUpdate("insert into hsp_version VALUES(1, 80)");
                update.execute();
            }
            // if the insert fails, then we know we are on version 63 (or earlier) of the db schema
            catch(PersistenceException e) {
                knownVersion = 63;
            }
        }
        else
            knownVersion = versionObject.getDatabaseVersion();

        Debug.getInstance().debug("knownVersion = ",knownVersion);

        /*
          try {
              SqlUpdate update = db.createSqlUpdate("insert into hsp_version VALUES(1,80)");
              update.execute();

              // use negative ID and random name so to be guaranteed to not conflict with any
              // legitmate
              SqlUpdate update = db.createSqlUpdate("insert into hsp_spawn VALUES(-1,'world',null,'upgrade_check',null,0,0,0,0,0,SYSDATE(),SYSDATE());");
              update.execute();

              update = db.createSqlUpdate("delete from hsp_spawn where id = -1");
              update.execute();
          }
          catch(PersistenceException e) {
              knownVersion = 63;
          }
          */
        /*
        // determine if we are at version 62 of the database schema
        try {
            SqlQuery query = db.createSqlQuery("select world from hsp_player");
            query.findList();
        }
        catch(PersistenceException e) {
            knownVersion = 62;
        }

        if( knownVersion < 63 ) {
            log.info(logPrefix + " Upgrading from version 0.6.2 database to version 0.6.3");
            SqlUpdate update = db.createSqlUpdate("ALTER TABLE hsp_player "
                    + "ADD(`world` varchar(32) DEFAULT NULL"
                    + ",`x` double DEFAULT NULL"
                    + ",`y` double DEFAULT NULL"
                    + ",`z` double DEFAULT NULL"
                    + ",`pitch` float DEFAULT NULL"
                    + ",`yaw` float DEFAULT NULL);"
            );
            update.execute();
            log.info(logPrefix + " Upgrade from version 0.6.2 database to version 0.6.3 complete");
        }

        if( knownVersion < 80 ) {
            log.info(logPrefix + " Upgrading from version 0.6.3 database to version 0.8");
            SqlUpdate update = db.createSqlUpdate(
                    "CREATE TABLE `hsp_version` ("+
                            "`id` int(11) NOT NULL,"+
                            "`database_version` int(11) NOT NULL,"+
                            "PRIMARY KEY (`id`)"+
                            ")"
            );
            update.execute();
            update = db.createSqlUpdate("insert into hsp_version VALUES(1,80)");
            update.execute();

            update = db.createSqlUpdate("ALTER TABLE hsp_spawn modify group_name varchar(32)");
            update.execute();
            log.info(logPrefix + " Upgrade from version 0.6.3 database to version 0.8 complete");
        }

        if( knownVersion < 91 ) {
            log.info(logPrefix + " Upgrading from version 0.8 database to version 0.9.1");

            boolean success = false;
            // we must do some special work for SQLite since it doesn't respond to ALTER TABLE
            // statements from within the EBeanServer interface.  PITA!
            if( isSqlLite() ) {
                EBeanUtils ebu = EBeanUtils.getInstance();
                try {
                    Connection conn = ebu.getConnection();
                    Statement stmt = conn.createStatement();
                    stmt.execute("BEGIN TRANSACTION;");
                    stmt.execute("CREATE TEMPORARY TABLE hsphome_backup("
                            +"id integer primary key"
                            +",player_name varchar(32)"
                            +",updated_by varchar(32)"
                            +",world varchar(32)"
                            +",x double not null"
                            +",y double not null"
                            +",z double not null"
                            +",pitch float not null"
                            +",yaw float not null"
                            +",last_modified timestamp not null"
                            +",date_created timestamp not null);");
                    stmt.execute("INSERT INTO hsphome_backup SELECT"
                            +" id, player_name,updated_by,world"
                            +",x,y,z,pitch,yaw"
                            +",last_modified,date_created"
                            +" FROM hsp_home;");
                    stmt.execute("DROP TABLE hsp_home;");
                    stmt.execute("CREATE TABLE hsp_home("
                            +"id integer primary key"
                            +",player_name varchar(32)"
                            +",name varchar(32)"
                            +",updated_by varchar(32)"
                            +",world varchar(32)"
                            +",x double not null"
                            +",y double not null"
                            +",z double not null"
                            +",pitch float not null"
                            +",yaw float not null"
                            +",default_home intger(1) not null DEFAULT 0"
                            +",bed_home intger(1) not null DEFAULT 0"
                            +",last_modified timestamp not null"
                            +",date_created timestamp not null"
                            +",constraint uq_hsp_home_1 unique (player_name,name));");
                    stmt.execute("INSERT INTO hsp_home SELECT"
                            +" id, player_name,null,updated_by,world"
                            +",x,y,z,pitch,yaw,1,0"
                            +",last_modified,date_created"
                            +" FROM hsphome_backup;");
                    stmt.execute("DROP TABLE hsphome_backup;");
                    stmt.execute("COMMIT;");
    //					stmt.execute("ALTER TABLE `hsp_home` ADD `name` varchar(32)");
    //					stmt.execute("ALTER TABLE `hsp_home` ADD `bed_home` integer(1) not null");
    //					stmt.execute("ALTER TABLE `hsp_home` ADD `default_home` integer(1) not null");
    //					stmt.execute(sql);
                    stmt.close();
                    conn.close();

                    success = true;
                }
                catch(SQLException e) {
                    log.severe(logPrefix + " error attempting to update SQLite database schema!");
                    e.printStackTrace();
                }
            }
            else {
                SqlUpdate update = db.createSqlUpdate("ALTER TABLE `hsp_home` ADD (`name` varchar(32)"
                        + ",`bed_home` tinyint(1) DEFAULT '0' NOT NULL"
                        + ",`default_home` tinyint(1) DEFAULT '0' NOT NULL"
                        + ");"
                );
                update.execute();

                update = db.createSqlUpdate("ALTER TABLE `hsp_home` DROP INDEX `uq_hsp_home_1`");
                update.execute();

                update = db.createSqlUpdate("ALTER TABLE `hsp_home` ADD UNIQUE KEY `uq_hsp_home_1` (`player_name`,`name`)");
                update.execute();
                success = true;
            }

            if( success ) {
                SqlUpdate update = db.createSqlUpdate("update hsp_home set default_home=1, bed_home=0");
                update.execute();

                update = db.createSqlUpdate("update hsp_version set database_version=91");
                update.execute();
                log.info(logPrefix + " Upgrade from version 0.8 database to version 0.9.1 complete");
            }
        }*/
    }
}
