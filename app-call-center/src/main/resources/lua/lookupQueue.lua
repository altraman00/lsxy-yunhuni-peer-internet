--坐席找排队
local aCs_key = KEYS[1]
local agent_state_key = KEYS[2]
local aQs_key = KEYS[3]
local extension_state_key_prefix = KEYS[4]
local agent_lock_key = KEYS[5]
local queue_lock_key_prefix = KEYS[6]
local cQs_key_prefix = KEYS[7]

local agent_reg_expire = tonumber(ARGV[1])
local cur_time = tonumber(ARGV[2])
local idle = ARGV[3]
local fetching = ARGV[4]
local target_condition = ARGV[5]
local extension_enable = ARGV[6]

local array_to_map = function(_array)
    local _map ={}
    if(_array)
    then
        for i=1,#_array,2 do
            _map[_array[i]] = _array[i+1]
        end
    end
    return _map;
end

local result
local aQs = {}
local aCs = {}
local agent
local agent_locked = false

--判断坐席状态,给坐席上锁
agent = array_to_map(redis.call('HGETALL',agent_state_key))
redis.log(redis.LOG_WARNING,agent['state'])
redis.log(redis.LOG_WARNING,agent['extension'])
redis.log(redis.LOG_WARNING,agent['lastRegTime'])
if(agent and agent['state'] == idle
        and agent['extension'] and agent['lastRegTime']
        and (agent['lastRegTime'] + agent_reg_expire) >= cur_time)
then
    local extension = array_to_map(redis.call('HGETALL',extension_state_key_prefix..agent['extension']))
    redis.log(redis.LOG_WARNING,extension['enable'])
    if(extension and extension['enable'] and extension['enable'] == extension_enable)
    then
        local ok = redis.call('setnx',agent_lock_key, '1')
        redis.log(redis.LOG_WARNING,ok)
        if ok == 1 then
            redis.call('EXPIRE', agent_lock_key, '60')
            agent_locked = true
        end
    end
end

--坐席不可用，加锁失败
if(agent_locked == false) then
    return result
end

--查找指定了坐席的排队aqs
aQs = redis.call('ZREVRANGE',aQs_key,0,-1)
local aQs_size = #aQs
for i = 1, aQs_size do
    local q_lock_key = queue_lock_key_prefix..aQs[i]
    local ok = redis.call('setnx',q_lock_key, '1')
    redis.log(redis.LOG_WARNING,ok)
    if ok == 1 then
        result = aQs[i]
        redis.call('HSET',agent_state_key,'lastTime',cur_time)
        redis.call('HSET',agent_state_key,'state',fetching)
        redis.call('ZREM',aQs_key,result)
        redis.call('DEL', q_lock_key)
        redis.call('DEL',agent_lock_key)
        return result
    end
end

--指定了条件
if(string.len(target_condition)>0) then
    aCs[1] = target_condition
else
    aCs = redis.call('ZREVRANGE',aCs_key,0,-1)
end

local aCs_size = #aCs

redis.log(redis.LOG_WARNING,'aCs_size'..aCs_size)

for i = 1, aCs_size do
    local cQs_key = cQs_key_prefix..aCs[i]
    local cQs = redis.call('ZREVRANGE',cQs_key,0,-1)
    local cQs_size = #cQs
    for j = 1, cQs_size do
        local q_lock_key = queue_lock_key_prefix..cQs[j]
        local ok = redis.call('setnx',q_lock_key, '1')
        redis.log(redis.LOG_WARNING,ok)
        if ok == 1 then
            result = cQs[j]
            redis.call('HSET',agent_state_key,'lastTime',cur_time)
            redis.call('HSET',agent_state_key,'state',fetching)
            redis.call('ZREM',cQs_key,result)
            redis.call('DEL', q_lock_key)
            redis.call('DEL',agent_lock_key)
            return result
        end
    end
end
if result == nil then
    redis.call('DEL',agent_lock_key)
end

return result