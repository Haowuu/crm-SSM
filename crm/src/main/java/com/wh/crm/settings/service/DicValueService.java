package com.wh.crm.settings.service;

import com.wh.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {

    public List<DicValue> queryDicValueByDiCType(String type);

}
