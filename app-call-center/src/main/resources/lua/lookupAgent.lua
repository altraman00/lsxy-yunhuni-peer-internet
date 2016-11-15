--排队找坐席
local cAs_key = KEYS[1]
local agent_state_key_prefix = KEYS[2]
local extension_state_key_prefix = KEYS[3]
local agent_lock_key_prefix = KEYS[4]

local cur_time = redis.call('TIME')[1]*1000
local result

local cas = redis.call('ZREVRANGE',cAs_key,0,-1)

redis.log(redis.LOG_WARNING,cAs_key)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,agent_lock_key_prefix)
redis.log(redis.LOG_WARNING,#cas)

for i=1,#cas do
	local agent_id = cas[i]
	redis.log(redis.LOG_WARNING,agent_state_key_prefix..agent_id)
    local agent_state = redis.call('HGET',agent_state_key_prefix..agent_id,'state')
	redis.log(redis.LOG_WARNING,agent_state)
	if(agent_state == 'IDLE')
	then
		local agent = redis.call('HMGET',agent_state_key_prefix..agent_id,'extension','lastRegTime','lastTime')
		if(agent and agent['extension'] and agent['lastRegTime'] and (agent['lastTime'] + 1000 * 60 * 5) >= cur_time )
		then
			local extension = redis.call('HMGET',extension_state_key_prefix..agent['extension'],
				'lastRegisterTime','lastRegisterStatus','registerExpires')
			if(extension and extension['lastRegisterStatus'] and (extension['lastRegisterTime'] + extension['registerExpires']) <= cur_time)
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
end
return result