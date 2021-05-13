package com.chen.settings.service;

import com.chen.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:44
 */
public interface DicService {

    Map<String, List<DicValue>> getDicValueMap();

}
