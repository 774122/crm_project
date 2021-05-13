package com.chen.settings.service.UserServiceImp;

import com.chen.settings.dao.DicTypeDao;
import com.chen.settings.dao.DicValueDao;
import com.chen.settings.domain.DicType;
import com.chen.settings.domain.DicValue;
import com.chen.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:44
 */
@Service
public class DicServiceImpl implements DicService {
    @Autowired
    private DicTypeDao dicTypeDao;
    @Autowired
    private DicValueDao dicValueDao;


    @Override
    public Map<String, List<DicValue>> getDicValueMap() {

        Map<String, List<DicValue>> map = new HashMap<>();
        List<DicType> dicTypes = dicTypeDao.getAllType();

        // 遍历每一个类型
        for(DicType dicType : dicTypes){
            // 根据dicType从dic_Value中取值
            List<DicValue> dicValues = dicValueDao.getListByCode(dicType.getCode());
            map.put(dicType.getCode()+"List", dicValues);
        }

        return map;
    }
}
