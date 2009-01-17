package com.sapienter.jbilling.server.notification.db;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;

import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.LanguageDTO;
import org.hibernate.annotations.OrderBy;

@Entity
@TableGenerator(
        name = "notification_message_GEN", 
        table = "jbilling_table", 
        pkColumnName = "name", 
        valueColumnName = "next_id", 
        pkColumnValue = "notification_message", 
        allocationSize = 10)
@Table(name = "notification_message")
public class NotificationMessageDTO implements Serializable {

    private int id;
    private NotificationMessageTypeDTO notificationMessageType;
    private CompanyDTO entity;
    private LanguageDTO language;
    private short useFlag;
    private Set<NotificationMessageSectionDTO> notificationMessageSections = new HashSet<NotificationMessageSectionDTO>(
            0);
    private int versionNum;

    public NotificationMessageDTO() {

    }

    public NotificationMessageDTO(int id, CompanyDTO entity,
            LanguageDTO language, short useFlag) {
        this.id = id;
        this.entity = entity;
        this.language = language;
        this.useFlag = useFlag;
    }

    public NotificationMessageDTO(int id,
            NotificationMessageTypeDTO notificationMessageType,
            CompanyDTO entity, LanguageDTO language, short useFlag,
            Set<NotificationMessageSectionDTO> notificationMessageSections) {
        this.id = id;
        this.notificationMessageType = notificationMessageType;
        this.entity = entity;
        this.language = language;
        this.useFlag = useFlag;
        this.notificationMessageSections = notificationMessageSections;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notification_message_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    public NotificationMessageTypeDTO getNotificationMessageType() {
        return this.notificationMessageType;
    }

    public void setNotificationMessageType(
            NotificationMessageTypeDTO notificationMessageType) {
        this.notificationMessageType = notificationMessageType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    public CompanyDTO getEntity() {
        return this.entity;
    }

    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    public LanguageDTO getLanguage() {
        return this.language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    @Column(name = "use_flag", nullable = false)
    public short getUseFlag() {
        return this.useFlag;
    }

    public void setUseFlag(short useFlag) {
        this.useFlag = useFlag;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notificationMessage")
    @OrderBy(clause="section")
    public Set<NotificationMessageSectionDTO> getNotificationMessageSections() {
        return this.notificationMessageSections;
    }

    public void setNotificationMessageSections(
            Set<NotificationMessageSectionDTO> notificationMessageSections) {
        this.notificationMessageSections = notificationMessageSections;
    }
    
    @Version
    @Column(name="OPTLOCK")
    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

}
