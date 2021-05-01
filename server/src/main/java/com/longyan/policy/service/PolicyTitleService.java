package com.longyan.policy.service;

import com.alibaba.fastjson.JSON;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.longyan.policy.annotation.Audit;
import com.longyan.policy.controller.vo.PolicyView;
import com.longyan.policy.dao.EnterpriseDao;
import com.longyan.policy.dao.GraphDao;
import com.longyan.policy.dao.RedisDao;
import com.longyan.policy.domain.PolicyDepart;
import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.domain.Subscribe;
import com.longyan.policy.domain.graph.GraphEntity;
import com.longyan.policy.mapper.PolicyDepartMapper;
import com.longyan.policy.mapper.SubscribeMapper;
import com.longyan.policy.mapper.graph.PolicyGraphEntityMapper;
import com.longyan.policy.mapper.graph.PolicyGraphRelationMapper;
import com.longyan.policy.mapper.PolicyTitleMapper;
import com.longyan.policy.util.RecommendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PolicyTitleService {

    @Autowired
    private PolicyTitleMapper policyTitleMapper;

    @Autowired
    private PolicyDepartMapper policyDepartMapper;

    @Autowired
    private PolicyGraphRelationMapper policyGraphRelationMapper;

    @Autowired
    private PolicyGraphEntityMapper policyGraphEntityMapper;

    @Autowired
    private GraphDao graphDao;

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Autowired
    private WritableService writableService;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Value("${subscribe.max-amount}")
    private Integer subscribeMaxAmount;

    @Autowired
    RedisDao redisDao;

    /**
     * 可以用来查询所有政策标题，也可以通过type查
     * @return
     */
    @Audit
    public List<PolicyTitle> findPolicyByType(Integer type) {
        if (type == 0) {
            return policyTitleMapper.findAllPolicyTitle();
        }
        else {
            return policyTitleMapper.findPolicyTitleByType(type);
        }
    }

    /**
     * 提取文章所有的标签，最大上限6个标签，最好做到每个类别有一个
     * @param id
     * @return
     */
    public List<String> extractAllTags(Integer id) {
        List<GraphEntity> graphEntityList = graphDao.findByArticleId(id);
        List<String> res = new ArrayList<>();
        if (graphEntityList.size() < 6) {
            for (GraphEntity graphEntity : graphEntityList) {
                res.add(graphEntity.getName());
            }
        }
        else {
            HashMap<String, List<String>> tagMap = new HashMap<>();
            for (GraphEntity graphEntity : graphEntityList) {
                if (tagMap.containsKey(graphEntity.getLabel())) {
                    List<String> entities = tagMap.get(graphEntity.getLabel());
                    entities.add(graphEntity.getName());
                }
                else {
                    List<String> t = new ArrayList<>();
                    t.add(graphEntity.getName());
                    tagMap.put(graphEntity.getLabel(), t);
                }
            }
            int count = 0;
            while (count < 6) {
                Set<String> labelSet = tagMap.keySet();
                for (String label : labelSet) {
                    List<String> entities = tagMap.get(label);
                    if (entities != null && entities.size() > 0) {
                        res.add(entities.get(0));
                        count++;
                        if (count >= 6) {
                            break;
                        }
                        entities.remove(0);
                        tagMap.put(label, entities);
                    }
                }
            }
        }
        return res;
    }

    public List<PolicyTitle> searchPolicyTitle(PolicyTitle policyTitle) {
        List<PolicyTitle> res = new ArrayList<>();
        if (policyTitle.getTitle() != null) {
            JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
            List<SegToken> segTokens = jiebaSegmenter.process(policyTitle.getTitle(), JiebaSegmenter.SegMode.SEARCH);
            List<String> searchWords = segTokens.stream().map(SegToken::toString).collect(Collectors.toList());
            for (String searchWord : searchWords) {
                String[] word = searchWord.split(",|\\[");
                String w = word[1];
                policyTitle.setTitle(w);
                List<PolicyTitle> policyTitleList = policyTitleMapper.searchPolicyTitle(policyTitle);
                RecommendUtil.combineAddScore(policyTitleList, res, 20);
                res = graphPolicyTitleSearch(null, w, res, 2);
            }
        }

        if (policyTitle.getDate() != null) {
            String[] dateInfo = policyTitle.getDate().split(":");
            List<PolicyTitle> policyTitles = policyTitleMapper.findPolicyTitleByDate(dateInfo[0], dateInfo[1]);
            res = RecommendUtil.selectAdaptPolicyTitles(res, policyTitles);
        }
        List<PolicyTitle> sortedRes = res.stream().sorted(new Comparator<PolicyTitle>() {
            @Override
            public int compare(PolicyTitle o1, PolicyTitle o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                }
                else if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
                else return 0;
            }
        }).collect(Collectors.toList());
        return sortedRes;
    }

    public List<PolicyTitle> findAllPolicyTitles(Integer page, Integer limit) {
        return policyTitleMapper.findAllPolicyTitles(page, limit);
    }

    public PolicyTitle getPolicyTitleById(Integer id) {
        PolicyTitle p = policyTitleMapper.findPolicyTitleById(id);
        return p;
    }

    public void extractPolicyDepart() {
        List<PolicyTitle> policyTitleList = policyTitleMapper.findAllPolicyTitle();
        for (PolicyTitle policyTitle : policyTitleList) {
            PolicyDepart policyDepart = policyDepartMapper.findDepartByName(policyTitle.getDepart());
            policyTitle.setDepartId(policyDepart.getId());
            PolicyTitle repair = new PolicyTitle();
            repair.setDepartId(policyDepart.getId());
            repair.setId(policyTitle.getId());
            policyTitleMapper.updatePolicyTitle(repair);
        }
    }

    public Object getPolicyGraph() {
        return graphDao.getAllGraph();
    }

    /**
     * 组合函数，把老和新的List做一个组合，如果新List里包含老的，则新的加分,只有一项的不进行加分
     * @param newPolicyTitleList
     * @param totalPolicyTitleList
     * @return
     */
    private List<PolicyTitle> combinePolicyList(List<PolicyTitle> newPolicyTitleList,
                                               List<PolicyTitle> totalPolicyTitleList,
                                               Integer score) {
        List<Integer> newPolicyTitleIdList = newPolicyTitleList.stream().map(PolicyTitle::getId)
                                                                        .collect(Collectors.toList());
        List<Integer> totalPolicyTitleIdList = totalPolicyTitleList.stream().map(PolicyTitle::getId)
                                                                            .collect(Collectors.toList());
        for (int i = newPolicyTitleIdList.size() - 1; i >= 0; i--) {
            int index = totalPolicyTitleIdList.indexOf(newPolicyTitleIdList.get(i));
            if (index >= 0) {
                Integer s = totalPolicyTitleList.get(index).getScore();
                totalPolicyTitleList.get(index).setScore(score + s);
                newPolicyTitleList.remove(i);
            }
        }
        totalPolicyTitleList.addAll(newPolicyTitleList);
        return totalPolicyTitleList;
    }

    /**
     * 使用知识图谱直接搜索，属于PolicyTitle的私有函数
     * @param type
     * @param name
     * @param res
     * @param score
     * @return
     */

    private List<PolicyTitle> graphPolicyTitleSearch(String type,
                                                     String name,
                                                     List<PolicyTitle> res,
                                                     Integer score) {
        List<GraphEntity> graphEntities = graphDao.findEntityByName(name, type);
        List<String> policyTitlesIds = new ArrayList<>();
        for (GraphEntity graphEntity : graphEntities) {
            String[] articleIds = graphEntity.getArticle_id().split(",");
            for (String a : articleIds) {
                policyTitlesIds.add(a);
            }
        }
        policyTitlesIds = policyTitlesIds.stream().distinct().collect(Collectors.toList());
        List<PolicyTitle> graphEntityList = new ArrayList<>();
        if (policyTitlesIds.size() > 0) {
            graphEntityList = policyTitleMapper.batchFindPolicyTitlesByIds(policyTitlesIds);
        }
        res = combinePolicyList(graphEntityList, res, score);
        return res;
    }

    /**
     * 获取所有订阅的政策
     * @param userId
     * @return
     */
    public List<PolicyTitle> getUserSubscribe(Integer userId) {
        //先查询所有部门和区域的政策，是该政策分数+10,10分为基础分，不是重复了才有
        List<PolicyTitle> res = new ArrayList<>();
        Subscribe subscribe = subscribeMapper.findUserSubscribe(userId);
        if (subscribe == null) {
            return null;
        }
        List<String> departIdListStr = Arrays.asList(subscribe.getDepartIds().split(","));
        List<Integer> departIdList = departIdListStr.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        List<Integer> departWithoutRegion = new ArrayList<>(departIdList);
        if (subscribe.getRegions() != null) {
            List<String> regionList = Arrays.asList(subscribe.getRegions().split(","));
            for (String region : regionList) {
                List<PolicyDepart> departList = policyDepartMapper.findDepartByRegion(region);
                List<Integer> departIdExtraList = departList.stream().map(PolicyDepart::getId).collect(Collectors.toList());
                departIdList.addAll(departIdExtraList);
            }
        }
        departIdList = departIdList.stream().distinct().collect(Collectors.toList());
        PolicyTitle departPolicyTitleSearch = new PolicyTitle();
        for (Integer departId : departIdList) {
            departPolicyTitleSearch.setDepartId(departId);
            List<PolicyTitle> policyTitleList = policyTitleMapper.searchPolicyTitle(departPolicyTitleSearch);
            policyTitleList.stream().forEach(policyTitle -> {
                if (departWithoutRegion.contains(policyTitle.getDepartId())) {
                    policyTitle.setScore(policyTitle.getScore() + 25);
                }
            });
            res.addAll(policyTitleList);
        }
        //查询知识图谱：与organization直接相关的+10
        res = graphPolicyTitleSearch("Organization", subscribe.getOrganization(), res, 10);
        //查询知识图谱，与行业有关+10
        String[] industryArr = subscribe.getIndustry().split(",");
        for (String industry : industryArr) {
            res = graphPolicyTitleSearch("Event", industry, res, 10);
        }
        //查询知识图谱，与关键词有关的+5
        String[] keywords = subscribe.getKeywords().split(",");
        for (String keyword : keywords) {
            res = graphPolicyTitleSearch(null, keyword, res, 5);
        }
        for (PolicyTitle policyTitle : res) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(policyTitle.getDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int policyYear = calendar.get(Calendar.YEAR);
                calendar.setTime(new Date());
                int nowYear = calendar.get(Calendar.YEAR);
                policyTitle.setScore(policyTitle.getScore() - 10 * (nowYear - policyYear));
            } catch (ParseException e) {
                continue;
            }
        }
        //进行排序
        List<PolicyTitle> sortedRes = res.stream().sorted(new Comparator<PolicyTitle>() {
            @Override
            public int compare(PolicyTitle o1, PolicyTitle o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                }
                else if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
                else return 0;
            }
        }).collect(Collectors.toList());
        if (subscribe.getMate() == 0) {
            if (sortedRes.size() <= 1) {
                return sortedRes;
            }
            else {
                List<PolicyTitle> preciseMatchList = new ArrayList<>();
                for (int i = 1; i < sortedRes.size(); i++) {
                    preciseMatchList.add(sortedRes.get(i - 1));
                    if (sortedRes.get(i - 1) != sortedRes.get(i)) {
                        break;
                    }
                }
                return preciseMatchList;
            }
        }
        else {
            if (sortedRes.size() > subscribeMaxAmount) {
                sortedRes = sortedRes.subList(0, subscribeMaxAmount);
            }
            return sortedRes;
        }
    }

    public HashMap<String, Object> getCreatePolicyInfo() {
        List<PolicyDepart> policyDepartList = policyDepartMapper.findAllDeparts();
        List<String> regionList = new ArrayList<>();
        for (PolicyDepart policyDepart : policyDepartList) {
            regionList.add(policyDepart.getRegion());
        }
        regionList = regionList.stream().distinct().collect(Collectors.toList());
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("regions", regionList);
        hashMap.put("departs", policyDepartList);
        return hashMap;
    }

    public Integer addPolicy(String depart, String title, String content) {
        Date nowTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String date = ft.format(nowTime);
        Integer id = writableService.addPolicy(depart, "", date, title, content,null);
        return id;
    }

    public List<PolicyTitle> enterpriseRecommendPolicyTitles(Integer userId) {
        List<PolicyTitle> policyTitles = getCachePolicyTitles("recommend_" + String.valueOf(userId));
        if (policyTitles != null) {
            return policyTitles;
        }
        List<PolicyTitle> policyTitleList = enterpriseDao.getRecommendPolicyTitles(userId);
        writeCache("recommend_" + String.valueOf(userId), policyTitleList);
        return policyTitleList;
    }

    private List<PolicyTitle> getCachePolicyTitles(String id) {
        try {
            String object = redisDao.getSaveObject(id);
            if (object != null) {
                List<PolicyTitle> policyTitleList = JSON.parseArray(object, PolicyTitle.class);
                return policyTitleList;
            }
            else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }

    private void writeCache(String id, List<PolicyTitle> policyTitles) {
        try {
            redisDao.saveDataById(id, JSON.toJSONString(policyTitles));
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }

    public List<PolicyTitle> getRelevant(Integer policyId) {
        PolicyTitle policyTitle = policyTitleMapper.findPolicyTitleById(policyId);
        PolicyTitle search = new PolicyTitle();
        search.setTitle(policyTitle.getTitle());
        List<PolicyTitle> policyTitleList = searchPolicyTitle(search);
        List<Integer> policyIdList = policyTitleList.stream().map(PolicyTitle :: getId).collect(Collectors.toList());
        int index = policyIdList.indexOf(policyId);
        if (index >= 0) {
            policyTitleList.remove(index);
        }
        if (policyTitleList.size() > 4) {
            return policyTitleList.subList(0, 4);
        }
        return policyTitleList;
    }


}
