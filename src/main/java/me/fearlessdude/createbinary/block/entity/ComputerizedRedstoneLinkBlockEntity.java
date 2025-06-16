package me.fearlessdude.createbinary.block.entity;

import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkFrequencySlot;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComputerizedRedstoneLinkBlockEntity extends SmartBlockEntity {
    public ConcurrentHashMap<UUID, LinkPair> pairs = new ConcurrentHashMap<>();
    public LinkPair pair;
    public Queue<Runnable> tasks = new ArrayBlockingQueue<>(32);

    public ComputerizedRedstoneLinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTERIZED_REDSTONE_LINK_BE.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        pair = new LinkPair(this, new Item[]{Items.DIAMOND, Items.EMERALD});
        behaviours.add(pair.transmit);
        behaviours.add(pair.receive);
    }

    public void tick(Level level, BlockPos pos, BlockState state, ComputerizedRedstoneLinkBlockEntity be) {
        be.tick();
        if (!getLevel().isClientSide()) {
            Runnable a;
            while ((a = tasks.poll()) != null) {
                a.run();
            }
        }

        AtomicBoolean isTransmitting = new AtomicBoolean(false);
        AtomicBoolean isReceiving = new AtomicBoolean(false);

        pairs.values().forEach(linkPair -> {
            linkPair.transmit.tick();
            linkPair.receive.tick();

            if (linkPair.sendSignal > 0) { isTransmitting.set(true); }
            if ((linkPair.recvSignal > 0) && (linkPair.recvSignal != linkPair.sendSignal)) { isReceiving.set(true); }
        });

        level.setBlock(pos, state.setValue(BooleanProperty.create("transmitting"), isTransmitting.get()), 1 | 4);
        level.setBlock(pos, state.setValue(BooleanProperty.create("receiving"), isReceiving.get()), 1 | 4);

//        if (pair != null) {
//            pair.setFrequency(pair.items);
//            pair.transmitSignal(5);
//        }
    }

    public UUID add(LinkPair pair) {
//        if (pairs.size() >= CreateComputingConfigServer.get().MAXIMUM_CONCURRENT_LINKS.get())
//            return null;
        UUID uuid = UUID.randomUUID();
        pair.index = uuid;
        tasks.add(() -> {
            pair.transmit.initialize();
            pair.receive.initialize();
        });
        pairs.put(uuid, pair);
        return uuid;
    }

    public static class LinkPair {
        ComputerizedRedstoneLinkBlockEntity parent;
        private final LinkBehaviour transmit;
        private final LinkBehaviour receive;
        public UUID index;

        public boolean registered = false;

        public int sendSignal = 0;
        public int recvSignal = 0;

        public Item[] items;

//        public List<Utils.Receiver<Integer>> listeners = new ArrayList<>();

        public LinkPair(ComputerizedRedstoneLinkBlockEntity parent, Item[] items) {
            this.parent = parent;
            Pair<ValueBoxTransform, ValueBoxTransform> slots =
                    ValueBoxTransform.Dual.makeSlots(RedstoneLinkFrequencySlot::new);
            transmit = LinkBehaviour.transmitter(parent, slots, this::getSignal);
            receive = LinkBehaviour.receiver(parent, slots, this::setSignal);
            this.items = items;
//            this.setFrequency(items);
        }

        public void setFrequency(Item[] items) {
            parent.tasks.add(() -> {
                transmit.setFrequency(true, new ItemStack(items[0]));
                transmit.setFrequency(false, new ItemStack(items[1]));
                receive.setFrequency(true, new ItemStack(items[0]));
                receive.setFrequency(false, new ItemStack(items[1]));
                this.items = items;
            });
            dirty();
        }

        public void transmitSignal(int strength) {
            sendSignal = strength;
            transmit.notifySignalChange();
        }

        private void dirty() {
            transmit.notifySignalChange();
            receive.notifySignalChange();
        }

        private void setSignal(int i) {
            recvSignal = i;
//            listeners.forEach(a -> a.receive(i));
        }

        private int getSignal() {
            return sendSignal;
        }
    }
}
