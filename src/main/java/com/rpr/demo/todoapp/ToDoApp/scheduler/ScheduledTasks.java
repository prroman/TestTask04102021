package com.rpr.demo.todoapp.ToDoApp.scheduler;

import com.rpr.demo.todoapp.ToDoApp.repository.PriceRepository;
import com.rpr.demo.todoapp.ToDoApp.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private PriceService priceService;
    private PriceRepository priceRepository;

    @Autowired
    public ScheduledTasks(PriceService priceService, PriceRepository priceRepository) {
        this.priceService = priceService;
        this.priceRepository = priceRepository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void getLastPrices() {
        priceRepository.save(priceService.getLastPrice("BTC", "USD"));
        priceRepository.save(priceService.getLastPrice("ETH", "BTC"));
        priceRepository.save(priceService.getLastPrice("XRP", "USD"));
        System.out.println("Prices saved to db");
    }
}
