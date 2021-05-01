package com.longyan.policy.util;

import com.longyan.policy.domain.PolicyTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendUtil {

    /**
     * 合并政策列表的工具
     * @param newPolicyTitleList 新的政策列表（待合并对象）
     * @param totalPolicyTitleList 总共的政策列表(合并到这上面)
     * @param score 这次加分
     * @return 总共的政策列表
     */
    public static List<PolicyTitle> combineAddScore(List<PolicyTitle> newPolicyTitleList,
                                      List<PolicyTitle> totalPolicyTitleList,
                                      Integer score) {
        List<Integer> newPolicyTitleIdList = newPolicyTitleList.stream().map(PolicyTitle::getId)
                .collect(Collectors.toList());
        List<Integer> totalPolicyTitleIdList = totalPolicyTitleList.stream().map(PolicyTitle::getId)
                .collect(Collectors.toList());
        for (int i = newPolicyTitleIdList.size() - 1; i >= 0; i--) {
            int index = totalPolicyTitleIdList.indexOf(newPolicyTitleIdList.get(i));
            if (index > 0) {
                Integer s = totalPolicyTitleList.get(index).getScore();
                totalPolicyTitleList.get(index).setScore(score + s);
                newPolicyTitleList.remove(i);
            }
        }
        totalPolicyTitleList.addAll(newPolicyTitleList);
        return totalPolicyTitleList;
    }

    public static List<PolicyTitle> selectAdaptPolicyTitles(List<PolicyTitle> total, List<PolicyTitle> conditionList) {
        List<Integer> totalIdList = total.stream().map(PolicyTitle::getId).collect(Collectors.toList());
        List<Integer> conditionIdList = conditionList.stream().map(PolicyTitle::getId).collect(Collectors.toList());
        List<PolicyTitle> res = new ArrayList<>();
        for (Integer conditionId : conditionIdList) {
            int index = totalIdList.indexOf(conditionId);
            if (index >= 0) {
                res.add(total.get(index));
            }
        }
        return res;
    }
}
