package com.github.taoroot.taoiot.security.service;

import cn.hutool.core.util.IdUtil;
import com.github.taoroot.taoiot.security.LoginType;
import com.github.taoroot.taoiot.security.SecurityProperties;
import com.github.taoroot.taoiot.security.SecurityUser;
import com.github.taoroot.taoiot.security.SecurityUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhiyi
 */
@Log4j2
@AllArgsConstructor
@ConditionalOnClass({SqlSessionFactory.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class DbUserDetailsServiceImpl implements SecurityUserDetailsService {

    private final SqlSessionFactory sqlSessionFactory;

    private final SecurityProperties securityProperties;

    private final PasswordEncoder passwordEncoder;

    private final String DEFAULT_ROLE = "ROLE_USER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        sqlSessionFactory.getConfiguration().addMapper(DBUserMapper.class);
        DbUser myUser = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DBUserMapper mapper = session.getMapper(DBUserMapper.class);
            myUser = mapper.getByUsername(securityProperties.getTableName(), username);
        } catch (Exception e) {
            log.error(e);
        }
        if (myUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new SecurityUser(
                myUser.getId(),
                LoginType.PASS,
                myUser.getWxMpOpenid(),
                myUser.getAliMpOpenid(),
                myUser.getUsername(),
                myUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(myUser.getRoles())
        );
    }


    @Override
    public UserDetails loadUserByWechat(String openId) throws UsernameNotFoundException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DBUserMapper mapper = session.getMapper(DBUserMapper.class);
            DbUser myUser = mapper.getByWxMpOpenid(securityProperties.getTableName(), openId);
            if (myUser == null) {
                myUser = new DbUser();
                myUser.setPassword(passwordEncoder.encode(IdUtil.fastSimpleUUID()));
                myUser.setRoles("ROLE_USER");
                mapper.insert(securityProperties.getTableName(), IdUtil.simpleUUID(), IdUtil.simpleUUID(), null, openId, DEFAULT_ROLE);
            }
            myUser = mapper.getByWxMpOpenid(securityProperties.getTableName(), openId);
            return new SecurityUser(
                    myUser.getId(),
                    LoginType.WX,
                    myUser.getWxMpOpenid(),
                    myUser.getAliMpOpenid(),
                    myUser.getUsername(),
                    myUser.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(myUser.getRoles())
            );
        } catch (Exception e) {
            log.error("数据库查询失败", e);
        }

        return null;
    }

    @Override
    public UserDetails loadUserByAlipay(String openId) throws UsernameNotFoundException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DBUserMapper mapper = session.getMapper(DBUserMapper.class);
            DbUser myUser = mapper.getByAliMpOpenid(securityProperties.getTableName(), openId);
            if (myUser == null) {
                myUser = new DbUser();
                myUser.setPassword(passwordEncoder.encode(IdUtil.fastSimpleUUID()));
                myUser.setRoles("ROLE_USER");
                mapper.insert(securityProperties.getTableName(), IdUtil.simpleUUID(), IdUtil.simpleUUID(), openId, null, DEFAULT_ROLE);
            }
            myUser = mapper.getByAliMpOpenid(securityProperties.getTableName(), openId);
            return new SecurityUser(
                    myUser.getId(),
                    LoginType.ALIPAY,
                    myUser.getWxMpOpenid(),
                    myUser.getAliMpOpenid(),
                    myUser.getUsername(),
                    myUser.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(myUser.getRoles())
            );
        } catch (Exception e) {
            log.error("数据库查询失败", e);
        }
        return null;
    }

}
