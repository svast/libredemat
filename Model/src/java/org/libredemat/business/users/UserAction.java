package org.libredemat.business.users;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.libredemat.security.SecurityContext;
import org.libredemat.util.UserUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@Entity
@Table(name="user_action")
public class UserAction {

    public enum Type {

        CREATION("Creation"),
        MODIFICATION("Modification"),
        STATE_CHANGE("StateChange"),
        QoS("QoS"),
        CONTACT("Contact"),
        DELETION("Deletion"),
        MERGE("Merge"),
        MOVE("Move"),
        INTERNAL_NOTE("InternalNote"),
        WAITING_NOTIFICATION("WaitingNotification"),
        NOTIFICATION_ERROR("NotificationError"),
        NOTIFIED("Notified"),
        SERVICE_EXTERNE("ServiceExterne"),
        SYNCHRONISE("Synchronise");

        private String name;
        private Type(String type) { this.name = type; }
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable=false)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Type type;

    @Column(name="user_id",nullable=false)
    private Long userId;

    @Column(name="target_id",nullable=false)
    private Long targetId;

    @Column(name="note",length=1024)
    private String note;

    @Column(columnDefinition="TEXT")
    private String data;

    @Column(name="file")
    private byte[] file;

    protected UserAction() { /* empty constructor for Hibernate */ }

    public UserAction(Type type, Long targetId) {
        this(type, targetId, new JsonObject());
    }

    public UserAction(Type type, Long targetId, JsonObject payload) {
        this(type, targetId, payload, null);
    }

    public UserAction(Type type, Long targetId, JsonObject payload, Long originUserId) {
        this.date = new Date();
        this.type = type;

        // Retrieve transcriber of the trace from the originUserId parameter in priority, or get current logged user
        if (originUserId != null) this.userId = originUserId;
        else if (SecurityContext.getCurrentUserId() != null) this.userId = SecurityContext.getCurrentUserId();
        else this.userId = -1l;

        JsonObject user = new JsonObject();
        user.addProperty("id", userId);
        user.addProperty("name", UserUtils.getDisplayName(userId));
        payload.add("user", user);

        this.targetId = targetId;
        user = new JsonObject();
        user.addProperty("id", targetId);
        user.addProperty("name", UserUtils.getDisplayName(targetId));
        payload.add("target", user);
        data = new Gson().toJson(payload);
    }

    public UserAction(Type type, Long targetId, Long userId)
    {
        date = new Date();
        this.type = type;
        this.userId = userId;
        JsonObject payload = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("id", userId);
        user.addProperty("name", UserUtils.getDisplayName(userId));
        payload.add("user", user);
        this.targetId = targetId;
        user = new JsonObject();
        user.addProperty("id", targetId);
        user.addProperty("name", UserUtils.getDisplayName(targetId));
        payload.add("target", user);
        data = new Gson().toJson(payload);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
