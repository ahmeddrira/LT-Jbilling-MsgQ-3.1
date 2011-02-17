package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.pricing.PriceModelBL;
import com.sapienter.jbilling.server.pricing.PriceModelWS;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Brian Cowdery
 * @since 20-09-2010
 */
public class PlanItemWS implements Serializable {

    public static final Integer DEFAULT_PRECEDENCE = -1;

    private Integer id;
    private Integer itemId; // affected item
    private Integer periodId; // item period
    private PriceModelWS model;
    private String bundledQuantity;
    private Integer precedence = DEFAULT_PRECEDENCE;

    public PlanItemWS() {
    }

    public PlanItemWS(Integer itemId, PriceModelWS model) {
        this.itemId = itemId;
        this.model = model;
    }

    public PlanItemWS(PlanItemDTO dto) {
        this.id = dto.getId();
        this.precedence = dto.getPrecedence();

        setBundledQuantity(dto.getBundledQuantity());

        if (dto.getModel() != null) this.model = new PriceModelWS(dto.getModel());
        if (dto.getItem() != null) this.itemId = dto.getItem().getId();
        if (dto.getPeriod() != null) this.periodId = dto.getPeriod().getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAffectedItemId() {
        return getItemId();
    }

    public void setAffectedItemId(Integer affectedItemId) {
        setItemId(affectedItemId);
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public PriceModelWS getModel() {
        return model;
    }

    public void setModel(PriceModelWS model) {
        this.model = model;
    }

    public String getBundledQuantity() {
        return bundledQuantity;
    }

    public BigDecimal getBundledQuantityAsDecimal() {
        return bundledQuantity != null ? new BigDecimal(bundledQuantity) : null;
    }

    public void setBundledQuantity(String bundledQuantity) {
        this.bundledQuantity = bundledQuantity;
    }

    public void setBundledQuantity(BigDecimal bundledQuantity) {
        this.bundledQuantity = (bundledQuantity != null ? bundledQuantity.toString() : null);
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    @Override
    public String toString() {
        return "PlanItemWS{"
               + "id=" + id
               + ", itemId=" + itemId
               + ", periodId=" + periodId
               + ", model=" + model
               + ", bundledQuantity=" + bundledQuantity
               + ", precedence=" + precedence
               + '}';
    }
}

