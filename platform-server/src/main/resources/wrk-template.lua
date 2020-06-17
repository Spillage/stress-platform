function randomNum(startNum,endNum)
    math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
    counter = tostring(math.random(startNum,endNum))
    return counter
end

function randomStr()
    local template ='xxxxxxxxxxxx'
    math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
    return string.gsub(template, '[xy]', function (c)
        local v = (c == 'x') and math.random(0, 0xf) or math.random(8, 0xb)
        return string.format('%x', v)
    end)
end

function randomUuid()
    local template ='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
    math.randomseed(os.time() + assert(tonumber(tostring({}):sub(7))))
    return string.gsub(template, '[xy]', function (c)
        local v = (c == 'x') and math.random(0, 0xf) or math.random(8, 0xb)
        return string.format('%x', v)
    end)
end

function request()

