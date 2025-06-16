local p = peripheral.wrap('left')

local uuid = p.openHandle('minecraft:diamond', 'minecraft:emerald')

p.setSignal(uuid, 15)
