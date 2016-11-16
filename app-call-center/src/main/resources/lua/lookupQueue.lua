--坐席找排队
local aCs_key = KEYS[1]
local agent_state_key = KEYS[2]
local extension_state_key_prefix = KEYS[3]
local agent_lock_key = KEYS[4]
local queue_lock_key_prefix = KEYS[5]
local cQs_key_prefix = KEYS[6]

local agent_reg_expire = tonumber(ARGV[1])
local cur_time = tonumber(ARGV[2])
local idle = ARGV[3]
local fetching = ARGV[4]
local target_condition = ARGV[5]

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
    redis.log(redis.LOG_WARNING,extension['lastRegisterStatus'])
    redis.log(redis.LOG_WARNING,extension['lastRegisterTime'])
    redis.log(redis.LOG_WARNING,extension['registerExpires'])
    if(extension and extension['lastRegisterStatus']
            and (extension['lastRegisterTime'] + extension['registerExpires']) >= cur_time)
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

--指定了条件
if(target_condition) then
    aCs[0] = target_condition
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
            redis.call('EXPIRE', q_lock_key, '60')
            redis.call('HSET',agent_state_key,'state',fetching)
            result = cQs[j]
            break
        end
    end
end

return result