package com.loopitis.enums;

public enum eRedisDB {
    DB_0(0);

    private final int db;

    private eRedisDB(int db) {
        this.db = db;
    }


    public int getDB() {
        return this.db;
    }
}
