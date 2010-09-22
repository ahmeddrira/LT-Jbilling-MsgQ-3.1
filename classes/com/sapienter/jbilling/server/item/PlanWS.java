package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.item.db.PlanDTO;

import java.util.List;

/**
 * @author Brian Cowdery
 * @since 20-09-2010
 */
public class PlanWS {

    private Integer id;
    private Integer itemId; // plan subscription item    
    private String description;
    private List<PlanItemWS> planItems;

    public PlanWS() {
    }

    public PlanWS(PlanDTO dto, List<PlanItemWS> planItems) {
        this.id = dto.getId();
        this.description = dto.getDescription();
        this.planItems = planItems;

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

    public Integer getPlanSubscriptionItemId() {
        return getItemId();
    }

    public void setPlanSubscriptionItemId(Integer planSubscriptionItemId) {
        setItemId(planSubscriptionItemId);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PlanItemWS> getPlanItems() {
        return planItems;
    }

    public void setPlanItems(List<PlanItemWS> planItems) {
        this.planItems = planItems;
    }

    @Override
    public String toString() {
        return "PlanWS{"
               + "id=" + id
               + ", itemId=" + itemId
               + ", description='" + description + '\''
               + ", planItems=" + planItems
               + '}';
    }
}
