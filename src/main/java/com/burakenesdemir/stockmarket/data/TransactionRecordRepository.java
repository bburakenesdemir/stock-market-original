package com.burakenesdemir.stockmarket.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    TransactionRecord getTransactionRecordByHashtag(String hashtag);
}
