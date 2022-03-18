package com.wh.crm.settings.service.impl;

import com.wh.crm.settings.domain.DicValue;
import com.wh.crm.settings.mapper.DicValueMapper;
import com.wh.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/11  15:24
 * @description
 */
@Service
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueByDiCType(String type) {

        return dicValueMapper.queryDicValueByDiCType(type);
    }
}
