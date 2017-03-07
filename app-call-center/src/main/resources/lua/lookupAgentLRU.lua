--排队找坐席
local cAs_key = KEYS[1]
local agent_state_key_prefix = KEYS[2]
local extension_state_key_prefix = KEYS[3]
local agent_lock_key_prefix = KEYS[4]

local agent_reg_expire = tonumber(ARGV[1])
local cur_time = tonumber(ARGV[2])
local idle = ARGV[3]
local fetching = ARGV[4]
local extension_enable = ARGV[5]
local target_agent_id = ARGV[6]
local lock_info = ARGV[7]
local result

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

--指定坐席ID查找，先获取坐席lock，然后判断坐席分机状态，解锁
if(target_agent_id and string.len(target_agent_id) > 0)
then
	redis.log(redis.LOG_WARNING,'target_agent_id='..target_agent_id)
	local agent_id = target_agent_id
	local ok = redis.call('setnx',agent_lock_key_prefix..agent_id, lock_info)
	redis.log(redis.LOG_WARNING,ok)
	if ok == 1 then
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
				redis.call('HSET',agent_state_key_prefix..agent_id,'lastTime',cur_time)
				redis.call('HSET',agent_state_key_prefix..agent_id,'state',fetching)
				result = agent_id
			end
		end
		redis.call('DEL', agent_lock_key_prefix..agent_id)
	end
	return result;
end


local cas = redis.call('ZREVRANGE',cAs_key,0,-1,'WITHSCORES')
local cas_size = #cas
redis.log(redis.LOG_WARNING,cAs_key)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,extension_state_key_prefix)
redis.log(redis.LOG_WARNING,agent_lock_key_prefix)
redis.log(redis.LOG_WARNING,'cur_time'..cur_time)
redis.log(redis.LOG_WARNING,'cas_size'..cas_size)

local idleAgents = {}

--获取空闲的状态的坐席列表
for i=1,cas_size,2 do
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
			local idleAgent = {id=agent_id,score=cas[i+1],lastTime=agent["lastTime"] }
			if idleAgent.score == nil then
				idleAgent.score = 0
			else
				idleAgent.score = tonumber(idleAgent.score)
			end
			if idleAgent.lastTime == nil then
				idleAgent.lastTime = 0
			else
				idleAgent.lastTime = tonumber(idleAgent.lastTime)
			end
			table.insert(idleAgents,idleAgent)
		end
	end
end

--根据最后活动时间降序排序
table.sort(idleAgents, function(a,b)
	if a.score == b.score then
		return a.lastTime<b.lastTime
	else
		return a.score>b.score
	end
end )

--遍历空闲坐席列表，加锁，判断状态
for i in pairs(idleAgents) do
	local agent_id = idleAgents[i].id
	local ok = redis.call('setnx',agent_lock_key_prefix..agent_id, lock_info)
	redis.log(redis.LOG_WARNING,ok)
	if ok == 1 then
		redis.call('HSET',agent_state_key_prefix..agent_id,'lastTime',cur_time)
		redis.call('HSET',agent_state_key_prefix..agent_id,'state',fetching)
		redis.call('DEL', agent_lock_key_prefix..agent_id)
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
				redis.call('HSET',agent_state_key_prefix..agent_id,'lastTime',cur_time)
				redis.call('HSET',agent_state_key_prefix..agent_id,'state',fetching)
				result = agent_id
			end
		end
		redis.call('DEL', agent_lock_key_prefix..agent_id)
		if(result and string.len(result) > 0) then
			return result
		end
	end
end

return result