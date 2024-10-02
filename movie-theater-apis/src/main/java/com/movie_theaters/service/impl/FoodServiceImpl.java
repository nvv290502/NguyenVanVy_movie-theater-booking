package com.movie_theaters.service.impl;

import com.movie_theaters.entity.Food;
import com.movie_theaters.repository.FoodRepository;
import com.movie_theaters.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;

    @Override
    public List<Food> getAll() {
        return foodRepository.findAll();
    }
}
