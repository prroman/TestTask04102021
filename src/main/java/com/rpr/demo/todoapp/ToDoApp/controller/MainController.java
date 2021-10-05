package com.rpr.demo.todoapp.ToDoApp.controller;

import com.rpr.demo.todoapp.ToDoApp.exceptions.IllegalCurrencyException;
import com.rpr.demo.todoapp.ToDoApp.model.Price;
import com.rpr.demo.todoapp.ToDoApp.model.PriceResultCSV;
import com.rpr.demo.todoapp.ToDoApp.repository.PriceRepository;
import com.rpr.demo.todoapp.ToDoApp.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    private PriceRepository priceRepository;
    private PriceService priceService;

    @Autowired
    public MainController(PriceRepository priceRepository, PriceService priceService) {
        this.priceRepository = priceRepository;
        this.priceService = priceService;
    }

    @GetMapping("cryptocurrencies/minprice")
    public Price getLastPriceMin(@RequestParam String name) {
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")) {
            return priceService.getLastPriceMin(name);
        } else {
            throw new IllegalCurrencyException();
        }
    }

    @GetMapping("cryptocurrencies/maxprice")
    public Price getLastPriceMax(@RequestParam String name) {
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")) {
            return priceService.getLastPriceMax(name);
        } else {
            throw new IllegalCurrencyException();
        }
    }

    @GetMapping("cryptocurrencies")
    public List<Price> getLastPricePagebale(@RequestParam String name, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")) {
            return priceService.getLastPricePagebale(name, page, size);
        } else {
            throw new IllegalCurrencyException();
        }
    }

    @GetMapping("cryptocurrencies/csv")
    public void generatePriceListSummary(HttpServletResponse servletResponse) throws IOException {
        priceService.writePricesToCSV(servletResponse);
    }
}
