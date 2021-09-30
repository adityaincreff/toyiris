package com.increff.toyiris.dto;

import com.increff.toyiris.pojo.StorePojo;
import com.increff.toyiris.service.ApiException;
import com.increff.toyiris.service.StoreService;
import com.increff.toyiris.util.FileUtil;
import com.increff.toyiris.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
@Service
public class StoreDto {
    @Autowired
    private StoreService storeService;



    public void add(@RequestPart MultipartFile file) throws IOException, ApiException {

        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String dataRow = TSVFile.readLine();
        if (checkFileHeading(dataRow) == false) {
            throw new ApiException("File orientation is not correct");
        }
        boolean errors = scanFileForErrors(file);
        if (errors) {
            throw new ApiException("File contains some errors");
        }
        dataRow = TSVFile.readLine();
        while (dataRow != null) {
            storeService.add(convertRowsToPojo(dataRow));
            dataRow = TSVFile.readLine();
        }


    }

    private StorePojo convertRowsToPojo(String dataRow) throws ApiException {
        StringTokenizer st = new StringTokenizer(dataRow);
        StorePojo storePojo = new StorePojo();
        List<String> dataArray = new ArrayList<>();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }
        if(dataArray.size()<2){
            throw new ApiException("One or more fields empty");
        }
        else if(dataArray.size()>2){
            throw new ApiException("Extra values added");
        }else {
            storePojo.setBranch(StringUtil.toLowerCaseTrim(dataArray.get(0)));
            storePojo.setCity(StringUtil.toLowerCaseTrim(dataArray.get(1)));
            return storePojo;

        }}

    private boolean scanFileForErrors(MultipartFile file) throws IOException, ApiException {
        BufferedReader TSVFile = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));
        boolean ans = false;
        String dataRow = TSVFile.readLine();
        int rowNumber = 2;
        refreshFile();
        dataRow= TSVFile.readLine();
        FileWriter fos = new FileWriter("files/error-files/store-error.txt", true);
        PrintWriter dos = new PrintWriter(fos);
        while (dataRow != null) {
            try {
                StorePojo dataConverted = convertRowsToPojo(dataRow);
                dataConverted = normalize(dataConverted);
                check(dataConverted);
               // storeService.exists(dataConverted);

            } catch (ApiException e) {
                String x = dataRow + "\t" + e.getMessage();
                dos.println(rowNumber + "\t" + dataRow + "\t" + e.getMessage());
                ans = true;

            }
            rowNumber++;
            dataRow = TSVFile.readLine();
        }
        fos.close();
        return ans;


    }

    private void check(StorePojo dataConverted) throws ApiException {
        if(StringUtil.isEmpty(dataConverted.getBranch())|| StringUtil.isEmpty(dataConverted.getCity()))
        {
            throw new ApiException("One or more fields are empty");
        }
    }

    public StorePojo normalize(StorePojo dataConverted) {
        dataConverted.setCity(StringUtil.toLowerCaseTrim(dataConverted.getCity()));
        dataConverted.setBranch(StringUtil.toLowerCaseTrim(dataConverted.getBranch()));
        return dataConverted;
    }

    private void refreshFile() throws IOException {
        FileWriter fos = new FileWriter("files/error-files/store-error.txt", false);
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Row Number\tBranch\tCity\tError Message");
        fos.close();
    }

    private boolean checkFileHeading(String dataRow) {
        StringTokenizer st = new StringTokenizer(dataRow,"\t");
        List<String> dataArray = new ArrayList<>();
        while (st.hasMoreElements()) {
            dataArray.add(st.nextElement().toString());
        }
        if (!dataArray.get(0).equals("Branch") || !dataArray.get(1).equals("City")) {
            return false;
        }
        return true;

    }

    public void downloadErrors(HttpServletResponse response) throws IOException, ApiException {
        File file = new File("C:\\Users\\user\\IdeaProjects\\toyiris\\files\\error-files\\store-error.txt");
        if (file.exists() == false) {
            throw new ApiException("Upload a file first");
        }
        FileUtil.downloadFile("error-files/store-error", response);
    }

    public void selectAll(HttpServletResponse response) throws IOException {
        List<StorePojo> storePojo = storeService.selectAll();
        FileWriter fos = new FileWriter("files/downloads/store.txt");
        PrintWriter dos = new PrintWriter(fos);
        dos.println("Branch\tCity");
        for (StorePojo s : storePojo) {
            dos.print(s.getBranch() + '\t' + s.getCity());
            dos.println();
        }
        fos.close();
        FileUtil.downloadFile("downloads/store", response);
    }
}
