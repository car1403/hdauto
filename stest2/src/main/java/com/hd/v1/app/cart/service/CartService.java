package com.hd.v1.app.cart.service;
import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdDuplicateException;
import com.hd.common.exception.IdNotFoundException;
import com.hd.common.frame.HDService;
import com.hd.v1.app.cart.repository.CartRepository;
import com.hd.v1.common.entity.CartEntity;
import com.hd.v1.common.entity.CustEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements HDService<CartEntity, Long> {

    private final CartRepository cartRepository;

    @Override
    public CartEntity get(Long aLong) {
        return cartRepository.findById(aLong).orElseThrow(()-> new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
    }

    @Override
    public CartEntity save(CartEntity cartEntity) {
        if(cartEntity.getId() != null){
            Optional<CartEntity> item =
                    cartRepository.findById(cartEntity.getId());

            if (item.isPresent()) {
                cartEntity.setCnt(cartEntity.getCnt() + item.get().getCnt());
            }
        }
        return cartRepository.save(cartEntity);
    }

    @Override
    public CartEntity modify(CartEntity cartEntity) {
        cartRepository.findById(cartEntity.getId()).orElseThrow(()-> new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
        return cartRepository.save(cartEntity);
    }

    @Override
    public Long remove(Long aLong) {
        CartEntity item = cartRepository.findById(aLong).orElseThrow(()-> new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
        cartRepository.delete(item);
        return aLong;
    }

    @Override
    public List<CartEntity> getall() {
        List<CartEntity> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(), ErrorCode.DATA_DOSE_NOT_EXIST);
        }
        return carts;
    }

    public List<CartEntity> getall(String id) {
        List<CartEntity> carts = cartRepository.findByCustId(id);
        if(carts.isEmpty()){
            throw new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(), ErrorCode.DATA_DOSE_NOT_EXIST);
        }
        return carts;
    }

}