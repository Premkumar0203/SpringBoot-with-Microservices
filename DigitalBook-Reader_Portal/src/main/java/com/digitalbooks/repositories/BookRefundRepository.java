package com.digitalbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.entities.BookRefund;

public interface BookRefundRepository extends JpaRepository<BookRefund, Integer>
{

}
