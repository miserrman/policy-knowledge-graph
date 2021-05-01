package com.longyan.policy.mapper;

import com.longyan.policy.domain.PolicyTitle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
public interface PolicyTitleMapper {

    List<PolicyTitle> findPolicyTitleByType(Integer type);

    List<PolicyTitle> findAllPolicyTitle();

    List<PolicyTitle> searchPolicyTitle(PolicyTitle policyTitle);

    PolicyTitle findPolicyTitleById(Integer id);

    List<PolicyTitle> findAllPolicyTitles(Integer page, Integer limit);

    Integer updatePolicyTitle(PolicyTitle policyTitle);

    List<PolicyTitle> findUserSubscribePolicy(Integer userId);

    /**
     * 批量Id查询接口
     * @param ids
     * @return
     */
    List<PolicyTitle> batchFindPolicyTitlesByIds(List<String> ids);

    PolicyTitle findPolicyTitleByLink(String link);

    Integer insertPolicyTitle(PolicyTitle policyTitle);

    List<PolicyTitle> findPolicyTitleByDate(@Param("startDate") String startDate,
                                            @Param("endDate") String endDate);

}
