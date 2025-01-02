package com.hd.v1.common.entity;

import com.hd.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="db_cust")
@ToString(callSuper = true)
public class CustEntity extends BaseEntity {
    @Id
    @Column(name="cust_id")
    private String id;
    @Column(nullable = false)
    private String pwd;
    @Column(nullable = false)
    private String name;
    @Builder
    public CustEntity(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }
}
