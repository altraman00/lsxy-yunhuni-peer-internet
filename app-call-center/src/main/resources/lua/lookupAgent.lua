--排队找坐席
local cAs_key = KEYS[1]
local agent_state_key_prefix = KEYS[2]
local extension_state_key_prefix = KEYS[3]
local agent_lock_key_prefix = KEYS[4]

local agent_reg_expire = 1000 * 60 * 5
local cur_time = redis.call('TIME')[1]*1000
local result

local cas = redis.call('ZREVRANGE',cAs_key,0,-1)

redis.log(redis.LOG_WARNING,cAs_key)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,agent_lock_key_prefix)
redis.log(redis.LOG_WARNING,#cas)

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

for i=1,#cas do
	local agent_id = cas[i]
	redis.log(redis.LOG_WARNING,agent_state_key_prefix..agent_id)
    local agent = array_to_map(redis.call('HGETALL',agent_state_key_prefix..agent_id))
	redis.log(redis.LOG_WARNING,agent['state'])
	redis.log(redis.LOG_WARNING,agent['extension'])
	redis.log(redis.LOG_WARNING,agent['lastRegTime'])
	if(agent and agent['state'] == 'IDLE'
		and agent['extension'] and agent['lastRegTime']
			and (agent['lastRegTime'] + agent_reg_expire) >= cur_time)
	then
		local extension = array_to_map(redis.call('HGETALL',extension_state_key_prefix..agent['extension']))
		redis.log(redis.LOG_WARNING,extension['lastRegisterStatus'])
		if(extension and extension['lastRegisterStatus']
				and (extension['lastRegisterTime'] + extension['registerExpires']) <= cur_time)
		then
			local ok = redis.call('setnx',agent_lock_key_prefix..agent_id, '1')
			if ok == 1 then
				redis.call('expire', agent_lock_key_prefix..agent_id, 60)
				redis.call('HSET',agent_state_key_prefix..agent_id,'state','BUSY')
				result = agent_id
				break
			end
		end
	end
end
return result