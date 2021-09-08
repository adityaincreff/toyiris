package com.increff.toyiris.controller;


import com.increff.toyiris.dto.StyleDto;
import com.increff.toyiris.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@RestController
@RequestMapping(path = "/api/style")
public class StyleController {
    @Autowired
    private StyleDto styleDto;

    @ApiOperation(value = "Adds styles")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void add(@RequestPart MultipartFile file) throws IOException, ApiException {
        styleDto.add(file);
    }
    @ApiOperation(value="Displays all styles")
    @RequestMapping(method=RequestMethod.GET)
    public void selectAll(HttpServletResponse response) throws IOException{
        styleDto.selectAll(response);
    }
    @ApiOperation(value="Download errors")
    @RequestMapping(path="/errors",method=RequestMethod.GET)
    public void downloadErrors(HttpServletResponse response) throws IOException, ApiException {
        styleDto.downloadErrors(response);
    }


}
