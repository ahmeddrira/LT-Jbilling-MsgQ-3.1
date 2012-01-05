/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

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
    private PriceModelWS model;
    private PlanItemBundleWS bundle;
    private Integer precedence = DEFAULT_PRECEDENCE;

    public PlanItemWS() {
    }

    public PlanItemWS(Integer itemId, PriceModelWS model, PlanItemBundleWS bundle) {
        this.itemId = itemId;
        this.model = model;
        this.bundle = bundle;
    }

    public PlanItemWS(PlanItemDTO dto) {
        this.id = dto.getId();
        this.precedence = dto.getPrecedence();

        if (dto.getModel() != null) this.model = new PriceModelWS(dto.getModel());
        if (dto.getBundle() != null) this.bundle = new PlanItemBundleWS(dto.getBundle());
        if (dto.getItem() != null) this.itemId = dto.getItem().getId();
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

    public PriceModelWS getModel() {
        return model;
    }

    public void setModel(PriceModelWS model) {
        this.model = model;
    }

    public PlanItemBundleWS getBundle() {
        return bundle;
    }

    public void setBundle(PlanItemBundleWS bundle) {
        this.bundle = bundle;
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
               + ", model=" + model
               + ", bundle=" + bundle
               + ", precedence=" + precedence
               + '}';
    }
}

