package com.example.partnermatchingsystem.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.partnermatchingsystem.model.Tag;
import com.example.partnermatchingsystem.mapper.TagMapper;
import com.example.partnermatchingsystem.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author zyf
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-02-06 21:24:39
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag> implements TagService{

}




