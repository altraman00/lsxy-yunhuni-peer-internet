local ok = redis.call('setnx', KEYS[1], ARGV[1])
if ok == 1 then
    redis.call('expire', KEYS[1], ARGV[2])
end
return ok