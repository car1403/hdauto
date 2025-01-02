package com.hd.v1.common.entity;

import com.hd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="db_cart")
public class CartEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long id;
    private Long cnt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cust_id")
    private CustEntity cust;
    @ManyToOne
    @JoinColumn(name="item_id")
    private ItemEntity item;

    public CartEntity(Long id, Long cnt, CustEntity cust, ItemEntity item) {
        this.id = id;
        this.cnt = cnt;
        this.cust = cust;
        this.item = item;
    }
}
