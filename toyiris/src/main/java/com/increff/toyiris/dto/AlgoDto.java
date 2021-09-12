package com.increff.toyiris.dto;

import com.increff.toyiris.model.*;
import com.increff.toyiris.pojo.*;
import com.increff.toyiris.service.*;
import com.increff.toyiris.util.DateUtil;
import com.increff.toyiris.util.NumberUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        noosReport(cleanedSales,input.getDate());
        goodSizes(cleanedSales,input.getGoodSize(),input.getBadSize());
    }

    private void goodSizes(List<SalesData> cleanedSales, double goodSize, double badSize) {

        //key->category,subCategory
        //value->sizeIdentificationData
        HashMap<Pair<String,String>, SizeIdentificationData> clubbing=new HashMap<Pair<String,String>,SizeIdentificationData>();

        //1.Aggregate revenue at category subcategory level
        for(SalesData salesData:cleanedSales){
            Pair<String, String> catSubCat = new Pair<String, String>(salesData.getCategory(), salesData.getSubCategory());
            SizeIdentificationData helper = clubbing.computeIfAbsent(catSubCat,o-> new SizeIdentificationData());
            helper.setCategory(salesData.getCategory());
            helper.setSubCategory(salesData.getSubCategory());
            helper.setRevenue(salesData.getRevenue());

        }
        //{Cat,SubCat,Size} -> GoodSizePojo
        HashMap<Pair<String, Pair<String, String>>, GoodSizePojo> finalIdentification = new HashMap<Pair<String, Pair<String, String>>, GoodSizePojo>();

        //Calculate percentage of each size
        for(SalesData salesData:cleanedSales){
            Pair<String,String>catSubCat=new Pair<String,String>(salesData.getCategory(), salesData.getSubCategory());
            Pair<String, Pair<String, String>> sizeCat = new Pair<String, Pair<String, String>>(salesData.getSize(), catSubCat);
            GoodSizePojo helper = finalIdentification.computeIfAbsent(sizeCat,o-> new GoodSizePojo());
            helper.setCategory(salesData.getCategory());
            helper.setSize(salesData.getSize());
            helper.setSubcategory(salesData.getSubCategory());
            double addition=salesData.getRevenue()*100/clubbing.get(catSubCat).getRevenue();
            helper.setRevContri(helper.getRevContri()+addition);
            if(helper.getRevContri() >=goodSize){
                helper.setTypeOfSizes("Good Size");
            }
            else if(helper.getRevContri() <=badSize){
                helper.setTypeOfSizes("Bad Size");
            }
            else{
                helper.setTypeOfSizes("Average Size");
            }
            finalIdentification.put(sizeCat,helper);


        }

        reportService.deleteIdentification();
        for (Map.Entry mapElement : finalIdentification.entrySet()) {
            GoodSizePojo helper = (GoodSizePojo) mapElement.getValue();
            reportService.addIdentification(helper);
        }


    }

    private void noosReport(List<SalesData> cleanedSales, LocalDate input) throws IOException {
        //key->category
        HashMap<String, NoosData> noosCategoryDataMap=new HashMap<>();
        //key->style
        HashMap<String,NoosData> noosStyleDataMap=new HashMap<String,NoosData>();
        cleanedSales = cleanedSales.stream().filter(sale -> sale.getDate().isBefore(input)).filter(sale -> sale.getDate().isAfter(input.minusMonths(6))).collect(Collectors.toList());

        //aggregate revenue to category level.
        //first sale day and last day for each category
        for(SalesData salesData:cleanedSales){
            NoosData noosData = noosCategoryDataMap.computeIfAbsent(salesData.getCategory(), o -> new NoosData());
            noosData.setRevenue(noosData.getRevenue()+ salesData.getRevenue());
            noosData.setFirstSaleDay(getFirstSaleDay(noosData,salesData));
            noosData.setLastSaleDay(getLastSaleDay(noosData,salesData));
            NoosData noosStyle=noosStyleDataMap.computeIfAbsent(salesData.getStyleCode(), o-> new NoosData());
            noosStyle.setRevenue(noosStyle.getRevenue()+salesData.getRevenue());
            noosStyle.setFirstSaleDay(getFirstSaleDay(noosStyle,salesData));
            noosStyle.setLastSaleDay(getLastSaleDay(noosStyle,salesData));
        }
        HashMap<String,NoosPojo> categoryNoos=new HashMap<String,NoosPojo>();
        // For category ROS
        // 1. calculate style rev contri
        // 2. calculate style ros
        for (SalesData salesData : cleanedSales) {
            NoosPojo noosPojo = categoryNoos.computeIfAbsent(salesData.getCategory(),o-> new NoosPojo());
            double addition = salesData.getRevenue() * 100 / noosCategoryDataMap.get(salesData.getCategory()).getRevenue();
            noosPojo.setStyleRevContri(noosPojo.getStyleRevContri() + addition);
            double additionInRos = salesData.getQuantity() / (DateUtil.differenceInDays(noosCategoryDataMap.get(salesData.getCategory()).getFirstSaleDay(), noosCategoryDataMap.get(salesData.getCategory()).getLastSaleDay())+1);
            noosPojo.setStyleRos(noosPojo.getStyleRos() + additionInRos);
            noosPojo.setCategory(salesData.getCategory());
            noosPojo.setStyleCode(salesData.getStyleCode());
        }

        HashMap< String, NoosPojo> finalNoos = new HashMap< String, NoosPojo>();
        // 1. calculate style rev contri
        // 2. calculate style ros
        for (SalesData salesData : cleanedSales) {
            NoosPojo noosPojo = finalNoos.computeIfAbsent(salesData.getStyleCode(),o-> new NoosPojo());
            double addition = salesData.getRevenue() * 100 / noosCategoryDataMap.get(salesData.getCategory()).getRevenue();
            noosPojo.setStyleRevContri(noosPojo.getStyleRevContri() + addition);
            double additionInRos = salesData.getQuantity() / (DateUtil.differenceInDays(noosStyleDataMap.get(salesData.getStyleCode()).getFirstSaleDay(), noosStyleDataMap.get(salesData.getStyleCode()).getLastSaleDay())+1);
            noosPojo.setStyleRos(noosPojo.getStyleRos() + additionInRos);
            noosPojo.setCategory(salesData.getCategory());
            noosPojo.setStyleCode(salesData.getStyleCode());
        }

        FileWriter fos = new FileWriter("files/algo-files/NOOS_Report.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        reportService.deleteNoos();
        for (Map.Entry mapElement : finalNoos.entrySet()) {
            NoosPojo helper = (NoosPojo) mapElement.getValue();
            if(helper.getStyleRos() >= categoryNoos.get(helper.getCategory()).getStyleRos()){
                dos.println(helper.getCategory()+"\t"+helper.getStyleCode()+"\t"+helper.getStyleRevContri()+"\t"+helper.getStyleRos());
                reportService.addNoos(helper);
            }
        }
        fos.close();
    }







    private LocalDate getFirstSaleDay(NoosData noosData, SalesData salesData) {
        if (noosData.getFirstSaleDay() == null)
            return salesData.getDate();

        return salesData.getDate().isBefore(noosData.getFirstSaleDay()) ? salesData.getDate() : noosData.getFirstSaleDay();
    }
    private LocalDate getLastSaleDay(NoosData noosData, SalesData salesData) {
        if (noosData.getLastSaleDay() == null)
            return salesData.getDate();

        return salesData.getDate().isAfter(noosData.getLastSaleDay()) ? salesData.getDate() : noosData.getLastSaleDay();
    }


    private List<SalesData> liquidationCleanup(List<SalesData> salesData, double liquidationMultiplier) throws IOException {
    List<SalesData> cleanedSales=new ArrayList<SalesData>();
    HashMap<Pair<String,String>, LiquidationData> liquidationMap=new HashMap<Pair<String,String>,LiquidationData>();
    for(SalesData sale : salesData){
        Pair<String,String> catSubCatKey=new Pair<String,String>(sale.getCategory(),sale.getSubCategory());
        LiquidationData data = liquidationMap.computeIfAbsent(catSubCatKey, o -> new LiquidationData(sale.getCategory(), sale.getSubCategory()));
        data.setAvgDiscount(((sale.getQuantity() * sale.getDiscount()) + (data.getAvgDiscount() * data.getQuantity())) / (sale.getQuantity() + data.getQuantity()));
        data.setRevenue(data.getRevenue()+sale.getRevenue());
        data.setQuantity(data.getQuantity() + sale.getQuantity());

    }
    for(SalesData sale:salesData){
        Pair<String,String> catSubCat=new Pair<String,String>(sale.getCategory(),sale.getSubCategory());
        LiquidationData catSubCatData=liquidationMap.get(catSubCat);
        if(sale.getDiscount() <= catSubCatData.getAvgDiscount()){
            cleanedSales.add(sale);
            catSubCatData.setQuantity(catSubCatData.getQuantity());
        } else{
            catSubCatData.setAvgCleanedDiscount(((sale.getDiscount() * sale.getQuantity()) + (catSubCatData.getCleanedQuantity() * catSubCatData.getAvgCleanedDiscount())) / (sale.getQuantity() + catSubCatData.getCleanedQuantity()));
            catSubCatData.setCleanedQuantity(catSubCatData.getCleanedQuantity() + sale.getQuantity());
            catSubCatData.setCleanedRevenue(sale.getRevenue() + catSubCatData.getCleanedRevenue());
        }
        liquidationMap.put(catSubCat,catSubCatData);
    }
        FileWriter fos = new FileWriter("files/algo-files/Liquidation_Cleanup_Report.txt", false);
        PrintWriter dos=new PrintWriter(fos);
        reportService.deleteLiquidation();
        for (Map.Entry mapElement : liquidationMap.entrySet()) {
            LiquidationData helper = (LiquidationData) mapElement.getValue();
            dos.println(helper.getCategory() + '\t' + helper.getSubCategory() + '\t' + helper.getCleanedRevenue() + "\t" + helper.getAvgCleanedDiscount());
            reportService.addLiquidation(convertDataToPojo(helper));
        }
        fos.close();
        return cleanedSales;
    }

    private LiquidationPojo convertDataToPojo(LiquidationData data) {
        LiquidationPojo converted = new LiquidationPojo();
        converted.setCategory(data.getCategory());
        converted.setSubcategory(data.getSubCategory());
        converted.setAvgDiscount(data.getAvgDiscount());
        converted.setAvgDiscountAfterCleanup(data.getAvgCleanedDiscount());
        converted.setRevenueCleanup(data.getCleanedRevenue());
        return converted;
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
