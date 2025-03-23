package fr.nhsoul.neobank.network;

import fr.nhsoul.neobank.NeobankMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoBitsClient {
    public static void handleDataOnMain(final NeoBits data, final IPayloadContext context) {
        NeobankMod.NeoBitsAmount = data.amount();
    }
}
