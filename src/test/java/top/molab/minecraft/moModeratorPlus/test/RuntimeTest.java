package top.molab.minecraft.moModeratorPlus.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlayerList;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;

public class RuntimeTest {

    private ServerMock serverMock;

    @BeforeEach
    public void setUp() {
        serverMock = MockBukkit.mock();
        MockBukkit.load(MoModeratorPlus.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    public PlayerMock getOp() {
        PlayerMock player = serverMock.addPlayer();
        player.setOp(true);
        return player;
    }

    @Test
    public void BanCommandTest() {
        PlayerMock op = this.getOp();
        PlayerMock anotherPlayer = serverMock.addPlayer();
        op.performCommand("ban " + anotherPlayer.getName() + " 1d");
        assert !anotherPlayer.reconnect();
    }


    @Test
    public void BanIPCommandTest() {
        PlayerMock op = this.getOp();
        PlayerMock anotherPlayer = serverMock.addPlayer();
        serverMock.setPlayers(20);
        op.performCommand("banip " + anotherPlayer.getName() + " 1d");
        MockPlayerList playerList = serverMock.getPlayerList();
        for (int i = 0; i < 20; i++) {
            assert !playerList.getPlayer(i).reconnect();
        }
    }

    @Test
    public void KickCommandTest() {
        PlayerMock op = this.getOp();
        PlayerMock anotherPlayer = serverMock.addPlayer();
        op.performCommand("kick " + anotherPlayer.getName() + " 1d");
        assert !anotherPlayer.isOnline();
    }

}
