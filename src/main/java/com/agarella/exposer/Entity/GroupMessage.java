package com.agarella.exposer.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "GROUP_MESSAGE")
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GroupMessage {
    @Id
    @Column(name = "group_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name_group")
    private String group;
    @Column(name = "total_per_group")
    private int total;
}
