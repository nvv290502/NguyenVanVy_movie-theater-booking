package com.movie_theaters.repository;

import com.movie_theaters.dto.response.BillFoodHistory;
import com.movie_theaters.entity.BillFood;
import com.movie_theaters.entity.compositekeyfields.BillFoodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillFoodRepository extends JpaRepository<BillFood, BillFoodId> {

    @Query("SELECT bf FROM BillFood bf WHERE bf.bill.id = :billId")
    List<BillFood> findByBillId(Long billId);

    @Query("SELECT new com.movie_theaters.dto.response.BillFoodHistory(bf.food.id, bf.food.name, bf.quantity, bf.food.price, bf.food.price * bf.quantity) "+
            "FROM BillFood bf "+
            "WHERE bf.bill.billCode = :billCode")
    List<BillFoodHistory> getBillFoodDetail(String billCode);
}
