package com.rpr.demo.todoapp.ToDoApp.controller;

import com.rpr.demo.todoapp.ToDoApp.exceptions.IllegalCurrencyException;
import com.rpr.demo.todoapp.ToDoApp.model.Price;
import com.rpr.demo.todoapp.ToDoApp.model.PriceResultCSV;
import com.rpr.demo.todoapp.ToDoApp.repository.PriceRepository;
import com.rpr.demo.todoapp.ToDoApp.service.PriceService;
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
    public List<Price> getLastPricePagebale(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        if (name.equals("BTC") || name.equals("ETH") || name.equals("XRP")) {
            return priceService.getLastPricePagebale(name, page, size);
        } else {
            throw new IllegalCurrencyException();
        }
    }

    @GetMapping("cryptocurrencies/csv")
    public void generatePriceListSummary(HttpServletResponse servletResponse) throws IOException {
        List<String> uniqueCurrencies = priceRepository.findDistinctCurrencies();
        List<PriceResultCSV> summary = new ArrayList<>();

        for (int i =0; i < uniqueCurrencies.size(); i++) {
            PriceResultCSV temp = new PriceResultCSV(uniqueCurrencies.get(i),
                    priceService.getLastPriceMin(uniqueCurrencies.get(i)).getLprice(),
                    priceService.getLastPriceMax(uniqueCurrencies.get(i)).getLprice());
            summary.add(temp);
        }

        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"priceListSummary.csv\"");
        priceService.writeEmployeesToCsv(servletResponse.getWriter(), summary);
    }
}
