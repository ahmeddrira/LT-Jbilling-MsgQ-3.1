package com.sapienter.jbilling.server.mediation.cache;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractFinder implements IFinder {

    protected final JdbcTemplate jdbcTemplate;
    protected final ILoader loader;

    public AbstractFinder(JdbcTemplate template, ILoader loader) {
        this.jdbcTemplate = template;
        this.loader = loader;
    }

}
