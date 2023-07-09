package com.rest.expensetracker.resources;

import java.util.*;

import com.rest.expensetracker.domain.Catagory;
import com.rest.expensetracker.services.CatagoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CatagoryResource {

    @Autowired
    CatagoryService catagoryService;

    @GetMapping("")
    public ResponseEntity<List<Catagory>> getAllCatagories(HttpServletRequest request){
        int userId=(Integer) request.getAttribute("userId");
        List<Catagory> catagories=catagoryService.fetchAllCatagories(userId);
        return new ResponseEntity<>(catagories,HttpStatus.OK);
    }

    @GetMapping("/{catagoryId}")
    public  ResponseEntity<Catagory> getCatagoryById(HttpServletRequest request, @PathVariable("catagoryId") Integer catagoryId){
        int userId= (Integer) request.getAttribute("userId");
        Catagory catagory=catagoryService.fetchAllCatagoryById(userId,catagoryId);
        return new ResponseEntity<Catagory>(catagory,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Catagory> addCategory(HttpServletRequest request,
                                                @RequestBody Map<String, Object> categoryMap) {
        int userId = (Integer) request.getAttribute("userId");
        String title = (String) categoryMap.get("title");
        String description = (String) categoryMap.get("description");
        Catagory catagory = catagoryService.addCatagory(userId, title, description);
        return new ResponseEntity<>(catagory, HttpStatus.CREATED);
    }

    @PutMapping("/{catagoryId}")
    public ResponseEntity<Map<String,Boolean>> updateCatagory(HttpServletRequest request, @PathVariable("catagoryId") Integer catagoryId,@RequestBody Catagory catagory){
        int userId=(Integer) request.getAttribute("userId");
        catagoryService.updateCatagory(userId,catagoryId,catagory);
        Map<String,Boolean> map=new HashMap<>();
        map.put("Success",true);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @DeleteMapping("/{catagoryId}")
    public ResponseEntity<Map<String,Boolean>> deleteCatagory(HttpServletRequest request,@PathVariable("catagoryId") Integer catagoryId){
        int userId= (Integer) request.getAttribute("userId");
        catagoryService.removeCatagoryWithAllTransactions(userId,catagoryId);
        Map<String,Boolean> map=new HashMap<>();
        map.put("Success",true);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
