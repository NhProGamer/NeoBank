package fr.nhsoul.neobank;

import fr.nhsoul.neobank.network.NeoBits;
import fr.nhsoul.neobank.network.NeoBitsClient;
import fr.nhsoul.neobank.network.NeoBitsServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = NeoBankMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistrationHandler {
    private RegistrationHandler() {}

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NeoBankMod.MODID).versioned("1.0.0")
                .executesOn(HandlerThread.NETWORK);
        registrar.playBidirectional(
                NeoBits.TYPE,
                NeoBits.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeoBitsClient::handleDataOnMain,
                        NeoBitsServer::handleDataOnMain
                )
        );
    }
}
