package com.rest.expensetracker.resources;

import java.util.*;

import com.rest.expensetracker.domain.Transaction;
import com.rest.expensetracker.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/{catagoryId}/transactions")
public class TransactionResource {
    @Autowired
    TransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @PathVariable("catagoryId") Integer catagoryId, @RequestBody Map<String, Object> transactionMap) {
        int userId = (Integer) request.getAttribute("userId");
        Double amount = Double.valueOf(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Long transactionDate = (Long) transactionMap.get("transactionDate");
        Transaction transaction = transactionService.addTransaction(userId, catagoryId, amount, note, transactionDate);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest request, @PathVariable("catagoryId") Integer catagoryId, @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        Transaction transaction = transactionService.fetchTransactionById(userId, catagoryId, transactionId);
        return new ResponseEntity<>(transaction,HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest request,@PathVariable("catagoryId") Integer catagoryId){
        int userId=(Integer) request.getAttribute("userId");
        List<Transaction> transactions=transactionService.fetchAllTransaction(userId,catagoryId);
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String,Boolean>> updateTransaction(HttpServletRequest request,@PathVariable("catagoryId") Integer catagoryId, @PathVariable("transactionId") Integer transactionId, @RequestBody Transaction transaction){
        int userId=(Integer) request.getAttribute("userId");
        transactionService.updateTransaction(userId,catagoryId,transactionId,transaction);
        Map<String,Boolean> map=new HashMap<>();
        map.put("Success",true);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Map<String,Boolean>> deleteTransaction(HttpServletRequest request,@PathVariable("catagoryId") Integer catagoryId, @PathVariable("transactionId") Integer transactionId){
        int userId=(Integer) request.getAttribute("userId");
        transactionService.removeTransaction(userId,catagoryId,transactionId);
        Map<String,Boolean> map=new HashMap<>();
        map.put("Success",true);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}

