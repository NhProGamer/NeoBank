package fr.nhsoul.neobank.network;

import fr.nhsoul.neobank.Bank;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoBitsServer {
    public static void handleDataOnMain(final NeoBits data, final IPayloadContext context) {
        if (data.amount() <= Bank.getMoney(context.player())) {
            Bank.giveChange(data.amount(), context.player());
        }

    }
}
