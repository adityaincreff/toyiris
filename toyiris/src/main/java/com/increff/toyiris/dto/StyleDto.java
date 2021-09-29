package com.increff.toyiris.dto;

import com.increff.toyiris.pojo.StylePojo;
import com.increff.toyiris.service.ApiException;
import com.increff.toyiris.service.StyleService;

import com.increff.toyiris.util.FileUtil;
import com.increff.toyiris.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

@Service
public class StyleDto {
    @Autowired
    private StyleService styleService;

    public void add(@RequestPart MultipartFile file) throws IOException, ApiException {
        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String dataRow = TSVFile.readLine(); //Read First Line
        if (checkFileHeading(dataRow) == false) {
            throw new ApiException("File Headings are not proper");
        }
        boolean errors = scanFileForErrors(file);
        if (errors) {
            throw new ApiException("File contains some errors");
        }
        dataRow = TSVFile.readLine();

        while (dataRow != null) {
            styleService.add(convertRowsToPojo(dataRow));
            dataRow = TSVFile.readLine();


        }
    }

    public boolean scanFileForErrors(MultipartFile file) throws IOException, ApiException {
        BufferedReader TSVFile = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));
        boolean ans = false;
        String dataRow = TSVFile.readLine();
        int rowNumber = 2;
        refreshFile();
        dataRow = TSVFile.readLine();
        FileWriter fos = new FileWriter("files/error-files/style-error.txt", true);
        PrintWriter dos = new PrintWriter(fos);
        while (dataRow != null) {
            try {
                StylePojo dataConverted=convertRowsToPojo(dataRow);
                check(dataConverted);
                //styleService.exists(convertRowsToPojo(dataRow));
            } catch (ApiException e) {
                String x = dataRow + '\t' + e.getMessage();
                dos.println(rowNumber + "\t" + x);
                ans = true;
            }

            rowNumber++;
            dataRow = TSVFile.readLine();

        }
        fos.close();
        return ans;

    }

    private void check(StylePojo dataConverted) throws ApiException {
        if(StringUtil.isEmpty(dataConverted.getStyleCode())||StringUtil.isEmpty(dataConverted.getBrand())||StringUtil.isEmpty(dataConverted.getCategory())||StringUtil.isEmpty(dataConverted.getSubCategory())||StringUtil.isEmpty(String.valueOf(dataConverted.getMrp()))||StringUtil.isEmpty(dataConverted.getGender())){
            throw new ApiException("One or more fields are empty");
        }
    }

    private void refreshFile() throws IOException {
        FileWriter fos = new FileWriter("files/error-files/style-error.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Row Number\tStyle Code\tBrand\tCategory\tSub-Category\tMRP\tGender\tError Message");
        fos.close();
    }

    private boolean checkFileHeading(String dataRow) {
        StringTokenizer st = new StringTokenizer(dataRow, "\t");
        List<String> dataArray = new ArrayList<>();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }
        if (!dataArray.get(0).equals("Style Code") || !dataArray.get(1).equals("Brand") || !dataArray.get(2).equals("Category") || !dataArray.get(3).equals("Sub-Category") || !dataArray.get(4).equals("MRP") || !dataArray.get(5).equals("Gender")) {
            return false;
        }
        return true;
    }

    public void selectAll(HttpServletResponse response) throws IOException {
        List<StylePojo> stylePojo = styleService.selectAll();
        FileWriter fos = new FileWriter("files/downloads/style.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Style Code\tBrand\tCategory\tSub-Category\tMRP\tGender");
        for (StylePojo s : stylePojo) {
            dos.print(s.getStyleCode() + '\t' + s.getBrand() + '\t' + s.getCategory() + '\t' + s.getSubCategory() + '\t' + s.getMrp() + '\t' + s.getGender());
            dos.println();
        }
        fos.close();
        FileUtil.downloadFile("downloads/style", response);
    }

    public void downloadErrors(HttpServletResponse response) throws ApiException, IOException {
    File file=new File("C:\\Users\\user\\IdeaProjects\\toyiris\\files\\error-files\\style-error.txt");
    if(file.exists()==false){
        throw new ApiException("Upload file first");
    }
    FileUtil.downloadFile("error-files/style-error",response);
    }


    private StylePojo convertRowsToPojo(String dataRow) throws ApiException {
        StringTokenizer st = new StringTokenizer(dataRow, "\t");
        StylePojo stylePojo = new StylePojo();
        List<String> dataArray = new ArrayList<String>();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }
        if(dataArray.size()<6)
        {
            throw new ApiException("One or more fields empty.");
        }
        else if(dataArray.size()>6){
            throw new ApiException("Extra fields added");
        }
        else if(!StringUtil.toLowerCaseTrim(dataArray.get(4)).matches(("[+-]?[0-9]+(.[0-9][0-9])?")))
        {
            throw new ApiException("MRP is not a decimal number");
        }else{
        stylePojo.setStyleCode(StringUtil.toLowerCaseTrim(dataArray.get(0)));
        stylePojo.setBrand(StringUtil.toLowerCaseTrim(dataArray.get(1)));
        stylePojo.setCategory(StringUtil.toLowerCaseTrim(dataArray.get(2)));
        stylePojo.setSubCategory(StringUtil.toLowerCaseTrim(dataArray.get(3)));
        stylePojo.setMrp(Double.parseDouble(StringUtil.toLowerCaseTrim(dataArray.get(4))));
        stylePojo.setGender(StringUtil.toLowerCaseTrim(dataArray.get(5)));
        return stylePojo;}
    }
}