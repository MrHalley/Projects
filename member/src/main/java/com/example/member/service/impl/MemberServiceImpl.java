package com.example.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.IgnorePropertiesUtil;
import com.example.common.vo.GiteeToken;
import com.example.member.dao.GiteeDao;
import com.example.member.dao.MemberLevelDao;
import com.example.member.entity.GiteeEntity;
import com.example.member.entity.MemberLevelEntity;
import com.example.member.exception.PhoneException;
import com.example.member.exception.UsernameException;
import com.example.member.vo.MemberUserLoginVo;
import com.example.member.vo.MemberUserRegisterVo;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.member.dao.MemberDao;
import com.example.member.entity.MemberEntity;
import com.example.member.service.MemberService;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    private MemberLevelDao memberLevelDao;

    @Resource
    private GiteeDao giteeDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public void register(MemberUserRegisterVo vo) {

        MemberEntity memberEntity = new MemberEntity();

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(levelEntity.getId());

        //设置其它的默认信息
        //检查用户名和手机号是否唯一。感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());

        memberEntity.setNickname(vo.getUserName());
        memberEntity.setUsername(vo.getUserName());
        //密码进行MD5加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);
        memberEntity.setMobile(vo.getPhone());
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());

        //保存数据
        this.baseMapper.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneException {
        Integer phoneCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (phoneCount > 0) {
            throw new PhoneException();
        }
    }


    @Override
    public void checkUserNameUnique(String userName) throws UsernameException {
        Integer usernameCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (usernameCount > 0) {
            throw new UsernameException();
        }
    }

    @Override
    public MemberEntity login(MemberUserLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();

        //1、去数据库查询 SELECT * FROM ums_member WHERE username = ? OR mobile = ?
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginacct).or().eq("mobile", loginacct));

        if (memberEntity == null) {
            //登录失败
            return null;
        } else {
            //获取到数据库里的password
            String password1 = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //进行密码匹配
            boolean matches = passwordEncoder.matches(password, password1);
            if (matches) {
                //登录成功
                return memberEntity;
            }
        }
        return null;
    }

    @Override
    public MemberEntity login(GiteeToken giteeToken) throws Exception {
        //具有登录和注册逻辑

        //查gitee账号
        Map<String,String> querys = new HashMap<>();
        querys.put("access_token",giteeToken.getAccess_token());
        HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "post", new HashMap<>(), querys);
        String json = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSON.parseObject(json);
        String name = jsonObject.getString("name");

        //1、判断当前社交用户是否已经登录过系统
        GiteeEntity giteeEntity = giteeDao.selectOne(new QueryWrapper<GiteeEntity>().eq("name", name));
        if (giteeEntity != null) {
            //这个用户已经注册过
            //更新用户的访问令牌的时间和access_token
            MemberEntity memberEntity = this.baseMapper.selectById(giteeEntity.getMemberId());
            GiteeEntity newerGiteeEntity = transferToGiteeEntity(giteeToken);
            BeanUtils.copyProperties(newerGiteeEntity,giteeEntity,IgnorePropertiesUtil.getNullPropertyNames(newerGiteeEntity));
            this.giteeDao.updateById(giteeEntity);
//            update.setId(memberEntity.getId());
//            update.setAccessToken(socialUser.getAccess_token());
//            update.setExpiresIn(socialUser.getExpires_in());
//            this.baseMapper.updateById(update);
//
//            memberEntity.setAccessToken(socialUser.getAccess_token());
//            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity register = new MemberEntity();

            register.setNickname(name);
            register.setCreateTime(new Date());

            //把用户信息插入到数据库中
            this.baseMapper.insert(register);
            giteeEntity = transferToGiteeEntity(giteeToken);
            giteeEntity.setMemberId(register.getId());
            giteeEntity.setName(name);
            giteeDao.insert(giteeEntity);

            return register;
        }
    }

    private GiteeEntity transferToGiteeEntity(GiteeToken giteeToken){
        GiteeEntity giteeEntity = new GiteeEntity();
        giteeEntity.setAccessToken(giteeToken.getAccess_token());
        Date createAt = new Date();
        createAt.setTime(giteeToken.getCreated_at()*1000);
        giteeEntity.setCreateAt(createAt);
        giteeEntity.setRefreshToken(giteeToken.getRefresh_token());
        Date expiresIn = new Date();
        expiresIn.setTime((giteeToken.getCreated_at()+giteeToken.getExpires_in())*1000);
        giteeEntity.setExpiresIn(expiresIn);
        giteeEntity.setTokenType(giteeToken.getToken_type());
        giteeEntity.setCreateTime(new Date());
        return giteeEntity;
    }
}