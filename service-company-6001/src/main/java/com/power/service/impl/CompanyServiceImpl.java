package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.power.mapper.CompanyMapper;
import com.power.pojo.Company;
import com.power.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyMapper companyMapper;
    @Override
    public Company getByFullName(String fullName) {
        Company tempCompany = companyMapper.selectOne(
                new QueryWrapper<Company>()
                        .eq("company_name", fullName)
        );

        return tempCompany;
    }
}
