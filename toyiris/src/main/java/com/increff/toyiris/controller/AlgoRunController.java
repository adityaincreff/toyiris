package com.increff.toyiris.controller;

import com.increff.toyiris.dto.AlgoDto;
import com.increff.toyiris.model.InputForm;
import com.increff.toyiris.pojo.AlgoInputPojo;
import com.increff.toyiris.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api
@RestController
@RequestMapping(path = "/api/algo")
public class AlgoRunController {
    @Autowired
    private AlgoDto algoDto;

    @ApiOperation(value = "Adds the parameters")
    @RequestMapping(path = "/input", method = RequestMethod.POST)
    public void addParameters(@RequestBody InputForm inputForm) throws ApiException {
        System.out.println(inputForm.getLiquidationMultiplier());
        System.out.println(inputForm.getDate());
        System.out.println(inputForm.getBadSize());
        System.out.println(inputForm.getGoodSize());
        algoDto.addParameters(inputForm);
    }

    @ApiOperation(value = "Retreives the parameters")
    @RequestMapping(path = "/input", method = RequestMethod.GET)
    public AlgoInputPojo getParameters() throws ApiException {
        return algoDto.getParameters();
    }

    @ApiOperation(value = "Runs the Algorithm")
    @RequestMapping(method = RequestMethod.GET)
    public void runAlgo() throws ApiException, IOException {
        algoDto.algoRun();
    }
}
