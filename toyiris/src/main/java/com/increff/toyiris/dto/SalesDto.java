package com.increff.toyiris.dto;

import com.increff.toyiris.pojo.SalesPojo;
import com.increff.toyiris.pojo.SkuPojo;
import com.increff.toyiris.pojo.StorePojo;
import com.increff.toyiris.service.*;
import com.increff.toyiris.util.DatatypeConversion;
import com.increff.toyiris.util.DateUtil;
import com.increff.toyiris.util.FileUtil;
import com.increff.toyiris.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class SalesDto {
    @Autowired
    private SalesService salesService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StyleService styleService;

    public void add(@RequestPart MultipartFile file) throws IOException, ApiException {
        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String dataRow = TSVFile.readLine();
        if (checkFileHeading(dataRow) == false) {
            throw new ApiException("File orientation is not correct.");
        }
        boolean errors = scanFileForErrors(file);
        if (errors == true) {
            throw new ApiException("File contains some errors");
        }
        dataRow = TSVFile.readLine();
        // for(StorePojo t:storeService.selectAll())
        //{
        // for(SkuPojo s:skuService.selectAll()){
        //    for(LocalDate date=LocalDate.now();date.isAfter(LocalDate.now().minusDays(60));date=date.minusDays(1)){
        //      SalesPojo salesPojo=new SalesPojo();
        //     salesPojo.setDate(date);
        //  salesPojo.setRevenue(1000+Math.random()*4000);
        //salesPojo.setDiscount(Math.random()*salesPojo.getRevenue());
        // salesPojo.setQuantity((int) (1+Math.random()*15));
        //salesPojo.setStoreId(t.getId());
        //salesPojo.setSkuId(s.getId());
        //salesService.add(salesPojo);

        ///}


        //    }
        //}
        salesService.delete(true);
        while (dataRow != null) {
            salesService.add(convertRowsToPojo(dataRow));
            dataRow = TSVFile.readLine();
        }
    }

    private SalesPojo convertRowsToPojo(String dataRow) throws ApiException {
        StringTokenizer st = new StringTokenizer(dataRow);
        List<String> dataArray = new ArrayList<String>();
        SalesPojo salesPojo = new SalesPojo();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }

        if (dataArray.size() < 6) {
            throw new ApiException("One or more fields are empty");
        } else if (dataArray.size() > 6) {
            throw new ApiException("Extra fields are added.");
        } else if (!DateUtil.validateData(StringUtil.toLowerCaseTrim(dataArray.get(0)))) {
            throw new ApiException("Date is Invalid");

        } else if (!StringUtil.toLowerCaseTrim(dataArray.get(3)).matches("-?(0|[1-9]\\d*)")) {
            throw new ApiException("Quantity is not integer");
        } else if (Integer.parseInt(StringUtil.toLowerCaseTrim(dataArray.get(3))) <= 0) {
            throw new ApiException("Quantity cannot be negative or zero");
        } else if (!StringUtil.toLowerCaseTrim(dataArray.get(4)).matches("[+-]?[0-9]+(.[0-9][0-9]?)?")) {
            throw new ApiException("Discount is not a decimal number");
        } else if (Double.parseDouble(StringUtil.toLowerCaseTrim(dataArray.get(4))) < 0) {
            throw new ApiException("Discount cannot be less than 0");
        } else if (!StringUtil.toLowerCaseTrim(dataArray.get(5)).matches("[+-]?[0-9]+(.[0-9][0-9]?)?")) {
            throw new ApiException("Revenue is not a decimal number");
        } else if (Double.parseDouble(StringUtil.toLowerCaseTrim(dataArray.get(5))) < 0) {
            throw new ApiException("Revenue cannot be less than 0");
        } else {
            salesPojo.setDate(convertStringToDate(dataArray.get(0)));
            salesPojo.setSkuId(skuService.select(StringUtil.toLowerCaseTrim(dataArray.get(1))));
            salesPojo.setStoreId(storeService.select(StringUtil.toLowerCaseTrim(dataArray.get(2))));
            salesPojo.setQuantity(DatatypeConversion.convertStringToInteger(dataArray.get(3)));
            salesPojo.setDiscount(DatatypeConversion.convertStringToDouble(dataArray.get(4)));
            salesPojo.setRevenue(DatatypeConversion.convertStringToDouble(dataArray.get(5)));
            return salesPojo;

        }
    }

    private LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }

    private boolean scanFileForErrors(MultipartFile file) throws IOException {
        BufferedReader TSVFile = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));
        boolean ans = false;
        String dataRow = TSVFile.readLine();
        int rowNumber = 2;
        refreshFile();
        dataRow = TSVFile.readLine();
        FileWriter fos = new FileWriter("files/error-files/sales-error.txt", true);
        PrintWriter dos = new PrintWriter(fos);
        while (dataRow != null) {
            try {
                SalesPojo dataConverted = convertRowsToPojo(dataRow);
                check(dataConverted);
                //salesService.exists(dataConverted);

            } catch (ApiException e) {
                dos.println(rowNumber + "\t" + dataRow + "\t" + e.getMessage());
                ans = true;
            }
            rowNumber++;
            dataRow = TSVFile.readLine(); //READ NEXT LINE
        }
        fos.close();
        return ans;
    }

    private void check(SalesPojo dataConverted) throws ApiException {
        if (dataConverted.getDate().isAfter(LocalDate.now())) {
            throw new ApiException("Sales date cannot be greater than today's date");
        }
        if (dataConverted.getRevenue() < 0 || dataConverted.getDiscount() < 0 || dataConverted.getQuantity() < 0) {
            throw new ApiException("Revenue,Discount or Quantity is negative");
        }
    }

    private void refreshFile() throws IOException {
        FileWriter fos = new FileWriter("files/error-files/sales-error.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Row Number\tDate\tSKU\tBranch\tQuantity\tDiscount\tRevenue\tError Message");
        fos.close();
    }

    private boolean checkFileHeading(String dataRow) {
        StringTokenizer st = new StringTokenizer(dataRow, "\t");
        List<String> dataArray = new ArrayList<String>();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }
        if (!dataArray.get(2).equals("Branch") || !dataArray.get(1).equals("SKU") || !dataArray.get(3).equals("Quantity") || !dataArray.get(0).equals("Date") || !dataArray.get(4).equals("Discount") || !dataArray.get(5).equals("Revenue")) {
            return false;
        }
        return true;
    }

    public void downloadErrors(HttpServletResponse response) throws ApiException, IOException {
        File file = new File("C:\\Users\\user\\IdeaProjects\\toyiris\\files\\error-files\\sku-error.txt");
        if (file.exists() == false) {
            throw new ApiException("Upload a file first.");

        }
        FileUtil.downloadFile("error-files/sales-error", response);
    }

    public void selectAll(HttpServletResponse response) throws IOException {
        List<SalesPojo> salesPojo = salesService.selectAll();
        FileWriter fos = new FileWriter("files/downloads/sku.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Date\tSku Code\tStore\tQuantity\tDiscount\tRevenue");
        for (SalesPojo s : salesPojo) {
            dos.println(s.getDate().getDayOfMonth() + "/" + s.getDate().getMonthValue() + "/" + s.getDate().getYear() + '\t' + skuService.selectById(s.getSkuId()) + '\t' + storeService.selectById(s.getStoreId()) + '\t' + s.getQuantity() + '\t' + s.getDiscount() + '\t' + s.getRevenue());
        }
        fos.close();
        FileUtil.downloadFile("downloads/sku", response);

    }
}
