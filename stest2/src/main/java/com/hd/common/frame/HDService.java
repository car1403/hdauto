package com.hd.common.frame;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HDService<V,K> {

    V get(K k);
    @Transactional
    V save(V v);
    @Transactional
    V modify(V v);
    @Transactional
    K remove(K k);

    List<V> getall();

}