package com.agarella.exposer.Entity;

import javax.persistence.*;

@Entity
@Table(name = "GROUP_MESSAGE")
public class GroupMessage {
    @Id
    @Column(name = "group_message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name_group")
    private String group;
    @Column(name = "total_per_group")
    private int total;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
