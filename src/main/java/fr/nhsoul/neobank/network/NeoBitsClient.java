package fr.nhsoul.neobank.network;

import fr.nhsoul.neobank.NeoBankMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoBitsClient {
    public static void handleDataOnMain(final NeoBits data, final IPayloadContext context) {
        NeoBankMod.NeoBitsAmount = data.amount();
    }
}
