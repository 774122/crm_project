package com.chen.settings.dao;

import com.chen.settings.domain.DicValue;

import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:42
 */
public interface DicValueDao {

    List<DicValue> getListByCode(String dicType);

}
