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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

@Entity
@TableGenerator(
        name = "notification_message_type_GEN", 
        table = "jbilling_table", 
        pkColumnName = "name", 
        valueColumnName = "next_id", 
        pkColumnValue = "notification_message_type", 
        allocationSize = 10)
@Table(name = "notification_message_type")
public class NotificationMessageTypeDTO implements Serializable {

    private int id;
    private int sections;
    private Set<NotificationMessageDTO> notificationMessages = new HashSet<NotificationMessageDTO>(
            0);
    private int versionNum;

    public NotificationMessageTypeDTO() {
    }

    public NotificationMessageTypeDTO(int id, int sections) {
        this.id = id;
        this.sections = sections;
    }

    public NotificationMessageTypeDTO(int id, int sections,
            Set<NotificationMessageDTO> notificationMessages) {
        this.id = id;
        this.sections = sections;
        this.notificationMessages = notificationMessages;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notification_message_type_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "sections", nullable = false)
    public int getSections() {
        return this.sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notificationMessageType")
    public Set<NotificationMessageDTO> getNotificationMessages() {
        return this.notificationMessages;
    }

    public void setNotificationMessages(
            Set<NotificationMessageDTO> notificationMessages) {
        this.notificationMessages = notificationMessages;
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
