package com.rpr.demo.todoapp.ToDoApp.service;

import com.rpr.demo.todoapp.ToDoApp.model.Price;
import com.rpr.demo.todoapp.ToDoApp.model.PriceResultCSV;
import com.rpr.demo.todoapp.ToDoApp.repository.PriceRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PriceService {

    private MongoTemplate mongoTemplate;
    private PriceRepository priceRepository;

    public PriceService(MongoTemplate mongoTemplate, PriceRepository priceRepository) {
        this.mongoTemplate = mongoTemplate;
        this.priceRepository = priceRepository;
    }

    public Price getLastPrice(String curr1, String curr2) {
        final String uri = "https://cex.io/api/last_price/" + curr1 + "/" + curr2;
        final RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate.getForObject(uri, Price.class);
    }

    public Price getLastPriceMin(String currency_name) {
        Query query = new Query();
        query
                .with(Sort.by(Sort.Direction.ASC, "lprice"))
                .addCriteria(Criteria.where("curr1").is(currency_name));
        Price price = mongoTemplate.findOne(query, Price.class);
        return price;
    }

    public Price getLastPriceMax(String currency_name) {
        Query query = new Query();
        query
                .with(Sort.by(Sort.Direction.DESC, "lprice"))
                .addCriteria(Criteria.where("curr1").is(currency_name));
        Price price = mongoTemplate.findOne(query, Price.class);
        return price;
    }

    public List<Price> getLastPricePagebale(String name, int page, int size) {
        final Pageable pageableRequest = PageRequest.of(page, size);
        Query query = new Query();
        query
                .with(pageableRequest)
                .with(Sort.by(Sort.Direction.ASC, "lprice"))
                .addCriteria(Criteria.where("curr1").is(name));
        List<Price> price = mongoTemplate.find(query, Price.class);
        return price;
    }

    public void writeEmployeesToCsv(Writer writer, List<PriceResultCSV> priceSummary) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (PriceResultCSV price : priceSummary) {
                csvPrinter.printRecord(price.getName(), price.getMinPrice(), price.getMaxPrice());
            }
        } catch (IOException e) {
            throw new IOException();
        }
    }
}
