package top.molab.minecraft.moModeratorPlus.dataStorage;


import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.hikari.HikariConfig;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDataManager implements ILocalDataManager {

    public static final String BANSTAT_TABLE_NAME = "MoModPlus.BanStat";
    public static final String PLAYER_NAME = "PlayerName";
    public static final String UUID_PATH = "UUID";
    public static final String IP_PATH = "IP";
    public static final String BAN_ID = "BanID";
    public static final String BAN_TYPE = "BanType";
    public static final String EFFECTIVE_TIME = "EffectiveTime";
    public static final String EXPIRE_TIME = "ExpireTime";
    public static final String REASON = "Reason";
    public static final String OPERATOR = "Operator";
    public static final String OPERATOR_UUID = "OperatorUUID";
    public static final String VERSION_PATH = "Version";
    public static final String CONFIG_TABLE = "MoModPlus.Config";
    private SQLTable banStatTable;
    private SQLTable configTable;
    private SQLManager manager;


    public MySQLDataManager() throws SQLException {
        this.banStatTable = SQLTable.of(BANSTAT_TABLE_NAME, table -> {
                    table.addColumn(PLAYER_NAME, "VARCHAR(32)");
                    table.addColumn(UUID_PATH, "VARCHAR(64)");
                    table.addColumn(IP_PATH, "VARCHAR(32)");

                    table.addColumn(BAN_ID, "VARCHAR(16)");
                    table.setIndex(BAN_ID, IndexType.PRIMARY_KEY);
                    table.addColumn(BAN_TYPE, "VARCHAR(8)");
                    table.addColumn(EFFECTIVE_TIME, "BIGINT");
                    table.addColumn(EXPIRE_TIME, "BIGINT");
                    table.addColumn(REASON, "TEXT");

                    table.addColumn(OPERATOR, "VARCHAR(32)");
                    table.addColumn(OPERATOR_UUID, "VARCHAR(64)");
                }
        );
        this.configTable = SQLTable.of(CONFIG_TABLE, table -> table.addColumn(VERSION_PATH, "VARCHAR(32)"));

        String host = RuntimeDataManager.getInstance().getConfig().getString("global.database.mysql.host");
        int port = RuntimeDataManager.getInstance().getConfig().getInt("global.database.mysql.port");
        String database = RuntimeDataManager.getInstance().getConfig().getString("global.database.mysql.database");
        String username = RuntimeDataManager.getInstance().getConfig().getString("global.database.mysql.username");
        String password = RuntimeDataManager.getInstance().getConfig().getString("global.database.mysql.password");

        HikariConfig config = new HikariConfig();
        MoModeratorPlus.instance.getLogger().info("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true");
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true");
        config.setUsername(username);
        config.setPassword(password);

        this.manager = EasySQL.createManager(config);
        MoModeratorPlus.instance.getLogger().info("MYSQL Database Connected");
        this.banStatTable.create(manager);
        this.configTable.create(manager);

        // 验证版本
        try (SQLQuery query = configTable.createQuery(this.manager)
                .selectColumns(VERSION_PATH)
                .build().execute()) {
            ResultSet resultSet = query.getResultSet();
            if (resultSet.next()) {
                String databaseVersion = resultSet.getString(VERSION_PATH);
                if (!databaseVersion.equals(VERSION)) {
                    MoModeratorPlus.instance.getLogger().warning("Database Version is not " + VERSION + ", Please Check Your Database");
                    // TODO: 如果有版本更新，要在这里加转换工具，但是目前没有版本更新，所以不写
                    throw new RuntimeException("Database Version is not " + VERSION + ", Please Check Your Database");
                }
                MoModeratorPlus.instance.getLogger().info("Database Version is " + databaseVersion);
            } else {
                // 写入当前数据库版本
                MoModeratorPlus.instance.getLogger().info("MYSQL Database Table Creating");
                configTable.createInsert(this.manager)
                        .setColumnNames(VERSION_PATH)
                        .setParams(VERSION)
                        .execute((exception, actions) -> {
                            MoModeratorPlus.instance.getLogger().warning("MYSQL Database Table Created FAILED");
                            MoModeratorPlus.instance.getLogger().warning(actions.getShortID() + "->" + actions.getSQLContent());
                            throw new RuntimeException("MYSQL Database Table Created FAILED:" + actions.getShortID() + "->" + actions.getSQLContent());
                        });
                MoModeratorPlus.instance.getLogger().info("MYSQL Database Table Created Successfully");
            }

        }
        MoModeratorPlus.instance.getLogger().info("Enable MYSql Database Successfully");

    }

    private boolean hasBannedPlayer(String col, String value) {
        try {
            try (SQLQuery query = banStatTable.createQuery()
                    .addCondition(col, "=", value)
                    .build().execute()) {
                ResultSet resultSet = query.getResultSet();
                return resultSet.next();
            }
        } catch (SQLException e) {
            MoModeratorPlus.instance.getLogger().warning("SQL Execute Failed");
            throw new RuntimeException(e);
        }
    }

    private BanStat getBanStat(String col, String value) {
        try {
            try (SQLQuery query = banStatTable.createQuery()
                    .addCondition(col, "=", value)
                    .build().execute()) {
                ResultSet resultSet = query.getResultSet();
                if (resultSet.next()) {
                    return new BanStat(
                            resultSet.getString(BAN_ID),
                            BanTypes.getTypeByString(resultSet.getString(BAN_TYPE)),
                            resultSet.getString(REASON),
                            resultSet.getLong(EFFECTIVE_TIME),
                            resultSet.getLong(EXPIRE_TIME),
                            resultSet.getString(IP_PATH),
                            resultSet.getString(PLAYER_NAME),
                            resultSet.getString(UUID_PATH),
                            resultSet.getString(OPERATOR),
                            resultSet.getString(OPERATOR_UUID)
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            MoModeratorPlus.instance.getLogger().warning("SQL Execute Failed");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasBannedPlayer(String PlayerName) {
        return hasBannedPlayer(PLAYER_NAME, PlayerName);
    }

    @Override
    public boolean hasBanID(String BanID) {
        return hasBannedPlayer(BAN_ID, BanID);
    }


    @Override
    public boolean hasBannedIP(String IP) {
        return hasBannedPlayer(IP_PATH, IP);
    }

    @Override
    public boolean hasBannedUUID(String UUID) {
        return hasBannedPlayer(UUID_PATH, UUID);
    }

    @Override
    public BanStat getBanStatFromPlayerName(String PlayerName) {
        return getBanStat(PLAYER_NAME, PlayerName);
    }

    @Override
    public BanStat getBanStatFromBanID(String BanID) {
        return getBanStat(BAN_ID, BanID);
    }

    @Override
    public BanStat getBanStatFromIP(String IP) {
        return getBanStat(IP_PATH, IP);
    }

    @Override
    public BanStat getBanStatFromUUID(String UUID) {
        return getBanStat(UUID_PATH, UUID);
    }

    @Override
    public void addNewBanStat(BanStat banStat) {
        this.updateBanStat(banStat);
    }

    @Override
    public void updateBanStat(BanStat banStat) {
        try {
            banStatTable.createReplace()
                    .setColumnNames(BAN_ID, REASON, BAN_TYPE, EFFECTIVE_TIME, EXPIRE_TIME, IP_PATH, PLAYER_NAME, UUID_PATH, OPERATOR, OPERATOR_UUID)
                    .setParams(banStat.BanID(), banStat.Reason(), banStat.BanType().name(), banStat.EffectiveTime(), banStat.ExpireTime(), banStat.IP(), banStat.PlayerName(), banStat.UUID(), banStat.Operator(), banStat.OperatorUUID())
                    .execute();
        } catch (SQLException e) {
            MoModeratorPlus.instance.getLogger().warning("SQL Execute Failed");
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteBanStat(BanStat banStat) {
        try {
            if (this.hasBanID(banStat.BanID())) {
                banStatTable.createDelete()
                        .addCondition(BAN_ID, "=", banStat.BanID())
                        .build().execute();
            }
        } catch (SQLException e) {
            MoModeratorPlus.instance.getLogger().warning("SQL Execute Failed");
            throw new RuntimeException(e);
        }
    }

    private List<String> getAllData(String col) {
        ArrayList<String> result = new ArrayList<>();
        try {
            try (SQLQuery query = banStatTable.createQuery().build().execute()) {
                ResultSet resultSet = query.getResultSet();
                while (resultSet.next()) {
                    result.add(resultSet.getString(col));
                }
                return result;
            }

        } catch (SQLException e) {
            MoModeratorPlus.instance.getLogger().warning("SQL Execute Failed");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllUUID() {
        return getAllData(UUID_PATH);
    }

    @Override
    public List<String> getAllPlayerName() {
        return getAllData(PLAYER_NAME);
    }

    @Override
    public List<String> getAllBanID() {
        return getAllData(BAN_ID);
    }
}
