package com.power.controller.service.impl;


import com.power.controller.mapper.StuMapper;
import com.power.controller.pojo.Stu;
import com.power.controller.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void save(Stu stu) {
        stuMapper.insert(stu);
    }
}
