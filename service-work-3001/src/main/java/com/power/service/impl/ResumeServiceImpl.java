package com.power.service.impl;

import com.power.mapper.ResumeMapper;
import com.power.pojo.Resume;
import com.power.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeMapper resumeMapper;

    @Transactional
    @Override
    public void initResume(String userId){
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdatedTime(LocalDateTime.now());
        // 模拟除零异常
//        int a = 1 / 0;

        resumeMapper.insert(resume);
    }


}
