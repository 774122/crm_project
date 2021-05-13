package com.chen.workbench.service.Impl;

import com.chen.utils.DateTimeUtil;
import com.chen.utils.UUIDUtil;
import com.chen.vo.PageNationVo;
import com.chen.workbench.dao.*;
import com.chen.workbench.domain.*;
import com.chen.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:26
 */
@Service
public class ClueServiceImpl implements ClueService {
    // 线索相关表
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;

    // 客户相关表
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    // 联系人相关表
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    // 交易相关表
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;


    @Override
    public Map<String, Boolean> saveClue(Clue clue) {
        Map<String,Boolean> map = new HashMap<>();
        int num = clueDao.saveClue(clue);
        if(num==1){
            map.put("success",true);
        }else {
            map.put("success",false);
        }

        return null;
    }

    @Override
    public PageNationVo<Clue> selectPageClue(String index,String pageSize, Clue clue) {
        Integer pageNo = Integer.valueOf(index);
        Integer pageSize2 = Integer.valueOf(pageSize);
        //查询总条数
        int totalClueNum = clueDao.askTotalClueNumByCondition(clue);
        int totalPageNum = totalClueNum%pageSize2==0 ? totalClueNum/pageSize2 : totalClueNum/pageSize2+1;
        int begin = (pageNo-1)*pageSize2;
        Map<String,Object> map = new HashMap<>();
        map.put("index",begin);
        map.put("pageSize",pageSize2);
        map.put("fullname",clue.getFullname());
        map.put("owner",clue.getOwner());
        map.put("company",clue.getCompany());
        map.put("phone",clue.getPhone());
        map.put("mphone",clue.getMphone());
        map.put("state",clue.getState());
        map.put("source",clue.getSource());
        List<Clue> clues = clueDao.askClueByCondition(map);

        PageNationVo<Clue> vo = new PageNationVo<>();
        vo.setTotal(totalClueNum);
        vo.setDataList(clues);

        return vo;
    }

    @Override
    public Clue getOneClue(String id) {
        Clue clue = clueDao.getOneClueById(id);
        return clue;
    }

    @Override
    public List<Activity> selectActivityListByClueId(String clueId) {

        List<Activity> list = clueDao.selectActivity(clueId);

        return list;
    }

    @Override
    public Map<String, Boolean> deleteRelation(String car_id) {
        int num = clueDao.deleteRelationById(car_id);
        Map<String,Boolean> map = new HashMap<>();
        if(num==1){
            map.put("success",true);
        }else{
            map.put("success",false);
        }
        return map;
    }

    @Override
    public Map<String, String> addRelation(String clueId, List<String> list) {
        Map<String,String> map = new HashMap<>();
        Map<String,String> val = new HashMap<>();
        int count = 0;
        for(String activityId : list){
            val.put("id", UUIDUtil.getUUID());
            val.put("clueId",clueId);
            val.put("activityId",activityId);
            int num = clueDao.addActRelation(val);
            count += num;
        }
        if(count==list.size()){
            map.put("success","关联添加成功!");
        }else{
            map.put("success","关联添加失败！");
        }
        return map;
    }

    // 重心：线索转换
    @Override
    public boolean convert(String clueId, Tran tran,String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        // 1.通过线索ID获取线索对象，获取该线索的信息
        Clue clue = clueDao.getById(clueId);

        //2.通过线索对象，提取客户信息，客户不存在时新建客户，（根据公司名称精确匹配，判断客户是否存在）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);//若customer为空，说明以前没有这个客户，新建
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setDescription(clue.getDescription());
            // 添加客户
            int num = customerDao.addCustomer(customer);
            if(num!=1) flag = false;
        }

        // 3.通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setCustomerId(customer.getId());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
            // 添加联系人
        int count = contactsDao.addContact(contacts);
        if(count!=1) flag = false;

        // 4.将线索的备注转换到客户备注以及联系人的备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getClueListById(clueId);
        for(ClueRemark cr : clueRemarks){
            // 取出备注信息
            String noteContent = cr.getNoteContent();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            int count2 = customerRemarkDao.addCustomerRemark(customerRemark);
            if(count2!=1) flag = false;

            // 创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(customer.getId());
            contactsRemark.setEditFlag("0");
            int count3 = contactsRemarkDao.addContactsRemark(contactsRemark);
            if(count3!=1) flag = false;
        }

        // 5.线索和市场活动的关系转移到联系人和市场活动的关系
            //查询出与该条线索关联的活动
        List<ClueActivityRelation> cars = clueActivityRelationDao.getRelationListByClueId(clueId);
        for(ClueActivityRelation car : cars){
            // 遍历每一条关联关系
            String activityId = car.getActivityId();

            // 创建联系人与市场活动的关系
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            // 添加
            int count4 = contactsActivityRelationDao.addContactsActivityRelation(contactsActivityRelation);
            if(count4!=1) flag = false;
        }

        // 6.如果有创建交易的需求，创建一条交易
        if(tran!=null){
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            // 添加交易
            int count5 = tranDao.addTran(tran);
            if(count5!=1) flag = false;

        // 7.如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            // 添加交易历史
            int count6 = tranHistoryDao.addHistory(tranHistory);
            if(count6!=1) flag = false;
        }

        // 8.删除线索备注
        for(ClueRemark cr : clueRemarks){
            int count7 = clueRemarkDao.delete(cr);
            if(count7!=1) flag = false;
        }

        // 9.删除线索和市场活动的关系
        for(ClueActivityRelation car : cars){
            int count8 = clueActivityRelationDao.delete(car);
            if(count8!=1) flag = false;
        }

        // 10.删除线索
        int count9 = clueDao.delete(clueId);
        if(count9!=1) flag = false;


        return flag;
    }

}
