package com.increff.toyiris.controller;


import com.increff.toyiris.dto.SkuDto;
import com.increff.toyiris.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@RestController
@RequestMapping(path = "/api/sku")
public class SkuController {
    @Autowired
    private SkuDto skuDto;

    @ApiOperation(value="Adds styles")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void add(@RequestPart MultipartFile file) throws IOException,ApiException {
        skuDto.add(file);
    }
    @ApiOperation(value="Display all SKU")
    @RequestMapping(method=RequestMethod.GET)
    public void selectALL(HttpServletResponse response)throws IOException{
        skuDto.selectALL(response);
    }
    @ApiOperation(value="Download errors")
    @RequestMapping(path="/errors",method = RequestMethod.GET)
    public void downloadErrors(HttpServletResponse response)throws ApiException,IOException{
        skuDto.downloadErrors(response);
    }

}
