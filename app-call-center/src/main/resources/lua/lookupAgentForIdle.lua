--排队找坐席
local cAs_key = KEYS[1]
local agent_state_key_prefix = KEYS[2]
local extension_state_key_prefix = KEYS[3]

local agent_reg_expire = tonumber(ARGV[1])
local cur_time = tonumber(ARGV[2])
local idle = ARGV[3]
local extension_enable = ARGV[4]
local result

local cas = redis.call('ZREVRANGE',cAs_key,0,-1)
local cas_size = #cas
redis.log(redis.LOG_WARNING,cAs_key)
redis.log(redis.LOG_WARNING,agent_state_key_prefix)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,'cur_time'..cur_time)
redis.log(redis.LOG_WARNING,'cas_size'..cas_size)

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

for i=1,cas_size do
    local agent_id = cas[i]
    local agent = array_to_map(redis.call('HGETALL',agent_state_key_prefix..agent_id))
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
            result = agent_id
            return result
        end
    end
end

return result