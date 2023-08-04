package com.aml.service;

import com.aml.base.Result;
import com.aml.entity.SinhVien;
import com.aml.repository.SinhVienRepository;
import com.aml.util.JsonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepo;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //@CachePut
    @Transactional
    public Result addSinhVien(String dto) {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SinhVien sinhVien = JsonUtil.parseObject(dto, SinhVien.class);
        if (sinhVien != null) {
            sinhVien = sinhVienRepo.save(sinhVien);
            String json = JsonUtil.toJsonString(sinhVien);
            redisTemplate.opsForValue().set("user:" + sinhVien.getMaSV(), json, 30, TimeUnit.SECONDS);
            return Result.success("Success", sinhVien);
        } else {
            return Result.fail("Fail to convert data sinhvien");
        }
    }
    // @Cacheable
    public Result findAll() {
        String json = redisTemplate.opsForValue().get("sinhViens");
        List<SinhVien> sinhViens = null;
        if (StringUtils.hasLength(json))
            sinhViens = JsonUtil.parseArray(json, SinhVien.class);
        if (sinhViens == null || sinhViens.size() == 0) {
            sinhViens = sinhVienRepo.findAll();
            json = JsonUtil.toJsonString(sinhViens);
            redisTemplate.opsForValue().set("sinhViens", json, 30, TimeUnit.SECONDS);
        }

        return Result.success("Success", sinhViens);
    }

    public Result getUserInfo(Integer id) {
        String json = redisTemplate.opsForValue().get("user:" + id);
        SinhVien sinhVien  = null;

        if (StringUtils.hasLength(json))
            sinhVien = JsonUtil.parseObject(json, SinhVien.class);
        if (sinhVien != null) {
            return Result.success("Success", sinhVien);
        } else {
            Optional<SinhVien> op = sinhVienRepo.findById(id);
            if (op.isPresent()) {
                sinhVien = op.get();
                json = JsonUtil.toJsonString(sinhVien);
                redisTemplate.opsForValue().set("user:" + id, json, 30, TimeUnit.SECONDS);
                return Result.success("Success", sinhVien);
            }
            else {
                return Result.fail("User not found!");
            }
        }
    }

    // @CacheEvict
    @Transactional
    @Modifying
    public Result deleteSinhVien(Integer id) {
        String json = redisTemplate.opsForValue().get("user:" + id);
        SinhVien sinhVien = JsonUtil.parseObject(json, SinhVien.class);
        if (sinhVien != null) {
            sinhVienRepo.delete(sinhVien);
            redisTemplate.delete("user:" + id);
        } else {
            sinhVienRepo.deleteById(id);
        }
        return Result.success("Success", id);
    }
}
