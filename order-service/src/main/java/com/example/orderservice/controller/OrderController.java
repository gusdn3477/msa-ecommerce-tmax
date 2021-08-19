package com.example.orderservice.controller;

import com.example.orderservice.client.CatalogServiceClient;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.mq.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseCatalog;
import com.example.orderservice.vo.ResponseOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final CatalogServiceClient catalogServiceClient;
    Gson gson = new Gson();

    @Autowired
    public OrderController(Environment env, OrderService orderService,
                           KafkaProducer kafkaProducer,
                           CatalogServiceClient catalogServiceClient) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.catalogServiceClient = catalogServiceClient;
    }

    @GetMapping("/health_check")
    public String status() {

        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping(value="/orders")
    public String practice(@RequestBody RequestOrder orderDetails) {

        FileWriter writer = null;
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        JsonArray fields = new JsonArray();
        JsonObject type = new JsonObject();
        JsonObject schema = new JsonObject();
        JsonObject obj_total = new JsonObject();
        JsonObject obj_total2 = new JsonObject();
        JsonObject obj_total3 = new JsonObject();
        JsonObject obj_total4 = new JsonObject();
        JsonObject obj_total5 = new JsonObject();
        JsonObject obj_total6 = new JsonObject();
        JsonObject obj_last = new JsonObject();

        JsonObject last = new JsonObject();
        JsonObject payload = new JsonObject();

        type.addProperty("type", "struct");
        obj_total.addProperty("type", "string");
        obj_total.addProperty("optional", "false");
        obj_total.addProperty("field", "product_id");

        obj_total2.addProperty("type", "int32");
        obj_total2.addProperty("optional", "false");
        obj_total2.addProperty("field", "qty");

        obj_total3.addProperty("type", "int32");
        obj_total3.addProperty("optinal", "false");
        obj_total3.addProperty("field", "unit_price");

        obj_total4.addProperty("type", "int32");
        obj_total4.addProperty("optinal", "false");
        obj_total4.addProperty("field", "total_price");

        obj_total5.addProperty("type", "string");
        obj_total5.addProperty("optinal", "false");
        obj_total5.addProperty("field", "user_id");

        obj_total6.addProperty("type", "string");
        obj_total6.addProperty("optinal", "false");
        obj_total6.addProperty("field", "order_id");

        fields.add(obj_total);
        fields.add(obj_total2);
        fields.add(obj_total3);
        fields.add(obj_total4);
        fields.add(obj_total5);
        fields.add(obj_total6);
        schema.addProperty("type", "struct");
        schema.add("fields", fields);
        schema.addProperty("optional", "false");
        schema.addProperty("name", "orders");

        obj_last.addProperty("product_id", "CATALOG-0001");
        obj_last.addProperty("qty", 10);
        obj_last.addProperty("user_price", 1200); // 일단 임의로 하드코딩 했습니다. 추후에 수정하겠습니다.
        obj_last.addProperty("total_price", 12000);
        obj_last.addProperty("user_id", "0e91b563-b635-460b-a801-6248b5e8ac20");
        obj_last.addProperty("order_id", "82bf004a-eee7-47cd-ac86-a44a3fab932c");
        //payload.add("payload", obj_last);

        last.add("schema", schema);
        last.add("payload", obj_last);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(last);
        try {
            writer = new FileWriter("C:\\Users\\Admin\\work\\demo_git\\tmax_msa\\pdf\\gson.json"); // 여기 수정하시면 됩니다.
            writer.write(json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        출처: https://itrh.tistory.com/76 [흑곰푸우]

        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping(value="/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails){

        log.info("Before and order data");
        //check how much stock is left
        //order-service -> catalog-service
        //resttemplate or openfeign

        boolean isAvailable = true;
        ResponseCatalog responseCatalog = catalogServiceClient.getCatalog(orderDetails.getProductId());
        if (responseCatalog != null &&
                responseCatalog.getStock() - orderDetails.getQty() < 0)
            isAvailable = false;

        if (isAvailable) {
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
            orderDto.setUserId(userId);
            OrderDto createdOrder = orderService.createOrder(orderDto);
            ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

            /* send message to Kafka topic */
            kafkaProducer.send("example-catalog-topic", orderDto);

            /* 강사님이 새롭게 추가하라고 하신 부분 */

            log.info("After added orders data");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
        } else {
            log.info("After added orders data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
