package com.revolut.bank.service;

import com.revolut.bank.model.Transfer;
import com.revolut.bank.repository.TransferRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TransferService {
    private TransferRepository transferRepository;

    public TransferService() {
        this.transferRepository = new TransferRepository();
    }

    public List<Transfer> findAllTransfers() throws SQLException {
        return transferRepository.findAllTransfers();
    }

    public Transfer findTransferById(Long id) throws SQLException {
        return transferRepository.findTransferById(id);
    }

    public void createTransfer(Transfer transfer) throws SQLException {
        Long id = transferRepository.createTransfer(transfer);
        transfer.setCreatedAt(new Date());
        transfer.setId(id);
    }

    public Transfer updateTransfer(Transfer transfer) throws SQLException {
        transferRepository.updateTransfer(transfer);
        return transferRepository.findTransferById(transfer.getId());
    }

    public void deleteTransfer(Long id) throws SQLException {
        transferRepository.deleteTransfer(id);
    }
}
