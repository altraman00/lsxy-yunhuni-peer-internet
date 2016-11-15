local cas = redis.call('ZREVRANGE',KEYS[1],0,-1)
local result = {}
for i=1,#cas do
    local agent_state = redis.call('HGET',KEYS[2]..cas[i],'state')
	if(agent_state == 'IDLE')
	then
		local agent_extension = redis.call('HGET',KEYS[2]..cas[i],'extension')
		if(agent_extension)
		then
			local extension = redis.call('HMGET',KEYS[3]..agent_extension,'lastAction','lastActionTime','lastRegisterTime','lastRegisterStatus',registerExpires)
		end
	end
	break
end
return result