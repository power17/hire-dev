package com.power.service.impl;


import com.power.mapper.StuMapper;
import com.power.pojo.Stu;
import com.power.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author power
 * @since 2026-03-21
 */
@Service
public class StuServiceImpl  implements StuService {
    @Autowired
    private StuMapper stuMapper;

    public StuServiceImpl(StuMapper stuMapper) {
        this.stuMapper = stuMapper;
    }

    @Override
    public void save(Stu stu) {
        stuMapper.insert(stu);
    }
}
