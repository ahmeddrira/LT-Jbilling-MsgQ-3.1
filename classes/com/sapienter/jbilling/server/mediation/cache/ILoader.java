package com.sapienter.jbilling.server.mediation.cache;

public interface ILoader {

    public String getTableName();

    public void flush();

}
