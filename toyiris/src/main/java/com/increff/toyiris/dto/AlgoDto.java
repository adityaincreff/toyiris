package com.increff.toyiris.dto;

import com.increff.toyiris.model.InputForm;
import com.increff.toyiris.model.LiquidationData;
import com.increff.toyiris.model.SalesData;
import com.increff.toyiris.pojo.AlgoInputPojo;
import com.increff.toyiris.pojo.SalesPojo;
import com.increff.toyiris.pojo.SkuPojo;
import com.increff.toyiris.pojo.StylePojo;
import com.increff.toyiris.service.*;
import com.increff.toyiris.util.NumberUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AlgoDto {
    @Autowired
    private AlgoService algoService;
    @Autowired
    private SalesService salesService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private ReportService reportService;


    public void addParameters(InputForm inputForm) throws ApiException {
        checkParameters(inputForm);
        AlgoInputPojo inputPojo = convertFormToPojo(inputForm);
        algoService.addParameters(inputPojo);

    }

    private AlgoInputPojo convertFormToPojo(InputForm inputForm) {
        AlgoInputPojo inputs = new AlgoInputPojo();
        inputs.setLiquidationMultiplier(inputForm.getLiquidationMultiplier());
        inputs.setDate(inputForm.getDate());
        inputs.setBadSize(inputForm.getBadSize());
        inputs.setGoodSize(inputForm.getGoodSize());
        return inputs;
    }

    private void checkParameters(InputForm inputForm) throws ApiException {
        if (NumberUtil.greaterThan100(inputForm.getLiquidationMultiplier()) || NumberUtil.greaterThan100(inputForm.getGoodSize()) || NumberUtil.greaterThan100(inputForm.getBadSize())) {
            throw new ApiException("Percentages cannot be greater than 100.");
        }
        if (NumberUtil.negative(inputForm.getLiquidationMultiplier()) || NumberUtil.negative(inputForm.getBadSize()) || NumberUtil.negative(inputForm.getGoodSize())) {
            throw new ApiException("Percentages cannot be negative");
        }
        if (inputForm.getDate().isAfter(LocalDate.now())) {
            throw new ApiException("Cannot run algo for date after today's date");
        }

    }

    public AlgoInputPojo getParameters() throws ApiException {
        return algoService.selectRecent();
    }

    public void algoRun() throws ApiException, IOException {
        AlgoInputPojo input = algoService.selectRecent();
        List<SalesPojo> list = salesService.selectAll();
        List<SalesData> salesData = convertIntoSalesData(list);
        List<SalesData> cleanedSales=liquidationCleanup(salesData,input.getLiquidationMultiplier());


    }

    private List<SalesData> liquidationCleanup(List<SalesData> salesData, double liquidationMultiplier) throws IOException {
    List<SalesData> cleanedSales=new ArrayList<SalesData>();
    HashMap<Pair<String,String>, LiquidationData> liquidationMap=new HashMap<Pair<String,String>,LiquidationData>();
    for(SalesData sale : salesData){
        Pair<String,String> catSubCatKey=new Pair<String,String>(sale.getCategory(),sale.getSubCategory());
        LiquidationData data = liquidationMap.computeIfAbsent(catSubCatKey, o -> new LiquidationData(sale.getCategory(), sale.getSubCategory()));
        data

    }
    }


    private List<SalesData> convertIntoSalesData(List<SalesPojo> list) {
     List<SalesData> converted=new ArrayList<SalesData>();
     HashMap<Integer,SkuPojo> skuMap=skuService.selectAllMap();
     HashMap<Integer, StylePojo> styleMap=styleService.selectAllMap();
     for(SalesPojo p: list){
         SkuPojo skuPojo=skuMap.get(p.getSkuId());
         SalesData data=new SalesData();
         data.setQuantity(p.getQuantity());
         data.setDate(p.getDate());
         data.setDiscount(p.getDiscount() * 100 / (p.getRevenue() + p.getDiscount()));
         data.setSize(skuPojo.getSize());
         data.setRevenue(p.getRevenue());
         data.setCategory(styleMap.get(skuPojo.getStyleId()).getCategory());
         data.setSubCategory(styleMap.get(skuPojo.getStyleId()).getSubCategory());
         data.setStyleCode(styleMap.get(skuPojo.getStyleId()).getStyleCode());
         converted.add(data);

     }
         return converted;
    }
}
