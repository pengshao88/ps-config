package cn.pengshao.psconfig.server.dal;

import cn.pengshao.psconfig.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/4/29 23:13
 */
@Repository
@Mapper
public interface ConfigsMapper {

    @Select("select app, env, version, ns, config_key as configKey, config_value as configValue from configs " +
            "where app = #{app} and env = #{env} and version = #{version} and ns = #{ns}")
    List<Configs> list(String app, String env, String version, String ns);

    @Select("select distinct ns from configs where app = #{app} and env = #{env} and version = #{version}")
    List<String> listNs(String app, String env, String version);

    @Select("select app, env, version, ns, config_key as configKey, config_value as configValue from configs " +
            "where app = #{app} and env = #{env} and version = #{version} and ns = #{ns} and config_key = #{configKey}")
    Configs select(String app, String env, String version, String ns, String configKey);

    @Insert("insert into configs(app, env, version, ns, config_key, config_value) values(#{app}, #{env}, #{version}, #{ns}, #{configKey}, #{configValue})")
    int insert(Configs configs);

    @Update("update configs set config_value = #{configValue} where app = #{app} and env = #{env} and version = #{version} and ns = #{ns} and config_key = #{configKey}")
    int update(Configs configs);

}
