package com.monitoring.finance.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestTableController {

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/testtable")
    public List<TestEntity> getTestTableContents() {
        return testRepository.findAll(); // Fetch all entries from test_table
    }

    @PostMapping("/testtable")
    public TestEntity addTestEntity(@RequestBody TestEntity newEntity) {
        return testRepository.save(newEntity);
    }

    @GetMapping("/testtable/column1/{column1Value}")
    public List<TestEntity> getTestTableByColumn1(@PathVariable String column1Value) {
        return testRepository.findByColumn1(column1Value);
    }
}
