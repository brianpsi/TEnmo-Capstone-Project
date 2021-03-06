package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers();

    List<Transfer> getAllTransferByAccountId(int id);

    Transfer getTransferById(int id);

    Transfer createTransfer(Transfer transfer);

    void deleteTransfer(int id);

    List<Transfer> getPendingTransfers(Long id);
}
