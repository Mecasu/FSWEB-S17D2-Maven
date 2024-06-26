package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    private Taxable taxable;
    public Map<Integer, Developer> developers;


    @PostConstruct
    public void init() {
        developers = new HashMap<>();
        developers.put(1, new Developer(111, "Melih", 20000d, Experience.JUNIOR));
    }

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @GetMapping
    public List<Developer> getDevList() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDev(@PathVariable int id) {
        return developers.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDev(@RequestBody Developer developer){
        double taxRate;
        double salary = developer.getSalary();
        if(developer.getExperience() == Experience.JUNIOR){
            taxRate = taxable.getSimpleTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }else if(developer.getExperience() == Experience.MID){
            taxRate = taxable.getMiddleTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }else{
            taxRate = taxable.getUpperTaxRate();
            developer.setSalary(salary - (salary * (taxRate / 100)));
        }
        this.developers.put(developer.getId(),developer);
        return developer;
    }
    @PutMapping("{id}")
    public Developer updateDev(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, new Developer(developer.getId(), developer.getName(), developer.getSalary(), developer.getExperience()));
        return developer;
    }

    @DeleteMapping("{id}")
    public Developer deleteDev(@PathVariable int id) {
        Developer developer = developers.get(id);
        developers.remove(id, developer);
        return developer;
    }
}
