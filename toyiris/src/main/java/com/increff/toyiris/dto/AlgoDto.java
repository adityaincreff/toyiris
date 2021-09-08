package com.increff.toyiris.dto;

import com.increff.toyiris.model.InputForm;
import com.increff.toyiris.pojo.AlgoInputPojo;
import com.increff.toyiris.service.AlgoService;
import com.increff.toyiris.service.ApiException;
import com.increff.toyiris.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AlgoDto {
    @Autowired
    private AlgoService algoService;

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
        if(inputForm.getDate().isAfter(LocalDate.now()))
        {
            throw new ApiException("Cannot run algo for date after today's date");
        }

    }

    public AlgoInputPojo getParameters()throws ApiException {
    return algoService.selectRecent();
    }

    public void algoRun() {
    }
}
