package com.revolut.bank.controller;

import com.google.gson.Gson;
import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.exception.NotSameCurrencyException;
import com.revolut.bank.exception.NotSufficientBalanceException;
import com.revolut.bank.model.Transfer;
import com.revolut.bank.service.TransferService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

public class TransferController {
    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    private TransferService transferService;

    public TransferController() throws SQLException {
        this.transferService = new TransferService();
    }

    public Route findAllTransfers() {
        return (request, response) -> {
            response.type("application/json");
            try {
                List<Transfer> transfers = transferService.findAllTransfers();
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(transfers)));
            } catch (Exception e) {
                LOG.warn("Exception in findAllTransfers() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route findTransferById() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                Transfer transfer = transferService.findTransferById(id);
                response.status(HttpStatus.OK_200);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(transfer)));
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
            } catch (NoSuchEntityException e) {
                response.status(HttpStatus.NO_CONTENT_204);
                return "";
            } catch (Exception e) {
                LOG.warn("Exception in findTransferById() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route createTransfer() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Transfer transfer = new Gson().fromJson(request.body(), Transfer.class);
                if (transfer.getSourceAccountId() == null || transfer.getDestinationAccountId() == null
                        || transfer.getSum() == null || transfer.getCurrency() == null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Fill sourceAccountId, destinationAccountId," +
                            "sum, currency for creating money transfer!"));
                }
                transferService.createTransfer(transfer);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(transfer)));
            } catch (NotSufficientBalanceException | NoSuchEntityException | NotSameCurrencyException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            } catch (Exception e) {
                LOG.warn("Exception in createTransfer() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route updateTransfer() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Transfer transfer = new Gson().fromJson(request.body(), Transfer.class);
                if (transfer.getCurrency() == null && transfer.getSum() == null || transfer.getId() == null
                        || transfer.getSourceAccountId() != null || transfer.getDestinationAccountId() != null) {
                    response.status(HttpStatus.BAD_REQUEST_400);
                    return new Gson().toJson(new StandardResponse("Add id and (currency and/or sum) to update transfer!" +
                            " Do not add sourceAccountId, destinationAccountId - we can not update them!"));
                }
                Transfer updatedTransfer = transferService.updateTransfer(transfer);
                return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(updatedTransfer)));
            } catch (Exception e) {
                LOG.warn("Exception in updateTransfer() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }

    public Route deleteTransfer() {
        return (request, response) -> {
            response.type("application/json");
            try {
                Long id = Long.parseLong(request.params(":id"));
                transferService.deleteTransfer(id);
                return "";
            } catch (NumberFormatException e) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return new Gson().toJson(new StandardResponse("Id should has integer type!"));
            } catch (Exception e) {
                LOG.warn("Exception in deleteTransfer() ", e);
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        };
    }
}
