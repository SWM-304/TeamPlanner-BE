package com.tbfp.teamplannerbe.test;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/test/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate redisTemplate;

    @PostMapping("/{key}")
    public void set(@PathVariable String key, @RequestBody String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    @GetMapping("/{key}")
    public String get(@PathVariable String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
}
