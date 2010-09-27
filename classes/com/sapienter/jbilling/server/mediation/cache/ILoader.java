package com.sapienter.jbilling.server.mediation.cache;

public interface ILoader {

    /** System created primary key for all loaded tables, helpful in implementing logic and ordering. */
    final String CACHE_PRIMARY_KEY = "CACHE_ID";
    
    public String getTableName();
    
    public void init();
    public void destroy();
}
