--登录
local agentStateKey = KEYS[1]
local extensionStateKey = KEYS[2]

function string.split(str, delimiter)
    if str==nil or str=='' or delimiter==nil then
        return nil
    end
    local result = {}
    for match in (str..delimiter):gmatch("(.-)"..delimiter) do
        table.insert(result, match)
    end
    return result
end

redis.call('HMSET',agentStateKey,unpack(string.split(ARGV[1],",")))
--因为不知道传进来的state会不会包含","号，所以单独分出来
redis.call('HSET',agentStateKey,"state",ARGV[2])
redis.call('HSET',extensionStateKey,"agent",ARGV[3])
if (#KEYS > 2)
then
    redis.call('ZADD',KEYS[3],unpack(string.split(ARGV[4],",")))
    for i=4,#KEYS do
        redis.call('ZADD',KEYS[i],unpack(string.split(ARGV[i+1],",")))
    end
end






