package com.magic.interview.service.sql_batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-25 14:06
 **/
@RestController
@RequestMapping("/sqlBatch")
public class SqlBatchController {

    @Autowired
    private BatchService batchService;

    @GetMapping("/test")
    public String test(@RequestParam String type) throws SQLException {
        switch (type) {
            case "single":
                batchService.single();
                break;
            case "batch":
                batchService.insertB();
                break;
            case "batch2":
                batchService.insertB2();
                break;
            case "foreach":
                batchService.foreachInsert();
                break;
        }
        return "succ";
    }
}
