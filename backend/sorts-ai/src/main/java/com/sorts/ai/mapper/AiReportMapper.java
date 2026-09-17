package com.sorts.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sorts.ai.entity.AiReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 报告仓储。
 *
 * @author sorts
 */
@Mapper
public interface AiReportMapper extends BaseMapper<AiReport> {

    /** 属主校验 + 软删除（带审计时间） */
    @Update("UPDATE t_ai_report SET deleted = 1, deleted_at = NOW(), updated_at = NOW() "
            + "WHERE id = #{id} AND user_id = #{userId} AND deleted = 0")
    int softDeleteOwned(@Param("userId") Long userId, @Param("id") Long id);

    /** 批量软删除：整体校验属主，影响行数 < 请求数时由服务层抛错回滚 */
    @Update("<script>"
            + "UPDATE t_ai_report SET deleted = 1, deleted_at = NOW(), updated_at = NOW() "
            + "WHERE user_id = #{userId} AND deleted = 0 "
            + "AND id IN "
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>"
            + "</script>")
    int softDeleteBatchOwned(@Param("userId") Long userId, @Param("ids") List<Long> ids);
}
