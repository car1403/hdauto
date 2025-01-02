package com.hd.v1.common.entity;

import com.hd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="db_item")
@ToString(callSuper = true)
public class ItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;
    @Column(unique = true)
    private String name;
    private Long price;
    @Builder
    public ItemEntity(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
