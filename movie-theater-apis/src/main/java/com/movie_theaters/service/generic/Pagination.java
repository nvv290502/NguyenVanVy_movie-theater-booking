package com.movie_theaters.service.generic;

import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.EmptyListException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public abstract class Pagination<T,ID, T1> {

    private final JpaRepository<T, ID> repository;

    protected Pagination(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public Page<T1> getAll(int page, int size, String column, String sortType){
        Sort sort = Sort.by(column);
        if("desc".equalsIgnoreCase(sortType)){
            sort = sort.descending();
        }else{
            sort = sort.ascending();
        }
        Page<T> obj =  repository.findAll(PageRequest.of(page,size,sort));
        Page<T1> obj1 = obj.map(this::convert);
        return obj1;
    }

    public Page<T1> getByIsEnabled(int page, int size, String column, String sortType){
        Sort sort = Sort.by(column);
        if("desc".equalsIgnoreCase(sortType)){
            sort = sort.descending();
        }else{
            sort = sort.ascending();
        }
        Page<T> obj =  getIsEnabledMethod(PageRequest.of(page,size,sort), true);
        Page<T1> obj1 = obj.map(this::convert);
        return obj1;
    }
    protected abstract T1 convert(T entity);
    protected abstract Page<T> getIsEnabledMethod(PageRequest pageRequest, Boolean isEnabled);
//    Page<T> getByIsEnable(int page, int size, String column, String sortType);
}
