package com.deniss.neotech.db;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



public class StoredTime implements Serializable{

        private static final long serialVersionUID = 1L;
        private Integer id;
        private Timestamp theTime;

    public StoredTime(Timestamp theTime) {
            this.theTime = theTime;
        }

    public StoredTime() {
    }

    public Timestamp getTheTime() {
            return theTime;
        }

        public void setTheTime(Timestamp theTime) {
            this.theTime = theTime;
        }
        public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    }

