package fr.nhsoul.neobank;

import fr.nhsoul.neobank.network.NeoBits;
import fr.nhsoul.neobank.network.NeoBitsClient;
import fr.nhsoul.neobank.network.NeoBitsServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.util.Tuple;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import fr.nhsoul.neobank.init.NeobankModMenus;
import fr.nhsoul.neobank.init.NeobankModItems;
import fr.nhsoul.neobank.init.NeobankModBlocks;

@Mod("neobank")
public class NeobankMod {
	public static final Logger LOGGER = LogManager.getLogger(NeobankMod.class);
	public static final String MODID = "neobank";
	public static int NeoBitsAmount = 0;

	public NeobankMod(IEventBus modEventBus) {
		NeoForge.EVENT_BUS.register(this);

		NeobankModBlocks.REGISTRY.register(modEventBus);

		NeobankModItems.REGISTRY.register(modEventBus);

		NeobankModMenus.REGISTRY.register(modEventBus);
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		CompoundTag playerNBT = player.getPersistentData();
		PacketDistributor.sendToPlayer((ServerPlayer) player, new NeoBits(playerNBT.getInt("NeoBits")));
	}
}
