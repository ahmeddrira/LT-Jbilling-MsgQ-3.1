/*
 JBILLING CONFIDENTIAL
 _____________________

 [2003] - [2012] Enterprise jBilling Software Ltd.
 All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Enterprise jBilling Software.
 The intellectual and technical concepts contained
 herein are proprietary to Enterprise jBilling Software
 and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden.
 */

package com.sapienter.jbilling.server.pricing.db;

import com.sapienter.jbilling.server.util.sql.JDBCUtils;
import com.sapienter.jbilling.server.util.sql.TableGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rate_card")
@javax.persistence.TableGenerator(
        name = "rate_card_GEN",
        table = "jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue = "rate_card",
        allocationSize = 10
)
// no cache
public class RateCardDTO implements Serializable {

    public static final String TABLE_PREFIX = "rate_";

    public static final TableGenerator.Column[] TABLE_COLUMNS = new TableGenerator.Column[] {
            new TableGenerator.Column("match", "varchar(20)", false),
            new TableGenerator.Column("comment", "varchar(255)", true),
            new TableGenerator.Column("rate", "numeric(22,10)", false)
    };

    private Integer id;
    private String name;
    private String tableName;

    public RateCardDTO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rate_card_GEN")
    @Column(name = "id", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "table_name", nullable = false, unique = true)
    public String getTableName() {
        if (tableName == null && name != null) {
            tableName = JDBCUtils.toDatabaseObjectName(name);
        }

        if (tableName != null && !tableName.startsWith(TABLE_PREFIX)) {
            tableName = TABLE_PREFIX + tableName;
        }

        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
