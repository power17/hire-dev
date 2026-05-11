package com.power.controller;

import com.power.pojo.Company;
import com.power.result.GraceJsonResult;
import com.power.service.CompanyService;
import com.power.vo.CompanySimpleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("getByFullName")
    public GraceJsonResult getByFullName(String fullName) {

        if (StringUtils.isBlank(fullName)) {
            return GraceJsonResult.error();
        }

        Company company = companyService.getByFullName(fullName);
        if (company == null) return GraceJsonResult.ok(null);

        CompanySimpleVO companySimpleVO = new CompanySimpleVO();
        BeanUtils.copyProperties(company, companySimpleVO);

        return GraceJsonResult.ok(companySimpleVO);
    }
}
