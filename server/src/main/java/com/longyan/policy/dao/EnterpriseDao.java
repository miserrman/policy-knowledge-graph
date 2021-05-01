package com.longyan.policy.dao;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.longyan.policy.domain.Enterprise;
import com.longyan.policy.domain.EnterpriseUser;
import com.longyan.policy.domain.Industry;
import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.domain.graph.GraphEntity;
import com.longyan.policy.mapper.EnterpriseMapper;
import com.longyan.policy.mapper.EnterpriseUserMapper;
import com.longyan.policy.mapper.IndustryMapper;
import com.longyan.policy.mapper.PolicyTitleMapper;
import com.longyan.policy.util.RecommendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EnterpriseDao {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private EnterpriseUserMapper enterpriseUserMapper;

    @Autowired
    private PolicyTitleMapper policyTitleMapper;

    @Autowired
    private IndustryMapper industryMapper;

    @Autowired
    private GraphDao graphDao;


    public List<String> analyseSeriesData(Enterprise enterprise) {
        List<String> keywords = new ArrayList<>();
        List<Enterprise> enterpriseList = enterpriseMapper.findAllEnterprises();
        Double peopleAverage = enterpriseList.stream().mapToDouble(Enterprise::getResearchersNum).average().getAsDouble();
        Double profitAverage = enterpriseList.stream().mapToDouble(Enterprise::getProfit).average().getAsDouble();
        Double techOutfitAverage = enterpriseList.stream().mapToDouble(Enterprise::getTechOutfit).average().getAsDouble();
        if (enterprise.getResearchersNum() >= peopleAverage) {
            keywords.add("人才");
        }
        if (enterprise.getTechOutfit() >= techOutfitAverage) {
            keywords.addAll(Arrays.asList("高新技术", "技术", "科技", "科技成果"));
        }
        return keywords;
    }

    public List<String> combineGraphEntities(List<GraphEntity> graphEntityList) {
        List<String> res = new ArrayList<>();
        for (GraphEntity graphEntity : graphEntityList) {
            List<String> articleIds = Arrays.asList(graphEntity.getArticle_id().split(","));
            res.addAll(articleIds);
        }
        res = res.stream().distinct().collect(Collectors.toList());
        return res;
    }


    /**
     * 现在的逻辑是匹配的关键词就加一次分,应该是合理的我觉得
     * @param type1
     * @param nameList1
     * @param type2
     * @param nameList2
     * @return
     */
    public List<String> keywordsCombineGraphEntities(List<String> nameList1,
                                                     String type1,
                                                     List<String> nameList2,
                                                     String type2) {
        List<String> labels = graphDao.getAllLabels();
        if (!labels.contains(type1))
            type1 = null;
        if (!labels.contains(type2))
            type2 = null;
        List<String> res = new ArrayList<>();
        for (int i = 0; i < nameList1.size(); i++) {
            for (int j = 0; j < nameList2.size(); j++) {
                List<GraphEntity> graphEntityList = graphDao.findTupleByName(nameList1.get(i), type1,
                                                                             nameList2.get(j), type2);
                List<String> articleIds = combineGraphEntities(graphEntityList);
                res.addAll(articleIds);
            }
        }
        res = res.stream().distinct().collect(Collectors.toList());
        return res;
    }

    public HashMap<String, List<String>> generateKeywordsLink(Enterprise enterprise) {
        HashMap<String, List<String>> resMap = new HashMap<>();
        List<String> locations = new ArrayList<>();
        locations.add(enterprise.getRegion());
        resMap.put("Location", locations);
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        if (enterprise.getCertificates() != null) {
            List<String> certificates = jiebaSegmenter.sentenceProcess(enterprise.getCertificates());
            resMap.put("certificates", certificates);
        }
        Industry industry = industryMapper.findIndustryById(enterprise.getIndustry());
        if (industry.getKeywords() != null) {
            List<String> industryKeywords = Arrays.asList(industry.getKeywords().split(","));
//            industryKeywords.add(industry.getName());
            resMap.put("industries", industryKeywords);
        }
        List<String> scaleKeywords = new ArrayList<>();
        switch (enterprise.getScale()) {
            case 0: scaleKeywords.addAll(Arrays.asList("微型", "微")); break;
            case 1: scaleKeywords.addAll(Arrays.asList("小型", "小")); break;
            case 2: scaleKeywords.addAll(Arrays.asList("中型", "中")); break;
        }
        resMap.put("Organization", scaleKeywords);
        List<String> seriesKeywords = analyseSeriesData(enterprise);
        resMap.put("keywords", seriesKeywords);
        return resMap;
    }

    /**
     * 根据关键词生成推荐政策
     * @param map 关键词字典
     * @return
     */
    public List<PolicyTitle> generateSearchPolicyTitle(HashMap<String, List<String>> map) {
        List<String> entityLabelTypes = graphDao.getAllLabels();
        List<String> keyList = new ArrayList<>(map.keySet());
        List<PolicyTitle> policyTitleListRes = new ArrayList<>();
        for (int i = 0; i < keyList.size(); i++) {
            List<String> entityNames = map.get(keyList.get(i));
            String type = null;
            //如果是含有类型的key,则要在neo4j里面精准查找类型
            if (entityLabelTypes.contains(keyList.get(i))) {
                type = keyList.get(i);
            }
            //先给所有直接搜索到名字的实体对应的文章加分，+10
            List<String> entityNameIdList = new ArrayList<>();
            for (String entityName : entityNames) {
                List<GraphEntity>entities = graphDao.findEntityByName(entityName, type);
                entityNameIdList.addAll(combineGraphEntities(entities));
            }
            entityNameIdList = entityNameIdList.stream().distinct().collect(Collectors.toList());
            List<PolicyTitle> entityNameTitles = policyTitleMapper.batchFindPolicyTitlesByIds(entityNameIdList);
            RecommendUtil.combineAddScore(entityNameTitles, policyTitleListRes, 10);
            //合并查询两个领域的所有关键词,+30分
            for (int j = 0; j < keyList.size(); j++) {
                if (j == i) {
                    continue;
                }
                List<String> keywordsPairIdList = keywordsCombineGraphEntities(map.get(keyList.get(i)),
                                                                               keyList.get(i),
                                                                               map.get(keyList.get(j)),
                                                                               keyList.get(j));
                if (keywordsPairIdList.size() > 0) {
                    List<PolicyTitle> keywordsPolicyTitles = policyTitleMapper.batchFindPolicyTitlesByIds(keywordsPairIdList);
                    RecommendUtil.combineAddScore(keywordsPolicyTitles, policyTitleListRes, 30);
                }
            }
        }
        return policyTitleListRes;
    }

    public List<PolicyTitle> getRecommendPolicyTitles(Integer userId) {
        EnterpriseUser enterpriseUser = enterpriseUserMapper.findEnterpriseUserByUserId(userId);
        if (enterpriseUser == null) {
            throw new RuntimeException("用户信息错误");
        }
        Enterprise enterprise = enterpriseMapper.findEnterpriseById(enterpriseUser.getEnterpriseId());
        if (enterprise == null) {
            throw new RuntimeException("企业信息错误");
        }
        HashMap<String, List<String>> map = generateKeywordsLink(enterprise);
        List<PolicyTitle> policyTitleList = generateSearchPolicyTitle(map);
        for (PolicyTitle policyTitle : policyTitleList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(policyTitle.getDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int policyYear = calendar.get(Calendar.YEAR);
                calendar.setTime(new Date());
                int nowYear = calendar.get(Calendar.YEAR);
                policyTitle.setScore(policyTitle.getScore() - 30 * (nowYear - policyYear));
            } catch (ParseException e) {
                continue;
            }
        }
        List<PolicyTitle> sortedRes = policyTitleList.stream().sorted(new Comparator<PolicyTitle>() {
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
        if (sortedRes.size() > 15) {
            return sortedRes.subList(0, 15);
        }
        else {
            return sortedRes;
        }
    }
    public Enterprise getEnterpriseByUserId(Integer userId) {
        EnterpriseUser enterpriseUser = enterpriseUserMapper.findEnterpriseUserByUserId(userId);
        Enterprise enterprise = enterpriseMapper.findEnterpriseById(enterpriseUser.getEnterpriseId());
        return enterprise;
    }
}
